package megaparty.planeRace;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import megaparty.GameFrame;
import megaparty.MiniGame;
import megaparty.Player;
import megaparty.MegaParty;

/**
 *
 * @author Niklas
 */
public class PlaneRaceGame extends MiniGame {

    private ArrayList<Plane> planes, deadPlanes;
    private ArrayList<Wall> walls, deadWalls;
    private double countDownToStart, gameOverCounter;
    private static final BufferedImage COVER_IMG = megaparty.MegaParty.loadImage("planeRace/Resources/Textures/icon.png");
    private double wallsCreateCD, wallsCreateCDM, wallSpeedUP, wallSpeedUPM, wallSpeed;
    private Random rng;
    private int startingPlanes, backgroundX, groundX, pStartLocX, pStartLocY;
    private boolean gameOver;
    private BufferedImage background;
    private boolean readyToExit = false;

    @Override
    public String getName() {
        return "Air Plane Mainia";
    }
    
    @Override
    public String getDescription(){
        return "Flappy bird rip of 100%.\n\nRecommended Players: 1 - 8";
    }

    public PlaneRaceGame(GameFrame frame, MegaParty program, Player[] players) {
        super(frame, program, players);
        planes = new ArrayList();
        deadPlanes = new ArrayList();
        walls = new ArrayList();
        deadWalls = new ArrayList();
        wallsCreateCDM = 5;
        wallSpeedUPM = 5;
        wallsCreateCD = 0;
        groundX = (int) (930 * megaparty.MegaParty.GAME_PIXEL_FIX);
        gameOverCounter = 0;
        rng = new Random();
        background = megaparty.MegaParty.loadImage("planeRace/Resources/Textures/loopingBackground.png");
    }
    
    @Override
    public void init(){
        megaparty.MegaParty.getSoundHandler().setMusic("planeRace/Resources/Sound/PimPoyPocket.wav");
    }

