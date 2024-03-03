import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    Socket socket;
    String ip;
    int port;
    int nextPort;
    ObjectInputStream input;
    ObjectOutputStream output;

    public Client(int port, int nextPort, String ip) {
        this.port = port;
        this.nextPort = nextPort;
        this.ip = ip;
    }

    public void run() {
        try {
            socket = new Socket(ip, 54000 + port);
            boolean connected = true;
            Scanner sc = new Scanner(System.in);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            while (connected) {
                System.out.println("Digite a mensagem: ");
                String message = sc.nextLine();
                System.out.println("Digite o destino:" +
                        "\n 1. P1 " +
                        "\n 2. P2 " +
                        "\n 3. P3 " +
                        "\n 4. P4 ");
                int destination = sc.nextInt();
                sc.nextLine();
                if (message.equalsIgnoreCase("fim")) {
                    connected = false;
                } else {
                    Message sendMessage = new Message("P" + port, "P" + destination, message, nextPort);
                    output.writeObject(sendMessage);
                    
                }
            }

            output.flush();
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
