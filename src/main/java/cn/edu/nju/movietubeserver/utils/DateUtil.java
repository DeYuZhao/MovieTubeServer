package cn.edu.nju.movietubeserver.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author leolu
 * @create 2019-12-30-18:03
 **/
public class DateUtil
{

    private DateUtil()
    {

    }

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTime()
    {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN, Locale.CHINA));
    }

    public static String formatDateToString(LocalDateTime localDateTime)
    {
        return localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN, Locale.CHINA));
    }

    public static LocalDateTime formatStringToDate(String localTime)
    {
        return LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }
}
