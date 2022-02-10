/**
 * Mr.Whatever class. This is a player class that inherits from RandomStrategy.
 */
public class WhateverPlayer extends RandomStrategy implements Player {

    /**
     *
     * @param board the game's board
     * @param mark the mark to put on the board - X or O.
     */
    public void playTurn(Board board, Mark mark) {
        playRandom(board, mark);
    }
}
