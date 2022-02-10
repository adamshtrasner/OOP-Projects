package gameobjects;

import brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * An object that the ball eliminates.
 */
public class Brick extends GameObject {

    private final CollisionStrategy collisionStrategy;
    private final Counter counter;
    private boolean isOnCollisionEnterCalled = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param collisionStrategy CollisionStrategy instance
     * @param counter Counter for number of bricks in the game
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.counter = counter;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // this piece of code prevents reducing the counter of a brick
        // multiple times although it already collided with the ball
        // (which can happen when multiple balls collide with the brick
        //  at the same time)
        if (!isOnCollisionEnterCalled) {
            collisionStrategy.onCollision(this, other, counter);
            isOnCollisionEnterCalled = true;
        }
    }
}
