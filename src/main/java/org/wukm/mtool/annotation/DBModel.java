package org.wukm.mtool.annotation;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.annotation
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午12:09
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DBModel {
    /**
     * 指定数据库表名称
     * @return
     */
    String name();

    /**
     * 指定数据库表主键名称
     * @return
     */
    String id();
}
