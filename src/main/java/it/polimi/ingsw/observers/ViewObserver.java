package it.polimi.ingsw.observers;

import java.util.ArrayList;

/**
 * Custom observer interface for views. It supports different types of notification.
 */
public interface ViewObserver {

    /**
     * Creates a new connection to the server with the updated info.
     *
     * @param address server address
     * @param port server port
     */
    void onUpdateServerData(String address, int port);

    /**
     * Sends to the server a login request with the chosen nickname.
     *
     * @param nickname the nickname the client wants to (try to) login with.
     */
    void onUpdateNickname(String nickname);

    /**
     * Based on the client's choice, asks for the game parameters (if the choice is "CREATE") or asks for an
     * existing game number (if the choice is "JOIN").
     *
     * @param choice the choice made by the client.
     */
    void onUpdateCreateOrJoin(String choice);

    /**
     * Sends to the server the game parameters chosen by the client, so that it can build the desired game.
     *
     * @param gameNumber the ID of the game the client wishes to create.
     * @param mode the game mode of the game the client wishes to create ({@code false} -> NORMAL, {@code true} -> EXPERT).
     * @param numOfPlayers the maximum number of players of the game the client wishes to create.
     */
    void onUpdateGameInfo(int gameNumber, boolean mode, int numOfPlayers);

    /**
     * Sends to the server the ID of the game the client wishes to join.
     *
     * @param gameNumber the ID of the game the client wishes to join.
     */
    void onUpdateGameNumber(int gameNumber);

    /**
     * Sends to the server the Wizard ID the client wishes to embody for that game.
     *
     * @param wizardID the {@code String} representing the WizardID the client wishes to embody.
     */
    void onUpdateWizardID(String wizardID);

    /**
     * Sends to the server the Assistant Card the client wishes to play.
     *
     * @param assistantCard the Assistant Card the client wishes to play.
     */
    void onUpdateAssistantCard(String assistantCard);

    /**
     * Sends to the server the color of the student the client wishes to move, as well as the ID of the island
     * the client wishes to move it onto.
     *
     * @param color the color of the student the client wishes to move.
     * @param islandID the ID of the island the client wishes to move the student onto.
     */
    void onUpdateIslandStudentMove(String color, int islandID);

    /**
     * Sends to the server the color of the student the client wishes to move to its table.
     *
     * @param color the color of the student the client wishes to move to its table.
     */
    void onUpdateTableStudentMove(String color);

    /**
     * Sends to the server the number of steps the client wishes Mother Nature to move of.
     *
     * @param steps the steps the client wishes Mother Nature to move of.
     */
    void onUpdateMotherNatureSteps(int steps);

    /**
     * Sends to the server the ID of the cloud the client wishes to take students from.
     *
     * @param cloudID the ID of the cloud the client wishes to take students from.
     */
    void onUpdateCloudChoice(int cloudID);

    /**
     * Sends to the server the ID of the Character Card the client wishes to play.
     * Note that this method is called if no parameters are required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     */
    void onUpdateCharacterCard(int characterCardID);

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the {@code int} parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par the required {@code int} parameter (its meaning changes from card to card).
     */
    void onUpdateCharacterCardInt(int characterCardID, int par);

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the String parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par the required {@code String} parameter (its meaning changes from card to card).
     */
    void onUpdateCharacterCardString(int characterCardID, String par);

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the String and the {@code int} parameters required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par1 the required {@code String} parameter.
     * @param par2 the required {@code int} parameter.
     */
    void onUpdateCharacterCardStringInt(int characterCardID, String par1, int par2);

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the list of {@code String} parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param list the required list of {@code String} parameters (its meaning changes from card to card).
     */
    void onUpdateCharacterCardArrayListString(int characterCardID, ArrayList<String> list);

    /**
     * Sends to the server a String that contains the client's choice between playing a Character Card or
     * moving a student (this choice is only possible in Expert Mode).
     *
     * @param choice the choice made by the client.
     * */
    void onUpdateActionChoice(String choice);

}
