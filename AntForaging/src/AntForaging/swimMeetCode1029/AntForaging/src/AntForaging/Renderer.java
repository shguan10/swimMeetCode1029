package AntForaging;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Created by andriysheptunov on 2/6/17.
 */
public class Renderer extends Canvas {

    public JFrame frame;
    private int height = 720;
    private int width = height;
    private String title = "Ant Foraging Swarm";

    private Graphics2D g2;

    public Renderer() {
        setPreferredSize(new Dimension(width, height));

        frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void render(ArrayList<State> states, int timestep) {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.g2 = g2;

        {
            clear();
            for (State s : states) {
                if (s instanceof FoodSource) {
                    drawPathTo((FoodSource) s);
                    drawFoodSource((FoodSource) s);
                }
            }
            drawNest();
            testText("jesus has risen " + System.nanoTime() + " many times, and " + timestep + " timesteps have passed");
        }

        g2.dispose();
        g.dispose();
        bs.show();
    }

    private void clear() {
        g2.setColor(Color.white);
        g2.fillRect(0, 0, width, height);
    }

    private void drawPathTo(FoodSource foodSource) {
        double sn=1/(1+Math.exp(-25*(foodSource.getPhom()-.1)));
        g2.setColor(new Color((float)sn,(float)(1.0-sn),0));
        g2.setStroke(new BasicStroke(5));
        //g2.setStroke(new BasicStroke((float) foodSource.getPhom() * 10));
        g2.draw(new Line2D.Double(width / 2, height / 2, foodSource.getX() + (width / 2), foodSource.getY() + (height / 2)));
    }

    private void drawFoodSource(FoodSource foodSource) {
        g2.setColor(Color.blue);
        g2.fillOval(foodSource.getX() + (width / 2) - 5, foodSource.getY() + (height / 2) - 5, 10, 10);
    }

    private void drawNest() {
        g2.setColor(Color.magenta);
        g2.fillRect(width / 2 - 8, height / 2 - 8, 16, 16);
    }

    private void testText(String text) {
        g2.setColor(Color.black);
        g2.setFont(new Font("Helvetica", 0, 12));
        g2.drawString(text, 20, 20);
    }

}
