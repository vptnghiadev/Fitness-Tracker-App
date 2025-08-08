package com.example.workout_fitness;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateSummary {

    public static String mDate;

    public static String getDate() {
        Date today = new Date();
        // Locale tiếng Việt, chỉ hiển thị ngày và tháng
        SimpleDateFormat format = new SimpleDateFormat("dd 'tháng' M", new Locale("vi", "VN"));
        mDate = format.format(today);
        return mDate;
    }
}
