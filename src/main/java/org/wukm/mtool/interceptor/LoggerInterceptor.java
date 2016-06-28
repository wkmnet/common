/**
 * Apache LICENSE-2.0
 * Project name : mtool
 * Package name : org.wkm.mtool.interceptor
 * Author : Wukunmeng
 * User : wkm
 * Date : 15-10-12
 * Time : 下午9:55
 * 版权所有,侵权必究！
 */
package org.wukm.mtool.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.JsonRender;
import com.jfinal.render.JspRender;
import com.jfinal.render.Render;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Project name : mtool
 * Package name : org.wkm.mtool.interceptor
 * Author : Wukunmeng
 * User : wkm
 * Date : 15-10-12
 * Time : 下午9:55
 * 版权所有,侵权必究！
 * To change this template use File | Settings | File and Code Templates.
 */
public class LoggerInterceptor implements Interceptor{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 输出请求和响应参数
     * @param inv
     */
    public void intercept(Invocation inv) {
        logger.info("controllerKey:" + inv.getControllerKey());
        logger.info("methodName:" + inv.getMethodName());

        Controller c = inv.getController();
        logger.info("method:" + c.getRequest().getMethod());
        printHeader(c.getRequest());
        Map<String, String[]> map =  c.getParaMap();
        if(map != null){
            StringBuilder para = new StringBuilder();
            for(Map.Entry<String,String[]> entry : map.entrySet()){
                para.append(entry.getKey() + "=" + entry.getValue());
                para.append(SystemUtils.LINE_SEPARATOR);
            }
            printRequest(para);
        }
        String para = c.getPara();
        if(!StringUtils.isBlank(para)){
            printRequest(para.split("-"));
        }
        inv.invoke();

        printHeader(c.getResponse());
        Object value = inv.getReturnValue();
        if(value == null){
            logger.info("return:");
        } else if (value instanceof String){
            logger.info("return:" + value);
        } else {
            logger.info("return:" + value.getClass() + ";" + value);
        }
        printReturnValue(c.getRender());
    }

    private void printHeader(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        JSONObject header = new JSONObject();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            header.put(headerName,request.getHeader(headerName));
        }
        header.put("uri",request.getRequestURI());
        logger.info("Headers:" + SystemUtils.LINE_SEPARATOR + header.toString(4));
    }

    private void printHeader(HttpServletResponse response){
        Collection<String> headerNames = response.getHeaderNames();
        JSONObject header = new JSONObject();
        for (String headerName:headerNames){
            header.put(headerName,response.getHeader(headerName));
        }
        logger.info("Headers:" + SystemUtils.LINE_SEPARATOR + header.toString(4));
    }

    /**
     * 打印请求信息
     * @param value
     */
    private void printRequest(Object value){
        logger.info(SystemUtils.LINE_SEPARATOR + "request:" + SystemUtils.LINE_SEPARATOR + value);
    }

    /**
     * 打印响应信息
     * @param value
     */
    private void printResponse(Object value){
        logger.info(SystemUtils.LINE_SEPARATOR + "response:" + SystemUtils.LINE_SEPARATOR + value);
    }

    /**
     * 打印响应结果
     * @param render
     */
    private void printReturnValue(Render render){
        if(render == null){
            logger.info("render:");
            return;
        }

        logger.info("render:" + render.getClass());

        if(render instanceof JsonRender){
            String value = ((JsonRender) render).getJsonText();
            if(value.startsWith("[")){
                printResponse(JSONArray.fromObject(value).toString(4));
            }else if(value.startsWith("{")){
                printResponse(JSONObject.fromObject(value).toString(4));
            }else {
                printResponse(value);
            }
        }
        if(render instanceof JspRender){
            printResponse(render.getView());
        }

    }
}
