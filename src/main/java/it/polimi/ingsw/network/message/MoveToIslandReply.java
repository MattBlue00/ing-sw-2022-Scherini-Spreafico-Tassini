package it.polimi.ingsw.network.message;

public class MoveToIslandReply extends Message{

    private final String color;
    private final int islandID;

    MoveToIslandReply(String nickname, String color, int islandID) {
        super(nickname, MessageType.MOVE_TO_ISLAND_REPLY);
        this.islandID = islandID;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getIslandID() {
        return islandID;
    }
}
