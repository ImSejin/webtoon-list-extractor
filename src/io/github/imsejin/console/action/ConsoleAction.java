package io.github.imsejin.console.action;

import io.github.imsejin.console.model.WorkingProcess;
import io.github.imsejin.console.serivce.ConsoleService;

/**
 * ConsoleAction
 * 
 * @author SEJIN
 */
public class ConsoleAction {
	
	private static final String APPLICATION_TITLE = "	                 _                    _    _     _  \r\n" + 
													"	___ __   ____   | |                  | |  (_)   | |\r\n" + 
													"	\\  \\  \\ /   /___| |_  ___  ___  _ ___| |   _ ___| |_ \r\n" + 
													"	 \\  \\  \\   / __ \\  _|/ _ \\/ _ \\| '_  \\ |  | | __|  _|\r\n" + 
													"	  \\  \\  \\ /| ___/ |_( (_) )(_) ) | | | |__| |__ \\ |_\r\n" + 
													"	   \\__\\__\\ \\____|\\__|\\___/\\___/|_| |_|____|_|___/\\__|\r\n" + 
													"	    _____       _                  _  \r\n" + 
													"	   |  ___|     | |                | |\r\n" + 
													"	   | |___ __  _| |_ _ __ __ _  ___| |_  ___  _ __\r\n" + 
													"	   |  ___|\\ \\/ /  _| '_ / _` |/ __|  _|/ _ \\| '__|\r\n" + 
													"	   | |___ /    \\ |_| | | (_| | (__| |_( (_) ) | \r\n" + 
													"	   |_____|__/\\__\\__|_|  \\__,_|\\___|\\__|\\___/|_|  ";

	public ConsoleAction() {
		// Initially clear console
		ConsoleService.clear();

		System.out.println(APPLICATION_TITLE);
		System.out.println();
	}

	public synchronized static void print(WorkingProcess workingProcess) {
		// ConsoleService.clear();

		ConsoleService consoleService = new ConsoleService();
		consoleService.setWorkingProcess(workingProcess);

		Thread t = new Thread(consoleService);
		t.start();
	}

}
