import com.xdq.core.model.JdbcConfig;
import com.xdq.core.utils.SplitUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * <b><code>ShowFiveMinuteSnapTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 15:12.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ShowFiveMinuteSnapTest implements Serializable{

    @Autowired
    private JdbcConfig jdbcConfig;

    @Test
    public void test() throws ParseException {
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        SQLContext sqlContext = new SQLContext(jsc);

        sqlContext.udf().register("timestamp",
                new UDF1<Long, Timestamp>() {
                    /**
                     * field_comment
                     *
                     * @since nile-cmgdbs-mintaka project_version
                     */
                    private static final long serialVersionUID = 1L;

                    public Timestamp call(Long t) {
                        return new Timestamp(t);
                    }
                }, DataTypes.TimestampType);

        String path = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/fiveMinuteData/txt/201809050900";

        DataFrame dataFrame = SplitUtils.SplitFiveMinSnap(path,sqlContext);

        dataFrame.show(10);
        /*dataFrame.registerTempTable("t");

        Long time = new Long("1553243136000");

        DataFrame d = sqlContext.sql("select timestamp("+time+") as time,msisdn,cgi from t");

        d.show(10);


        d.write().mode("append").jdbc(jdbcConfig.getUrl(),"D_XDQ_TEST",jdbcConfig.getConnectionProperties());*/

        jsc.close();



    }

}
