package it.polimi.ingsw.observers;

public interface Observer<Message> {
    void update(Message message);
}
