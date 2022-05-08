package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Bard extends CharacterCard implements ArraylistStringCard{

    /*
        CHARACTER CARD DESCRIPTION:
        The bard allows the player to switch up to 2 students between the hall and the dining room.
    */

    List<String> students;

    public Bard() {
        super(10, 1);
    }


    @Override
    public void doOnClick(List<String> par) {
        this.students = new ArrayList<>(par);
    }


    public void doEffect(GameExpertMode game) {

        int maxNumOfChanges = students.size();

        while(maxNumOfChanges > 0){

            Color color1 = Color.valueOf(students.get(maxNumOfChanges-1));
            Color color2 = Color.valueOf(students.get(maxNumOfChanges-2));
            try {

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

            }
            catch(StudentNotFoundException | NonExistentColorException | FullTableException e){
                e.printStackTrace();
            }
            maxNumOfChanges=maxNumOfChanges-2;
        }

    }

}
