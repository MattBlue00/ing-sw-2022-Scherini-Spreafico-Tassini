package it.polimi.ingsw.network.message;

public class PlayerNumberReply extends Message{

    private int playerNumber;

    public PlayerNumberReply(String nickname, int playerNumber) {
        super(nickname, MessageType.PLAYER_NUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
