import java.util.*;
import static java.lang.Math.min;

public class QueueBetween {
    private HashMap<Truck, Integer> fast;
    private HashMap<Truck, Integer> slow;
    private HashMap<Integer,Truck> fastValue;
    private HashMap<Integer,Truck> slowValue;
    private boolean[] proceesing;
    private int time;
    private int left;
    private int arrived;
    private HashMap<Integer, Truck> arrivedID;

    public QueueBetween(){
        this.fastValue=new HashMap<>();
        this.fast=new HashMap<>();
        this.slowValue=new HashMap<>();
        this.slow=new HashMap<>();
        this.proceesing = new boolean[3];
        Arrays.fill(proceesing,false);
        this.time=0;
        this.left=0;
        this.arrived=0;
        this.arrivedID=new HashMap<>();
    }

    public synchronized boolean isNotFull(){
        return fast.size() + slow.size() < 10;
    }
    public synchronized void addTime(){
        time++;
    }
    public synchronized int getTime(){
        return time;
    }
    public synchronized void InQueue(){
        arrived--;
    }

    public synchronized void newArrived(Truck truck){
        arrived++;
        arrivedID.put(truck.getTruckId(),truck);
    }

    public synchronized void addInQueue(Truck truck) throws InterruptedException {
        if(isNotFull() && fast.size()<=slow.size()){
            fast.put(truck,fast.size()+1);
            fastValue.put(fast.size(),truck);
            InQueue();
        }
        else if(isNotFull() && fast.size()>slow.size()){
            slow.put(truck, slow.size() + 1);
            slowValue.put(slow.size(), truck);
            InQueue();
        }
    }

    public synchronized void print() throws InterruptedException {
        System.out.println("Fast queue:");
        if(fast.size()==0)
            System.out.println("-------");
        for(int i=1; i<fastValue.size()+1; i++){
            System.out.println("spot:" + i +" truck:"+fastValue.get(i).getTruckId()+" weight:"+fastValue.get(i).getValue());
        }
        System.out.println("\nSlow queue:");
        if(slow.size()==0)
            System.out.println("-------");
        for(int i=1; i<slowValue.size()+1; i++){
            System.out.println("spot:" + i +" truck:"+slowValue.get(i).getTruckId()+" weight:"+slowValue.get(i).getValue());
        }
        System.out.println("\nArrived not in queue:"+ arrived);
        System.out.println("Left:"+left);
        System.out.println("Queue fast is processing:"+proceesing[1]);
        System.out.println("Queue slow is processing:"+proceesing[2]);
    }

    public synchronized int estimatingTime(Integer truckID){
        int estTime=0;
        int estTime2=0;
        Truck estTruck;
        if(arrivedID.containsKey(truckID)) {
            estTruck = arrivedID.get(truckID);
            if (fast.containsKey(estTruck)) {
                int spot = fast.get(estTruck);
                for (int i = 1; i < spot; i++) {
                    estTime += fastValue.get(i).getValue();
                }
                return estTime;
            } else if (slow.containsKey(estTruck)) {
                int spot = slow.get(estTruck);
                for (int i = 1; i < spot; i++) {
                    estTime += slowValue.get(i).getValue();
                }
                return estTime;
            } else {
                for (int i = 1; i < fast.size(); i++) {
                    estTime += fastValue.get(i).getValue();
                }
                for (int i = 1; i < slow.size(); i++) {
                    estTime2 += slowValue.get(i).getValue();
                }
                return min(estTime,estTime2);
            }
        }
        return -1;
    }

    public synchronized int getQueueNo(Truck truck){
        if(fast.containsKey(truck))
            return 1;
        else if(slow.containsKey(truck))
            return 2;
        return 0;
    }

    public synchronized State getState(int truckID) {
        Truck stateTruck;
        if (arrivedID.containsKey(truckID)) {
            stateTruck = arrivedID.get(truckID);
            return stateTruck.getState();
        }
        else return null;
    }

    public synchronized int getQueueSpot(Truck truck){
        if(fast.containsKey(truck)){
            return fast.get(truck);
        }else if (slow.containsKey(truck))
            return slow.get(truck);
        return 0;
    }

    public synchronized boolean startProceesing(int queueNo, Truck truck) throws InterruptedException {
        if(!proceesing[queueNo]){
            proceesing[queueNo]=true;
            if(queueNo==1){
                fastValue.remove(fast.get(truck));
                fast.remove(truck);
                truck.setProcessingQueue(queueNo);
            }else if(queueNo==2){
                slowValue.remove(slow.get(truck));
                slow.remove(truck);
                truck.setProcessingQueue(queueNo);
            }
            return true;
        }
        return false;
    }
    public synchronized void stopProceesing(int queueNo, Truck truck){
        proceesing[queueNo]=false;
        left++;
        arrivedID.remove(truck.getTruckId());
    }

    public synchronized void changeSpot(Truck truck, int queueNo) {
        if (queueNo == 1) {
            Integer spot = fast.get(truck);
            if(spot<=slowValue.size()) {
                Truck slowTruck = slowValue.get(spot);
                if (truck.getValue() > slowTruck.getValue()) {
                    fast.remove(truck);
                    fastValue.remove(spot);
                    slow.remove(slowTruck);
                    slowValue.remove(spot);
                    fast.put(slowTruck, spot);
                    fastValue.put(spot, slowTruck);
                    slow.put(truck, spot);
                    slowValue.put(spot, truck);
                }
            }
        } else if (queueNo == 2) {
            Integer spot = slow.get(truck);
            if(spot<=fastValue.size()) {
                Truck fastTruck = fastValue.get(spot);
                if (truck.getValue() < fastTruck.getValue()) {
                    fast.remove(truck);
                    fastValue.remove(spot);
                    slow.remove(fastTruck);
                    slowValue.remove(spot);
                    fast.put(fastTruck, spot);
                    fastValue.put(spot, fastTruck);
                    slow.put(truck, spot);
                    slowValue.put(spot, truck);
                }
            }
        }
    }
}
