import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Profesor implements Runnable {

    private CyclicBarrier barrier;
    private Semaphore barrierReady;
    private Semaphore finishedSem;
//    private static Profesor instance = null;

    private boolean slots[] = {false, false};
    private short slotCycle;
//    private short currWaiting = 0;

    private Student student1;
    private Student student2;

    private final String ime = "Profesor";

    public Profesor() {
        barrier = new CyclicBarrier(3);
        finishedSem = new Semaphore(0, true);
        barrierReady = new Semaphore(2);


        student1 = null;
        student2 = null;

        slotCycle = -1;
    }


    @Override
    public void run() {
//        noinspection TryWithIdenticalCatches
        System.out.println("Pocela profesor nit");
        while(true) {
            try {
                barrierReady.release(2);
                barrier.await();
//                System.out.println("Pukla barijera brane studenti: " + student1.getIme() + " i " + student2.getIme());
                if ((student1 != null) && (student2 != null)) {
//                    System.out.println("Profesor zapocinje odbranu");

/*
                    Thread s1 = createStudentThread(student1, 0);
                    Thread s2 = createStudentThread(student2, 1);

                    System.out.println("pre startova");
                    s1.start();
                    System.out.println("posle prvog starta");
                    s2.start();
                    System.out.println("Posle drugog starta");

                    System.out.println("Gotov je student 1");
                    s1.join();
                    System.out.println("Gotov je sutdent 2");
                    s2.join();
*/
                    int wait_time = Math.max(student1.getTrajanjeOdbrane(), student2.getTrajanjeOdbrane());


//                    System.out.println("Prosao wait time");
                    Thread.sleep(wait_time,0);
                    Random rand = new Random();

                    student1.setOcena(rand.nextInt(11));
                    student2.setOcena(rand.nextInt(11));

                    student1.setImeIspitivaca(ime);
                    student2.setImeIspitivaca(ime);

                    student1 = null;
                    student2 = null;

                    slots[0] = false;
                    slots[1] = false;

                    finishedSem.release(2);

                }


//                currWaiting = 0;

                barrier.reset();
//                System.out.println(barrier.getNumberWaiting() + " br na cekanju");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public Semaphore getBarrierReady() {
        return barrierReady;
    }

    private Thread createStudentThread(Student s, int i) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("unutar mini treda");
                System.out.println("Odbrana studenta " + (i + 1));
                for (int i = 0; i < s.getTrajanjeOdbrane(); i++) {};

                Random rand = new Random();
                int ocena = rand.nextInt(11);

                s.setImeIspitivaca(ime);
                s.setOcena(ocena);

                Main.gradeSum += ocena;
                Main.numberOfStudents++;

                slots[i] = false;
            }
        });
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

/*
    public static Profesor getInstance() {
        return instance == null ? instance = new Profesor() : instance;
    }
*/

    public Semaphore getFinishedSem() {
        return finishedSem;
    }

    public synchronized void setStudent(Student s) {
        int i = (++slotCycle % 2);
        slots[i] = true;

        if (i == 0) student1 = s;
        else if (i == 1) student2 = s;
    }

    public boolean[] getSlots() {
        return slots;
    }

    public synchronized boolean isReady(Student s){
        boolean res = (!slots[0] || !slots[1]) && !barrier.isBroken();
        if(res) setStudent(s);
        return res;
    }

}
