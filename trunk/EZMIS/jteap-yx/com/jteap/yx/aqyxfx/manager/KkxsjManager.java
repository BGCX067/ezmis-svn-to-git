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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.yx.aqyxfx.model.Fjhty;
import com.jteap.yx.aqyxfx.model.Zb;

public class KkxsjManager extends JdbcManager
{
    /**
     * 根据日期查询数据 作者:童贝 时间:Mar 2, 2011
     * 
     * @param rq
     * @return
     * @throws SQLException
     */
    public List<Zb> showFjhtyDataByRq(String tableName, String ksrq, String jsrq)
            throws SQLException
    {
        List<Zb> zbList = new ArrayList<Zb>();
        if (StringUtil.isEmpty(ksrq) && StringUtil.isEmpty(jsrq))
        {
            ksrq = DateUtils.getDate("yyyy-MM-dd");
            jsrq = DateUtils.getDate("yyyy-MM-dd");
        }
        // 3类责任单位,为了好合计，分开求
        String[] zrbms = new String[]
        { "汽机", "锅炉", "电气", "热工", "一值", "二值", "三值", "四值", "五值", "燃运部", "生技部",
                "其他" };
        String[] jxbStr = new String[]
        { "汽机", "锅炉", "电气", "热工" };
        String[] fdbStr = new String[]
        { "一值", "二值", "三值", "四值", "五值" };
        String[] tqStr = new String[]
        { "燃运部", "生技部", "其他" };
        // 性质列表
        String[] xzStr = new String[]
        { "强迫停运", "考核非停" };
        String[] xsStr = new String[]
        { "xs", "ftxs" };
        String[] csStr = new String[]
        { "cs", "ftcs" };
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
            Map<String, Fjhty> fjMap = new HashMap<String, Fjhty>();
            // 责任单位
            for (String zrbm : zrbms)
            {
                Fjhty ty = new Fjhty();
                // 性质
                for (int i = 0; i < xzStr.length; i++)
                {
                    Integer cs = 0;// 次数累计
                    // 机组编号
                    for (int j = 0; j < jzStr.length; j++)
                    {
                        String sql = "select fssj,jssj from " + tableName
                                + " where zrbm='" + zrbm + "' and xz='"
                                + xzStr[i] + "' and jzbh='" + jzStr[j]
                                + "' and txsj between " + " to_date('" + ksrq
                                + "','yyyy-mm-dd') and to_date('" + jsrq
                                + "','yyyy-mm-dd')";
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery(sql);
                        Double curValue = 0d;// 当前的累计值
                        int curCs = 0; // 当前累计次数
                        while (resultSet.next())
                        {
                            Date fssj = resultSet.getTimestamp("FSSJ");
                            Date jssj = resultSet.getTimestamp("JSSJ");
                            if(fssj !=null &&jssj !=null){
                            String val = this.compareStringTimes(DateUtils
                                    .getDate(fssj, "yyyy-MM-dd HH:MM:ss"),
                                    DateUtils.getDate(jssj,
                                            "yyyy-MM-dd HH:MM:ss"),
                                    "yyyy-MM-dd HH:MM:ss");
                            curValue += Double.parseDouble(val);
                            curCs++;}
                        }
                        cs += curCs;
                        BeanUtils
                                .setProperty(ty, xsStr[i] + bhStr[j], curValue);
                        if (resultSet != null)
                        {
                            resultSet.close();
                        }
                        if (statement != null)
                        {
                            statement.close();
                        }
                    }
                    BeanUtils.setProperty(ty, csStr[i], cs.toString());
                }
                fjMap.put(zrbm, ty);
            }
            // 合计
            Fjhty jxbFj = getHjFjhty(jxbStr, fjMap);
            Fjhty fdbFj = getHjFjhty(fdbStr, fjMap);
            Fjhty tqFj = getHjFjhty(tqStr, fjMap);
            // 全厂合计
            Fjhty qchj = new Fjhty();
            qchj.setCs(jxbFj.getCs() + fdbFj.getCs() + tqFj.getCs());
            qchj.setFtcs(jxbFj.getFtcs() + fdbFj.getFtcs() + tqFj.getFtcs());
            qchj
                    .setFtxs1(jxbFj.getFtxs1() + fdbFj.getFtxs1()
                            + tqFj.getFtxs1());
            qchj
                    .setFtxs2(jxbFj.getFtxs2() + fdbFj.getFtxs2()
                            + tqFj.getFtxs2());
            qchj
                    .setFtxs3(jxbFj.getFtxs3() + fdbFj.getFtxs3()
                            + tqFj.getFtxs3());
            qchj
                    .setFtxs4(jxbFj.getFtxs4() + fdbFj.getFtxs4()
                            + tqFj.getFtxs4());
            qchj.setXs1(jxbFj.getXs1() + fdbFj.getXs1() + tqFj.getXs1());
            qchj.setXs2(jxbFj.getXs2() + fdbFj.getXs2() + tqFj.getXs2());
            qchj.setXs3(jxbFj.getXs3() + fdbFj.getXs3() + tqFj.getXs3());
            qchj.setXs4(jxbFj.getXs4() + fdbFj.getXs4() + tqFj.getXs4());
            qchj.setCsbfb(1d);
            qchj.setXsbfb(1d);
            Double qccs = qchj.getCs() + qchj.getFtcs();
            Double qcsj = qchj.getXs1() + qchj.getXs2() + qchj.getXs3()
                    + qchj.getXs4() + qchj.getFtxs1() + qchj.getFtxs2()
                    + qchj.getFtxs3() + qchj.getFtxs4();

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            // 设置次数百分比，时间百分比
            for (String zrbm : zrbms)
            {
                Fjhty curTy = fjMap.get(zrbm);
                if (qccs.doubleValue() != 0d)
                {
                    Double csbfb = (curTy.getCs() + curTy.getFtcs()) / qccs;
                    csbfb = Double.parseDouble(decimalFormat.format(csbfb));
                    curTy.setCsbfb(csbfb);
                }
                if (qcsj.doubleValue() != 0d)
                {
                    Double sjbfb = (curTy.getXs1() + curTy.getXs2()
                            + curTy.getXs3() + curTy.getXs4()
                            + curTy.getFtxs1() + curTy.getFtxs2()
                            + curTy.getFtxs3() + curTy.getFtxs4())
                            / qcsj;
                    sjbfb = Double.parseDouble(decimalFormat.format(sjbfb));
                    curTy.setXsbfb(sjbfb);
                }
            }
            // 设置合计的次数百分比和时间百分比
            if (qccs.doubleValue() != 0d)
            {
                Double jxbcsbfb = (jxbFj.getCs() + jxbFj.getFtcs()) / qccs;
                jxbcsbfb = Double.parseDouble(decimalFormat.format(jxbcsbfb));
                jxbFj.setCsbfb(jxbcsbfb);

                Double fdbcsbfb = (fdbFj.getCs() + fdbFj.getFtcs()) / qccs;
                fdbcsbfb = Double.parseDouble(decimalFormat.format(fdbcsbfb));
                fdbFj.setCsbfb(fdbcsbfb);
            }

            if (qcsj.doubleValue() != 0d)
            {
                Double jxbsjbfb = (jxbFj.getXs1() + jxbFj.getXs2()
                        + jxbFj.getXs3() + jxbFj.getXs4() + jxbFj.getFtxs1()
                        + jxbFj.getFtxs2() + jxbFj.getFtxs3() + jxbFj
                        .getFtxs4())
                        / qcsj;
                jxbsjbfb = Double.parseDouble(decimalFormat.format(jxbsjbfb));
                jxbFj.setXsbfb(jxbsjbfb);

                Double fdbsjbfb = (fdbFj.getXs1() + fdbFj.getXs2()
                        + fdbFj.getXs3() + fdbFj.getXs4() + fdbFj.getFtxs1()
                        + fdbFj.getFtxs2() + fdbFj.getFtxs3() + fdbFj
                        .getFtxs4())
                        / qcsj;
                fdbsjbfb = Double.parseDouble(decimalFormat.format(fdbsjbfb));
                fdbFj.setXsbfb(fdbsjbfb);
            }

            // 把记录放入最后的结果集中
            for (String jxb : jxbStr)
            {
                zbList.add(fjMap.get(jxb));
            }
            zbList.add(jxbFj);
            for (String fdb : fdbStr)
            {
                zbList.add(fjMap.get(fdb));
            }
            zbList.add(fdbFj);
            for (String tq : tqStr)
            {
                zbList.add(fjMap.get(tq));
            }
            zbList.add(qchj);
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
        return zbList;
    }

