package io.github.imsejin.common.util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 문자열 유틸리티<br>
 * String utilities
 *
 * <p>
 *
 * </p>
 *
 * @author SEJIN
 */
public final class StringUtils {

    private StringUtils() {}

    private static final char WHITE_SPACE = '\u0020';

    /**
     * Null2void.
     *
     * @param str the str
     * @return the string
     */
    public static String null2void(String str) {
        return isNullOrBlank(str) ? "" : str;
    }

    /**
     * Null2string.
     *
     * @param str the str
     * @param defaultValue the default value
     * @return the string
     */
    public static String ifBlank(String str, String defaultValue) {
        return isNullOrBlank(str) ? defaultValue : str;
    }

    public static String padStart(int len, String origin) {
        if (origin.length() >= len) return origin;

        StringBuilder builder = new StringBuilder();
        return builder.append(String.valueOf(WHITE_SPACE).repeat(len))
                .append(origin)
                .toString();
    }

    public static String padStart(int len, String origin, String appendix) {
        if (origin.length() >= len) return origin;

        StringBuilder builder = new StringBuilder();
        return builder.append(String.valueOf(appendix).repeat(len))
                .append(origin)
                .toString();
    }

    public static String padEnd(int len, String origin) {
        if (origin.length() >= len) return origin;

        StringBuilder builder = new StringBuilder();
        return builder.append(origin)
                .append(String.valueOf(WHITE_SPACE).repeat(len))
                .toString();
    }

    public static String padEnd(int len, String origin, String appendix) {
        if (origin.length() >= len) return origin;

        StringBuilder builder = new StringBuilder();
        return builder.append(origin)
                .append(String.valueOf(appendix).repeat(len))
                .toString();
    }

    /**
     * Count of.
     *
     * @param str the str
     * @param charToFind the char to find
     * @return the int
     */
    public static int countOf(String str, String charToFind) {
        int findLength = charToFind.length();
        int count = 0;

        for (int idx = str.indexOf(charToFind); idx >= 0; idx = str.indexOf(charToFind, idx + findLength)) {
            count++;
        }

        return count;
    }

