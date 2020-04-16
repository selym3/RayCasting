package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyTracker extends KeyAdapter  {

    public Set<Integer> keys;

    public KeyTracker() {
        keys = new HashSet<Integer>();
    }

    public boolean get(int keyCode) {
        return keys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(1);
        keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

}