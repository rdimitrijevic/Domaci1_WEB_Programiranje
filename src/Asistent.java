import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Asistent implements Runnable {

    private Semaphore finishedSem;
    private ReentrantLock isReadyLock;
    private Semaphore beginSem;

    private AvgQueue queue;
    private Student student = null;
    private final String ime = "Asistent";

    private boolean programEnded = false;

    @Override
    public void run() {
        Main.start.countDown();
/*
        synchronized (queue){
            try {
                queue.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/

        while (true) {
            if(programEnded) return;

            try {
                beginSem.acquire();
                synchronized (this) {
                    wait(student.getTrajanjeOdbrane(), 0);
                    Random rand = new Random();

                    int ocena = rand.nextInt(11);
                    student.setOcena(ocena);
                    student.setImeIspitivaca(ime);

                    queue.queuePut(student);
                    notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Asistent(AvgQueue queue) {
        finishedSem = new Semaphore(0);
        beginSem = new Semaphore(0);
        isReadyLock = new ReentrantLock();
        this.queue = queue;
    }

    public Semaphore getBeginSemaphore() {
        return beginSem;
    }

    public Semaphore getFinishedLock() {
        return finishedSem;
    }

    public ReentrantLock getIsReadyLock() {
        return isReadyLock;
    }

    public synchronized void setStudent(Student student) {
        this.student = student;
    }

    public void end(){
        programEnded = true;
    }

}
