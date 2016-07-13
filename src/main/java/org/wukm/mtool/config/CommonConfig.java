/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.config
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午5:32
 * ---------------------------------
 */
package org.wukm.mtool.config;

import com.jfinal.config.*;
import com.jfinal.core.Controller;
import com.jfinal.handler.Handler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.interceptor.LoggerInterceptor;
import org.wukm.mtool.model.CloudBean;
import org.wukm.mtool.model.ServerBean;
import org.wukm.mtool.model.ToolMenu;
import org.wukm.mtool.plugin.MailerPlugin;
import org.wukm.mtool.plugin.QuartzPlugin;
import org.wukm.mtool.routes.HtmlRoutes;
import org.wukm.mtool.routes.RestfulRoutes;
import org.wukm.mtool.util.ConstantUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.config
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-9
 * Time : 下午5:32
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class CommonConfig extends JFinalConfig {

    //日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configConstant(Constants me) {
        prop = PropKit.use("system.properties");
        Prop temp = PropKit.use(new File(prop.get("cloud.key.file")));
        if(!StringUtils.isBlank(temp.get("cloud.public.key"))){
            ConstantUtil.CLOUD_PUBLIC_KEY = temp.get("cloud.public.key");
        }
        if(!StringUtils.isBlank(temp.get("cloud.private.key"))){
            ConstantUtil.CLOUD_PRIVATE_KEY = temp.get("cloud.private.key");
        }
        //开发模式
        if(prop.getBoolean("open.dev.model")) {
            me.setDevMode(true);
        } else {
            me.setDevMode(false);
        }
        me.setEncoding(ConstantUtil.CHARSET_UTF_8);
        me.setViewType(ViewType.JSP);
        logger.info("开发模式:" + me.getDevMode());
        logger.info("编码:" + me.getEncoding());
    }

    @Override
    public void configRoute(Routes me) {

        me.add(new HtmlRoutes());
        me.add(new RestfulRoutes());
        for(Map.Entry<String, Class<? extends Controller>> entry : me.getEntrySet()){
            logger.info("key=" + entry.getKey() + ",class=" + entry.getValue().getName());
        }
    }

    @Override
    public void configPlugin(Plugins me) {
        if(prop.getBoolean("open.database")){
            logger.info("start init database...");
            initDatabase(me);
            logger.info("end init database.");
        }
        QuartzPlugin quartzPlugin = new QuartzPlugin("jobs.properties");
        me.add(quartzPlugin);

        me.add(new MailerPlugin());
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new LoggerInterceptor());
//        for(Interceptor i : InterceptorManager.NULL_INTERS){
//            logger.info("Interceptor=" + i.getClass().getName());
//        }
    }

    @Override
    public void configHandler(Handlers me) {
        List<Handler> handlers = me.getHandlerList();
        for(Handler h : handlers){
            logger.info("Handler=" + h.getClass().getName());
        }
    }

    private void initDatabase(Plugins me){
        C3p0Plugin mysqlDatasource = new C3p0Plugin(loadPropertyFile("c3p0.properties"));
        me.add(mysqlDatasource);
        ActiveRecordPlugin mysqlPlugin = new ActiveRecordPlugin(mysqlDatasource);
        me.add(mysqlPlugin);
        mysqlPlugin.setDevMode(true); //是否开启开发模式
        mysqlPlugin.setDialect(new MysqlDialect());
        mysqlPlugin.setTransactionLevel(8);
        mysqlPlugin.setShowSql(true);
        mysqlPlugin.addMapping("toolMenu","id", ToolMenu.class);
        mysqlPlugin.addMapping("cloud","id", CloudBean.class);
        mysqlPlugin.addMapping("ServerList","id", ServerBean.class);

        C3p0Plugin reportDatasource = new C3p0Plugin(loadPropertyFile("report.properties"));
        me.add(reportDatasource);
        ActiveRecordPlugin reportMysqlPlugin = new ActiveRecordPlugin(ConstantUtil.REPORT_CONFIG_NAME,reportDatasource);
        me.add(reportMysqlPlugin);
        reportMysqlPlugin.setDevMode(true); //是否开启开发模式
        reportMysqlPlugin.setDialect(new MysqlDialect());
        reportMysqlPlugin.setTransactionLevel(8);
        reportMysqlPlugin.setShowSql(true);
    }
}
