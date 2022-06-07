package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.Map;

/**
 * Defines a generic view to be implemented by each view type (e.g. CLI, GUI in JavaFX, ...).
 */
public interface View {

    /**
     * Asks the client informations about what server they want to try to connect to.
     */

    void askServerData();

    /**
     * Asks the client which nickname they want to use.
     * If it is unique the client is connected to the server, else they are asked to choose a different nickname.
     */

    void askNickname();

    /**
     * Asks the client if they want to create or join a game.
     */

    void askCreateOrJoin();

    /**
     * Asks the client about the game info: number of players and game mode.
     */

    void askGameInfo();

    /**
     * Asks the client the gameID (which must be unique).
     * If it is unique a new game is created, else they are asked to choose a different gameID.
     */

    void askGameNumber();

    /**
     * Asks the client which WizardID they want to choose (must be unique).
     * If it is unique the client is added to the game, else they are asked to choose a different Wizard.
     */

    void askWizardID();

    /**
     * Asks the client which AssistantCard they want to play.
     */

    void askAssistantCard();

    /**
     * Asks the client where they want to move the student (on an island or on the correspondent table).
     */

    void askMoveStudent();

    /**
     * Asks the client how many steps MotherNature will have to be moved.
     */

    void askMotherNatureSteps();

    /**
     * Asks the client which cloudID they want to choose.
     */

    void askCloud();

    /**
     * When in GameExpertMode asks the client which CharacterCard they want to play.
     */

    void askCharacterCard();

    /**
     * When in GameExpertMode asks the client if they want to play a CharacterCard or to move a student.
     */

    void askAction();

    /**
     * Shows a generic message to the client.
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
     * Shows the client in which round fase they are.
     * If {@code isActionPhase} is {@code true} then it will show the action phase, else it will show the planning phase.
     *
     * @param isActionPhase
     */

    void showPhaseUpdate(boolean isActionPhase);

}
