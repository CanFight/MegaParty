package megaparty;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.imageio.ImageIO;
import megaparty.planeRace.PlaneRaceGame;
import megaparty.wizardwars.WizardWars;

public class MegaParty implements Runnable {

    public static int UPDATES_PER_SEC = 30;
    private static final int UPDATES_PER_SEC_BASE = 30;
    public static double GAME_PIXEL_FIX = 1.0;
    public static double GAME_SPEED_FIX = 1.0;
    public static int FRAMES_PER_SEC = 60;
    private int framesC, updatesC;
    private double nsPerFrame, nsPerUpdate;
    private boolean isRunning;
    private GameFrame frame;
    private NetworkHandler netHandler;
    private NetworkInterface netFace;
    private InetAddress netAddress, broadcast;
    private volatile int[] playerListIDs;
    private volatile Player[] players;
    private int port;
    private MiniGame currentGame;
    private ArrayList<MiniGame> miniGameList;
    private ArrayList<Player> scoreBoard;
    private static SoundHandler soundHandler;
    private Menu menu;

    public static void setUpdateSpeed(int i) {
        UPDATES_PER_SEC = i;
        setSpeedFix();
    }

    public static void setSpeedFix() {
        GAME_SPEED_FIX = (double) UPDATES_PER_SEC_BASE / (double) UPDATES_PER_SEC;

    }

    public MegaParty() {
        setUpdateSpeed(60);
    }

    public void init() {
        miniGameList = new ArrayList();
        soundHandler = new SoundHandler();
        isRunning = true;
        port = 8532;
        playerListIDs = new int[100];
        for (int i = 0; i < playerListIDs.length; i++) {
            playerListIDs[i] = 0;
        }
        scoreBoard = new ArrayList();
        players = new Player[100];
        frame = new GameFrame(this);
        java.awt.Dimension objDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        GAME_PIXEL_FIX = (double) objDimension.getHeight() / (double) 1080;
        System.out.println(GAME_PIXEL_FIX);
        findCorrectNetworkInterface();
        netHandler = new NetworkHandler(netFace, netAddress, broadcast, this, port);
        //currentGame = new PlaneRaceGame(frame, this, players);
        menu = new Menu(this);
        menu.addGameToList(new PlaneRaceGame(frame, this, players));
        menu.addGameToList(new WizardWars(frame, this, players));

        menu.refreshListPanel();

        new Thread(this).start();
    }

    public Player[] getPlayers() {
        return players;
    }

    @Override
    public void run() {

        nsPerUpdate = 1000000000.0 / UPDATES_PER_SEC;
        nsPerFrame = 1000000000.0 / FRAMES_PER_SEC;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;
        double unprocessedTimeFPS = 0;

        int frames = 0;
        int updates = 0;

        long frameCounter = System.currentTimeMillis();

        while (isRunning) {

            long currentTime = System.nanoTime();
            long passedTime = currentTime - lastTime;
            lastTime = currentTime;
            unprocessedTime += passedTime;
            unprocessedTimeFPS += passedTime;

            if (unprocessedTime >= nsPerUpdate) {
                unprocessedTime = 0;
                updates++;
                update();
            }

            if (unprocessedTimeFPS >= nsPerFrame) {
                unprocessedTimeFPS = 0;
                render();
                frames++;
            }

            if (System.currentTimeMillis() - frameCounter >= 1000) {
                framesC = frames;
                updatesC = updates;
                frames = 0;
                updates = 0;
                frameCounter += 1000;
                updateSec();
            }

        }
        dispose();
    }

    private void update() {
        menu.update();
        sortScoreBoard();
        if (currentGame != null) {
            currentGame.update();
        }
        for (int i = 0; i < players.length; i++) {
            if (playerListIDs[i] != 0) {
                players[i].updateConnectionCheck();
                if (players[i].isDisconnected()) {
                    scoreBoard.remove(players[i]);
                    players[i] = null;
                    playerListIDs[i] = 0;
                }
            }
        }
    }

