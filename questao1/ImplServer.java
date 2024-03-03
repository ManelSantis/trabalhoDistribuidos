import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ImplServer implements Runnable {
    public Socket socketClient;
    private boolean connected = true;
    private Scanner sc = null;

    public ImplServer(Socket client) {
        socketClient = client;
    }

    public void run() {
        String message;
        try {
            sc = new Scanner(socketClient.getInputStream());

            while (connected) {
                if (sc.hasNextLine()) {
                    message = sc.nextLine();
                    if (message.equalsIgnoreCase("fim")) {
                        connected = false;
                    } else {
                        System.out.println(message);
                        String[] parts = message.split(":");
                        forwardMessage(parts[1], parts[2], Integer.parseInt(parts[3].replace(" ", "")));
                    }
                }
            }

            sc.close();
            System.out.println("Fim do cliente " + socketClient.getInetAddress().getHostAddress());
            socketClient.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void forwardMessage(String destination, String content, int nextPort) {
        try {
            // Verificar se a mensagem é para o atual cliente na topologia
            int id = 0;
            if (nextPort == 5001) {
                id = 4;
            } else {
                id = nextPort - 5001;
            }

            if (destination.equals("P" + id)) {
                System.out.println("Mensagem recebida: " + content);
            } else {
                // Conecta ao próximo cliente na topologia do anel
                System.out.println(
                        "Mensagem nao e para mim P" + id + " mandando para a proxima. A proxima porta e " + nextPort);

                Socket socket = new Socket("localhost", nextPort);
                PrintStream ps = new PrintStream(socket.getOutputStream());
                // Envia a mensagem para o próximo cliente
                ps.println("Para: " + destination + ":" + content + ":" + nextPort);
                ps.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
