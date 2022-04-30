package it.polimi.ingsw.observers;

import it.polimi.ingsw.model.Wizard;

import java.util.Map;

public interface ViewObserver {

    void onUpdateNickname(String nickname);

    void onUpdateWizardID(String wizardID);

    void onUpdateServerInfo(Map<String, String> serverInfo);
}
