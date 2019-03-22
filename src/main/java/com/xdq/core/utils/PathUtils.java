package com.xdq.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <b><code>PathUtils</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 17:25.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
public class PathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathUtils.class);

    //获取当前五分钟快照的路径
    public static String getPath(String path,String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            //e.printStackTrace();
            LOGGER.info("calendar",e);
        }

        return path + "/" + sdf.format(calendar.getTime());
    }

    //获取连续n个五分钟快照的路径,n传负值表示向前取，正值向后取
    public static List<String> getPaths(String path,String time,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            //e.printStackTrace();
            LOGGER.info("calendar",e);
        }

        List<String> paths = new ArrayList<>();

        for(int i=0;i<Math.abs(n);i++){
            calendar.add(Calendar.MINUTE,+5*i*n/Math.abs(n));
            paths.add(path + "/" + sdf.format(calendar.getTime()));
            calendar.add(Calendar.MINUTE,-5*i*n/Math.abs(n));
        }

        return paths;
    }
}
