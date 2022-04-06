package it.polimi.ingsw.observers;

public interface Observer<T> {
    void update(T message);
}
