package it.polimi.ingsw.model;

public enum Wizard {
    BLUE, PINK, YELLOW, GREEN;

    public static boolean isWizardValid(String wizardID){
        if(wizardID == null)
                return false;
        Wizard[] wizards = Wizard.values();
        for(Wizard wizard : wizards){
            if(wizard.toString().equals(wizardID))
                    return true;
        }
        return false;
    }

}
