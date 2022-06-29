package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The table is a part of the dining room, which is a third of a {@link School}. In each school, there's a table for
 * every existent {@link Color}. It can hold up to 10 students of the same color, and a coin is awarded to the school's
 * owner every three students of the same color (once a coin for a certain amount of students is obtained, it can't be
 * obtained again if - for example - the player loses their third red student and earns it back afterwards).
 */

public class Table implements Serializable {

    private final Color color;
    private boolean hasProfessor;
    private final List<Student> students;
    private final boolean[] coinsAvailable = new boolean[Constants.COINS_AVAILABLE_PER_TABLE];

    /**
     * Table constructor.
     *
     * @param color the color of the table.
     */

    public Table(Color color) {
        this.color = color;
        this.students = new ArrayList<>();
        this.hasProfessor = false;
        for(int i=0; i<Constants.COINS_AVAILABLE_PER_TABLE; i++){
            coinsAvailable[i]=true;
        }
    }

    /**
     * Returns the color of the table.
     *
     * @return the {@link Color} of the table.
     */

    public Color getColor() {
        return color;
    }

    /**
     * Returns the number of students currently seated at the table.
     *
     * @return an {@code int} corresponding to the number of students currently seated at the table.
     */

    public int getNumOfStudents(){
        return students.size();
    }

    /**
     * Returns a list of the students currently seated at the table.
     *
     * @return a list of the students currently seated at the table.
     */

    public List<Student> getStudents() {
        return students;
    }

    /**
     * Checks if the head of the table has a professor.
     *
     * @return {@code true} if it has, {@code false} otherwise.
     */

    public boolean getHasProfessor() {
        return hasProfessor;
    }

    /**
     * Sets the boolean flag to {@code true} if the head of the table has now a professor, {@code false} otherwise.
     *
     * @param hasProfessor the new flag.
     */

    public void setHasProfessor(boolean hasProfessor) {
        this.hasProfessor = hasProfessor;
    }

    /**
     * Adds a student to the table, if possible.
     *
     * @param addedStudent the {@link Student} to add.
     * @param player the {@link Player} whose coins' wallet will be updated if conditions are met.
     * @throws FullTableException if the table has reached its maximum capacity.
     */

    public void addStudent(Student addedStudent, Player player) throws
            FullTableException {

        if(students.size() >= Constants.TABLE_LENGTH)
            throw new FullTableException("The " + addedStudent.color() + " table is full!");

        students.add(addedStudent);
        player.setCoinsWallet(
                player.getCoinsWallet() +
                coinCheck()
        );

    }

    /**
     * Returns the number of coins to assign to the player (0 or 1) after the addition of a student.
     *
     * @return 0 or 1, as a {@code int}.
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

    /**
     * Removes a student from the table and returns it, if possible.
     *
     * @return the removed {@link Student}.
     * @throws StudentNotFoundException if the table is empty.
     */

    public Student removeStudent() throws StudentNotFoundException {

        Optional<Student> studentToRemove = this.students.stream().findFirst();

        if(studentToRemove.isPresent()) {
            students.remove(studentToRemove.get());
            return studentToRemove.get();
        }
        else
            throw new StudentNotFoundException("No such student is present in the table.");

    }

}
