/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-11
 * Time : 下午7:47
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import com.jfinal.upload.UploadFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.service.BarrageService;

import java.util.Enumeration;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-11
 * Time : 下午7:47
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class UploadController extends RestfulController {

    public void save(){
        logger.info("upload save...");
        UploadFile file = getFile();
        logger.info("fileName:" + file.getFileName());
        Enumeration<String> enumeration = getParaNames();
        logger.info("key:barrageType,value:" + getPara("barrageType"));
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            logger.info("key:" + key + ",value:" + getPara(key));
        }
        int type = getParaToInt("barrageType",0);
        long movieId = getParaToLong("movieId");
        long platformId = getParaToLong("platformId");
        switch (type){
            case 1:
                BarrageService barrageService = Duang.duang(BarrageService.class);
                barrageService.processBarrageFile(file.getFile(),movieId,platformId);
                renderJson(ok("文件上传成功,已处理完成"));
                break;
            default:
                renderJson(ok("文件上传成功,没有处理器,自动销毁"));
        }
        FileUtils.deleteQuietly(file.getFile());
    }

    public void show(){
        JSONArray type = new JSONArray();
        JSONObject bilibili = new JSONObject();
        bilibili.put("code",1);
        bilibili.put("name","哔哩哔哩");
        type.add(bilibili);
        renderJson(ok(type));
    }
}
