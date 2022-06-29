package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.server.Server;

/**
 * This {@link Message} is used when the {@link Server} asks for the next move to a player.
 * Each message has a {@link AskType} (which represents what the {@link Server} is asking for).
 */

public class AskMessage extends Message{
    private final AskType askType;

    /**
     * Default constructor.
     *
     * @param askType a {@link AskType} value that represents what the {@link Server} is asking for.
     */

    public AskMessage(AskType askType) {
        super(null, MessageType.ASK_TYPE);
        this.askType = askType;
    }

    /**
     * Returns a {@link AskType} value that represents what the {@link Server} is asking for.
     *
     * @return a {@link AskType} value that represents what the {@link Server} is asking for.
     */

    public AskType getAskType() {
        return askType;
    }
}
