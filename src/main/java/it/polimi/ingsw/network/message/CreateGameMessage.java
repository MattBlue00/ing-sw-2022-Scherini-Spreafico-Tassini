package it.polimi.ingsw.network.message;


/**
 * The message extends {@link CharacterCardMessage}.
 * It is used when a client tries to create a Game.
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
     * @param isExpertMode the flag to indicate if the Game has normal or expert rules.
     */
    public CreateGameMessage(String nickname, int gameNumber, int playerNum, boolean isExpertMode) {
        super(nickname, MessageType.CREATE_GAME);
        this.gameNumber = gameNumber;
        this.playerNum = playerNum;
        this.isExpertMode = isExpertMode;
    }

    public int getGameNumber(){
        return gameNumber;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public boolean isExpertMode() {
        return isExpertMode;
    }
}
