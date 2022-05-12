package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CommandLineInterface extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;

    public CommandLineInterface() {
        this.out = System.out;
    }

    private String readLine() throws ExecutionException{
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());

        inputThread = new Thread(futureTask);
        inputThread.start();
        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }


    @Override
    public void askNickname() {
        out.println("Enter your nickname: ");
        try {
            String nickname = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCreateOrJoin() {

    }

    @Override
    public void askGameInfo() {

    }

    @Override
    public void askWizardID() {

    }
}
