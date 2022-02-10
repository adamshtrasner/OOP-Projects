package gamemanager;

import brick_strategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.*;
import java.util.Random;

/**
 * Game manager - this class is responsible for game initialization,
 * holding references for game objects and calling update methods for
 * every update iteration.
 * @author Adam Shtrasner
 * @see danogl.GameManager
 */
public class BrickerGameManager extends GameManager {

    /*
    Window dimensions through in the X axis
     */
    private static final float WINDOW_X = 700;
    /*
    Window dimensions through in the Y axis
     */
    private static final float WINDOW_Y = 500;

    public static final int BORDER_WIDTH = 20;
    private static final int NUM_OF_LIVES = 4;

    private static final float BALL_SPEED = 200;
    private static final float BALL_RADIUS = 20;

    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;

    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_COLS = 8;
    private static final float BRICK_ROWS = 5;

    private static final float NUMERIC_LIFE_COUNTER_RADIUS = 20;

    /*
     A tag attached to the paddle/s of the game so that a status object
     will disappear only when hitting them.
     */
    private static final String PADDLE_TAG = "paddle";

    /*
     A tag attached to the ball so that the camera would know to follow it
     when the camera strategy is turned on
     */
    private static final String BALL_TAG = "ball";
    private static final String TITLE = "Bricker";

    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter counter;
    private Counter livesCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    /**
     * Constructor
     * @param windowTitle title of the window
     * @param windowDimensions dimensions of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * Initialization of the Bricker game -
     * creating the ball, paddle, walls, background,
     * @param imageReader an image reader
     * @param soundReader a sound reader
     * @param inputListener an input listener
     * @param windowController a window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;

        // initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.counter = new Counter(0);
        this.livesCounter = new Counter(NUM_OF_LIVES);

        Renderable ballImage = imageReader.readImage("assets/ball.png",
                true);
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                true);
        Renderable brickImage = imageReader.readImage("assets/brick.png",
                true);
        Renderable heartImage = imageReader.readImage("assets/heart.png",
                true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");

        // creating ball
        createBall(ballImage, collisionSound, windowDimensions);

        // create paddle
        createPaddle(paddleImage, inputListener, windowDimensions);

        // create bricks
        createBricks(brickImage, windowDimensions);

        // create walls
        createWalls(windowDimensions);

        // set background
        setBackground(windowController, imageReader);

        // create numeric life counter
        createNumericLifeCounter();

        // create graphic life counter
        createGraphicLifeCounter(heartImage);

    }

    /**
     * Repositions the ball in the center and tosses it in random directions.
     * @param ball the game object ball
     */
    public void repositionBall(GameObject ball) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();

