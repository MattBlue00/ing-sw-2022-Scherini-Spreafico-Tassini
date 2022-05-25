package it.polimi.ingsw.network.message;

public class ActionChoiceMessage extends Message{

    private String choice;

    public ActionChoiceMessage(String nickname, String choice) {
        super(nickname, MessageType.ACTION_CHOICE);
        this.choice = choice;
    }

    public String getChoice() {
        return choice;
    }
}
