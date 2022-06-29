package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;

import java.util.Map;

/**
 * This interface defines a generic view to be implemented by each view type (e.g. CLI, GUI in JavaFX, ...).
 */

public interface View {

    /**
     * Asks the client information about what server they want to try to connect to.
     */

    void askServerData();

    /**
     * Asks the client which nickname they want to use.
     * If it is unique, the client joins the lobby; otherwise, they are asked to choose a different nickname.
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
     * Asks the client which Assistant Card they want to play.
     */

    void askAssistantCard();

    /**
     * Asks the client where they want to move the student (on an island or on the correspondent table).
     */

    void askMoveStudent();

    /**
     * Asks the client how many steps they wish to move Mother Nature of.
     */

    void askMotherNatureSteps();

    /**
     * Asks the client which cloud they want to choose.
     */

    void askCloud();

    /**
     * Asks the client which Character Card they want to play (Expert mode exclusive).
     */

    void askCharacterCard();

    /**
     * Asks the client if they want to play a CharacterCard or to move a student (Expert mode exclusive).
     */

    void askAction();

    /**
     * Shows a generic message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    void showGenericMessage(String message);

    /**
     * Shows to the client the existing games they may join.
     */

    void showExistingGames(Map<Integer, GameController> existingGames);

    /**
     * Shows to the client the latest Assistant Cards played, the Game Board and the players' order.
     *
     * @param game the game whose status needs to be shown.
     */

    void showGameStatusFirstActionPhase(Game game);

    /**
     * Shows the current Game Board to the client.
     *
     * @param game the game whose status needs to be shown.
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
     * Shows the client in which round phase they are.
     *
     * @param isActionPhase {@code true} if the current player is about to play the Action Phase, {@code false}
     *        otherwise.
     */

    void showPhaseUpdate(boolean isActionPhase);

    /**
     * Shows the client what happened on the {@link GameBoard}.
     *
     * @param s the update to notify.
     */

    void showUpdateMessage(String s);

    /**
     * Closes the client's app.
     */

    void quit();

    /**
     * Sets the view user's nickname.
     *
     * @param nickname the nickname chosen by the client.
     */

    void setNickname(String nickname);
}
