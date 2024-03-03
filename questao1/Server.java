import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    ServerSocket socketServer;
    Socket client;
    int port;
    private int nextPort;

    public int getNextPort() {
        return nextPort;
    }

    public void setNextPort(int nextPort) {
        this.nextPort = nextPort;
    }

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
                System.out.println("Cliente adicionado na porta " + client.getPort());
                // Cria uma thread do servidor para tratar a conexão
                ImplServer server = new ImplServer(client);
                Thread t = new Thread(server);
                // Inicia a thread para o cliente conectado
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server(54001)).start();
        new Thread(new Server(54002)).start();
        new Thread(new Server(54003)).start();
        new Thread(new Server(54004)).start();
    }
}
