package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class AssistantCardsPlayedMessage extends Message{

    private List<Player> players;

    public AssistantCardsPlayedMessage(List<Player> players){
        super(null, MessageType.ASSISTANT_CARDS_PLAYED);
        this.players = players;
    }

}
