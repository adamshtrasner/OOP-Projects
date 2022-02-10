package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Says what to do when brick collided with ball.
 */
public class RemoveBrickStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructs a new RemoveBrickStrategy instance.
     * @param gameObjectCollection game object collection managed by game manager.
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * A getter for the game object collection instance
     * @return a game object collection
     */
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }

    /**
     * This function removes thisObj when otherObj collides with it,
     * and decrements a counter of an object that's collided.
     * In this version we're only talking about the Brick object and its counter.
     * @param thisObj The collided object
     * @param otherObj The object that collides
     * @param counter Counter for number of bricks
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if (gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            counter.decrement();
        }
    }
}
