package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate how many players a newborn game should have. It was used just for
 * testing purposes.
 */
public class PlayerNumberMessage extends Message{

    private final int playerNumber;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param playerNumber an {@code int} representing the chosen players' number.
     */

    public PlayerNumberMessage(String nickname, int playerNumber) {
        super(nickname, MessageType.PLAYER_NUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    /**
     * Returns the chosen players' number.
     *
     * @return an {@code int} representing the chosen players' number.
     */

    public int getPlayerNumber() {
        return playerNumber;
    }
}
