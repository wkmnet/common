/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.util
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午7:45
 * ---------------------------------
 */
package org.wukm.mtool.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.util
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午7:45
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class CommonUtil {

    //日志
    private static Logger LOG = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil(){}

    /**
     * ID生成器
     * @return
     */
    public static String createId(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /** *//**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在
     * 包含汉字的字符串时存在隐患，现调整如下：
     * @param src 要截取的字符串
     * @param start_idx 开始坐标（包括该坐标)
     * @param end_idx   截止坐标（包括该坐标）
     * @return
     */
    public static String substring(String src, int start_idx, int end_idx){
        byte[] b = src.getBytes();
        String tgt = "";
        for(int i=start_idx; i<=end_idx; i++){
            tgt +=(char)b[i];
        }
        return tgt;
    }

    /**
     * 请求参数拼接成url串
     * @param params
     * @return
     */
    public static String urlConnect(Map<String,String> params){
        Iterator<String> i = new TreeSet<String>(params.keySet()).iterator();
        StringBuilder url = new StringBuilder();
        while (i.hasNext()){
            String key = i.next();
            String value = urlEncode(params.get(key));
            url.append(key + "=" + value + "&");
        }
        url.delete(url.lastIndexOf("&"),url.length());
        LOG.info("url:" + url.toString());
        return url.toString();
    }

    /**签名 Ucloud**/
    public static String siginUcloud(Map<String,String> params){
        params.forEach((key,value) ->{
            LOG.info("key={},value={}",key,value);
        });
        Iterator<String> i = new TreeSet<String>(params.keySet()).iterator();
        StringBuilder url = new StringBuilder();
        while (i.hasNext()){
            String key = i.next();
            String value = params.get(key);
            url.append(key + value);
        }
        if(!StringUtils.isBlank(ConstantUtil.CLOUD_PRIVATE_KEY))
            url.append(ConstantUtil.CLOUD_PRIVATE_KEY);
        else
            url.append("46f09bb9fab4f12dfc160dae12273d5332b5debe");
        LOG.info("url:" + url.toString());
        return sha1(url.toString());
    }

    public static String toHexString(byte[] bytes){
        return Hex.encodeHexString(bytes);
    }

    public static String urlEncode(String url){
        try {
            return URLEncoder.encode(url, ConstantUtil.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e){
            LOG.error("UnsupportedEncodingException:" + e.getMessage(),e);
        }
        return null;
    }

    public static String sha1(String source){
        return toHexString(DigestUtils.sha1(source.toString().getBytes()));
    }

    public static void main(String[] args){
        String s = "ActionCreateUHostInstanceCPU2ChargeTypeMonthDiskSpace10ImageIdf43736e1-65a5-4bea-ad2e-8a46e18883c2LoginModePasswordMemory2048NameHost01PasswordVUNsb3VkLmNuPublicKeyucloudsomeone@example.com1296235120854146120Quantity1Regioncn-north-0146f09bb9fab4f12dfc160dae12273d5332b5debe";
        Map<String,String> m = new HashMap<>();
        m.put("Action","CreateUHostInstance");
        m.put("CPU","2");
        m.put("ChargeType","Month");
        m.put("DiskSpace","10");
        m.put("ImageId","f43736e1-65a5-4bea-ad2e-8a46e18883c2");
        m.put("LoginMode","Password");
        m.put("Memory","2048");
        m.put("Name","Host01");
        m.put("Password","VUNsb3VkLmNu");
        m.put("PublicKey","ucloudsomeone@example.com1296235120854146120");
        m.put("Quantity","1");
        m.put("Region","cn-north-01");
        System.out.println(CommonUtil.siginUcloud(m));
        System.out.println(s);
        System.out.println(CommonUtil.sha1(s));
    }
}
