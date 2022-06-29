package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate the ID of the game the client wishes to join.
 */

public class JoinGameMessage extends Message{

    private final int gameID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param gameID an {@code int} representing the ID of the game to join.
     */

    public JoinGameMessage(String nickname, int gameID) {
        super(nickname, MessageType.JOIN_GAME);
        this.gameID = gameID;
    }

    /**
     * Returns the chosen game ID.
     * 
     * @return an {@code int} representing the chosen game ID.
     */

    public int getGameID() {
        return gameID;
    }
}
