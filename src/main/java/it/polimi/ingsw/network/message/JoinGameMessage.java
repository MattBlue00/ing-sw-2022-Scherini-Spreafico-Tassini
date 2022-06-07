package it.polimi.ingsw.network.message;

/**
 * Message class used by the client to choose a game to join.
 */
public class JoinGameMessage extends Message{

    private int gameID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname
     * @param gameID gameID of the game to join
     */
    public JoinGameMessage(String nickname, int gameID) {
        super(nickname, MessageType.JOIN_GAME);
        this.gameID = gameID;
    }

    /**
     * This method returns the gameID chosen.
     * 
     * @return {@code gameID}
     */
    public int getGameID() {
        return gameID;
    }
}
