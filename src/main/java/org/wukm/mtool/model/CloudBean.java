/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-5
 * Time : 下午6:31
 * ---------------------------------
 */
package org.wukm.mtool.model;

import com.jfinal.plugin.activerecord.Model;
import org.wukm.mtool.annotation.DBModel;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-5
 * Time : 下午6:31
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@DBModel(name = "cloud",id = "id")
public class CloudBean extends Model<CloudBean> {
    //方便查询使用
    public static CloudBean cloud = new CloudBean();
}
