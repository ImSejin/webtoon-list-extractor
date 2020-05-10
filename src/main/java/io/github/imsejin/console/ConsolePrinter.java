package io.github.imsejin.console;

import java.io.IOException;

import io.github.imsejin.common.ApplicationMetadata;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * ConsolePrinter
 * 
 * @author SEJIN
 */
@UtilityClass
public class ConsolePrinter {
    
    private final ProcessBuilder COMMAND = new ProcessBuilder("cmd", "/c", "cls").inheritIO();

    static {
        // Initially clear console
        clear();

        // Prints the information of this application
        ConsoleThread.printMetadata(ApplicationMetadata.APPLICATION_TITLE);
    }

    @SneakyThrows({ IOException.class, InterruptedException.class })
    public void clear() {
        COMMAND.start().waitFor();
    }

    public synchronized void print(WorkingProcess workingProcess) {
        // ConsoleThread will be multiply used in thread
        ConsoleThread thread = new ConsoleThread();
        thread.setWorkingProcess(workingProcess);

        // Separates upper console logs
        System.out.println("\n");

        new Thread(thread).start();
    }

}
