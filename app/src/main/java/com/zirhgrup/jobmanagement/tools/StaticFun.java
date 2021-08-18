package com.zirhgrup.jobmanagement.tools;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import com.zirhgrup.jobmanagement.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class StaticFun {

    public static boolean validateEmail(String data) {
        return !data.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(data).matches();
    }

    public static String getTimeData(long timestamp) {
        Date date = new Timestamp(timestamp);
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }


}
