package com.xdq.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <b><code>TimeUtils</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 17:23.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
public class TimeUtils {

    //将时间字符串转成毫秒级时间戳
    public  static Long getTimeStamp(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long timeStamp = calendar.getTime().getTime();

        return timeStamp;
    }

    //获取两时间相减得到相差多少分钟
    public static Long phaseMinute(String endTime,String beginTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(endTime);
            Date d2 = sdf.parse(beginTime);
            return (d1.getTime() - d2.getTime())/60000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Long(-1);
    }

    //时间大小比较 time1比time2小，返回true
    public static Boolean timeCompare(String time1,String time2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(time1);
            Date d2 = sdf.parse(time2);
            if(d1.getTime()<d2.getTime()){
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
