package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class defines everything that an observable class should allow.
 */

public abstract class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the class.
     *
     * @param obs the {@link Observer} to add.
     */

    public void addObserver(Observer obs){
        observers.add(obs);
    }

    /**
     * Notifies all the observing classes with a message.
     *
     * @param message the message to notify.
     */

    public void notifyObservers(Message message){
        for(Observer obs : observers){
            obs.update(message);
        }
    }

}
