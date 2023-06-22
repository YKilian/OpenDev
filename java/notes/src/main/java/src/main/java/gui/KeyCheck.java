package src.main.java.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import src.main.java.utilz.Actions;

public class KeyCheck implements KeyListener {
    private gui gui;

    private boolean isKeyPressedCONTROL;
    private boolean isKeyPressedDEL;  
    private boolean isKeyPressedSPACE;

    private boolean isKeyPressedZ;
    private boolean isKeyPressedS;
    private boolean isKeyPressedX;
    private boolean isKeyPressedV;

    public KeyCheck(gui gui){
        this.gui = gui;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch( keyCode ) { 
            case KeyEvent.VK_CONTROL:
                isKeyPressedCONTROL = true;
                break;
            case KeyEvent.VK_S:
                isKeyPressedS = true;
                break;
            case KeyEvent.VK_Z:
                isKeyPressedZ = true;
                break;
            case KeyEvent.VK_X:
                isKeyPressedX = true;
                break;
            case KeyEvent.VK_V:
                isKeyPressedV = true;
                break;
            case KeyEvent.VK_DELETE:
                isKeyPressedDEL = true;
                break;
            case KeyEvent.VK_SPACE:
                isKeyPressedSPACE = true;
                break;
        };

        checkKeys(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch( keyCode ) { 
            case KeyEvent.VK_CONTROL:
                isKeyPressedCONTROL = false;
                break;
            case KeyEvent.VK_S:
                isKeyPressedS = false;
                break;
            case KeyEvent.VK_Z:
                isKeyPressedZ = false;
                break;
            case KeyEvent.VK_X:
                isKeyPressedX = false;
                break;
            case KeyEvent.VK_V:
                isKeyPressedV = false;
                break;
            case KeyEvent.VK_DELETE:
                isKeyPressedDEL = false;
                break;
            case KeyEvent.VK_SPACE:
                isKeyPressedSPACE = false;
                break;
        };
    }

    private void checkKeys(KeyEvent e) {
        if (isKeyPressedCONTROL && isKeyPressedS) {
            gui.actionHandler(Actions.SAVE.getAction());
        }
        if (isKeyPressedCONTROL && isKeyPressedZ) {
            gui.actionHandler(Actions.UNDO.getAction());
        }
        if (isKeyPressedCONTROL && isKeyPressedX) {
            gui.actionHandler(Actions.SAVESTATUS.getAction());
        }
        if (isKeyPressedCONTROL && isKeyPressedV) {
            gui.actionHandler(Actions.SAVESTATUS.getAction());
        }
        if (isKeyPressedDEL) {
            gui.actionHandler(Actions.DELETE.getAction());
        }
        if (isKeyPressedSPACE) {
            gui.actionHandler(Actions.SAVESTATUS.getAction());
        }
    }
}
