package megaparty.planeRace;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import megaparty.Player;

/**
 *
 * @author Niklas
 */
public class Plane {

    private Player player;
    private double speedY, posX, posY, jumpCd, jumpCdM, jumpForce, gravity, facingAngle;
    private boolean dead;
    private int maxVel, width, height;
    private BufferedImage planeModel, planeModelRot;
    private static final BufferedImage planeImg = megaparty.MegaParty.loadImage("planeRace/Resources/Textures/Plane.png");

    public Plane(double posX, double posY, Player player) {
        this.player = player;
        jumpForce = 1.2;
        gravity = 0.2;
        maxVel = 30;
        width = (int)(60 * megaparty.MegaParty.GAME_PIXEL_FIX);
        height = (int)(60 * megaparty.MegaParty.GAME_PIXEL_FIX);
        facingAngle = 0;
        setModel(planeImg);
        setFacingImage(facingAngle);
        this.posX = posX;
        this.posY = posY;
        dead = false;
    }

    public void draw(Graphics g) {
        if(planeModelRot != null){
            setFacingImage(facingAngle);
            g.drawImage(planeModelRot, (int)posX - width / 2, (int)posY - height / 2, null);
            //g.fillOval((int) posX, (int) posY, 20, 20);
            //g.fillOval(5 + (int) posX + (int) (10 * Math.cos(facingAngle)), 5 + (int) posY + (int) (10 * Math.sin(facingAngle)), 10, 10);
        }
        g.setColor(Color.ORANGE);
        g.setFont(new Font("LucidaSans", Font.BOLD, 16));
        g.drawString(player.getNickName(), 10 + (int) posX - g.getFontMetrics().stringWidth(player.getNickName()) / 2, (int) posY - 10);

        if (player.isLagging()) {
            g.setColor(Color.red);
            g.setFont(new Font("LucidaSans", Font.BOLD, 16));
            g.drawString("DC: " + player.timeToDC(),(int) posX - g.getFontMetrics().stringWidth("DC: " + player.timeToDC()) / 2, (int) posY - height / 2);
        }
    }

    public void setFacingImage(double angle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle, planeModel.getWidth() / 2, planeModel.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        planeModelRot = op.filter(planeModel, null);
    }

    public void setModel(BufferedImage baseImg) {
        planeModel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double imageSizeFixer = (double) width / (double) baseImg.getWidth();
        AffineTransform transform = new AffineTransform();
        transform.scale(imageSizeFixer, imageSizeFixer);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        planeModel = op.filter(baseImg, null);
    }

    public boolean isPlayerDCed() {
        return player.isDisconnected();
    }

    public void update() {

        if (jumpCd > 0) {
            jumpCd -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
        }
        if (speedY < maxVel) {
            speedY += gravity * megaparty.MegaParty.GAME_SPEED_FIX;
        }
        if (player.getCtrlPushRight() > 0.01 && speedY > -maxVel) {
            speedY += jumpForce * Math.sin(player.getCtrlFacingRight() * player.getCtrlPushRight() * megaparty.MegaParty.GAME_SPEED_FIX);
        }
        if (speedY > maxVel) {
            speedY = maxVel;
        }
        facingAngle = Math.atan2(speedY, 10);
        posY += speedY * megaparty.MegaParty.GAME_PIXEL_FIX * megaparty.MegaParty.GAME_SPEED_FIX;

    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosX() {
        return posX;
    }

    public Player getPlayer() {
        return player;
    }

}
