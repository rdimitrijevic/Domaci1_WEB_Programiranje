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

    public String getIme() {
        return ime;
    }

    @Override
    public void run() {
//        System.out.println("Pocela je student nit : " + ime);
        while (true) {

            if (profesor.isReady(this)) {

//                System.out.println(ime + " brani kod profesora");
                try {
//                    System.out.println(ime + " je zauzeo mesto za odbranu kod prof");

//                    System.out.println(ime + " pocinje odbranu kod profesora");
                    vremePocetka = (int) (System.currentTimeMillis() - Main.startTime);
//                    profesor.setStudent(this);

                    profesor.getBarrierReady().acquire();
                    profesor.getBarrier().await();

//                    System.out.println(ime + " kod prof brani rad...");
                    profesor.getFinishedSem().acquire();
                    System.out.println(printMe((int) (System.currentTimeMillis() - Main.startTime)));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    System.out.println(ime + " :: \n");
                    e.printStackTrace();
                }

//                System.out.println("Kraj niti " + ime);
                return;
            } else if (asistent.getIsReadyLock().tryLock()) {
//                System.out.println(ime + " brani kod asistenta");
                asistent.setStudent(this);
                vremePocetka = (int) (System.currentTimeMillis() - Main.startTime);
                asistent.getBeginSemaphore().release();
                synchronized (asistent) {
                    try {
//                        System.out.println(ime + " kod asistenta brani rad...");
                        asistent.wait();
//                    asistent.getFinishedLock().acquire();

//                  Nakon sto semafor signalizira da je
//                  asistent nit zavrsila ocenjivanje
//                  student nit printuje svoje vrednosti
                        System.out.println(printMe((int) (System.currentTimeMillis() - Main.startTime)));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        asistent.getIsReadyLock().unlock();
                    }

//                    System.out.println("Kraj niti " + ime);
                    return;
                }
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

    private String printMe(int trenutno) {
        return "ImeTreda: " + ime + " , Arrival: " + getVremePrispeca() + ", Prof: " + imeIspitivaca
                + ", TTC: " + getTrajanjeOdbrane() + ":" + getVremePocetka() + ", Ocena: " + ocena + ", Trenuto vreme " + trenutno;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
}
