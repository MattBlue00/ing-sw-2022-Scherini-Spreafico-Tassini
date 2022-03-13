package it.polimi.ingsw.model;

public class Student {
    private final Color color;
    private final Game game;

    public Student(Color color, Game game) {
        this.color = color;
        this.game = game;
    }

    public Color getColor() {
        return color;
    }
}
