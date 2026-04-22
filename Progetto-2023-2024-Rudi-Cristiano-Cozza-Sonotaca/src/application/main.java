package application;

import application.controller.Controller;
import application.model.LevelProgress;
import application.view.GameFrame;
import application.view.GamePanel;
import application.view.ImmaginiGioco;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException{
        ImmaginiGioco immaginigioco = new ImmaginiGioco();
        LevelProgress progress=new LevelProgress();
        GamePanel gamePanel = new GamePanel(immaginigioco,progress);
        Controller controllerPlayer = new Controller(gamePanel);
        gamePanel.setController(controllerPlayer);
        GameLoop gameLoop = new GameLoop(controllerPlayer);
        gameLoop.inizioGioco();
        GameFrame frame = new GameFrame(gamePanel);
        frame.showWindow();
    }
}
