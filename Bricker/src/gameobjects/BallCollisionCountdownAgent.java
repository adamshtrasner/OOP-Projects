package gameobjects;

import danogl.GameObject;
import brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball
 * with a brick with a change camera strategy. It checks ball's
 * collision counter every frame, and once it finds the ball
 * has collided countDownValue times since instantiation, it calls
 * the strategy to reset the camera to normal.
 */
public class BallCollisionCountdownAgent extends GameObject {

    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private final Ball ball;
    private final ChangeCameraStrategy owner;
    private final int countDownValue;

    /**
     *Construct a new BallCollisionCountdownAgent instance.
     *
     * @param ball Ball object whose collisions are to be counted.
     * @param owner Object asking for countdown notification.
     * @param countDownValue Number of ball collisions. Notify caller
     *                      object that the ball collided countDownValue
     *                      times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball,
                                       ChangeCameraStrategy owner,
                                       int countDownValue) {
        super(ball.getTopLeftCorner(), ball.getDimensions(), null);
        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() - countDownValue >= NUM_BALL_COLLISIONS_TO_TURN_OFF) {
            this.owner.turnOffCameraChange();
            owner.getGameObjectCollection().removeGameObject(this);
        }
    }
}
