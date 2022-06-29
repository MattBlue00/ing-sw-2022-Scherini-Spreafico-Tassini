package it.polimi.ingsw.network.message;

import java.io.Serial;

/**
 * This {@link Message} is used to communicate to the connected players that a player has disconnected.
 */

public class DisconnectionMessage extends Message{

    @Serial
    private static final long serialVersionUID = -5422965079989607600L;
    private final String text;

    /**
     * Default constructor.
     *
     * @param text the message that will be shown to the clients.
     */

    public DisconnectionMessage(String text) {
        super(null, MessageType.DISCONNECTION);
        this.text = text;
    }

    /**
     * Returns the message's text.
     *
     * @return the message's text.
     */

    public String getMessageStr() {
            return text;
        }
}
