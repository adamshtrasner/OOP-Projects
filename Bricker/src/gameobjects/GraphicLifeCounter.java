package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Displays a graphic object on the game window showing as many widgets as lives left.
 */
public class GraphicLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private final int numOfLives;

    /**
     * Construct a new GameObject instance.
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param livesCounter lives counter of game.
     * @param widgetRenderable  image to use for widgets.
     * @param gameObjectCollection game object collection managed by game manager.
     * @param numOfLives setting of number of lives a player will have in a game.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            gameObjectCollection.removeGameObject(this, Layer.BACKGROUND);
        }
    }
}
