import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Profesor implements Runnable {

    private CyclicBarrier barrier;
    private Semaphore finishedSem;
    private static Profesor instance = null;

    private short slots[] = {0, 0};
    private short slotCycle;

    private Student student1;
    private Student student2;

    private final String ime = "Profesor";

    private Profesor() {
        barrier = new CyclicBarrier(3);
        finishedSem = new Semaphore(0);

        student1 = null;
        student2 = null;

        slotCycle = -1;
    }


    @Override
    public void run() {
        //noinspection TryWithIdenticalCatches
        try {
            if ((student1 != null) && (student2 != null)) {

                Thread s1 = createStudentThread(student1, 0);
                Thread s2 = createStudentThread(student2, 1);

                s1.start();
                s2.start();

                s1.join();
                s2.join();

                finishedSem.release(2);
            }

            barrier.reset();
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private Thread createStudentThread(Student s, int i) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < s.getTrajanjeOdbrane(); i++) {};

                Random rand = new Random();
                int ocena = rand.nextInt(11);

                s.setImeIspitivaca(ime);
                s.setOcena(ocena);

                Main.gradeSum += ocena;
                Main.numberOfStudents++;

                slots[i] = 0;
            }
        });
    }

    public static void main(String[] args) {
        Thread pThread = new Thread(new Profesor());
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public static Profesor getInstance() {
        return instance == null ? instance = new Profesor() : instance;
    }

    public Semaphore getFinishedSem() {
        return finishedSem;
    }

    public void setStudent(Student s) {
        int i = (slotCycle = (short) ((slotCycle + 1) % 2));
        slots[i] = 1;
        if (i == 0) student1 = s;
        else if (i == 1) student2 = s;
    }

    public short[] getSlots() {
        return slots;
    }

}
