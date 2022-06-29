package it.polimi.ingsw.observers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * his abstract class defines everything that an observable view should allow.
 */

public abstract class ViewObservable {

    protected final List<ViewObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the view.
     *
     * @param obs the {@link Observer} to add.
     */

    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    /**
     * Adds a list of observers to the view.
     *
     * @param observerList the list of observers to add.
     */

    public void addAllObservers(List<ViewObserver> observerList) {
        observers.addAll(observerList);
    }

    /**
     * Notifies all the observing classes with an action to do.
     *
     * @param lambda the action the observers should do.
     */

    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }
}