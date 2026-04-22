package application.model;

import application.Block;

import application.GameStatus;
import application.audio.Sound;
import application.controller.Controller;
import java.util.LinkedList;
import java.util.Random;


public class Player {

    private final World world;

    private final Sound morte = new Sound("death.wav");

    private final Sound danno = new Sound("damage.wav");

    public Sound getMorte() {
        return morte;
    }

    private final LinkedList<Enemy> nemici;

    Position getPosition(int i)
    {
        return coordinatePlayer.get(i);
    }

    private int lives;


    public int getLives() {
        return lives;
    }

    private boolean isJumping=false;
    private boolean isFalling=false;

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    private int progresso=0;

    private int preDirection=Settings.MOVE_RIGHT;

    public int getPreDirection() {
        return preDirection;
    }

    private LinkedList<Position> coordinatePlayer;

    public LinkedList<Position> getCoordinatePlayer() {
        return coordinatePlayer;
    }

    private int direction = Settings.NOT_MOVING;

    public Player(LinkedList<Position> coordinate,LinkedList<Enemy> nemici ,World world) {

        this.world=world;
        this.coordinatePlayer = coordinate;
        this.nemici = nemici;

    }

    private int coins=0;

    public int getCoins() {
        return coins;
    }
    public static int cont=0;
    private int velC=150;
    private int lenC=150;
    private boolean velocita=false;
    private boolean scudo=false;

    public boolean getScudo() {
        return scudo;
    }

    private boolean lentezza=false;

    public boolean getVelocita() {
        return velocita;
    }

    public boolean getLentezza() {
        return lentezza;
    }

    public void setVelocita(boolean velocita) {
        this.velocita = velocita;
    }

    public void setLentezza(boolean lentezza) {
        this.lentezza = lentezza;
    }

    public int getVelC() {
        return velC;
    }

    public int getLenC() {
        return lenC;
    }

    public void setVelC(int velC) {
        this.velC = velC;
    }

    public void setLenC(int lenC) {
        this.lenC = lenC;
    }

    private Sound abilita;

    public void move(){

        if(Controller.getPressed().contains(Settings.JUMP) && Game.getInstance().getWorld().isBlocco(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j()) && cont==0){
            Sound salta = new Sound("jump.wav");
            salta.play();
        }

        //qui aggiorniamo la linked list con le nuove coordinate che abbiamo precedentemnete controllato nel movePlayer in world
        coordinatePlayer = simulateMove();

        if(coordinatePlayer.getLast().i()+1>=world.getViewPort().size() || world.isMorte(coordinatePlayer.getLast().i()+1,coordinatePlayer.getLast().j()) || world.isNemico(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j()) || world.isNemico(coordinatePlayer.getLast().i(), coordinatePlayer.getLast().j()) || world.isFreccia(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j()) || world.isFireball(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j() ) ||  world.isFreccia(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j()) ||  world.isFireball(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j()) ) {

            if(world.getLiv()==1 && world.isMorte(coordinatePlayer.getLast().i()+1,coordinatePlayer.getLast().j())){
                Sound acqua=new Sound("water.wav");
                acqua.play();
            } else if (world.getLiv()==2 && world.isMorte(coordinatePlayer.getLast().i()+1,coordinatePlayer.getLast().j())){
                Sound lava=new Sound("fire.wav");
                lava.play();
            }

            killPlayer();
        }


        if(world.isCoin(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j()) ||  world.isCoin(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j())){
            Sound coin = new Sound("coin.wav");
            coin.play();
            coins++;
            if(coins==5){
                coins=0;
                if(lives<4) lives++;
            }
        }
        else if(world.isVelocita(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j()) || world.isVelocita(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j()))

        {
            abilita = new Sound("speed.wav");
            abilita.play();
            if(lentezza){
                lentezza=false;
                lenC=150;
            }
            velocita=true;
            if(velC!=150) velC=150;
            world.setAb(1);
        }
        else if(world.isScudo(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j())  || world.isScudo(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j())){
            abilita = new Sound("shield.wav");
            abilita.play();
            for(int i=0;i<50;i++) abilita.incrementVolume();
            scudo=true;
        }
        else if(world.isLentezza(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j()) || world.isLentezza(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j())){
            abilita = new Sound("slowness.wav");
            abilita.play();
            if(velocita){
                velocita=false;
                velC=150;
            }
            lentezza=true;
            if(lenC!=150) lenC=150;
            world.setAb(4);
        }
        else if(world.isVita(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j()) || world.isVita(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j())){

            if(getLives()!=4){
                abilita = new Sound("life.wav");
                abilita.play();
                setLives(getLives()+1);
            }
        }

        if(direction!=Settings.NOT_MOVING && direction!=Settings.JUMP) preDirection=direction;
        if(protezione>0) protezione--;
        if(Controller.getPressed().contains(Settings.MOVE_RIGHT) && progresso<(world.getViewPort().getFirst().length() -Settings.Filtro_Size_Colonna) && getPosition(0).j()>=Settings.Filtro_Size_Colonna+progresso-15) progresso+=(getPosition(0).j()-(Settings.Filtro_Size_Colonna+progresso-15));
        else if(Controller.getPressed().contains(Settings.MOVE_LEFT) && progresso>0 && getPosition(0).j()<=Settings.Filtro_Size_Colonna+progresso-21) progresso-=((Settings.Filtro_Size_Colonna+progresso-21)-getPosition(0).j());

        makeSounds();
        checkJump();


    }
    public void checkAbilities(){
        if(velocita){
            velC--;
            if(velC<=0){
                velocita=false;
                velC=150;
                world.setAb(2);
            }
        }
        else if(lentezza){
            lenC--;
            if(lenC<=0){
                lentezza=false;
                lenC=150;
                world.setAb(2);
            }
        }
    }
    private int protezione=0;

