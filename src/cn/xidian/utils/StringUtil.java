package cn.xidian.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 字符串工具类
 * 
 * @author wangrui
 * @date 2016-11-17
 */
public class StringUtil {

	/**
	 * 判读字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static Boolean strIsNotEmpty(String s) {
		Boolean flag = true;
		if (s == null || s.length() <= 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 4舍5入保留2位小数
	 * 
	 * @param fl
	 * @return
	 */
	public static Float save2Float(Float fl) {
		Float result = 0.0f;
		if (fl != null) {
			String str = String.format("%.2f", fl);
			result = Float.valueOf(str);
		}
		return result;
	}

	/**
	 * 判断对象为空或者为0
	 * 
	 * @param obj
	 * @return
	 */
	public static Boolean isNullOrZero(Object obj) {
		Boolean flag = false;
		if (obj == null || obj.toString().equals("0")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 相加
	 * 
	 * @param s1
	 * @param s2
	 * @return s1+s2
	 */
	public static String add(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.add(b2).toString();
	}
	/**
	 *多个数 相加
	 * 
	 * @param s1
	 * @param s2
	 * @return s1+s2
	 */
	public static String multiadd(String s1, String s2,String s3) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		BigDecimal b3 = new BigDecimal(s3);
		return b1.add(b2).add(b3).toString();
	}

	/**
	 * 相减
	 * 
	 * @param s1
	 * @param s2
	 * @return s1-s2
	 */
	public static String sub(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.subtract(b2).toString();
	}

	/**
	 * 相除
	 * 
	 * @param s1
	 * @param s2
	 * @return s1/s2
	 */
	public static String divide(String s1, String s2) {
		String str = "0";
		if (!s2.equals("0")&&!s2.equals("0.00")) {
			BigDecimal b1 = new BigDecimal(s1);
			BigDecimal b2 = new BigDecimal(s2);
			str = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).toString();// 四舍五入，保留2位小数
		}
		return str;
	}

	/**
	 * 相乘
	 * 
	 * @param s1
	 * @param s2
	 * @return s1*s2
	 */
	public static String multiply(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.multiply(b2).toString();
	}

	/**
	 * 获取日期当前月份的第一天
	 * 
	 * @param dateStr
	 *            yyyy-MM
	 * @return yyyy-MM-dd 00:00:00
	 */
	public static String monthFirstDay(String dateStr) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(dateStr + "-01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.set(Calendar.DATE, 1);
		str = sdf.format(calendar.getTime()) + " 00:00:00";
		return str;
	}

	/**
	 * 获取日期当前月份的最后一天
	 * 
	 * @param dateStr
	 *            yyyy-MM
	 * @return yyyy-MM-dd 23:59:59
	 */
	public static String monthLastDay(String dateStr) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(dateStr + "-01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		str = sdf.format(calendar.getTime()) + " 23:59:59";
		return str;
	}

	/**
	 * 获取季度第一天
	 * 
	 * @param year
	 *            yyyy
	 * @param quarter
	 *            0-全年,1/2/3/4代表各季度
	 * @return yyyy-MM-dd 00:00:00
	 */
	public static String quarterFirstDay(String year, String quarter) {
		String str = "";
		Integer intQuart = Integer.valueOf(quarter);
		switch (intQuart) {
		case 0:
			str = year + "-01";
			break;
		case 1:
		case 2:
		case 3:
			str = year + "-0" + String.valueOf((intQuart - 1) * 3 + 1);
			break;
		case 4:
			str = year + "-10";
			break;
		default:
			break;
		}
		str = monthFirstDay(str);
		return str;
	}

	/**
	 * 获取季度最后一天
	 * 
	 * @param year
	 *            yyyy
	 * @param quarter
	 *            0-全年,1/2/3/4代表各季度
	 * @return yyyy-MM-dd 23:59:59
	 */
	public static String quarterLastDay(String year, String quarter) {
		String str = "";
		Integer intQuart = Integer.valueOf(quarter);
		switch (intQuart) {
		case 0:
		case 4:
			str = year + "-12";
			break;
		case 1:
		case 2:
		case 3:
			str = year + "-0" + String.valueOf(intQuart * 3);
			break;
		default:
			break;
		}
		str = monthLastDay(str);
		return str;
	}

	/**
	 * 将Float型字符串转换成%形式
	 * 
	 * @param strFloat
	 * @return
	 */
	public static String strFloatToPer(String strFloat) {
		String str = "";
		str = multiply(strFloat, "100") + "%";
		return str;
	}

	/**
	 * 当天0时刻
	 * 
	 * @param str
	 * @return yyyy-MM-dd 00:00:00
	 */
	public static String dayFirstTime(String str) {
		return str + " 00:00:00";
	}

	/**
	 * 当天最后时间
	 * 
	 * @param str
	 * @return yyyy-MM-dd 23:59:59
	 */
	public static String dayLastTime(String str) {
		return str + " 23:59:59";
	}
	/**
	 * 将Float型字符串转换成%形式
	 * 
	 * @param strFloat
	 * @return
	 */
	public static String strfloatToPer(Float strFloat) {
		String str = "";
		str = strFloat*100 + "%";
		return str;
	}
	/**
	 * 当object为null时自动转化为0
	 * 
	 * @param object
	 * @return
	 **/
	public static Object objnull(Object o){
		if(o==null) return 0;
		else return o;
	}

}
