import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ImplClient implements Runnable {
    private Socket client;
    private boolean connected = true;
    private PrintStream ps;
    private int port;
    private int nextPort;
    private String ip;
    private Scanner sc = new Scanner(System.in);
    
    public ImplClient(Socket c, int nextPort, int port, String ip) {
        this.client = c;
        this.nextPort = nextPort;
        this.port = port;
        this.ip = ip;
    }

    public void run() {

        Thread senderThread = new Thread(this::sendMessage);
        //Thread receiverThread = new Thread(this::receiveMessage);
        senderThread.start();
        //receiverThread.start();

        try {
            senderThread.join();
           // receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ps.close();
            client.close();
            sc.close();
            System.out.println("Cliente finaliza conexão.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage() {
        try {
            ps = new PrintStream(client.getOutputStream());
            sc = new Scanner(System.in);
            String message;
            int destination;

            while (connected) {
                System.out.println("Digite a mensagem: ");
                message = sc.nextLine();
                System.out.println("Digite o destino:" +
                        "\n 1. P1 " +
                        "\n 2. P2 " +
                        "\n 3. P3 " +
                        "\n 4. P4 ");
                destination = sc.nextInt();
                sc.nextLine();
                if (message.equalsIgnoreCase("fim")) {
                    connected = false;
                } else {
                    ps.println("Para:P" + destination + ":" + message + ":" + nextPort);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try {

            while (connected) {
                String message = sc.nextLine();
                if (message.equalsIgnoreCase("fim")) {
                    connected = false;
                    break;
                }

                if (message.split(":").length >=3) {

                // Verifica se a mensagem é para este cliente
                String[] parts = message.split(":");
                if (parts[1].equals("P" + port)) {
                    ps.println("Mensagem recebida: " + parts[2]);
                } else {

                    System.out.println(
                            "Mensagem nao e para mim P" + port + " mandando para a proxima. A proxima porta e "
                                    + nextPort);

                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("127.0.0.1", nextPort));
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    // Envia a mensagem para o próximo cliente
                    ps.println("Para: " + parts[1] + ":" + parts[2] + ":" + parts[3]);
                    ps.close();
                    socket.close();
                }

            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
