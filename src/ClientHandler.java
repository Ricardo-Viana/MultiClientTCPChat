import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ClientHandler extends Thread {

    static List<ClientHandler> clientHandlerList = new ArrayList<>();

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientHandler(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            clientHandlerList.add(this);
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
            closeClientSocket(clientSocket, in, out);
        }
    }

    public void run() {
        while(clientSocket.isConnected()) {
            try { // an echo server
                String data = in.readUTF(); // read a line of data from the stream
                System.out.println("Mensagem recebida: " + data);
                multipleClientsMessage(data);
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                closeClientSocket(clientSocket, in, out);
                break;
            }
        }
    }

    public void multipleClientsMessage(String data) throws IOException {
        clientHandlerList.forEach(clientHandler -> {if (!clientHandler.clientSocket.equals((this.clientSocket))){
            try {
                DataOutputStream out = clientHandler.getOut();
                out.writeUTF(data.toUpperCase());
            } catch (IOException e) {
                closeClientSocket(clientSocket, in, out);
            }
        }
        });
    }

    public DataOutputStream getOut() {
        return this.out;
    }

    public void closeClientSocket(Socket clientSocket, DataInputStream in, DataOutputStream out) {
        clientHandlerList.remove(this);
        try{
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
            if (clientSocket != null){
                clientSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}