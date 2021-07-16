package com.zirhgrup.jobmanagement.tools;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import com.zirhgrup.jobmanagement.R;

public class StaticFun{

    public static boolean validateEmail(String data){
        return !data.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(data).matches();
    }

}
