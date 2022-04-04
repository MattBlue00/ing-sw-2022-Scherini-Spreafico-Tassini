package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentTableException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.Optional;

public class Bard extends CharacterCard {

    //TODO: need to decide where to call the "doEffect" method

    private int maxNumOfChanges;

    public Bard() {
        super(10, 1);
        this.maxNumOfChanges = 2;
    }

    public void doEffect(GameExpertMode game) {

        while(maxNumOfChanges >= 0){
            //TODO: the view will ask the player which students to change

            Color color1 = Color.BLUE; // PLACEHOLDER for the hall student
            Color color2 = Color.YELLOW; // PLACEHOLDER for the table student

            //TODO: need to ask the player for choices (from table to hall or otherwise?)

            // Algorithm to switch two students between the hall and the right table(s)

            try {

                Optional<Student> hallStudent =
                        game.getCurrentPlayer().getSchool().getHall().removeStudent(color1.toString());

                if (!hallStudent.isPresent())
                    throw new StudentNotFoundException("No such student was found in the hall.");

                Optional<Student> tableStudent =
                        game.getCurrentPlayer().getSchool().getTable(color2.toString()).removeStudent();

                if(!tableStudent.isPresent())
                    throw new StudentNotFoundException("No such student was found on the " + color2 + " table.");

                if(game.getCurrentPlayer().getSchool().getTable(color1.toString()).getNumOfStudents()
                    >= Constants.TABLE_LENGTH)
                    throw new FullTableException("The " + color1 + " table is full!");

                game.getCurrentPlayer().getSchool().getHall().addStudent(tableStudent.get());
                game.getCurrentPlayer().getSchool().getTable(hallStudent.get().getColor().toString())
                        .addStudent(hallStudent.get(), game.getCurrentPlayer());

                maxNumOfChanges--;

            }
            catch(StudentNotFoundException | NonExistentTableException | FullTableException e){
                e.printStackTrace();
            }

        }

    }
}
