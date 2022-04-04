package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.GameView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class GameController implements PropertyChangeListener {

    private GameView view;
    private Game model;

    public GameController() {
        this.view = new GameView();
        this.model = new Game(2); // TODO: player 1 needs to choose players' number
    }

    public void printRoundNumber(){
        view.printRoundNumber(model.getRoundNumber());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        model.setRoundNumber((int) evt.getNewValue());
    }
}
