package it.polimi.ingsw.network.message;

public abstract class Message {
    private final String nickname;
    private final MessageType messageType;

    Message(String nickname, MessageType messageType){
        this.nickname = nickname;
        this.messageType = messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
