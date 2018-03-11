package megaparty;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Niklas
 */
public abstract class MiniGame {
    
    protected GameFrame frame;
    protected MegaParty program;
    protected Player[] players;
    
    public MiniGame(GameFrame frame, MegaParty program, Player[] players){
        this.frame = frame;
        this.program = program;
        this.players = players;
    } 
    
    public abstract void draw(Graphics g);
    
    public abstract void update();
    
    public abstract String getName();
    
    public abstract String getDescription();
    
    public abstract boolean readyToExit();
    
    public abstract void init();
    
    public abstract BufferedImage getImage();
}
