package com.github.codetanzania.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LocalTimeUtils {

    private static final String FMT_FULL_DATE_TIME  = "yyyy-MM-dd HH:mm:ss";
    private static final String FMT_SHORT_DATE_TIME = "MMM dd HH:mm";

    public static final String FMT_HH_MM_SS = "HH:mm:ss";
    public static final String FMT_MM_SS    = "HH:mm:ss";

    private static String formatDate(
            @NonNull Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(d);
    }

    public static String formatShortDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        return formatDate(cal.getTime(), FMT_SHORT_DATE_TIME);
    }

    public static String formatShortDate(@NonNull Date date) {
        return formatDate(date, FMT_SHORT_DATE_TIME);
    }

    public static String formatCountDown(long time, @NonNull String fmt) {
        if (fmt.equals(FMT_MM_SS)) {
            int minutes = (int) (time / (60 * 1000));
            int seconds = (int) ((time / 1000) % 60);
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
        throw new UnsupportedOperationException("The format " + fmt + "is not supported.");
    }
}
