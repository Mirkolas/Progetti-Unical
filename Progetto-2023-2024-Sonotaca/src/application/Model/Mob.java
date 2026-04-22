package application.Model;

public class Mob extends Nemici implements Runnable {
    private Posizione coordinate;
    private final MondoGioco mondoGioco;
    public Mob(Posizione coordinate, MondoGioco mondoGioco) {
        super(coordinate, mondoGioco);
        this.coordinate = coordinate;
        this.mondoGioco = mondoGioco;
    }
    public Posizione getCoordinate() {
        return coordinate;
    }
    public synchronized Posizione SimulaMovimento() {
        return super.SimulaMovimento();
    }
    public synchronized void moveNemico() {
        coordinate = super.MuoviNemico();
    }
    @Override
    public void run() {mondoGioco.moveNemico(this);}
}

