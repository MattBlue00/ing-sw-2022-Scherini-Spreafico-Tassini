package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bard extends CharacterCard implements ArrayListStringCard, Serializable {

    /*
        CHARACTER CARD DESCRIPTION:
        The bard allows the player to switch up to 2 students between the hall and the dining room.
    */

    private List<String> students;

    public Bard() {
        super(10, 1);
    }


    @Override
    public void doOnClick(List<String> par) {
        this.students = new ArrayList<>(par);
    }


    public void doEffect(GameExpertMode game) throws TryAgainException {

        int maxNumOfChanges = students.size();

        while(maxNumOfChanges > 0){

            Color color1 = Color.valueOf(students.get(maxNumOfChanges-2));
            Color color2 = Color.valueOf(students.get(maxNumOfChanges-1));

                Student hallStudent =
                        game.getCurrentPlayer().getSchool().getHall().removeStudent(color1.toString());

                Student tableStudent =
                        game.getCurrentPlayer().getSchool().getTable(color2.toString()).removeStudent();

                if(game.getCurrentPlayer().getSchool().getTable(color1.toString()).getNumOfStudents()
                    >= Constants.TABLE_LENGTH)
                    throw new FullTableException("The " + color1 + " table is full!");

                game.getCurrentPlayer().getSchool().getHall().addStudent(tableStudent);
                game.getCurrentPlayer().getSchool().getTable(hallStudent.getColor().toString())
                        .addStudent(hallStudent, game.getCurrentPlayer());

            maxNumOfChanges=maxNumOfChanges-2;
        }

    }

}
