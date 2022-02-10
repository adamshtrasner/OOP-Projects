package brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;
import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * A strategy that randomizes 2 strategies out of the 5.
 * If one of the randomized strategies is also a multiple strategy, then
 * 3 strategies, not including the multiple strategy, will be randomized.
 */
public class MultipleStrategies extends RemoveBrickStrategyDecorator {

    private final CollisionStrategy puckStrategy;
    private final CollisionStrategy addPaddleStrategy;
    private final CollisionStrategy changeCameraStrategy;
    private final CollisionStrategy expandOrShrinkPaddleStrategy;

    public MultipleStrategies(CollisionStrategy toBeDecorated,
                              CollisionStrategy puckStrategy,
                              CollisionStrategy addPaddleStrategy,
                              CollisionStrategy changeCameraStrategy,
                              CollisionStrategy expandOrShrinkPaddleStrategy) {
        super(toBeDecorated);
        this.puckStrategy = puckStrategy;
        this.addPaddleStrategy = addPaddleStrategy;
        this.changeCameraStrategy = changeCameraStrategy;
        this.expandOrShrinkPaddleStrategy = expandOrShrinkPaddleStrategy;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        // Randomly pick 2 strategies out of 5 strategies,
        // including the multiple strategy.
        Random rand = new Random();
        int numStrategy = rand.nextInt(7);

        if (numStrategy == 0) {
            puckStrategy.onCollision(thisObj, otherObj, counter);
            addPaddleStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 1) {
            puckStrategy.onCollision(thisObj, otherObj, counter);
            changeCameraStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 2) {
            puckStrategy.onCollision(thisObj, otherObj, counter);
            expandOrShrinkPaddleStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 3) {
            addPaddleStrategy.onCollision(thisObj, otherObj, counter);
            changeCameraStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 4) {
            addPaddleStrategy.onCollision(thisObj, otherObj, counter);
            expandOrShrinkPaddleStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 5) {
            expandOrShrinkPaddleStrategy.onCollision(thisObj, otherObj, counter);
            changeCameraStrategy.onCollision(thisObj, otherObj, counter);
        }

        if (numStrategy == 6) {
            // if a multiple strategy is picked out of the 2 strategies,
            // we randomize between 3 strategies.
            numStrategy = rand.nextInt(3);

            if (numStrategy == 0) {
                puckStrategy.onCollision(thisObj, otherObj, counter);
                changeCameraStrategy.onCollision(thisObj, otherObj, counter);
                expandOrShrinkPaddleStrategy.onCollision(thisObj, otherObj, counter);
            }

            if (numStrategy == 1) {
                puckStrategy.onCollision(thisObj, otherObj, counter);
                changeCameraStrategy.onCollision(thisObj, otherObj, counter);
                addPaddleStrategy.onCollision(thisObj, otherObj, counter);
            }

            if (numStrategy == 2) {
                addPaddleStrategy.onCollision(thisObj, otherObj, counter);
                changeCameraStrategy.onCollision(thisObj, otherObj, counter);
                expandOrShrinkPaddleStrategy.onCollision(thisObj, otherObj, counter);
            }

        }
    }
}
