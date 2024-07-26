import java.net.*;
import java.io.*;

public class MultiTCPServer {
    public static void main(String[] args) {
        try {
            int serverPort = 6666; // the server port
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (serverSocket.isBound()) {
                System.out.println("Aguardando conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
                System.out
                        .println("Conexao feita com: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}