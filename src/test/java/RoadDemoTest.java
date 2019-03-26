/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

import com.xdq.core.analysis.job.RoadDemo;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <b><code>RoadDemoTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/26 22:24.
 *
 * @author 谢德奇
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RoadDemoTest {

    @Autowired
    private RoadDemo roadDemo;

    @Test
    public void roadRealUserPathDetailTest(){
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");

        String path = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/test.txt";


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        roadDemo.execute(javaSparkContext, new String[]{path});
    }
}
