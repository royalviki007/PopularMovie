package com.example.vikash.masterdetail.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DateUtil {


    public static int getYear(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        cal.setTime(sdf.parse(dateString));
        return cal.get(Calendar.YEAR);
    }

}