    public void startNewRound() {
        pStartLocX = 100;
        pStartLocY = (int)(600 * megaparty.MegaParty.GAME_PIXEL_FIX);
        backgroundX = 0;
        countDownToStart = 6;
        wallSpeed = 5;
        wallSpeedUP = wallSpeedUPM;
        gameOverCounter = 150;
        wallsCreateCDM = 5;
        wallsCreateCD = 0;
        planes.clear();
        walls.clear();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (pStartLocX > 300) {
                    pStartLocX = 125;
                    pStartLocY -= 75;
                }
                planes.add(new Plane(pStartLocX, pStartLocY, players[i]));
                pStartLocX += 50;
            }
        }
        startingPlanes = planes.size();
    }

    private void checkForMorePlayers() {
        boolean playerAlreadyPlaying = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                playerAlreadyPlaying = false;
                for (Plane p : planes) {
                    if (p.getPlayer() == players[i]) {
                        playerAlreadyPlaying = true;
                    }
                }
                if (!playerAlreadyPlaying) {
                    if (pStartLocX > 300) {
                        pStartLocX = 125;
                        pStartLocY -= 75;
                    }
                    planes.add(new Plane(pStartLocX, pStartLocY, players[i]));
                    pStartLocX += 50;
                }
            }
        }
        startingPlanes = planes.size();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(background, backgroundX, 0, frame.getWidth(), frame.getHeight(), null);
        g.drawImage(background, backgroundX + frame.getWidth(), 0, frame.getWidth(), frame.getHeight(), null);
        if (!planes.isEmpty()) {
            for (Plane p : planes) {
                p.draw(g);
            }
        }
        if (!walls.isEmpty()) {
            for (Wall w : walls) {
                w.draw(g);
            }
        }

        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        String wS = "" + wallSpeed;
        if (wS.length() > 6) {
            wS = wS.substring(0, 5);
        }
        g.setColor(Color.yellow);
        g.fillRect(frame.getWidth() / 2 - g.getFontMetrics().stringWidth("Speed: " + wS) / 2 - 5, 80, g.getFontMetrics().stringWidth("Speed: " + wS) + 10, 30);
        g.setColor(Color.black);
        g.drawString("Speed: " + wS, (frame.getWidth() / 2) - (g.getFontMetrics().stringWidth("Speed: " + wS) / 2), 100);

        drawCountDown(g);
        if(gameOver){
            g.setColor(Color.yellow);
            g.fillRect(frame.getWidth() - (int)(300 * megaparty.MegaParty.GAME_PIXEL_FIX), 0, (int)(300 * megaparty.MegaParty.GAME_PIXEL_FIX), frame.getHeight());
            g.setColor(Color.black);
            g.setFont(new Font("LucidaSans", Font.BOLD, 40));
            g.drawString("[Score]", frame.getWidth() - 150 - (int)(g.getFontMetrics().stringWidth("[Score]") / 2) , 50);
            
            int dSY = 150;
            g.setFont(new Font("LucidaSans", Font.BOLD, 20));
            for(megaparty.Player p: program.getScoreBoard()){
                g.drawString(p.getNickName() + "    " + p.getScore(), frame.getWidth() - (int)(280 * megaparty.MegaParty.GAME_PIXEL_FIX), dSY);
                dSY += 40;
            }
        }
    }

    public void drawCountDown(Graphics g) {
        if (countDownToStart > 0) {
            g.setColor(Color.red);
            if (planes.isEmpty()) {
                g.setFont(new Font("LucidaSans", Font.PLAIN, 100));
                g.drawString("Waiting for Players...", frame.getWidth() / 2 - g.getFontMetrics().stringWidth("Waiting for Players...") / 2, frame.getHeight() / 2 + 50);

            } else {
                g.setFont(new Font("LucidaSans", Font.PLAIN, 400));
                g.drawString("" + (int) countDownToStart, frame.getWidth() / 2 - g.getFontMetrics().stringWidth("" + (int) countDownToStart) / 2, frame.getHeight() / 2 + 200);
            }
        }
    }

    @Override
    public void update() {
        checkForDeadPlanes();
        if (!gameOver) {
            if (planes.isEmpty()) {
                gameOver = true;
                gameOverCounter = 3;
                return;
            }
            if (countDownToStart <= 0) {
                createWalls();
                updateWalls();
                updateBackground();
                increaseWallSpeed();
                if (!planes.isEmpty()) {
                    for (Plane p : planes) {
                        p.update();
                    }
                }
                checkWallCollide();
            } else {
                countDownToStart -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
                checkForMorePlayers();
            }
        } else {
            if (gameOverCounter > 0) {
                gameOverCounter -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
            } else {
                gameOverCounter = 3;
                gameOver = false;
                startNewRound();
            }
        }
    }

    private void updateBackground() {
        if (backgroundX > -frame.getWidth()) {
            backgroundX -= wallSpeed * megaparty.MegaParty.GAME_PIXEL_FIX * megaparty.MegaParty.GAME_SPEED_FIX;
        } else {
            backgroundX += frame.getWidth();
        }

    }

    private void checkWallCollide() {
        for (Plane p : planes) {
            for (Wall w : walls) {
                if (w.checkCollide((int) p.getPosX(), (int) p.getPosY())) {
                    p.setDead(true);
                    deadPlanes.add(p);
                }
            }
        }
    }

    private void increaseWallSpeed() {
        if (wallSpeedUP < 0) {
            wallSpeedUP = wallSpeedUPM;
            wallSpeed *= 1.1;
            wallsCreateCDM *= 0.9;
        } else {
            wallSpeedUP -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
        }
    }

    private void createWalls() {
        if (wallsCreateCD < 0) {
            wallsCreateCD = wallsCreateCDM;
            walls.add(new Wall((int) (100 * megaparty.MegaParty.GAME_PIXEL_FIX) + rng.nextInt(frame.getHeight() - 
                    (int) (500 * megaparty.MegaParty.GAME_PIXEL_FIX)), (int) (220 * megaparty.MegaParty.GAME_PIXEL_FIX), 
                    frame.getWidth(), frame.getHeight()));
        } else {
            wallsCreateCD -= 1.0 / megaparty.MegaParty.UPDATES_PER_SEC;
        }
    }

    private void updateWalls() {
        double wallSpeedCorr = wallSpeed * megaparty.MegaParty.GAME_PIXEL_FIX * megaparty.MegaParty.GAME_SPEED_FIX;
        for (Wall w : walls) {
            if (w.isOutSide()) {
                deadWalls.add(w);
            } else {
                w.move(wallSpeedCorr);
            }
        }
        for (Wall w : deadWalls) {
            walls.remove(w);
        }
        deadWalls.clear();

    }

    private void checkForDeadPlanes() {
        if (!planes.isEmpty()) {
            for (Plane p : planes) {
                if (p.getPosY() < 0 || p.getPosY() > groundX || p.isDead() || p.isPlayerDCed()) {
                    p.setDead(true);
                    megaparty.MegaParty.getSoundHandler().playSound("planeRace/Resources/Sound/RocketHit.wav");
                    deadPlanes.add(p);
                }
            }
            for (Plane p : deadPlanes) {
                p.getPlayer().addScore(startingPlanes - planes.size());
                planes.remove(p);
            }
            deadPlanes.clear();
        }
    }

    @Override
    public BufferedImage getImage() {
        return COVER_IMG;
    }

    @Override
    public boolean readyToExit() {
        return readyToExit;
    }

}
