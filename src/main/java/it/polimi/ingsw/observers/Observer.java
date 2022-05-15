package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.message.Message;

public interface Observer {
    void update(Message message);
}
