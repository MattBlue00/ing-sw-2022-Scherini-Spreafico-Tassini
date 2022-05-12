package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;

import java.util.List;

public class Monk extends CharacterCard implements StringIntCard{

    private Student[] students;
    private String chosenColor;
    private int chosenIsland;

    public Monk(List<Student> studentsBag) {
        super(1, 1);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    @Override
    public void doOnClick(String par1, int par2){
        chosenColor = par1;
        chosenIsland = par2;
    }

    @Override
    public void doEffect(GameExpertMode game) {

        try {
            for (int i = 0; i < 4; i++) {
                if (students[i].getColor().toString().equals(chosenColor)) {
                    game.getBoard().getIslands().getIslandFromID(chosenIsland).addStudent(students[i]);
                    students[i] = game.getBoard().getStudentsBag().remove(game.getBoard().getStudentsBag().size() - 1);
                    break;
                }
            }
        } catch (IslandNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Debug method

    public Student[] getStudents(){
        return students;
    }

    //TODO: what if parameters are incorrect? We should give players other attempts!

}
