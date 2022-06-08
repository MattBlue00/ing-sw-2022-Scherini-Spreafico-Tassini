package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.server.Server;

/**
 * The generic AskMessage, extends {@link Message}.
 * It is used when the {@link Server} asks for the next move to a player.
 * The message has a {@link AskType} (type of ask the server makes).
 */
public class AskMessage extends Message{
    private final AskType askType;

    /**
     * Default constructor.
     *
     * @param askType the type of ASK Server makes
     */
    public AskMessage(AskType askType) {
        super(null, MessageType.ASK_TYPE);
        this.askType = askType;
    }

    public AskType getAskType() {
        return askType;
    }
}
