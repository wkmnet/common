package org.wukm.mtool.model;

import com.jfinal.plugin.activerecord.Model;
import org.wukm.mtool.annotation.DBModel;

/**
 * Created with IntelliJ IDEA.
 * User: wkm
 * Date: 15-9-5
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
@DBModel(name = "toolMenu",id="id")
public class ToolMenu extends Model<ToolMenu>{

    //方便查询使用
    public static ToolMenu menu = new ToolMenu();
}
