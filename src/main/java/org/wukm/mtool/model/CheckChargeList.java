/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午4:46
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
 * Date : 16-8-2
 * Time : 下午4:46
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@DBModel(name = "CheckChargeList",id = "id")
public class CheckChargeList extends Model<CheckChargeList> {

    //方便查询使用
    public static CheckChargeList checkChargeList = new CheckChargeList();
}
