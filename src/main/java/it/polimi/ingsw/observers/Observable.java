package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs){
        observers.add(obs);
    }

    public void removeObserver(Observer obs){
        observers.remove(obs);
    }

    public void notifyObservers(Message message){
        for(Observer obs : observers){
            obs.update(message);
        }
    }

}
