package io.github.imsejin.wnliext.console;

import io.github.imsejin.wnliext.common.ApplicationMetadata;
import lombok.SneakyThrows;

/**
 * ConsolePrinter
 *
 * @author SEJIN
 */
public final class ConsolePrinter {

    private static final ProcessBuilder COMMAND = new ProcessBuilder("cmd", "/c", "cls").inheritIO();

    private ConsolePrinter() {
    }

    @SneakyThrows
    public static void clear() {
        COMMAND.start().waitFor();
    }

    public static void printLogo() {
        // Initially clear console
        clear();

        // Prints the information of this application
        ConsoleThread.printMetadata(ApplicationMetadata.APPLICATION_TITLE);
    }

    public static synchronized void print(WorkingProcess workingProcess) {
        // ConsoleThread will be multiply used in thread
        ConsoleThread thread = new ConsoleThread();
        thread.setWorkingProcess(workingProcess);

        // Separates upper console logs
        System.out.println("\n");

        new Thread(thread).start();
    }

}
