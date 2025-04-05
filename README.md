# 💬 Java Chat and File Transfer System

### 📌 Course Project – IIT (BHU) MNC  
**Contributors:**
- **Aviral Shukla** (Roll No: 24124012)
- **Arnav Raghuvanshi** (Roll No: 24124007)
- **Aryan Raj** (Roll No: 24124009)

🔗 **GitHub Repository:** [Raj-here3/file-chat-app](https://github.com/Raj-here3/file-chat-app)

---

## 📝 Project Overview

A real-time, Java-based desktop application designed to replicate the essential functions of messaging platforms like WhatsApp. The app allows:
- Real-time text communication over a local network
- Seamless file transfers
- A modern GUI built with Java Swing

It demonstrates the power of Java Sockets for client-server communication and utilizes custom-built classes to manage chat and file handling efficiently.

---

## 🚀 Key Features

- 📡 Real-time messaging using TCP sockets  
- 📁 File transfer support between users  
- 🎨 Modern GUI using Java Swing & JTextPane  
- 🧵 Multi-threaded server to handle concurrent clients  
- 🎨 User-specific color-coded messages  
- 🔔 Feedback messages for sent/received files and chat status  
- 🧭 Clean and user-friendly interface  

---

## 🧰 Tech Stack

- **Language:** Java  
- **GUI:** Java Swing  
- **Networking:** Java Sockets, I/O Streams  
- **Libraries/Extensions Used:**
  - `javax.swing.*`
  - `java.io.*`
  - `java.net.*`
  - `javax.swing.text.*`

---

## 🏗️ Architecture

**Client-Server Model:**

- **Server**
  - Uses `ServerSocket` to listen for connections
  - Creates a new `ClientHandler` thread per client
  - Broadcasts messages and files to all clients

- **Client**
  - Connects via `Socket`
  - Sends and receives messages/files
  - Displays content using GUI interface

---

## 📸 Screenshots / Illustrations

- Chat interface with user-colored messages  
- File transfer confirmations  
- Server log output on the terminal  
- Real-time communication among multiple clients  

> *(Screenshots can be added in the repository's `/screenshots` folder and linked here)*

---

## 👨‍💻 Individual Contributions

### 🔹 Aviral Shukla – Server & Client Handler
- Developed multi-threaded server
- Managed connections using `ClientHandler`
- Broadcast messages & file streams
- Handled I/O stream logic  
**Skills:** Java Networking, Multithreading, I/O Streams

### 🔹 Arnav Raghuvanshi – Chat GUI
- Designed GUI with Java Swing
- Used `JTextPane` & `StyledDocument` for formatting
- Added interactive chat/file buttons
- Managed thread-safe GUI updates  
**Skills:** Java Swing, Event Handling, UI/UX Design

### 🔹 Aryan Raj – File Transfer System
- Implemented file sending/receiving using `JFileChooser`
- Sent file data via sockets using threads
- Ensured large file support & proper buffering  
**Skills:** Java I/O, File Handling, Threads

---

## 🧪 How to Run

1. **Start the server**  
   Run `Server.java`
2. **Launch clients**  
   Run `ChatGUI.java` on multiple instances with different usernames
3. **Start chatting**  
   Use the chatbox and click the file button to send files
4. **File handling**  
   Receiver gets a confirmation, and the file is saved in the current directory

---

## ✅ Conclusion

This project blends GUI development, multithreaded socket programming, and file handling to simulate real-world messaging applications. It demonstrates strong fundamentals in OOP, networking, and responsive UI design in Java.