        // removing every object that has gone out of bounds from below
        float objectHeight;
        for (GameObject obj: gameObjects()) {
            objectHeight = obj.getCenter().y();
            if (objectHeight > windowDimensions.y()) {
                gameObjects().removeGameObject(obj);
            }
        }
    }

    /*
    Checks whether the ball exceeded the limits from below or all bricks are gone.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();

        String prompt = "";
        if (ballHeight > windowDimensions.y()) {
            // we lose
            this.livesCounter.decrement();
            if (this.livesCounter.value() == 0) {
                prompt = "You Lose!";
            }
            else {
                repositionBall(this.ball);
            }
        }
        if (this.counter.value() == 0) {
            // we win
            prompt = "You Won!";
        }

        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }


    /*
    Creates the ball - the main object of the game.
    */
    private void createBall(Renderable ballImage, Sound collisionSound, Vector2 windowDimensions) {
        this.ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        ball.setTag(BALL_TAG);
        repositionBall(this.ball);

        gameObjects().addGameObject(ball);

    }

    /*
    Creates the paddle - one of the main game objects.
     */
    private void createPaddle(Renderable paddleImage,
                              UserInputListener inputListener,
                              Vector2 windowDimensions) {
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions,
                BORDER_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()-30));
        paddle.setTag(PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /*
    Creates BRICK_ROWS rows and BRICK_COLS columns of bricks
     */
    private void createBricks(Renderable brickImage,
                             Vector2 windowDimensions) {

        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(gameObjects(),
                this,
                this.imageReader,
                this.soundReader,
                this.inputListener,
                this.windowController,
                this.windowDimensions
                );
        float brick_length = (windowDimensions.x() - 2 * BORDER_WIDTH - BRICK_COLS - 1)
                / BRICK_COLS;

        float x_start, y_start;
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                if (col == 0) {
                    x_start = BORDER_WIDTH;
                } else {
                    x_start = BORDER_WIDTH + brick_length * col + col;
                }
                if (row == 0) {
                    y_start = BORDER_WIDTH;
                } else {
                    y_start = BORDER_WIDTH + BRICK_HEIGHT * row + row;
                }

                // increment number of bricks with each brick adding
                this.counter.increment();
                GameObject brick = new Brick(
                        new Vector2(x_start, y_start),
                        new Vector2((windowDimensions.x() - 2 * BORDER_WIDTH - BRICK_COLS - 1) /
                                BRICK_COLS, BRICK_HEIGHT),
                        brickImage,
                        brickStrategyFactory.getStrategy(),
                        this.counter
                );
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /*
    Creates the numeric life counter object.
     */
    private void createNumericLifeCounter() {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(
                this.livesCounter,
                new Vector2(5, windowDimensions.y() - 60),
                new Vector2(NUMERIC_LIFE_COUNTER_RADIUS, NUMERIC_LIFE_COUNTER_RADIUS),
                gameObjects()
        );
        gameObjects().addGameObject(numericLifeCounter);
    }

    /*
    Creates the Graphic life counter object.
     */
    private void createGraphicLifeCounter(Renderable heartImage) {
        GraphicLifeCounter[] graphicLifeCounter = new GraphicLifeCounter[NUM_OF_LIVES];
        for (int i = 1; i <= this.livesCounter.value(); i++) {
            graphicLifeCounter[i-1] = new GraphicLifeCounter(
                    new Vector2(5 + 30*(i-1), windowDimensions.y() - 30),
                    new Vector2(20, 20),
                    this.livesCounter,
                    heartImage,
                    gameObjects(),
                    i
            );
            gameObjects().addGameObject(graphicLifeCounter[i-1], Layer.BACKGROUND);
        }
    }

    /*
     * Sets the background for the game.
     */
    private void setBackground(WindowController windowController, ImageReader imageReader) {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/3386851.jpg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /*
     * Create borders for the game.
     */
    private void createWalls(Vector2 windowDimensions) {
        // left wall
        createLeftWall(windowDimensions);

        // right wall
        createRightWall(windowDimensions);

        // upper wall
        createUpperWall(windowDimensions);

    }

    /*
     * Creates the left border.
     */
    private void createLeftWall(Vector2 windowDimensions) {
        gameObjects().addGameObject(

                new GameObject(
                        //anchored at top-left corner of the screen
                        Vector2.ZERO,

                        //height of border is the height of the screen
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),

                        //Rectangle shaped border
                        null
                )
                , Layer.STATIC_OBJECTS

        );
    }

    /*
     * Creates the right border.
     */
    private void createRightWall(Vector2 windowDimensions) {
        gameObjects().addGameObject(

                new GameObject(
                        //anchored at top-left corner of the screen
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),

                        //height of border is the height of the screen
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),

                        //Rectangle shaped border
                        null
                )
                , Layer.STATIC_OBJECTS

        );

    }

    /*
     * Creates the upper border.
     */
    private void createUpperWall(Vector2 windowDimensions) {
        gameObjects().addGameObject(

                new GameObject(
                        //anchored at top-left corner of the screen
                        Vector2.ZERO,

                        //height of border is the height of the screen
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),

                        //Rectangle shaped border
                        null
                )
                , Layer.STATIC_OBJECTS

        );

    }



    public static void main(String[] args) {
        // Entry point for game
        new BrickerGameManager(TITLE, new Vector2(WINDOW_X, WINDOW_Y)).run();
    }
}
