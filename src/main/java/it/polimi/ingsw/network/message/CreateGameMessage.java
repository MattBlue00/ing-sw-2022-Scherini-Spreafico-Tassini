package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate the parameters of the game the client wishes to create.
 */

public class CreateGameMessage extends Message{

    private final int gameNumber;
    private final int playerNum;
    private final boolean isExpertMode;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param gameNumber the unique id for the game.
     * @param playerNum the number of players in the game (2 or 3).
     * @param isExpertMode the flag that indicates whether the match follows normal or expert mode rules.
     */

    public CreateGameMessage(String nickname, int gameNumber, int playerNum, boolean isExpertMode) {
        super(nickname, MessageType.CREATE_GAME);
        this.gameNumber = gameNumber;
        this.playerNum = playerNum;
        this.isExpertMode = isExpertMode;
    }

    /**
     * Returns the chosen id for the match.
     *
     * @return an {@code int} representing the chosen id for the match.
     */

    public int getGameNumber(){
        return gameNumber;
    }

    /**
     * Returns the chosen number of players for the match.
     *
     * @return an {@code int} representing the chosen number of players for the match.
     */

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Returns the flag representing whether the match follows the normal or the expert mode rules.
     *
     * @return {@code true} if expert mode was chosen, {@code false} otherwise.
     */

    public boolean isExpertMode() {
        return isExpertMode;
    }
}
