package it.polimi.ingsw.network.message;


/**
 * The message extends {@link Message}.
 * It is used when the cloud is chosen from a player
 * at the end of an action phase.
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

    public int getCloudID() {
        return cloudID;
    }
}
