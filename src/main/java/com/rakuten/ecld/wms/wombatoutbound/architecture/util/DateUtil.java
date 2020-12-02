package com.rakuten.ecld.wms.wombatoutbound.architecture.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Timestamp convertString2Timestamp(String source, String pattern) {
        if (source == null || pattern == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(true);
        Date date;
        try {
            date = sdf.parse(source);
        } catch (ParseException e) {
            return null;
        }
        return new Timestamp(date.getTime());
    }
}
