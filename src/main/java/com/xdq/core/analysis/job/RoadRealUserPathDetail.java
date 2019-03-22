package com.xdq.core.analysis.job;

import com.xdq.core.common.SparkJob;
import com.xdq.core.model.JdbcConfig;
import com.xdq.core.utils.PathUtils;
import com.xdq.core.utils.SplitUtils;
import com.xdq.core.utils.TimeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * <b><code>RoadRealUserPathDetail</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 17:08.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@Repository
public class RoadRealUserPathDetail extends SparkJob{

    @Autowired
    private JdbcConfig jdbcConfig;

    private String D_HX_ROAD_WAY_POINT = "D_HX_ROAD_WAY_POINT"; //路点信息表

    private String D_HX_ROAD_WAY_PATH = "D_HX_ROAD_WAY_PATH"; //路径匹配表

    @Override
    public void execute(JavaSparkContext javaSparkContext, String[] args) {
        super.execute(javaSparkContext, args);
        if (args.length >= 2) {
            deal(javaSparkContext, args[0], args[1]);
        }
    }

    public void deal(JavaSparkContext javaSparkContext, String inputpath,
                     String statisticTime) {
        SQLContext sqlContext = new SQLContext(javaSparkContext);
        sqlContext.setConf("spark.sql.parquet.binaryAsString", "true");

        sqlContext.udf().register("timestamp", new UDF1<Long, Timestamp>() {
            /**
             * field_comment
             *
             * @since 自定义函数，将时间戳转换时间格式
             */
            private static final long serialVersionUID = 1L;

            public Timestamp call(Long t) {
                return new Timestamp(t);
            }
        }, DataTypes.TimestampType);

        //获取当前五分钟的时间戳
        Long currentTime = TimeUtils.getTimeStamp(statisticTime);

        //获取当前五分钟快照及下一个五分钟快照的路径
        List<String> paths = PathUtils.getPaths(inputpath,statisticTime,2);

        //获取当前五分钟快照表
        DataFrame currentFiveSnap = SplitUtils.SplitFiveMinSnap(paths.get(0),sqlContext);
        currentFiveSnap.registerTempTable("current_five_snap");
        //获取下一个五分钟快照表
        DataFrame nextFiveSnap = SplitUtils.SplitFiveMinSnap(paths.get(1),sqlContext);
        nextFiveSnap.registerTempTable("next_five_snap");

        //加载路点信息表
        DataFrame roadPoint = sqlContext.read().jdbc(jdbcConfig.getUrl(), D_HX_ROAD_WAY_POINT, jdbcConfig.getConnectionProperties());
        roadPoint.registerTempTable("road_point");

        //加载路径匹配表
        DataFrame roadPath = sqlContext.read().jdbc(jdbcConfig.getUrl(), D_HX_ROAD_WAY_PATH, jdbcConfig.getConnectionProperties());
        roadPath.registerTempTable("road_path");

        //统计结果 msisdn,path_id,speed,statistic_time
        DataFrame result = sqlContext.sql("select msisdn,ID as path_id,SPEED as speed,timestamp("+currentTime+") as statistic_time from" +
                " (select a.msisdn,a.ID as prev_roadId,b.ID as next_roadId from" +
                " (select msisdn,ID from current_five_snap inner join road_point on current_five_snap.cgi=road_point.CGI) a" +
                " inner join" +
                " (select msisdn,ID from next_five_snap inner join road_point on next_five_snap.cgi=road_point.CGI) b" +
                " on a.msisdn=b.msisdn) c" +
                " inner join" +
                " road_path on c.prev_roadId=road_path.BEGIN_POINT_ID and c.next_roadId=road_path.END_POINT_ID" +
                " ");

        result.show(10);

        //存明细，存入hdfs


    }




}
