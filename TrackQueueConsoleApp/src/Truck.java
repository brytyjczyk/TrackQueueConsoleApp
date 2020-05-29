public class Truck implements Runnable{
    private int truckId;
    private int value;
    private int startTime;
    private State state;
    private QueueBetween queue;
    private int processingQueue;

    public Truck(int truckId, int value, QueueBetween queue){
        this.truckId=truckId;
        this.value=value;
        this.startTime=0;
        this.state=State.ARRIVE;
        this.queue=queue;
        queue.newArrived(this);
    }

    public void setProcessingQueue(int no){
        processingQueue=no;
    }
    public int getTruckId() {
        return truckId;
    }
    public int getValue() {
        return value;
    }
    public State getState(){
        return state;
    }

    public void run() {
        while (!state.equals(State.LEFT)) {
            if (state.equals(State.ARRIVE)) {
                if (queue.isNotFull()) {
                    state = State.INQUEUE;
                    try {
                        queue.addInQueue(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    state = State.FORQUEUE;
            } else if (state.equals(State.FORQUEUE)) {
                if (queue.isNotFull()) {
                    state = State.INQUEUE;
                    try {
                        queue.addInQueue(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (state.equals(State.INQUEUE)) {
                if (queue.getQueueSpot(this) >= 2) {
                    queue.changeSpot(this, queue.getQueueNo(this));
                }

                else {
                    try {
                        if (queue.getQueueSpot(this) == 1 && queue.startProceesing(queue.getQueueNo(this), this)) {
                                state = State.PROCEESSING;
                                startTime = queue.getTime();
                            }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (state.equals(State.PROCEESSING)) {
                if (queue.getTime() == startTime + value) {
                    queue.stopProceesing(processingQueue, this);
                    state = State.LEFT;
                }
            }
        }
    }
}
