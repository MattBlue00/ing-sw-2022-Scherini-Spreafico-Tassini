package it.polimi.ingsw.model.charactercards;

/**
 * Defines a generic Character Card that needs a {@link String} and an {@code int} as parameters.
 */

public interface StringIntCard {

    /**
     * Sets the parameter of a Character Card.
     *
     * @param par1 the {@code String} parameter to set.
     * @param par2 the {@code int} parameter to set.
     */

    void doOnClick(String par1, int par2);

}
