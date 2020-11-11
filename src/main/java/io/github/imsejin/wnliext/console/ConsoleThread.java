package io.github.imsejin.wnliext.console;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import lombok.Setter;

import static io.github.imsejin.wnliext.common.Constants.console.PERCENT_MULTIPLES;
import static io.github.imsejin.wnliext.common.Constants.console.PROGRESS_BAR_LENGTH;

/**
 * Console thread
 */
public final class ConsoleThread implements Runnable {

    private static final int compensatoryMultiples = PERCENT_MULTIPLES / PROGRESS_BAR_LENGTH;

    private static final ColoredPrinter cp = new ColoredPrinter.Builder(1, false)
            .foreground(FColor.WHITE)
            .background(BColor.BLACK)
            .build();

    @Setter
    private WorkingProcess workingProcess;

    public static void printMetadata(String[] appTitle) {
        for (int i = 0; i < appTitle.length; i++) {
            String line = appTitle[i];

            for (int j = 0; j < line.length(); j++) {
                if (i < 3) {
                    // By third line
                    if (j < 39) {
                        // Webtoon
                        cp.print(line.charAt(j), Attribute.BOLD, FColor.GREEN, BColor.NONE);
                    } else {
                        // List
                        cp.print(line.charAt(j), Attribute.BOLD, FColor.CYAN, BColor.NONE);
                    }
                } else if (i < 6) {
                    // By fourth line
                    if (j < 44 - 4) {
                        // Webtoon
                        cp.print(line.charAt(j), Attribute.BOLD, FColor.GREEN, BColor.NONE);
                    } else {
                        // List
                        cp.print(line.charAt(j), Attribute.BOLD, FColor.CYAN, BColor.NONE);
                    }
                } else if (i < 12) {
                    // Extractor
                    cp.print(line.charAt(j), Attribute.BOLD, FColor.YELLOW, BColor.NONE);
                } else {
                    if (j < 35) {
                        // :: WebtoonList Extractor ::
                        cp.print(line.charAt(j), Attribute.LIGHT, FColor.MAGENTA, BColor.NONE);
                    } else {
                        // (v1.0.0.RELEASE)
                        cp.print(line.charAt(j), Attribute.LIGHT, FColor.RED, BColor.NONE);
                    }
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    @Override
    public synchronized void run() {
        printProcessing();
    }

    public synchronized void printProcessing() {
        String message = workingProcess.getMessage();
        int totalProcess = workingProcess.getTotalProcess();
        int i;

        // Gets changing current process and prints progress states
        while ((i = workingProcess.getCurrentProcess() + 1) < totalProcess) {
            // Calculates the percentage of current progress
            double percentage = ((double) i / totalProcess) * PERCENT_MULTIPLES;

            // Prints progress bar
            System.out.print(" |");
            for (int j = 0; j < PROGRESS_BAR_LENGTH; j++) {
                if (percentage > j * compensatoryMultiples) {
                    cp.print(" ", Attribute.HIDDEN, FColor.GREEN, BColor.GREEN);
                } else {
                    cp.print(" ", Attribute.HIDDEN, FColor.WHITE, BColor.WHITE);
                }
            }
            System.out.print("| ");

            // Prints currently processing item and current process
            System.out.print(message + "... ");
            System.out.print("(" + i + "/" + totalProcess + ")");

            // Enable console typewriter to overwrite them
            System.out.print("\r");
        }
    }

}
