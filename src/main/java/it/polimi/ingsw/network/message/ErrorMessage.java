package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate a generic error to the clients.
 */

public class ErrorMessage extends Message{

    /**
     * Default constructor.
     */

    public ErrorMessage() {
        super(null, MessageType.ERROR_MESSAGE);
    }

}
