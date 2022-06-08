package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * It is used when a player tries to move a student to an Island
 * during the action phase.
 */
public class MoveToIslandMessage extends Message{

    private final String color;
    private final int islandID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param color the String representation of the color chosen.
     * @param islandID the id of the chosen island.
     */
    public MoveToIslandMessage(String nickname, String color, int islandID) {
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
