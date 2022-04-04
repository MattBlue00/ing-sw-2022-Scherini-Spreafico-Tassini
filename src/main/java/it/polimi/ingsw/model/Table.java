package it.polimi.ingsw.model;

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

    public void addStudent(Student addedStudent, Player player){
        students.add(addedStudent);
        player.setCoinsWallet(
                player.getCoinsWallet() +
                coinCheck()
        );
    }

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

    public Optional<Student> removeStudent(){
        return this.students.stream().findFirst();
    }

    //for debugging
    public void showTable(){
        System.out.println(this.color + " table");
        System.out.println("Numero studenti (in questa table): " + this.students.size());
    }

    //for debugging
    public void addStudentForDebug(Student student){
        this.students.add(student);
    }

}
