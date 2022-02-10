package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * A decorator for RemoveBrickStrategy.
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {

    private final CollisionStrategy toBeDecorated;

    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated) {
        this.toBeDecorated = toBeDecorated;
    }

    public GameObjectCollection getGameObjectCollection() {
        return toBeDecorated.getGameObjectCollection();
    }

    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        toBeDecorated.onCollision(thisObj, otherObj, counter);
    }

}