    private void updateSec() {
        netHandler.broadCastServer();
    }

    public void sortScoreBoard() {
        if (players.length != 0) {
            ArrayList<Player> pSorted = new ArrayList();
            scoreBoard.clear();
            for (Player p : players) {
                    if(p != null){
                        scoreBoard.add(p);
                    }
                }
            int hV;
            Player hP;
            for (int i = 0; i < 100; i++) {
                hV = -1;
                hP = null;
                for (Player p : scoreBoard) {
                    if (p.getScore() > hV) {
                        hV = p.getScore();
                        hP = p;
                    }
                }
                if (hP != null) {
                    pSorted.add(hP);
                    scoreBoard.remove(hP);
                }
                if (scoreBoard.isEmpty()) {
                    break;
                }
            }
            scoreBoard = pSorted;
        }
    }

    private void render() {
        if (currentGame != null) {
            BufferStrategy bs = frame.getBufferStrategy();
            if (bs == null) {
                frame.createBufferStrategy(2);
                return;
            }

            Graphics g = bs.getDrawGraphics();

            currentGame.draw(g);
            g.setColor(Color.blue);
            g.setFont(new Font("LucidaSans", Font.BOLD, 20));
            g.drawString("FPS: " + framesC, 15, 25);
            g.drawString("Updates: " + updatesC, 15, 50);
            String ip = "";
            try {
                ip = netAddress.getHostAddress();
            } catch (Exception e) {
                ip = "???";
            }
            g.drawString("IP: " + ip, 15, 75);
            g.drawString("Port: " + port, 15, 100);
            g.dispose();
            bs.show();
        }
    }

    private void dispose() {
        System.exit(0);
    }

    private void findCorrectNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (networkInterface.getHardwareAddress() != null && !networkInterface.getDisplayName().toLowerCase().contains("vm")
                            && addr.getHostAddress().substring(0, 1).equalsIgnoreCase("1")) {
                        netFace = networkInterface;
                        netAddress = addr;
                        java.util.List<InterfaceAddress> list = netFace.getInterfaceAddresses();
                        Iterator<InterfaceAddress> it = list.iterator();

                        while (it.hasNext()) {
                            InterfaceAddress ia = it.next();
                            if (ia.getBroadcast() != null) {
                                broadcast = ia.getBroadcast();

                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("could not find network interface");
        }

    }

    public int getPlayerListIdByIP(int ipID) {
        for (int i = 0; i < playerListIDs.length; i++) {
            if (playerListIDs[i] == ipID) {
                return i;
            }
        }
        return -1;
    }

    public int getPlayerListID(int i) {
        return playerListIDs[i];
    }

    public int addPlayerToIdList(int ipID) {
        for (int i = 0; i < playerListIDs.length; i++) {
            if (playerListIDs[i] == 0) {
                try {
                    playerListIDs[i] = ipID;
                    addPlayer(i);
                    return i;
                } catch (Exception e) {
                }
            }
        }
        System.err.println("List is full could not add player");
        return -1;
    }

    public void addPlayer(int id) {
        if (id >= 0 && id < players.length) {
            players[id] = new Player();
            scoreBoard.remove(players[id]);
            System.out.println("player added: " + id);
        }
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public ArrayList<Player> getScoreBoard() {
        return scoreBoard;
    }

    public static SoundHandler getSoundHandler() {
        return soundHandler;
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(megaparty.MegaParty.class.getResource(path));
        } catch (Exception e) {
            System.out.println("Failed loading: " + path);
            return null;
        }
    }

    public ArrayList<MiniGame> getMiniGameList() {
        return miniGameList;
    }

    public void setCurrentGame(MiniGame currentGame) {
        this.currentGame = currentGame;
    }

    public void setFrameVisiblity(boolean b) {
        frame.setVisible(b);
    }
}
