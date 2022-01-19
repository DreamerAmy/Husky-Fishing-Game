package Models;

import javax.swing.*;
import java.awt.*;

/**
 * This class extends from abstract class Fish. It has two methods. Fish3 as constructor
 * and paint as paint function used in stage
 */
public class Fish3 extends Models.Fish {
    public Fish3(double x, double y, double r, int value, int density,
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

            icon = new ImageIcon("res/images/fish3_head_right.png").getImage();
        } else {

            icon = new ImageIcon("res/images/fish3.png").getImage();
        }
        // The image will be inside a rectangle with location as first two variable
        // and width and height as third and fourth variable
        g.drawImage(icon, (int) (x - 2 * r), (int) (y - r), (int) (4 * r), (int) (2 * r), null);
    }
}

