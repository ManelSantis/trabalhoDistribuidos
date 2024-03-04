public class Server1 {
    public static void main(String[] args) {
        new Thread(new Server(54002)).start();

    }
}
