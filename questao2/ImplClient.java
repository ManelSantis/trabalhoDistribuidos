import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ImplClient implements Runnable {
    private Socket client;
    private boolean connected = true;
    private int port;
    private int nextPort;
    private Scanner sc = new Scanner(System.in);

    ObjectInputStream input;
    ObjectOutputStream output;

    public ImplClient(Socket c, int nextPort, int port) {
        this.client = c;
        this.nextPort = nextPort;
        this.port = port;
    }

    public void run() {

        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            while (connected) {
                String message;
                int destination = 0;
                int tipo;

                System.out.println("Digite a mensagem: ");
                message = sc.nextLine();

                System.out.println("Tipo da mensagem: " +
                        "\n 1. Broadcast " +
                        "\n 2. Unicast ");
                tipo = sc.nextInt();
                sc.nextLine();

                if (tipo == 2) {
                    System.out.println("Escolha o destino:" +
                            "\n 1. P1 " +
                            "\n 2. P2 " +
                            "\n 3. P3 " +
                            "\n 4. P4 ");
                    destination = sc.nextInt();
                    sc.nextLine();
                }

                if (message.equalsIgnoreCase("fim")) {
                    connected = false;
                } else {
                    Message sendMessage;
                    if (tipo == 2) {
                        sendMessage = new Message("P" + port, "P" + destination, message, nextPort);
                    } else {
                        sendMessage = new Message("P" + port, "ALL", message, nextPort);
                    }
                    output.writeObject(sendMessage);
                    output.flush();
                }
            }
            input.close();
            output.close();
            client.close();
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
