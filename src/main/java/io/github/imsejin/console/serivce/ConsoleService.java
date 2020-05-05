package io.github.imsejin.console.serivce;

import static io.github.imsejin.common.Constants.console.*;

import java.io.IOException;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

import io.github.imsejin.console.model.WorkingProcess;
import lombok.Setter;

/**
 * ConsoleService
 * 
 * @author SEJIN
 */
public class ConsoleService implements Runnable {

    private final int compensatoryMultiples = PERCENT_MULTIPLES / PROGRESS_BAR_LENGTH;

    @Setter
    private WorkingProcess workingProcess;

    private static final ProcessBuilder COMMAND = new ProcessBuilder("cmd", "/c", "cls").inheritIO();
    private static final ColoredPrinter cp = new ColoredPrinter.Builder(1, false)
	        .foreground(FColor.WHITE)
			.background(BColor.BLACK)
			.build();

	public synchronized void printProcessing() throws InterruptedException {
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

	public static void clear() {
		try {
			COMMAND.start().waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		try {
			printProcessing();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
