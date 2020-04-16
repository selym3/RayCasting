package engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import util.KeyTracker;
import util.MouseTracker;

public class Engine extends Canvas implements Runnable {

    public static interface Constants {
        double TICK_RATE = 60;

        Color BG_COLOR = Color.BLUE;

        Color FG_COLOR = Color.GRAY;
    }

    public static void main(String[] args) {
        new Engine(600, 600);
    }

    private static final long serialVersionUID = 1L;

    private boolean isRunning;
    private Thread engineThread;

    private final Window window;
    private final KeyTracker keys;
    private final MouseTracker mouse;

    double posX = 7.0f;
    double posY = 7.0f;

    double dirX = -1;
    double dirY = 0;

    double planeX = 0;
    double planeY = 0.66;

    // implement depth with new algorithim
    double depth = 16;

    int mHeight = 32;
    int mWidth = 32;

    boolean[][] mMap = new boolean[mWidth][mHeight];

    public Engine(int width, int height) {
        engineThread = new Thread(this);
        engineThread.setName("Engine-Thread");

        mouse = new MouseTracker();
        keys = new KeyTracker();
        addKeyListener(keys);
        addMouseMotionListener(mouse);

        for (int row = 0; row < mHeight; row++) {
            mMap[0][row] = true;
            mMap[mHeight - 1][row] = true;
        }

        for (int col = 0; col < mWidth; col++) {
            mMap[col][0] = true;
            mMap[col][mWidth - 1] = true;
        }

        isRunning = false;

        window = new Window(this, width, height);
        start();
    }

    // private int lastX = 0;
    final double rotSpeed = 0.01;
    final double moveSpeed = 0.05;

    private void tick() {
        // if (mouse.getX() - lastX < 0) {
        // fA -= 0.1f;
        // } else if (mouse.getX() - lastX > 0) {
        // fA += 0.1f;
        // }

        // lastX = mouse.getX();

        if (keys.get(KeyEvent.VK_UP)) {
            if (mMap[(int) (posX + dirX * moveSpeed)][(int) posY] == false)
                posX += dirX * moveSpeed;
            if (mMap[(int) posX][(int) (posY + dirY * moveSpeed)] == false)
                posY += dirY * moveSpeed;
        }

        if (keys.get(KeyEvent.VK_DOWN)) {
            if (mMap[(int) (posX - dirX * moveSpeed)][(int) posY] == false)
                posX -= dirX * moveSpeed;
            if (mMap[(int) posX][(int) (posY + dirY * moveSpeed)] == false)
                posY -= dirY * moveSpeed;
        }

        if (keys.get(KeyEvent.VK_RIGHT)) {
            // both camera direction and camera plane must be rotated
            double oldDirX = dirX;
            dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
            dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
            planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
        }

        if (keys.get(KeyEvent.VK_LEFT)) {
            // both camera direction and camera plane must be rotated
            double oldDirX = dirX;
            dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
            dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(rotSpeed);
            planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
        }

    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Constants.BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Constants.FG_COLOR);
        g.fillRect(0, getHeight() / 2, getWidth(), getHeight());

        // add rendering here

        for (int x = 0; x < getWidth(); x++) {
            double cameraX = 2 * x / (double) getWidth() - 1;
            double rayDirX = dirX + planeX * cameraX;
            double rayDirY = dirY + planeY * cameraX;

            int mapX = (int) posX;
            int mapY = (int) posY;

            double sideDistX;
            double sideDistY;

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            int stepX;
            int stepY;

            boolean hit = false;
            int side = -1;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (posX - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1 - posX) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (posY - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - posY) * deltaDistY;
            }

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                if (mMap[mapX][mapY]) {
                    hit = true;
                }
            }

            // Calculate distance projected on camera direction (Euclidean distance will
            // give fisheye effect!)
            if (side == 0) {
                perpWallDist = (mapX - posX + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (mapY - posY + (1 - stepY) / 2) / rayDirY;
            }

            // Calculate height of line to draw on screen
            int lineHeight = (int) (getHeight() / perpWallDist);

            // calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + getHeight() / 2;
            if (drawStart < 0) {
                drawStart = 0;
            }
            int drawEnd = lineHeight / 2 + getHeight() / 2;
            if (drawEnd >= getHeight()) {
                drawEnd = getHeight() - 1;
            }

            float brightness = (1 - (float) perpWallDist / (float) mWidth);
            brightness = brightness < 0 ? 0 : (brightness > 1) ? 1 : brightness;

            g.setColor(Color.getHSBColor(0, 0,brightness));
            g.drawLine(x, drawStart, x, drawEnd);

        }

        // end rendering here

        bs.show();
        g.dispose();
    }

    private synchronized void start() {
        if (isRunning) {
            return;
        }
        engineThread.start();
        isRunning = true;
    }

    private synchronized void stop() {
        if (!isRunning) {
            return;
        }
        try {
            engineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }

    @Override
    public void run() {
        requestFocus();
        long lastTime = System.nanoTime();
        double ns = 1000000000 / Constants.TICK_RATE;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                window.setTitle("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }

}