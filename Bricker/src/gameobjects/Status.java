package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A Status object. An object of this instance falls from a brick when a ball hits it
 * and disappears when it collides with a paddle (either the main paddle or the mock one)
 */
public class Status extends GameObject {

    private final GameObjectCollection gameObjectCollection;

    /*
     A tag attached to the paddle/s of the game so that a status object
     will disappear only when hitting them.
     */
    private static final String PADDLE_TAG = "paddle";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Status(Vector2 topLeftCorner,
                             Vector2 dimensions,
                             Renderable renderable,
                             GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        // changes collision to happen only when hitting a paddle
        return other.getTag().equals(PADDLE_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjectCollection.removeGameObject(this);
    }
}
