package it.polimi.ingsw.model.charactercards;

/**
 * Defines a generic Character Card that needs an {@code int} as a parameter.
 */

public interface IntCard {

    /**
     * Sets the parameter of a Character Card.
     *
     * @param par the parameter to set.
     */

    void doOnClick(int par);
}
