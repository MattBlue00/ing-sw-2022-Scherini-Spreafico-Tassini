package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.model.exceptions.NonExistentCharacterCardException;

public class GameExpertMode extends Game{

    private final CharacterCard[] characters;

    public GameExpertMode(int playersNumber) {
        super(playersNumber);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    // TODO: at the end of player's turn, set the card's status to false
    public void playerPlaysCharacterCard(int id){

        try {
            if(getCurrentPlayer().getCharacterCardAlreadyPlayed())
                throw new CharacterCardAlreadyPlayedException("You have already played a character card this round!");
            else
                getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

            boolean found = false;
            for(CharacterCard card : characters){
                if(card.getId() == id) {
                    card.setIsActive(true);
                    found = true;
                }
            }

            if(!found)
                throw new NonExistentCharacterCardException("No character card with id " + id + " exists.");
            else
                getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

        }
        catch(CharacterCardAlreadyPlayedException | NonExistentCharacterCardException e){
            e.printStackTrace();
        }

    }

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {
        setMaxSteps(getCurrentPlayer().getLastCardPlayed().getMotherNatureSteps());
        for(CharacterCard card : characters){
            if(card.getId() == 4 && card.getIsActive()) {
                card.doEffect(this);
                card.setIsActive(false);
            }
        }
        if(steps > getMaxSteps() || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);
    }

}
