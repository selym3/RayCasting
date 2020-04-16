package util;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class MouseTracker implements MouseMotionListener {

    private int x;
    private int y;
 
    public MouseTracker() {}

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



}