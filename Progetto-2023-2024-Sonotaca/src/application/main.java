package application;

import application.Controller.Controller;
import application.View.GameFrame;
import application.View.GamePanel;
import application.View.ImmaginiGioco;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException{
        ImmaginiGioco immaginigioco = new ImmaginiGioco();
        GamePanel gamePanel = new GamePanel(immaginigioco);
        Controller controllerPlayer = new Controller(gamePanel);
        gamePanel.setController(controllerPlayer);
        GameLoop gameLoop = new GameLoop(controllerPlayer);
        gameLoop.inizioGioco();
        GameFrame frame = new GameFrame(gamePanel);
        frame.showWindow();
    }
}
