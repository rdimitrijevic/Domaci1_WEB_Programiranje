import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Asistent implements Runnable {

    private Semaphore finishedSem;
    private ReentrantLock isReadyLock;
    private Semaphore beginSem;

    private short busy = 0;

    private Student student = null;
    private final String ime = "Asistent";

    @Override
    public void run() {

        while( true ) {
            try {
                beginSem.acquire();

                for(int i = 0; i < student.getTrajanjeOdbrane(); i++);
                Random rand = new Random();

                int ocena = rand.nextInt(11);
                student.setOcena(ocena);
                student.setImeIspitivaca(ime);

                Main.gradeSum += ocena;
                Main.numberOfStudents++;

                busy = 0;

                finishedSem.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Asistent() {
        finishedSem = new Semaphore(0);
        beginSem = new Semaphore(0);
        isReadyLock = new ReentrantLock();
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
        busy = 1;
        this.student = student;
    }

    public short getBusy() {
        return busy;
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
