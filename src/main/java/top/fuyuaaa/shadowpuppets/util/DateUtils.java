package top.fuyuaaa.shadowpuppets.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:59
 */
@Slf4j
public class DateUtils {

    public static Date parseDate(String date){
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            log.error("date transform error, method: parseDate, params: {}", date);
        }
        return new Date();
    }
}
