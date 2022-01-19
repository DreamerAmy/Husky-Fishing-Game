package Models;

import javax.swing.*;
import java.awt.*;

/**
 * This class extends from abstract class Fish. It has two methods. Fish2 as constructor
 * and paint as paint function used in stage
 */
public class Fish2 extends Models.Fish {
    public Fish2(double x, double y, double r, int value, int density,
          int movingDirection, double movingSpeed) {

        super(x, y, r, value, density, movingDirection, movingSpeed);
    }

    /**
     * This method paints the fish using image icon. Used in stage
     * @param g graphics used in stage
     */
    public void paint(Graphics g) {
        // If movingDirection is larger than 0,
        // Fish will move from left to right.
        Image icon;
        if (movingDirection > 0) {

            icon = new ImageIcon("res/images/fish2.png").getImage();
        } else {

            icon = new ImageIcon("res/images/fish2_head_left.png").getImage();
        }
        // The image will be inside a rectangle with location as first two variable
        // and width and height as third and fourth variable
        g.drawImage(icon, (int) (x - 2 * r), (int) (y - r), (int) (4 * r), (int) (2 * r), null);
    }
}