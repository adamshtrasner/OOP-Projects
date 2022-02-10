package brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import gamemanager.BrickerGameManager;
import gameobjects.Ball;
import gameobjects.BallCollisionCountdownAgent;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Changes camera focus from ground to ball until ball collides
 * NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {

    private final WindowController windowController;
    private final BrickerGameManager gameManager;

    /*
     A tag attached to the ball so that the camera would know to follow it
     when the camera strategy is turned on
     */
    private static final String BALL_TAG = "ball";

    public ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                                WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (otherObj.getTag().equals(BALL_TAG)) {

            if (gameManager.getCamera() == null) {
                gameManager.setCamera(
                        new Camera(
                                otherObj, 			//object to follow
                                Vector2.ZERO, 	//follow the center of the object
                                windowController.getWindowDimensions().mult(1.2f),  //widen the frame a bit
                                windowController.getWindowDimensions()   //share the window dimensions
                        )
                );
                createBallCollisionCountdownAgent((Ball) otherObj);
            }
        }
    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange() {
        gameManager.setCamera(null);
    }

    private void createBallCollisionCountdownAgent(Ball ball) {
        BallCollisionCountdownAgent ballCollisionCountdownAgent =
                new BallCollisionCountdownAgent(ball,
                        this,
                        ball.getCollisionCount());
        this.getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
    }


}
