/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午5:40
 * ---------------------------------
 */
package org.wukm.mtool.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.ChargeBean;
import org.wukm.mtool.util.CommonUtil;
import org.wukm.mtool.util.ConstantUtil;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午5:40
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class CheckService {

    private String app_id = "wxe9cc9f27c215ef34";

    private String mch_id = "1328960401";

    private String device_info = "WEB";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //下载对账文件
    private String check_url= "https://api.mch.weixin.qq.com/pay/downloadbill";

    public File checkChargeFile(String date){
        Map<String,String> map = new HashMap<>();
        String randomString = RandomStringUtils.randomAlphabetic(30);
        map.put("appid",app_id);
        map.put("mch_id",mch_id);
        map.put("device_info",device_info);
        map.put("nonce_str",randomString);
        map.put("bill_date",date);
        map.put("bill_type","ALL");
        map.put("sign", CommonUtil.signMd5(map));
        logger.info("map:" + map);
        return request(map);
    }

    private File request(Map<String,String> map){
        Request request = Request.Post(check_url);
        String body = CommonUtil.printOutXml(map);
        logger.info("request:" + SystemUtils.LINE_SEPARATOR + body);
        request.bodyString(body,
                ContentType.APPLICATION_XML);
        Response response = null;
        try {
            response = request.execute();
            File file = getFile(map.get("bill_date"));
            response.saveContent(file);
            return file;
        } catch (ClientProtocolException e){
            logger.info("ClientProtocolException:" + e.getMessage(),e);
        } catch (IOException e){
            logger.info("IOException:" + e.getMessage(),e);
        } finally {
            if(response != null) {
                response.discardContent();
            }
        }
        return null;
    }

    public File getLocalCheckChargeFile(String date){
        File file = getFile(date);
        if(file.exists() && file.isFile()){
            return file;
        }
        return checkChargeFile(date);
    }

    private File getFile(String date){
        File root = new File(ConstantUtil.SYSTEM_ROOT);
        File dir = new File(root,"file");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir,date + ".data");
        return file;
    }

    public Map<String,ChargeBean> parse(File file){
        FileReader fileReader = null;
        BufferedReader reader = null;
        Map<String,ChargeBean> map = new HashMap<>();
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            String line = null;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                logger.info("process:" + line);
                if(row == 0) {
                    row ++;
                    continue;
                }
                if(!line.startsWith("`")) {
                    break;
                }
                String[] cs = StringUtils.split(line,",");
                ChargeBean temp = new ChargeBean();
                temp.setId(cs[5].substring(1));
                temp.setOrderNo(cs[6].substring(1));
                if("`".equals(cs[19])) {
                    temp.setType("sale");
                } else {
                    temp.setType("refund");
                }
                BigDecimal bigDecimal = new BigDecimal(cs[12].substring(1));
                temp.setAmount(bigDecimal.multiply(new BigDecimal("100")).intValue());
                temp.setStatus(true);
                BigDecimal settle = new BigDecimal(cs[22].substring(1));
                temp.setSettle(settle.multiply(new BigDecimal("100")).intValue());
                map.put(cs[5].substring(1),temp);
                row ++;
            }
            return map;
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException:" + e.getMessage(),e);
        } catch (IOException e) {
            logger.error("IOException:" + e.getMessage(),e);
        } finally {
            try {
                if(reader != null){
                    reader.close();
                }
                if(fileReader != null){
                    fileReader.close();
                }
            } catch (IOException e) {
                logger.error("IOException:" + e.getMessage(),e);
            }
        }
        return null;
    }

}
