package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;

import java.util.Map;

public interface View {

    /**
     * Sends a message to the client to ask what server they want to try to connect to.
     */

    void askServerData();

    /**
     * Sends a message to the client to ask which nickname they want to use.
     */

    void askNickname();

    /**
     * Sends a message to the client to ask if they want to create or join a game.
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
     * Sends a message to the client to ask which WizardID they want to choose (must be unique).
     */

    void askWizardID();

    /**
     * Sends a message to the client to ask which AssistantCard they want to play.
     */

    void askAssistantCard();

    /**
     * Sends a message to the client to ask where they want to move the student (on an island or on the correspondent table).
     */

    void askMoveStudent();

    /**
     * Sends a message to the client to ask how many steps MotherNature will have to be moved.
     */

    void askMotherNatureSteps();

    /**
     * Sends a message to the client to ask which cloudID they want to choose.
     */

    void askCloud();

    /**
     * When in GameExpertMode sends a message to the client to ask which CharacterCard they want to play.
     */

    void askCharacterCard();

    /**
     * When in GameExpertMode sends a message to the client to ask if they want to play a CharacterCard or to move a student.
     */

    void askAction();

    /**
     * Sends a  generic message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    void showGenericMessage(String message);

    /**
     * Shows to the client the existing games they can join.
     */

    void showExistingGames(Map<Integer, GameController> existingGames);

    /**
     * Shows the last AssistantCards played, the GameBoard and the players' order.
     *
     * @param game the game whose status needs to be shown.
     */

    void showGameStatusFirstActionPhase(Game game);

    /**
     * Shows the current GameBoard to the client.
     */

    void showGameStatus(Game game);

    /**
     * Shows to the client the AssistantCards they can play.
     *
     * @param game the game whose status needs to be shown.
     */

    void showDeck(Game game);

    /**
     * Shows a disconnection message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    void showDisconnectionMessage(String message);

    /**
     * Shows a phase update message to the client
     *
     * @param message the message that will be shown to the client.
     */

    void showPhaseUpdate(boolean isActionPhase);

}
