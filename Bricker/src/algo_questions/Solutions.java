package algo_questions;
import java.util.*;

/**
 * Solution to the algorithmic questions.
 * @author Adam Shtrasner
 */
public class Solutions {

    /**
     * Method computing the maximal amount of tasks out of n tasks that can be
     * completed with m time slots. A task can only be completed in a time slot
     * if the length of the time slot is grater than the no. of hours needed to
     * complete the task.
     * - A greedy solution.
     * @param tasks array of integers of length n. tasks[i] is the time in hours
     *             required to complete task i.
     * @param timeSlots array of integersof length m. timeSlots[i] is the length
     *                 in hours of the slot i.
     * @return maximal amount of tasks that can be completed
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        // sort arrays
        // expected runtime O(max(n,m)*log(max(n,m))
        // w.c O(max(n,m)^2)
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);

        // reverse arrays so that they will be sorted from high to low
        // runtime of (O(max(n,m))
        int[] tasksReversed = reverseArray(tasks);
        int[] timeSlotsReversed = reverseArray(timeSlots);

        int maxNumOfTasks = 0;
        int i = 0;
        int j = 0;
        while (i < tasks.length && j < timeSlots.length) {
            // iterating over all tasks by order and
            // taking a time slot each time it's possible.
            // runtime O(max(n,m))
            if (tasksReversed[i] <= timeSlotsReversed[j]) {
                maxNumOfTasks++;
                j++;
            }
            i++;
        }
        // total expected runtime O(max(n,m)*log(max(n,m))
        // w.c runtime O(max(n,m)^2) (depends on the runtime of the sort function of Array)
        return maxNumOfTasks;
    }
    
    /*
      A method to reverse the order of an array.
     */
    private static int[] reverseArray(int[] arr) {
        int[] rev = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rev[i] = arr[arr.length - i - 1];
        }
        return rev;
    }

    /**
     * Method computing the nim amount of leaps a frog needs to jump across n waterlily leaves,
     * from leaf 1 to leaf n. The leaves vary in size and how stable they are,
     * so some leaves allow larger leaps than others. leapNum[i] is an integer
     * telling you how many leaves ahead you can jump from leaf i.
     * If leapNum[3]=4, the frog can jump from leaf 3, and land on any of the leaves 4, 5, 6 or 7.
     * - A greedy solution.
     * @param leapNum array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i.
     * @return minimal no. of leaps to last leaf.
     */
    public static int minLeap(int[] leapNum) {
        int i = 0, indexToJump = 0, numLeaps = 0;
        int maxDistance;
        while (i != leapNum.length - 1) {
            // The greedy principle:
            // at each leaf we currently in,
            // we'll choose the next leaf from which we can jump to the furthest
            // leaf from.
            maxDistance = 0;
            for (int j = 1; j <= leapNum[i]; j++) {
                if (leapNum[i + j] + (i + j) >= maxDistance ||
                        leapNum[i + j] + (i + j) == leapNum.length - 1) {
                    maxDistance = leapNum[i + j] + (i + j);
                    indexToJump = i + j;
                }
            }
            i = indexToJump;
            numLeaps++;
        }
        // w.c O(n^2) where n is the number of leaves.
        return numLeaps;
    }

    /**
     * Method computing the solution to the following problem:
     * A boy is filling the water trough for his father's cows in their village.
     * The trough holds n liters of water. With every trip to the village well,
     * he can return using either the 2 bucket yoke, or simply with a single bucket.
     * A bucket holds 1 liter. In how many different ways can he fill the water trough?
     * n can be assumed to be greater or equal to 0, less than or equal to 48.
     * - The solution is implemented with dynamic programming.
     * @param n number of liters of water the trough holds
     * @return valid output of algorithm.
     */
    public static int bucketWalk(int n) {
        // table to store results of the sub problems
        int[] bucketTable = new int[n + 2];

        // initialize first three values in table
        bucketTable[0] = bucketTable[1] = 1;

        // fill entries in bucketTable[]
        for (int i = 2; i <= n; i++) {
            // to fill i liters of water
            // we add the number of ways to fill
            // (i - 1) and (i - 2) liters.
            bucketTable[i] = bucketTable[i - 1] + bucketTable[i - 2];
        }

        // return last entry
        return bucketTable[n];
    }

    /**
     * Method computing the solution to the following problem: Given an integer n,
     * return the number of structurally unique BST's (binary search trees) which has
     * exactly n nodes of unique values from 1 to n. You can assume n is at least 1
     * and at most 19.
     * - The solution is implemented with dynamic programming.
     * @param n number of nodes with unique values from 1 to n
     * @return he number of structurally unique BST's (binary search trees) which has
     *         exactly n nodes of unique values from 1 to n.
     */
    public static int numTrees(int n)  {
        // table to store results of subproblems
        int[] numBST = new int[n + 2];

        // initialize first two values in table
        numBST[0] = 1;
        numBST[1] = 1;

        // fill entries in numBST[]
        // runtime O(n^2)
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                numBST[i] += numBST[j - 1] * numBST[i - j];
            }
        }

        // return last entry
        return numBST[n];
    }

}