    public int getProtezione() {
        return protezione;
    }

    public void setProtezione(int protezione) {
        this.protezione = protezione;
    }
    private void makeSounds() {
        if(!Controller.getPressed().isEmpty() ||  isFalling() ) {
            if(world.getViewPort().get(coordinatePlayer.getLast().i()).charAt(coordinatePlayer.getLast().j())=='n' && world.getViewPort().get(coordinatePlayer.getFirst().i()).charAt(coordinatePlayer.getFirst().j())=='n'){
                Sound tel = new Sound("tel.wav");
                tel.play();
            }
            Sound cammina;
            if (world.isErba(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("grass.wav");
                cammina.play();
            } else if (world.isTerra(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("dirt.wav");
                cammina.play();
            } else if (world.isWall(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("wood.wav");
                cammina.play();
            } else if (world.isSpeciale(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("sand.wav");
                cammina.play();
            } else if(world.isTubo(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("tubo.wav");
                cammina.play();
            } else if(world.isBarile(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("barrel.wav");
                cammina.play();
            } else if(world.isPonte(coordinatePlayer.getLast().i() + 1, coordinatePlayer.getLast().j())) {
                cammina = new Sound("wood.wav");
                cammina.play();
            }
        }
    }

    private void checkJump() {
        if(!isJumping() && !isFalling() && !world.isBlocco(coordinatePlayer.getLast().i()+1,coordinatePlayer.getLast().j())) setFalling(true);

        if(isJumping()){
            if(cont<3){
                if(world.getViewPort().get(coordinatePlayer.getFirst().i()-1).charAt(coordinatePlayer.getFirst().j())=='q' && !world.isUsato(coordinatePlayer.getFirst().i()-1,coordinatePlayer.getFirst().j())) {
                    abilita = new Sound("ability.wav");
                    abilita.play();
                    world.setMatrice_Principale(coordinatePlayer.getFirst().i() - 1, coordinatePlayer.getFirst().j(), Block.USATO);
                    generateRandomBlock();
                }
                if(world.isBlocco(coordinatePlayer.getFirst().i()-1,coordinatePlayer.getFirst().j())){
                    Sound sbatte = new Sound("hit.wav");
                    sbatte.play();
                    if(world.isSpeciale(coordinatePlayer.getFirst().i()-1,coordinatePlayer.getFirst().j())){
                        abilita = new Sound("ability.wav");
                        abilita.play();
                        world.setMatrice_Principale(coordinatePlayer.getFirst().i()-1,coordinatePlayer.getFirst().j(), Block.USATO);
                        generateRandomBlock();
                    }
                    setJumping(false);
                    setFalling(true);
                }
                else cont++;
            }
            else{
                setJumping(false);
                setFalling(true);
            }
        }

        if(isFalling()){
            if(world.isBlocco(coordinatePlayer.getLast().i()+1,coordinatePlayer.getLast().j())) {
                setFalling(false);
                cont = 0;
            }

            if(world.isNemico(coordinatePlayer.getLast().i()+1,  coordinatePlayer.getLast().j()))
            {
                killEnemy(coordinatePlayer.getLast().i()+1,  coordinatePlayer.getLast().j());
                isJumping=true;
                cont=1;
            }

            if(world.isFreccia(coordinatePlayer.getLast().i()+1,  coordinatePlayer.getLast().j()))
            {
                killPlayer();
            }
        }
    }

    public void resetAbilities(){
        if(world.getPlayer().getVelocita()){
            world.getPlayer().setVelocita(false);
            world.getPlayer().setVelC(150);
            world.setAb(2);
        } else if(world.getPlayer().getLentezza()){
            world.getPlayer().setLentezza(false);
            world.getPlayer().setLenC(150);
            world.setAb(2);
        }
    }
    private void generateRandomBlock(){
        Random r=new Random();
        int n=r.nextInt(0,10);
        if(n<=5) world.setMatrice_Principale(coordinatePlayer.getFirst().i()-2,coordinatePlayer.getFirst().j(), Block.MONETA);
        else if(n==6) world.setMatrice_Principale(coordinatePlayer.getFirst().i()-2,coordinatePlayer.getFirst().j(), Block.VELOCITA);
        else if(n==7) world.setMatrice_Principale(coordinatePlayer.getFirst().i()-2,coordinatePlayer.getFirst().j(), Block.SCUDO);
        else if(n==8) world.setMatrice_Principale(coordinatePlayer.getFirst().i()-2,coordinatePlayer.getFirst().j(), Block.LENTEZZA);
        else world.setMatrice_Principale(coordinatePlayer.getFirst().i()-2,coordinatePlayer.getFirst().j(), Block.VITA);

    }
    public int getProgresso() {
        return progresso;
    }

    public void updateDirection(int direction) {

        this.direction = direction;


    }


    public LinkedList<Position> simulateMove() {
        LinkedList<Position> newCoordinate = new LinkedList<>();

        int testa_i = coordinatePlayer.getFirst().i();
        int testa_j = coordinatePlayer.getFirst().j();

        int corpo_i = coordinatePlayer.get(1).i();
        int corpo_j = coordinatePlayer.get(1).j();


        if (Controller.getPressed().contains(Settings.JUMP)) {
            if (!isJumping && !isFalling) {
                isJumping = true;
            }
        }



        if(isJumping){
            testa_i = testa_i - 1;
            corpo_i = corpo_i - 1;
        }
        else if(isFalling){
            testa_i = testa_i + 1;
            corpo_i = corpo_i + 1;
        }
        if (Controller.getPressed().contains(Settings.MOVE_LEFT)){
            if((!isJumping && !isFalling) || (!world.isBlocco(testa_i, testa_j - 1) && !world.isBlocco(corpo_i, corpo_j - 1) && !world.isBlocco(testa_i - 1, testa_j - 1) && world.isValidPosition(testa_i, testa_j - 1)/* && world.isValidPosition(corpo_i, corpo_j - 1) && world.isValidPosition(testa_i - 1, testa_j - 1)*/)) {
                testa_j = testa_j - 1;
                corpo_j = corpo_j - 1;
            }
        }
        if (Controller.getPressed().contains(Settings.MOVE_RIGHT)){
            if((!isJumping && !isFalling) || (!world.isBlocco(testa_i, testa_j + 1) && !world.isBlocco(corpo_i, corpo_j + 1) && !world.isBlocco(testa_i - 1, testa_j + 1) && world.isValidPosition(testa_i, testa_j + 1)/* && world.isValidPosition(corpo_i, corpo_j + 1) && world.isValidPosition(testa_i - 1, testa_j + 1)*/)) {
                testa_j = testa_j + 1;
                corpo_j = corpo_j + 1;
            }
        }
        if(world.isTel1()){
            newCoordinate.add(new Position(10, 130));
            newCoordinate.add(new Position(11, 130));
            world.setTel1(false);
            return newCoordinate;
        }
        else if(world.isTel2()){
            newCoordinate.add(new Position(15, 126));
            newCoordinate.add(new Position(16, 126));
            world.setTel2(false);
            return newCoordinate;
        }

        newCoordinate.add(new Position(testa_i,testa_j));
        newCoordinate.add(new Position(corpo_i,corpo_j));


        return newCoordinate;
    }

    public void setScudo(boolean b) {
        scudo=b;
    }

    public void killPlayer(){

        if(!scudo) {
            resetAbilities();


            LinkedList<Position> coordinPlayer = new LinkedList<>();
            coordinPlayer.add(world.getLevel().getCoordinatePlayer().getFirst());
            coordinPlayer.add( world.getLevel().getCoordinatePlayer().getLast());


            danno.play();

            if (lives == 1) {
                morte.play();
            }

            world.setMatrice_Principale(coordinatePlayer.getFirst().i(),coordinatePlayer.getFirst().j(),Block.VUOTO);
            world.setMatrice_Principale(coordinatePlayer.getLast().i(),coordinatePlayer.getLast().j(),Block.VUOTO);


            isFalling = false;
            isJumping = false;
            Player.cont=0;
            setCoordinate(coordinPlayer);
            progresso=0;
            if (lives > 0) setLives(world.getPlayer().getLives()-1);
            if (lives==0)
            {
                Game.setGameStatus(GameStatus.GAME_OVER);
                world.getLevel().pauseAllEnemies();
            }

            world.updateDirection(Settings.MOVE_RIGHT);
            world.setMorte(true);
        }

        else
        {
            world.getPlayer().setProtezione(10);
            setScudo(false);
        }

    }

    public void setLives(int lives) {
        this.lives = lives;
    }


    void killEnemy(int nemico_i, int nemico_j) {

        for (int i = 0; i < nemici.size(); i++) {

            // nei nemici formati da 2 blocchi getLast è la testa mentre nei nemici di un blocco non fa differenza
            if(nemici.get(i).getCoordinate().getLast().i()==nemico_i && nemici.get(i).getCoordinate().getLast().j()==nemico_j)
            {

                Sound morteNemico;
                if(nemici.get(i) instanceof  Skeleton)
                {
                    morteNemico = new Sound("deathS.wav");
                    morteNemico.play();
                    ((Skeleton)nemici.get(i)).removeArrows();
                }

                else if(nemici.get(i) instanceof  Creeper)
                {
                    morteNemico = new Sound("deathC.wav");
                    morteNemico.play();

                }
                else if(nemici.get(i) instanceof  MiniZombie)
                {
                    morteNemico = new Sound("deathZ.wav");
                    morteNemico.play();

                }


                if (world.getArrayFuture().get(i) != null) {
                    world.getArrayFuture().get(i).cancel(true); // Cancella l'esecuzione futura e interrompe se attualmente in esecuzione.
                    world.getLevel().setNemiciMorti(i);
                }



                for(int j=0 ; j<nemici.get(i).getCoordinate().size();j++)
                {
                    world.setMatrice_Principale(nemici.get(i).getCoordinate().get(j).i(),nemici.get(i).getCoordinate().get(j).j(),Block.VUOTO);
                }
            }
        }
    }

    public void setCoordinate(LinkedList<Position> coordinate) {
        this.coordinatePlayer = coordinate;
    }
}
