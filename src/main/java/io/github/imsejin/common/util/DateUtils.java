package io.github.imsejin.common.util;

import static java.time.format.DateTimeFormatter.*;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import lombok.experimental.UtilityClass;

/**
 * 날짜 유틸리티<br>
 * Date utilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
@UtilityClass
public class DateUtils {

    /** 날짜 유형 */
    public enum DateType {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    }

    /**
     * 날짜 유형을 문자열로 변환한다.
     */
    private String convertTypeToPattern(DateType type) {
        String pattern;

        if (type == DateType.YEAR) pattern = "yyyy";
        else if (type == DateType.MONTH) pattern = "MM";
        else if (type == DateType.DAY) pattern = "dd";
        else if (type == DateType.HOUR) pattern = "HH";
        else if (type == DateType.MINUTE) pattern = "mm";
        else if (type == DateType.SECOND) pattern = "ss";
        else pattern = "yyyyMMdd";

        return pattern;
    }

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 년, 월, 일을 증감한다. 년, 월, 일은 가감할 수를
	 * 의미하며, 음수를 입력할 경우 감한다.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.addYearMonthDay("19810828", 0, 0, 19)  = "19810916"
	 * DateUtil.addYearMonthDay("20060228", 0, 0, -10) = "20060218"
	 * DateUtil.addYearMonthDay("20060228", 0, 0, 10)  = "20060310"
	 * DateUtil.addYearMonthDay("20060228", 0, 0, 32)  = "20060401"
	 * DateUtil.addYearMonthDay("20050331", 0, -1, 0)  = "20050228"
	 * DateUtil.addYearMonthDay("20050301", 0, 2, 30)  = "20050531"
	 * DateUtil.addYearMonthDay("20050301", 1, 2, 30)  = "20060531"
	 * DateUtil.addYearMonthDay("20040301", 2, 0, 0)   = "20060301"
	 * DateUtil.addYearMonthDay("20040229", 2, 0, 0)   = "20060228"
	 * DateUtil.addYearMonthDay("20040229", 2, 0, 1)   = "20060301"
	 * </pre>
	 * 
	 * @param dateStr
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param year
	 *            가감할 년. 0이 입력될 경우 가감이 없다
	 * @param month
	 *            가감할 월. 0이 입력될 경우 가감이 없다
	 * @param day
	 *            가감할 일. 0이 입력될 경우 가감이 없다
	 * @return yyyyMMdd 형식의 날짜 문자열
	 * @throws IllegalArgumentException
	 *             날짜 포맷이 정해진 바와 다를 경우. 입력 값이 <code>null</code>인 경우.
	 */
    public String addYearMonthDay(String sDate, int year, int month, int day) {

		String dateStr = validChkDate(sDate);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",
				Locale.getDefault());
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ dateStr);
		}

		if (year != 0)
			cal.add(Calendar.YEAR, year);
		if (month != 0)
			cal.add(Calendar.MONTH, month);
		if (day != 0)
			cal.add(Calendar.DATE, day);
		return sdf.format(cal.getTime());
	}

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 년을 증감한다. <code>year</code>는 가감할
	 * 수를 의미하며, 음수를 입력할 경우 감한다.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.addYear("20000201", 62)  = "20620201"
	 * DateUtil.addYear("20620201", -62) = "20000201"
	 * DateUtil.addYear("20040229", 2)   = "20060228"
	 * DateUtil.addYear("20060228", -2)  = "20040228"
	 * DateUtil.addYear("19000101", 200) = "21000101"
	 * </pre>
	 * 
	 * @param dateStr
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param year
	 *            가감할 년. 0이 입력될 경우 가감이 없다
	 * @return yyyyMMdd 형식의 날짜 문자열
	 * @throws IllegalArgumentException
	 *             날짜 포맷이 정해진 바와 다를 경우. 입력 값이 <code>null</code>인 경우.
	 */
	public String addYear(String dateStr, int year) {
		return addYearMonthDay(dateStr, year, 0, 0);
	}

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 월을 증감한다. <code>month</code>는 가감할
	 * 수를 의미하며, 음수를 입력할 경우 감한다.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.addMonth("20010201", 12)  = "20020201"
	 * DateUtil.addMonth("19800229", 12)  = "19810228"
	 * DateUtil.addMonth("20040229", 12)  = "20050228"
	 * DateUtil.addMonth("20050228", -12) = "20040228"
	 * DateUtil.addMonth("20060131", 1)   = "20060228"
	 * DateUtil.addMonth("20060228", -1)  = "20060128"
	 * </pre>
	 * 
	 * @param dateStr
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param month
	 *            가감할 월. 0이 입력될 경우 가감이 없다
	 * @return yyyyMMdd 형식의 날짜 문자열
	 * @throws IllegalArgumentException
	 *             날짜 포맷이 정해진 바와 다를 경우. 입력 값이 <code>null</code>인 경우.
	 */
	public String addMonth(String dateStr, int month) {
		return addYearMonthDay(dateStr, 0, month, 0);
	}

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 일(day)를 증감한다. <code>day</code>는
	 * 가감할 수를 의미하며, 음수를 입력할 경우 감한다. <br/>
	 * <br/>
	 * 위에 정의된 addDays 메서드는 사용자가 ParseException을 반드시 처리해야 하는 불편함이 있기 때문에 추가된
	 * 메서드이다.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.addDay("19991201", 62) = "20000201"
	 * DateUtil.addDay("20000201", -62) = "19991201"
	 * DateUtil.addDay("20050831", 3) = "20050903"
	 * DateUtil.addDay("20050831", 3) = "20050903"
	 * // 2006년 6월 31일은 실제로 존재하지 않는 날짜이다 -> 20060701로 간주된다
	 * DateUtil.addDay("20060631", 1) = "20060702"
	 * </pre>
	 * 
	 * @param dateStr
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param day
	 *            가감할 일. 0이 입력될 경우 가감이 없다
	 * @return yyyyMMdd 형식의 날짜 문자열
	 * @throws IllegalArgumentException
	 *             날짜 포맷이 정해진 바와 다를 경우. 입력 값이 <code>null</code>인 경우.
	 */
	public String addDay(String dateStr, int day) {
		return addYearMonthDay(dateStr, 0, 0, day);
	}

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열 <code>dateStr1</code>과 <code>
	 * dateStr2</code> 사이의 일 수를 구한다.<br>
	 * <code>dateStr2</code>가 <code>dateStr1</code> 보다 과거 날짜일 경우에는 음수를 반환한다. 동일한
	 * 경우에는 0을 반환한다.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.getDaysDiff("20060228","20060310") = 10
	 * DateUtil.getDaysDiff("20060101","20070101") = 365
	 * DateUtil.getDaysDiff("19990228","19990131") = -28
	 * DateUtil.getDaysDiff("20060801","20060802") = 1
	 * DateUtil.getDaysDiff("20060801","20060801") = 0
	 * </pre>
	 * 
	 * @param sDate1
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param sDate2
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @return 일 수 차이.
	 * @throws IllegalArgumentException
	 *             날짜 포맷이 정해진 바와 다를 경우. 입력 값이 <code>null</code>인 경우.
	 */
	public int getDaysDiff(String sDate1, String sDate2) {
		String dateStr1 = validChkDate(sDate1);
		String dateStr2 = validChkDate(sDate2);

		if (!checkDate(sDate1) || !checkDate(sDate2)) {
			throw new IllegalArgumentException("Invalid date format: args[0]="
					+ sDate1 + " args[1]=" + sDate2);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",
				Locale.getDefault());

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(dateStr1);
			date2 = sdf.parse(dateStr2);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: args[0]="
					+ dateStr1 + " args[1]=" + dateStr2);
		}
		int days1 = (int) ((date1.getTime() / 3600000) / 24);
		int days2 = (int) ((date2.getTime() / 3600000) / 24);

		return days2 - days1;
	}

	/**
	 * <p>
	 * yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 유효한 날짜인지 검사.
	 * </p>
	 * 
	 * <pre>
	 * DateUtil.checkDate("1999-02-35") = false
	 * DateUtil.checkDate("2000-13-31") = false
	 * DateUtil.checkDate("2006-11-31") = false
	 * DateUtil.checkDate("2006-2-28")  = false
	 * DateUtil.checkDate("2006-2-8")   = false
	 * DateUtil.checkDate("20060228")   = true
	 * DateUtil.checkDate("2006-02-28") = true
	 * </pre>
	 * 
	 * @param sDate
	 *            날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @return 유효한 날짜인지 여부
	 */
	public boolean checkDate(String sDate) {
		String dateStr = validChkDate(sDate);

		String year = dateStr.substring(0, 4);
		String month = dateStr.substring(4, 6);
		String day = dateStr.substring(6);

		return checkDate(year, month, day);
	}

	/**
	 * <p>
	 * 입력한 년, 월, 일이 유효한지 검사.
	 * </p>
	 * 
	 * @param year
	 *            연도
	 * @param month
	 *            월
	 * @param day
	 *            일
	 * @return 유효한 날짜인지 여부
	 */
	public boolean checkDate(String year, String month, String day) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd",
					Locale.getDefault());

			Date result = formatter.parse(year + "." + month + "." + day);
			String resultStr = formatter.format(result);
			if (resultStr.equalsIgnoreCase(year + "." + month + "." + day))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 날짜형태의 String의 날짜 포맷 및 TimeZone을 변경해 주는 메서드
	 * 
	 * @param strSource
	 *            바꿀 날짜 String
	 * @param fromDateFormat
	 *            기존의 날짜 형태
	 * @param toDateFormat
	 *            원하는 날짜 형태
	 * @param strTimeZone
	 *            변경할 TimeZone(""이면 변경 안함)
	 * @return 소스 String의 날짜 포맷을 변경한 String
	 */
	public String convertDate(String strSource, String fromDateFormat,
			String toDateFormat, String strTimeZone) {
		SimpleDateFormat simpledateformat = null;
		Date date = null;
		String _fromDateFormat = "";
		String _toDateFormat = "";

		if (StringUtils.isNullToString(strSource).trim().equals("")) {
			return "";
		}
		if (StringUtils.isNullToString(fromDateFormat).trim().equals(""))
			_fromDateFormat = "yyyyMMddHHmmss"; // default값
		if (StringUtils.isNullToString(toDateFormat).trim().equals(""))
			_toDateFormat = "yyyy-MM-dd HH:mm:ss"; // default값

		try {
			simpledateformat = new SimpleDateFormat(_fromDateFormat,
					Locale.getDefault());
			date = simpledateformat.parse(strSource);
			if (!StringUtils.isNullToString(strTimeZone).trim().equals("")) {
				simpledateformat.setTimeZone(TimeZone.getTimeZone(strTimeZone));
			}
			simpledateformat = new SimpleDateFormat(_toDateFormat,
					Locale.getDefault());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}

		return simpledateformat.format(date);
	}

	/**
	 * yyyyMMdd 형식의 날짜문자열을 원하는 캐릭터(ch)로 쪼개 돌려준다<br/>
	 * 
	 * <pre>
	 * ex) 20030405, ch(.) -> 2003.04.05
	 * ex) 200304, ch(.) -> 2003.04
	 * ex) 20040101,ch(/) --> 2004/01/01 로 리턴
	 * </pre>
	 * 
	 * @param sDate
	 *            yyyyMMdd 형식의 날짜문자열
	 * @param ch
	 *            구분자
	 * @return 변환된 문자열
	 */
	public String formatDate(String sDate, String ch) {
		String dateStr = validChkDate(sDate);

		String str = dateStr.trim();
		String yyyy = "";
		String mm = "";
		String dd = "";

		if (str.length() == 8) {
			yyyy = str.substring(0, 4);
			if (yyyy.equals("0000"))
				return "";

			mm = str.substring(4, 6);
			if (mm.equals("00"))
				return yyyy;

			dd = str.substring(6, 8);
			if (dd.equals("00"))
				return yyyy + ch + mm;

			return yyyy + ch + mm + ch + dd;
		} else if (str.length() == 6) {
			yyyy = str.substring(0, 4);
			if (yyyy.equals("0000"))
				return "";

			mm = str.substring(4, 6);
			if (mm.equals("00"))
				return yyyy;

			return yyyy + ch + mm;
		} else if (str.length() == 4) {
			yyyy = str.substring(0, 4);
			if (yyyy.equals("0000"))
				return "";
			else
				return yyyy;
		} else
			return "";
	}

	/**
	 * HH24MISS 형식의 시간문자열을 원하는 캐릭터(ch)로 쪼개 돌려준다 <br>
	 * 
	 * <pre>
	 *     ex) 151241, ch(/) -> 15/12/31
	 * </pre>
	 * 
	 * @param sTime
	 *            HH24MISS 형식의 시간문자열
	 * @param ch
	 *            구분자
	 * @return 변환된 문자열
	 */
	public String formatTime(String sTime, String ch) {
		String timeStr = validChkTime(sTime);
		return timeStr.substring(0, 2) + ch + timeStr.substring(2, 4) + ch
				+ timeStr.substring(4, 6);
	}

    /**
     * 입력받은 연도가 윤년인지 아닌지 검사한다.
     */
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? true : false;
    }

	/**
	 * 현재(한국기준) 날짜정보를 얻는다. <BR>
	 * 표기법은 yyyy-mm-dd <BR>
	 * 
	 * @return String yyyymmdd형태의 현재 한국시간. <BR>
	 */
	public String getCurrentDate(String dateType) {
		Calendar aCalendar = Calendar.getInstance();

		int year = aCalendar.get(Calendar.YEAR);
		int month = aCalendar.get(Calendar.MONTH) + 1;
		int date = aCalendar.get(Calendar.DATE);
		String strDate = Integer.toString(year)
				+ ((month < 10) ? "0" + Integer.toString(month) : Integer
						.toString(month))
				+ ((date < 10) ? "0" + Integer.toString(date) : Integer
						.toString(date));

		if (!"".equals(dateType))
			strDate = convertDate(strDate, "yyyyMMdd", dateType);

		return strDate;
	}

	/**
	 * 날짜형태의 String의 날짜 포맷만을 변경해 주는 메서드
	 * 
	 * @param sDate
	 *            날짜
	 * @param sTime
	 *            시간
	 * @param sFormatStr
	 *            포멧 스트링 문자열
	 * @return 지정한 날짜/시간을 지정한 포맷으로 출력
	 * @See Letter Date or Time Component Presentation Examples G Era designator
	 *      Text AD y Year Year 1996; 96 M Month in year Month July; Jul; 07 w
	 *      Week in year Number 27 W Week in month Number 2 D Day in year Number
	 *      189 d Day in month Number 10 F Day of week in month Number 2 E Day
	 *      in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in day
	 *      (0-23) Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm
	 *      (0-11) Number 0 h Hour in am/pm (1-12) Number 12 m Minute in hour
	 *      Number 30 s Second in minute Number 55 S Millisecond Number 978 z
	 *      Time zone General time zone Pacific Standard Time; PST; GMT-08:00 Z
	 *      Time zone RFC 822 time zone -0800
	 * 
	 * 
	 * 
	 *      Date and Time Pattern Result "yyyy.MM.dd G 'at' HH:mm:ss z"
	 *      2001.07.04 AD at 12:08:56 PDT "EEE, MMM d, ''yy" Wed, Jul 4, '01
	 *      "h:mm a" 12:08 PM "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific
	 *      Daylight Time "K:mm a, z" 0:08 PM, PDT
	 *      "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
	 *      "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
	 *      "yyMMddHHmmssZ" 010704120856-0700
	 */
	public String convertDate(String sDate, String sTime,
			String sFormatStr) {
		String dateStr = validChkDate(sDate);
		String timeStr = validChkTime(sTime);

		Calendar cal = null;
		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(dateStr.substring(6, 8)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));

		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.ENGLISH);

		return sdf.format(cal.getTime());
	}

	/**
	 * 입력받은 일자 사이의 임의의 일자를 반환
	 * 
	 * @param sDate1
	 *            시작일자
	 * @param sDate2
	 *            종료일자
	 * @return 임의일자
	 */
	public String getRandomDate(String sDate1, String sDate2) {
		String dateStr1 = validChkDate(sDate1);
		String dateStr2 = validChkDate(sDate2);

		String randomDate = null;

		int sYear, sMonth, sDay;
		int eYear, eMonth, eDay;

		sYear = Integer.parseInt(dateStr1.substring(0, 4));
		sMonth = Integer.parseInt(dateStr1.substring(4, 6));
		sDay = Integer.parseInt(dateStr1.substring(6, 8));

		eYear = Integer.parseInt(dateStr2.substring(0, 4));
		eMonth = Integer.parseInt(dateStr2.substring(4, 6));
		eDay = Integer.parseInt(dateStr2.substring(6, 8));

		GregorianCalendar beginDate = new GregorianCalendar(sYear, sMonth - 1,
				sDay, 0, 0);
		GregorianCalendar endDate = new GregorianCalendar(eYear, eMonth - 1,
				eDay, 23, 59);

		if (endDate.getTimeInMillis() < beginDate.getTimeInMillis()) {
			throw new IllegalArgumentException("Invalid input date : " + sDate1
					+ "~" + sDate2);
		}

		SecureRandom r = new SecureRandom();

		long rand = ((r.nextLong() >>> 1) % (endDate.getTimeInMillis()
				- beginDate.getTimeInMillis() + 1))
				+ beginDate.getTimeInMillis();

		GregorianCalendar cal = new GregorianCalendar();
		// SimpleDateFormat calformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat calformat = new SimpleDateFormat("yyyyMMdd",
				Locale.ENGLISH);
		cal.setTimeInMillis(rand);
		randomDate = calformat.format(cal.getTime());

		// 랜덤문자열를 리턴
		return randomDate;
	}

	/**
	 * 입력받은 요일의 영문명을 국문명의 요일로 반환
	 * 
	 * @param sWeek
	 *            영문 요일명
	 * @return 국문 요일명
	 */
	public String convertWeek(String sWeek) {
		String retStr = null;

		if (sWeek.equals("SUN")) {
			retStr = "일요일";
		} else if (sWeek.equals("MON")) {
			retStr = "월요일";
		} else if (sWeek.equals("TUE")) {
			retStr = "화요일";
		} else if (sWeek.equals("WED")) {
			retStr = "수요일";
		} else if (sWeek.equals("THR")) {
			retStr = "목요일";
		} else if (sWeek.equals("FRI")) {
			retStr = "금요일";
		} else if (sWeek.equals("SAT")) {
			retStr = "토요일";
		}

		return retStr;
	}

	/**
	 * 입력일자의 유효 여부를 확인
	 * 
	 * @param sDate
	 *            일자
	 * @return 유효 여부
	 */
	public boolean validDate(String sDate) {
		String dateStr = validChkDate(sDate);

		Calendar cal;
		boolean ret = false;

		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(dateStr.substring(6, 8)));

		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		String pad4Str = "0000";
		String pad2Str = "00";

		String retYear = (pad4Str + year).substring(year.length());
		String retMonth = (pad2Str + month).substring(month.length());
		String retDay = (pad2Str + day).substring(day.length());

		String retYMD = retYear + retMonth + retDay;

		if (sDate.equals(retYMD)) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 입력일자, 요일의 유효 여부를 확인
	 * 
	 * @param sDate
	 *            일자
	 * @param sWeek
	 *            요일 (DAY_OF_WEEK)
	 * @return 유효 여부
	 */
	public boolean validDate(String sDate, int sWeek) {
		String dateStr = validChkDate(sDate);

		Calendar cal;
		boolean ret = false;

		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(dateStr.substring(6, 8)));

		int Week = cal.get(Calendar.DAY_OF_WEEK);

		if (validDate(sDate)) {
			if (sWeek == Week) {
				ret = true;
			}
		}

		return ret;
	}

	/**
	 * 입력시간의 유효 여부를 확인
	 * 
	 * @param sTime
	 *            입력시간
	 * @return 유효 여부
	 */
	public boolean validTime(String sTime) {
		String timeStr = validChkTime(sTime);

		Calendar cal;
		boolean ret = false;

		cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));

		String HH = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String MM = String.valueOf(cal.get(Calendar.MINUTE));

		String pad2Str = "00";

		String retHH = (pad2Str + HH).substring(HH.length());
		String retMM = (pad2Str + MM).substring(MM.length());

		String retTime = retHH + retMM;

		if (sTime.equals(retTime)) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 입력된 일자에 연, 월, 일을 가감한 날짜의 요일을 반환
	 * 
	 * @param sDate
	 *            날짜
	 * @param year
	 *            연
	 * @param month
	 *            월
	 * @param day
	 *            일
	 * @return 계산된 일자의 요일(DAY_OF_WEEK)
	 */
	public String addYMDtoWeek(String sDate, int year, int month, int day) {
		String dateStr = validChkDate(sDate);

		dateStr = addYearMonthDay(dateStr, year, month, day);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ dateStr);
		}

		SimpleDateFormat rsdf = new SimpleDateFormat("E", Locale.ENGLISH);

		return rsdf.format(cal.getTime());
	}

	/**
	 * 입력된 일자에 연, 월, 일, 시간, 분을 가감한 날짜, 시간을 포멧스트링 형식으로 반환
	 * 
	 * @param sDate
	 *            날짜
	 * @param sTime
	 *            시간
	 * @param year
	 *            연
	 * @param month
	 *            월
	 * @param day
	 *            일
	 * @param hour
	 *            시간
	 * @param minute
	 *            분
	 * @param formatStr
	 *            포멧스트링
	 * @return
	 */
	public String addYMDtoDayTime(String sDate, String sTime, int year,
			int month, int day, int hour, int minute, String formatStr) {
		String dateStr = validChkDate(sDate);
		String timeStr = validChkTime(sTime);

		dateStr = addYearMonthDay(dateStr, year, month, day);

		dateStr = convertDate(dateStr, timeStr, "yyyyMMddHHmm");

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm",
				Locale.ENGLISH);

		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ dateStr);
		}

		if (hour != 0) {
			cal.add(Calendar.HOUR, hour);
		}

		if (minute != 0) {
			cal.add(Calendar.MINUTE, minute);
		}

		SimpleDateFormat rsdf = new SimpleDateFormat(formatStr, Locale.ENGLISH);

		return rsdf.format(cal.getTime());
	}

	/**
	 * 입력된 일자를 int 형으로 반환
	 * 
	 * @param sDate
	 *            일자
	 * @return int(일자)
	 */
	public int datetoInt(String sDate) {
		return Integer.parseInt(convertDate(sDate, "0000", "yyyyMMdd"));
	}

	/**
	 * 입력된 시간을 int 형으로 반환
	 * 
	 * @param sTime
	 *            시간
	 * @return int(시간)
	 */
	public int timetoInt(String sTime) {
		return Integer.parseInt(convertDate("00000101", sTime, "HHmm"));
	}

	/**
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 * 
	 * @param dateStr dateStr
	 * @return
	 */
	public String validChkDate(String dateStr) {
		String _dateStr = dateStr;

		if (dateStr == null
				|| !(dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) {
			throw new IllegalArgumentException("Invalid date format: "
					+ dateStr);
		}
		if (dateStr.length() == 10) {
			_dateStr = StringUtils.removeMinusChar(dateStr);
		}
		return _dateStr;
	}

	/**
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 * 
	 * @param timeStr timeStr
	 * @return
	 */
	public String validChkTime(String timeStr) {
		String _timeStr = timeStr;

		if (_timeStr.length() == 5) {
			_timeStr = StringUtils.remove(_timeStr, ':');
		}
		if (_timeStr == null || !(_timeStr.trim().length() == 4)) {
			throw new IllegalArgumentException("Invalid time format: "
					+ _timeStr);
		}

		return _timeStr;
	}
	/**
     * <p>
     * Date -> String
     * </p>.
     *
     * @param date Date which you want to change.
     * @param format the format
     * @param locale the locale
     * @return String The Date string. Type, yyyyMMdd
     * HH:mm:ss.
     */
    public String toString(Date date, String format, Locale locale) {

        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

        String tmp = sdf.format(date);

        return tmp;
    }
    
    public String getDateFromToday(String format, int fromDay){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, fromDay);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    /**
     * 복합날짜열을 변환한다.
     * 
     * <p>
     * e.g. "20190606,20190501~20190531,yesterday~today,20190601~today,today,yesterday,20190101,20181201~20190101"
     * </p>
     * 
     * <pre>
     * {
     *   "simpleDates": [
     *     "20190606",
     *     "20190611",
     *     "20190610",
     *     "20190101"
     *   ],
     *   "complexDates": [
     *     {
     *       "endDate": "20190531",
     *       "strDate": "20190501"
     *     },
     *     {
     *       "endDate": "20190611",
     *       "strDate": "20190610"
     *     },
     *     {
     *       "endDate": "20190611",
     *       "strDate": "20190601"
     *     },
     *     {
     *       "endDate": "20190101",
     *       "strDate": "20181201"
     *     }
     *   ]
     * }
     *</pre>
     */
    public Map<String, Object> convertCompoundedDate(String date) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<String> simpleDates = new ArrayList<>();
        List<Object> complexDates = new ArrayList<>();

        // today와 yesterday 문자열을 날짜 형태로 변환한다
        date = date.replace("today", getToday());
        date = date.replace("yesterday", getYesterday());

        // ,(comma)를 기준으로 날짜 조건을 분리한다
        List<String> temp1 = Arrays.asList(date.split(","));
        List<String> temp2 = new ArrayList<>();
        for (int i = 0; i < temp1.size(); i++) {
            // 복합일자를 분리한다
            if (temp1.get(i).contains("~")) {
                temp2.add(temp1.get(i));
            }
            // 단일일자를 분리한다
            else {
                // 존재하는 날짜인지 확인한다
                if (validDate(temp1.get(i)) == false)
                    throw new RuntimeException("Invalid date: " + temp1.get(i));

                simpleDates.add(temp1.get(i));
            }
        }

        // 복합일자의 시작일과 종료일을 구분한다
        for (int i = 0; i < temp2.size(); i++) {
            String[] temp3 = temp2.get(i).split("~");

            // 존재하는 날짜인지 확인한다
            if (validDate(temp3[0]) == false)
                throw new RuntimeException("Invalid date: " + temp3[0]);
            if (validDate(temp3[1]) == false)
                throw new RuntimeException("Invalid date: " + temp3[1]);

            // 시작일이 종료일보다 크거나 같은지 확인한다
            if (Integer.parseInt(temp3[0]) >= Integer.parseInt(temp3[1]))
                throw new RuntimeException(
                        "Start date precedes end date: " + temp3[0] + "~" + temp3[1] + "; You can change it to this: " + temp3[0] + " or " + temp3[1]);

            Map<String, Object> map = new HashMap<>();
            map.put("strDate", temp3[0]);
            map.put("endDate", temp3[1]);

            complexDates.add(map);
        }

        result.put("simpleDates", simpleDates);
        result.put("complexDates", complexDates);

        return result;
    }

    /**
     * 오늘 날짜(yyyyMMdd)를 반환한다.
     * 
     * <pre>
     * DateUtil.getToday(): "20191231"
     * </pre>
     */
    public String getToday() {
        return LocalDate.now().format(ofPattern("yyyyMMdd"));
    }

    /**
     * 오늘 날짜 중 해당하는 요소를 반환한다.
     * 
     * <pre>
     * DateUtil.getToday(): "20191231"
     * 
     * DateUtil.getToday(DateType.YEAR): "2019"
     * DateUtil.getToday(DateType.MONTH): "12"
     * DateUtil.getToday(DateType.DAY): "31"
     * </pre>
     */
    public String getToday(DateType type) {
        return LocalDateTime.now().format(ofPattern(convertTypeToPattern(type)));
    }

    /**
     * 오늘을 기준으로 어제 날짜(yyyyMMdd)를 반환한다.
     * 
     * <pre>
     * DateUtil.getToday(): "20191231"
     * DateUtil.getYesterday(): "20191230"
     * </pre>
     */
    public String getYesterday() {
        return LocalDate.now().minusDays(1).format(ofPattern("yyyyMMdd"));
    }

    /**
     * 오늘을 기준으로 어제 날짜 중 해당하는 요소를 반환한다.
     * 
     * <pre>
     * DateUtil.getYesterday(): "20191230"
     * 
     * DateUtil.getYesterday(DateType.YEAR): "2019"
     * DateUtil.getYesterday(DateType.MONTH): "12"
     * DateUtil.getYesterday(DateType.DAY): "30"
     * </pre>
     */
    public String getYesterday(DateType type) {
        return LocalDateTime.now().minusDays(1).format(ofPattern(convertTypeToPattern(type)));
    }

    /**
     * 현재의 연월일시분초(yyyyMMddHHmmss)를 반환한다.
     * 
     * <pre>
     * DateUtil.getCurrentDateTime(): "20191231175959"
     * </pre>
     */
    public String getCurrentDateTime() {
        return LocalDateTime.now().format(ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 콘솔 형식의 현재 연월일시분초를 반환한다.
     * 
     * <pre>
     * DateUtil.getConsoleTime(): "2019-12-31 17:59:59.311"
     * </pre>
     */
    public String getConsoleTime() {
        return LocalDateTime.now().format(ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    /**
     * 달력에 실재하는 날짜인지 확인한다.
     * 날짜 형식은 "yyyyMMdd", "yyyy-MM-dd" 둘 다 지원한다.
     * 
     * <pre>
     * DateUtil.isValidDate("2019-02-28"): true
     * DateUtil.isValidDate("20190229"): false
     * DateUtil.isValidDate("20200229"): true
     * DateUtil.isValidDate("2020-02-29"): true
     * </pre>
     */
    public boolean isValidDate(String date) {
        try {
            // 날짜 형식을 통일한다
            date = StringUtils.removeMinusChar(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormat.setLenient(false);
            dateFormat.parse(date);
        } catch (ParseException ex) {
            return false;
        }
        return true;
    }

    /**
     * 달력에 실재하는 날짜와 요일인지 확인한다.
     * 날짜 형식은 "yyyyMMdd", "yyyy-MM-dd" 둘 다 지원한다.
     * 
     * <pre>
     * DateUtil.isValidDate("20190228", DayOfWeek.THURSDAY): true
     * DateUtil.isValidDate("2019-02-28", DayOfWeek.THURSDAY): true
     * DateUtil.isValidDate("20190229", DayOfWeek.FRIDAY): false
     * DateUtil.isValidDate("20200229", DayOfWeek.SATURDAY): true
     * DateUtil.isValidDate("2020-02-29", DayOfWeek.SATURDAY): true
     * </pre>
     */
    public boolean isValidDate(String date, DayOfWeek dayOfWeek) {
        // 유효한 날짜인지 확인한다
        if (!isValidDate(date)) return false;

        final int year = Integer.parseInt(date.substring(0, 4));
        final int month = Integer.parseInt(date.substring(4, 6));
        final int day = Integer.parseInt(date.substring(6));
        LocalDate ld = LocalDate.of(year, month, day);

        // 유효한 요일인지 확인한다
        return ld.getDayOfWeek().equals(dayOfWeek);
    }

    /**
     * 해당 연월의 말일을 포함한 일자를 반환한다.
     * 
     * <pre>
     * DateUtil.getMonthlyLastDate(2019, 2): "20190228"
     * DateUtil.getMonthlyLastDate(2020, 2): "20200229"
     * </pre>
     */
    public String getMonthlyLastDate(int year, int month) {
        LocalDate lastDate = YearMonth.of(year, month).atEndOfMonth();
        return lastDate.format(ofPattern("yyyyMMdd"));
    }

    /**
     * 해당 연월의 말일을 포함한 일자를 반환한다.
     * 
     * <pre>
     * DateUtil.getMonthlyLastDate("2019", "2"): "20190228"
     * DateUtil.getMonthlyLastDate("2020", "2"): "20200229"
     * </pre>
     */
    public String getMonthlyLastDate(String year, String month) {
        LocalDate lastDate = YearMonth.of(Integer.valueOf(year), Integer.valueOf(month)).atEndOfMonth();
        return lastDate.format(ofPattern("yyyyMMdd"));
    }

}