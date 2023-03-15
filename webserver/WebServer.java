package webserver;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
// import java.text.MessageFormat;
// import java.util.ArrayList;
// import org.json.JSONArray;
// import org.json.JSONObject;

public class WebServer {
    private static final int PORT = 8080;
    private static final String ROOT_DIRECTORY = "webserver/";

    private static class ConnectionThread extends Thread {
        Socket connection;
        ConnectionThread(Socket connection) {
           this.connection = connection;
        }
        public void run() {
           handleConnection(connection);
        }
     }

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Failed to create server socket: " + e.getMessage());
            return;
        }

        System.out.println("Listening on port " + PORT);

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ConnectionThread thread = new ConnectionThread(clientSocket);
                
                try {
                    thread.start();
                } catch (Exception e) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error accepting connection: " + e.getMessage());
            }
        }
        
    }
    private static String getMimeType(String fileName) {
        int pos = fileName.lastIndexOf('.');
        if (pos < 0)  // no file extension in name
            return "x-application/x-unknown";
        String ext = fileName.substring(pos+1).toLowerCase();
        if (ext.equals("txt")) return "text/plain";
        else if (ext.equals("html")) return "text/html";
        else if (ext.equals("htm")) return "text/html";
        else if (ext.equals("css")) return "text/css";
        else if (ext.equals("js")) return "text/javascript";
        else if (ext.equals("java")) return "text/x-java";
        else if (ext.equals("jpeg")) return "image/jpeg";
        else if (ext.equals("jpg")) return "image/jpeg";
        else if (ext.equals("png")) return "image/png";
        else if (ext.equals("gif")) return "image/gif";
        else if (ext.equals("ico")) return "image/x-icon";
        else if (ext.equals("class")) return "application/java-vm";
        else if (ext.equals("jar")) return "application/java-archive";
        else if (ext.equals("zip")) return "application/zip";
        else if (ext.equals("xml")) return "application/xml";
        else if (ext.equals("xhtml")) return"application/xhtml+xml";
        else return "x-application/x-unknown";
           // Note:  x-application/x-unknown  is something made up;
           // it will probably make the browser offer to save the file.
     }
    private static void handleConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {
            String request = in.readLine();
            if (request == null) {
                return;
            }

            String[] tokens = request.split("\\s+");
            String method = tokens[0];
            String path = tokens[1];
            String HTTPversion = tokens[2];

            // HTML Documents
            File home = new File(ROOT_DIRECTORY + "html/index.html");
            long homeSize = home.length();
            File forbiddenFile = new File(ROOT_DIRECTORY + "html/404.html");
            long forbiddenFileSize = forbiddenFile.length();

            // Text Documents
            File text = new File(ROOT_DIRECTORY + "text/text.txt");
            long textSize = text.length();

            // Images Documents
            File image = new File(ROOT_DIRECTORY + "image/image.jpg");
            long imageSize = image.length();
            

            if(path.equals("/") && method.equals("GET")){
                out.write((HTTPversion + " 200 OK" + "\r\n").getBytes());
                out.write(("Content-Type: " + getMimeType(home.getName()) + "\r\n").getBytes());
                out.write(("Content-Length: " + homeSize + "\r\n").getBytes());
                out.write(("Connection: close" + "\r\n").getBytes());
                out.write(("\r\n").getBytes()); // separate headers from body with a blank line
                if(home.exists() && home.isFile()){
                    byte[] fileContent = Files.readAllBytes(home.toPath());
                    out.write(fileContent);
                }
                out.flush();
            }
            if(path.equals("/text") && method.equals("GET")){
                out.write((HTTPversion + " 200 OK" + "\r\n").getBytes());
                out.write(("Content-Type: " + getMimeType(text.getName()) + "\r\n").getBytes());
                out.write(("Content-Length: " + textSize + "\r\n").getBytes());
                out.write(("Connection: close" + "\r\n").getBytes());
                out.write(("\r\n").getBytes()); // separate headers from body with a blank line
                if(text.exists() && text.isFile()){
                    byte[] fileContent = Files.readAllBytes(text.toPath());
                    out.write(fileContent);
                }
                out.flush();
            }
            if(path.equals("/image") && method.equals("GET")){
                out.write((HTTPversion + " 200 OK" + "\r\n").getBytes());
                out.write(("Content-Type: " + getMimeType(image.getName()) + "\r\n").getBytes());
                out.write(("Content-Length: " + imageSize + "\r\n").getBytes());
                out.write(("Connection: close" + "\r\n").getBytes());
                out.write(("\r\n").getBytes()); // separate headers from body with a blank line
                if(image.exists() && image.isFile()){
                    byte[] fileContent = Files.readAllBytes(image.toPath());
                    out.write(fileContent);
                }
                out.flush();
            }else{
                out.write((HTTPversion + " 404 Not Found" + "\r\n").getBytes());
                out.write(("Content-Type: " + getMimeType(forbiddenFile.getName()) + "\r\n").getBytes());
                out.write(("Content-Length: " + forbiddenFileSize + "\r\n").getBytes());
                out.write(("Connection: close" + "\r\n").getBytes());
                out.write(("\r\n").getBytes()); // separate headers from body with a blank line
                if(forbiddenFile.exists() && forbiddenFile.isFile()){
                    byte[] fileContent = Files.readAllBytes(forbiddenFile.toPath());
                    out.write(fileContent);
                }
                out.flush();
            }
           

            

        } catch (IOException e) {
            System.err.println("Error handling request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}

     
