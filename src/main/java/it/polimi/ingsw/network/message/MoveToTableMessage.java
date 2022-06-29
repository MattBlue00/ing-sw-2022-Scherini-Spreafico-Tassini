package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which student the player wishes to move to its table.
 */

public class MoveToTableMessage extends Message{

    private final String color;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param color the {@link String} representation of the chosen color.
     */

    public MoveToTableMessage(String nickname, String color) {
        super(nickname, MessageType.MOVE_TO_TABLE_REPLY);
        this.color = color;
    }

    /**
     * Returns the chosen student's color.
     *
     * @return a {@link String} representation of the chosen student's color.
     */

    public String getColor() {
        return color;
    }
}
