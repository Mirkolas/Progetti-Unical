package application.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Level {

    private final List<LinkedList<Position>> coordinateMiniZombi = new LinkedList<>();
    private final List<LinkedList<Position>> coordinateCreeper = new LinkedList<>();
    private final List<LinkedList<Position>> coordinateSkeleton = new LinkedList<>();
    private final LinkedList<Position> coordinateBossFinale = new LinkedList<>();
    private final LinkedList<Position> coordinatePlayer = new LinkedList<>();
    private final List<Enemy> miniZombis = new LinkedList<>();
    private final List<Enemy> creepers = new LinkedList<>();

    private final List<Enemy> skeletons = new LinkedList<>();

    private BossFinale bossFinale = null;

    private ScheduledFuture<?> futureBossFinale ;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final LinkedList<Enemy> arrayNemici = new LinkedList<>();

    private final LinkedList<Integer> indiciNemiciMorti = new LinkedList<>();
    private final LinkedList<ScheduledFuture<?>> arrayFuture = new LinkedList<>();
    private final World world;

    public Level(World world, int livello) {
        this.world = world;

        coordinatePlayer.add(initializePlayer(livello).getFirst());
        coordinatePlayer.add(initializePlayer(livello).getLast());

        inizializzaCoordinateNemici(livello);
        inizializzaNemici();
        scheduleEnemies();
    }

    private void inizializzaCoordinateNemici(int livello) {
        int[][] positionsMiniZombi = {};
        int[][] positionsCreeper = {};
        int[][] positionsSkeleton = {};
        int[][] positionsBossFinale;



        if (livello == 1) {
            positionsMiniZombi = new int[][]{
                    {16, 22}, {16, 40}, {16, 51}, {16, 53}, {16, 97}, {16, 99},
                    {16, 114}, {16, 124}, {16, 128}, {16, 130},{16,174},{16,176}
            };

            positionsCreeper = new int[][]{{16, 106}, {15, 106},{16,146},{15,146}};
        } else if (livello == 2) {

            positionsMiniZombi = new int[][]{
                    {16, 36}, {16, 48}, {16, 56}, {16, 92}, {16, 96},
                    {16, 152}, {16, 155}, {16, 158}, {11, 139}
            };

            positionsCreeper = new int[][]{{16, 106}, {15, 106},{16, 25}, {15, 25},{16, 28}, {15, 28},{16, 65}, {15, 65},{16, 101}, {15, 101},
                    {16, 149}, {15, 149},{16, 168}, {15, 168},{12, 169}, {11, 169},{8, 152}, {7, 152}};

            positionsSkeleton = new int[][]{{12, 113}, {11, 113},{16, 116}, {15, 116}};
        }

        else if  (livello == 3)
        {
            positionsMiniZombi = new int[][]{
                    {16, 31}, {16, 42}, {16, 57}, {16, 60}, {16, 102}, {16, 123},
                    {16, 126}
            };

            positionsCreeper = new int[][]{{16, 15}, {15, 15},{13,87},{12,87},{16,116},{15,116},{16, 133}, {15, 133},{16, 136}, {15, 136},{16, 146}, {15, 146},{16, 150}, {15, 150}};

            positionsSkeleton = new int[][]{{16,35},{15,35},{16,109},{15,109},{16, 158}, {15, 158},{16, 240}, {15, 240}};

            positionsBossFinale = new int[][]{
                    {5,257},{5,258},{5,259},{5,260},{5,261}, {5,262},{5,263},{5,264},
                    {6,257}                                                 ,{6,264},
                    {7,257},                                                 {7,264},
                    {8,257},{8,258},{8,259},{8,260},{8,261}, {8,262},{8,263},{8,264},
            };

            for (int[] ints : positionsBossFinale) {
                coordinateBossFinale.add(new Position(ints[0], ints[1]));
            }

            bossFinale = new BossFinale(coordinateBossFinale,this.world);
            futureBossFinale = executorService.scheduleAtFixedRate(
                    bossFinale, 500, 100, TimeUnit.MILLISECONDS
            );



        }



        for (int i = 0; i < positionsCreeper.length; i += 2) {
            LinkedList<Position> creeperPositions = new LinkedList<>();
            creeperPositions.add(new Position(positionsCreeper[i][0], positionsCreeper[i][1]));
            creeperPositions.add(new Position(positionsCreeper[i + 1][0], positionsCreeper[i + 1][1]));
            coordinateCreeper.add(creeperPositions);
        }

        for (int i = 0; i < positionsSkeleton.length; i += 2) {
            LinkedList<Position> skeletonPositions = new LinkedList<>();
            skeletonPositions.add(new Position(positionsSkeleton[i][0], positionsSkeleton[i][1]));
            skeletonPositions.add(new Position(positionsSkeleton[i + 1][0], positionsSkeleton[i + 1][1]));
            coordinateSkeleton.add(skeletonPositions);
        }


        for (int[] pos : positionsMiniZombi) {
            LinkedList<Position> miniZombiPositions = new LinkedList<>();
            miniZombiPositions.add(new Position(pos[0], pos[1]));
            coordinateMiniZombi.add(miniZombiPositions);
        }
    }

    private List<Position> initializePlayer(int liv) {
        if (liv == 1 || liv == 2) {
            return List.of(new Position(15, 5), new Position(16, 5));
        }
        return List.of(new Position(9, 1), new Position(10, 1));

    }

    private void inizializzaNemici() {
        // MINI ZOMBI
        for (LinkedList<Position> positions : coordinateMiniZombi) {
            Enemy miniZombi = new MiniZombie(positions, this.world);
            miniZombis.add(miniZombi);
            arrayNemici.add(miniZombi);
        }

        // CREEPER
        for (LinkedList<Position> positions : coordinateCreeper) {
            Enemy creeper = new Creeper(positions, this.world);
            creepers.add(creeper);
            arrayNemici.add(creeper);
        }

        // SCHELETRI
        for (LinkedList<Position> positions : coordinateSkeleton) {
            Enemy skeleton = new Skeleton(positions, this.world);
            skeletons.add(skeleton);
            arrayNemici.add(skeleton);
        }
    }

    private void scheduleEnemies() {
        // MINI ZOMBI
        for (Enemy miniZombi : miniZombis) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) miniZombi, 500, 300, TimeUnit.MILLISECONDS
            );

            arrayFuture.add(future);
        }

        // CREEPER
        for (Enemy creeper : creepers) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) creeper, 500, 500, TimeUnit.MILLISECONDS
            );

            arrayFuture.add(future);
        }

        //SCHELETRI
        for (Enemy skeleton : skeletons) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) skeleton, 500, 200, TimeUnit.MILLISECONDS
            );

            arrayFuture.add(future);
        }


    }

    public void pauseAllEnemies() {

        for (ScheduledFuture<?> scheduledFuture : arrayFuture) {

            if (scheduledFuture != null) {
                scheduledFuture.cancel(true); // Cancella l'esecuzione futura e interrompe se attualmente in esecuzione.
            }


        }

        if(world.getLiv()==3)
        {
            if (futureBossFinale != null) {
                futureBossFinale.cancel(true); // Cancella l'esecuzione futura e interrompe se attualmente in esecuzione.
            }
        }

    }

    public void resumeAllEnemies() {
        // Resume MINI ZOMBI
        arrayFuture.clear();

        if(bossFinale!=null)
        {
            futureBossFinale = executorService.scheduleAtFixedRate(
                    bossFinale, 500, 100, TimeUnit.MILLISECONDS
            );

        }

        for (Enemy miniZombi : miniZombis) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) miniZombi, 500, 300, TimeUnit.MILLISECONDS
            );
            arrayFuture.add(future);
        }

        // Resume CREEPER
        for (Enemy creeper : creepers) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) creeper, 500, 500, TimeUnit.MILLISECONDS
            );
            arrayFuture.add(future);
        }

        // Resume SCHELETRI
        for (Enemy skeleton : skeletons) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                    (Runnable) skeleton, 500, 200, TimeUnit.MILLISECONDS
            );
            arrayFuture.add(future);
        }

        for (Integer integer : indiciNemiciMorti) {
            arrayFuture.get(integer).cancel(true);
        }
    }

    public LinkedList<Enemy> getArrayNemici() {
        return arrayNemici;
    }

    public LinkedList<Position> getCoordinatePlayer() {
        return coordinatePlayer;
    }

    public LinkedList<ScheduledFuture<?>> getArrayFuture() {
        return arrayFuture;
    }

    public void setNemiciMorti(int i) {
        indiciNemiciMorti.add(i);
    }
}

