/**
 * The Game class. This class implements a single round of Tic Tac Toe.
 */
public class Game {

    Player[] players = new Player[2];
    Mark[] marks = {Mark.X, Mark.O};
    Renderer renderer;

    /**
     * Initializes the game
     * @param playerX the player that draws X
     * @param playerO the player that draws O
     * @param renderer the renderer
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.players[0] = playerX;
        this.players[1] = playerO;
        this.renderer = renderer;
    }

    /**
     * The main program of the game.
     * @return X - if playerX has won.
     *         O - if playerO has won.
     *         BLANK - if the game ended with a tie.
     */
    public Mark run() {
        Board board = new Board();
        renderer.renderBoard(board);

        while (true) {
            this.players[0].playTurn(board, this.marks[0]);
            renderer.renderBoard(board);
            if (board.gameEnded()) {
                break;
            }
            this.players[1].playTurn(board, this.marks[1]);
            renderer.renderBoard(board);
            if (board.gameEnded()) {
                break;
            }
        }

        return board.getWinner();
    }
}

