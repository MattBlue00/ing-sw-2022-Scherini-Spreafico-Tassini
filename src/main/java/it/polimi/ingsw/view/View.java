package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;

import java.util.Map;

public interface View {
    void askServerData();
    /**
     * Sends a message to the client to ask which nickname he wants to use.
     */
    void askNickname();

    /**
     * Sends a message to the client to ask if he wants to create or join a game.
     */
    void askCreateOrJoin();

    /**
     * Sends a message to the client to ask the game info: number of players and game mode.
     */
    void askGameInfo();

    /**
     * Sends a message to the client to ask the gameID (which must be unique).
     */
    void askGameNumber();

    /**
     * Sends a message to the client to ask which WizardID he wants to choose (must be unique).
     */
    void askWizardID();

    /**
     * Sends a message to the client to ask which AssistantCard he wants to play.
     */
    void askAssistantCard();

    /**
     * Sends a message to the client to ask where he wants to move the student (on an island or on the correspondent table).
     */
    void askMoveStudent();

    /**
     * Sends a message to the client to ask how many steps MotherNature will have to be moved.
     */
    void askMotherNatureSteps();

    /**
     * Sends a message to the client to ask which cloudID he wants to choose.
     */
    void askCloud();

    /**
     * When in GameExpertMode sends a message to the client to ask which CharacterCard he wants to play.
     */
    void askCharacterCard();

    /**
     * When in GameExpertMode sends a message to the client to ask if he wants to play a CharacterCard or to move a student.
     */
    void askAction();

    /**
     * Sends a  generic message to the client.
     *
     * @param message the message that will be shown to the client.
     */
    void showGenericMessage(String message);

    /**
     * Shows to the client the existing games he can join.
     */
    void showExistingGames(Map<Integer, GameController> existingGames);

    /**
     * Shows the last AssistantCards played, the GameBoard and the players order.
     * @param game
     */
    void showGameStatusFirstActionPhase(Game game);

    /**
     * Shows the current GameBoard to the client.
     */
    void showGameStatus(Game game);

    /**
     * Shows to the client the AssistantCards he can play.
     *
     * @param game
     */
    void showDeck(Game game);

    /**
     * Shows a disconnection message to the client.
     *
     * @param message the message that will be shown to the client.
     */
    void showDisconnectionMessage(String message);
}
