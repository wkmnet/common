/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.routes
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:51
 * ---------------------------------
 */
package org.wukm.mtool.routes;

import com.jfinal.config.Routes;
import org.wukm.mtool.controller.*;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.routes
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:51
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class RestfulRoutes extends Routes {
    @Override
    public void config() {
        add("/api/menu", MenuController.class);
        add("/api/qrcode", QrcodeController.class);
        add("/api/upload", UploadController.class);
        add("/api/cloud", UcloudController.class);
        add("/api/monitor",MonitorController.class);
    }
}
