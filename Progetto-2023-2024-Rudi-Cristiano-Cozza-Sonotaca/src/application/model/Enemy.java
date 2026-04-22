package application.model;

import java.util.LinkedList;



public class Enemy{


    protected int direction=1;

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private final World world;
    private LinkedList<Position> coordinateNemico ;
    public Enemy(LinkedList<Position> coordinate , World world) {

        this.world=world;
        this.coordinateNemico = coordinate;

    }

    public  LinkedList<Position> move(){

        coordinateNemico = simulateMove();

        if(world.isPlayer(coordinateNemico.getLast().i(),coordinateNemico.getLast().j())  && world.getPlayer().getProtezione()==0 )
        {
            world.getPlayer().killPlayer();

        }


        return coordinateNemico;

    }


    public  LinkedList<Position> simulateMove()   {

        LinkedList<Position> newCoordinate = new LinkedList<>();

        int corpo_i= coordinateNemico.getFirst().i();
        int corpo_j= coordinateNemico.getFirst().j();




        //nemico di un blocco
        if(coordinateNemico.size()==1)
        {


            if(direction==Settings.MOVE_LEFT)
            {
                corpo_j--;
            }
            if(direction==Settings.MOVE_RIGHT)
            {
                corpo_j++;
            }

            newCoordinate.add((new Position(corpo_i,corpo_j)));
        }

        else if (coordinateNemico.size()==2)
        {
            int testa_i = coordinateNemico.getLast().i();
            int testa_j = coordinateNemico.getLast().j();

            if(direction==Settings.MOVE_LEFT)
            {
                corpo_j--;
                testa_j--;
            }
            if(direction==Settings.MOVE_RIGHT)
            {
                corpo_j++;
                testa_j++;
            }

            newCoordinate.add((new Position(corpo_i,corpo_j)));
            newCoordinate.add((new Position(testa_i,testa_j)));



        }

        return newCoordinate;
    }


    public int getDirection() {
        return direction;
    }

    public LinkedList<Position> getCoordinate() {
        return coordinateNemico;
    }

    Position getPosition(int i)
    {
        return coordinateNemico.get(i);
    }
}
