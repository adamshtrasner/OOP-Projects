package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * An avatar can move around the world.
 * @author Adam Shtrasner and Noam Altman
 */
public class Avatar extends GameObject {

    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 300;
    private static final float MAX_ENERGY_LEVEL = 100;
    private static final float MIN_ENERGY_LEVEL = 0;
    private float energyLevel = MAX_ENERGY_LEVEL;

    private static UserInputListener inputListener;
    private static ImageReader imageReader;

    private static Renderable staticRenderable;
    private static Renderable rightWalkRenderable;
    private static Renderable leftWalkRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    private Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * It can stand, walk, jump and fly, and never reaches the end of the world.
     * @param gameObjects The collection of all participating game objects.
     * @param layer  The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;

        // states of the avatar
        Avatar.staticRenderable = new AnimationRenderable(new String[] {"pepse/assets/mage.png"},
                imageReader,
                true, 0);
        Avatar.leftWalkRenderable = new AnimationRenderable(new String[]
                {"pepse/assets/left_leg_mage.png"},
                imageReader,
                true, 0);
        Avatar.rightWalkRenderable = new AnimationRenderable(new String[]
                {"pepse/assets/right_leg_mage.png"},
                imageReader,
                true, 0);

        Avatar avatar = new Avatar(topLeftCorner,
                Vector2.ONES.mult(Block.SIZE * 2),
                staticRenderable);

        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            this.renderer().setIsFlippedHorizontally(true);
            xVel -= VELOCITY_X;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            this.renderer().setIsFlippedHorizontally(false);
            xVel += VELOCITY_X;
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    ()->physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
            decreaseEnergy();
        }
        // Make avatar fly
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                this.energyLevel > MIN_ENERGY_LEVEL) {
            transform().setVelocityY(VELOCITY_Y);
            decreaseEnergy();
        }
        if (getVelocity().y() == 0) {
            increaseEnergy();
        }
    }

    /*
     Decreases the energy of the avatar.
     */
    private void decreaseEnergy() {
        if (this.energyLevel / 2 < MIN_ENERGY_LEVEL) {
            this.energyLevel = MIN_ENERGY_LEVEL;
        }
        else {
            this.energyLevel /= 2;
        }
    }

    /*
     Increases the energy of the avatar.
     */
    private void increaseEnergy() {
        if (this.energyLevel * 2 > MAX_ENERGY_LEVEL) {
            this.energyLevel = MAX_ENERGY_LEVEL;
        }
        else {
            if (this.energyLevel == MIN_ENERGY_LEVEL) {
                this.energyLevel = 1;
            }
            else {
                this.energyLevel *= 2;
            }
        }
    }
}
