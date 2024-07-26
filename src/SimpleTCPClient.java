import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        // Cria socket de comunicacao com o servidor e obtem canais de entrada e saida
        System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        // Espera mensagem ser digitada da entrada padrão (teclado)
        System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

        new Thread(new Runnable(){
            @Override
            public void run() {
                String response;
                while(socket.isConnected()) {
                    try {
                        response = input.readUTF();
                        System.out.println(response);
                    } catch (IOException e) {
                        closeClientSocket(socket, input, output);
                    }
                }
            }
        }).start();

        System.out.print("Digite uma mensagem: ");
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n");
        while(socket.isConnected()){
            String msg = scanner.nextLine();

            // Envia mensagem para o servidor no canal de saida
            output.writeUTF(msg);
        }

    }

    public void closeClientSocket(Socket clientSocket, DataInputStream input, DataOutputStream output){
        try{
            clientSocket.close();
            input.close();
            output.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String serverIp = "0.0.0.0";
        int serverPort = 6666;
        try {
            // Cria e roda cliente
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
