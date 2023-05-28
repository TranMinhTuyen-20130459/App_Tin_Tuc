package com.example.newsapp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Format {

    public static String format(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss")
                    .withLocale(Locale.US)
                    .withZone(ZoneId.of("GMT+7"));
            LocalDateTime localDateTime = LocalDateTime.parse(date.substring(0, date.lastIndexOf(' ')), formatter);
            return getPassedTime(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } catch (Exception e) {
            return date;
        }
    }

    public static String getPassedTime(long since) {
        long elapseTime = System.currentTimeMillis() - since;

        if (elapseTime < 60 * 1000) {
            return "now";
        } else if (elapseTime < 60 * 60 * 1000L) {
            return String.format(Locale.getDefault(), "%d phút trước", elapseTime / (60 * 1000));
        } else if (elapseTime < 24 * 60 * 60 * 1000L) {
            return String.format(Locale.getDefault(), "%d giờ trước", elapseTime / (60 * 60 * 1000));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm", Locale.getDefault());
        return sdf.format(new Date(elapseTime / (24 * 60 * 60 * 1000)));
    }
}
