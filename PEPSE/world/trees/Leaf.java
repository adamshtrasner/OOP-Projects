package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Responsible for the creation of leaves on top of a tree.
 * @author Adam Shtrasner and Noam Altman
 */
public class Leaf extends Block {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";
    private static final int RADIUS =  120;
    private static final int MIN_LIFE_LONG = 3;
    private static final int MAX_LIFE_LONG = 100;
    private static final float DROPOUT_Y_VELOCITY = 70f;
    private static final float DROPOUT_X_VELOCITY = 50f;

    private static float waitTimeBeforeMoving;
    private static int waitTimeBeforeDropout;
    private static int waitTimeBeforeDeath;
    private static int timeToFadeout = 10;

    private Transition<Float> horizontalTransition;
    private int leafLayer;

    private static final HashMap<Float, ArrayList<Leaf>> leafMap = new HashMap<>();

    public Leaf(Vector2 topLeftCorner, Renderable renderable, int leafLayer) {
        super(topLeftCorner, renderable);
        this.leafLayer = leafLayer;
    }

    /**
     * This method creates square of leaves of one tree.
     * @param center location of the top-left corner of the highest block of the tree trunk.
     */
    public static void create(GameObjectCollection gameObjects,
                              int leafLayer,
                              Random random,
                              Vector2 center){
        if(!leafMap.containsKey(center.x())) {
            ArrayList<Leaf> treeLeavesArr = new ArrayList<>();
            for (float i = center.x() - RADIUS; i < center.x() + Block.SIZE + RADIUS; i += Block.SIZE) {
                for (float j = center.y() - RADIUS; j < center.y() + Block.SIZE + RADIUS; j += Block.SIZE) {
                    int randInt = random.nextInt(10);
                    if (randInt < 6) {
                        final float finalI = i;
                        final float finalJ = j;
                        Leaf leafObject = new Leaf(new Vector2(i, j),
                                new RectangleRenderable(LEAF_COLOR),
                                leafLayer);

                        moveLeaf(leafObject, random);

                        circleOfLifeLeaf(leafObject, new Vector2(finalI, finalJ), gameObjects, leafLayer,
                                random);

                        treeLeavesArr.add(leafObject);
                        leafObject.setTag(LEAF_TAG);
                        gameObjects.addGameObject(leafObject, leafLayer);
                    }
                }
            }
            leafMap.put(center.x(), treeLeavesArr);
        }
    }

    /*
     Creates the schedule task which is responsible for the movement of the leaves.
     */
    private static void moveLeaf(Leaf leafObject, Random random){
        waitTimeBeforeMoving = random.nextFloat();
        // delaying the move of the leaf:
        new ScheduledTask(leafObject,
                waitTimeBeforeMoving,
                false,
                () -> addsTransitionsMoveLeaf(leafObject));
    }

    /*
     This method adds the 2 transitions that takes care on the move of the leaf.
     */
    private static void addsTransitionsMoveLeaf(Leaf leafObject){
        // creating the transitions:
        Transition<Float> wind_move_angle_transition = new Transition<Float>(
                leafObject, //the game object being changed
                angle ->  leafObject.renderer().setRenderableAngle(angle),  //the method to call
                0f,    //initial transition value
                (float) Math.PI / 2,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a linear interpolator
                5,   // transition fully over a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value

        Transition<Vector2> wind_move_width_transition = new Transition<Vector2>(
                leafObject, //the game object being changed
                leafObject::setDimensions,  //the method to call
                new Vector2(Block.SIZE, Block.SIZE),    //initial transition value
                new Vector2(Block.SIZE - 5, Block.SIZE - 5),   //final transition value
                Transition.LINEAR_INTERPOLATOR_VECTOR,  //use a linear interpolator
                5,   // transition fully over a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value
    }

    /*
     Sets the circle of life of a leaf.
     */
    private static void circleOfLifeLeaf(Leaf leafObject, Vector2 originLocation,
                                         GameObjectCollection gameObjects,  int leafLayer,
                                         Random random){
        int IfDropedOut = random.nextInt(10);
        if(IfDropedOut < 5) {
            waitTimeBeforeDropout = random.nextInt(MAX_LIFE_LONG - MIN_LIFE_LONG) + MIN_LIFE_LONG;
            new ScheduledTask(leafObject,
                    waitTimeBeforeDropout,
                    false,
                    () -> leafDropout(leafObject, originLocation, gameObjects, leafLayer, random));
        }
    }

    /*
     Function responsible for the drop out of leaves.
     */
    private static void leafDropout(Leaf leafObject, Vector2 originLocation,
                                    GameObjectCollection gameObjects,  int leafLayer,
                                    Random random){
        leafObject.transform().setVelocityY(DROPOUT_Y_VELOCITY);
        leafObject.horizontalTransition = new Transition<Float>(
                leafObject, //the game object being changed
                vel ->  leafObject.transform().setVelocityX(vel),  //the method to call
                0f,    //initial transition value
                DROPOUT_X_VELOCITY,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a linear interpolator
                1,   // transition fully over a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value
        leafObject.renderer().fadeOut(timeToFadeout, () -> leafDeath(leafObject,
                originLocation, gameObjects, leafLayer, random));
    }

    /*
     Function responsible for the disappearance of the leaves.
     */
    private static void leafDeath(Leaf leafObject, Vector2 originLocation,
                                  GameObjectCollection gameObjects,  int leafLayer,
                                  Random random){
        waitTimeBeforeDeath = random.nextInt(5);
        new ScheduledTask(leafObject,
                waitTimeBeforeDeath,
                false,
                () -> leafRebirth(leafObject, originLocation, gameObjects, leafLayer, random));
    }

    /*
     Function responsible for the reconstruction of a leaf.
     */
    private static void leafRebirth(Leaf leafObject, Vector2 originalLocation,
                                    GameObjectCollection gameObjects, int leafLayer,
                                    Random random){
        gameObjects.removeGameObject(leafObject);
        leafObject = new Leaf(originalLocation, new RectangleRenderable(LEAF_COLOR), leafLayer);
        leafObject.setTag(LEAF_TAG);
        gameObjects.addGameObject(leafObject, leafLayer);

        Leaf.moveLeaf(leafObject, random);
        Leaf.circleOfLifeLeaf(leafObject, originalLocation, gameObjects, leafLayer, random);
    }

    /**
     * This method removes leaves of the tree in a given x coordinate.
     * @param gameObjects The collection of all participating game objects.
     * @param leafLayer The number of the layer to which the created leaf should be added.
     * @param x x coordinate of the tree
     */
    public static void remove(GameObjectCollection gameObjects, int leafLayer, float x){
        if(leafMap.containsKey(x)) {
            ArrayList<Leaf> treeLeavesList = leafMap.get(x);
            for (Leaf leaf : treeLeavesList) {
                gameObjects.removeGameObject(leaf, leafLayer);
            }
            leafMap.remove(x);
        }
    }

    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        this.transform().setVelocityY(0);
        this.transform().setVelocityX(0);
        other.transform().setVelocity(Vector2.ZERO);
        this.removeComponent(horizontalTransition);
    }
}
