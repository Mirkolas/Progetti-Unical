package application;

import application.Controller.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {
    private final Controller ControllerPersonaggio;
    public GameLoop(Controller ControllerPersonaggio) {
        this.ControllerPersonaggio = ControllerPersonaggio;
    }
    private ScheduledExecutorService executorService = null;
    public void inizioGioco() {
        if(executorService !=null) return;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(ControllerPersonaggio::LivelloSuperato,0,66*2, TimeUnit.MILLISECONDS);
    }
}
