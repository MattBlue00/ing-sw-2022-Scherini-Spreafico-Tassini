package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Allows the player to switch from 0 to 2 students between the hall and the dining room.
 */

public class Bard extends CharacterCard implements ArrayListStringCard, Serializable {

    private List<String> students;

    /**
     * Character Card constructor.
     */

    public Bard() {
        super(10, 1);
    }

    /**
     * Sets the parameter of the {@link Bard}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(List<String> par) {
        this.students = new ArrayList<>(par);
    }

    /**
     * Allows the {@link Bard} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Bard} will do his effect.
     * @throws TryAgainException if at least one of the specified students does not exist or can't be moved.
     */

    public void doEffect(GameExpertMode game) throws TryAgainException {

        int maxNumOfChanges = students.size();

        while(maxNumOfChanges > 0){

            Color color1 = Color.valueOf(students.get(maxNumOfChanges-2));
            Color color2 = Color.valueOf(students.get(maxNumOfChanges-1));

            if(game.getCurrentPlayer().getSchool().getTable(color1.toString()).getNumOfStudents()
                    >= Constants.TABLE_LENGTH)
                throw new FullTableException("The " + color1 + " table is full!");

            Optional<Student> hallStudent =
                    game.getCurrentPlayer().getSchool().getHall().getStudents().
                            stream().filter(x -> x.color().equals(color1)).findFirst();

            if(hallStudent.isEmpty())
                throw new StudentNotFoundException("There's no " + color1 + " student in the hall!");

            Optional<Student> tableStudent =
                    game.getCurrentPlayer().getSchool().getTable(color2.toString()).getStudents().
                            stream().filter(x -> x.color().equals(color2)).findFirst();

            if(tableStudent.isEmpty())
                throw new StudentNotFoundException("The " + color2 + " table is empty!");

            game.getCurrentPlayer().getSchool().getHall().addStudent(tableStudent.get());
            game.getCurrentPlayer().getSchool().getTable(color2.toString()).removeStudent();
            game.getCurrentPlayer().getSchool().getTable(hallStudent.get().color().toString())
                    .addStudent(hallStudent.get(), game.getCurrentPlayer());
            game.getCurrentPlayer().getSchool().getHall().removeStudent(hallStudent.get().color().toString());

            maxNumOfChanges=maxNumOfChanges-2;
        }

    }

}
