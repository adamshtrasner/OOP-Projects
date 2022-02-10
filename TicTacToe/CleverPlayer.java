/**
 * Mr.s Clever Class. A player that beats the WhateverPlayer most of the time.
 */
public class CleverPlayer implements Player {

    /**
     * The clever player strategy.
     * The strategy is simply marking a spot in the board by order from coordinates (0,0)
     * to (Board.SIZE - 1, Board.SIZE - 1). If a spot is already taken, we skip to
     * the next spot.
     * @param board the game's board
     * @param mark the mark to put on the board - X or O.
     */
    public void playTurn(Board board, Mark mark) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.putMark(mark, row, col)) {
                    return;
                }
            }
        }
    }
}
