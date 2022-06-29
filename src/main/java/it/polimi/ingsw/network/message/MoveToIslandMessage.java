package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which student the player wishes to move to which island.
 */

public class MoveToIslandMessage extends Message{

    private final String color;
    private final int islandID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param color the {@link String} representation of the color chosen.
     * @param islandID an {@code int} representing the id of the chosen island.
     */

    public MoveToIslandMessage(String nickname, String color, int islandID) {
        super(nickname, MessageType.MOVE_TO_ISLAND_REPLY);
        this.islandID = islandID;
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

    /**
     * Returns the chosen island's ID.
     *
     * @return an {@code int} representing the chosen island's ID.
     */

    public int getIslandID() {
        return islandID;
    }
}
