package application.Model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Livelli {
    private final Posizione CoordinatePersonaggio;
    private final List<Nemici> Slime = new LinkedList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final LinkedList<Nemici> arrayNemici = new LinkedList<>();
    private final LinkedList<Integer> indiciNemiciMorti = new LinkedList<>();
    private final LinkedList<ScheduledFuture<?>> NewNemici = new LinkedList<>();
    private final MondoGioco mondoGioco;
    private Time time;
    private ScheduledFuture<?> timer;

    public Livelli(MondoGioco mondoGioco, int livello) {
        this.mondoGioco = mondoGioco;
        CoordinatePersonaggio=InizializzaPersonaggio();
        InizializzaNemici(livello);
        AvviaNemici();
        AvviaTempo();
    }

    private void AvviaTempo(){
        time= new Time(mondoGioco);
        timer=executorService.scheduleAtFixedRate(time, 0,10, TimeUnit.MILLISECONDS);
    }

    private void InizializzaNemici(int livello) {
        int[][] posizioneSlime = {};
        if (livello == 1) {posizioneSlime = new int[][]{{26, 8},{25,3},{18,6},{9,5},{15,3},{15,13},{5,26},{24,26},{5,16}};
        } else if (livello == 2) {posizioneSlime = new int[][]{  {26, 8},{25,3},{18,6},{9,5},{15,3},{15,13},{5,26},{24,26},{5,16}};
        } else if (livello == 3) {posizioneSlime = new int[][]{{26, 8},{25,3},{18,6},{9,5},{15,3},{15,13},{5,26},{24,26},{5,16}};}

        for (int[] pos : posizioneSlime) {
            LinkedList<Posizione> SlimePos = new LinkedList<>();
            SlimePos.add(new Posizione(pos[0], pos[1]));
            for (Posizione positions : SlimePos) {Nemici Slime = new Mob(positions, this.mondoGioco);this.Slime.add(Slime);arrayNemici.add(Slime);}
        }
    }

    private void AvviaNemici() {
        for (Nemici slime : Slime) {ScheduledFuture<?> future = executorService.scheduleAtFixedRate((Runnable) slime, 500, 300, TimeUnit.MILLISECONDS);NewNemici.add(future);}
    }

    public void FermaNemici() {
        for (ScheduledFuture<?> scheduledFuture : NewNemici) if (scheduledFuture != null) scheduledFuture.cancel(true);timer.cancel(true);}

    public void RiavviaNemici() {
        NewNemici.clear();
        for (Nemici slime : Slime) { ScheduledFuture<?> future = executorService.scheduleAtFixedRate( (Runnable) slime, 500, 300, TimeUnit.MILLISECONDS);NewNemici.add(future);}
        for (Integer integer : indiciNemiciMorti) NewNemici.get(integer).cancel(true);
        timer=executorService.scheduleAtFixedRate(time,0,10,TimeUnit.MILLISECONDS);
    }
    private Posizione InizializzaPersonaggio() {return new Posizione(29, 5);}
    public LinkedList<Nemici> getArrayNemici() {return arrayNemici;}
    public Posizione getCoordinatePersonaggio() {return CoordinatePersonaggio;}
    public LinkedList<ScheduledFuture<?>> getNewNemici() {return NewNemici;}
    public void setNemiciMorti(int i) {indiciNemiciMorti.add(i);}
    public Time getTime() {return time;}
}



