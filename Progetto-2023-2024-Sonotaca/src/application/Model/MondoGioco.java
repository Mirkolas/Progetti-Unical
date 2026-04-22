package application.Model;
import application.Block;
import application.Controller.Controller;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

public class MondoGioco {

    private final LinkedList<ScheduledFuture<?>> NewNemici;
    private final LinkedList<Nemici> arrayNemici;
    private final Personaggio personaggio;
    private final int liv;
    private boolean ris=false;
    private Block[][] Matrice;
    public Personaggio getPersonaggio() {
        return personaggio;
    }
    private List<String> Livello;
    public List<String> getLivello() {
        return Livello;
    }
    public void setMatrice(int i, int j, Block block) {
        Matrice[i][j] = block;
    }
    public int getLiv() {
        return liv;
    }
    public Livelli getLevel() {
        return livelli;
    }
    private final Livelli livelli;
    public LinkedList<Nemici> getNemici() {
        return arrayNemici;
    }

    private boolean morte = false;
    public void setMorte(boolean morte) {
        this.morte = morte;
    }
    
    public MondoGioco(int liv, int lives){
        livelli = new Livelli(this , liv );
        Posizione coordinatePersonaggio = livelli.getCoordinatePersonaggio();
        arrayNemici =  livelli.getArrayNemici();
        NewNemici = livelli.getNewNemici();
        personaggio = new Personaggio(coordinatePersonaggio,arrayNemici,this);
        Personaggio.setLives(lives);
        this.liv=liv;
        inizializzaMatricePrincipale();
    }


    public void MuoviPersonaggio() {
        if(Controller.getPremuti().contains(ImpostazioniDim.SHOOT)){personaggio.shootBall();}
        if (Controller.getPremuti().contains(ImpostazioniDim.PULSANTE) && ( isPulsante(Personaggio.getCoordinatePersonaggio().i(),Personaggio.getCoordinatePersonaggio().j()+1) || isPulsante(Personaggio.getCoordinatePersonaggio().i(),Personaggio.getCoordinatePersonaggio().j()-1))){
            if(!(isNemico(9,4) || isNemico(9,6)|| isNemico(9,5))) personaggio.premipulsante();
        }
        personaggio.moveBall();
        Posizione newPosizione = personaggio.SimulaMovimento();
        boolean count = isPosizioneValida(newPosizione.i(), newPosizione.j()) && !isBlocco(newPosizione.i(), newPosizione.j()) && !isTrofeo(newPosizione.i(), newPosizione.j());
        if (count) {
            Matrice[Personaggio.getCoordinatePersonaggio().i()][Personaggio.getCoordinatePersonaggio().j()] = Block.NULLO;
            personaggio.move();
            if (!morte) {
                Matrice[newPosizione.i()][newPosizione.j()] = Block.PERSONAGGIO;
            } else {
                morte = false;
                Matrice[Personaggio.getCoordinatePersonaggio().i()][Personaggio.getCoordinatePersonaggio().j()] = Block.PERSONAGGIO;
            }
        }
    }

    public  void moveNemico(Nemici nemici) {
        Posizione newPosition = nemici.SimulaMovimento();
        int pos_i = newPosition.i();
        int pos_j = newPosition.j();
        int direzione = nemici.getDirezione();
        if (!nemici.getCoordinate().equals(newPosition)) {
            if (!isBlocco(pos_i + 1, pos_j) || isBlocco(pos_i, pos_j) || isNemico(pos_i, pos_j + direzione)) {
                nemici.setDirezione(-direzione);
            }
        }
            if (nemici instanceof Mob mob) {mob.moveNemico();}
    }

    public void inizializzaMatricePrincipale() {
        file file = new file();
        try {
            Livello = file.leggi("src/application/resources/Livelli/Livello" + liv + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matrice = new Block[Livello.size()][Livello.getLast().length()];
        for (int i = 0; i < Livello.size(); i++) {
            String riga = Livello.get(i);
            for (int j = 0; j < riga.length(); j++) {
                char cella = riga.charAt(j);
                if (cella == 'a') { Matrice[i][j] = Block.NULLO;
                } else if (cella == 'M') {Matrice[i][j] = Block.FRUTTA;
                } else if (cella == 'P') {Matrice[i][j] = Block.PULSANTE;
                } else if (cella == 'e') {Matrice[i][j] = Block.PIATTAFORMA;
                } else if (cella == 'g') {Matrice[i][j] = Block.MYSTERY;
                } else if (cella == 'j') {Matrice[i][j] = Block.TROFEO;
                } else if (cella == 'm') {Matrice[i][j] = Block.MORTE;}
            }
        }
    }
    public void AggiornaPos(int direzione) {personaggio.AggiornaPos(direzione);}
    private boolean isType(int i, int j, Block b) {
        if (isPosizioneValida(i, j))
            return Matrice[i][j] == b;
        return false;
    }
    public LinkedList<ScheduledFuture<?>> getNewNemici() {return NewNemici;}
    public boolean isPosizioneValida(int i, int j) {return i>=0 && i< ImpostazioniDim.Righe && j>=0 && j<ImpostazioniDim.Colonne;}

    public boolean isWall(int i,int j) {return isType(i,j,Block.PIATTAFORMA);}
    public boolean isPersonaggio(int i, int j) {return isType(i,j,Block.PERSONAGGIO);}
    public boolean isCoin(int i, int j) { return isType(i,j,Block.FRUTTA);}
    public boolean isMystery(int i, int j) { return isType(i,j,Block.MYSTERY);}
    public boolean isBalls(int i, int j) {
        if (!isPosizioneValida(i, j)) return false;
        LinkedList<Ball> balls = personaggio.getBalls();
        if (balls == null) {return false;}
        for (Ball t : balls) {
            if (t == null) {
                continue;
            }
            if (Objects.equals(t.getPos(), new Posizione(i, j))) return true;
        }
        return false;
    }
    public boolean isRis() {return ris;}
    public void setRis(boolean ris) {this.ris = ris;}
    public boolean isMorte(int i, int j) { return isType(i,j,Block.MORTE);}
    public boolean isTrofeo(int i, int j) { return isType(i,j,Block.TROFEO);}
    public boolean isUsato(int i, int j) { return isType(i,j,Block.USATO);}
    public boolean isPulsante(int i, int j) { return isType(i,j,Block.PULSANTE);}
    public boolean isVita(int i, int j) { return isType(i,j,Block.VITA);}
    public boolean isNemico(int i, int j) {
        for(Nemici n : arrayNemici){ if (!n.isDeath() && Objects.equals(n.getCoordinate(), new Posizione(i, j))) return true;}
        return false;
    }
    public boolean isBlocco(int i, int j){return isWall(i,j) || isMystery(i,j) || isUsato(i,j) || isPulsante(i,j);}}






