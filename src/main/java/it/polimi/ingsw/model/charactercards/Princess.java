package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;

import java.util.List;

public class Princess extends CharacterCard implements StringCard {

    private Student[] students;
    private String chosenColor;

    public Princess(List<Student> studentsBag) {
        super(11, 2);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    @Override
    public void doOnClick(String par){
        chosenColor = par;
    }

    @Override
    public void doEffect(GameExpertMode game) {

        try {
            for (int i = 0; i < 4; i++) {
                if (students[i].getColor().toString().equals(chosenColor)) {
                    game.getCurrentPlayer().getSchool().getTable(students[i].getColor().toString()).addStudent(
                            students[i], game.getCurrentPlayer()
                    );
                    students[i] = game.getBoard().getStudentsBag().remove(game.getBoard().getStudentsBag().size() - 1);
                    break;
                }
            }
        } catch (NonExistentColorException | FullTableException e){
            e.printStackTrace();
        }

    }

    // Debug method

    public Student[] getStudents(){
        return students;
    }

    //TODO: what if parameters are incorrect? We should give players other attempts!

}
