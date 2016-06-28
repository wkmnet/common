/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:54
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.model.ToolMenu;
import org.wukm.mtool.service.ToolMenuService;
import org.wukm.mtool.util.CommonUtil;

import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:54
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class MenuController extends RestfulController {

    public void show(){
        String type = getPara();
        logger.info("show type:" + type);
        ToolMenuService toolMenuService = Duang.duang(ToolMenuService.class);
        if(StringUtils.equals("all",type)){
            renderJson(toolMenuService.loadAllMenus());return;
        }
        if(StringUtils.equals("root",type)){
            renderJson(toolMenuService.loadRootsMenu());return;
        }
        renderJson(ok(JSONObject.fromObject(toolMenuService.loadMenuById(type))));
    }

    public void save(){
        String body = getBody();
        logger.info("receive : " + body);
        JSONObject menu = JSONObject.fromObject(body);

        if(StringUtils.isBlank(menu.getString("menuName"))){
            renderJson(fail("菜单名称不能为空")); return;
        }
        if(StringUtils.isBlank(menu.getString("menuLink"))){
            renderJson(fail("菜单连接不能为空")); return;
        }
        if(StringUtils.isBlank(menu.getString("parentId"))){
            logger.info("parentId is empty");
            renderJson(fail("父结点怎么为空,请选择增加根结点还是增加子结点")); return;
        }

        ToolMenu toolMenu = new ToolMenu();
        if("root".equalsIgnoreCase(menu.getString("parentId"))){
            toolMenu.set("parentId", "root");
        } else {
            toolMenu.set("parentId", menu.getString("parentId"));
        }
        toolMenu.set("id", CommonUtil.createId());
        toolMenu.set("menuName", menu.getString("menuName"));
        toolMenu.set("menuLink", menu.getString("menuLink"));

        if(toolMenu.save()){
            renderJson(ok("数据保存成功"));
        } else {
            renderJson(fail("数据保存失败"));
        }
    }

    public void update(){
        String id = getPara();
        ToolMenu menu = ToolMenu.menu.findById(id);
        if(menu == null || !StringUtils.equals(id,menu.getStr("id"))){
            renderJson(fail("找不到要更新的数据")); return;
        }
        String body = getBody();
        logger.info("update:" + body);
        JSONObject object = JSONObject.fromObject(body);
        if(StringUtils.isBlank(object.getString("id")) || !StringUtils.equals(id,object.getString("id"))){
            renderJson(fail("无效数据")); return;
        }
        if(StringUtils.isBlank(object.getString("menuName"))){
            renderJson(fail("菜单名不能为空")); return;
        }
        menu.set("menuName",object.getString("menuName"));
        if(!StringUtils.equals("root",menu.getStr("parentId"))){
            if(StringUtils.isBlank(object.getString("menuLink"))){
                renderJson(fail("菜单连接不能为空")); return;
            }
            menu.set("menuLink",object.getString("menuLink"));
        }
        if(menu.update()){
            renderJson(ok("更新成功"));
        } else {
            renderJson(fail("更新失败"));
        }
    }

    public void delete(){
        String itemId = getPara(0);
        if(StringUtils.isBlank(itemId)){
            renderJson(fail("无效参数"));
            return;
        }
        ToolMenu menu = ToolMenu.menu.findById(itemId);
        if(StringUtils.equalsIgnoreCase(menu.getStr("parentId"),"root")){
            List<ToolMenu> children = ToolMenu.menu.find("select * from toolMenu where parentId=?",menu.getStr("id"));
            for(ToolMenu child:children){
                ToolMenu.menu.deleteById(child.getStr("id"));
            }
            ToolMenu.menu.deleteById(menu.getStr("id"));
        } else {
            ToolMenu.menu.deleteById(menu.getStr("id"));
        }
        renderJson(ok());
    }

}
