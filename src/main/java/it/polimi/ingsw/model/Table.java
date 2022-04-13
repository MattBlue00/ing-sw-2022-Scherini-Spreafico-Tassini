package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Table {

    private final Color color;
    private boolean hasProfessor;
    private List<Student> students;
    private boolean coinsAvailable[] = new boolean[Constants.COINS_AVAILABLE_PER_TABLE];


    public Table(Color color) {
        this.color = color;
        this.students = new ArrayList<>();
        this.hasProfessor = false;
        for(int i=0; i<Constants.COINS_AVAILABLE_PER_TABLE; i++){
            coinsAvailable[i]=true;
        }
    }

    // Getter and setter methods

    public Color getColor() {
        return color;
    }

    public int getNumOfStudents(){
        return students.size();
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean getHasProfessor() {
        return hasProfessor;
    }

    public void setHasProfessor(boolean hasProfessor) {
        this.hasProfessor = hasProfessor;
    }

    // Table methods

    public void addStudent(Student addedStudent, Player player) throws
            FullTableException {

        if(students.size() >= Constants.TABLE_LENGTH)
            throw new FullTableException("The " + addedStudent.getColor() + " table is full!");

        students.add(addedStudent);
        player.setCoinsWallet(
                player.getCoinsWallet() +
                coinCheck()
        );

    }

    /*
        This method assign a player a coin if the required condition is respected.
     */
    public int coinCheck(){

        int size = this.students.size();
        if(size % Constants.OFFSET_COINS == 0 && size != 0){
            int pos = size/Constants.OFFSET_COINS;
            if(coinsAvailable[pos-1]){
                coinsAvailable[pos-1] = false;
                return 1;
            }
            else return 0;
        }
        else return 0;

    }

    public Student removeStudent() throws StudentNotFoundException {

        Optional<Student> studentToRemove = this.students.stream().findFirst();

        if(studentToRemove.isPresent()) {
            students.remove(studentToRemove.get());
            return studentToRemove.get();
        }
        else
            throw new StudentNotFoundException("No such student is present in the table.");

    }

    //for debugging
    /*public void showTable(){
        System.out.println(this.color + " table");
        System.out.println("Numero studenti (in questa table): " + this.students.size());
    }*/

}
