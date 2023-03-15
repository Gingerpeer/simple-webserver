# Simple Web Server
A minimalistic Java-based web server that serves static content such as HTML, CSS, JavaScript, images, and text files.

# Getting Started
These instructions will help you set up the project on your local machine for development and testing purposes.

# Prerequisites
You need to have Java Development Kit (JDK) installed on your machine. You can download the latest JDK from Oracle's official website.

# Installation
Clone the repository:
git clone https://github.com/yourusername/simple-web-server.git
Navigate to the project folder:
cd simple-web-server
Compile the source code:
webserver/WebServer.java
Run the server:
webserver.WebServer
The server will start listening on port 8080.

# Usage
The server supports the following routes:

Home page: http://localhost:8080/
Text file: http://localhost:8080/text
Image file: http://localhost:8080/image
If a requested route is not found, a 404 Not Found error page will be displayed.

# Customization
You can modify the server settings by changing the following constants in the WebServer.java file:

private static final int PORT = 8080;
private static final String ROOT_DIRECTORY = "webserver/";
PORT: The port number on which the server listens for incoming connections.
ROOT_DIRECTORY: The root directory where the server's content is stored.
Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

# License
This project is licensed under the MIT License - see the LICENSE file for details.
