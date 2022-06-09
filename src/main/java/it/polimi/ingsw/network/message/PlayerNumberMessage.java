package it.polimi.ingsw.network.message;

/**
 * this class is used just for testing purposes.
 * It is used to simply send the number of players of a Game.
 */
public class PlayerNumberMessage extends Message{

    private final int playerNumber;

    /**
     * Default constructor.
     *
     * @param nickname the serialized Model.
     * @param playerNumber the chosen color String.
     */
    public PlayerNumberMessage(String nickname, int playerNumber) {
        super(nickname, MessageType.PLAYER_NUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
