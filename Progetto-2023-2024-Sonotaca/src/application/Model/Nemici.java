package application.Model;


public class Nemici {


    protected int direzione =1;
    private boolean death=false;
    public void setDirezione(int direzione) {
        this.direzione = direzione;
    }
    private final MondoGioco mondoGioco;
    private Posizione coordinateNemico;

    public Nemici(Posizione coordinate , MondoGioco mondoGioco) {
        this.mondoGioco = mondoGioco;
        this.coordinateNemico = coordinate;
    }

    public  Posizione MuoviNemico(){
        coordinateNemico = SimulaMovimento();
        if((mondoGioco.isPersonaggio(coordinateNemico.i(),coordinateNemico.j())) || (mondoGioco.isPersonaggio(coordinateNemico.i(),coordinateNemico.j()+1 ))) {mondoGioco.getPersonaggio().MortePersonaggio();}
        return coordinateNemico;
    }

    public  Posizione SimulaMovimento()   {
        int pos_i= coordinateNemico.i();
        int pos_j= coordinateNemico.j();
        if(direzione == ImpostazioniDim.MUOVI_SINISTRA) pos_j--;
        else pos_j++;
        return  new Posizione(pos_i,pos_j);

    }

    public boolean isDeath() {return death;}
    public void setDeath(boolean death) {this.death = death;}
    public int getDirezione() {return direzione;}
    public Posizione getCoordinate() {return coordinateNemico;}
}
