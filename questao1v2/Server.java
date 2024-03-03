import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    ServerSocket socketServer;
    Socket client;
    int port;
    ObjectInputStream input;
    ObjectOutputStream output;

    public Server(int port) {
        this.port = port;
        this.client = new Socket();
    }

    public void run() {
        try {
            socketServer = new ServerSocket(port);
            System.out.println("Servidor rodando na porta " + socketServer.getLocalPort() + " e aguardando clientes.");
            boolean connected = true;
            while (connected) {
                client = socketServer.accept();
                System.out.println(client);

                output = new ObjectOutputStream(client.getOutputStream());
                input = new ObjectInputStream(client.getInputStream());

                Message receiveMessage = (Message) input.readObject();

                System.out.println("Messagem recebida: " + receiveMessage);
                forwardMessage(receiveMessage);
                output.flush();
                if (receiveMessage.getMessage().equals("fim")) {
                    client.close();
                    socketServer.close();
                }
                input.close();
                output.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void forwardMessage(Message message) {
        try {
            System.out.println("THREAD");
            String id = "P" + (port - 54000);
            if (id.equals(message.getReceiver())) {
                System.out.println("Mensagem recebida: " + message.getMessage());
            } else {
                // Conecta ao próximo cliente na topologia do anel
                System.out.println("Mensagem nao e para o " + id + " mandando para a proxima. A proxima porta e " + message.getPort());

                Socket socket = new Socket("127.0.0.1", message.getPort());
                System.out.println(socket);
                ObjectOutputStream outputNext = new ObjectOutputStream(socket.getOutputStream());
                outputNext.writeObject(message);
                outputNext.flush();
                outputNext.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server(54001)).start();
        new Thread(new Server(54002)).start();

    }
}
