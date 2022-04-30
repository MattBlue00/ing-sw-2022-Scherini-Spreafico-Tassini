package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.client.ClientController;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CommandLineInterface extends ViewObservable implements View {

    private final String INPUT_CANCELED = "An error has occurred. Your input has been canceled.";
    private final PrintStream out;
    private Thread inputThread;

    public CommandLineInterface() {
        this.out = System.out;
    }

    public void init() {
        out.println("Welcome to Eriantys Board Game!");
        try {
            askServerInfo();
        } catch (ExecutionException e) {
            out.println(INPUT_CANCELED);
        }
    }

    private void askServerInfo() throws ExecutionException{
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;

        out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");

            String address = readLine();

            if (address.equals("")) {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            } else if (ClientController.isValidIpAddress(address)) {
                serverInfo.put("address", address);
                validInput = true;
            } else {
                out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);

        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            String port = readLine();

            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true;
                } else {
                    out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);

        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
    }

    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        inputThread = new Thread(futureTask);
        inputThread.start();
        String input = null;
        try {
            input = futureTask.get();
        }
        catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    @Override
    public void askPlayerData() {
        try {
            out.print("Enter your nickname: ");
            String nickname = readLine();
            notifyObserver(obs -> obs.onUpdateNickname(nickname));
            String wizardID = null;
            while(!Wizard.isWizardValid(wizardID)) {
                out.print("Enter a valid wizard ID: ");
                wizardID = readLine().toUpperCase();
            }
            String finalWizardID = wizardID;
            notifyObserver(obs -> obs.onUpdateWizardID(finalWizardID));
        } catch (ExecutionException e) {
            out.println(INPUT_CANCELED);
        }
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        if (nicknameAccepted && connectionSuccessful)
            out.println("Hello, " + nickname + "! You connected to the server.");
        else if (connectionSuccessful)
            askPlayerData();
        else if (nicknameAccepted) {
            out.println("Max number of players was reached. Connection was refused.");
            out.println("The app is shutting down.");
            System.exit(1);
        } else {
            showErrorAndExit("Server unreachable.");
        }
    }

    @Override
    public void showErrorAndExit(String error) {
        inputThread.interrupt();
        out.println("Error: " + error);
        out.println("The app is shutting down.");
        System.exit(1);
    }

    public void clearCli() {
        out.flush();
    }

}
