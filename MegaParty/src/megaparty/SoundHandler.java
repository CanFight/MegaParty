package megaparty;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//SoundHandler By Daniel Berg Thomsen
public class SoundHandler {

    private Clip music; //To keep track of the background music.

    public SoundHandler() {
        try {
            music = AudioSystem.getClip();  //Initialize the clip
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMusic(final String url) { //Set the music to be played
        try {
            music.close();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(url));
            music.open(inputStream);
            music.loop(Clip.LOOP_CONTINUOUSLY);
            music.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stopMusic(){
        music.stop();
    }

    public void playSound(final String url) {    //Effectively plays any sound on top of eachother
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(url));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
