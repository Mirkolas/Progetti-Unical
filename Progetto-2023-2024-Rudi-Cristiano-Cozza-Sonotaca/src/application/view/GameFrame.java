package application.view;


import application.model.Settings;
import javax.swing.*;
import java.awt.*;

public class GameFrame {

    private final GamePanel panel;


    public GameFrame(GamePanel gamePanel) {
        this.panel = gamePanel;

    }

    public void showWindow() {
        JFrame frame = new JFrame();
        frame.setSize(Settings.WINDOW_SIZE_X, Settings.WINDOW_SIZE_Y);
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
