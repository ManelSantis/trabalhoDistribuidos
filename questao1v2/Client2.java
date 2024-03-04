public class Client2 {
    public static void main(String args[]) {
        Client c3 = new Client(3, 54004, "127.0.0.1");
        Thread proccess3 = new Thread(c3);
        proccess3.start();
    }
}
