package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo of sun.
 * @author Adam Shtrasner and Noam Altman
 */
public class SunHalo {

    private static final String SUN_HALO_TAG = "sun halo";
    private static final float RADIUS = 300;

    /**
     * This function creates a halo around a given object that represents the sun.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created halo should be added.
     * @param sun A game object representing the sun (it will be followed by the created game object).
     * @param color The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {

        GameObject sunHalo = new GameObject(sun.getCenter(),
                Vector2.ONES.mult(RADIUS),
                new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);

        // set halo to the center of the sun
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects.addGameObject(sunHalo, layer);

        return sunHalo;
    }
}
