package it.polimi.ingsw.model.charactercards;

/**
 * Defines a generic Character Card that needs a {@link String} as a parameter.
 */

public interface StringCard {

    /**
     * Sets the parameter of a Character Card.
     *
     * @param par the parameter to set.
     */

    void doOnClick(String par);
}
