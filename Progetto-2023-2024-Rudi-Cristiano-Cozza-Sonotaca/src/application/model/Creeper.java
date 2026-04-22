package application.model;
import application.audio.Sound;

import java.util.LinkedList;

public class Creeper extends Enemy implements Runnable  {
    private LinkedList<Position> coordinate;
    private final World world;
    public Creeper(LinkedList<Position> coordinate, World world){
        super(coordinate,world);
        this.coordinate=coordinate;
        this.world=world;
    }

    public LinkedList<Position> getCoordinate() {
        return coordinate;
    }

    private int esplosione=0;

    public int getEsplosione() {
        return esplosione;
    }

    public LinkedList<Position> simulateMove()
    {

        if(!world.getPlayer().getCoordinatePlayer().isEmpty()) {
            Position testaPersonaggio=new Position(world.getPlayer().getCoordinatePlayer().getFirst().i(),world.getPlayer().getCoordinatePlayer().getFirst().j());
            if(testaPersonaggio.i()<=coordinate.getLast().i()+4 && testaPersonaggio.i()>=coordinate.getLast().i()-3 && testaPersonaggio.j()<=coordinate.getLast().j()+3 && testaPersonaggio.j()>=coordinate.getLast().j()-3){
                esplosione+=1;
            }
            else if(esplosione>0) esplosione=0;
        }

        if(esplosione==0)
        {
            return super.simulateMove();
        }

        else
            return coordinate;

    }


    public void moveNemico(){


        if(esplosione>5){
            Sound e = new Sound("explode.wav");
            e.play();


            if(world.getPlayer().getProtezione()==0)
            {

                world.getPlayer().killPlayer();
            }
            esplosione=0;
        }



        if(esplosione==0)
        {
            coordinate = super.move();
        }

    }

    @Override
    public void run() {
        world.moveNemico(this);
    }

}
