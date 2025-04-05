package com.whatsappclone.gui;

import com.whatsappclone.client.Client;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ChatGUI extends JFrame {
    private Client client;
    private JTextPane chatPane;
    private StyledDocument doc;
    private StyleContext styleContext;
    private JTextField messageField;
    private JButton sendButton;
    private JButton fileButton;
    
    private void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            new Thread(() -> client.sendFile(selectedFile.getAbsolutePath())).start();
        }
    }
    
    public ChatGUI(String username) {
        // 1. Initialize style context first
        styleContext = new StyleContext();
        
        // 2. Set up UI components
        initializeUI(username);  // Creates chatPane
        
        // 3. Initialize document AFTER UI components exist
        doc = chatPane.getStyledDocument();
        setupStyles();

        // 4. Initialize client LAST
        client = new Client(username, this);
        client.start();

        // 5. Make visible ONLY AFTER full initialization
        setVisible(true);
    }

    private void initializeUI(String username) {
        setTitle("WhatsApp Clone - " + username);
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize chatPane FIRST
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatPane);

        // Initialize other components
        messageField = new JTextField();
        sendButton = new JButton("Send");
        fileButton = new JButton("File");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(fileButton, BorderLayout.WEST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Event handlers with safety checks
        ActionListener safeSend = e -> {
            if (doc != null) sendMessage();
        };
        sendButton.addActionListener(safeSend);
        messageField.addActionListener(safeSend);
        fileButton.addActionListener(e -> sendFile());
    }

    private void setupStyles() {
        Style defaultStyle = styleContext.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, "SansSerif");
        StyleConstants.setFontSize(defaultStyle, 14);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && doc != null) {
            try {
                // Immediately display your own message using default style
                doc.insertString(doc.getLength(), "You: " + message + "\n", doc.getStyle("default"));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            client.sendMessage(message);
            messageField.setText("");
        }
    }
    
    public void displayMessage(String formattedMessage) {
        SwingUtilities.invokeLater(() -> {
            try {
                String[] parts = formattedMessage.split("\\|", 2);
                if (parts.length < 2) {
                    doc.insertString(doc.getLength(), formattedMessage + "\n", doc.getStyle("default"));
                    return;
                }
                String colorHex = parts[0];
                String message = parts[1];
                Style userStyle = styleContext.addStyle("user_" + colorHex, null);
                StyleConstants.setForeground(userStyle, Color.decode(colorHex));
                StyleConstants.setBold(userStyle, true);
                String[] messageParts = message.split(": ", 2);
                String usernamePart = messageParts[0] + ": ";
                String contentPart = messageParts.length > 1 ? messageParts[1] : "";
                doc.insertString(doc.getLength(), usernamePart, userStyle);
                doc.insertString(doc.getLength(), contentPart + "\n", doc.getStyle("default"));
            } catch (BadLocationException | NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username != null && !username.trim().isEmpty()) {
                new ChatGUI(username).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
