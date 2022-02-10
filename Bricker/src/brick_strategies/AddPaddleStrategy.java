package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.MockPaddle;
import gameobjects.Paddle;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces extra paddle to game window which remains until colliding
 * NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {

    private static final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE = 3;

    /*
     A tag attached to the paddle/s of the game so that a status object
     will disappear only when hitting them.
     */
    private static final String PADDLE_TAG = "paddle";

    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final Renderable renderable;

    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                             ImageReader imageReader,
                             UserInputListener inputListener,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.renderable = imageReader.readImage("assets/paddle.png", true);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);

        // Adds additional paddle to game and delegates to held object.
        GameObjectCollection gameObjectCollection = this.getGameObjectCollection();
        if (!ifMockPaddleExists()) {
            for (GameObject obj: gameObjectCollection) {
                if (obj instanceof Paddle && ! (obj instanceof MockPaddle)) {
                    createMockPaddle(obj, gameObjectCollection);
                    break;
                }
            }
        }
    }

    /*
    Checks if a Mock Paddle already exists in the game.
     */
    private boolean ifMockPaddleExists() {
        for (GameObject obj: this.getGameObjectCollection()) {
            if (obj instanceof MockPaddle) {
                return true;
            }
        }
        return false;
    }

    /*
     Creates a MockPaddle object.
     */
    private void createMockPaddle(GameObject paddle, GameObjectCollection gameObjectCollection) {
        GameObject mockPaddle = new MockPaddle(paddle.getTopLeftCorner(),
                paddle.getDimensions(),
                renderable,
                inputListener,
                windowDimensions,
                gameObjectCollection,
                20,
                NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE);
        mockPaddle.setCenter(windowDimensions.mult(0.5f));
        mockPaddle.setTag(PADDLE_TAG);
        getGameObjectCollection().addGameObject(mockPaddle);
    }
}
