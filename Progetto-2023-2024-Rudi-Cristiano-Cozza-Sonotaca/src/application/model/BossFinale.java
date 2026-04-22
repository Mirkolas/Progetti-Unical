package application.model;

import application.Block;
import application.GameStatus;
import application.audio.Sound;
import application.view.GamePanel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BossFinale  implements Runnable {

    private LinkedList<Position> coordinate;
    private final World world;
    private final LinkedList<Fireball> fireballs= new LinkedList<>();
    private final List<Fireball> toBeRemoved=new ArrayList<>();
    public List<Fireball> getToBeRemoved() {
        return toBeRemoved;
    }

    public BossFinale(LinkedList<Position> coordinate, World world) {

        this.coordinate = coordinate; // richiama la super classe
        this.world = world;
    }

    private int direction = 1 ;
    private void moveFireball(){
        for(Fireball f:fireballs){

            f.move();


        }
        for(Fireball a:toBeRemoved){
            world.setMatrice_Principale(a.getPos().i(),a.getPos().j(),Block.VUOTO);
            fireballs.remove(a);
        }
        toBeRemoved.clear();
    }
    private void shootFireball(){
        Random r=new Random();
        int n=r.nextInt(1,5);
        Sound fireballS = new Sound("hit"+n+".wav");
        int volume =  50 - world.getPlayer().getProgresso()/5;
        for(int i=0;i<volume;i++) fireballS.reduceVolume();
        fireballS.play();

        if(!world.isBlocco(coordinate.get(12).i(),coordinate.get(12).j()-1) && !world.isPlayer(coordinate.get(12).i(),coordinate.get(12).j()-1)){
            Fireball fireball=new Fireball(this,world,new Position(coordinate.get(12).i(), coordinate.get(12).j()-1));
            world.setMatrice_Principale(coordinate.get(12).i(),coordinate.get(12).j(), Block.FRECCIA);
            fireballs.add(fireball);
        }
        else if(world.isPlayer(coordinate.get(12).i(),coordinate.get(12).j()+direction)){
            world.getPlayer().killPlayer();
        }
    }

    void removeFireballs(){
        fireballs.clear();
    }



    private LinkedList<Position> simulateMove()
    {
        LinkedList<Position> newCoordinate = new LinkedList<>();

        for(int i =0 ; i <coordinate.size();i++)
        {
            newCoordinate.add(new Position(coordinate.get(i).i()+direction,coordinate.get(i).j()));
        }

        return newCoordinate;


    }


    private void moveBossFinale() {
        LinkedList<Position> newPosition = simulateMove();

        //devo verificare che le nuove posizioni sia valide
        int count = 0;
        for (Position p : newPosition) {
            //is valid position controlliamo sia il range del mondo e sia se andiamo contro muro,oggetti
            if (!world.isBlocco(p.i(), p.j())) {
                count++;

            }

        }

        if(count !=newPosition.size())
        {
            direction = -direction;
        }

        if (count == newPosition.size()) {

            if (!coordinate.equals(newPosition)) {
                for (int k = 0; k < coordinate.size(); k++) {

                    world.setMatrice_Principale(coordinate.get(k).i(), coordinate.get(k).j(), Block.VUOTO);
                }
            }
            move();

            for(int k=0; k<coordinate.size();k++) {


                world.setMatrice_Principale(coordinate.get(k).i(),coordinate.get(k).j(), Block.BOSS);

            }

        }
    }

    private void move()
    {
        if(world.isDirectionBoss() && direction!=1)
        {
         direction=1;
         removeFireballs();
        }

        coordinate = simulateMove();

        for (int i = 0 ; i <coordinate.size();i++)
        {
            if (!world.isValidPosition(coordinate.get(i).i(), coordinate.get(i).j())  )
        {



            GamePanel.getSoundtrack().pause();
            GamePanel.setSoundtrack(null);

            Game.setGameStatus(GameStatus.WIN);

            killBossFinale();


            world.getLevel().pauseAllEnemies();




            return;
        }
            if(world.isPlayer(coordinate.get(i).i(),coordinate.get(i).j()) )
            {
                world.getPlayer().killPlayer();
                break;
            }
        }
    }


    private int passoFireball =0;

    private int passo = 0 ;

    @Override
    public void run() {

        if(passo%40==0 && !world.isDirectionBoss())
        {
            Random r=new Random();
            int n=r.nextInt(1,5);
            Sound dragonSound = new Sound("e"+n+".wav");
            int volume =  50 - world.getPlayer().getProgresso()/5;
            for(int i=0;i<volume;i++) dragonSound.reduceVolume();
            dragonSound.play();
        }

        moveBossFinale();


        if(passoFireball %2==0 ) moveFireball();
        if(passo%16==0 && !world.isDirectionBoss() ) shootFireball();


        passo+=1;
        passoFireball +=1;

    }


    private void killBossFinale(){
        for (int i = 0 ; i<coordinate.size();i++)
        {
            world.setMatrice_Principale(coordinate.get(i).i(),coordinate.get(i).j(),Block.VUOTO);
        }

    }

}

