/**
 * A Tournament class. Describing a number of rounds between 2 players.
 */
public class Tournament {

    Player[] players = new Player[2];
    Renderer renderer;
    int rounds;

    /**
     * Initializes a tournament.
     * @param rounds number of total rounds.
     * @param renderer a renderer
     * @param players a 2 size list of players
     */
    Tournament(int rounds, Renderer renderer, Player[] players) {
        this.players[0] = players[0];
        this.players[1] = players[1];
        this.renderer = renderer;
        this.rounds = rounds;
    }

    /**
     * The method runs a tournament, where in each round the players switch the mark.
     */
    public void playTournament() {

        int[] playerWins = {0, 0};
        int ties = 0;

        for(int i = 0; i < this.rounds; i++) {
            Game game;
            if (i % 2 == 0) {
                game = new Game(this.players[0], this.players[1], this.renderer);
            }
            else {
                game = new Game(this.players[1], this.players[0], this.renderer);
            }
            Mark m = game.run();

            if (m == Mark.X) {
                if (i % 2 == 0) {
                    playerWins[0]++;
                }
                else {
                    playerWins[1]++;
                }
            }
            if (m == Mark.O) {
                if (i % 2 == 0) {
                    playerWins[1]++;
                }
                else {
                    playerWins[0]++;
                }
            }
            if (m == Mark.BLANK) {
                ties++;
            }
        }

        System.out.println(String.format("=== player 1: %d | player 2: %d | Draws: %d ===\r",
                playerWins[0], playerWins[1], ties));

    }

    public static void main(String[] args) {

        PlayerFactory playerFact = new PlayerFactory();
        Player player1 = playerFact.buildPlayer(args[2]);
        Player player2 = playerFact.buildPlayer(args[3]);
        Player[] players = {player1, player2};

        RendererFactory rendererFact = new RendererFactory();
        Renderer renderer = rendererFact.buildRenderer(args[1]);

        Tournament tournament = new Tournament(Integer.parseInt(args[0]), renderer, players);
        tournament.playTournament();
    }
}
