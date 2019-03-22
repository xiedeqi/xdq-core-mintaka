/*
 * 广州丰石科技有限公司拥有本软件版权2018并保留所有权利。
 * Copyright 2018, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package com.xdq.core.utils;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.split;

/**
 * <b><code>SplitUtils</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/12/4 0004 下午 5:58.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
public class SplitUtils {

    /**
     * Instantiates a new Split utils.
     */
    protected SplitUtils() {}
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER= LoggerFactory.getLogger(SplitUtils.class);

    //提取五分钟快照txt文件中的time,msisdn,cgi数据
    public static DataFrame SplitFiveMinSnap(String path, SQLContext sqlContext) {
        DataFrame resultFrame = sqlContext.read().text(path).withColumn("splitcol", split(col("value"), ","))
                .select( col("splitcol").getItem(0).as("time"),
                        col("splitcol").getItem(1).as("msisdn"),
                        col("splitcol").getItem(10).as("cgi")
                ).drop("splitcol");
        return resultFrame;
    }
}
