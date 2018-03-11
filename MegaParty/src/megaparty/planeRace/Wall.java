package megaparty.planeRace;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Niklas
 */
public class Wall {

    private Rectangle upperRect, lowerRect;
    private double locX;
    private static final BufferedImage PIPE_TOP_IMG= megaparty.MegaParty.loadImage("planeRace/Resources/Textures/PipeTop.png");
    private static final BufferedImage PIPE_BOT_IMG= megaparty.MegaParty.loadImage("planeRace/Resources/Textures/PipeBot.png");
    private static final BufferedImage PIPE_MID_IMG= megaparty.MegaParty.loadImage("planeRace/Resources/Textures/PipeMid.png");
    
    public Wall(int holeTopY, int holeHight, int xSpawn, int screenHight){
        upperRect = new Rectangle(xSpawn, 0, (int)(150 * megaparty.MegaParty.GAME_PIXEL_FIX), holeTopY);
        lowerRect = new Rectangle(xSpawn, holeTopY + holeHight, (int)(150 * megaparty.MegaParty.GAME_PIXEL_FIX), screenHight - (holeTopY + holeHight));
        locX = xSpawn;
    }
    
    public void move(double speed){
        locX -= speed;
        upperRect.x = (int)locX;
        lowerRect.x = (int)locX;
    }
    
    public boolean checkCollide(int xC, int yC){
        return (upperRect.contains(xC, yC) || lowerRect.contains(xC, yC));
    }
    
    public void draw(Graphics g){
        int imgHeight = PIPE_TOP_IMG.getHeight() * upperRect.width / PIPE_TOP_IMG.getWidth();
        g.setColor(Color.magenta);
        g.drawImage(PIPE_TOP_IMG, upperRect.x, upperRect.height - imgHeight, upperRect.width, imgHeight, null);
        g.drawImage(PIPE_MID_IMG, upperRect.x, 0, upperRect.width, upperRect.height - imgHeight, null);
        g.drawImage(PIPE_BOT_IMG, lowerRect.x, lowerRect.y, upperRect.width, imgHeight, null);
        g.drawImage(PIPE_MID_IMG, lowerRect.x, lowerRect.y + imgHeight, lowerRect.width, lowerRect.height - imgHeight, null);
    }
    
    public int getX(){
        return upperRect.x;
    }
    
    public boolean isOutSide(){
        return upperRect.x < 0 - upperRect.width;
    }
}
