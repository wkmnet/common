/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.routes
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:17
 * ---------------------------------
 */
package org.wukm.mtool.routes;

import com.jfinal.config.Routes;
import org.wukm.mtool.controller.IndexController;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.routes
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:17
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class HtmlRoutes extends Routes {

    @Override
    public void config() {
        add("/", IndexController.class,"/");
    }
}
