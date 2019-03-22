package com.xdq.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
