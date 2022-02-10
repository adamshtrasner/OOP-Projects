package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Displays a graphic object on the game window showing a numeric count of lives left.
 * @author Adam Shtrasner
 */
public class NumericLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final GameObjectCollection gameObjectCollection;
    private TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param livesCounter lives counter of game.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjectCollection game object collection managed by game manager.
     */
    public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner,
                              Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        // initialization
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.gameObjectCollection = gameObjectCollection;

        // initialization of the text
        initializeText();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRenderable.setString(String.format("Lives Left: %d", livesCounter.value()));
    }

    /*
    Initializes the numeric counter object.
     */
    private void initializeText() {
        textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        textRenderable.setColor(Color.red);
        GameObject textObject = new GameObject(topLeftCorner, dimensions, textRenderable);
        gameObjectCollection.addGameObject(textObject, Layer.BACKGROUND);
    }
}
