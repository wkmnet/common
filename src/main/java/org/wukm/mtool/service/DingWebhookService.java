/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wukm
 * Date : 17-3-1
 * Time : 下午4:00
 * -------------------------------------------------
 **/
package org.wukm.mtool.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.ServerBean;

import java.io.IOException;
import java.util.Arrays;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wukm
 * Date : 17-3-1
 * Time : 下午4:00
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class DingWebhookService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String WEBHOOK_URL = "https://oapi.dingtalk.com/robot/send?access_token=b3768017e79d42dd960bb772e71fbc53d797c86692b4e3b1e198b287378c4bd6";

    public void dingWebhook(ServerBean serverBean, JSONObject response){
        logger.info("发现异常信息,准备盯盯一下相关人员");
        JSONObject notify = new JSONObject();
        notify.put("msgtype","markdown");
        JSONObject body = new JSONObject();
        body.put("title",serverBean.getStr("serverName").concat("【异常】"));
        StringBuilder text = new StringBuilder();
        text.append("# ".concat(serverBean.getStr("serverName")).concat("--异常报告"));
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append("---");
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append("## ".concat("一、报告提供商"));
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append("报告提供服务商:");
        text.append("![my-logo](http://cs.vmoiver.com/Uploads/Activity/2016-05-04/5729b59da6d7e.png)");
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append("## ".concat("二、报告内容"));
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append(">* ".concat("错误编码:").concat(response.getString("code")));
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append(">* ".concat("错误信息:").concat(response.getString("message")));
        text.append(SystemUtils.LINE_SEPARATOR);
        text.append(">* ".concat("错误详情:").concat(response.getString("body")));
        text.append(SystemUtils.LINE_SEPARATOR);
        body.put("text",text.toString());
        JSONObject at = new JSONObject();
        JSONArray mobiles = new JSONArray();
        mobiles.addAll(Arrays.asList(StringUtils.split(serverBean.getStr("notifyMail"),',')));
        if(mobiles.isEmpty()) {
            at.put("isAtAll",false);
        } else {
            if(mobiles.contains("all")) {
                at.put("isAtAll",true);
            } else {
                at.put("isAtAll",false);
                at.put("atMobiles", mobiles);
            }
        }
        notify.put("markdown",body);
        Request request = Request.Post(WEBHOOK_URL).bodyString(notify.toString(), ContentType.APPLICATION_JSON);
        Response resp = null;
        try {
            resp = request.execute();
            logger.info("request:{},response:{}",serverBean.getStr("checkInterface"),resp.returnContent().asString());
        } catch (IOException e){
            logger.error("IOException:{}",e.getMessage());
            logger.error("IOException:",e);
        } finally {
            resp.discardContent();
        }

    }
}
