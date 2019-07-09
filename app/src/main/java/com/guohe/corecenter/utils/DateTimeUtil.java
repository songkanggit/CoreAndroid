package com.guohe.corecenter.utils;

import org.jetbrains.annotations.NonNls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by songkang on 2018/3/28.
 */

public class DateTimeUtil {
    /**
     * 一分钟
     */
    public static final long ONE_MINUTE = 60*1000;

    /**
     * 一小时
     */
    public static final long ONE_HOUR = 60*ONE_MINUTE;

    /**
     * 一天
     */
    public static final long ONE_DAY = ONE_HOUR*24;


    public static SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    public static SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy/MM/dd");

    public static SimpleDateFormat SDF_CHARACTOR = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    public static SimpleDateFormat ONLY_TIME = new SimpleDateFormat("HH:mm");

    public static SimpleDateFormat ONLY_DATE_TIME = new SimpleDateFormat("MM月dd日 HH:mm");

    public static Date string2Date(String date, SimpleDateFormat sdf_t) {
        Date d = null;
        try {
            d = sdf_t.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String timeStampToDateString(@NonNls String timeStamp, SimpleDateFormat dateFormate) {
        Date d = new Date(Long.valueOf(timeStamp));
        return dateFormate.format(d);
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static boolean isToday(Date day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCurrentYear(Date day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    public static final String getCharacterMonth(final int month) {
        switch (month) {
            case 0: {
                return "一月";
            }
            case 1: {
                return "二月";
            }
            case 2: {
                return "三月";
            }
            case 3: {
                return "四月";
            }
            case 4: {
                return "五月";
            }
            case 5: {
                return "六月";
            }
            case 6: {
                return "七月";
            }
            case 7: {
                return "八月";
            }
            case 8: {
                return "九月";
            }
            case 9: {
                return "十月";
            }
            case 10: {
                return "十一月";
            }
            case 11: {
                return "十二月";
            }
        }
        return "";
    }
}

