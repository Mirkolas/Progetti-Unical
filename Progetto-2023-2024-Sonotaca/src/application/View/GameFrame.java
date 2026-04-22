package application.View;


import application.Model.ImpostazioniDim;
import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private final GamePanel panel;
    public GameFrame(GamePanel gamePanel) {this.panel = gamePanel;}
    public void showWindow() {
        JFrame frame = new JFrame();
        frame.setSize(ImpostazioniDim.Lunghezza_Finestra, ImpostazioniDim.Altezza_Finestra);
        frame.add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        frame.setUndecorated(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = toolkit.getScreenSize();
        frame.setLocation((screenDimension.width-frame.getWidth())/2, (screenDimension.height- frame.getHeight())/2);
        frame.setVisible(true);
    }
}
