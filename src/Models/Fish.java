package Models;

import java.awt.*;

/**
 * This abstract class Fish is the parent of Fish1, 2, 3 and 4. This class defines common
 * functions and common variables of four kinds of fish. Five different methods are implemented:
 * Fish as constructor, paint, refresh, hooked and runFish.
 */
public abstract class Fish {
    public double x;
    public double y;
    public double r;
    public int value;
    public int density;
    public int movingDirection;
    public double movingSpeed;
    public boolean isHooked;

    /**
     * This function is the constructor of Fish. It is also used in children Fish 1, 2, 3, 4 for inheritance.
     * @param x position x of fish
     * @param y position y of fish
     * @param r radius r of fish as a circle
     * @param value the price/score of the fish
     * @param density the density of the fish
     * @param movingDirection left or right direction of the fish
     * @param movingSpeed velocity of the fish when it moves
     */
    public Fish(double x, double y, double r, int value, int density, int movingDirection, double movingSpeed) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.value = value;
        this.density = density;
        this.movingDirection = movingDirection;
        this.movingSpeed = movingSpeed;
        this.isHooked = false;
    }

    /**
     * Abstract method paint, used in Fish1, 2, 3, and 4
     * @param g graphics used in stage
     */
    public abstract void paint(Graphics g);

    /**
     * refresh the position of the fish
     * @param newX new position x of fish
     * @param newY new position y of fish
     */
    public void refresh(double newX, double newY) {
        x = newX;
        y = newY;
    }

    /**
     * When fish is hooked, remove fish from fishList in the stage
     * @param stage used in Views.Stage
     * @param i used in Views.Stage to go through a for loop
     */
    public void hooked(Views.Stage stage, int i) {
        stage.fishList.remove(i);
    }

    /**
     * If fish goes to the limit of the window, fish will go back.
     */
    public void runFish() {
        x += movingDirection * movingSpeed;
        if (x < 0 || x > 1050) {
            movingDirection = -movingDirection;
        }
    }
}
