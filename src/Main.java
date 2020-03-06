import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static long startTime = System.currentTimeMillis();

    public static long gradeSum = 0;
    public static long numberOfStudents = 0;
    public static double averageGrade = 0.0;

    public static void main(String[] args) throws InterruptedException {
        Asistent asistent = new Asistent();
        Profesor profesor = new Profesor();

        Thread pThread, aThread;
        (aThread = new Thread(asistent)).setDaemon(true);
        (pThread = new Thread(profesor)).setDaemon(true);

        aThread.start();
        pThread.start();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);

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

    }
}
