package it.polimi.ingsw.network.message;

/**
 * Message to notify an error to the client.
 */
public class ErrorMessage extends Message{

    private final String error;

    /**
     * Default constructor.
     *
     * @param error error text
     */
    public ErrorMessage(String error) {
        super(null, MessageType.ERROR_MESSAGE);
        this.error = error;
    }
}
