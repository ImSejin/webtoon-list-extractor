package io.github.imsejin.console.action;

import io.github.imsejin.base.model.ApplicationMetadata;
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
		// ConsoleService.clear();

		ConsoleService consoleService = new ConsoleService();
		consoleService.setWorkingProcess(workingProcess);

		Thread t = new Thread(consoleService);
		t.start();
	}

}
