import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static long startTime = System.currentTimeMillis();
    private static int deadline = 5000;
    public static int studentNum = 30;

    public static final CountDownLatch start = new CountDownLatch(3);
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

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);

        Random rand = new Random();
        for(int i = 0; i < studentNum; i++) {
            Student s = new Student(rand.nextInt(1001)
                    , 500 + rand.nextInt(501)
                    , profesor
                    , asistent);

            executorService.schedule(s, s.getVremePrispeca(), TimeUnit.MILLISECONDS);
        }


        /*
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);

        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(500, 700, profesor, asistent),500, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(600, 400, profesor, asistent),600, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(600, 200, profesor, asistent),600, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(600, 300, profesor, asistent),600, TimeUnit.MILLISECONDS);
        executorService.schedule(new Student(900, 480, profesor, asistent),900, TimeUnit.MILLISECONDS);
*/

    }

    public static int currentTime(){
        return (int) (startTime - System.currentTimeMillis());
    }

    public static int timeLeft(){
        return (int) ( deadline - (startTime - System.currentTimeMillis()));
    }
}
