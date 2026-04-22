package application.model;
import javax.swing.*;

public class LevelProgress extends JProgressBar{

    public LevelProgress() {}
    public void setRange(int min,int max){
        this.setMinimum(min);
        this.setMaximum(max);
    }
    public void updateProgress(int progresso){
        this.setValue(progresso);
    }
}
