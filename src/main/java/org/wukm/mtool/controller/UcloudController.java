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
import org.wukm.mtool.service.CloudService;

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
        if(true){
            renderJson(ok("你所填写的数据,没有出现问题,但是本功能暂不启用[风险高].")); return;
        }
        CloudService cloudService = Duang.duang(CloudService.class);
        renderJson(ok(cloudService.clearUrlCache(json)));
    }

    public void index(){
        CloudService cloudService = Duang.duang(CloudService.class);
        renderJson(cloudService.getUcloudDomain());
    }
}
