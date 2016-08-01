/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-13
 * Time : 上午11:23
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import com.jfinal.plugin.activerecord.DbKit;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.service.ExcelService;
import org.wukm.mtool.util.ConstantUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-13
 * Time : 上午11:23
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class ReportController extends RestfulController {

    public void show(){
        String sql = getPara("sql");
        logger.info("get sql:" + sql);
        sql = StringUtils.replace(sql,"@","%");
        logger.info("new sql:" + sql);
        try {
            Connection connection = DbKit.getConfig(ConstantUtil.REPORT_CONFIG_NAME).getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cNum = rsmd.getColumnCount();
            logger.info("c-num:" + cNum);
            String[] columnNames = new String[cNum];
            for(int ci = 0;ci < cNum;ci++){
                String label = rsmd.getColumnLabel(ci + 1);
                if(StringUtils.isBlank(label)){
                    label = rsmd.getColumnName(ci + 1);
                }
                columnNames[ci] = label;
            }
            List<String[]> data = new ArrayList<>();
            while(rs.next()){
                String[] temp = new String[cNum];
                for(int r = 0;r < cNum;r++){
                    temp[r] = rs.getString(r + 1) == null?"":rs.getString(r + 1);
                }
                data.add(temp);
            }
            rs.close();
            statement.close();
            File f = new File(getRequest().getSession().getServletContext().getRealPath("/"));
            File file = new File(f,"file");
            if(!file.exists()){
                file.mkdir();
                logger.info("create dir:" + file.getAbsolutePath());
            }
            ExcelService excelService = Duang.duang(ExcelService.class);
            logger.info("columnNames:" + Arrays.asList(columnNames));
            logger.info("data size:" + data.size());
            String name = excelService.outputExcel(file,"数据",columnNames,data);
            JSONObject detail = new JSONObject();
            detail.put("fileName",name);
            detail.put("url","/file/" + name);
            renderJson(ok(detail)); return;
        } catch (SQLException e){
            logger.info("SQLException:" + e.getMessage(),e);
        }
        renderJson(fail("创建Excel失败"));
    }
}
