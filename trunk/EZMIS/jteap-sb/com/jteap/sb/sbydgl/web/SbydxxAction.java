package com.jteap.sb.sbydgl.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.sb.sbydgl.manager.SbydxxManager;

/**
 * 设备异动信息Action
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class SbydxxAction extends AbstractAction {

	private SbydxxManager sbydxxManager;

	public SbydxxManager getSbydxxManager() {
		return sbydxxManager;
	}

	public void setSbydxxManager(SbydxxManager sbydxxManager) {
		this.sbydxxManager = sbydxxManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return sbydxxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "type", "ydbh", "ydmc", "status", "sbbm",
				"sbmc", "ydyy", "ydfa", "ydhyxfs", "sqbm", "sqr", "sqsj",
				"jxbyj", "jxbhsr", "jxbhssj", "fdbyj", "fdbhsr", "fdbhssj",
				"sjbyj", "sjbshr", "sjbshsj", "ldyj", "ldspr", "ldspsj",
				"zxqk", "zxr", "zxsj", "fhr", "fhsj", "remark", "time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "type", "ydbh", "ydmc", "status", "sbbm",
				"sbmc", "ydyy", "ydfa", "ydhyxfs", "sqbm", "sqr", "sqsj",
				"jxbyj", "jxbhsr", "jxbhssj", "fdbyj", "fdbhsr", "fdbhssj",
				"sjbyj", "sjbshr", "sjbshsj", "ldyj", "ldspr", "ldspsj",
				"zxqk", "zxr", "zxsj", "fhr", "fhsj", "remark" };
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String flbm = request.getParameter("flbm");
		String sbbm = request.getParameter("sbbm");
		if (StringUtil.isNotEmpty(flbm)) {
			HqlUtil.addWholeCondition(hql, "sbpjCatalog.flbm like '"+flbm+"%'");
		}else if(StringUtil.isNotEmpty(sbbm)){
			HqlUtil.addWholeCondition(hql, "obj.sbbm = '"+sbbm+"'");
		}
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}
	
	/**
	 * 
	 * 描述 : 获取具体一个流程实例 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : 异常 :
	 */
	public String showProcessinstance() throws Exception {
		String ydglid = request.getParameter("ydglid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select t.processinstance_ from jbpm_variableinstance t where t.name_='docid' and t.stringvalue_='"
					+ ydglid + "'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			String processinstance = "";
			while (rs.next()) {
				processinstance = rs.getString("processinstance_");
			}
			outputJson("{success:true,processinstance:'" + processinstance
					+ "'}");
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}

}
