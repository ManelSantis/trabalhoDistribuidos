import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ImplServer implements Runnable {
    public Socket socketClient;
    ObjectInputStream input;
    ObjectOutputStream output;
    private boolean connected = true;
    ArrayList<Message> messages;
    int port;

    public ImplServer(Socket client, int port) {
        socketClient = client;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socketClient.getOutputStream());
            input = new ObjectInputStream(socketClient.getInputStream());
            messages = new ArrayList<>();
            while (connected) {
                Message receiveMessage = (Message) input.readObject();
                if (receiveMessage.getMessage().equals("fim")) {
                    connected = false;
                } else {
                    output.flush();
                    forwardMessage(receiveMessage);
                }
            }
            input.close();
            output.close();
            socketClient.close();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void forwardMessage(Message message) {

        String id = "P" + (port - 54000);
        try {
            if (id.equals(message.getReceiver())) {
                System.out.println("Mensagem recebida.");
                messages.add(message);
                System.out.println("------- LOG de Mensagens de " + id + " ------");

                for (int i = 0; i < messages.size(); i++) {
                    System.out.println(
                            "[" + messages.get(i).getSender() + " -> " + messages.get(i).getReceiver() + "]: " +
                                    messages.get(i).getMessage());
                }
                System.out.println("-------------------------------------");
            } else if ((message.getReceiver().equals("ALL") && (id.equals("P1")))) {
                System.out.println("Mensagem recebida pelo processo central. Mandando para todos os processos ativos em Broadcast.");
                // Pegar o valor da porta do destinatario.
                for (int i = 1; i <=3; i++) {
                    Socket socket = new Socket("127.0.0.1", 54000 + i);
                    output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(message);
                    output.flush();
                }
            } else {
                if (id.equals("P1")) {
                    System.out.println("Mensagem recebida pelo processo central. Mandando para o destinatario: "
                            + message.getReceiver());
                    // Pegar o valor da porta do destinatario.
                    int aux = Integer.parseInt(message.getReceiver().replace("P", ""));
                    Socket socket = new Socket("127.0.0.1", 54000 + aux);
                    output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(message);
                    output.flush();
                } else {
                    System.out.println("Enviando mensagem para o processo central.");
                    Socket socket = new Socket("127.0.0.1", message.getPort());
                    output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(message);
                    output.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
