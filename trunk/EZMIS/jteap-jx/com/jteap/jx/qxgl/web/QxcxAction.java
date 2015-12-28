package com.jteap.jx.qxgl.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.qxgl.manager.QxcxManager;

/**
 * 缺陷查询Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings(
{ "unchecked", "serial" })
public class QxcxAction extends AbstractAction
{

    private QxcxManager qxcxManager;
    private DataSource dataSource;

    /**
     * 
     * 描述 : 缺陷查询 作者 : wangyun 时间 : Aug 2, 2010 异常 : Exception
     */
    public String showQxcxAction() throws Exception
    {
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            orderSql = " order by a." + sort + " " + dir;
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);
        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and a.status is not null ");
            sbSql.append("and e.id = any (select max(id) from tb_wf_log group by pi_id) ");
            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and ");
                sbSql.append(hqlWhereTemp);
            }

            if (StringUtil.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }

            String sql = sbSql.toString();
            Page page = qxcxManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProperties());

            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 导出excel表
     */
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

        Connection conn = null;
        try
        {
            conn = this.dataSource.getConnection();

            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and a.status is not null and a.status !='申请' ");
            sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and ");
                sbSql.append(hqlWhereTemp);
            }
            String sql = sbSql.toString();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List list = new ArrayList();
            ResultSetMetaData rsmeta = rs.getMetaData();

            while (rs.next())
            {
                Map map = new HashMap();
                for (int i = 1; i <= rsmeta.getColumnCount(); i++)
                {
                    Object obj = rs.getObject(i);
                    // 针对oracle timestamp日期单独处理，转换成date
                    if (obj instanceof oracle.sql.TIMESTAMP)
                    {
                        obj = ((oracle.sql.TIMESTAMP) obj).dateValue()
                                .toString()
                                + " "
                                + ((oracle.sql.TIMESTAMP) obj).timeValue()
                                        .toString();
                    }

                    map.put(rsmeta.getColumnName(i), obj);
                }
                list.add(map);
            }
            rs.close();

            // 调用导出方法
            export(list, paraHeader, paraDataIndex, paraWidth);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return NONE;
    }
    /**
     * 返回当前统计期 的查询 excel
     * @return
     * @throws Exception
     */
    public String exportExcelByTj() throws Exception{
    	response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  //获取当前年月
        Date curD=DateUtils.getPreDate(new Date());
		String curNian=DateUtils.getDate(curD, "yyyy");
		String curYue=DateUtils.getDate(curD, "MM");
		
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("缺陷查询" + curNian + "-"+curYue+"数据.xls")
						.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileUrl = request.getSession().getServletContext().getRealPath("/");
		try {
			bis = new BufferedInputStream(new FileInputStream(fileUrl+"/userfiles/excel/缺陷查询.xls"));
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (final IOException e) {
			System.out.println("IOException.");
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return NONE;
    }
    @Override
    public HibernateEntityDao getManager()
    {
        return null;
    }

    @Override
    public String[] listJsonProperties()
    {
        return new String[]
        { "ID_", "VERSION_", "START_", "END_", "PROCESSINSTANCE_", "FLOW_NAME",
                "FLOW_CONFIG_ID", "FLOW_FORM_ID", "ZRBM", "SBMC", "QXMC",
                "QXFL", "FXR", "FXSJ", "XQSJ", "STATUS", "ID", "XQSJ",
                "YQXQSJ", "YQBZ", "JZBH", "QXDBH", "JXBQRJG","XQR","SPSJ"};
    }

    @Override
    public String[] updateJsonProperties()
    {
        return null;
    }

    public QxcxManager getQxcxManager()
    {
        return qxcxManager;
    }

    public void setQxcxManager(QxcxManager qxcxManager)
    {
        this.qxcxManager = qxcxManager;
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
