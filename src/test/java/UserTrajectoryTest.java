import com.xdq.core.analysis.job.UserTrajectory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <b><code>UserTrajectoryTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/27 11:00.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class UserTrajectoryTest {

    @Autowired
    private UserTrajectory userTrajectory;

    private String path;

    @Before
    public void before(){
        path = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/test.txt";
    }

    @Test
    public void roadRealUserPathDetailTest(){
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        userTrajectory.execute(javaSparkContext, new String[]{path});
    }
}
