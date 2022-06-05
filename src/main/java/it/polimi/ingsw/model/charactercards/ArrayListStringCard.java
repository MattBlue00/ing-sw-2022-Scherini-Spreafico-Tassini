package it.polimi.ingsw.model.charactercards;

import java.util.List;
import java.util.ArrayList;

/**
 * Defines a generic Character Card that needs an {@link ArrayList} of {@link String} as a parameter.
 */

public interface ArrayListStringCard {

    /**
     * Sets the parameter of a Character Card.
     *
     * @param par the parameter to set.
     */

    void doOnClick(List<String> par);
}
