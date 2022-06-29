package it.polimi.ingsw.network.message;

import java.io.Serial;
import java.io.Serializable;

/**
 * This abstract class defines everything that a message surfing the network should have. By doing so, the usage of
 * the "instance of" primitive is avoided.
 */

public abstract class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 6589184250663958343L;
    private final String nickname;
    private final MessageType messageType;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param messageType the message type.
     */

    public Message(String nickname, MessageType messageType){
        this.nickname = nickname;
        this.messageType = messageType;
    }

    /**
     * Returns the client's nickname.
     *
     * @return the client's nickname.
     */

    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the message type.
     *
     * @return the message type.
     */

    public MessageType getMessageType() {
        return messageType;
    }
}
