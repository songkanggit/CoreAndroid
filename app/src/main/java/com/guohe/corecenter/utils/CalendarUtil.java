package com.guohe.corecenter.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kousou on 2018/9/30.
 */

public class CalendarUtil {
    public static void main(String[] args) {
        int currentMaxDays = getMaxDayOfCurrentMonth();
        int maxDaysByDate = getMaxDayOfSpecialMonthYear(2012, 11);
        String week = getWeekOfSpecialDate("2012-12-25");
        System.out.println("本月天数：" + currentMaxDays);
        System.out.println("2012年11月天数：" + maxDaysByDate);
        System.out.println("2012-12-25是：" + week);
    }



    /**
     * 获取当月的 天数
     * */

    public static int getMaxDayOfCurrentMonth() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */

    public static int getMaxDayOfSpecialMonthYear(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期
     */

    public static String getWeekOfSpecialDate(String date) {

        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;
        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }
}
