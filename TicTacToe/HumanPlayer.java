import  java.util.Scanner;

/**
 * The Human Player class. This class implements a human player, according to coordinates
 * given by a human user.
 */
public class HumanPlayer implements Player {

    /**
     * Initializes the player.
     */
    public HumanPlayer () { }

    /**
     * Plays a single turn in the game.
     * @param board the board.
     * @param mark the mark to put in the board.
     */
    public void playTurn(Board board, Mark mark) {
        System.out.println("Player " + mark + ", type coordinates: ");
        Scanner in = new Scanner(System.in);

        int num = in.nextInt();
        int row = num/10 - 1;
        int col = num%10 - 1;

        boolean success = board.putMark(mark, row, col);

        while (!success) {
            System.out.println("Invalid coordinates, type again: ");
            num = in.nextInt();
            row = num/10 - 1;
            col = num%10 - 1;
            success = board.putMark(mark, row, col);
        }
    }
}
