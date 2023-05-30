package com.example.newsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Format {

    public static String formartDate(String inputDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        String outputDate = "";
        try {
            Date currentDate = new Date();
            Date date = inputDateFormat.parse(inputDate.toString());

            long timeDifferenceMillis = currentDate.getTime() - date.getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
            long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);
            long months = days / 30;

            if (months > 0) {
                outputDate = months + " tháng trước";
            } else if (days > 0) {
                outputDate = days + " ngày trước";
            } else if (hours > 0) {
                outputDate = hours + " giờ trước";
            } else {
                outputDate = minutes + " phút trước";
            }

            return outputDate;
        } catch (Exception e) {
            System.out.println("Invalid date format");
            return inputDate;
        }
    }


}
