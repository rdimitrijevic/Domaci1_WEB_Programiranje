import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Profesor implements Runnable {

    private CyclicBarrier barrier;
    private static Profesor instance = null;

    private Student student1;
    private Student student2;

    private Profesor() {
        barrier = new CyclicBarrier(3);
        student1 = null;
        student2 = null;
    }


    @Override
    public void run() {
        try {
            barrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread pThread = new Thread(new Profesor());
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public static Profesor getInstance(){
        return instance == null ? instance = new Profesor(): instance;
    }
}
