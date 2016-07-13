/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-13
 * Time : 下午3:12
 * ---------------------------------
 */
package org.wukm.mtool.service;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-13
 * Time : 下午3:12
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class ExcelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String outputExcel(File dir, String sheetName, String[] title, List<String[]> data){
        String name = System.currentTimeMillis() + ".xls";
        File file = new File(dir,name);
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(file);
            WritableSheet sheet = wwb.createSheet(sheetName,0);
            int i = 0;
            for (int j = 0;j < title.length;j++) {
                // Label构造器中有三个参数，第一个为列，第二个为行，第三个则为单元格填充的内容
                Label label = new Label(j, i, title[j]);
                // 将被写入数据的单元格添加到工作表
                sheet.addCell(label);
                logger.info("add title:" + label);
            }
            for(int row = 0;row < data.size();row ++) {
                i ++;
                for (int j = 0; j < data.get(row).length; j++) {
                    // Label构造器中有三个参数，第一个为列，第二个为行，第三个则为单元格填充的内容
                    Label label = new Label(j, i, data.get(row)[j]);
                    // 将被写入数据的单元格添加到工作表
                    sheet.addCell(label);
                    logger.info("add data:" + label);
                }
            }
            wwb.write();
            wwb.close();
            return name;
        } catch (IOException e){
            logger.info("IOException:" + e.getMessage(),e);
        } catch (WriteException e) {
            logger.info("WriteException:" + e.getMessage(),e);
        }
        return null;
    }

}
