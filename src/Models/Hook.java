package Models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class hook is used to draw the hook and implement the functions of the hook.
 * Hook will goes up and down or wait on the bank swinging. Three states are implemented
 * using enum and switch. The algorithm will get the information if the hook and fish touch each other
 * When they touch, hook and fish go up, fish is removed from fishList in stage
 */
public class Hook {
    // hook location
    private double basePositionX;
    private double basePositionY;
    // hook rotation angle
    private double theta = 0;
    // line length
    private double lineLength= 0.0;
    // radius of object hook
    final double hookRadius = 14.0;
    // weight of hook
    private double hookWeight = 700.0;
    // the velocity that hook goes down
    public double downVelocity = 40.0;
    // hook acceleration direction
    public int hookDirection = 1;
    // import fish type from Fish
    private Models.Fish fish;
    // the state of hook: wait, forward and backward
    public HookState hookState;

    /**
     * constructor of hook, assign hook's initial position
     * @param x initial x position of hook
     * @param y initial y position of hook
     */
    public Hook(double x, double y){
        basePositionX = x;
        basePositionY = y;
        hookState = HookState.WAIT;
    }

    /**
     * Current hook position x
     * @return hook current position x
     */
    double hookX(){
        double hookPositionX = basePositionX + lineLength * Math.cos(theta);
        return hookPositionX;
    }

    /**
     * Current hook position y
     * @return hook current position y
     */
    double hookY(){
        double hookPositionY = basePositionY + lineLength * Math.sin(theta);
        return hookPositionY;
    }

    /**
     * current weight of hook plus fish
     * @return current weight of hook (and fish)
     */
    double curWeight(){
        double curWeight;
        // no fish, no other weight
        if (fish == null){
            curWeight = hookWeight;
        }
        // with fish, weight is fish's weight + hook weight
        else{
            curWeight = hookWeight + fish.density * fish.r * fish.r;
        }
        return curWeight;
    }

    /**
     * Velocity of hook goes up
     * @return hook's going up velocity
     */
    double upVelocity(){
        double upV = 15000.0 / curWeight();
        return upV;
    }

    /**
     * The distance between point A and point B using theory of triangle
     * @param Ax x coordinate of A
     * @param Ay y coordinate of A
     * @param Bx x coordinate of B
     * @param By y coordinate of B
     * @return distance between A and B
     */
    public double distanceBetweenAB(double Ax, double Ay, double Bx, double By){
        return Math.sqrt((Ax-Bx)*(Ax-Bx) + (Ay-By)*(Ay-By));
    }

    /**
     * A boolean showing if fish is hooked
     * @param fishA from Fish class, fishA is one fish object
     * @return true and make hook goes up, if fish and hook is close enough. false if fish not hooked.
     */
    boolean hookFish(Models.Fish fishA){
        // if distance between fish and hook is smaller enough to make intersection
        if(distanceBetweenAB(hookX(),hookY(),fishA.x,fishA.y) < (hookRadius/2 + fishA.r)){
            // hook goes up after fish is caught
            fish = fishA;
            hookState = HookState.UP;
            return true;
        } else {
            return false;
        }
    }

    /**
     * When press space button, launch is used to change state from WAIT to DOWN
     */
    public void launch(){
        if(hookState == HookState.WAIT)
            hookState = HookState.DOWN;
    }

