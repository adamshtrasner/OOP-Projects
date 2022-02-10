package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A Status object. The status that is changed is the length of a paddle
 * (either the main paddle or the mock one).
 */
public class PaddleSizeChanger extends Status {
    private static final float SHRINK_FACTOR = 0.5f;
    private static final float WIDEN_FACTOR = 2f;
    private static final float MIN_PADDLE_SIZE = 25f;
    private final Vector2 windowDimensions;

    /*
     A string that equals either to "shrink" or "widen",
     so that we know whether to shrink or widen the paddle accordingly.
     */
    private final String resize;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height in window coordinates.
     * @param renderable           The renderable representing the object. Can be null,
     *                             in which case
     * @param gameObjectCollection game object collection managed by game manager.
     * @param windowDimensions     dimensions of game window.
     * @param resize               a string, either "shrink" or "widen".
     */
    public PaddleSizeChanger(Vector2 topLeftCorner,
                             Vector2 dimensions,
                             Renderable renderable,
                             GameObjectCollection gameObjectCollection,
                             Vector2 windowDimensions,
                             String resize) {
        super(topLeftCorner, dimensions, renderable, gameObjectCollection);
        this.windowDimensions = windowDimensions;
        this.resize = resize;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Shrinks or widens the paddle according to resize
        if (other.getDimensions().x() >= MIN_PADDLE_SIZE &&
                other.getDimensions().x() * WIDEN_FACTOR <= windowDimensions.x()) {

            if (resize.equals("shrink")) {
                other.setDimensions(
                        new Vector2(other.getDimensions().x() * SHRINK_FACTOR,
                                other.getDimensions().y()));
            }
            else if (resize.equals("widen")){
                other.setDimensions(
                        new Vector2(other.getDimensions().x() * WIDEN_FACTOR,
                                other.getDimensions().y()));
            }
        }
    }
}
