package application.Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class Sound {
    private Clip clip;
    public Sound(String name) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/resources/sounds/" + name)));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            clip = null;
        }
    }
    public void loop() {
        if (clip != null)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void play() {
        if (clip != null) {
            if (clip.getFramePosition() == clip.getFrameLength())
                clip.setFramePosition(0);
            clip.start();
        }
    }
    public void pause() {
        if (clip != null)
            clip.stop();
    }
    public void incrementVolume() {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float value = gainControl.getValue();
            value += 1.0f;
            if (value <= gainControl.getMaximum())
                gainControl.setValue(value);
        }
    }

    public void reduceVolume() {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float value = gainControl.getValue();
            value -= 1.0f;
            if (value >= gainControl.getMinimum())
                gainControl.setValue(value);
        }
    }
}


