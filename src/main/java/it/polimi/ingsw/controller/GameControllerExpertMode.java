package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.charactercards.IntCard;
import it.polimi.ingsw.model.charactercards.StringCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.*;

public class GameControllerExpertMode extends GameController{

    public GameControllerExpertMode() {
        super();
    }

    @Override
    public void actionPhase(Message message) throws IslandNotFoundException, WrongMessageSentException,
            InvalidNumberOfStepsException, EmptyCloudException, NotEnoughCoinsException,
            CharacterCardAlreadyPlayedException, CharacterCardNotFoundException, FullTableException,
            StudentNotFoundException, NonExistentColorException {

        switch(message.getMessageType()){
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                if(getMovesLeft() == 0){
                    throw new WrongMessageSentException("No moves left!");
                }
                else{
                    handleStudentMovement(message);
                    setMovesLeft(getMovesLeft() - 1);
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                if(getMovesLeft() == 0 && !getMotherNatureMoved()){
                    handleMotherNature(message);
                    setMotherNatureMoved(true);
                }
                else
                    throw new WrongMessageSentException("You need to move other " + getMovesLeft() +
                            " students before moving Mother Nature!");
                break;
            case CLOUD_CHOICE_REPLY:
                if(getMotherNatureMoved()) {
                    handleCloudChoice(message);
                    setPlayerActionPhaseDone(true);
                }
                else
                    throw new WrongMessageSentException("You need to move mother nature first!");
                break;
            case CHARACTER_CARD_REPLY:
                if(!getMotherNatureMoved())
                    handleCharacterCardChoice(message);
                else
                    throw new WrongMessageSentException("You are no longer able to play a character card!");
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /*
        This method lets the current player play a character card, if possible.
     */

    public void handleCharacterCardChoice(Message receivedMessage) throws
            CharacterCardAlreadyPlayedException, NotEnoughCoinsException, CharacterCardNotFoundException {

        int chosenCardID = ((CharacterCardMessage) receivedMessage).getCardID();

        if(getGame() instanceof GameExpertMode) {

            if(receivedMessage instanceof CharacterCardMessageInt) {
                ((IntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageInt) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageString) {
                ((StringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageString) receivedMessage).getPar());
            }

            ((GameExpertMode) getGame()).playerPlaysCharacterCard(chosenCardID);
        }
    }

    @Override
    public void nextPlayerActionPhase(){
        winCheck();
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
    }

    @Override
    public void nextRound(){
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        setCurrentPlayerIndex(0);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlanningPhaseDone(false);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        getGame().setRoundNumber(getGame().getRoundNumber() + 1);
    }
}
