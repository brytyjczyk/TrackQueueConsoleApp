import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd implements Runnable {
    private int i = 0;
    private Thread[] trucks;
    private int trucksNo;
    private int maxThreads;
    private QueueBetween queueBetween;


    public Cmd(int maxThreads, QueueBetween queueBetween) {
        this.trucks = new Thread[maxThreads];
        this.trucksNo = 0;
        this.maxThreads = maxThreads;
        this.queueBetween = queueBetween;
    }


    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String newline = "";
        while (true) {
            try {
                newline = in.readLine();
                String[] args = newline.split(" ");
                if (args[0].equals("arrive")) {
                    trucks[i] = new Thread(new Truck(trucksNo, Integer.parseInt(args[1]), queueBetween));
                    trucks[i].start();
                    i++;
                    trucksNo++;
                } else if (args[0].equals("addTime"))
                    queueBetween.addTime();
                else if (args[0].equals("print"))
                    queueBetween.print();
                else if (args[0].equals("truck")) {
                    State state = queueBetween.getState(Integer.parseInt(args[1]));
                    if(state == null)
                        System.out.println("There was no such truck or it had already left");
                    else
                        System.out.println(state);
                }
                else if (args[0].equals("estimatedTime")) {
                    int time = queueBetween.estimatingTime(Integer.parseInt(args[1]));
                    if(time==-1)
                        System.out.println("There is no such truck");
                    else
                        System.out.println(time);
                }
                else if (args[0].equals("exit"))
                    System.exit(0);
                else
                    System.out.println("SYNTAX: arrive [truck weight (int)] ; addTime ; print ; truck [truck id]; estimatedTime [truck id]; exit");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
