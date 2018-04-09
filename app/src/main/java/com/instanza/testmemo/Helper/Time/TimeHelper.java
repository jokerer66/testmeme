package com.instanza.testmemo.Helper.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 2017/10/9.
 */

public class TimeHelper {
    public static String getcurrentTime(){
        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        return curDate.toString();
    }
}
