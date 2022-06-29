package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.message.Message;

/**
 * This interface defines everything that an observer class should allow.
 */

public interface Observer {

    /**
     * Updates the observed object with the given message.
     *
     * @param message the update message.
     */

    void update(Message message);
}
