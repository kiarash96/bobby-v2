package main;

import scene.SceneManager;

import javax.swing.JPanel;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by kiarash on 7/16/16.
 */
public class GamePanel extends JPanel {
    public static final int WIDTH = 1280, HEIGHT = 960;
    public static final int FPS = 60;

    Image buffer;
    Queue<AWTEvent> eventQueue;
    boolean running;

    SceneManager sm;

    public GamePanel() {
        super();

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
        running = true;

        eventQueue = new ConcurrentLinkedQueue<>();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK
                | AWTEvent.KEY_EVENT_MASK);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        sm = new SceneManager();
    }

    @Override
    protected void processEvent(AWTEvent e) {
        eventQueue.add(e);
    }

    public void mainLoop() throws InterruptedException {
        while (running) {
            long start = System.nanoTime();

            processInput();
            update();
            render();

            long elapsed = System.nanoTime() - start;
            long wait = (long) (1000.0 / FPS - (double) (elapsed) / (1000 * 1000));
            if (wait > 0.0)
                Thread.sleep(wait);
        }
    }

    private void processInput() {
        while (!eventQueue.isEmpty()) {
            // TODO: process event
            System.err.println(eventQueue.poll());
        }
    }

    private void update() {
        // TODO
    }

    private void render() {
        Graphics2D g = (Graphics2D) buffer.getGraphics();

        // clear the buffer
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        sm.render(g);

        // draw the buffer on screen
        this.getGraphics().drawImage(buffer, 0, 0, null);
    }
}
