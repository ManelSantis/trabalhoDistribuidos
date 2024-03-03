public class Client1 {
    public static void main(String args[]) {
        new Thread(new Server(54002)).start();

       Client c2 = new Client(2, 54003, "localhost");
       c2.run();
    }
}
