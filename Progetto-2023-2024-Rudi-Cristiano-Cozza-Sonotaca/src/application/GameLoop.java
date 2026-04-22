package application;

import application.controller.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {
    private final Controller controllerPlayer;

    public GameLoop(Controller controllerPlayer) {
        this.controllerPlayer = controllerPlayer;
    }

    private ScheduledExecutorService executorService = null;

    public void inizioGioco()
    {
        if(executorService !=null) return;

        executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(controllerPlayer::update,0,66, TimeUnit.MILLISECONDS);
        // THREAD CHE GESTISCE L'ESECUZIONE DI GIOCO CON UN TICK OGNI 66 MILLISECOND

    }
}
