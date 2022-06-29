package it.polimi.ingsw.network.message;


/**
 * This {@link Message} is used to communicate which cloud has been chosen by a player at the end of their Action Phase.
 */

public class CloudChoiceMessage extends Message{

    private final int cloudID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cloudID the chosen cloud id.
     */

    public CloudChoiceMessage(String nickname, int cloudID) {
        super(nickname, MessageType.CLOUD_CHOICE_REPLY);
        this.cloudID = cloudID;
    }

    /**
     * Returns the id of the chosen cloud.
     *
     * @return an {@code int} representing the id of the chosen cloud.
     */

    public int getCloudID() {
        return cloudID;
    }
}
