import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.Tuple2;

import java.io.Serializable;

/**
 * <b><code>ParquetTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/20 11:28.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
public class ParquetTest implements Serializable {

    @Test
    public void saveParquet() {
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        String savePath = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/route-speed-detail";

        SQLContext sqlContext = new SQLContext(jsc);
        DataFrame dataFrame1 = sqlContext.sql("select '18816793328' as msisdn,'a1-b1' as path,12.0 as speed,'201903201000' as time");
        DataFrame dataFrame2 = sqlContext.sql("select '18816793321' as msisdn,'a1-b1-c1' as path,15.0 as speed,'201903201000' as time");
        DataFrame dataFrame3 = sqlContext.sql("select '18816793323' as msisdn,'a1' as path,17.0 as speed,'201903201000' as time");
        DataFrame dataFrame4 = sqlContext.sql("select '18816793328' as msisdn,'c1-d1' as path,22.0 as speed,'201903201005' as time");
        DataFrame dataFrame5 = sqlContext.sql("select '18816793325' as msisdn,'b1' as path,32.0 as speed,'201903201000' as time");
        DataFrame dataFrame6 = sqlContext.sql("select '18816793326' as msisdn,'a1-b1-c1-d1' as path,52.0 as speed,'201903201000' as time");
        DataFrame dataFrame7 = sqlContext.sql("select '18816793329' as msisdn,'a1-b1' as path,12.0 as speed,'201903201000' as time");
        DataFrame dataFrame = dataFrame1.unionAll(dataFrame2).unionAll(dataFrame3).unionAll(dataFrame4).unionAll(dataFrame5).unionAll(dataFrame6).unionAll(dataFrame7);

        dataFrame.write().mode(SaveMode.Overwrite).parquet(savePath);

        jsc.close();
    }

    @Test
    public void test(){
        SparkConf sparkConf = new SparkConf()
                .setAppName("test")
                .setMaster("local[4]").set("spark.driver.allowMultipleContexts","true");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        String getPath = this.getClass().getClassLoader().getResource(".")
                .getPath()+"data/route-speed-detail";

        SQLContext sqlContext = new SQLContext(jsc.sc());
        DataFrame dataFrame = sqlContext.parquetFile(getPath);

        JavaRDD<Row> rdd = dataFrame.javaRDD();

        JavaPairRDD<String,String> rdd2 = rdd.mapToPair(new PairFunction<Row, String, String>() {
            @Override
            public Tuple2<String, String> call(Row row) throws Exception {
                return new Tuple2<>(row.getString(1),row.getString(0));
            }
        });

        JavaPairRDD<String,Integer> rdd3 = rdd2.mapToPair(new PairFunction<Tuple2<String, String>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, String> s) throws Exception {
                return new Tuple2<>(s._1,1);
            }
        });

        JavaPairRDD<String,Integer> rdd4 = rdd3.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });


        for(Tuple2 t2:rdd4.collect()){
            System.out.println(t2._1+":"+t2._2);
        }

        jsc.close();

    }

}
