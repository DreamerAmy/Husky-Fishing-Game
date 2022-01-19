package Views;
import javafx.animation.Timeline;


import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer implements Runnable {
    public boolean canplay=true;
    private String soundName;
    //constructor
    public SoundPlayer(String soundName) {
        this.soundName = soundName;
    }
    //run the music
    public void run() {
        //load a music file
        final File file = new File(soundName); // music file
        try {
            //get audio input from file
            final AudioInputStream in =
                    AudioSystem.getAudioInputStream(file);
            // change music format
            final int ch = in.getFormat().getChannels();
            final float rate = in.getFormat().getSampleRate();
            final AudioFormat outFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, rate,
                    16, ch, ch * 2, rate, false);
             //load every line of data
            final DataLine.Info info =
                    new DataLine.Info(SourceDataLine.class, outFormat);
            final SourceDataLine line =
                    (SourceDataLine) AudioSystem.getLine(info);
            //if the music file not empty, then start to play
            if (line != null) {
                line.open(outFormat);
                line.start(); // start playing music
                canplay=true;
                final byte[] buffer = new byte[65536];
                //play music
                for (int n = 0; n != -1&&canplay;
                     //read from music buffer
                     n = AudioSystem
                             .getAudioInputStream(outFormat, in)
                             .read(buffer, 0, buffer.length)) {
                    line.write(buffer, 0, n);
                }
                line.drain();
                line.stop();
            }
            line.close();
            in.close();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
