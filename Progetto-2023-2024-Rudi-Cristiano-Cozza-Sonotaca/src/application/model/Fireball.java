package application.model;

import application.Block;

public class Fireball {
    private Position pos;

    private final World world;
    private final BossFinale bossFinale;
    public Fireball(BossFinale b,World world, Position p){
        this.bossFinale=b;
        this.world=world;
        pos=p;
    }

    public Position getPos() {
        return pos;
    }

    public void move(){
        int dir = -1;
        if(world.isPlayer(pos.i(), pos.j()-1)){
            bossFinale.getToBeRemoved().add(this);
            world.getPlayer().killPlayer();
        }
        else if(pos.j()+ dir <11 || world.isBlocco(pos.i(), pos.j()+ dir) || world.isNemico(pos.i(), pos.j()+ dir) ||  world.isNemico(pos.i(), pos.j()+ (dir*2) ) || world.isFreccia(pos.i(), pos.j()+ dir)) {
            bossFinale.getToBeRemoved().add(this);
        }
        else if(!world.isBlocco(pos.i(), pos.j()+ dir) && !world.isPlayer(pos.i(), pos.j()+ dir) && !world.isNemico(pos.i(), pos.j()+ dir) && !world.isFireball(pos.i(), pos.j()+ dir)) {
            world.setMatrice_Principale(pos.i(), pos.j(), Block.VUOTO);
            pos = new Position(pos.i(), pos.j() + dir);
            world.setMatrice_Principale(pos.i(), pos.j(), Block.FIREBALL);
        }
    }

}
