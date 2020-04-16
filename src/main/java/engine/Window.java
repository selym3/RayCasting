package engine;

import javax.swing.JFrame;

import java.awt.Component;

/**
 * Simple window for handling single children.
 * 
 * @author Myles Pasetsky (@selym3)
 */
public class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * @param component component
     */
    public Window(Component component, int width, int height) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        component.setSize(width, height);
        add(component);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}