package com.xdq.core.analysis.job;

import com.xdq.core.common.SparkJob;
import com.xdq.core.common.SpringContextInstance;
import com.xdq.core.model.JdbcConfig;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <b><code>CityWholeDayOrignSubscriberStatistics</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/12/11 11:18.
 *
 * @author xiedeqi
 * @since nile-travel-mintaka 0.1.0
 */
@Component
public class CityWholeDayOrignSubscriberStatistics extends SparkJob {

    /**
     * 日志
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CityWholeDayOrignSubscriberStatistics.class);

    @Override
    public void execute(JavaSparkContext jsc, String[] args) {
        super.execute(jsc, args);
        if(args.length == 3){
            try {
                jsc.hadoopConfiguration().set("parquet.enable.summary-metadata",
                        "false");
                jsc.hadoopConfiguration().set(
                        "spark.sql.parquet.output.committer.class",
                        "org.apache.spark.sql.parquet.DirectParquetOutputCommitter");
                dealRealTime(jsc,args);
            } catch (ParseException e) {
                LOGGER.error("CityWholeDayOrignSubscriberStatistics", e);
            }
        }else{
            LOGGER.error("the number of args is not satisfied.input args is : " +
                    "snapPath,tableName,statisticTime");
        }
    }

    public void dealRealTime(JavaSparkContext jsc, String[] args) throws ParseException {
        String snapPath = args[0]; //全市全天用户明细hdfs路径
        String tableName = args[1]; //输出的表名
        String statisticTime = args[2];//分析时间（20180815分析的是15号的数据）

        HiveContext sqlContext = new HiveContext(jsc.sc());
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

        Calendar pathCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        pathCalendar.setTime(sdf.parse(statisticTime));
        Long outputTime = pathCalendar.getTime().getTime();//输出的统计时间

        DataFrame allDataFrame = sqlContext.parquetFile(snapPath+"/"+statisticTime);
        allDataFrame.registerTempTable("all_data_frame");

        //获取省份城市编码表
        DataFrame provinceCityDataFrame = sqlContext.sql("select province,province_code,city,city_code from KLFX_DB.TW_PERS_NIL_PROVINCE_CITY");
        provinceCityDataFrame.registerTempTable("pcity_data_frame");

        //获取国际编码表
        DataFrame countryDataFrame = sqlContext.sql("select country,imsi_seg from KLFX_DB.nil_imsi_country_5bit");
        countryDataFrame.registerTempTable("country_data_frame");

        //省内
        DataFrame inProvinceDataFrame = allDataFrame.filter("attribution_scope=2");
        inProvinceDataFrame.registerTempTable("in_province_data_frame");
        DataFrame screenInProvinceDataFrame = sqlContext.sql("select" +
                " a.city as orign,2 as attribution_scope,count(1) as subscriber_count" +
                " from pcity_data_frame a inner join in_province_data_frame b" +
                " on a.province_code=b.attribution_province_code and a.city_code=b.attribution_city_code group by a.province,a.city");

        //省外
        DataFrame outProvinceDataFrame = allDataFrame.filter("attribution_scope=3");
        outProvinceDataFrame.registerTempTable("out_province_data_frame");
        DataFrame screenOutProvinceDataFrame = sqlContext.sql("select a.province as orign,3 as attribution_scope,count(1) as subscriber_count" +
                " from (select province,province_code from pcity_data_frame group by province,province_code) a inner join" +
                " out_province_data_frame b on a.province_code=b.attribution_province_code group by a.province");

        //国际
        DataFrame outCountryDataFrame = allDataFrame.filter("attribution_scope=4");
        outCountryDataFrame.registerTempTable("out_country_data_frame");
        DataFrame screenOutCountryDataFrame = sqlContext.sql("select" +
                " a.country as orign,4 as attribution_scope,count(1) as subscriber_count" +
                " from country_data_frame a inner join out_country_data_frame b" +
                " on a.imsi_seg=substring(b.imsi,1,5) group by a.country");

        //未知及本地
        DataFrame otherDataFrame = allDataFrame.filter("attribution_scope=0 or attribution_scope=1");
        otherDataFrame.registerTempTable("other_data_frame");
        DataFrame screenOtherDataFrame = sqlContext.sql("select" +
                " case when attribution_scope=0 then '未知' else '本地' end as orign,attribution_scope,count(1) as subscriber_count" +
                " from other_data_frame group by attribution_scope");

        DataFrame resultDataFrame = screenInProvinceDataFrame.unionAll(screenOutProvinceDataFrame).unionAll(screenOtherDataFrame).unionAll(screenOutCountryDataFrame);
        resultDataFrame.registerTempTable("result_data_frame");

        DataFrame finalResultDataFrame = sqlContext.sql("select timestamp("+outputTime+") as statistic_time," +
                " orign,attribution_scope,subscriber_count from result_data_frame");

        //7.写入pg
        JdbcConfig jdbcConfig = SpringContextInstance.getBean(JdbcConfig.class);
        finalResultDataFrame.write().mode("append")
                .jdbc(jdbcConfig.getUrl(), tableName, jdbcConfig.getConnectionProperties());




    }
}
