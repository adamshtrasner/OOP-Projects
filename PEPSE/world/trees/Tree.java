package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 * @author Adam Shtrasner and Noam Alterman
 */
public class Tree {

    private static final int MINIMAL_HEIGHT = 270;
    private static final int MAXIMAL_HEIGHT = 330;
    private static final String TREE_TAG = "tree";

    private GameObjectCollection gameObjects;
    private int treeLayer;
    private final int leafLayer;
    private int seed;
    private Vector2 windowDimensions;
    private Function<Float, Float> groundHeightAt;

    private Random rand;

    private HashMap<Float, ArrayList<GameObject>> blockTreeMap = new HashMap<>();

    /**
     *
     * @param gameObjects The collection of all participating game objects.
     * @param treeLayer The number of the layer to which the created tree should be added.
     * @param leafLayer The number of the layer to which the created leaf should be added.
     * @param windowDimensions The number of the layer to which the created game object
     *      *                        should be added.
     * @param groundHeightAt the groundHeight function from Terrain
     */
    public Tree(GameObjectCollection gameObjects,
                int treeLayer,
                int leafLayer,
                int seed,
                Vector2 windowDimensions,
                Function<Float, Float> groundHeightAt) {

        this.gameObjects = gameObjects;
        this.treeLayer = treeLayer;
        this.leafLayer = leafLayer;
        this.seed = seed;
        this.windowDimensions = windowDimensions;
        this.groundHeightAt = groundHeightAt;
    }

    /**
     * This method creates trees in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        float minXFloor = ((int)(minX/Block.SIZE)) * Block.SIZE;
        for (float xCoordinateByBlock = minXFloor; xCoordinateByBlock < maxX; xCoordinateByBlock += Block.SIZE) {
            rand = new Random(Objects.hash(xCoordinateByBlock, seed));
            int randInt = rand.nextInt(15);
            if (randInt < 1) {
                float yGround = (groundHeightAt.apply(xCoordinateByBlock));
                float randomHeight = MINIMAL_HEIGHT +
                        rand.nextInt(MAXIMAL_HEIGHT - MINIMAL_HEIGHT);
                int roundedRandomHeight = (int) Math.floor((randomHeight / Block.SIZE)) * Block.SIZE;
                if(!blockTreeMap.containsKey(xCoordinateByBlock)) {
                    ArrayList<GameObject> treeList = new ArrayList<>();
                    for (int j = (int) yGround - roundedRandomHeight; j < yGround; j += Block.SIZE) {
                        GameObject baseTreeBlock = new Block(
                                new Vector2(xCoordinateByBlock, j),
                                new RectangleRenderable
                                        (new Color(100, 50, 20)));
                        treeList.add(baseTreeBlock);
                        baseTreeBlock.setTag(TREE_TAG);
                        gameObjects.addGameObject(baseTreeBlock, treeLayer);
                    }
                    blockTreeMap.put(xCoordinateByBlock, treeList);
                    Leaf.create(gameObjects, leafLayer, rand, new Vector2(xCoordinateByBlock,
                            yGround - roundedRandomHeight));
                }
            }
        }
    }

    /**
     * This method removes trees in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void removeInRange(int minX, int maxX) {
        float minXFloor = ((int)(minX/Block.SIZE)) * Block.SIZE;
        for (float xCoordinateByBlock = minXFloor; xCoordinateByBlock < maxX; xCoordinateByBlock += Block.SIZE) {
            if(blockTreeMap.containsKey(xCoordinateByBlock)) {
                ArrayList<GameObject> treeList = blockTreeMap.get(xCoordinateByBlock);
                for (GameObject gameObject : treeList) {
                    gameObjects.removeGameObject(gameObject, treeLayer);
                }
                blockTreeMap.remove(xCoordinateByBlock);
                Leaf.remove(gameObjects, leafLayer, xCoordinateByBlock);
            }
        }
    }
}
