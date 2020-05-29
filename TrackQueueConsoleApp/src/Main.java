public class Main {
    private static QueueBetween queue = new QueueBetween();
    private static int maxThreads=50;

    public static void main(String[] args) throws InterruptedException {
        Thread cmd = new Thread(new Cmd(maxThreads,queue));
        cmd.start();
        cmd.join();
    }
}
