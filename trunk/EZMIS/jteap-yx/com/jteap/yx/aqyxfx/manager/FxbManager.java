package com.jteap.yx.aqyxfx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.yx.aqyxfx.model.Fjhjcl;
import com.jteap.yx.aqyxfx.model.Zb;

public class FxbManager extends JdbcManager
{

    public List<Zb> showFjhJclDataByRq(String tableName, String ksrq,String jsrq)
            throws SQLException
    {
        List<Zb> list=new ArrayList<Zb>();
        if (StringUtil.isEmpty(ksrq) && StringUtil.isEmpty(jsrq))
        {
            ksrq = DateUtils.getDate("yyyy-MM-dd");
            jsrq = DateUtils.getDate("yyyy-MM-dd");
        }
        // 3类责任单位,为了好合计，分开求
        String[] zrbms = new String[]
        { "汽机", "锅炉", "电气", "热工", "一值", "二值", "三值", "四值", "五值", "燃运部", "生技部","其他" };
        // 机组列表
        String[] jzStr = new String[]
        { "#1机组", "#2机组", "#3机组", "#4机组" };
        String[] bhStr = new String[]
        { "1", "2", "3", "4" };

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = dataSource.getConnection();
            // 责任单位
            for (int t=0;t<zrbms.length ;t++)
            {
                Fjhjcl ty = new Fjhjcl();
                // 机组编号
                for (int j = 0; j < jzStr.length; j++)  
                {
                        String sql = "select fssj,jssj,jcl from " + tableName+ " where zrbm='" + zrbms[t] + "' and jzbh='"+ jzStr[j] + "' and to_char(txsj,'yyyy-MM-dd') between '"+ ksrq + "'and '"+jsrq+"' and xz='被迫降出力'";
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery(sql);
                        Double curJcl = 0d; // 当前降出力累计
                        Double curValue = 0d;// 当前的累计值
                        int curCs = 0; // 当前累计次数
                        while (resultSet.next())
                        {
                            Date fssj = resultSet.getTimestamp("FSSJ");
                            Date jssj = resultSet.getTimestamp("JSSJ");
                            String jclStr = (String) resultSet.getObject("JCL");
                            if(fssj==null || jssj == null || jclStr == null) {
                                System.out.println(sql);
                            }
                            else{
                            String val = this.compareStringTimes(DateUtils.getDate(fssj, "yyyy-MM-dd HH:MM:ss"), DateUtils.getDate(jssj,"yyyy-MM-dd HH:MM:ss"), "yyyy-MM-dd HH:MM:ss");
                            curValue += Double.parseDouble(val);
                            curJcl += Double.parseDouble(jclStr);
                            curCs++;
                            }
                        }
                            BeanUtils.setProperty(ty, "_" + bhStr[j] + "_cs", curCs);
                            BeanUtils.setProperty(ty, "_" + bhStr[j] + "_sj", curValue);
                            BeanUtils.setProperty(ty, "_" + bhStr[j] + "_jcl", curJcl);
                       
                        if (resultSet != null)
                        {
                            resultSet.close();
                        }
                        if (statement != null)
                        {
                            statement.close();
                        }
                    
                }
                list.add(ty);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            if (statement != null)
            {
                statement.close();
            }
            if (conn != null)
            {
                conn.close();
            }
        }
        return list;
    }

    public String compareStringTimes(String t1, String t2, String parrten)
    {

        SimpleDateFormat formatter = new SimpleDateFormat(parrten);
        ParsePosition pos = new ParsePosition(0);
        ParsePosition pos1 = new ParsePosition(0);
        Date dt1 = formatter.parse(t1, pos);
        Date dt2 = formatter.parse(t2, pos1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt1);
        Long day1 = calendar.getTimeInMillis();
        calendar.setTime(dt2);
        Long day2 = calendar.getTimeInMillis();
        Long val = Math.abs(day2 - day1);
        Double res = val.doubleValue() / (1000 * 3600);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(res);
    }
}
