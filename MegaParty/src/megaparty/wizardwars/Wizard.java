package megaparty.wizardwars;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import megaparty.Player;

/**
 *
 * @author Niklas
 */
public class Wizard {

    private double posX, posY, kbX, kbY, speed, facingAngle;
    private BufferedImage wizModel, wizModelRot;
    private int width, height, radius, hp, hpM, kills;
    private static final BufferedImage wizImg = megaparty.MegaParty.loadImage("wizardwars/Resources/Textures/Wizard.png");
    private static final BufferedImage barrierImg = megaparty.MegaParty.loadImage("wizardwars/Resources/Textures/Barrier.png");
    private Player player;
    private boolean dead;
    private static double friction = 0.5;
    private ArrayList<Wizard> wizards;

    private double barrierCd, barrierCdM = 15, barrierDur, barrierDurM = 2;
    
    
    public Wizard(Player p, ArrayList<Wizard> wizards) {
        player = p;
        this.wizards = wizards;
        posX = 250;
        posY = 250;
        dead = false;
        hpM = 100;
        hp = hpM;
        width = (int) (60 * megaparty.MegaParty.GAME_PIXEL_FIX);
        height = (int) (60 * megaparty.MegaParty.GAME_PIXEL_FIX);
        radius = width / 2;
        facingAngle = 0;
        speed = 4;
        setModel(wizImg);
        setFacingImage(facingAngle);
    }

    public void draw(Graphics g) {
        g.drawImage(wizModelRot, (int) posX - width / 2, (int) posY - height / 2, null);
        if (barrierDur > 0) {
            g.drawImage(barrierImg, (int) posX - width / 2, (int) posY - height / 2, width, height, null);
        }
    }

    public void update() {
        if (player.getCtrlPushLeft() > 0.05) {
            facingAngle = player.getCtrlFacingLeft();
            setFacingImage(facingAngle);
            posX += speed * Math.cos(facingAngle) * player.getCtrlPushLeft() + kbX;
            posY += speed * Math.sin(facingAngle) * player.getCtrlPushLeft() + kbY;
        }
//        knockbackFriction();

        updateSpells();
        if (player.getCtrlPushRight() > 0.1) {//Decide here what spell is being used
            spellCast();
        }
    }

    private void spellCast() {
        System.out.println(player.getCtrlFacingRight());
        if (player.getCtrlFacingRight() > 0.79 && player.getCtrlFacingRight() < 2.36) {//pushed down INCINERATE
            System.out.println("INCINERATE");
        }
        if (player.getCtrlFacingRight() > -2.36 && player.getCtrlFacingRight() < -0.79) {//pushed up FIREBALL
            System.out.println("FIREBALL");
        }
        if (player.getCtrlFacingRight() > 2.36 || player.getCtrlFacingRight() < -2.36) {//pushed left TELEPORT
            System.out.println("TELEPORT");
        }
        if (player.getCtrlFacingRight() > -0.79 && player.getCtrlFacingRight() < 0.79) {//pushed right BARRIER
            System.out.println("BARRIER");
            if (barrierCd <= 0) {
                barrierCd = barrierCdM;
                barrierDur = barrierDurM;
            }
        }
    }

    public void updateSpells() {
        if (barrierDur > 0) {
            barrierDur -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
        }
        if (barrierCd > 0) {
            barrierCd -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
        }
    }

    private void knockbackFriction() {
        boolean isKbX = false;
        boolean isKbY = false;
        if (kbX > friction) {
            isKbX = true;
            kbX -= friction;
        }
        if (kbX < friction) {
            isKbX = true;
            kbX += friction;
        }
        if (kbY > friction) {
            isKbY = true;
            kbY -= friction;
        }
        if (kbY < friction) {
            isKbY = true;
            kbY += friction;
        }
        if (!isKbX) {
            kbX = 0;
        }
        if (!isKbY) {
            kbY = 0;
        }
    }

    public void damage(int i) {
        if (barrierDur <= 0) {
            hp -= i;
            if (hp <= 0) {
                dead = true;
            }
        }
    }

    public void addKills(int i) {
        kills += i;
    }

    private void setFacingImage(double angle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle, wizModel.getWidth() / 2, wizModel.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        wizModelRot = op.filter(wizModel, null);
    }

    private void setModel(BufferedImage baseImg) {
        wizModel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double imageSizeFixer = (double) width / (double) baseImg.getWidth();
        AffineTransform transform = new AffineTransform();
        transform.scale(imageSizeFixer, imageSizeFixer);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        wizModel = op.filter(baseImg, null);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

}
