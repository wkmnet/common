/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-17
 * Time : 下午4:38
 * ---------------------------------
 */
package org.wukm.mtool.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.CloudBean;
import org.wukm.mtool.util.CommonUtil;
import org.wukm.mtool.util.ConstantUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-17
 * Time : 下午4:38
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class CloudService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String UCLOUND_CDN = "http://api.spark.ucloud.cn/?";

    public JSONObject clearUrlCache(JSONObject body){
        Map<String,String> params = createRequestMap();
        params.put("Action","RefreshUcdnDomainCache");
        if(body.getBoolean("file")) {
            params.put("Type","file");
        } else {
            params.put("Type","dir");
        }
        params.put("DomainId",body.getString("domainId"));
        JSONArray urls = body.getJSONArray("urls");
        for(int i = 0;i < urls.size();i++){
            JSONObject jsonObject = urls.getJSONObject(i);
            StringBuilder temp = new StringBuilder();
            temp.append("http://");
            temp.append(body.getString("domain"));
            if(!jsonObject.getString("url").startsWith("/")){
                temp.append("/");
            }
            temp.append(jsonObject.getString("url"));
            if(body.getBoolean("file")){
                if(jsonObject.getString("url").endsWith("/")){
                    JSONObject res = new JSONObject();
                    res.put("error","not file");
                    res.put("message","请选择目录");
                    return res;
                }
            } else {
                if(!jsonObject.getString("url").endsWith("/")){
                    JSONObject res = new JSONObject();
                    res.put("error","not dir");
                    res.put("message","请选择文件");
                    return res;
                }
            }
            params.put("UrlList." + i,temp.toString());
        }

//        params.put("Signature", CommonUtil.siginUcloud(params));
        return processRequest(params);
    }

    private JSONObject processRequest(Map<String,String> params){
        try {
            String requestUrl = UCLOUND_CDN.concat(CommonUtil.urlConnect(params));
            logger.info("request:" + requestUrl);
            Request request = Request.Get(requestUrl);
            Content content = request.execute().returnContent();
            String cs = content.asString();
            logger.info("response:" + cs);
            return JSONObject.fromObject(cs);
        } catch (IOException e){
            logger.error("IOException:" + e.getMessage(),e);
        }
        return null;
    }

    public JSONObject getUcloudDomain(){
        Map<String,String> params = createRequestMap();
        params.put("Action","DescribeUcdnDomain");
//        params.put("DomainId.0","ucdn-rje24i");
        params.put("Signature", CommonUtil.siginUcloud(params));
        return processRequest(params);
    }

    private Map<String,String> createRequestMap(){
        Map<String,String> map = new HashMap<>();
//        map.put("ProjectId","");
//        map.put("DomainId","");
        map.put("PublicKey", ConstantUtil.CLOUD_PUBLIC_KEY);
        return map;
    }

    public boolean addUrl(JSONObject input){
        CloudBean cloud = new CloudBean();
        cloud.put("ucdnId",input.get("ucdnId"));
        cloud.put("url",input.get("url"));
        logger.info("save:" + input.toString(4));
        return cloud.save();
    }

    public boolean deleteByCloudId(String id){
        return new CloudBean().deleteById(id);
    }

    public List<CloudBean> findByCloudId(JSONObject input){
        List<CloudBean> list =  CloudBean.cloud.find("select * from cloud where ucdnId=?",input.getString("ucdnId"));
        if(list == null || list.size() == 0){
            return null;
        }
        logger.info("get:" + JSONArray.fromObject(list).toString(4));
        return list;
    }
}
