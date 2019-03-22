import com.xdq.core.analysis.job.RoadRealUserPathDetail;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <b><code>RoadRealUserPathDetailTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 18:04.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RoadRealUserPathDetailTest {

    @Autowired
    private RoadRealUserPathDetail roadRealUserPathDetail;

    private String path;
    private String statisticTime;

    @Before
    public void before(){
        path = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/fiveMinuteData/txt";

        statisticTime = "201809050900";
    }

    @Test
    public void roadRealUserPathDetailTest(){
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        roadRealUserPathDetail.execute(javaSparkContext, new String[]{path, statisticTime});
    }


}
