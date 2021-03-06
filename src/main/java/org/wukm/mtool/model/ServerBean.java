/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-7
 * Time : 上午11:33
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
 * Date : 16-7-7
 * Time : 上午11:33
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@DBModel(name = "ServerList",id = "id")
public class ServerBean extends Model<ServerBean> {

    public static ServerBean SERVER_BEAN = new ServerBean();

}
