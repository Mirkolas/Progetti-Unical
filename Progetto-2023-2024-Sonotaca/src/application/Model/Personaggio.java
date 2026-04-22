package application.Model;

import application.Block;

import application.Stato;
import application.Audio.Sound;
import application.Controller.Controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;




public class Personaggio {

    public static MondoGioco mondoGioco;
    private final Sound morte = new Sound("MorteFinale.wav");
    private final Sound danno = new Sound("Danno.wav");
    public Sound getMorte() {
        return morte;
    }
    private final LinkedList<Ball> Balls = new LinkedList<>();
    private int sleepShoot=0;
    private final List<Ball> toBeRemoved = new ArrayList<>();
    private static int lives;
    public static int getLives() {
        return lives;
    }
    private boolean StoSaltando = false;
    private boolean StoCadendo = false;
    public boolean isStoSaltando() {
        return StoSaltando;
    }
    public boolean isStoCadendo() {
        return StoCadendo;
    }
    public void setStoSaltando(boolean stoSaltando) {
        StoSaltando = stoSaltando;
    }
    public void setStoCadendo(boolean stoCadendo) {StoCadendo = stoCadendo;}
    private int PreDirezione = ImpostazioniDim.MUOVI_DESTRA;
    public int getPreDirezione() {
        return PreDirezione;
    }
    public static Posizione CoordinatePersonaggio;
    private final LinkedList<Nemici> nemici;
    public static Posizione getCoordinatePersonaggio() {
        return CoordinatePersonaggio;
    }
    private int direzione = ImpostazioniDim.NON_IN_MOVIMENTO;
    private int Moneta = 0;
    public int getMoneta() {
        return Moneta;
    }
    public static int cont = 0;
    private Sound abilita;
    public void AggiornaPos(int direzione) {
        this.direzione = direzione;
    }
    private int kill = 0;
    public int getKill() {
        return kill;
    }

    public Personaggio(Posizione coordinate, LinkedList<Nemici> nemici, MondoGioco mondoGioco) {
        Personaggio.mondoGioco = mondoGioco;
        this.nemici = nemici;
        CoordinatePersonaggio = coordinate;

    }

