import java.util.Random;

/**
 * The Random Strategy class. This class implements 1 method which is a game's strategy
 * based on randomization. CleverPlayer and SnartypamtsPlayer inherit this class.
 */
public abstract class RandomStrategy {

    Random random = new Random();

    /**
     * This method picks a cell uniformly out of all the empty cells
     * to put the mark in.
     * @param board the game's board
     * @param mark the mark to put on the board - X or O.
     */
    protected void playRandom(Board board, Mark mark) {
        int row = random.nextInt(Board.SIZE);
        int col = random.nextInt(Board.SIZE);
        boolean success = board.putMark(mark, row, col);
        while (!success) {
            row = random.nextInt(Board.SIZE);
            col = random.nextInt(Board.SIZE);
            success = board.putMark(mark, row, col);
        }
    }
}