    /**
     * 得到合计记录 作者:童贝 时间:Mar 2, 2011
     * 
     * @param zrbm
     * @param map
     * @return
     */
    public Fjhty getHjFjhty(String[] zrbms, Map<String, Fjhty> map)
    {
        Fjhty fjhty = new Fjhty();
        for (String zrbm : zrbms)
        {
            Fjhty curTy = map.get(zrbm);
            fjhty.setCs(fjhty.getCs() + curTy.getCs());
            fjhty.setFtcs(fjhty.getFtcs() + curTy.getFtcs());
            fjhty.setFtxs1(fjhty.getFtxs1() + curTy.getFtxs1());
            fjhty.setFtxs2(fjhty.getFtxs2() + curTy.getFtxs2());
            fjhty.setFtxs3(fjhty.getFtxs3() + curTy.getFtxs3());
            fjhty.setFtxs4(fjhty.getFtxs4() + curTy.getFtxs4());
            fjhty.setXs1(fjhty.getXs1() + curTy.getXs1());
            fjhty.setXs2(fjhty.getXs2() + curTy.getXs2());
            fjhty.setXs3(fjhty.getXs3() + curTy.getXs3());
            fjhty.setXs4(fjhty.getXs4() + curTy.getXs4());
        }
        return fjhty;
    }

    /**
     * 返回两个时间之间相差的小时数 作者:童贝 时间:Mar 2, 2011
     * 
     * @param t1
     * @param t2
     * @param parrten
     * @return
     */
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

    public static void main(String[] args)
    {
        // 2.465
        // System.out.println(KkxsjManager.compareStringTimes("2010-01-12
        // 11:29:24" ,"2010-01-12 13:29:25","yyyy-MM-dd HH:MM:ss"));
    }
}
