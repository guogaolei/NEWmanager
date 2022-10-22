package com.ghl.manage.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String getCurrentMonthStartDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()).replace("-", "");
	}
	public static String getCurrentMonthEndDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()).replace("-", "");
	}

	public static String  getTime(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
	}
	
	public static void main(String[] args) {
		System.out.println(getCurrentMonthStartDay());
		System.out.println(getCurrentMonthEndDay());
		System.out.println(getTime());
	}
}
