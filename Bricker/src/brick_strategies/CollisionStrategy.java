package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Says what to do when brick collided with ball.
 * @author Adam Shtrasner
 */
public interface CollisionStrategy {

    public GameObjectCollection getGameObjectCollection();
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

}
