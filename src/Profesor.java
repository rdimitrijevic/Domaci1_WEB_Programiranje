import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Profesor implements Runnable {

    private CyclicBarrier barrier;
    private Semaphore barrierReady;
    private Semaphore finishedSem;

    private boolean slots[] = {false, false};
    private short slotCycle;

    private Student student1;
    private Student student2;
    private AvgQueue queue;

    private final String ime = "Profesor";
    private boolean first_iter = true;
    private boolean programEnded = false;

    public Profesor(AvgQueue queue) {
        barrier = new CyclicBarrier(2);
        finishedSem = new Semaphore(0, true);
        barrierReady = new Semaphore(2);

        this.queue = queue;
        student1 = null;
        student2 = null;

        slotCycle = -1;
    }


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

        while(true) {
            try {
                if(!first_iter) barrierReady.release(2);
                if(first_iter) first_iter = false;

                while( !(slots[0] && slots[1]) ) { if (programEnded) { Main.endQueue.countDown(); return; } }

                if ((student1 != null) && (student2 != null)) {

                    int wait_time = Math.max(student1.getTrajanjeOdbrane(), student2.getTrajanjeOdbrane());


                    Thread.sleep(wait_time,0);
                    Random rand = new Random();

                    student1.setOcena(rand.nextInt(11));
                    student2.setOcena(rand.nextInt(11));

                    student1.setImeIspitivaca(ime);
                    student2.setImeIspitivaca(ime);

                    queue.queuePut(student1);
                    queue.queuePut(student2);

                    student1 = null;
                    student2 = null;

                    slots[0] = false;
                    slots[1] = false;

                    finishedSem.release(2);
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Semaphore getBarrierReady() {
        return barrierReady;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public Semaphore getFinishedSem() {
        return finishedSem;
    }

    private void setStudent(Student s) {
        int i = (++slotCycle % 2);
        slots[i] = true;

        if (i == 0) student1 = s;
        else student2 = s;
    }

    public void end(){
        programEnded = true;
    }

    /**
     * @param s Student objekat koji zahteva da pristup
     * @return true ukoliko se student uspesno registrovao kod profesora
     */
    public synchronized boolean isReady(Student s){
        boolean res = (!slots[0] || !slots[1]) && !barrier.isBroken();
        if(res) setStudent(s);
        return res;
    }

}
