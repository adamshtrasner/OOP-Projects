package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The extra paddle of the game, instantiated if the ball hit a brick with
 * the AddPaddleStrategy strategy.
 */
public class MockPaddle extends Paddle {

    public static boolean isInstantiated;
    private final GameObjectCollection gameObjectCollection;
    private int numCollisionsToDisappear;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object. Can be null, in which case
     * @param inputListener       listener object for user input.
     * @param windowDimensions    dimensions of game window.
     * @param minDistanceFromEdge border for paddle movement.
     */
    public MockPaddle(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      UserInputListener inputListener,
                      Vector2 windowDimensions,
                      GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge,
                      int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.numCollisionsToDisappear --;
        if (this.numCollisionsToDisappear == 0) {
            gameObjectCollection.removeGameObject(this);
        }
    }
}
