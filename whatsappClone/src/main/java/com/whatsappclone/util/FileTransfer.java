package com.whatsappclone.util;

import java.io.*;
import java.net.*;

public class FileTransfer {

    public static void sendFile(String filePath, Socket socket) throws IOException {
        File file = new File(filePath);
        byte[] buffer = new byte[4096];
        
        // Open file stream (we close this when done)
        FileInputStream fis = new FileInputStream(file);
        // Get the socket's output stream (do NOT close this stream)
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        // Send file metadata: name and size
        dos.writeUTF(file.getName());
        dos.writeLong(file.length());
        
        // Send file content
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, bytesRead);
        }
        dos.flush(); // Ensure all file data is sent
        
        // Wait for acknowledgment using readUTF
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String response = dis.readUTF();
        if ("RECEIVED".equals(response)) {
            System.out.println("File sent successfully: " + file.getName());
        } else {
            System.err.println("Error: Improper acknowledgment received: " + response);
        }
        
        // Close file stream only; do not close socket streams!
        fis.close();
    }

    public static void receiveFile(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Receive metadata
        String fileName = dis.readUTF();
        long fileSize = dis.readLong();

        File file = new File(System.getProperty("user.dir"), fileName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4096];

        long totalBytesRead = 0;
        int bytesRead;

        while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesRead))) != -1) {
            fos.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }

        fos.flush();
        fos.close();
        
        // Ensure all file data is received before sending acknowledgment
        if (totalBytesRead == fileSize) {
            dos.writeUTF("RECEIVED");
            dos.flush();
        } else {
            System.err.println("File transfer incomplete.");
        }
    }

}