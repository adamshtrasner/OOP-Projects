/**
 * The class of the game's board, using the enum Mark to
 * put marks X or O in the board, and the enum BoardStatus
 * to know whether the game is still on going, ended with a tie,
 * or if there's a win.
 */
public class Board {

    public static final int SIZE = 6;
    public static final int WIN_STREAK = 4;

    private Mark[][] board;
    private BoardStatus status = BoardStatus.PLAYABLE;
    private int numOfCellsLeft = SIZE * SIZE;

    /**
     * Initializes the board.
     */
    public Board () {
        this.board = new Mark[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                this.board[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Puts a mark in a given spot on the board.
     * @param mark the mark to put in the board.
     * @param row the row to put the mark in.
     * @param col the column to put the mark in.
     * @return true - if the mark has been put in the board successfully.
     *         false - otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {

        // if the board is full
        if (this.numOfCellsLeft == 0) {
            return false;
        }

        // if row or col are out of the board's bounds
        if (row < 0|| row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }

        // if there's already a mark in the given spot on the board
        if (this.board[row][col] != Mark.BLANK) {
            return false;
        }

        if (mark != Mark.BLANK) {
            this.numOfCellsLeft --;
            this.board[row][col] = mark;
            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    checkStatus(mark, i, j);
                }
            }
        }
        return true;
    }

    /**
     *
     * @return true - if the game has ended (Board's status isn't PLAYABLE)
     *         false - otherwise
     */
    public boolean gameEnded() {
        return this.status != BoardStatus.PLAYABLE;
    }

    /**
     *
     * @return X - if the winner of the game is player X.
     *         O - if the winner of the game is player X.
     *         TIE - if the game ended with a tie.
     */
    public Mark getWinner() {
        if (this.status == BoardStatus.WIN_X) {
            return Mark.X;
        }
        else if (this.status == BoardStatus.WIN_O) {
            return Mark.O;
        }
        else {
            return Mark.BLANK;
        }
    }

    /**
     *
     * @param row row number of the board
     * @param col column number of the board
     * @return the mark in the given row and column of the board.
     */
    public Mark getMark(int row, int col) {
        if (row < 0|| row >= SIZE || col < 0 || col >= SIZE) {
            return Mark.BLANK;
        }
        return this.board[row][col];
    }

    // ----------------- Private Methods -----------------


    /**
     * Checks the status of the board and changes it
     * according to the marks in the board.
     * @param mark the mark to check
     * @param row row number of the board
     * @param col column number of the board
     */
    private void checkStatus(Mark mark, int row, int col) {
        BoardStatus local_status;
        if (mark == Mark.X) {
            local_status = BoardStatus.WIN_X;
        }
        else {
            local_status = BoardStatus.WIN_O;
        }
        if (countMarkInDirection(row, col, 0, 1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, 0, -1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, 1, 0, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, -1, 0, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, 1, 1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, 1, -1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, -1, 1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (countMarkInDirection(row, col, -1, -1, mark) == WIN_STREAK) {
            this.status = local_status;
        }
        if (this.numOfCellsLeft == 0) {
            this.status = BoardStatus.TIE;
        }
    }

    /**
     * Counts the number of marks from a specific spot.
     * @param row row number of the board to start from
     * @param col column number of the board to start from
     * @param rowDelta direction towards row.
     *                 0 means no movement, 1 means right, -1 means left.
     * @param colDelta direction towards column.
     *                 0 means no movement, 1 means right, -1 means left.
     * @param mark the mark to check.
     * @return number of marks in the direction given from a given spot.
     */
    private int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while(row < SIZE && row >= 0 && col < SIZE && col >= 0 && this.board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

}
