package application.Model;

public class Time implements Runnable {
    private int timer = 0;
    private MondoGioco mondoGioco;
    public Time(MondoGioco w) {
        mondoGioco = w;
    }
    public int getTimer() {
        return timer;
    }
    @Override
    public void run() {
        if (timer <= 594000 ) timer++;
    }
}