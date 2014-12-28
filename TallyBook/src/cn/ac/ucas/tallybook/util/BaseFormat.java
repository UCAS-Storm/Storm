package cn.ac.ucas.tallybook.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFormat {
	
	
	/**
	 * 字符串转换成日期
	 * @param dateStr
	 * @return
	 */
	public static Date String2Date(String str) {
		
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 日期转换成字符串
	 * @param date
	 * @return
	 */
	public static String Date2String(Date date) {
		
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
	    String str = sbf.format(date);
	    return str;
	}
}
