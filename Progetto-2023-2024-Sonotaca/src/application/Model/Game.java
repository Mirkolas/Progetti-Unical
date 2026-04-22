package application.Model;

import application.Stato;
import application.View.GamePanel;

import java.io.IOException;


import static application.Model.Personaggio.CoordinatePersonaggio;

public class Game
{
    private MondoGioco mondoGioco;
    private static float tempo1;
    private static float tempo2;
    private static float tempo3;
    public static Game getMondo() {return mondo;}
    public void setDirezione(int direzione)  { mondoGioco.AggiornaPos(direzione);}
    private boolean cont=false;
    private boolean nextLevel=false;
    private boolean Lock4=true;
    private Game() throws IOException {stato = Stato.MENU;}
    private static final Game mondo;
    private static Stato stato;
    public static Stato getStati() {
        return stato;
    }
    public static  void setGameStatus(Stato stato) {
        Game.stato = stato;
    }
    static {
        try {
            mondo = new Game();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void LivelloSuperato() {
        if (stato == Stato.IN_GIOCO ) {
            if(mondoGioco.getLivello().get(CoordinatePersonaggio.i()).charAt(CoordinatePersonaggio.j()+1)!='j' || nextLevel){
                if(nextLevel) nextLevel=false;
                mondoGioco.MuoviPersonaggio();
            }
            else if (mondoGioco.getLivello().get(CoordinatePersonaggio.i()).charAt(CoordinatePersonaggio.j()+1)=='j'){
                if(!cont) {
                    setGameStatus(Stato.VITTORIA);
                    mondoGioco.getLevel().FermaNemici();
                    if (mondoGioco.getLiv()==1) {tempo1 = mondoGioco.getLevel().getTime().getTimer();}
                    else if (mondoGioco.getLiv()==2){tempo2 = mondoGioco.getLevel().getTime().getTimer();}
                    else if (mondoGioco.getLiv()==3){tempo3 = mondoGioco.getLevel().getTime().getTimer(); setLock4(false);}
                    cont = true;
                    if (mondoGioco.getLiv() == 1) {GamePanel.setLock2(false);}
                    else if (mondoGioco.getLiv() == 2) {GamePanel.setLock3(false);}
                }
                if (stato == Stato.IN_GIOCO && mondoGioco.getLiv() < 3) {
                    cont=false;
                    mondoGioco = new MondoGioco(mondoGioco.getLiv() + 1,3);
                    nextLevel=true;

                }
            }

        }
        if(stato!=Stato.VITTORIA && cont) cont= false;
    }
    public boolean getLock4() {
        return Lock4;
    }
    public void setLock4(boolean lock4) {
        Lock4 = lock4;
    }
    public static void setTempo1(float tempo1) {
        Game.tempo1 = tempo1;
    }
    public static void setTempo2(float tempo2) {
        Game.tempo2 = tempo2;
    }
    public static void setTempo3(float tempo3) {
        Game.tempo3 = tempo3;
    }
    public static int getTempo() {
        return (int) tempo1;
    }
    public static int getTempo2() {
        return (int) tempo2;
    }
    public static int getTempo3() {
        return (int) tempo3;
    }
    public MondoGioco getWorld() { return mondoGioco;}
    public void setWorld(MondoGioco mondoGioco) {
        this.mondoGioco = mondoGioco;
    }
}
