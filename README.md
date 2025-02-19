# JavaFX Messaging App (Client-Server Chat System)

## Overview

The **JavaFX Messaging App** is a **real-time chat application** that allows multiple clients to connect to a central **server** and exchange messages. The application is built with **JavaFX for the graphical user interface** and **socket programming** for network communication.

This project consists of **two main components**:
- **Client Application**: A JavaFX-based GUI that allows users to send and receive messages.
- **Server Application**: Manages connected clients and message distribution.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features

✅ **Real-time Messaging** with multiple users  
✅ **JavaFX-Based Graphical User Interface**  
✅ **Client-Server Architecture** using sockets  
✅ **Message Broadcasting** to all connected clients  
✅ **Secure Connection Handling**  
✅ **Scalable and Extensible Codebase**  

---

## Technology Stack

### 🖥️ Client (JavaFX)
- **Java 11**
- **JavaFX 12** (Controls & FXML for UI)
- **Maven** (for dependency management)
- **JUnit 5** (for testing)

### ⚙️ Server (Java)
- **Java 8+**
- **Socket Programming** (for handling client connections)
- **Multi-threading** (to support multiple clients)
- **Maven**
- **JUnit 5** (for server-side testing)

---

## Installation

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/YourUsername/Messaging-App.git
cd Messaging-App
```

### 2️⃣ Install Client Dependencies
Navigate to the **client** directory and install dependencies:
```bash
cd client
mvn clean install
```

### 3️⃣ Install Server Dependencies
Navigate to the **server** directory and install dependencies:
```bash
cd ../server
mvn clean install
```

---

## Running the Application

### 🖥️ Start the Server
Run the following command inside the `server/` folder:
```bash
mvn exec:java -Dexec.mainClass="GuiServer"
```
This will start the chat server, waiting for client connections.

### 💬 Start the Client
Open a new terminal, navigate to the `client/` folder, and run:
```bash
mvn exec:java -Dexec.mainClass="GuiClient"
```
The **JavaFX-based chat UI** should now launch, allowing users to connect and send messages.

---

## Usage

1. **Start the server first** to allow clients to connect.
2. **Launch multiple client applications** to simulate multiple users.
3. **Clients enter a username and join the chat.**
4. **Messages are broadcasted in real-time** to all connected users.
5. **Clients can disconnect** safely without affecting others.

---

## Project Structure

```
📂 Messaging-App/
│
├── 📂 client/           # JavaFX client (chat UI)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── GuiClient.java      # Main client application
│   │   │   │   ├── controllers/        # JavaFX controllers
│   │   │   │   ├── model/              # Chat logic
│   │   │   ├── resources/              # UI assets (FXML, CSS)
│   │   ├── pom.xml                      # Maven dependencies
│
├── 📂 server/           # Server for managing chat sessions
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── GuiServer.java       # Server main class
│   │   │   │   ├── connections/         # Socket handling
│   │   │   │   ├── handlers/            # Message processing
│   │   ├── pom.xml                       # Maven dependencies
│
├── README.md            # Project documentation
└── .gitignore           # Files to ignore in version control
```

---

## Contributing

We welcome contributions! Follow these steps to contribute:

1. **Fork the Repository**  
   ```bash
   git fork https://github.com/YourUsername/Messaging-App.git
   ```
2. **Create a Feature Branch**  
   ```bash
   git checkout -b feature/my-new-feature
   ```
3. **Commit Your Changes**  
   ```bash
   git commit -m "Add my new feature"
   ```
4. **Push to GitHub**  
   ```bash
   git push origin feature/my-new-feature
   ```
5. **Submit a Pull Request**  
   - Go to GitHub and create a **Pull Request** for review.

---
---

## 🚀 Future Enhancements

✅ **User Authentication** (login & registration)  
✅ **Private Messaging** between clients  
✅ **Chat History Storage** using a database  
✅ **File Sharing & Media Support**  

---

The **JavaFX Messaging App** provides a simple yet powerful **real-time chat system**, allowing multiple users to connect and chat in a **smooth and interactive JavaFX interface**. 📨💻

Happy coding! 🎮💬
