package it.polimi.ingsw.network.message;

public class CreateGameMessage extends Message{
    private int gameNumber;
    private int playerNum;
    private boolean isExpertMode;

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
