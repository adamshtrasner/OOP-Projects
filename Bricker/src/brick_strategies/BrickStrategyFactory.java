package brick_strategies;


import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import gamemanager.BrickerGameManager;
import java.util.Random;

/**
 * Factory class for creating Collision strategies
 */
public class BrickStrategyFactory {

    private final Random rand = new Random();
    private final GameObjectCollection gameObjectCollection;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;

    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                BrickerGameManager gameManager,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions) {

        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    /**
     * method randomly selects between 5 strategies and returns one CollisionStrategy
     * object which is a RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies, or decorated by one of the decorator
     * strategies and a pair of additional two decorator strategies.
     * @return
     */
    public CollisionStrategy getStrategy() {
        CollisionStrategy removeBrickStrategy = new RemoveBrickStrategy(gameObjectCollection);

        CollisionStrategy puckStrategy = new PuckStrategy(removeBrickStrategy,
                imageReader,
                soundReader);

        CollisionStrategy addPaddleStrategy = new AddPaddleStrategy(removeBrickStrategy,
                imageReader,
                inputListener,
                windowDimensions);

        CollisionStrategy expandOrShrinkPaddleStrategy = new ExpandOrShrinkPaddleStrategy(
                removeBrickStrategy,
                windowDimensions,
                imageReader);

        CollisionStrategy changeCameraStrategy = new ChangeCameraStrategy(removeBrickStrategy,
                windowController,
                gameManager);

        CollisionStrategy multipleStrategy = new MultipleStrategies(removeBrickStrategy,
                puckStrategy,
                addPaddleStrategy,
                changeCameraStrategy,
                expandOrShrinkPaddleStrategy);

        // choose randomly between the possible brick strategies
        int numStrategy = this.rand.nextInt(6);

        if (numStrategy == 0) {
            return removeBrickStrategy;
        }

        if (numStrategy == 1) {
            return puckStrategy;
        }

        if (numStrategy == 2) {
            return addPaddleStrategy;
        }

        if (numStrategy == 3) {
            return expandOrShrinkPaddleStrategy;
        }

        if (numStrategy == 4) {
            return changeCameraStrategy;
        }

        return multipleStrategy;
    }
}