    public void move() {
        if (Controller.getPremuti().contains(ImpostazioniDim.SALTA) && Game.getMondo().getWorld().isBlocco(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j()) && cont == 0) {
            Sound salta = new Sound("Jump.wav");
            salta.play();
        }
        CoordinatePersonaggio = SimulaMovimento();
        if (mondoGioco.isNemico(CoordinatePersonaggio.i()+1, CoordinatePersonaggio.j()) || mondoGioco.isNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()) || mondoGioco.isMorte(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j())) {

            if (mondoGioco.getLiv() == 1 && mondoGioco.isMorte(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j())) {
                Sound acqua = new Sound("Acqua.wav");
                acqua.play();
            } else if (mondoGioco.getLiv() == 2 && mondoGioco.isMorte(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j())) {
                Sound lava = new Sound("Fuoco.wav");
                lava.play();
            }
            MortePersonaggio();
        }
        if (mondoGioco.isCoin(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()) || mondoGioco.isCoin(CoordinatePersonaggio.i(), CoordinatePersonaggio.j())) {
            Sound coin = new Sound("Moneta.wav");
            coin.play();
            Moneta++;
        } else if (mondoGioco.isVita(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()) || mondoGioco.isVita(CoordinatePersonaggio.i(), CoordinatePersonaggio.j())) {
            if (getLives() != 4) {
                abilita = new Sound("Vita.wav");
                abilita.play();
                setLives(getLives() + 1);
            }
        }
        if (direzione != ImpostazioniDim.NON_IN_MOVIMENTO && direzione != ImpostazioniDim.SALTA && direzione !=ImpostazioniDim.SHOOT && direzione !=ImpostazioniDim.PULSANTE) PreDirezione = direzione;
        if (direzione != ImpostazioniDim.PULSANTE) SuonoPassi();
        ControlloSalto();
    }


    private void SuonoPassi() {
        if (!Controller.getPremuti().isEmpty() || isStoCadendo()) {
            Sound cammina;
            if (mondoGioco.isBlocco(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j())) {
                cammina = new Sound("Cammina.wav");
                cammina.play();
            }

        }
    }

    public void premipulsante(){
        if (mondoGioco.isRis()){
            mondoGioco.setMatrice(10,4,Block.PIATTAFORMA);
            mondoGioco.setMatrice(10,5,Block.PIATTAFORMA);
            mondoGioco.setMatrice(10,6,Block.PIATTAFORMA);
        }
        else {
            mondoGioco.setMatrice(10,4,Block.NULLO);
            mondoGioco.setMatrice(10,5,Block.NULLO);
            mondoGioco.setMatrice(10,6,Block.NULLO);
        }
        Sound apri =new Sound("Abilita.wav");
        apri.play();
        mondoGioco.setRis(!mondoGioco.isRis());
    }
    private void ControlloSalto() {
        if (!isStoSaltando() && !isStoCadendo() && !mondoGioco.isBlocco(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j()))
            setStoCadendo(true);

        if (isStoSaltando()) {
            if (cont < 2) {
                if (mondoGioco.isNemico(CoordinatePersonaggio.i() - 2, CoordinatePersonaggio.j())) {
                    Sound sbatte = new Sound("Hit.wav");
                    UccisioneNemico(CoordinatePersonaggio.i() - 2, CoordinatePersonaggio.j());
                    mondoGioco.setMatrice(CoordinatePersonaggio.i() - 2, CoordinatePersonaggio.j(),Block.FRUTTA);
                    sbatte.play();
                    }
                if (mondoGioco.isBlocco(CoordinatePersonaggio.i() - 1, CoordinatePersonaggio.j()) || mondoGioco.isMorte(CoordinatePersonaggio.i() - 1, CoordinatePersonaggio.j())) {
                    Sound sbatte = new Sound("Hit.wav");
                    sbatte.play();
                    if (mondoGioco.isMystery(CoordinatePersonaggio.i() - 1, CoordinatePersonaggio.j())) {
                        abilita = new Sound("Abilita.wav");
                        abilita.play();
                        mondoGioco.setMatrice(CoordinatePersonaggio.i() - 1, CoordinatePersonaggio.j(), Block.USATO);
                        GeneraAbilita();
                    }
                    setStoSaltando(false);
                    setStoCadendo(true);
                } else cont++;
            } else {
                setStoSaltando(false);
                setStoCadendo(true);
            }
        }
        if (isStoCadendo()) {
            if (mondoGioco.isBlocco(CoordinatePersonaggio.i() + 1, CoordinatePersonaggio.j())) {
                setStoCadendo(false);
                cont = 0;
            }


        }
    }

    public void moveBall(){
        if(sleepShoot!=0) sleepShoot--;
        for(Ball t:Balls){
            if(t.getTempo()<10){t.MuoviBall();}
            else toBeRemoved.add(t);
        }
        for(Ball t:toBeRemoved){Balls.remove(t);}
        toBeRemoved.clear();
    }

    public void shootBall(){
        if(sleepShoot!=0) return;
        sleepShoot=3;
        Sound Spara=new Sound("Spara.wav");
        Spara.play();
        if(mondoGioco.isNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()) || mondoGioco.isNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()+ PreDirezione)){
            if(mondoGioco.isNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j())) {
                Sound kill=new Sound("Danno.wav");
                kill.play();
                UccisioneNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j());
            }
            else UccisioneNemico(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()+ PreDirezione);
        } else if(!mondoGioco.isBlocco(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()+ PreDirezione) && mondoGioco.isPosizioneValida(CoordinatePersonaggio.i(), CoordinatePersonaggio.j()+ PreDirezione)) {
            Ball Ball = new Ball(this, mondoGioco, PreDirezione, new Posizione(CoordinatePersonaggio.i() , CoordinatePersonaggio.j() + PreDirezione), 0);
            Balls.add(Ball);
        }
    }
    private void GeneraAbilita() {
        Random r = new Random();
        int n = r.nextInt(1, 3);
        if (n == 1) mondoGioco.setMatrice(CoordinatePersonaggio.i() - 2, CoordinatePersonaggio.j(), Block.FRUTTA);
        else mondoGioco.setMatrice(CoordinatePersonaggio.i() - 2, CoordinatePersonaggio.j(), Block.VITA);
    }

    public void UccisioneNemico(int nemico_i, int nemico_j) {
        kill++;
        for (int i = 0; i < nemici.size(); i++) {
            if(nemici.get(i).getCoordinate().i()==nemico_i && nemici.get(i).getCoordinate().j()==nemico_j)
            {
                Sound morteNemico;
                if(nemici.get(i) instanceof Mob)
                {
                    morteNemico = new Sound("Hit.wav");
                    morteNemico.play();
                }
                if (mondoGioco.getNewNemici().get(i) != null) {
                    mondoGioco.getNewNemici().get(i).cancel(true);
                    mondoGioco.getLevel().setNemiciMorti(i);
                }
                nemici.get(i).setDeath(true);
                mondoGioco.setMatrice(nemici.get(i).getCoordinate().i(),nemici.get(i).getCoordinate().j(),Block.FRUTTA);

            }
        }
    }

    public Posizione  SimulaMovimento() {
        int pos_i = CoordinatePersonaggio.i();
        int pos_j = CoordinatePersonaggio.j();
        if (Controller.getPremuti().contains(ImpostazioniDim.SALTA)) {
            if (!StoSaltando && !StoCadendo) {
                StoSaltando = true;
            }
        }
        if (StoSaltando) {
            pos_i = pos_i - 1;
        } else if (StoCadendo) {
            pos_i = pos_i + 1;
        }
        if (Controller.getPremuti().contains(ImpostazioniDim.MUOVI_SINISTRA)) {
            if ((!StoSaltando && !StoCadendo) ||  (!mondoGioco.isMorte(pos_i, pos_j - 1) && !mondoGioco.isBlocco(pos_i, pos_j - 1)  && mondoGioco.isPosizioneValida(pos_i, pos_j - 1))) {
                pos_j = pos_j - 1;
            }
        }
        if (Controller.getPremuti().contains(ImpostazioniDim.MUOVI_DESTRA)) {
            if ((!StoSaltando && !StoCadendo) || (!mondoGioco.isMorte(pos_i, pos_j + 1)  && !mondoGioco.isBlocco(pos_i, pos_j + 1)  && mondoGioco.isPosizioneValida(pos_i, pos_j + 1))) {
                pos_j = pos_j + 1;
            }
        }
        return  new Posizione(pos_i, pos_j);
    }

    public void MortePersonaggio() {
            Posizione coordinPlayer = mondoGioco.getLevel().getCoordinatePersonaggio();
            danno.play();
            if (lives == 1) {morte.play();}
            mondoGioco.setMatrice(CoordinatePersonaggio.i(), CoordinatePersonaggio.j(), Block.NULLO);
            mondoGioco.setMatrice(CoordinatePersonaggio.i(), CoordinatePersonaggio.j(), Block.NULLO);
            StoCadendo = false;
            StoSaltando = false;
            Personaggio.cont = 0;
            setCoordinate(coordinPlayer);
            if (lives > 0) setLives(Personaggio.getLives() - 1);
            if (lives == 0) {
                Game.setGameStatus(Stato.GAME_OVER);
                mondoGioco.getLevel().FermaNemici();
            }
            mondoGioco.AggiornaPos(ImpostazioniDim.MUOVI_DESTRA);
            mondoGioco.setMorte(true);
        }

    public List<Ball> getToBeRemoved() {
        return toBeRemoved;
    }
    public LinkedList<Ball> getBalls() {
        return Balls;
    }
    public static void setLives(int lives) {
        Personaggio.lives = lives;
    }
    public void setCoordinate(Posizione coordinate) {
        CoordinatePersonaggio = coordinate;
    }
}
