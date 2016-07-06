/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-17
 * Time : 下午4:23
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.model.CloudBean;
import org.wukm.mtool.service.CloudService;

import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-17
 * Time : 下午4:23
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class UcloudController extends RestfulController{

    public void save(){
        String body = getBody();
        logger.info("body=" + body);
        JSONObject json = JSONObject.fromObject(body);
        logger.info("json:" + json.toString(4));
        JSONArray urls = json.getJSONArray("urls");
        if(urls.isEmpty()){
            renderJson(fail("url不能为空")); return;
        }
        if(StringUtils.isBlank(json.getString("domainId"))){
            renderJson(fail("请选择对应的U-CLOUD域名")); return;
        }
        CloudService cloudService = Duang.duang(CloudService.class);
        JSONObject result = cloudService.clearUrlCache(json);
        if(StringUtils.isBlank(result.getString("error"))){
            renderJson(ok(result));return;
        }
        renderJson(fail(result.getString("message")));
    }

    public void show(){
        String uid = getPara(0);
        String uid_sub = getPara(1);
        if(StringUtils.isBlank(uid) || StringUtils.isBlank(uid_sub)){
            renderJson(fail("不存在的u-cloud-id")); return;
        }
        uid = uid.concat("-").concat(uid_sub);
        JSONObject req = new JSONObject();
        req.put("ucdnId",uid);
        CloudService cloudService = Duang.duang(CloudService.class);
        List<CloudBean> list = cloudService.findByCloudId(req);
        JSONArray res = new JSONArray();
        if(list != null){
            for (CloudBean cb:list){
                JSONObject temp = new JSONObject();
                temp.put("id",cb.getInt("id"));
                temp.put("url",cb.getStr("url"));
                res.add(temp);
            }
        }
        logger.info("get uid:" + uid + ",data=" + req.toString(4));
        renderJson(ok(res));
    }

    public void update(){
        String uid = getPara(0);
        String uid_sub = getPara(1);
        if(StringUtils.isBlank(uid) || StringUtils.isBlank(uid_sub)){
            renderJson(fail("不存在的u-cloud-id")); return;
        }
        uid = uid.concat("-").concat(uid_sub);
        logger.info("uid=" + uid);
        String body = getBody();
        logger.info("request:" + body);
        JSONObject input = JSONObject.fromObject(body);
        if(StringUtils.isBlank(input.getString("url"))){
            renderJson(fail("找不到URL")); return;
        }
        input.put("ucdnId",uid);
        CloudService cloudService = Duang.duang(CloudService.class);
        if(cloudService.addUrl(input)){
            renderJson(ok());
        } else {
            renderJson(fail("操作数据库失败"));
        }
    }

    public void delete(){
        String id = getPara(0);
        if(StringUtils.isBlank(id)){
            renderJson(fail("不存在的id")); return;
        }
        CloudService cloudService = Duang.duang(CloudService.class);
        cloudService.deleteByCloudId(id);
        renderJson(ok());
    }

    public void index(){
        CloudService cloudService = Duang.duang(CloudService.class);
        JSONObject result = cloudService.getUcloudDomain();
        logger.info("response:" + result.toString(4));
        renderJson(result);
    }
}
