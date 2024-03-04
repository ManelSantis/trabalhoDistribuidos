public class Client3 {
    public static void main(String args[]) {
        Client c4 = new Client(4, 54001, "127.0.0.1");
        Thread proccess4 = new Thread(c4);
        proccess4.start();
    }
}
