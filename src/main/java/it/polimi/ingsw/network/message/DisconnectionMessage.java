package it.polimi.ingsw.network.message;

/**
 * Message used to notify a disconnection to the other players.
 */
public class DisconnectionMessage extends Message{

    private static final long serialVersionUID = -5422965079989607600L;
    private final String text;

    /**
     * Default constructor.
     *
     * @param text message that will be shown to the clients
     */
    public DisconnectionMessage(String text) {
        super(null, MessageType.DISCONNECTION);
        this.text = text;
    }

    /**
     * This method returns the message's text.
     *
     * @return {@code text}
     */
    public String getMessageStr() {
            return text;
        }
}
