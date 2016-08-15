/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午4:50
 * ---------------------------------
 */
package org.wukm.mtool.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.interceptor.Restful;
import com.jfinal.plugin.activerecord.DbKit;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.wukm.mtool.model.CheckChargeList;
import org.wukm.mtool.service.CheckService;
import org.wukm.mtool.util.ConstantUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.controller
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午4:50
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
@Before(Restful.class)
public class CheckChargeController extends RestfulController{

    public void add(){
        String date = getPara("date");
        CheckService checkService = Duang.duang(CheckService.class);
        File file = checkService.getLocalCheckChargeFile(date);
        if(file == null){
            renderJson(fail("失败"));
        } else {
            JSONObject jsonObject = JSONObject.fromObject(checkService.parse(file));
            renderJson(ok(jsonObject));
        }
    }

    public void show(){
        String id = getPara(0);
        try {
            String sql = "select " +
                    "id," +
                    "mch_id," +
                    "agency_id," +
                    "trans_type," +
                    "movie_id," +
                    "amount," +
                    "trade_no," +
                    "wx_trans_id," +
                    "commission," +
                    "status," +
                    "date_created," +
                    "date_trans," +
                    "prepay_id," +
                    "date_extended" +
                    " from trade_history where id > ?" +
                    " order by id limit 1000 ";
            Connection connection = DbKit.getConfig(ConstantUtil.REPORT_CONFIG_NAME.get(0)).getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,id);
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
            List<CheckChargeList> data = new ArrayList<>();

            while(rs.next()){
                CheckChargeList checkData = new CheckChargeList();
                for(int r = 0;r < columnNames.length;r++){
                    checkData.set(columnNames[r],rs.getString(columnNames[r]));
                }
                checkData.set("checked",0);
                data.add(checkData);
            }
            rs.close();
            statement.close();
            for(CheckChargeList checkChargeList:data){
                checkChargeList.save();
            }
            renderJson(ok()); return;
        } catch (SQLException e){
            logger.info("SQLException:" + e.getMessage(),e);
        }
        renderJson(fail("创建Excel失败"));
    }
}
