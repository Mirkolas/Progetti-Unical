package application.model;

import application.Block;
import application.controller.Controller;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class World {

    private final LinkedList<Position> coordinatePlayer;
    private final LinkedList<ScheduledFuture<?>> arrayFuture;
    private final LinkedList<Enemy> arrayNemici;
    private final Player player ;
    private final int liv;

    public Level getLevel() {
        return level;
    }
    private final Level level;

    public World(int liv, int lives){



        level = new Level(this , liv );

        coordinatePlayer = level.getCoordinatePlayer();
        arrayNemici =  level.getArrayNemici();
        arrayFuture = level.getArrayFuture();

        player = new Player(coordinatePlayer,arrayNemici,this);
        player.setLives(lives);

        this.liv=liv;

        inizializzaMatricePrincipale();



    }

    private boolean tel1;
    private boolean tel2;

    public boolean isTel1() {
        return tel1;
    }

    public boolean isTel2() {
        return tel2;
    }

    public void setTel1(boolean tel1) {
        this.tel1 = tel1;
    }

    public void setTel2(boolean tel2) {
        this.tel2 = tel2;
    }

    private LinkedList<Position> newPosition;
    private boolean morte=false;
    public void setMorte(boolean morte){
        this.morte=morte;
    }
    //serve per far muovere il player e aggiornare la sua posizione sulla matrice principale

    public void movePlayer() {
        if(velP%ab==0) {
            //restituisce a seconda del movimento le nuove posizione della testa e del corpo del giocatore
            newPosition = player.simulateMove();


            //devo verificare che le nuove posizioni sia valide
            int count = 0;
            for (Position p : newPosition) {
                // isValidPosition controlla se la position è nel range del mondo

                if (isValidPosition(p.i(), p.j()) && !isBlocco(p.i(), p.j())) {
                    count++;
                }
            }

            if (count == newPosition.size()) {
                telCheck();
                for (int k = 0; k < coordinatePlayer.size(); k++) {
                    matrice_Principale[player.getPosition(k).i()][player.getPosition(k).j()] = Block.VUOTO;
                }
                player.move();

                if (!morte) {
                    for (int k = 0; k < coordinatePlayer.size(); k++) {
                        matrice_Principale[newPosition.get(k).i()][newPosition.get(k).j()] = Block.PERSONAGGIO;
                    }
                } else {
                    morte = false;
                    for (int k = 0; k < coordinatePlayer.size(); k++) {
                        matrice_Principale[player.getCoordinatePlayer().get(k).i()][player.getCoordinatePlayer().get(k).j()] = Block.PERSONAGGIO;
                    }
                }
            }
        }
        velP+=1;
    }
    private int velP=0;
    private int ab=2;

    public void setAb(int ab) {
        this.ab = ab;
    }
    private int add=1;
    private int blockToRemove=69;
    private int blockToAdd=72;
    private int blockCount=0;

    private int moved = 0 ;
    public LinkedList<Enemy> getNemici() {
        return arrayNemici;
    }

    public void moveBlocks(){
        if(blockCount%4==0) {

            if(moved!=0){
                moved=0;
                Controller.getPressed().remove(add);
            }
            if(matrice_Principale[17][blockToAdd]==Block.PERSONAGGIO){
                Controller.getPressed().add(add);
                moved=add;
            }
            matrice_Principale[17][blockToRemove] = Block.VUOTO;
            matrice_Principale[17][blockToAdd] = Block.TERRA;
            blockToAdd+=add;
            blockToRemove+=add;
            if(blockToAdd==74 || blockToAdd==66){
                int temp=blockToRemove;
                blockToRemove=blockToAdd;
                blockToAdd=temp;
                add=-add;
            }
        }
        blockCount++;
    }

    private void telCheck() {
        if (viewPort.get(newPosition.getFirst().i()).charAt(newPosition.getFirst().j())=='n' && viewPort.get(newPosition.getLast().i()).charAt(newPosition.getLast().j())=='n'){
            if(newPosition.getFirst().j()==126 && (player.getPosition(0).j()!=126 || player.getPosition(0).i()!=15)) {
                tel1=true;
                newPosition.set(0, new Position(10, 130));
                newPosition.set(1, new Position(11, 130));
            } else if(newPosition.getFirst().j()==130 && (player.getPosition(0).j()!=130 || player.getPosition(0).i()!=10)){
                tel2=true;
                newPosition.set(0, new Position(15, 126));
                newPosition.set(1, new Position(16, 126));
            }
        }
    }

    private Block[][] matrice_Principale;

    public Player getPlayer() {
        return player;
    }

    private List<String> viewPort;

    public List<String> getViewPort() {
        return viewPort;
    }

    public void setMatrice_Principale(int i, int j,Block block) {
        matrice_Principale[i][j]=block;
    }


    public int getLiv() {
        return liv;
    }


    public void inizializzaMatricePrincipale(){


        file file = new file();
        try{
            viewPort = file.leggi("src/application/resources/Levels/Livello"+liv+".txt");
        } catch (IOException e){
            e.printStackTrace();
        }
        matrice_Principale =new Block[viewPort.size()][viewPort.getLast().length()];
        for (int i = 0; i<viewPort.size(); i++) {
            String riga = viewPort.get(i);
            for (int j = 0; j<riga.length(); j++) {
                char cella = riga.charAt(j);
                if (cella=='a'){
                    matrice_Principale[i][j] =Block.VUOTO;
                }
                else if (cella=='b') {
                    matrice_Principale[i][j] =Block.TERRA;
                }
                else if (cella=='e') {
                    matrice_Principale[i][j] = Block.MURO;
                }
                else if (cella=='f'){
                    matrice_Principale[i][j] = Block.ERBA;
                }
                else if (cella=='g'){
                    matrice_Principale[i][j] = Block.SPECIALE;
                }
                else if (cella=='h'){
                    matrice_Principale[i][j] = Block.TUBO;
                }
                else if (cella=='i'){
                    matrice_Principale[i][j] = Block.BARILE;
                }
                else if (cella=='j'){
                    matrice_Principale[i][j] = Block.FINE;
                }
                else if (cella=='k'){
                    matrice_Principale[i][j] = Block.PORTALE;
                }
                else if (cella=='l'){
                    matrice_Principale[i][j] = Block.PONTE;
                }
                else if (cella=='m'){
                    matrice_Principale[i][j] = Block.MORTE;
                }

            }
        }

    }


    public void updateDirection(int direction) {
        //CHIAMO LA FUNZIONE PER AGGIORNARE LA DIREZIONE DEL PLAYER
        player.updateDirection(direction);
    }

    private boolean isType(int i,int j,Block b) {
        if(isValidPosition(i,j))
            return matrice_Principale[i][j] == b;
        return false;
    }
    public  void moveNemico(Enemy enemy) {

    LinkedList<Position> newPosition = enemy.simulateMove();

        int controllo = 0 ;
        int corpo_i= newPosition.getFirst().i();
        int corpo_j= newPosition.getFirst().j();

        int direction = enemy.getDirection();


        if(!enemy.getCoordinate().equals(newPosition) )
        {
            if (corpo_j + direction<11  || !isBlocco(corpo_i + 1, corpo_j )  || isBlocco(corpo_i, corpo_j ) || isNemico(corpo_i,corpo_j) ||isNemico(corpo_i, corpo_j + direction ) || (isPlayer(corpo_i,corpo_j) && getPlayer().getProtezione()>0)) {
                enemy.setDirection(-direction);
                controllo=1;
            }
        }

        int count =0;

        if(controllo==0)
        {
            // verifca newPosition sia valida

            for (Position p : newPosition) {
                // isValidPosition controlla il range del mondo
                if(isValidPosition(p.i(),p.j()) && !isBlocco(p.i(),p.j()) && !isMorte(p.i()+1,p.j()) ) {
                    count++;

                }
            }
        }



        if (count==newPosition.size()) {
            if(!enemy.getCoordinate().equals(newPosition) )
            {
                for (int k=0; k<enemy.getCoordinate().size();k++)
                {
                    matrice_Principale[enemy.getPosition(k).i()][enemy.getPosition(k).j()] = Block.VUOTO;
                }
            }


            switch (enemy) {
                case Skeleton skeleton -> skeleton.moveNemico();
                case Creeper creeper -> creeper.moveNemico();
                case MiniZombie miniZombie -> miniZombie.moveNemico();
                default -> {
                }
            }

                //aggiorniamo nella matrice principale la nuova posizione dei nemici
            for(int k=0; k<enemy.getCoordinate().size();k++) {
                switch (enemy) {
                    case Skeleton ignored2 ->
                            matrice_Principale[newPosition.get(k).i()][newPosition.get(k).j()] = Block.SKELETON;
                    case Creeper ignored1 ->
                            matrice_Principale[newPosition.get(k).i()][newPosition.get(k).j()] = Block.CREEPER;
                    case MiniZombie ignored ->
                            matrice_Principale[newPosition.get(k).i()][newPosition.get(k).j()] = Block.MINIZOMBIE;
                    default -> {
                    }
                }

            }


        }


    }

    public LinkedList<ScheduledFuture<?>> getArrayFuture() {
        return arrayFuture;
    }

    public boolean isValidPosition(int i, int j) {
        return i>=0 && i<viewPort.size() && j>=0 && j< viewPort.get(i).length();
    }
    public boolean isWall(int i,int j) {return isType(i,j,Block.MURO);}

    public boolean isPlayer(int i,int j) {return isType(i,j,Block.PERSONAGGIO);}

    public boolean isTerra(int i, int j) { return isType(i,j,Block.TERRA);}
    public boolean isErba(int i, int j) { return isType(i,j,Block.ERBA);}
    public boolean isCoin(int i, int j) { return isType(i,j,Block.MONETA);}
    public boolean isSpeciale(int i, int j) { return isType(i,j,Block.SPECIALE);}
    public boolean isTubo(int i, int j) { return isType(i,j,Block.TUBO);}
    public boolean isBarile(int i, int j) { return isType(i,j,Block.BARILE);}
    public boolean isPonte(int i, int j) { return isType(i,j,Block.PONTE);}
    public boolean isMorte(int i, int j) { return isType(i,j,Block.MORTE);}
    public boolean isUsato(int i, int j) { return isType(i,j,Block.USATO);}
    public boolean isMiniZombie(int i, int j) { return isType(i,j,Block.MINIZOMBIE);}
    public boolean isCreeper(int i, int j) { return isType(i,j,Block.CREEPER);}
    public boolean isVelocita(int i, int j) { return isType(i,j,Block.VELOCITA);}
    public boolean isScudo(int i, int j) { return isType(i,j,Block.SCUDO);}
    public boolean isLentezza(int i, int j) { return isType(i,j,Block.LENTEZZA);}
    public boolean isVita(int i, int j) { return isType(i,j,Block.VITA);}

    public boolean isFreccia(int i, int j) { return isType(i,j,Block.FRECCIA);}
    public boolean isFireball(int i, int j) { return isType(i,j,Block.FIREBALL);}
    public boolean isSkeleton(int i, int j) { return isType(i,j,Block.SKELETON);}

    public boolean isBossFinale(int i, int j) { return isType(i,j,Block.BOSS);}
    public boolean isNemico(int i, int j) {return isSkeleton(i,j) || isCreeper(i,j)  || isMiniZombie(i,j) || isBossFinale(i,j) ;}

    public boolean isBlocco(int i, int j){return isWall(i,j) || isErba(i,j) || isTerra(i,j) || isSpeciale(i,j) || isTubo(i,j) || isBarile(i,j) || isPonte(i,j) || isUsato(i,j);}

    public void removePonte() {
        for(int j=255; j<268 ; j++)
        {
            matrice_Principale[14][j] = Block.VUOTO;
        }
    }

    boolean directionBoss = false;

    public boolean isDirectionBoss() {
        return directionBoss;
    }

    public void directionBoss() {
        directionBoss = true;
    }
}
