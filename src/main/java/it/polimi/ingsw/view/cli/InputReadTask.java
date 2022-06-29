package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * This class is used to read the input stream and making the input kind of "interruptable".
 */

public class InputReadTask implements Callable<String> {

    private final BufferedReader br;

    /**
     * InputReadTask constructor.
     */

    public InputReadTask() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Allows the thread to collect input, if there is any.
     *
     * @return the input collected.
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if any other thread has interrupted this thread.
     */

    @Override
    public String call() throws IOException, InterruptedException {
        String input;
        // wait until there is data to complete a readLine()
        while (!br.ready()) {
            Thread.sleep(200);
        }
        input = br.readLine();
        return input;
    }
}