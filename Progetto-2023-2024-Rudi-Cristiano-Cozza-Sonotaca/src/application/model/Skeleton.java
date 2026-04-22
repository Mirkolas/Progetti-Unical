package application.model;

import application.Block;

import java.util.*;

public class Skeleton  extends Enemy implements  Runnable {
    private LinkedList<Position> coordinateSkeleton;

    private final World world;

    public Skeleton(LinkedList<Position> coordinate, World world){
        super(coordinate,world);
        this.coordinateSkeleton=coordinate;
        this.world=world;
    }

    public LinkedList<Position> getCoordinate() {
        return coordinateSkeleton;
    }
    int passo=0;
    private final LinkedList<Arrow> arrows=new LinkedList<>();

    public LinkedList<Arrow> getArrows() {
        return arrows;
    }

    private final List<Arrow> toBeRemoved=new ArrayList<>();

    public List<Arrow> getToBeRemoved() {
        return toBeRemoved;
    }

    public LinkedList<Position> simulateMove()   {



        if (passo%4==0)
        {

            return super.simulateMove();
        }

        else
        {
            return coordinateSkeleton;
        }



    }

    public void moveNemico(){

        if (passo%4==0)
        {
            coordinateSkeleton =  super.move();
        }

        moveArrows();

        if(passo%10==0) shootArrow();


        passo+=1;

    }


    private void moveArrows(){
        for(Arrow a:arrows){
            if(a.getTempo()<10){
                a.move();
            }
            else toBeRemoved.add(a);
        }
        for(Arrow a:toBeRemoved){
            world.setMatrice_Principale(a.getPos().i(),a.getPos().j(),Block.VUOTO);
            arrows.remove(a);
        }
        toBeRemoved.clear();
    }
    private void shootArrow(){
        if(!world.isBlocco(coordinateSkeleton.getFirst().i(),coordinateSkeleton.getFirst().j()+direction) && !world.isPlayer(coordinateSkeleton.getFirst().i(),coordinateSkeleton.getFirst().j()+direction)){
            Arrow arrow=new Arrow(this,world,direction,new Position(coordinateSkeleton.getLast().i(), coordinateSkeleton.getLast().j()+direction),0);
            world.setMatrice_Principale(coordinateSkeleton.getLast().i(),coordinateSkeleton.getLast().j(), Block.FRECCIA);
            arrows.add(arrow);
        }
        else if(world.isPlayer(coordinateSkeleton.getFirst().i(),coordinateSkeleton.getFirst().j()+direction)){
            world.getPlayer().killPlayer();
        }
    }

    void removeArrows(){
        for(Arrow a: arrows)
        {
            world.setMatrice_Principale(a.getPos().i(),a.getPos().j(),Block.VUOTO);
        }
        arrows.clear();
    }

    @Override
    public void run() {

        world.moveNemico(this);
    }

}

