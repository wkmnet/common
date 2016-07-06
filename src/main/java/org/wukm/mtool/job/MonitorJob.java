/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.job
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-6
 * Time : 下午8:06
 * ---------------------------------
 */
package org.wukm.mtool.job;

import com.jfinal.aop.Duang;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.SystemUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.service.MonitorService;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.job
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-6
 * Time : 下午8:06
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class MonitorJob implements Job {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        printMonitor();
    }

    private void printMonitor(){
        String http = "http://danmu.test.vmovier.com/api/system/info";
        MonitorService monitor = Duang.duang(MonitorService.class);
        logger.info(http + "--->" + SystemUtils.LINE_SEPARATOR + monitor.scanServer(http).toString(4));
    }
}
