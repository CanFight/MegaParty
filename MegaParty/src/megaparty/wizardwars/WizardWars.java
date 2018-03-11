package megaparty.wizardwars;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import megaparty.GameFrame;
import megaparty.MegaParty;
import megaparty.MiniGame;
import megaparty.Player;

/**
 *
 * @author Niklas
 */
public class WizardWars extends MiniGame {

    private boolean readyToExit = false;
    private ArrayList<Wizard> wizards;
    
    public WizardWars(GameFrame frame, MegaParty program, Player[] players) {
        super(frame, program, players);
        wizards = new ArrayList();
        startNewRound();
    }

    public void startNewRound() {
        checkForMorePlayers();
        if(wizards.isEmpty()){
            return;
        }
        for(Wizard p: wizards){
            p.setDead(false);
        }
    }

    private void checkForMorePlayers() {
        boolean playerAlreadyPlaying = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                playerAlreadyPlaying = false;
                for (Wizard p : wizards) {
                    if (p.getPlayer() == players[i]) {
                        playerAlreadyPlaying = true;
                    }
                }
                if (!playerAlreadyPlaying) {
                    wizards.add(new Wizard(players[i], wizards));

                }
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "Wizard Wars";
    }

    @Override
    public String getDescription() {
        return "Kinda like warlock?\n\n[In development]";
    }

    @Override
    public BufferedImage getImage() {
        return COVER_IMG;
    }

    private static final BufferedImage COVER_IMG = megaparty.MegaParty.loadImage("");

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        for(Wizard p: wizards){
            if(!p.isDead()){
                p.draw(g);
            }
        }
    }

    @Override
    public void update() {
        for(Wizard p: wizards){
            if(!p.isDead()){
                p.update();
            }
        }
    }

    @Override
    public boolean readyToExit() {
        return readyToExit;
    }
}
