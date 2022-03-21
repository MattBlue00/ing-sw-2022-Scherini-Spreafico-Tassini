package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Table {

    private final Color color;
    private boolean hasProfessor;
    private List<Student> students;
    private final int MAX_TABLE_LENGTH = 10; // is used to see if we throw a FullTableException
    private final int COINS_AVAILABLE_PER_TABLE = 3;
    private final int OFFSET_COINS = 3; // constant offset between the coins on the table
    private boolean coinsAvailable[] = new boolean[COINS_AVAILABLE_PER_TABLE];


    public Table(Color color) {
        this.color = color;
        this.students = new ArrayList<>();
        this.hasProfessor = false;
        for(int i=0; i<COINS_AVAILABLE_PER_TABLE; i++){
            coinsAvailable[i]=true;
        }
    }

    public Color getColor() {
        return color;
    }

    public int getNumOfStudents(){
        return students.size();
    }

    public boolean getHasProfessor() {
        return hasProfessor;
    }

    public void setHasProfessor(boolean hasProfessor) {
        this.hasProfessor = hasProfessor;
    }

    public void addStudent(Student addedStudent, Player player){
        students.add(addedStudent);
        player.setCoinsWallet(
                player.getCoinsWallet() +
                coinCheck()
        );
    }

    public int coinCheck(){

        int size = this.students.size();
        if(size % OFFSET_COINS == 0 && size != 0){
            int pos = size/OFFSET_COINS;
            if(coinsAvailable[pos-1]){
                coinsAvailable[pos-1] = false;
                return 1;
            }
            else return 0;
        }
        else return 0;
    }


}
