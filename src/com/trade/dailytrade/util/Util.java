package com.trade.dailytrade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.trade.dailytrade.common.Constants;

public class Util {
	public static Date getDate(String date) throws ParseException{
		SimpleDateFormat dateFormat=new SimpleDateFormat(Constants.DATE_FORMAT);
		return dateFormat.parse(date);
	}
	public static String getDate(Date date) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(Constants.DATE_FORMAT);
		return dateFormat.format(date);
	}
	public static Date getSettlementDate(String currency, Date settlementDate) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(settlementDate);
		if(currency.equals("AED")||currency.equals("SAR")) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek>5) {
				int days=7-cal.get(Calendar.DAY_OF_WEEK);
				cal.add(Calendar.DATE, days+1);
			}
		}else {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			int days=0;
			if(dayOfWeek<2) {
				days=1;
			}else if(dayOfWeek>=7) {
				days=2;
			}
			cal.add(Calendar.DATE, days);
		}
		return cal.getTime();
	}
	public static Boolean isTodaySettlementDay(Date settlementDate) {
		Boolean status=false;
		String date1 = getDate(settlementDate);
		String date2 = getDate(new Date());	
		if (date1.equals(date2)) {
			status=true;
		}
		return status;
	}
	public static Date getTodaysDate() throws ParseException{
		SimpleDateFormat dateFormat=new SimpleDateFormat(Constants.DATE_FORMAT);
		return dateFormat.parse(dateFormat.format(new Date()));
	}

}
