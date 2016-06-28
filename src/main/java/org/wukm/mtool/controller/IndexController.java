/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:00
 * ---------------------------------
 */
package org.wukm.mtool.controller;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午6:00
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class IndexController extends ViewController {

    public void index(){
        render("/html/index/index.html");
    }

    public void manageMenu(){
        render("/html/menu/menu.html");
    }
    public void qrcode(){
        render("/html/tool/qrcode.html");
    }
    public void barrageFile(){
        render("/html/tool/barrage.html");
    }
    public void clearUCloudCache(){
        render("/html/tool/clearucloudcache.html");
    }
}
