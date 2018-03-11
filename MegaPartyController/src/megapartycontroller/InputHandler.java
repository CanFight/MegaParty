package megapartycontroller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private int keyUp, keyDown, keyLeft, keyRight, keyFireUp, keyFireDown, keyFireLeft, keyFireRight;
    private volatile boolean keyPUp, keyPDown, keyPLeft, keyPRight, keyPFireUp, keyPFireDown, keyPFireLeft, keyPFireRight;
    private RoboFightController program;
    private volatile double resultFacing, resultForce, fireFacing, fireForce;

    public InputHandler(RoboFightController prog) {
        keyUp = KeyEvent.VK_W;
        keyDown = KeyEvent.VK_S;
        keyLeft = KeyEvent.VK_A;
        keyRight = KeyEvent.VK_D;
        keyFireUp = KeyEvent.VK_UP;
        keyFireDown = KeyEvent.VK_DOWN;
        keyFireLeft = KeyEvent.VK_LEFT;
        keyFireRight = KeyEvent.VK_RIGHT;
        program = prog;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == keyUp) {
            keyPUp = true;
        }
        if (ke.getKeyCode() == keyDown) {
            keyPDown = true;
        }
        if (ke.getKeyCode() == keyLeft) {
            keyPLeft = true;
        }
        if (ke.getKeyCode() == keyRight) {
            keyPRight = true;
        }
        if (ke.getKeyCode() == keyFireUp) {
            keyPFireUp = true;
        }
        if (ke.getKeyCode() == keyFireDown) {
            keyPFireDown = true;
        }
        if (ke.getKeyCode() == keyFireLeft) {
            keyPFireLeft = true;
        }
        if (ke.getKeyCode() == keyFireRight) {
            keyPFireRight = true;
        }
        setResultingFacing();
        setResultingForce();
        setResultingFireFacing();
        setResultingFireForce();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == keyUp) {
            keyPUp = false;
        }
        if (ke.getKeyCode() == keyDown) {
            keyPDown = false;
        }
        if (ke.getKeyCode() == keyLeft) {
            keyPLeft = false;
        }
        if (ke.getKeyCode() == keyRight) {
            keyPRight = false;
        }
        if (ke.getKeyCode() == keyFireUp) {
            keyPFireUp = false;
        }
        if (ke.getKeyCode() == keyFireDown) {
            keyPFireDown = false;
        }
        if (ke.getKeyCode() == keyFireLeft) {
            keyPFireLeft = false;
        }
        if (ke.getKeyCode() == keyFireRight) {
            keyPFireRight = false;
        }
        setResultingFacing();
        setResultingForce();
        setResultingFireFacing();
        setResultingFireForce();
    }

    public int getLeftRightValue() {
        int x = 0;
        if (keyPRight) {
            x += 1;
        }
        if (keyPLeft) {
            x -= 1;
        }
        return x;
    }

    public int getUpDownValue() {
        int y = 0;
        if (keyPDown) {
            y += 1;
        }
        if (keyPUp) {
            y -= 1;
        }
        return y;
    }

    private void setResultingFacing() {
        resultFacing = Math.atan2(getUpDownValue(), getLeftRightValue());
    }
    
    
    private void setResultingFireFacing() {
        int x = 0;
        int y = 0;
        if (keyPFireRight) {
            x += 1;
        }
        if (keyPFireLeft) {
            x -= 1;
        }
        if (keyPFireDown) {
            y += 1;
        }
        if (keyPFireUp) {
            y -= 1;
        }
        fireFacing = Math.atan2(y, x);
    }
    
    private void setResultingForce(){
        if(getUpDownValue() != 0 || getLeftRightValue() != 0){
            resultForce = 1;
            return;
        }
        resultForce = 0;
    }
    
    private void setResultingFireForce(){
        int x = 0;
        int y = 0;
        fireForce = 1;
        if (keyPFireRight) {
            x += 1;
        }
        if (keyPFireLeft) {
            x -= 1;
        }
        if (keyPFireDown) {
            y += 1;
        }
        if (keyPFireUp) {
            y -= 1;
        }
        if(x == 0 && y == 0){
            fireForce = 0;
        }
    }

    public double getFireForce() {
        return fireForce;
    }

    public double getFireFacing() {
        return fireFacing;
    }

    public double getResultFacing() {
        return resultFacing;
    }

    public double getResultForce() {
        return resultForce;
    }
}
