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

    public void init(){
        out.println("   ('-.  _  .-')             ('-.         .-') _  .-') _                 .-')    \n" +
                " _(  OO)( \\( -O )           ( OO ).-.    ( OO ) )(  OO) )               ( OO ).  \n" +
                "(,------.,------.  ,-.-')   / . --. /,--./ ,--,' /     '._  ,--.   ,--.(_)---\\_) \n" +
                " |  .---'|   /`. ' |  |OO)  | \\-.  \\ |   \\ |  |\\ |'--...__)  \\  `.'  / /    _ |  \n" +
                " |  |    |  /  | | |  |  \\.-'-'  |  ||    \\|  | )'--.  .--'.-')     /  \\  :` `.  \n" +
                "(|  '--. |  |_.' | |  |(_/ \\| |_.'  ||  .     |/    |  |  (OO  \\   /    '..`''.) \n" +
                " |  .--' |  .  '.',|  |_.'  |  .-.  ||  |\\    |     |  |   |   /  /\\_  .-._)   \\ \n" +
                " |  `---.|  |\\  \\(_|  |     |  | |  ||  | \\   |     |  |   `-./  /.__) \\       / \n" +
                " `------'`--' '--' `--'     `--' `--'`--'  `--'     `--'     `--'       `-----'  ");

        out.println("Welcome to Eriantys!\n");
        askServerData();
    }

    public void askServerData(){
        String address;
        String port;

        boolean addressIsValid;
        boolean portIsValid;

        String defaultAddress = "127.0.0.1";
        int defaultPort = 12345;

        out.println("Please enter the following values to connect to the server [type enter to use default values]");
        do{
            out.println("Enter the server IP address [ default = "+defaultAddress+" ] : ");
            try {
                addressIsValid = true;
                address = readLine();
                if(address.equals("")) address = defaultAddress;
                else if(!ClientController.isValidAddress(address)){
                    addressIsValid = false;
                    out.println("Address is not valid, please enter the value again: ");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }while(!addressIsValid);

        do{
            out.println("Enter the server port [ default = "+defaultPort+" ] : ");
            try {
                portIsValid = true;
                port = readLine();
                if(port.equals("")) port = String.valueOf(defaultPort);
                else if(!ClientController.isValidPort(port)) {
                    portIsValid = false;
                    out.println("Port is not valid, please enter the value again: ");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }while(!portIsValid);

        int finalPortValue = Integer.parseInt(port);
        String finalAddress = address;

        notifyObserver(viewObserver -> viewObserver.onUpdateServerData(finalAddress, finalPortValue));
    }

    @Override
    public void askNickname() {
        out.println("Enter your nickname [must be unique] : ");
        try {
            String nickname = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCreateOrJoin() {
        out.println("Choose to create a new game or join an existing one [ type CREATE to create or JOIN to join ] :");
        String choice;
        try {
            do {
                choice = readLine();
                if(!choice.equalsIgnoreCase("CREATE") && !choice.equalsIgnoreCase("JOIN"))
                    out.println("Please enter a valid choice [ type CREATE to create or JOIN to join ] : ");
            }while(!choice.equalsIgnoreCase("CREATE") && !choice.equalsIgnoreCase("JOIN"));

            String finalChoice = choice;
            notifyObserver(viewObserver -> viewObserver.onUpdateCreateOrJoin(finalChoice));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askGameInfo() {
        out.println("To create a new game insert all the required parameters : ");
        try {
            out.println("Type the number (ID) of the game you want to create [ game number must be unique ]: ");
            int gameNumber = Integer.parseInt(readLine());
            out.println("Chose the type of game you want to play [ For Expert mode type TRUE, for normal mode type FALSE ] : ");
            boolean gameMode = Boolean.parseBoolean(readLine());
            out.println("Enter the number of players for the game [ 2 or 3 ] : ");
            int numOfPlayers = Integer.parseInt(readLine());
            notifyObserver(viewObserver -> viewObserver.onUpdateGameInfo(gameNumber, gameMode, numOfPlayers));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askGameNumber() {
        out.println("Type the number of the game you want to join : ");
        try {
            String gameNumber = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateGameNumber(Integer.parseInt(gameNumber)));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askWizardID() {
        out.println("Choose your wizard for this game between: [ BLUE_WIZARD | YELLOW_WIZARD | GREEN_WIZARD | PINK_WIZARD ] : ");
        out.println("(The wizard must be unique)");
        try {
            String wizard = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateWizardID(wizard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showGenericMessage(String message) {
        out.println(message);
    }


    public void clearInterface(){
        out.flush();
    }
}
