package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.model.exceptions.NonExistentCharacterCardException;
import it.polimi.ingsw.model.exceptions.NonExistentTableException;

import java.util.List;

public class GameExpertMode extends Game {

    private final CharacterCard[] characters;

    public GameExpertMode(int playersNumber) {
        super(playersNumber);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    // TODO: at the end of player's turn, set the card's status to false
    public void playerPlaysCharacterCard(int id) {

        try {
            if (getCurrentPlayer().getCharacterCardAlreadyPlayed())
                throw new CharacterCardAlreadyPlayedException("You have already played a character card this round!");

            boolean found = false;
            for (CharacterCard card : characters) {
                if (card.getId() == id) {
                    card.setIsActive(true);
                    found = true;
                }
            }

            if (!found)
                throw new NonExistentCharacterCardException("No character card with id " + id + " exists.");
            else
                getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

        } catch (CharacterCardAlreadyPlayedException | NonExistentCharacterCardException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {
        setMaxSteps(getCurrentPlayer().getLastCardPlayed().getMotherNatureSteps());
        for (CharacterCard card : characters) {
            if (card.getId() == 4 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
            }
        }
        if (steps > getMaxSteps() || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);
    }

    @Override
    public void profCheck() {

        boolean done = false;
        for (CharacterCard card : characters) {
            if (card.getId() == 2 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }

        if(!done)
            Game.profCheckAlgorithm(getPlayersNumber(), getPlayers());

    }

    @Override
    public void islandConquerCheck(int islandID){

        boolean done = false;
        for(CharacterCard card : characters){
            if(card.getId() == 9 && card.getIsActive()){
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }

        if(!done)
            getBoard().islandConquerCheck(getCurrentPlayer(), islandID);

    }

}
