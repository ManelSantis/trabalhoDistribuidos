import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    String sender;
    String receiver;
    String message;
    boolean brodcast = false;
    int port;
    int cont = 0;

    public Message (String sender, String receiver, String message, int port) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.port = port;
    }

    public void nextPort() {
        if (port == 54004) {
            port = 54001;
        } else {
            port++;
        }
        cont++;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isBrodcast() {
        return brodcast;
    }

    public void setBrodcast(boolean brodcast) {
        this.brodcast = brodcast;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

}
