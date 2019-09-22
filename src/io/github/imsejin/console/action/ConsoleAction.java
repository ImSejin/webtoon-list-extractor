package io.github.imsejin.console.action;

import io.github.imsejin.common.ApplicationMetadata;
import io.github.imsejin.console.model.WorkingProcess;
import io.github.imsejin.console.serivce.ConsoleService;

/**
 * ConsoleAction
 * 
 * @author SEJIN
 */
public class ConsoleAction {

	public ConsoleAction() {
		// Initially clear console
		ConsoleService.clear();

		ApplicationMetadata.printMetadata();
	}

	public synchronized static void print(WorkingProcess workingProcess) {
		// ConsoleService will be multiply used in thread
		ConsoleService consoleService = new ConsoleService();
		consoleService.setWorkingProcess(workingProcess);

		// Separates upper console logs
		System.out.println();
		System.out.println();

		Thread t = new Thread(consoleService);
		t.start();
	}

}
