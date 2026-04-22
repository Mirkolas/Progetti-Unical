package application.Controller;

import application.Stato;
import application.Model.*;
import application.View.GamePanel;


import java.awt.event.*;
import java.util.*;




public class Controller implements KeyListener, MouseListener {

    private final GamePanel gamePanel;

    public Controller(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private static final Set<Integer> premuti =new HashSet<>();

    public static Set<Integer> getPremuti() {
        return premuti;
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        Stato status = Game.getStati();
        if(status== Stato.IN_GIOCO) {
                int direzione = 0;
                if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    direzione = ImpostazioniDim.MUOVI_SINISTRA;
                    premuti.add(ImpostazioniDim.MUOVI_SINISTRA);
                }
                else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
                    direzione = ImpostazioniDim.MUOVI_DESTRA;
                    premuti.add(ImpostazioniDim.MUOVI_DESTRA);
                }
                else if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
                    direzione = ImpostazioniDim.SHOOT;
                    premuti.add(ImpostazioniDim.SHOOT);
                }
                else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
                    direzione = ImpostazioniDim.MUOVI_DESTRA;
                    premuti.add(ImpostazioniDim.MUOVI_DESTRA);
                }
                else if(e.getKeyCode()==KeyEvent.VK_N) {
                    direzione = ImpostazioniDim.PULSANTE;
                    premuti.add(ImpostazioniDim.PULSANTE);
                }
                else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
                    direzione = ImpostazioniDim.SALTA;
                    premuti.add(ImpostazioniDim.SALTA);
                }
                else if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                    direzione = ImpostazioniDim.PAUSA;
                }
                else direzione = ImpostazioniDim.NON_IN_MOVIMENTO;

                if (direzione == ImpostazioniDim.PAUSA)
                    Game.setGameStatus(Stato.PAUSA);


                else if (direzione != ImpostazioniDim.NON_IN_MOVIMENTO) {

                    Game.getMondo().setDirezione(direzione);

                }

            }

        else if(status== Stato.PAUSA){
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
            {
                Game.setGameStatus(Stato.IN_GIOCO);
                gamePanel.getWorld().getLevel().RiavviaNemici();
            }

        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        Stato status = Game.getStati();
        if (status == Stato.IN_GIOCO || status == Stato.PAUSA || status == Stato.VITTORIA) {
            if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                premuti.remove(ImpostazioniDim.MUOVI_SINISTRA);
            }
            else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
                premuti.remove(ImpostazioniDim.MUOVI_DESTRA);
            }
            else if (e.getKeyCode()==KeyEvent.VK_SPACE) {
                premuti.remove(ImpostazioniDim.SALTA);
            }
            else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
                premuti.remove(ImpostazioniDim.SHOOT);
            }
            else if (e.getKeyCode()==KeyEvent.VK_N) {
                premuti.remove(ImpostazioniDim.PULSANTE);
            }
        }
    }

    public void LivelloSuperato(){
        Game.getMondo().LivelloSuperato();
        gamePanel.LivelloSuperato();
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
