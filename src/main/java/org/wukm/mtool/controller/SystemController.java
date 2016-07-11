/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-11
 * Time : 下午2:21
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Duang;
import net.sf.json.JSONObject;
import org.wukm.mtool.model.MonitorInfoBean;
import org.wukm.mtool.service.MonitorService;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-11
 * Time : 下午2:21
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class SystemController extends RestfulController {

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
        JSONObject object = ok(json);
        object.put("code",0);
        renderJson(object);
    }

}
