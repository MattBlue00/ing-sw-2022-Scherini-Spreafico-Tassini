package it.polimi.ingsw.network.message;


public class MoveToTableMessage extends Message{

    private final String color;

    public MoveToTableMessage(String nickname, String color) {
        super(nickname, MessageType.MOVE_TO_TABLE_REPLY);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
