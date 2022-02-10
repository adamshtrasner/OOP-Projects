package brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Puck;
import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces several pucks instead of brick once removed.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator {

    private final Renderable renderable;
    private final Sound sound;

    public PuckStrategy(CollisionStrategy toBeDecorated,
                        ImageReader imageReader,
                        SoundReader soundReader) {
        super(toBeDecorated);
        this.renderable = imageReader.readImage("assets/mockBall.png", true);
        this.sound = soundReader.readSound("assets/blop_cut_silenced.wav");
    }

    /**
     * Add pucks to game on collision and delegate to held CollisionStrategy.
     * @param thisObj this object.
     * @param otherObj the other object.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);

        // initialize first puck's velocity to be 160 and
        // increase it by 10 for the next puck
        float puckVel = 160;
        for (int i = 0; i < 3; i++) {
            repositionAndAddPuck(createPuck(thisObj, otherObj), puckVel);
            puckVel += 10;
        }
    }

    /*
     Creates a Puck object.
     */
    private GameObject createPuck(GameObject thisObj, GameObject otherObj) {
        return new Puck(otherObj.getTopLeftCorner(),
                new Vector2(thisObj.getDimensions().x() / 3, thisObj.getDimensions().x() / 3),
                this.renderable,
                this.sound);
    }

    /*
     Repositions the puck and adds it to the game collection.
     */
    private void repositionAndAddPuck(GameObject puck, float puckVel) {
        float puckVelX = puckVel;

        Random rand = new Random();
        if (rand.nextBoolean()) {
            puckVelX *= -1;
        }

        puck.setVelocity(new Vector2(puckVelX, puckVel));
        getGameObjectCollection().addGameObject(puck);

    }
}
