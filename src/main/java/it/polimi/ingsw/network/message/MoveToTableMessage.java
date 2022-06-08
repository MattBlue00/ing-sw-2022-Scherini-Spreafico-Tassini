package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * It is used when a player tries to move a student
 * to the Table with the same color of the student during the action phase.
 */
public class MoveToTableMessage extends Message{

    private final String color;

    /**
     * Default constructor.
     *
     * @param nickname the serialized Model.
     * @param color the chosen color String.
     */
    public MoveToTableMessage(String nickname, String color) {
        super(nickname, MessageType.MOVE_TO_TABLE_REPLY);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
