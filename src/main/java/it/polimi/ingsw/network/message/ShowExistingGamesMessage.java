package it.polimi.ingsw.network.message;

import java.util.ArrayList;
import java.util.List;

public class ShowExistingGamesMessage extends Message{

    private final List<Integer> existingGames;

    public ShowExistingGamesMessage(List<Integer> existingGames) {
        super(null, MessageType.EXISTING_GAMES);
        this.existingGames = new ArrayList<>();
        this.existingGames.addAll(existingGames);
    }

    public List<Integer> getExistingGames() {
        return existingGames;
    }
}