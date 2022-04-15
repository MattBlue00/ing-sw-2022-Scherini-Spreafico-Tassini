package it.polimi.ingsw.network.message;


public class MoveToTableReply extends Message{

    private final String color;

    MoveToTableReply(String nickname, String color) {
        super(nickname, MessageType.MOVE_TO_TABLE_REPLY);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
