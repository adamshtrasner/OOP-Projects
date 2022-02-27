package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 * @author Adam Shtrasner and Noam Altman
 */
public class Sun {

    private static final String SUN_TAG = "sun";
    private static final float RADIUS = 150;

    /**
     * This function creates a yellow circle that moves in
     * the sky in an elliptical path (in camera coordinates).
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created sun should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param cycleLength The amount of seconds it should take the created
     *                   game object to complete a full cycle.
     * @return A new game object representing the sun.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {

        // creating the sun object
        GameObject sun = new GameObject(Vector2.ZERO,
                Vector2.ONES.mult(RADIUS),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        // creating the transition
        new Transition<Float>(
                sun, //the game object being changed
                angleInSky ->  sun.setCenter(calcSunPosition(windowDimensions, angleInSky)),  //the method to call
                (float)(-0.5 * Math.PI),    //initial transition value
                (float) (1.5 * Math.PI),   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a linear interpolator
                cycleLength,   // transition fully over a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);  //nothing further to execute upon reaching final value

        gameObjects.addGameObject(sun, layer);
        return sun;
    }

    /*
     This function calculates the center of the sun object according to angle in the sky.
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky) {
        float x = (float) (windowDimensions.x() * 0.5f * Math.cos(angleInSky) + windowDimensions.x() * 0.5);
        float y = (float) (windowDimensions.y() * 0.5f * Math.sin(angleInSky) + windowDimensions.y() * 0.5);
        return new Vector2(x, y);
    }
}
