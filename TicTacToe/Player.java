/**
 * A Player interface.
 */
interface Player {

    /**
     * A single turn implementation of a player.
     * @param board the game's board
     * @param mark the mark to put on the board - X or O.
     */
    void playTurn(Board board, Mark mark);
}
