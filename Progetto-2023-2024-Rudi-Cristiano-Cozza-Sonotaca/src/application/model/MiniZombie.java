package application.model;
import java.util.LinkedList;


public class MiniZombie extends Enemy implements Runnable {

    private LinkedList<Position> coordinate;
    private final World world;

    public MiniZombie(LinkedList<Position> coordinate, World world) {
        super(coordinate, world);
        this.coordinate = coordinate;
        this.world = world;
    }

    public LinkedList<Position> getCoordinate() {
        return coordinate;
    }

    public synchronized LinkedList<Position> simulateMove() {
        return super.simulateMove();
    }

    public synchronized void moveNemico() {
        coordinate = super.move();
    }

    @Override
    public void run() {

        world.moveNemico(this);
    }

}

