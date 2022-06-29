package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.GameController;

import java.util.Map;

/**
 * This {@link Message} is used to communicate to the clients the games currently present on the server.
 */

public class ShowExistingGamesMessage extends Message{

    private final Map<Integer, GameController> existingGames;

    /**
     * Default constructor.
     *
     * @param existingGames a Map containing the game IDs and the associated game controllers.
     */

    public ShowExistingGamesMessage(Map<Integer, GameController> existingGames) {
        super(null, MessageType.EXISTING_GAMES);
        this.existingGames = existingGames;
    }

    /**
     * Returns a map containing the game IDs as keys and the game controllers as values.
     *
     * @return a map containing the game IDs as keys and the game controllers as values.
     */

    public Map<Integer, GameController> getExistingGames() {
        return existingGames;
    }
}