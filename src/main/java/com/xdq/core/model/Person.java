/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package com.xdq.core.model;

import java.io.Serializable;

/**
 * <b><code>Person</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/5/27 17:12.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka ${PROJECT_VERSION}
 */
public class Person implements Serializable{
    private String name;
    private String sex;
    private String role;

    public Person(String name, String sex, String role) {
        this.name = name;
        this.sex = sex;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
