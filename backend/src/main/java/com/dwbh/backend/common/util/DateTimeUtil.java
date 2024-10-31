package com.dwbh.backend.common.util;

import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtil {

    public static LocalDateTime parse(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(date, formatter);
    }

    public static boolean isAfterDate(String nowDate, String targetDate) {
        LocalDateTime nowDate = parse(orgDate, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime targetDate = parse(distDate, "yyyy-MM-dd HH:mm:ss");

        return nowDate.isAfter(targetDate);
    }

    public static boolean isBetweenDate(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormat.format(Calendar.getInstance().getTime());

        return nowDate.compareTo(startDate) >= 0 && nowDate.compareTo(endDate) <= 0;
    }

    /* 오늘이 몇 주 전인지 구함 - week가 2일 경우 오늘이 startDate로부터 2주 전인지 */
    public static boolean isBeforeWeek(String startDate, int week) {
        LocalDateTime startDateParse = parse(startDate, "yyyy-MM-dd HH:mm:ss");

        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime endDate = startDateParse.plusWeeks(week);

        return nowDate.isBefore(endDate);
    }

}