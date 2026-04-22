package application.controller;

import application.GameStatus;
import application.model.Game;
import application.model.Settings;
import application.view.GamePanel;

import java.awt.event.*;
import java.util.*;

public class Controller implements KeyListener, MouseListener {


    private static int tipo=0;

    private final GamePanel gamePanel;

    public static int getTipo() {
        return tipo;
    }

    public static void setTipo(int tipo){
        Controller.tipo =tipo;
    }

    public Controller(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private static final Set<Integer> pressed=new HashSet<>();

    public static Set<Integer> getPressed() {
        return pressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private final int[][] comandi= {{KeyEvent.VK_LEFT, KeyEvent.VK_A}, {KeyEvent.VK_RIGHT, KeyEvent.VK_D}, {KeyEvent.VK_SPACE, KeyEvent.VK_SPACE}, {KeyEvent.VK_ESCAPE, KeyEvent.VK_ESCAPE}};

    @Override
    public void keyPressed(KeyEvent e) {
        GameStatus status = Game.getGameStatus();
        if(status==GameStatus.IN_GAME) {
            if(gamePanel.getWorld().getPlayer().getProgresso()!=(gamePanel.getWorld().getViewPort().getFirst().length()-Settings.Filtro_Size_Colonna)) {
                int direction;
                if(e.getKeyCode()==comandi[0][tipo]){
                    direction = Settings.MOVE_LEFT;
                    pressed.add(Settings.MOVE_LEFT);
                }
                else if(e.getKeyCode()==comandi[1][tipo]) {
                    direction = Settings.MOVE_RIGHT;
                    pressed.add(Settings.MOVE_RIGHT);
                }
                else if(e.getKeyCode()==comandi[2][tipo]) {
                    direction = Settings.JUMP;
                    pressed.add(Settings.JUMP);
                }
                else if(e.getKeyCode()==comandi[3][tipo]) {
                    direction = Settings.PAUSE;
                }
                else direction = Settings.NOT_MOVING;

                if (direction == Settings.PAUSE)
                    Game.setGameStatus(GameStatus.PAUSE);

                else if (direction != Settings.NOT_MOVING) {

                    Game.getInstance().setDirection(direction);

                }

            }
        }
        else if(status==GameStatus.PAUSE){
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
            {

                Game.setGameStatus(GameStatus.IN_GAME);
                gamePanel.getWorld().getLevel().resumeAllEnemies();
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameStatus status = Game.getGameStatus();
        if (status == GameStatus.IN_GAME || status == GameStatus.PAUSE || status == GameStatus.WIN) {
            if (e.getKeyCode()==comandi[0][tipo]) pressed.remove(Settings.MOVE_LEFT);
            else if (e.getKeyCode()==comandi[1][tipo]) pressed.remove(Settings.MOVE_RIGHT);
            else if (e.getKeyCode()==comandi[2][tipo]) pressed.remove(Settings.JUMP);
        }
    }


    public void update(){
        // AL primo avvio crea l'istanza Game
        // AGGIONRA IL MODEL
        Game.getInstance().update();

        //AGGIONRA LA VIEW
        gamePanel.update();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.select();
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
