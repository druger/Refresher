package com.druger.refresher.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by druger on 15.09.2015.
 */
public class DateHelper {

    public static String getDate(long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getTime(long time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(time);
    }

    public static String getFullDate(long date){
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        return fullDateFormat.format(date);
    }
}
