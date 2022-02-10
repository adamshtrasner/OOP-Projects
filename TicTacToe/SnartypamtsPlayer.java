/**
 * Mr.Snartypamts class. This is a player class that inherits from RandomStrategy,
 * but also implements another strategy.
 */
public class SnartypamtsPlayer extends RandomStrategy implements Player {

    /**
     * A signle turn implementation of Mr.Snartypamts.
     * Full detailed explanation in the README file.
     * @param board the game's board
     * @param mark the mark to put on the board - X or O.
     */
    public void playTurn(Board board, Mark mark) {
        int row = 0;
        boolean success = board.putMark(mark, row, Board.WIN_STREAK - 1);
        while (!success && row != Board.WIN_STREAK) {
            success = board.putMark(mark, ++row, Board.WIN_STREAK - 1);
        }
        if (!success) {
            playRandom(board, mark);
        }
    }
}
