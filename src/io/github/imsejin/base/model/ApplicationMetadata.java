package io.github.imsejin.base.model;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

/**
 * ApplicationMetadata
 * 
 * @author SEJIN
 */
public class ApplicationMetadata {

	public static final String APPLICATION_NAME = "WebtoonList Extractor";

	public static final String VERSION = "1.0.0";

	public static final String VERSION_NAME = "v" + VERSION + ".RELEASE";

	public static final String[] APPLICATION_TITLE = { "	                 _                     _    _     _",
														"	___ __   ____   | |                   | |  (_)   | |",
														"	\\  \\  \\ /   /___| |__  ___  ___  _ ___| |   _ ___| |_",
														"	 \\  \\  \\   / __ \\  _ \\/ _ \\/ _ \\| '_  \\ |  | | __|  _|",
														"	  \\  \\  \\ /| ___/ |_) )(_) )(_) ) | | | |__| |__ \\ |_",
														"	   \\__\\__\\ \\____|\\___/\\___/\\___/|_| |_|____|_|___/\\__|",
														"	    _____       _                  _", "	   |  ___|     | |                | |",
														"	   | |___ __  _| |_ _ __ __ _  ___| |_  ___  _ __",
														"	   |  ___|\\ \\/ /  _| '_ / _` |/ __|  _|/ _ \\| '__|",
														"	   | |___ /    \\ |_| | | (_| | (__| |_( (_) ) |",
														"	   |_____|__/\\__\\__|_|  \\__,_|\\___|\\__|\\___/|_|",
														"	   :: WebtoonList Extractor ::       (v1.0.0.RELEASE)" };

	private static final ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.WHITE)
			.background(BColor.BLACK).build();

	private ApplicationMetadata() {}

	public static void printMetadata() {
		for (int i = 0; i < APPLICATION_TITLE.length; i++) {
			String line = APPLICATION_TITLE[i];

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
				} else if (3 <= i && i < 6) {
					// By fourth line
					if (j < 44 - 4) {
						// Webtoon
						cp.print(line.charAt(j), Attribute.BOLD, FColor.GREEN, BColor.NONE);
					} else {
						// List
						cp.print(line.charAt(j), Attribute.BOLD, FColor.CYAN, BColor.NONE);
					}
				} else if (6 <= i && i < 12) {
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

		cp.clear();

		System.out.println();
	}

}
