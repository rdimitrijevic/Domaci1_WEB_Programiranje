import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Asistent implements Runnable {

    private Semaphore finishedLock;
    private ReentrantLock isReadyLock;
    private Semaphore beginSemaphore;

    private Student student = null;

    @Override
    public void run() {

        while( true ) {
            try {
                beginSemaphore.acquire();

                wait(student.getTrajanjeOdbrane());
                Random rand = new Random();
                student.setOcena(rand.nextInt(11));

                finishedLock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Asistent() {
        finishedLock = new Semaphore(0);
        beginSemaphore = new Semaphore(0);
        isReadyLock = new ReentrantLock();
    }

    public Semaphore getBeginSemaphore() {
        return beginSemaphore;
    }

    public Semaphore getFinishedLock() {
        return finishedLock;
    }

    public ReentrantLock getIsReadyLock() {
        return isReadyLock;
    }

    public synchronized void setStudent(Student student) {
        this.student = student;
    }

//    public synchronized int oceniMe(Student student) {
//        try {
//            wait(student.getTrajanjeOdbrane());
//            Random random = new Random();
//            return random.nextInt(11);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
