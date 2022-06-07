package it.polimi.ingsw.network.message;

import java.io.Serializable;

/**
 * Abstract message class which must be extended by each message type.
 * Both server and clients will communicate using this generic type of message.
 * This avoids the usage of the "instance of" primitive.
 */
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 6589184250663958343L;
    private final String nickname;
    private final MessageType messageType;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname
     * @param messageType message type
     */
    public Message(String nickname, MessageType messageType){
        this.nickname = nickname;
        this.messageType = messageType;
    }

    /**
     * This method returns the client's nickname.
     *
     * @return {@code nickname}
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method returns the message type.
     *
     * @return {@code messageType}
     */
    public MessageType getMessageType() {
        return messageType;
    }
}
