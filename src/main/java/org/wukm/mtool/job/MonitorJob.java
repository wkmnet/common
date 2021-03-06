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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.http.HttpStatus;
import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.ServerBean;
import org.wukm.mtool.service.DingWebhookService;
import org.wukm.mtool.service.MonitorService;
import org.wukm.mtool.util.CommonUtil;

import java.util.List;

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
        Calendar calendar = context.getCalendar();
        logger.info("calendar:" + calendar);
        printMonitor();
    }

    private void printMonitor(){
        MonitorService monitorService = Duang.duang(MonitorService.class);
        List<ServerBean> list = monitorService.listServer();

        for(ServerBean serverBean:list){
            StringBuilder output = new StringBuilder();
            output.append(SystemUtils.LINE_SEPARATOR);
            output.append("interface:" + serverBean.getStr("checkInterface"));
            output.append(SystemUtils.LINE_SEPARATOR);
            output.append("org status:" + serverBean.getInt("status"));
            output.append(SystemUtils.LINE_SEPARATOR);
            JSONObject jo = monitorService.scanServer(serverBean.getStr("checkInterface"));
            boolean checked = checkInterface(jo);
            if(checked){
                serverBean.set("status",1);
                output.append("change status:1");
            } else {
                serverBean.set("status",0);
                output.append("change status:0");
                if(serverBean.getStr("notifyMail").indexOf('@') < 0){
                    DingWebhookService webhookService = Duang.duang(DingWebhookService.class);
                    webhookService.dingWebhook(serverBean,jo);
                }else if(!StringUtils.isBlank(serverBean.getStr("notifyMail"))){
                    StringBuilder content = new StringBuilder();
                    content.append(serverBean.getStr("serverDomain") + ":服务无法连接,请尽快处理!<br/>");
                    content.append("响应码:" + jo.getInt("code"));
                    content.append("<br/>");
                    content.append("响应信息:" + jo.getString("message"));
                    content.append("<br/>");
                    content.append("响应体:" + jo.getString("body"));
                    CommonUtil.sendMail(serverBean.getStr("serverName"),content.toString(),
                            serverBean.getStr("notifyMail"));
                }
            }
            serverBean.update();
            output.append(SystemUtils.LINE_SEPARATOR);
            output.append("check interface result:");
            output.append(SystemUtils.LINE_SEPARATOR);
            if(jo == null){
                output.append("null");
            } else {
                output.append(jo.toString(4));
            }
            logger.info(output.toString());
        }

    }

    private boolean checkInterface(JSONObject object){
        if(object == null){
            return false;
        }
        if(object.containsKey("code")){
            return object.getInt("code") == HttpStatus.SC_OK;
        }
        return false;
    }
}
