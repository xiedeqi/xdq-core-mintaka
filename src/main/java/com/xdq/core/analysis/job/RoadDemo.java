/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package com.xdq.core.analysis.job;

import com.xdq.core.common.SparkJob;
import com.xdq.core.model.JdbcConfig;
import com.xdq.core.model.Road;
import com.xdq.core.utils.SplitUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import scala.Tuple2;
import java.util.Arrays;

/**
 * <b><code>RoadDemo</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/26 22:21.
 *
 * @author 谢德奇
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
@Repository
public class RoadDemo extends SparkJob {

    @Autowired
    private JdbcConfig jdbcConfig;

    @Override
    public void execute(JavaSparkContext javaSparkContext, String[] args) {
        super.execute(javaSparkContext, args);
        if (args.length >= 1) {
            deal(javaSparkContext, args[0]);
        }
    }

    public void deal(JavaSparkContext javaSparkContext, String inputpath) {
        SQLContext sqlContext = new SQLContext(javaSparkContext);
        sqlContext.setConf("spark.sql.parquet.binaryAsString", "true");

        DataFrame dataFrame = SplitUtils.SplitFiveMinSnapTest(inputpath,sqlContext).orderBy("msisdn","time").filter("cgi != '10038-108199'");

        JavaRDD<Row> rdd1 = dataFrame.javaRDD();

        JavaPairRDD<String,String> rdd2 = rdd1.mapToPair(new PairFunction<Row, String, String>() {
            @Override
            public Tuple2<String, String> call(Row row) throws Exception {
                return new Tuple2<>(row.getString(1)+","+row.getString(2),row.getString(0));
            }
        });

        JavaPairRDD<String,String> rdd3 = rdd2.reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String s, String s2) throws Exception {
                return s;
            }
        });

        JavaRDD<String> rdd4 = rdd3.map(new Function<Tuple2<String, String>, String>() {
            @Override
            public String call(Tuple2<String, String> t) throws Exception {
                return t._1+","+t._2;
            }
        });

        JavaPairRDD<String,String> rdd5 = rdd4.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] ss = s.split(",");
                return new Tuple2<>(ss[0],ss[1]+","+ss[2]);
            }
        });

        JavaPairRDD<String,String> rdd6 = rdd5.reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String s, String s2) throws Exception {
                return s+"|"+s2;
            }
        });

        JavaRDD<String> rdd7 = rdd6.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {
            @Override
            public Iterable<String> call(Tuple2<String, String> t) throws Exception {

                String ss[] = t._2.split("\\|");

                String[] values = new String[ss.length-1];

                for(int i=0;i<values.length;i++){
                    values[i] = t._1+","+ss[i]+","+ss[i+1];
                }

                return Arrays.asList(values);
            }
        });

        JavaRDD<Road> rdd8 = rdd7.map(new Function<String, Road>() {
            @Override
            public Road call(String s) throws Exception {
                String ss[] = s.split(",");
                Road road = new Road();
                road.setMsisdn(ss[0]);
                road.setBegin_cgi(ss[1]);
                road.setBegin_time(ss[2]);
                road.setEnd_cgi(ss[3]);
                road.setEnd_time(ss[4]);
                return road;
            }
        });

        DataFrame dataFrame1 = sqlContext.createDataFrame(rdd8,Road.class);

        dataFrame1.show(10);


    }




}
