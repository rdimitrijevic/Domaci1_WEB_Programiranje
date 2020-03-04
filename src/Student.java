
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
        while(true){
            if( !profesor.getBarrier().isBroken() ) {

            } else if( asistent.getIsReadyLock().tryLock() ) {
                asistent.setStudent(this);
                asistent.getBeginSemaphore().release();
                try {
                    asistent.getFinishedLock().acquire();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    asistent.getIsReadyLock().unlock();
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

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
}
