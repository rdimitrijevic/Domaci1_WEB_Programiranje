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

            if (profesor.isReady(this)) {
                try {

                    vremePocetka = (int) (System.currentTimeMillis() - Main.startTime);

                    profesor.getBarrierReady().acquire();
                    profesor.getBarrier().await();

                    profesor.getFinishedSem().acquire();

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
                synchronized (asistent) {
                    try {
                        asistent.wait();

/*

                          Nakon sto semafor signalizira da je
                          asistent nit zavrsila ocenjivanje
                          student nit printuje svoje vrednosti
*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        asistent.getIsReadyLock().unlock();
                    }

                    return;
                }
            }
        }
    }

    public String getIme() {
        return ime;
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

    public int getOcena() {
        return ocena;
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

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String printMe(int trenutno) {
        return "ImeTreda: " + ime + " , Arrival: " + getVremePrispeca() + ", Prof: " + imeIspitivaca
                + ", TTC: " + getTrajanjeOdbrane() + ":" + getVremePocetka() + ", Ocena: " + ocena;
    }
}
