package megaparty;

/**
 *
 * @author Niklas
 */
public class Player {

    private static final int DROP_TIME = 30;
    
    private double ctrlFacingLeft, ctrlFacingRight, ctrlPushLeft, ctrlPushRight, timeSinceLastPack;
    private String nickName;
    private int score;
    private boolean lagging, disconnected;

    public Player() {
        nickName = "Guest";
        score = 0;
    }

    public void addScore(int i){
        score += i;
    }

    public int getScore() {
        return score;
    }
    
    public boolean isLagging() {
        return lagging;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void updateConnectionCheck() {
        lagging = false;
        disconnected = false;
        timeSinceLastPack += 1.0 / MegaParty.UPDATES_PER_SEC;
        if (timeSinceLastPack >= 1) {
            lagging = true;
            ctrlPushRight = 0;
            ctrlPushLeft = 0;
        }
        if (timeSinceLastPack >= DROP_TIME) {
            disconnected = true;
        }
    }

    public synchronized int timeToDC(){
        return DROP_TIME - (int)timeSinceLastPack;
    }

    public synchronized void setTimeSinceLastPack(double d) {
        timeSinceLastPack = d;
    }

    public synchronized void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public synchronized String getNickName() {
        return nickName;
    }

    public synchronized double getCtrlFacingLeft() {
        return ctrlFacingLeft;
    }

    public synchronized void setCtrlFacingLeft(double ctrlFacingLeft) {
        this.ctrlFacingLeft = ctrlFacingLeft;
    }

    public synchronized double getCtrlFacingRight() {
        return ctrlFacingRight;
    }

    public synchronized void setCtrlFacingRight(double ctrlFacingRight) {
        this.ctrlFacingRight = ctrlFacingRight;
    }

    public synchronized double getCtrlPushLeft() {
        return ctrlPushLeft;
    }

    public synchronized void setCtrlPushLeft(double ctrlPushLeft) {
        this.ctrlPushLeft = ctrlPushLeft;
    }

    public synchronized double getCtrlPushRight() {
        return ctrlPushRight;
    }

    public synchronized void setCtrlPushRight(double ctrlPushRight) {
        this.ctrlPushRight = ctrlPushRight;
    }

}
