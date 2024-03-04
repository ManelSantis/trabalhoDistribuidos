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
        try {
            String id = "P" + (port - 54000);
            if (id.equals(message.getReceiver())) {
                System.out.println("Mensagem recebida.");
            } else if (message.getReceiver().equals("ALL")) {
                if ((id.equals(message.getSender())) && (message.getCont() < 3)) {
                    Socket socket = new Socket("127.0.0.1", message.getPort());
                    message.nextPort();
                    output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(message);
                    output.flush();
                } else {
                    if (id.equals(message.getSender())) {
                        System.out.println("Loop completo. Mensagem voltou ao cliente que enviou.");
                    } else {
                        System.out.println("Mensagem recebida. E enviando para o proximo cliente.");
                        Socket socket = new Socket("127.0.0.1", message.getPort());
                        message.nextPort();
                        output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(message);
                        output.flush();
                    }
                }
            } else {
                // Conecta ao próximo cliente na topologia do anel
                if (!id.equals(message.getSender())) {
                    System.out.println("Mensagem nao e para o " + id + " mandando para a proxima. A proxima porta e "
                            + message.getPort());
                }
                Socket socket = new Socket("127.0.0.1", message.getPort());
                message.nextPort();
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(message);
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        messages.add(message);
        System.out.println("------- LOG de Mensagens -------");
        for (int i = 0; i < messages.size(); i++) {
            System.out.println(
                    "[" + messages.get(i).getSender() + " -> " + messages.get(i).getReceiver() + "]: " +
                            messages.get(i).getMessage());
        }
        System.out.println("--------------------------------");
    }

}
