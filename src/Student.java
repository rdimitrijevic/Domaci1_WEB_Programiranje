import java.util.concurrent.BrokenBarrierException;

public class Student implements Runnable {

    private int vremePrispeca;
    private int vremePocetka;
    private int trajanjeOdbrane;
    private int ocena;

    private String ime;
    private String imeIspitivaca;

    private static int redniBrojNiti = 0;

    private Profesor profesor;
    private Asistent asistent;

    public Student(int vremePrispeca, int trajanjeOdbrane, Profesor profesor, Asistent asistent) {
        this.vremePrispeca = vremePrispeca;
        this.trajanjeOdbrane = trajanjeOdbrane;
        this.profesor = profesor;
        this.asistent = asistent;
        ime = "Student" + redniBrojNiti++;
    }

    @Override
    public void run() {
        while (true) {
            if (!profesor.getBarrier().isBroken()) {
                try {
                    profesor.getBarrier().await();

                    vremePocetka = (int) (System.currentTimeMillis() - Main.startTime);
                    profesor.setStudent(this);

                    profesor.getFinishedSem().acquire();

                    System.out.println(printMe());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                return;
            } else if (asistent.getIsReadyLock().tryLock()) {
                asistent.setStudent(this);
                vremePocetka = (int) (System.currentTimeMillis() - Main.startTime);
                asistent.getBeginSemaphore().release();
                try {
                    asistent.wait();
//                    asistent.getFinishedLock().acquire();

//                  Nakon sto semafor signalizira da je
//                  asistent nit zavrsila ocenjivanje
//                  student nit printuje svoje vrednosti
                    System.out.println(printMe());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    asistent.getIsReadyLock().unlock();
                }

                return;
            }
        }
    }

    public int getVremePrispeca() {
        return vremePrispeca;
    }

    public int getVremePocetka() {
        return vremePocetka;
    }

    public int getTrajanjeOdbrane() {
        return trajanjeOdbrane;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setVremePrispeca(int vremePrispeca) {
        this.vremePrispeca = vremePrispeca;
    }

    public void setVremePocetka(int vremePocetka) {
        this.vremePocetka = vremePocetka;
    }

    public void setTrajanjeOdbrane(int trajanjeOdbrane) {
        this.trajanjeOdbrane = trajanjeOdbrane;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setImeIspitivaca(String imeIspitivaca) {
        this.imeIspitivaca = imeIspitivaca;
    }

    private String printMe() {
        return "ImeTreda: " + ime + " , Arrival: " + (Main.startTime - vremePrispeca) + ", Prof: " + imeIspitivaca
                + ", TTC: " + trajanjeOdbrane + ":" + vremePocetka + ", Ocena: " + ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
}
