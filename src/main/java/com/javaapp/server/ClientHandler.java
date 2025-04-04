package com.javaapp.server;

import java.io.*;
import java.net.*;

import com.javaapp.util.FileTransfer;

import java.awt.Color;

class ClientHandler implements Runnable {
    private static final Color[] USER_COLORS = {
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.MAGENTA,
        Color.ORANGE
    };
    private static int colorIndex = 0;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String colorHex;

    private void handleFileTransfer() throws IOException {
        FileTransfer.receiveFile(socket);
        Server.broadcastMessage(colorHex + "|" + username + " sent a file.", this);
    }
    

    public ClientHandler(Socket socket) {
        this.socket = socket;
        synchronized (USER_COLORS) {
            this.colorHex = String.format("#%06X", USER_COLORS[colorIndex % USER_COLORS.length].getRGB() & 0xFFFFFF);
            colorIndex++;
        }
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            username = in.readLine();
            Server.broadcastMessage(colorHex + "|" + username + " has joined the chat!", this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("FILE_TRANSFER")) {
                    handleFileTransfer();
                } else {
                    Server.broadcastMessage(colorHex + "|" + username + ": " + message, this);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in ClientHandler: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
            Server.removeClient(this);
            Server.broadcastMessage(colorHex + "|" + username + " has left the chat!", this);
        }
    }
    
    void sendMessage(String message) {
        out.println(message);
    }
}
