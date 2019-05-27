/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package com.xdq.core.analysis.job;

import com.xdq.core.common.SparkJob;
import com.xdq.core.model.Person;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.stereotype.Component;

/**
 * <b><code>Demo</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/5/27 17:07.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
@Component
public class Demo extends SparkJob {

    @Override
    public void execute(JavaSparkContext javaSparkContext, String[] args) {
        super.execute(javaSparkContext, args);
        if (args.length >= 1) {
            deal(javaSparkContext, args[0]);
        }
    }

    public void deal(JavaSparkContext jsc, String inputpath) {

        //将hdfs文件读取到rdd
        JavaRDD<String> rdd = jsc.textFile(inputpath);

        //将rdd按逗号分割，封装成对象
        JavaRDD<Person> transToPerson = rdd.map(new Function<String, Person>() {
            @Override
            public Person call(String s) throws Exception {
                String[] ss = s.split(",");
                Person person = new Person(ss[0],ss[1],ss[2]);
                return person;
            }
        });

        //打印
        for(Person p:transToPerson.collect()){
            System.out.println(p.toString());
        }

    }

}
