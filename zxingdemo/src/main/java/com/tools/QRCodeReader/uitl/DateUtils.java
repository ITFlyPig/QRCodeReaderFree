package com.tools.QRCodeReader.uitl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${张志珍} on 2016/12/27
 * Project_NameDemoMusic
 * Package_Namecom.example.amd.demomusic.utils
 * 17:22.
 */

public class DateUtils {

    //将字符串转为时间戳

    public static long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();

        try{
            date = sf.parse(time);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}