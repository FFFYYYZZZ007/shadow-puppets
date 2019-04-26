package top.fuyuaaa.shadowpuppets.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:59
 */
@Slf4j
public class DateUtils {

    public final static String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

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
}
