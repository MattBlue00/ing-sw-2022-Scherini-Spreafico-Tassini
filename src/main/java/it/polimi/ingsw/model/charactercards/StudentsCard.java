package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.Student;

import java.util.List;

/**
 * Defines a generic Character Card that has students on itself.
 */

public interface StudentsCard {

    /**
     * Returns a list of the students on the card.
     *
     * @return a list of the students on the card.
     */

    List<Student> getStudentsOnTheCard();

    /**
     * Allows the view to properly show the students on the card.
     */

    void showStudentsOnTheCard();

}
