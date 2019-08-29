package io.github.imsejin.excel.service;

import java.io.IOException;

/**
 * ConsoleService
 * 
 * @author SEJIN
 */
public class ConsoleService implements Runnable {
	
	private String message;
	
	private Integer current;

	private final Integer total;
	
	public ConsoleService(String message, Integer total) {
		this.message = message;
		this.total = total;
	}
	
	public void setCurrent(Integer current) {
		this.current = current;
	}

	public void printProcessing() throws InterruptedException {
		clear();
		int totalNum = total.intValue();
		int i;
		while ((i = current.intValue() + 1) < totalNum) {
			double percentage = ((double) i / (double) totalNum) * 50.0;
			for (int j = 0; j < 50; j++) {
				if (percentage > j) {
					System.out.print("=");
				} else {
					System.out.print("-");
				}
			}
			
			System.out.println();
			System.out.println(message + " " + i + " / " + totalNum);
			Thread.sleep(50);
			clear();
		}
	}
	
	private void clear() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			printProcessing();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
