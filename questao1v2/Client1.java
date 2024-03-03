public class Client1 {
    public static void main(String args[]) {
        Client c2 = new Client(2, 54003, "127.0.0.1");
        Thread proccess2 = new Thread(c2);
        proccess2.start();
    }
}
