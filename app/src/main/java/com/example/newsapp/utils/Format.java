package com.example.newsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Format {

    public static String formartDate(String inputDate){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String outputDate = "";
        try {
            Date date = inputDateFormat.parse(inputDate.toString());
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            outputDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            outputDate = outputDateFormat.format(date);
            return outputDate;
        } catch (Exception e) {
            System.out.println("Invalid date format");
            return inputDate;
        }
    }
}
