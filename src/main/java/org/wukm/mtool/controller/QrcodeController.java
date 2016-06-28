/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-10
 * Time : 下午10:17
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.service.QRCodeService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-10
 * Time : 下午10:17
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class QrcodeController extends RestfulController {

    public void save(){
        String body = getBody();
        logger.info("body=" + body);
        if(StringUtils.isBlank(body)){
            renderJson(fail("body为空")); return;
        }
        try {
            JSONObject json = JSONObject.fromObject(body);
            if (StringUtils.isBlank(json.getString("content"))){
                renderJson(fail("输入源不能为空")); return;
            }
            QRCodeService codeService = Duang.duang(QRCodeService.class);
            File f = new File(getRequest().getSession().getServletContext().getRealPath("/"));
            File image = new File(f,"images");
            if(!image.exists()){
                logger.info("can not find dir:" + image.getAbsolutePath());
            }
            String fileName = codeService.createImage(json.getString("content"),image.getAbsolutePath());
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("alt",json.getString("content"));
            result.put("src","/images/" + fileName);
            result.put("success",true);
            result.put("message", "创建成功!");
            logger.info("return:" + JSONObject.fromObject(result).toString(4));
            renderJson(result);
        } catch (JSONException e){
            logger.error("JSONException:" + e.getMessage(),e);
            renderJson(fail(e.getMessage())); return;
        }
    }
}
