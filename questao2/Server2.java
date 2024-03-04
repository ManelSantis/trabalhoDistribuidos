public class Server2 {
    public static void main(String[] args) {
        new Thread(new Server(54003)).start();

    }
}
