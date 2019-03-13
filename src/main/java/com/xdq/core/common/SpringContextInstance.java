/*
 * 广州丰石科技有限公司拥有本软件版权2018并保留所有权利。
 * Copyright 2018, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */
package com.xdq.core.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <b><code>databaseConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/7/6 15:35.
 *
 * @author yangpy
 * @since nile-training-mintaka 0.1.0
 */

public class SpringContextInstance {
    private static ApplicationContext instance;

    protected SpringContextInstance() {
    }

    public static synchronized ApplicationContext getInstance() {
        if (null == instance)
            instance = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        return instance;
    }

    public static <T> T getBean(String id, Class<T> requiredType) {
        ApplicationContext context = getInstance();
        return context.getBean(id, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        ApplicationContext context = getInstance();
        return context.getBean(requiredType);
    }
}
