package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Responsible for the creation and management of terrain.
 * @author Adam Shtrasner and Noam Altman
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final String TERRAIN_TAG = "ground";
    private static final String TOP_TERRAIN_TAG = "top_ground";

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final int topGroundLayer;
    private final Vector2 windowDimensions;
    private final NoiseGenerator noiseGenerator;

    private HashMap<Integer, ArrayList<GameObject>> blockMap = new HashMap<>();

    /**
     * Constructs the terrain.
     * @param gameObjects The collection of all participating game objects.
     * @param groundLayer The number of the layer to which the created ground objects
     *                    should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param seed A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.topGroundLayer  = groundLayer + 1;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed);
    }

    /**
     * This method return the ground height at a given location.
     * @param x A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        return 2/3f * windowDimensions.y() + 200 * noiseGenerator.noise(x / 20);
    }

    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int i = minX;
        while (i <= maxX) {
            int roundedX = (i/Block.SIZE) * Block.SIZE;
            int yTop = (int)Math.floor(groundHeightAt(roundedX) / Block.SIZE) * Block.SIZE;
            if(!blockMap.containsKey(roundedX)) {
                ArrayList<GameObject> blockArrCol = new ArrayList<>();
                GameObject topGroundBlock = new Block(
                        new Vector2(roundedX, yTop),
                        new RectangleRenderable
                                (ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                blockArrCol.add(topGroundBlock);
                topGroundBlock.setTag(TOP_TERRAIN_TAG);
                gameObjects.addGameObject(topGroundBlock, topGroundLayer);
                for (int j = yTop + Block.SIZE; j < windowDimensions.y() + (Block.SIZE * 10) ; j+=Block.SIZE) {
                    GameObject groundBlock = new Block(
                            new Vector2(roundedX, j),
                            new RectangleRenderable
                                    (ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                    blockArrCol.add(groundBlock);
                    groundBlock.setTag(TERRAIN_TAG);
                    gameObjects.addGameObject(groundBlock, groundLayer);
                }
                blockMap.put(roundedX, blockArrCol);
            }

            i+= Block.SIZE;
        }
    }

    /**
     * This method removes terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void removeInRange(int minX, int maxX) {
        int i = minX;
        while (i <= maxX) {
            int roundedX = (i/Block.SIZE) * Block.SIZE;
            if (blockMap.containsKey(roundedX)) {
                ArrayList<GameObject> blockList = blockMap.get(roundedX);
                gameObjects.removeGameObject(blockList.get(0), topGroundLayer);
                for (int j = 1; j < blockList.size(); j++) {
                    gameObjects.removeGameObject(blockList.get(j), groundLayer);
                }
                blockMap.remove(roundedX);
            }
            i += Block.SIZE;
        }
    }
}
