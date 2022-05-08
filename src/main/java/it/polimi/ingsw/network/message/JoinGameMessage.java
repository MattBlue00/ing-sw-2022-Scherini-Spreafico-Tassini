package it.polimi.ingsw.network.message;

public class JoinGameMessage extends Message{

    private int gameID;

    public JoinGameMessage(String nickname, int gameID) {
        super(nickname, MessageType.JOIN_GAME);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
