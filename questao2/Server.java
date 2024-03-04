import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    ServerSocket socketServer;
    Socket client;
    int port;
    int cont = 0;

    public Server(int port) {
        this.port = port;
        this.client = new Socket();
    }

    public void run() {
        try {
            socketServer = new ServerSocket(port);
            System.out.println("Servidor rodando na porta " + socketServer.getLocalPort() + " e aguardando clientes.");
            
            while (true) {
                client = socketServer.accept();
                System.out.println("Client conectado: " + client.getPort());
                ImplServer server = new ImplServer(client, port);
                Thread t = new Thread(server);
                t.start();
                cont++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
