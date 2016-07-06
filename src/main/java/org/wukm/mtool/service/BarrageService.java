/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-12
 * Time : 下午6:25
 * ---------------------------------
 */
package org.wukm.mtool.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-12
 * Time : 下午6:25
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class BarrageService {

    private String url = "http://danmu.test.vmovier.com/api/new/barrage/batch/add";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void processBarrageFile(File file,long movieId,long platformId){
        logger.info("open:" + file.getName());
        logger.info("open:" + file.getAbsolutePath());
        logger.info("open:" + file.length());
        SAXReader saxReader = new SAXReader();
        JSONArray barrages = new JSONArray();
        try {
            Document document = saxReader.read(file);
            logger.info("document:" + document.asXML());
            Element rootElement = document.getRootElement();
            for (Iterator<Element> i = rootElement.elementIterator(); i.hasNext();) {
                Element child = i.next();
                StringBuilder xmlText = new StringBuilder();
                xmlText.append("name:" + child.getName());
                xmlText.append(SystemUtils.LINE_SEPARATOR);
                xmlText.append("value:" + child.getText());
                xmlText.append(SystemUtils.LINE_SEPARATOR);
                if("d".equals(child.getName())) {
                    JSONObject d = new JSONObject();
                    for (Iterator<Attribute> a = child.attributeIterator(); a.hasNext(); ) {
                        Attribute p = a.next();
                        if(StringUtils.equals("p",p.getName())) {
                            String[] ps = p.getValue().split(",");
                            long playTime = (long)(NumberUtils.toDouble(ps[0]) * 1000);
                            d.put("movie_id",movieId);
                            d.put("platform_id",platformId);
                            d.put("play_time",playTime);
                            d.put("barrage_type",NumberUtils.toInt(ps[1]));
                            d.put("barrage_font_size",ps[2]);
                            d.put("barrage_color",ps[3]);
//                            long publishTime = NumberUtils.toLong(ps[4]) * 1000;
//                            long extend_info = NumberUtils.toLong(ps[5]);
                            d.put("user_id",ps[6]);
                            d.put("show_status",1);
                            d.put("barrage_content",child.getStringValue());
                        }
                        barrages.add(d);
                    }
                }
                logger.info("print:" + xmlText.toString());
            }
            logger.info("end:" + file.getName());
            logger.info("add barrage info...");
            addBarrageInfo(barrages);
            logger.info("end barrage info.");
        } catch (DocumentException e) {
           logger.info("DocumentException:" + e.getMessage(),e);
        }
    }

    private void addBarrageInfo(JSONArray barrages){
        int row = barrages.size() % 100 == 0 ? barrages.size() / 100 : barrages.size() / 100 + 1;
        for(int i = 0 ;i < row;i++){
            int endIndex = (i + 1) * 100 > barrages.size() ? barrages.size() :(i + 1) * 100;
            List l = barrages.subList(i * 100,endIndex);
            String body = JSONArray.fromObject(l).toString();
            postBarrage(body);
        }
    }
    private void postBarrage(String body){
        Response response = null;
        try {
            Request request = Request.Post(url);
            logger.info("request body:" + body);
            response = request.bodyString(body, ContentType.APPLICATION_JSON).execute();
            logger.info(response.returnContent().asString());
        } catch (ClientProtocolException e){
            logger.error("body:" + body);
            logger.error("ClientProtocolException:" + e.getMessage(),e);
        } catch (IOException e){
            logger.error("body:" + body);
            logger.error("IOException:" + e.getMessage(),e);
        } finally {
            if(response != null){
                response.discardContent();
            }
        }
    }

}
