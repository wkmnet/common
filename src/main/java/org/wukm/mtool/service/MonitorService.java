/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-10
 * Time : 下午7:20
 * ---------------------------------
 */
package org.wukm.mtool.service;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wukm.mtool.model.MonitorInfoBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.sun.management.OperatingSystemMXBean;
import org.wukm.mtool.util.CommonUtil;


/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.service
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-6-10
 * Time : 下午7:20
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class MonitorService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了
    private static final int CPUTIME = 5000;

    private static final int PERCENT = 100;

    private static final int FAULTLENGTH = 10;

    /** *//**
     * 获得当前的监控对象.
     * @return 返回构造好的监控对象
     * @throws Exception
     * @author amg     * Creation date: 2008-4-25 - 上午10:45:08
     */
    public MonitorInfoBean getMonitorInfoBean(){
        int kb = 1024;

        // 可使用内存
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        // 剩余内存
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        // 最大可使用内存
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;

        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory
                .getOperatingSystemMXBean();


        // 操作系统
        String osName = System.getProperty("os.name");
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        // 已使用的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb
                .getFreePhysicalMemorySize())
                / kb;

        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent())
            ;
        int totalThread = parentThread.activeCount();

        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
        } else {
            cpuRatio = getCpuRateForLinux();
        }

        // 构造返回对象
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setFreeMemory(freeMemory);
        infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
        infoBean.setMaxMemory(maxMemory);
        infoBean.setOsName(osName);
        infoBean.setTotalMemory(totalMemory);
        infoBean.setTotalMemorySize(totalMemorySize);
        infoBean.setTotalThread(totalThread);
        infoBean.setUsedMemory(usedMemory);
        infoBean.setCpuRatio(cpuRatio);
        infoBean.setTotalSwapSpaceSize(osmxb.getTotalSwapSpaceSize());
        return infoBean;
    }

    private double getCpuRateForLinux(){
        String linuxVersion = SystemUtils.OS_VERSION;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try{
            logger.info("Get usage rate of CUP , linux version: " + linuxVersion);

            Process process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);

            if(linuxVersion.equals("2.4")){
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                String user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();

                logger.info(user+" , "+system+" , "+nice);

                user = user.substring(0,user.indexOf("%"));
                system = system.substring(0,system.indexOf("%"));
                nice = nice.substring(0,nice.indexOf("%"));

                float userUsage = new Float(user).floatValue();
                float systemUsage = new Float(system).floatValue();
                float niceUsage = new Float(nice).floatValue();

                return (userUsage+systemUsage+niceUsage)/100;
            }else{
                String line1 = brStat.readLine();
                String line2 = brStat.readLine();
                String line3 = brStat.readLine();
                tokenStat = new StringTokenizer(line3);
                logger.info(tokenStat.nextToken());
                String cpuRatio = tokenStat.nextToken();
                logger.info(cpuRatio);
                logger.info(tokenStat.nextToken());
                logger.info(tokenStat.nextToken());
                logger.info(tokenStat.nextToken());
                logger.info(tokenStat.nextToken());
                String cpuUsage = tokenStat.nextToken();
                logger.info("line1:" + line1);
                logger.info("line2:" + line2);
                logger.info("line3:" + line3);
                logger.info("CPU idle : " + cpuUsage);
//                Float usage = new Float(cpuUsage.substring(0,cpuUsage.indexOf("%")));

                return new BigDecimal(cpuRatio).doubleValue();
            }


        } catch(IOException ioe){
            logger.error(ioe.getMessage(),ioe);
            freeResource(is, isr, brStat);
            return 1;
        } finally{
            freeResource(is, isr, brStat);
        }

    }
    private void freeResource(InputStream is,  InputStreamReader isr, BufferedReader br){
        try{
            if(is!=null)
                is.close();
            if(isr!=null)
                isr.close();
            if(br!=null)
                br.close();
        }catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }

    /** *//**
     * 获得CPU使用率.
     * @return 返回cpu使用率
     * @author amg     * Creation date: 2008-4-25 - 下午06:05:11
     */
    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime))
                        .doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    /** *//**
     * 读取CPU信息.
     * @param proc
     * @return
     * @author amg     * Creation date: 2008-4-25 - 下午06:10:14
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = CommonUtil.substring(line, capidx, cmdidx - 1)
                        .trim();
                String cmd = CommonUtil.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idletime += Long.valueOf(
                            CommonUtil.substring(line, kmtidx, rocidx - 1).trim())
                            .longValue();
                    idletime += Long.valueOf(
                            CommonUtil.substring(line, umtidx, wocidx - 1).trim())
                            .longValue();
                    continue;
                }

                kneltime += Long.valueOf(
                        CommonUtil.substring(line, kmtidx, rocidx - 1).trim())
                        .longValue();
                usertime += Long.valueOf(
                        CommonUtil.substring(line, umtidx, wocidx - 1).trim())
                        .longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** *//**
     * 测试方法.
     * @param args
     * @throws Exception
     * @author amg     * Creation date: 2008-4-30 - 下午04:47:29
     */
    public static void main(String[] args) throws Exception {
        MonitorService service = new MonitorService();
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
        System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());

        System.out.println("可使用内存=" + monitorInfo.getTotalMemory());
        System.out.println("剩余内存=" + monitorInfo.getFreeMemory());
        System.out.println("最大可使用内存=" + monitorInfo.getMaxMemory());

        System.out.println("操作系统=" + monitorInfo.getOsName());
        System.out.println("总的物理内存=" + monitorInfo.getTotalMemorySize() + "kb");
        System.out.println("剩余的物理内存=" + monitorInfo.getFreeMemory() + "kb");
        System.out.println("已使用的物理内存=" + monitorInfo.getUsedMemory() + "kb");
        System.out.println("线程总数=" + monitorInfo.getTotalThread() + "kb");
    }
}
