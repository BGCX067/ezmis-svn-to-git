/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.zbjl.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.MD5;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.yx.zbjl.manager.JbyxfsManager;

/**
 * 交班方式Action
 * 
 * @author caihuiwen
 */
@SuppressWarnings(
{ "serial", "unchecked" })
public class JbyxfsAction extends AbstractAction
{

    private JbyxfsManager jbyxfsManager;
    private PersonManager personManager;
    private DataSource dataSource;

    public void setPersonManager(PersonManager personManager)
    {
        this.personManager = personManager;
    }

    public void setJbyxfsManager(JbyxfsManager jbyxfsManager)
    {
        this.jbyxfsManager = jbyxfsManager;
    }

    @Override
    public HibernateEntityDao getManager()
    {
        return null;
    }

    @Override
    public String[] listJsonProperties()
    {
        return null;
    }

    @Override
    public String[] updateJsonProperties()
    {
        return null;
    }

    /**
     * 根据 表单对应物理表名、值班时间、值班班次、机长号 获取所有交班运行方式
     * 
     * @return
     */
    public String findByFormSnAction()
    {

        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";

        String zbsj = request.getParameter("zbsj");
        String zbbc = request.getParameter("zbbc");
        String gwlb = request.getParameter("gwlb");

        List<String[]> formsList = new ArrayList<String[]>();
        if (gwlb != null)
        {
            String formSn = "";
            if ("值长".equals(gwlb))
            {
                formSn = "TB_YX_FORM_ZZJBYXFS";
            }
            else if ("#1机长".equals(gwlb))
            {
                formSn = "TB_YX_FORM_JZJBYXFS_300";
            }
            else if ("#2机长".equals(gwlb))
            {
                formSn = "TB_YX_FORM_JZJBYXFS_300";
            }
            else if ("#3机长".equals(gwlb))
            {
                formSn = "TB_YX_FORM_JZJBYXFS_600";
            }
            else if ("#4机长".equals(gwlb))
            {
                formSn = "TB_YX_FORM_JZJBYXFS_600";
            }
            else if ("主控".equals(gwlb))
            {
                formSn = "TB_YX_FORM_DQZKJBYXFS";
            }
            else if ("600MW副控".equals(gwlb))
            {
                formSn = "TB_YX_FORM_EQDQJBYXFS";
            }
            else if ("脱硫".equals(gwlb))
            {
                formSn = "TB_YX_FORM_TLJBYXFS";
            }
            else if ("精处理".equals(gwlb))
            {
                formSn = "TB_YX_FORM_JCLJLNJBYXFS";
            }
            formsList.add(new String[]
            { formSn, gwlb });
        }
        else
        {
            formsList.add(new String[]
            { "TB_YX_FORM_ZZJBYXFS", "值长" });
            formsList.add(new String[]
            { "TB_YX_FORM_JZJBYXFS_300", "#1机长" });
            formsList.add(new String[]
            { "TB_YX_FORM_JZJBYXFS_300", "#2机长" });
            formsList.add(new String[]
            { "TB_YX_FORM_JZJBYXFS_600", "#3机长" });
            formsList.add(new String[]
            { "TB_YX_FORM_JZJBYXFS_600", "#4机长" });
            formsList.add(new String[]
            { "TB_YX_FORM_DQZKJBYXFS", "主控" });
            formsList.add(new String[]
            { "TB_YX_FORM_EQDQJBYXFS", "600MW副控" });
            formsList.add(new String[]
            { "TB_YX_FORM_TLJBYXFS", "脱硫" });
            formsList.add(new String[]
            { "TB_YX_FORM_JCLJLNJBYXFS", "精处理" });
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < formsList.size(); i++)
        {
            list.addAll(jbyxfsManager
                    .findByFormSn(formsList.get(i), zbsj, zbbc));
        }

        try
        {
            // 分页
            int startIndex = Integer.parseInt(start);
            int limitIndex = Integer.parseInt(limit) + startIndex;
            if (limitIndex > list.size())
            {
                limitIndex = list.size();
            }
            List<Map<String, Object>> pageList = list.subList(startIndex,
                    limitIndex);

            String json = JSONUtil.listToJson(pageList, new String[]
            { "id", "zbsj", "zbbc", "zbzb", "tianxieren", "tianxieshijian",
                    "gwlb", "formSn", "jizhangtype" });
            json = "{totalCount:'" + list.size() + "',list:" + json + "}";
            this.outputJson(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 根据formSn、时间、班次查询单条记录,返回Id
     * 
     * @return
     */
    public String findOneBySnSjBcAction()
    {
        String formSn = request.getParameter("formSn");
        String zbsj = request.getParameter("zbsj");
        String zbbc = request.getParameter("zbbc");
        String jiZhangHao = request.getParameter("jizhangtype");

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                String id = jbyxfsManager.findJbyxfs(formSn, zbsj, zbbc,
                        jiZhangHao);
                id = id == null ? "" : id;

                this.outputJson("{success:true,id:'" + id + "'}");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return NONE;
    }

    /**
     * 获取最后一条记录
     * 
     * @return
     */
    public String findLasJtAction()
    {
        String formSn = request.getParameter("formSn");
        String jiZhangHao = request.getParameter("jizhangtype");

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                String id = jbyxfsManager.findLastByFormSn(formSn, jiZhangHao);
                id = id == null ? "" : id;

                this.outputJson("{success:true,id:'" + id + "'}");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return NONE;
    }

    /**
     * findTJBAction(通过formSn和查询条件queryParamsSql查询数据，返回json)
     * 
     * @param name
     * @param
     * @return 设定文件
     * @return String DOM对象
     * @Exception 异常对象
     * @since CodingExample Ver(编码范例查看) 1.1
     */
    public String findTJBAction()
    {
        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";
        String formSn = request.getParameter("formSn");
        String sqlWhere = request.getParameter("queryParamsSql");
        String sql = "select * from " + formSn + " a where 1=1 ";
        if (StringUtil.isNotEmpty(sqlWhere))
        {
            String hqlWhereTemp = sqlWhere.replace("$", "%");
            sql += "and" + hqlWhereTemp;
        }
        try
        {
            Page page = jbyxfsManager.pagedQueryTableData(sql, Integer
                    .valueOf(start), Integer.valueOf(limit));
            //查询的list存入session中，方便以后excel导出

            List list=this.jbyxfsManager.querySqlData(sql);
            this.sessionAttrs.put("exportExcelList", list);
            
            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProprties(formSn));
            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return NONE;
    }

    public String[] listJsonProprties(String formSn)
    {
        if ("TB_FORM_SJBZB_BKHDLTJ".equals(formSn))
        {
            return new String[]
            { "ID", "CBR", "JZ", "ZBZB", "ZBBC", "BKHFDL", "YY", "CBSJ" };
        }
        else if ("TB_FORM_SJBZB_FXB".equals(formSn))
        {
            return new String[]
            { "ID","CBR","TXSJ","JZBH","ZRBM","XZ","FSSJ","JLSJ","GLDHSJ","QJCZSJ",
              "JZBWSJ","GLDYSJ","TLTYSJ","JSSJ","TJSJ","YYQM","YYZM","YY","JCL"};
        }
        else if ("TB_FORM_SJBZB_YQFJDL".equals(formSn))
        {
            return new String[]
            { "ID", "CBR", "ZBSJ", "ZBBC", "ZBZB" };
        }
        return new String[]
        { "ID", "CBR", "ZBSJ", "ZBBC", "ZBZB" };
    }

    /**
     * 获取化学监督
     * 
     * @return
     */
    public String findHxjdAction()
    {
        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";

        String formSn = request.getParameter("formSn");
        String beginYmd = request.getParameter("beginYmd");
        String endYmd = request.getParameter("endYmd");
        String tianxieren = request.getParameter("tianxieren");
        String sylx = request.getParameter("sylx");
        String loginName = (String) sessionAttrs
                .get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                Person person = this.personManager
                        .getCurrentPerson(sessionAttrs);
                boolean flag = false;
                Set<P2Role> roleSet = person.getRoles();
                if (!Constants.ADMINISTRATOR_ACCOUNT.equals(loginName))
                {
                    for (P2Role role : roleSet)
                    {
                        // YX_HXJD||YX_HXSY
                        String sn = role.getRole().getRoleSn();
                        if ("YX_HXJD".equals(sn) || "YX_HXSY".equals(sn))
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag)
                    {
                        sylx = "已审阅";
                    }
                }
                List<Map<String, Object>> list = jbyxfsManager.findHxjd(formSn,
                        beginYmd, endYmd, tianxieren, sylx);

                // 分页
                int startIndex = Integer.parseInt(start);
                int limitIndex = Integer.parseInt(limit) + startIndex;
                if (limitIndex > list.size())
                {
                    limitIndex = list.size();
                }
                List<Map<String, Object>> pageList = list.subList(startIndex,
                        limitIndex);

                String[] arrayJson = new String[]
                { "id", "tianxieren", "tianxieshijian", "shenyueren",
                        "shenyueshijian", "status", "qyrq" };
                String json = JSONUtil.listToJson(pageList, arrayJson);
                json = "{totalCount:'" + list.size() + "',list:" + json + "}";
                this.outputJson(json);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 获取当前时间下的化学监督Id
     * 
     * @return
     */
    public String findCurrentHxjdAction()
    {
        String formSn = request.getParameter("formSn");
        String nowYmd = request.getParameter("nowYmd");

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                String id = jbyxfsManager.findCurrentHxjd(formSn, nowYmd);
                id = id == null ? "" : id;

                this.outputJson("{success:true,id:'" + id + "'}");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 保存化学监督审阅信息
     * 
     * @return
     */
    public String saveShenYueAction()
    {
        try
        {
            String userLoginName = request.getParameter("userLoginName");
            String userPassword = request.getParameter("userPassword");
            String id = request.getParameter("id");
            String formSn = request.getParameter("formSn");
            String nowYmdHms = request.getParameter("nowYmdHms");

            // 获得审阅人姓名
            Person person = personManager.findPersonByLoginNameAndPwd(
                    userLoginName, userPassword);
            if (person == null)
            {
                this.outputJson("{success:true,validate:false}");
                return NONE;
            }

            jbyxfsManager.saveShenYue(id, formSn, person.getUserName(),
                    nowYmdHms);
            this.outputJson("{success:true,validate:true}");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 获取运行日志表单
     * 
     * @return
     */
    public String findRunLogAction()
    {
        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";

        String formSn = request.getParameter("formSn");
        String beginYmd = request.getParameter("beginYmd");
        String endYmd = request.getParameter("endYmd");
        String tianxieren = request.getParameter("tianxieren");

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                List<Map<String, Object>> list = jbyxfsManager.findRunLog(
                        formSn, beginYmd, endYmd, tianxieren);

                // 分页
                int startIndex = Integer.parseInt(start);
                int limitIndex = Integer.parseInt(limit) + startIndex;
                if (limitIndex > list.size())
                {
                    limitIndex = list.size();
                }
                List<Map<String, Object>> pageList = list.subList(startIndex,
                        limitIndex);

                String[] arrayJson = new String[]
                { "id", "tianxieren", "tianxieshijian" };
                String json = JSONUtil.listToJson(pageList, arrayJson);
                json = "{totalCount:'" + list.size() + "',list:" + json + "}";
                this.outputJson(json);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 返回前一天尾水电量表码
     * 
     * @throws SQLException
     */
    public String findWsBmAction() throws Exception
    {
        String rq = request.getParameter("rq");
        String sql = "select BM,BM_2 from tb_yx_form_WSJL where to_char(tianxieshijian,'yyyy-mm-dd') = "
                + "to_char(to_date('" + rq + "','yyyy-mm-dd')-1,'yyyy-mm-dd')";
        StringBuffer data = jbyxfsManager.findWsdateStr(sql);
        this.outputJson("{success:true,data:" + data.toString() + "}");
        return NONE;
    }

    /**
     * 获取运行台账表单
     * 
     * @return
     */
    public String findTzFormAction()
    {
        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";

        String formSn = request.getParameter("formSn");
        String beginYmd = request.getParameter("beginYmd");
        String endYmd = request.getParameter("endYmd");
        String tianxieren = request.getParameter("tianxieren");

        if (StringUtil.isNotEmpty(formSn))
        {
            try
            {
                List<Map<String, Object>> list = jbyxfsManager.findTzForm(
                        formSn, beginYmd, endYmd, tianxieren);

                // 分页
                int startIndex = Integer.parseInt(start);
                int limitIndex = Integer.parseInt(limit) + startIndex;
                if (limitIndex > list.size())
                {
                    limitIndex = list.size();
                }
                List<Map<String, Object>> pageList = list.subList(startIndex,
                        limitIndex);

                String[] arrayJson = new String[]
                { "id", "tianxieren", "tianxieshijian" };
                String json = JSONUtil.listToJson(pageList, arrayJson);
                json = "{totalCount:'" + list.size() + "',list:" + json + "}";
                this.outputJson(json);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 获取运行日志 燃料统计表
     * 
     * @return
     */
    public String findRltjAction()
    {
        // 每页大小
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";

        // 分页
        int istart = Integer.parseInt(start);
        int ilimit = Integer.parseInt(limit);

        String formSn = request.getParameter("formSn");
        String beginYmd = request.getParameter("beginYmd");
        String endYmd = request.getParameter("endYmd");
        String nowBc = request.getParameter("nowBc");
        String zbzb = request.getParameter("zbzb");
        String cbr = request.getParameter("cbr");
        String tianxieren = request.getParameter("tianxieren");

        if (StringUtil.isNotEmpty(formSn))
        {
            String schema = SystemConfig.getProperty("jdbc.schema");

            String sql = "SELECT a.*,to_char(a.ZBSJ,'yyyy-MM-dd HH24:mi') CBSJ FROM "
                    + schema + "." + formSn + " a WHERE 1=1";
            if (StringUtil.isNotEmpty(beginYmd))
            {
                sql += " AND to_char(ZBSJ,'yyyy-MM-dd')>='" + beginYmd + "'";
            }
            if (StringUtil.isNotEmpty(endYmd))
            {
                sql += " AND to_char(ZBSJ,'yyyy-MM-dd')<='" + endYmd + "'";
            }
            if (StringUtil.isNotEmpty(nowBc))
            {
                sql += " AND ZBBC='" + nowBc + "'";
            }
            if (StringUtil.isNotEmpty(zbzb))
            {
                sql += " AND ZBZB='" + zbzb + "'";
            }
            if (StringUtil.isNotEmpty(cbr))
            {
                sql += " AND CBR like '%" + cbr + "%'";
            }
            if (StringUtil.isNotEmpty(tianxieren))
            {
                sql += " AND TIANXIEREN like '%" + tianxieren + "%'";
            }
            sql += " ORDER BY ZBSJ asc";

            try
            {
                this.sessionAttrs.put("exportExcelList",this.jbyxfsManager.querySqlData(sql));
                
                Page page = jbyxfsManager.pagedQueryTableData(sql, istart,
                        ilimit);

                String[] arrayJson = new String[]
                { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                        "TIANXIESHIJIAN", "COMBOCBR" };
                if ("TB_YX_FORM_LRLTJ1".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "A1GML", "A2GML",
                            "B1GML", "B2GML", "C1GML", "C2GML", "D1GML",
                            "D2GML", "RYLLJ" };
                }
                else if ("TB_YX_FORM_LRLTJ2".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "A1GML", "A2GML",
                            "B1GML", "B2GML", "C1GML", "C2GML", "D1GML",
                            "D2GML", "RYLLJ" };
                }
                else if ("TB_YX_FORM_LRLTJ3".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "A1GML", "A2GML",
                            "B1GML", "B2GML", "C1GML", "C2GML", "D1GML",
                            "D2GML", "E1GML", "E2GML", "F1GML", "F2GML",
                            "RYLLJ", "JYYB", "HYYB" };
                }
                else if ("TB_YX_FORM_LRLTJ4".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "A1GML", "A2GML",
                            "B1GML", "B2GML", "C1GML", "C2GML", "D1GML",
                            "D2GML", "E1GML", "E2GML", "F1GML", "F2GML",
                            "RYLLJ", "JYYB", "HYYB" };
                }
                else if ("TB_YX_FORM_FDTJ300".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "FDJDL_1", "FDJDL_2" };
                }
                else if ("TB_YX_FORM_FDTJ600".equals(formSn))
                {
                    arrayJson = new String[]
                    { "ID", "CBSJ", "ZBBC", "ZBZB", "CBR", "TIANXIEREN",
                            "TIANXIESHIJIAN", "COMBOCBR", "FDJDL_3", "FDJDL_4" };
                }
                String json = JSONUtil.listToJson((List) page.getResult(),
                        arrayJson);
                json = "{totalCount:'" + page.getTotalCount() + "',list:"
                        + json + "}";
                this.outputJson(json);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 获取当前时间、班次下的燃料Id
     * 
     * @return
     */
    public String findCurrRltjIdAction()
    {
        String formSn = request.getParameter("formSn");
        String nowYmd = request.getParameter("nowYmd");
        String nowBc = request.getParameter("nowBc");
        String ymdStr = nowYmd.split(" ")[0];

        if (StringUtil.isNotEmpty(formSn))
        {
            String schema = SystemConfig.getProperty("jdbc.schema");

            String sql = "SELECT ID FROM " + schema + "." + formSn
                    + " WHERE 1=1";
            if (StringUtil.isNotEmpty(nowYmd))
            {
                sql += " AND TO_CHAR(ZBSJ,'YYYY-MM-DD')='" + ymdStr + "'";
            }
            if (StringUtil.isNotEmpty(nowBc))
            {
                sql += " AND ZBBC='" + nowBc + "'";
            }
            sql += " ORDER BY TIANXIESHIJIAN DESC";
            try
            {
                String id = (String) jbyxfsManager.queryUniqueBySql(sql);
                id = id == null ? "" : id;
                this.outputJson("{success:true,id:'" + id + "'}");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return NONE;
    }

    /**
     * 获取上一天中班的耗煤量、耗油量
     * 
     * @return
     */
    public String findHmlHylAction()
    {
        String formSn = request.getParameter("formSn");
        String curYmd = request.getParameter("curYmd");
        Date curDate = DateUtils.StrToDate(curYmd, "yyyy-MM-dd");
        Date preDate = DateUtils.getPreDate(curDate);
        String preYmd = DateUtils.formatDate(preDate, "yyyy-MM-dd");

        if (StringUtil.isNotEmpty(formSn))
        {
            String schema = SystemConfig.getProperty("jdbc.schema");

            String sql = "SELECT * FROM " + schema + "." + formSn
                    + " WHERE 1=1";
            if (StringUtil.isNotEmpty(preYmd))
            {
                sql += " AND TO_CHAR(ZBSJ,'YYYY-MM-DD')='" + preYmd + "'";
            }
            sql += " AND ZBBC='中班'";
            sql += " ORDER BY TIANXIESHIJIAN DESC";

            try
            {
                List list = jbyxfsManager.querySqlData(sql);
                if (list.size() > 0)
                {
                    this.outputJson("{success:true,data:"
                            + JSONUtil.mapToJson((Map) list.get(0)) + "}");
                }
                else
                {
                    this.outputJson("{success:true}");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return NONE;
    }

    @Override
    public String exportExcel() throws Exception
    {
        // Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
        String paraHeader = request.getParameter("paraHeader");
        // paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");

        // 表索引信息（逗号表达式）
        String paraDataIndex = request.getParameter("paraDataIndex");

        // 宽度(逗号表达式)
        String paraWidth = request.getParameter("paraWidth");
        
       // 调用导出方法 this.sessionAttrs.put("exportExcelList", (List) page.getResult());
        export((List)this.sessionAttrs.get("exportExcelList"), paraHeader, paraDataIndex, paraWidth);
      
        return NONE;
    }
    
    /**
     * 根据用户Id验证密码
     * 
     * @return
     */
    public String validatePassByUIdAction()
    {
        String uId = request.getParameter("uId");
        String uPass = request.getParameter("uPass");

        Person person = personManager.get(uId);
        MD5 md5 = new MD5();
        String md5Pwd = md5.getMD5ofStr(uPass);

        try
        {
            if (person.getUserPwd().equals(md5Pwd))
            {
                this.outputJson("{success:true}");
            }
            else
            {
                this.outputJson("{success:false}");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return NONE;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

}
