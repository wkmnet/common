/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午10:12
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.core.Controller;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午10:12
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
//@Before(Restful.class)
public class TestController extends Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    public void index(){
//        show();
//    }

    public void show(){
        JSONObject o = new JSONObject();
        o.put("a","b");
        logger.info(o.toString(4));
        renderJson(o);
    }
}
