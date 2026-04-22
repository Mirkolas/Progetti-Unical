
package application.Model;

public class Ball {
    private Posizione pos;
    private final int dir;
    private int tempo;
    private final MondoGioco mondoGioco;
    private final Personaggio personaggio;
    public Ball(Personaggio p, MondoGioco world, int dir, Posizione pos, int tempo){
        this.personaggio=p;
        this.dir=dir;
        this.mondoGioco=world;
        this.tempo=tempo;
        this.pos=pos;
    }

    public void MuoviBall(){
        if(!mondoGioco.isPosizioneValida(pos.i(), pos.j()+dir) || mondoGioco.isBlocco(pos.i(), pos.j()+dir) || mondoGioco.isTrofeo(pos.i(), pos.j()+dir) || mondoGioco.isPulsante(pos.i(), pos.j()+dir)){personaggio.getToBeRemoved().add(this);}
        else if(mondoGioco.isNemico(pos.i(), pos.j()) || mondoGioco.isNemico(pos.i(), pos.j()+dir)){
            personaggio.getToBeRemoved().add(this);
            if(mondoGioco.isNemico(pos.i(), pos.j())) mondoGioco.getPersonaggio().UccisioneNemico(pos.i(), pos.j());
            else mondoGioco.getPersonaggio().UccisioneNemico(pos.i(), pos.j()+dir);
        }
        else if(!mondoGioco.isBlocco(pos.i(), pos.j()+dir)) {
            pos = new Posizione(pos.i(), pos.j() + dir);
            tempo+=1;
        }
    }
    public int getTempo() {
        return tempo;
    }
    public int getDir() {return dir;}
    public Posizione getPos() {
        return pos;
    }
}