package application.model;

import application.Block;

public class Arrow {
    private Position pos;
    private final int dir;
    private int tempo;
    private final World world;
    private final Skeleton skeleton;
    public Arrow(Skeleton s,World world,int dir, Position p, int tempo){
        this.skeleton=s;
        this.dir=dir;
        this.world=world;
        this.tempo=tempo;
        pos=p;
    }

    public Position getPos() {
        return pos;
    }

    public void move(){
        if(world.isPlayer(pos.i(), pos.j()+dir)){
            skeleton.getToBeRemoved().add(this);
            world.getPlayer().killPlayer();
        }
        else if(pos.j()+dir<11 || world.isBlocco(pos.i(), pos.j()+dir) || world.isNemico(pos.i(), pos.j()+dir) || world.isFreccia(pos.i(), pos.j()+dir) || world.isNemico(pos.i(), pos.j()+(dir*2)) ){
            skeleton.getToBeRemoved().add(this);
        }
        else if(!world.isBlocco(pos.i(), pos.j()+dir) && !world.isPlayer(pos.i(), pos.j()+dir) && !world.isNemico(pos.i(), pos.j()+dir) && !world.isFreccia(pos.i(), pos.j()+dir)) {
            world.setMatrice_Principale(pos.i(), pos.j(), Block.VUOTO);
            pos = new Position(pos.i(), pos.j() + dir);
            world.setMatrice_Principale(pos.i(), pos.j(), Block.FRECCIA);
            tempo+=1;
        }
    }
    public int getTempo() {
        return tempo;
    }

    public int getDir() {
        return dir;
    }
}
