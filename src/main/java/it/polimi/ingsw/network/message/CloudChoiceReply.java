package it.polimi.ingsw.network.message;

public class CloudChoiceReply extends Message{

    private final int cloudID;

    CloudChoiceReply(String nickname, int cloudID) {
        super(nickname, MessageType.CLOUD_CHOICE_REPLY);
        this.cloudID = cloudID;
    }

    public int getCloudID() {
        return cloudID;
    }
}
