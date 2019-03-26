/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package com.xdq.core.model;

import java.io.Serializable;

/**
 * <b><code>Road</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/26 23:43.
 *
 * @author 谢德奇
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
public class Road implements Serializable{
    private String msisdn;
    private String begin_cgi;
    private String begin_time;
    private String end_cgi;
    private String end_time;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getBegin_cgi() {
        return begin_cgi;
    }

    public void setBegin_cgi(String begin_cgi) {
        this.begin_cgi = begin_cgi;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_cgi() {
        return end_cgi;
    }

    public void setEnd_cgi(String end_cgi) {
        this.end_cgi = end_cgi;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
