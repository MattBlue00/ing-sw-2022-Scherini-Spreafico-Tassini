package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.Server;

import java.util.NoSuchElementException;

import static it.polimi.ingsw.network.server.Server.LOGGER;

/**
 * This server-side class is the {@link GameController}'s subclass, and offers new (overridden) methods to handle the
 * game flow of a game in Expert mode.
 */

public class GameControllerExpertMode extends GameController{

    /**
     * Game controller constructor.
     */
    
    public GameControllerExpertMode() {
        super();
    }

    /**
     * Establishes the right flow of the current player's Action Phase by using the controller's variables.
     * This overridden version has two more cases: case {@code ACTION_CHOICE} refers to those messages related to the
     * choice between playing a Character Card and moving a student, while case {@code CHARACTER_CARD_REPLY} refers to
     * those messages related to the Character Card handling.
     *
     * @param message the message sent by the client.
     * @throws TryAgainException if an exception cannot be caught by the controller, it is caught by the {@link Server} class.
     */

    @Override
    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
            case ACTION_CHOICE:
                if(((ActionChoiceMessage) message).getChoice().equals("STUDENT")) {
                    if (!getVirtualViewMap().isEmpty())
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                }
                else {
                    if (!getVirtualViewMap().isEmpty() && !getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed())
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCharacterCard();
                }
                break;
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                try {
                    if (getMovesLeft() == 0) {
                        throw new WrongMessageSentException("No moves left!");
                    } else {
                        handleStudentMovement(message);
                        setMovesLeft(getMovesLeft() - 1);
                        if (!getVirtualViewMap().isEmpty() && getMovesLeft() > 0) {
                            LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has moved a student.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            if (!getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed())
                                getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
                            else
                                getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                        }
                        if (!getVirtualViewMap().isEmpty() && getMovesLeft() == 0) {
                            LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has run out of students' moves.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMotherNatureSteps();
                        }
                    }
                }
                catch(FullTableException | StudentNotFoundException | IslandNotFoundException | NonExistentColorException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
                    }
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                try {
                    if (getMovesLeft() == 0 && !hasMotherNatureMoved()) {
                        handleMotherNature(message);
                        setMotherNatureMoved(true);
                        getGame().islandConquerCheck(getGame().getBoard().getMotherNaturePos());
                        if (!getVirtualViewMap().isEmpty()) {
                            LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has moved Mother Nature.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCloud();
                        }
                    } else
                        throw new WrongMessageSentException("You need to move other " + getMovesLeft() +
                                " students before moving Mother Nature!");
                }
                catch(InvalidNumberOfStepsException | IslandNotFoundException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMotherNatureSteps();
                    }
                }
                break;
            case CLOUD_CHOICE_REPLY:
                try {
                    if (hasMotherNatureMoved()) {
                        handleCloudChoice(message);
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has chosen a cloud.");
                    }
                    else
                        throw new WrongMessageSentException("You need to move mother nature first!");
                }
                catch(IndexOutOfBoundsException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage("There's no cloud with such id, please try again.");
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                catch(EmptyCloudException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                break;
            case CHARACTER_CARD_REPLY:
                try {
                    if (!hasMotherNatureMoved()) {
                        handleCharacterCardChoice(message);
                        if (!getVirtualViewMap().isEmpty()) {
                            LOGGER.info(getGame().getCurrentPlayer().getNickname());
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                        }
                    } else
                        throw new WrongMessageSentException("You are no longer able to play a character card!");
                }
                catch(CharacterCardAlreadyPlayedException | NotEnoughCoinsException | CharacterCardNotFoundException |
                        StudentNotFoundException | NoVetoTilesException | IslandNotFoundException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
                    }
                }
                catch(NoSuchElementException e){
                    if(!getVirtualViewMap().isEmpty()) {
                        LOGGER.info(getGame().getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + e.getMessage());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).
                                showGenericMessage("There's no Character Card with such ID!");
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
                    }
                }
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /**
     * Allows the current player to play a Character Card, if possible. Furthermore, if there are any, sets
     * the chosen card's parameters as specified in the message. If no exception is thrown, every player
     * (except the current one) will be notified.
     *
     * @param receivedMessage the message sent by the client.
     * @throws NotEnoughCoinsException if the current player cannot afford the price of the chosen card.
     * @throws CharacterCardAlreadyPlayedException if the current player has already played a Character Card during the
     *                                             same round.
     * @throws CharacterCardNotFoundException if the current player has chosen an ID that does not correspond to any of
     *                                        the three cards available in the game.
     */

    public void handleCharacterCardChoice(Message receivedMessage) throws TryAgainException, NoSuchElementException {

        int chosenCardID = ((CharacterCardMessage) receivedMessage).getCardID();

        if(getGame() instanceof GameExpertMode) {

            if(receivedMessage instanceof CharacterCardMessageInt) {
                ((IntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageInt) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageString) {
                ((StringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageString) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageStringInt) {
                ((StringIntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID))
                        .doOnClick(((CharacterCardMessageStringInt) receivedMessage).getPar1(), ((CharacterCardMessageStringInt) receivedMessage).getPar2());
            }
            if(receivedMessage instanceof CharacterCardMessageArrayListString){
                ((ArrayListStringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageArrayListString) receivedMessage).getPar());
            }

            ((GameExpertMode) getGame()).playerPlaysCharacterCard(chosenCardID);
            if(!getVirtualViewMap().isEmpty())
                broadcastUpdateMessage(getGame().getCurrentPlayer().getNickname() + " has played the " +
                        ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID).getClass().getSimpleName() + " Character Card!");

        }
    }

    /**
     * Sets the controller's variables and the players' order so that the first player of the current round's
     * Action Phase can play properly. This overridden version asks the next player which action to undertake
     * next (play a card or move a student) rather than asking where to move a student.
     */

    @Override
    public void endPlanningPhase() {
        setPlanningPhaseDone(true);
        setOrder();
        getGame().setCurrentPlayer(getGame().getPlayers().get(0));
        setCurrentPlayerIndex(0);
        setMovesLeft(getGame().getConstants().PLAYER_MOVES);
        if(!getVirtualViewMap().isEmpty()) {
            LOGGER.info("Action Phase is about to start.");
            broadcastPhaseUpdate(true);
            broadcastGameStatusFirstActionPhase();
            broadcastWaitingMessage();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
        }
    }

    /**
     * Resets the controller's variables in order to let the next player play his Action Phase properly. This
     * overridden version handles one more variable, specific to the Expert mode.
     */

    @Override
    public void nextPlayerActionPhase(){
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(getGame().getConstants().PLAYER_MOVES);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        if(!getVirtualViewMap().isEmpty()) {
            broadcastGameBoard();
            broadcastWaitingMessage();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
        }
    }

    /**
     * Sets the controller's variables in order to let the players play the next round properly. This overridden
     * version handles one more variable, specific to the Expert mode. If the game board's students' bag is empty,
     * instantly ends the game.
     */

    @Override
    public void nextRound(){
        try{
            if(isStudentBagEmpty())
                declareWinningPlayer();
            getGame().refillClouds();
        }
        catch(TieException e){
            quit();
        }
        catch(EmptyBagException ex){
            LOGGER.info(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        setCurrentPlayerIndex(0);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlayerPlanningPhaseDone(false);
        setPlanningPhaseDone(false);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        getGame().setRoundNumber(getGame().getRoundNumber() + 1);
        for(Player player : getGame().getPlayers())
            player.resetLatestAssistantCardPlayed();
        if(!getVirtualViewMap().isEmpty()) {
            LOGGER.info("Planning Phase is about to start.");
            broadcastGameBoard();
            broadcastPhaseUpdate(false);
            showDeck(getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()));
            broadcastWaitingMessage();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }
}
