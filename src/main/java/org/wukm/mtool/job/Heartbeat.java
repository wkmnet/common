/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.job
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-4
 * Time : 下午6:53
 * ---------------------------------
 */
package org.wukm.mtool.job;

import com.jfinal.aop.Duang;
import net.sf.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.MonitorInfoBean;
import org.wukm.mtool.service.MonitorService;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.job
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-4
 * Time : 下午6:53
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class Heartbeat implements Job {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        printSystemInfo();
    }

    private void printSystemInfo(){
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
        logger.info(json.toString(4));
    }
}
