import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
    Socket socket;
    String ip;
    int port;
    int nextPort;

    public Client(int port, int nextPort, String ip) {
        this.port = port;
        this.nextPort = nextPort;
        this.ip = ip;
    }

    public void run() {
        try {
            socket = new Socket(ip, 54000 + port);
            ImplClient c = new ImplClient(socket, nextPort, port);
            Thread t = new Thread(c);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
