import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static long startTime = -1;
    public static int deadline = 7000;
    public static int studentNum = 70;

    public static final CountDownLatch start = new CountDownLatch(3);
    public static final CountDownLatch endQueue = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        AvgQueue queue = new AvgQueue(studentNum);
        Asistent asistent = new Asistent(queue);
        Profesor profesor = new Profesor(queue);

        Thread pThread, aThread, qThread;
        qThread = new Thread(queue);
        aThread = new Thread(asistent);
        pThread = new Thread(profesor);

        qThread.start();
        aThread.start();
        pThread.start();

        start.await();

        startTime = System.currentTimeMillis();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);

        Random rand = new Random();
        for(int i = 0; i < studentNum; i++) {
            Student s = new Student(rand.nextInt(1001)
                    , 500 + rand.nextInt(501)
                    , profesor
                    , asistent);

            executorService.schedule(s, s.getVremePrispeca(), TimeUnit.MILLISECONDS);
        }

        long remaining = 1;
        long currentTime = 0;
        while (remaining > 0) {
            remaining = deadline - currentTime;
            currentTime = System.currentTimeMillis() - startTime;
        }

        asistent.end();
        profesor.end();

        executorService.shutdownNow();

        endQueue.await();

        queue.end();
    }

    public static int currentTime(){
        return (int) (startTime - System.currentTimeMillis());
    }

    public static int timeLeft(){
        return (int) ( deadline - (startTime - System.currentTimeMillis()));
    }
}