    /**
     * If string is null or empty string, return true. <br>
     * If not, return false.
     *
     * <pre>
     * StringUtils.isNullOrEmpty('') 		= true
     * StringUtils.isNullOrEmpty(null) 	= true
     * StringUtils.isNullOrEmpty('abc') 	= false
     * </pre>
     *
     * @param str original String
     * @return which empty string or not.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * <p>
     * Reverses a String as per.
     *
     * @param str the String to reverse, may be null
     * @return the reversed String, <code>null</code> if null String input
     *         {@link StringBuffer#reverse()}.
     *         </p>
     *         <p>
     *         <A code>null</code> String returns <code>null</code>.
     *         </p>
     *
     *         <pre>
     * StringUtils.reverse(null)  		   = null
     * StringUtils.reverse(&quot;&quot;)    = &quot;&quot;
     * StringUtils.reverse(&quot;bat&quot;) = &quot;tab&quot;
     *         </pre>
     */
    public static String reverse(String str) {
        if (str == null) return null;
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * 가장 마지막에 일치하는 문구를 원하는 문구로 대체한다.
     *
     * <pre>
     * StringUtils.replaceLast("ABC%DEF%GHI", "%", "-"): "ABC%DEF-GHI"
     * StringUtils.replaceLast("ABC%DEF%GHI", "%", "\\$"): "ABC%DEF$GHI"
     * </pre>
     */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * 공백 문자열인지 확인한다.
     *
     * <pre>
     * StringUtils.isNullOrBlank(null): true
     * StringUtils.isNullOrBlank(""): true
     * StringUtils.isNullOrBlank(" "): true
     * StringUtils.isNullOrBlank(" ABC"): false
     * </pre>
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 공백 문자열이 하나라도 있는지 확인한다.
     *
     * <pre>
     * StringUtils.anyNullOrBlank(null, " "): true
     * StringUtils.anyNullOrBlank(null, "ABC"): true
     * StringUtils.anyNullOrBlank("ABC", ""): true
     * StringUtils.anyNullOrBlank(" ", "ABC"): true
     * StringUtils.anyNullOrBlank(" ABC", "ABC"): false
     * </pre>
     */
    public static boolean anyNullOrBlank(String... strs) {
        // `new String[] {}`이 파라미터로 넘어 왔을 때
        if (strs == null || strs.length == 0) return true;

        for (var str : strs) {
            if (isNullOrBlank(str)) return true;
        }

        return false;
    }

    /**
     * 모두 공백 문자열인지 확인한다.
     *
     * <pre>
     * StringUtils.allNullOrBlank(null, " "): true
     * StringUtils.allNullOrBlank(null, "ABC"): false
     * StringUtils.allNullOrBlank("ABC", ""): false
     * StringUtils.allNullOrBlank(" ", "ABC"): false
     * StringUtils.allNullOrBlank(" ABC", "ABC"): false
     * </pre>
     */
    public static boolean allNullOrBlank(String... strs) {
        // `new String[] {}`이 파라미터로 넘어 왔을 때
        if (strs == null || strs.length == 0) return true;

        return Stream.of(strs).allMatch(StringUtils::isNullOrBlank);
    }

    /**
     * `기준 문자열`과 일치하는 문자열이 하나라도 있는지 확인한다.
     *
     * <pre>
     * StringUtils.anyEquals(null, null): false
     * StringUtils.anyEquals("", null): false
     * StringUtils.anyEquals(null, ""): false
     * StringUtils.anyEquals("", null, ""): true
     * StringUtils.anyEquals("ABC", "abc"): false
     * StringUtils.anyEquals("ABC", "abc", "ABC"): true
     * </pre>
     */
    public static boolean anyEquals(String s1, String... strs) {
        // `new String[] {}`이 파라미터로 넘어 왔을 때
        if (s1 == null || strs == null || strs.length == 0) return false;

        for (var str : strs) {
            if (s1.equals(str)) return true;
        }

        return false;
    }

    /**
     * 3자리 숫자마다 ,(comma)로 구분한 문자열을 반환한다.
     *
     * <pre>
     * StringUtils.formatComma(""): "0"
     * StringUtils.formatComma("-100"): "-100"
     * StringUtils.formatComma("100000"): "100,000"
     * </pre>
     */
    public static String formatComma(String amount) {
        return new DecimalFormat("###,###,###,###,###,###,###").format(amount);
    }

    /**
     * 3자리 숫자마다 ,(comma)로 구분한 문자열을 반환한다.
     *
     * <pre>
     * StringUtils.formatComma(0): "0"
     * StringUtils.formatComma(-100): "-100"
     * StringUtils.formatComma(100000): "100,000"
     * </pre>
     */
    public static String formatComma(long amount) {
        return new DecimalFormat("###,###,###,###,###,###,###").format(amount);
    }

    /**
     * `해당 문자열`을 원하는 만큼 반복하여 복제한다.
     *
     * <pre>
     * StringUtils.repeat(null, 2): "nullnull"
     * StringUtils.repeat("", 5): ""
     * StringUtils.repeat("abc", 3): "abcabcabc"
     * </pre>
     */
    public static String repeat(String str, int cnt) {
        return String.join("", Collections.nCopies(cnt, str));
    }

    public static Boolean string2boolean(String str) {
        if (anyEquals(str.toLowerCase(), "true", "y", "yes")) return true;
        else if (anyEquals(str.toLowerCase(), "false", "n", "no")) return false;
        else return null;
    }

    public static String match(String regex, String src) {
        return match(regex, src, 0);
    }

    public static String match(String regex, String src, int groupNo) {
        Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(src);

        String matched = null;
        while (matcher.find()) {
            matched = matcher.group(groupNo);
        }

        return matched;
    }

}
