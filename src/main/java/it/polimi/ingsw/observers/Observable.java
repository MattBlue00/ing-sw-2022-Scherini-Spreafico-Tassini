package it.polimi.ingsw.observers;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    private List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> obs){
        observers.add(obs);
    }

    public void removeObserver(Observer<T> obs){
        observers.remove(obs);
    }

    public void notifyObservers(T message){
        for(Observer<T> obs : observers){
            obs.update(message);
        }
    }

}
