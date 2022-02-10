package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.PaddleSizeChanger;
import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Changes the paddle's length.
 */
public class ExpandOrShrinkPaddleStrategy extends RemoveBrickStrategyDecorator {

    private static final float PADDLE_SIZE_CHANGER_SPEED = 160;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;

    public ExpandOrShrinkPaddleStrategy(CollisionStrategy toBeDecorated,
                                        Vector2 windowDimensions,
                                        ImageReader imageReader) {
        super(toBeDecorated);
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        GameObjectCollection gameObjectCollection = this.getGameObjectCollection();
        createPaddleSizeChanger(thisObj, gameObjectCollection);
    }

    /*
     Creates a PaddleSizeChanger object.
     */
    private void createPaddleSizeChanger(GameObject brick, GameObjectCollection gameObjectCollection) {
        String imagePath;
        String resize;

        // randomly choose whether the changer will be
        // a shrinking or a widening changer.
        Random rand = new Random();
        if (rand.nextBoolean()) {
            imagePath = "assets/buffNarrow.png";
            resize = "shrink";
        }
        else {
            imagePath = "assets/BuffWiden.png";
            resize = "widen";
        }

        GameObject paddleSizeChanger = new PaddleSizeChanger(
                brick.getTopLeftCorner(),
                brick.getDimensions(),
                imageReader.readImage(imagePath, true),
                gameObjectCollection,
                windowDimensions,
                resize);

        paddleSizeChanger.setVelocity(Vector2.DOWN.mult(PADDLE_SIZE_CHANGER_SPEED));
        gameObjectCollection.addGameObject(paddleSizeChanger);
    }
}