    /**
     * refresh the position of hook everytime called
     * @param stage used in stage, object stage
     */
    public void refresh(Views.Stage stage){
        switch (hookState){
            // in the WAIT situation, hook is above water
            case WAIT:
                // In this situation, hook goes through a half circle
                theta += hookDirection * Math.PI / Controllers.HomeController.PERIOD;
                // When theta goes to the edge (5 to 175 degree), it goes backwards by
                // changing hook acceleration direction
                if (theta >= Math.PI * 175 / 180) {
                    hookDirection = -1;
                }
                else if (theta <= Math.PI * 5 / 180) {
                    hookDirection = 1;
                }
                break;
            // in the DOWN situation, hook goes into the water
            case DOWN:
                lineLength += downVelocity;
                // When hook reaches edge, hooks goes up
                // down edge: 700. left edge: 50. right edge: 1000.
                if (hookX() < 50 || hookX() > 1000 || hookY() > 700) {
                    hookState = HookState.UP;
                    break;
                }
                // When hook gets fish, fish is hooked
                for(int i = 0; i < stage.fishList.size(); i++){
                    // testFish is a fish
                    Models.Fish testFish = stage.fishList.get(i);
                    if(hookFish(testFish)){
                        // calls hooked function in Fish
                        testFish.hooked(stage,i);
                        break;
                    }
                }
                break;
            // in the UP situation, hook goes out of water
            case UP:
                lineLength -= upVelocity();

                // fish is caught or not
                if (fish != null){
                    // hooked fish will move with hook
                    fish.refresh(hookX() + hookRadius * Math.cos(theta),
                            hookY() + hookRadius * Math.sin(theta));
                }
                // When fish comes back, add score and return to WAIT state
                if (lineLength <= 0){
                    // When fish goes to bank
                    if (fish != null) {
                        // add fish value to score and turns fish back to null
                        stage.score += fish.value;
                        fish = null;
                    }
                    // turns back to WAIT status and disappear the line
                    lineLength = 0;
                    hookState = HookState.WAIT;
                }
                break;
        }
    }

    /**
     * the rotateImage method provides the paint method with a way to rotate the hook.
     * When the hook is rotated, user can see the angle to make decision to press button.
     * @param image the image being rotated. It will be the hook.
     * @param theta the angle that image that is being rotated. It will change every time
     * @return a rotated image with BufferedImage format
     */
    public static BufferedImage rotateImage(final BufferedImage image, final double theta) {
        // use w and h represents height and width
        int width = image.getWidth();
        int height = image.getHeight();
        int type = image.getColorModel().getTransparency();
        // create a buffered image to return
        BufferedImage imageA;
        Graphics2D graphic;
        imageA = new BufferedImage(width, height, type);
        // graphic is imageA's graphic
        graphic = imageA.createGraphics();
        // implements rendering
        // newHintKey renders pixels
        RenderingHints.Key newHintKey = RenderingHints.KEY_INTERPOLATION;
        // newHintValue renders colors
        Object newHintValue = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        // setRenderingHint set the preference of rendering algorithm
        graphic.setRenderingHint(newHintKey, newHintValue);
        // rotate function takes three variables: rotation angle in radians,
        // rotation origin x coordinate, rotation origin y coordinate
        graphic.rotate(theta, width / 2, height / 2);
        // The image is drawn at its top left corner coordinate x, y in its coordinate space
        graphic.drawImage(image, 0, 0, null);
        graphic.dispose();
        return imageA;
    }

    /**
     * paint fish, hook and fishing line here
     * @param g graphics used in stage
     * @throws IOException
     */
    public void paint(Graphics g) throws IOException{
        switch (hookState) {
            case UP:
                if (fish != null){
                    // If fish is hooked, draw the fish and refresh it everytime
                    fish.paint(g);
                }
            default:
                // drawing the fishing line
                Graphics2D drawing = (Graphics2D)g;
                // set line width using setStroke function.
                Stroke newStroke = new BasicStroke(2.5f);
                // set line using setStroke function.
                drawing.setStroke(newStroke);
                // draw line between two points using drawLine function
                // First point's coordinates are first two variables
                // Second point's coordinates are third and fourth variables
                drawing.drawLine((int)(basePositionX), (int)(basePositionY)-120, (int)hookX(), (int)hookY());
                // drawing the hook
                BufferedImage hookImage = ImageIO.read(new File("res/images/fishhook.png"));
                // rotate image using function rotateImage
                BufferedImage rotatedHook = rotateImage(hookImage, theta );
                // draw the image inside a rectangle, with x and y coordinate and width and height
                g.drawImage(rotatedHook, (int)(hookX()-hookRadius), (int)(hookY()-hookRadius),
                        2*(int)hookRadius, 2*(int)hookRadius, null);
        }
    }
}
