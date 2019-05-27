/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

import com.xdq.core.analysis.job.Demo;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <b><code>DemoTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/5/27 17:15.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class DemoTest {
    @Autowired
    private Demo demo;

    private String path;

    @Before
    public void before(){
        path = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/test/test.txt";
    }

    @Test
    public void roadRealUserPathDetailTest(){
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        demo.execute(javaSparkContext, new String[]{path});
    }

}
