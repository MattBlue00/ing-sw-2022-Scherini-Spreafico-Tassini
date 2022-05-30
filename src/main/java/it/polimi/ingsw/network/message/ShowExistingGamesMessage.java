package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.GameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowExistingGamesMessage extends Message{

    private final Map<Integer, GameController> existingGames;

    public ShowExistingGamesMessage(Map<Integer, GameController> existingGames) {
        super(null, MessageType.EXISTING_GAMES);
        this.existingGames = existingGames;
    }

    public Map<Integer, GameController> getExistingGames() {
        return existingGames;
    }
}