package it.polimi.ingsw.network.message;

public class CloudChoiceMessage extends Message{

    private final int cloudID;

    public CloudChoiceMessage(String nickname, int cloudID) {
        super(nickname, MessageType.CLOUD_CHOICE_REPLY);
        this.cloudID = cloudID;
    }

    public int getCloudID() {
        return cloudID;
    }
}
