package io.github.imsejin.common;

import java.util.Arrays;
import java.util.List;

/**
 * GeneralUtil
 * 
 * @author SEJIN
 */
public class GeneralUtil {
	
	/**
	 * Converts from list of author to string of author.
	 * 
	 * @param list of author
	 * @return string of author
	 */
	public static String convertAuthor(List<String> author) {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < author.size(); i++) {
			String person = author.get(i);
			result.append(person);
			if (i < author.size() - 1) {
				result.append(Constants.file.DELIMITER_AUTHOR);
			}
		}

		return result.toString();
	}

	/**
	 * Converts from string of author to list of author.
	 * 
	 * @param string of author
	 * @return list of author
	 */
	public static List<String> convertAuthor(String author) {
		return Arrays.asList(author.split(Constants.file.DELIMITER_AUTHOR));
	}

}
