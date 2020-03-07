import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AvgQueue implements Runnable {
    private ArrayBlockingQueue<Student> writeQueue;

    private int numOfStudents = 0;
    private int sumOfGrades = 0;
    private double gradesAvg = 0;

    private boolean programEnded = false;

    public AvgQueue(int queueSize) {
        this.writeQueue = new ArrayBlockingQueue<>(queueSize);
/*        synchronized (this){
            this.notifyAll();
        }*/
    }

    @Override
    public void run() {
        Main.start.countDown();

        try {
            Student s = writeQueue.take();
            while(true){
                numOfStudents++;
                sumOfGrades += s.getOcena();
                gradesAvg = 1.0 * sumOfGrades / numOfStudents;

                System.out.println(s.printMe(Main.currentTime()));

                while((s = writeQueue.poll()) == null) { if(programEnded) { printResults(); return;} };
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void queuePut(Student s) {
        try {
            writeQueue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayBlockingQueue<Student> getWriteQueue() {
        return writeQueue;
    }

    private void printResults() {
        System.out.println("Broj ocenjenih studenta: " + numOfStudents);
        System.out.println("Suma svih ocena: " + sumOfGrades);
        System.out.println("Prosecna ocena: " + gradesAvg);
    }

    public void end() {
        programEnded = true;
    }
}
