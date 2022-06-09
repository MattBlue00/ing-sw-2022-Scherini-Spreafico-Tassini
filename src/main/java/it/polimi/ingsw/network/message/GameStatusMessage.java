package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * The message extends {@link Message}.
 * It contains the {@link Game} Model.
 * It is sent to the clients to show the current version of the GameBoard.
 */
public class GameStatusMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game the serialized Model.
     */
    public GameStatusMessage(Game game) {
        super(null, MessageType.GAME_STATUS);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
