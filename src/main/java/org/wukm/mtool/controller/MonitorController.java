/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-7
 * Time : 上午11:31
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.model.ServerBean;
import org.wukm.mtool.service.MonitorService;

import java.util.Iterator;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-7
 * Time : 上午11:31
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class MonitorController extends RestfulController{

    public void index(){
        MonitorService monitorService = Duang.duang(MonitorService.class);
        List<ServerBean> list = monitorService.listServer();
        JSONArray array = new JSONArray();
        for(ServerBean serverBean:list){
            JSONObject temp = new JSONObject();
            for(String name:serverBean._getAttrNames()){
                temp.put(name,serverBean.get(name));
            }
            array.add(temp);
        }
        renderJson(ok(array));
    }

    public void save(){
        String body = getBody();
        logger.info("request:" + body);
        JSONObject input = JSONObject.fromObject(body);
        logger.info("json:" + input.toString(4));
        Iterator i = input.keys();
        ServerBean serverBean = new ServerBean();
        while (i.hasNext()){
            Object key = i.next();
            serverBean.put(key.toString(),input.get(key));
        }
        MonitorService monitorService = Duang.duang(MonitorService.class);
        if(monitorService.addServer(serverBean)){
            renderJson(ok());return;
        }
        renderJson(fail("保存数据库失败"));
    }

    public void delete(){
        String id = getPara(0);
        if(StringUtils.isBlank(id)){
            renderJson(fail("Id不可以为空"));return;
        }
        MonitorService monitorService = Duang.duang(MonitorService.class);
        if(monitorService.deleteServer(id)){
            renderJson(ok());
        } else {
            renderJson(fail("保存数据库失败"));
        }
    }

}
