import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    String sender;
    String receiver;
    String message;
    int port;

    public Message (String sender, String receiver, String message, int port) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.port = port;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
