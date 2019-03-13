/*
 * 广州丰石科技有限公司拥有本软件版权2018并保留所有权利。
 * Copyright 2018, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */
package com.xdq.core.analysis;

import com.xdq.core.common.SparkJob;
import com.xdq.core.common.SpringContextInstance;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.Utils;
import scala.Tuple2;

import java.util.HashSet;
import java.util.Set;


/**
 * <b><code>databaseConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/7/6 15:30.
 *
 * @author yangpy
 * @since nile-training-mintaka 0.1.0
 */
public class App {
    private App() {
    }

    public static void main(String[] args) {
        Class[] classes = new Class[] {
                Long.class,
                double[].class,
                Set.class,
                Tuple2[].class,
                long[].class,
                HashSet.class,
                String.class
        };
        // 初始化Spark环境
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.serializer",
                "org.apache.spark.serializer.KryoSerializer");
        sparkConf.registerKryoClasses(classes);
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        if (args.length > 1) {
            // 获取传参的类名
            String className = args[0];
            Class clazz = Utils.classForName(className);
            // 从Spring获取对象
            Object job = SpringContextInstance.getBean(clazz);
            if (job instanceof SparkJob) {
                // 剔除传参里的类名
                String[] v = new String[args.length-1];
                System.arraycopy(args, 1, v, 0, args.length-1);
                // 执行Spark job
                ((SparkJob)job).execute(javaSparkContext, v);
            }
        }
        javaSparkContext.close();
    }
}
