package top.fuyuaaa.shadowpuppets.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:59
 */
@Slf4j
@SuppressWarnings("unused")
public class DateUtils {

    private final static String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, FORMAT_STR);
    }

    public static Date parseDate(String date){
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, FORMAT_STR);
        } catch (ParseException e) {
            log.error("date transform error, method: parseDate, params: {}", date);
        }
        return new Date();
    }

    /**
     * @param day 1明天/-1昨天/0今天
     * @return
     */
    public static Date get(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 8);
        calendar.add(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
