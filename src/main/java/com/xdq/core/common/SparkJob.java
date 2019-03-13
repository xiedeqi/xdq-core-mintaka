/*
 * 广州丰石科技有限公司拥有本软件版权2018并保留所有权利。
 * Copyright 2018, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */
package com.xdq.core.common;

import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

/**
 * <b><code>databaseConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/7/6 15:33.
 *
 * @author yangpy
 * @since nile-training-mintaka 0.1.0
 */
//mvn install clean package -P PROD_ENV -D maven.test.skip=true
public abstract class SparkJob implements Serializable {
    protected static final String INVALID_KEY = "INVALID_KEY";

    protected SparkJob() {
    }

    public void execute(JavaSparkContext sparkContext, String[] args) {
    }

}
