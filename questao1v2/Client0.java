public class Client0 {
    public static void main(String args[]) {

        Client c1 = new Client(1, 54002, "127.0.0.1");
        Thread proccess1 =  new Thread(c1);
        proccess1.start();
    }
}
