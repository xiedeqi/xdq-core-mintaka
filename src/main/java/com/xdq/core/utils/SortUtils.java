package com.xdq.core.utils;

/**
 * <b><code>SortUtils</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/27 14:11.
 *
 * @author xiedeqi
 * @since nile-cmgdnm-arterial-road-mintaka 0.1.0
 */
public class SortUtils {

    //根据时间从小到大排序->ABC 返回 A,B及B,C
    public static String[] sortByTime(String msisdn,String str){
        String[] ss = str.split("\\|");

        for(int i=0;i<ss.length-1;i++){
            for(int j=0;j<ss.length-1-i;j++){
                if(TimeUtils.timeCompare(ss[j+1].split(",")[1],ss[j].split(",")[1])){
                    String s = ss[j+1];
                    ss[j+1] = ss[j];
                    ss[j] = s;
                }
            }
        }

        String[] values = new String[ss.length-1];

        for(int i=0;i<ss.length-1;i++){
            String[] beginValues = ss[i].split(",");
            String[] endValues = ss[i+1].split(",");
            values[i] = msisdn+","+beginValues[0]+","+endValues[0]+","+ TimeUtils.phaseMinute(endValues[1],beginValues[1]);
        }

        return values;
    }

}
