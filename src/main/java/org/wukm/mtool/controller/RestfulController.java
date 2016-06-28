/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:52
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.MonitorInfoBean;
import org.wukm.mtool.service.MonitorService;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:52
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class RestfulController extends Controller {
    //日志
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public void index(){
        MonitorService monitorService = Duang.duang(MonitorService.class);
        MonitorInfoBean monitorInfo = monitorService.getMonitorInfoBean();
        JSONObject json = new JSONObject();
        json.put("cpu占有率",monitorInfo.getCpuRatio());
        json.put("可使用内存", monitorInfo.getTotalMemory());
        json.put("剩余内存", monitorInfo.getFreeMemory());
        json.put("最大可使用内存", monitorInfo.getMaxMemory());

        json.put("操作系统", monitorInfo.getOsName());
        json.put("总的物理内存", monitorInfo.getTotalMemorySize() + "kb");
        json.put("剩余的物理内存", monitorInfo.getFreeMemory() + "kb");
        json.put("已使用的物理内存", monitorInfo.getUsedMemory() + "kb");
        json.put("剩余的物理内存", monitorInfo.getFreePhysicalMemorySize());
        json.put("线程总数", monitorInfo.getTotalThread());
        json.put("swap大小", monitorInfo.getTotalSwapSpaceSize());
        renderJson(ok(json));
    }

    protected String body = null;

    protected String getBody(){
        if(getRequest().getMethod().equalsIgnoreCase("GET")){
            return body;
        }
        if(body != null) {
            return body;
        }
        try {
            BufferedReader reader = getRequest().getReader();
            StringBuilder body = new StringBuilder();
            String line;
            while(!StringUtils.isBlank(line = reader.readLine())){
                body.append(line);
            }
            this.body = body.toString();
        } catch (IOException e){
            logger.error("IOException:" + e.getMessage(),e);
        }
        return  body;
    }

    protected JSONObject ok(){
        return ok(null,null);
    }

    protected JSONObject ok(String message){
        return ok(message,null);
    }

    protected JSONObject ok(JSON data){
        return ok(null,data);
    }

    protected JSONObject ok(String message,JSON data){
        JSONObject ok = new JSONObject();
        ok.put("success",true);
        if(StringUtils.isBlank(message)) {
            ok.put("message", "成功");
        } else {
            ok.put("message", message);
        }
        if(data != null) {
            ok.put("data", data);
        }
        logger.info("response:" + ok.toString(4));
        return ok;
    }

    protected JSONObject fail(String message){
        JSONObject fail = new JSONObject();
        fail.put("success",false);
        if(StringUtils.isBlank(message)) {
            fail.put("message", "失败");
        } else {
            fail.put("message", message);
        }
        logger.info("response:" + fail.toString(4));
        return fail;
    }
}
