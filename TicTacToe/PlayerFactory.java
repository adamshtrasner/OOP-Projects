/**
 * A Player Factory.
 */
public class PlayerFactory {

    public Player buildPlayer(String playerType) {
        Player player;
        switch (playerType) {
            case "human":
                player = new HumanPlayer();
                break;
            case "whatever":
                player = new WhateverPlayer();
                break;
            case "clever":
                player = new CleverPlayer();
                break;
            case "snartypamts":
                player = new SnartypamtsPlayer();
                break;
            default:
                player = null;
        }
        return player;
    }
}
