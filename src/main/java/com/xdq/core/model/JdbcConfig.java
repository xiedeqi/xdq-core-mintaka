/*
 * 广州丰石科技有限公司拥有本软件版权2018并保留所有权利。
 * Copyright 2018, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */
package com.xdq.core.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * <b><code>databaseConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/7/11 21:28.
 *
 * @author wuguozhu
 * @since nile-training-mintaka 0.1.0
 */

public class JdbcConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * data base url.
     *
     * @since nile-training-mintaka 0.1.0
     */
    private String url;


    /**
     * data base properties.
     *
     * @since nile-training-mintaka 0.1.0
     */
    private Properties connectionProperties;

    /**
     * table name.
     *
     * @since nile-training-mintaka 0.1.0
     */
    private  String table;

    /**
     * Get url.
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set database url
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get database Properties.
     * @return Properties
     */
    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    /**
     * Set connection properties
     * @param connectionProperties
     */
    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    /**
     * Get table name.
     * @return
     */
    public String getTable() {
        return table;
    }

    /**
     * Set table name.
     * @param table
     */
    public void setTable(String table) {
        this.table = table;
    }
}
