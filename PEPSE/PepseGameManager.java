package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The main class of the simulator.
 * @author Adam Shtrasner and Noam Altman
 */
public class PepseGameManager extends GameManager {

    private static final int CYCLE_LENGTH = 30;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int TOP_GROUND_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TREE_LAYER = Layer.BACKGROUND + 20;
    private static final int LEAF_LAYER = Layer.BACKGROUND + 30;
    private static final int AVATAR_LAYER = Layer.DEFAULT;

    private final int gameSeed = new Random().nextInt(20);

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    private Terrain terrain;
    private Tree tree;
    private Avatar avatar;
    private float currentLocatonXAvatar = 640;
    private float range = 0;
    private float windowDimensionsX;
    private ArrayList<Integer> activeLayers = new ArrayList<Integer>(
            Arrays.asList(TOP_GROUND_LAYER, GROUND_LAYER, TREE_LAYER, LEAF_LAYER)
    );

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensionsX = windowController.getWindowDimensions().x() + Block.SIZE*2;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // create sky
        initializeSky();

        // create terrain
        initializeTerrain();

        // create night
        initializeNight();

        // create sun and sun halo
        initializeSun();

        // create trees
        initializeTrees();

        // create avatar
        initializeAvatar();
    }

    /**
     * Runs the entire simulation.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /*
     Initializes the sky.
     */
    private void initializeSky() {
        GameObject sky = Sky.create(gameObjects(),
                windowController.getWindowDimensions(),
                SKY_LAYER);
    }

    /*
    Initializes the terrain.
     */
    private void initializeTerrain() {
        this.terrain = new Terrain(gameObjects(),
                GROUND_LAYER,
                windowController.getWindowDimensions(),
                gameSeed);
        terrain.createInRange(0, (int)windowController.getWindowDimensions().x() + 2*Block.SIZE);
    }

    /*
    Initializes night mode.
     */
    private void initializeNight() {
        GameObject night = Night.create(gameObjects(),
                NIGHT_LAYER,
                windowController.getWindowDimensions(),
                CYCLE_LENGTH);
    }

    /*
    Initializes the sun and calls for initialization of its halo.
     */
    private void initializeSun() {
        GameObject sun = Sun.create(gameObjects(),
                SUN_LAYER,
                windowController.getWindowDimensions(),
                CYCLE_LENGTH);
        initializeSunHalo(sun);
    }

    /*
    Initializes the sun halo.
     */
    private void initializeSunHalo(GameObject sun) {
        GameObject sunHalo = SunHalo.create(gameObjects(),
                SUN_HALO_LAYER,
                sun,
                new Color(255, 255, 0, 20));
    }

    /*
    Initializes the trees.
     */
    private void initializeTrees() {
        tree = new Tree(gameObjects(),
                TREE_LAYER,
                LEAF_LAYER,
                gameSeed,
                windowController.getWindowDimensions(),
                terrain::groundHeightAt);
        tree.createInRange(0, (int) windowController.getWindowDimensions().x());
        gameObjects().layers().shouldLayersCollide(TOP_GROUND_LAYER, LEAF_LAYER, true);
    }

    /*
    Initializes the avatar.
     */
    private void initializeAvatar() {
        avatar = Avatar.create(gameObjects(),
                AVATAR_LAYER,
                new Vector2(windowController.getWindowDimensions().x() / 2,
                        terrain.groundHeightAt(0) - Block.SIZE * 4),
                inputListener,
                imageReader);

        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TREE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TOP_GROUND_LAYER, true);

        // Sets camera to focus on avatar
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float currDistance = avatar.getTopLeftCorner().x() - currentLocatonXAvatar;
        if(currDistance > Block.SIZE || currDistance < -Block.SIZE) {
            if (currDistance > 0) {
                terrain.createInRange((int) (windowDimensionsX + range),
                        (int) (windowDimensionsX + range + currDistance));
                tree.createInRange((int) (windowDimensionsX + range),
                        (int) (windowDimensionsX + range + currDistance));
                terrain.removeInRange((int) (range), (int) (range + currDistance));
                tree.removeInRange((int) (range), (int) (range + currDistance));
            }
            if (currDistance < 0) {
                terrain.createInRange((int) (range + currDistance), (int) range);
                tree.createInRange((int) (range + currDistance), (int) range);
                terrain.removeInRange((int) (windowDimensionsX + range + currDistance),
                        (int) (windowDimensionsX + range));
                tree.removeInRange((int) (windowDimensionsX + range + currDistance),
                        (int) (windowDimensionsX + range));
            }
            currentLocatonXAvatar = avatar.getTopLeftCorner().x();
            range = range + currDistance;
        }

    }
}
