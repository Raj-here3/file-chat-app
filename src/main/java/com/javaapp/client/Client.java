package com.javaapp.client;

import java.io.*;
import java.net.*;

import com.javaapp.gui.ChatGUI;
import com.javaapp.util.FileTransfer;

public class Client {
    private static final String SERVER_ADDRESS = "192.168.4.231"; // Replace with your server's IP address
    private static final int SERVER_PORT = 8888;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private ChatGUI chatGUI; // Reference to ChatGUI for displaying messages

    public Client(String username, ChatGUI chatGUI) {
        this.username = username;
        this.chatGUI = chatGUI;
    }

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send username to the server
            out.println(username);

            // Start a thread to listen for incoming messages
            new Thread(new MessageReceiver()).start();
        } catch (IOException e) {
            e.printStackTrace();
            chatGUI.displayMessage("Error connecting to the server: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (socket != null && !socket.isClosed()) {
            out.println(message);
        } else {
            chatGUI.displayMessage("Error: Not connected to the server.");
        }
    }

    public void sendFile(String filePath) {
        try {
            if (socket != null && !socket.isClosed()) {
                out.println("FILE_TRANSFER");
                FileTransfer.sendFile(filePath, socket);
                chatGUI.displayMessage("You sent a file: " + filePath);
            } else {
                chatGUI.displayMessage("Error: Not connected to the server.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            chatGUI.displayMessage("Error sending file: " + e.getMessage());
        }
    }
    

    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    // Display received messages in the ChatGUI
                    chatGUI.displayMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                chatGUI.displayMessage("Connection lost: " + e.getMessage());
            }
        }
    }
}
