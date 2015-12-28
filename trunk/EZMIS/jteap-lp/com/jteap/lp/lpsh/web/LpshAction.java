package com.jteap.lp.lpsh.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.lp.lpsh.manager.LpshManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 两票审核Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class LpshAction extends AbstractAction {

	private LpshManager lpshManager;
	private DictManager dictManager;
	private DataSource dataSource;

	/**
	 * 描述 : 两票显示列表
	 * 作者 : wangyun
	 * 时间 : 2010-10-26
	 * 异常 : Exception
	 */
	public String showListAction() throws Exception {
		String pzid = request.getParameter("pzid");
		String pfl = request.getParameter("pfl");

		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		String sqlWhere = request.getParameter("queryParamsSql");
		String hqlWhereTemp = "";
		if (StringUtils.isNotEmpty(sqlWhere)) {
			hqlWhereTemp = sqlWhere.replace("$", "%");
		}

		try {
			// 操作票票种对应数据库表名
			List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_CZP2TABLE");
			// 工作票票种对应数据库表名
			List<Dict> lstGzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_GZP2TABLE");
			//临时票票种对应数据库表名
			List<Dict> lstLsp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
			// 全部票种
			List<Dict> lstAll = new ArrayList<Dict>();

			String sql = "";
			// 如果pzid为空，则查看全厂票
			if (StringUtil.isEmpty(pzid)) {
				lstAll.addAll(lstGzp);
				lstAll.addAll(lstCzp);
				lstAll.addAll(lstLsp);
				sql = joinSql(lstAll, hqlWhereTemp);
			// 否则查看各分类票
			// 如果是工作票，则查看所有的工作票
			} else if ("gzp".equals(pzid)){
				sql = joinSql(lstGzp, hqlWhereTemp);
				// 如果是操作票，则查看所有的操作票
			} else if ("czp".equals(pzid)) {
				sql = joinSql(lstCzp, hqlWhereTemp);
			} else if("lsp".equals(pzid)){
				sql = joinSql(lstLsp, hqlWhereTemp);
				
			}else {
				Dict dict = null;
				if ("gzp".equals(pfl)) {
					dict = dictManager.findDictByCatalogNameWithKey("LP_GZP2TABLE", pzid);
				} else if("lsp".equals(pfl)){
					dict = dictManager.findDictByCatalogNameWithKey("LP_LSP2TABLE", pzid);
				}else{
					dict = dictManager.findDictByCatalogNameWithKey("LP_CZP2TABLE", pzid);
				}
				String tableName = dict.getValue();
				sql = joinSql(tableName, pzid, hqlWhereTemp);
			}
			sql = "select * from (" + sql + ")";
			Page page = lpshManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), 
					new String[] { "ID", "PH", "BZMC", "FZRMC", "STATUS",
					"JHKSSJ", "JHJSSJ", "SHBM", "SHR", "SHSJ", "PMC", "PZ", "SHZT", "TABLENAME"});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 保存审核信息
	 * 作者 : wangyun
	 * 时间 : 2010-10-27
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String shbm = request.getParameter("shbm");
		String shr = request.getParameter("shr");
		String shsj = request.getParameter("shsj");
		String shzt = request.getParameter("shzt");
		String pz = request.getParameter("pz");
		String tablename = request.getParameter("tablename");
		String status = ("0".equals(shzt)?"不合格":"合格");

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("update ");
		sbSql.append(tablename);
		sbSql.append(" set shbm='");
		sbSql.append(shbm);
		sbSql.append("' , shr='");
		sbSql.append(shr);
		sbSql.append("' , shsj=to_date('");
		sbSql.append(shsj);
		sbSql.append("','yyyy-MM-dd') , shzt='");
		sbSql.append(shzt);
		sbSql.append("' , pz='");
		sbSql.append(pz);
		sbSql.append("', status='");
		sbSql.append(status);
		sbSql.append("' where id='");
		sbSql.append(id);
		sbSql.append("'");

		lpshManager.executeSqlBatch(sbSql.toString());

		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 
	 * 描述 : 根据业务ID获得流程ID
	 * 作者 : wangyun
	 * 时间 : 2010-10-27
	 * 异常 : Exception
	 */
	public String getPidAction() throws Exception {
		Connection conn = null;
		try {
			String id = request.getParameter("id");

			conn = dataSource.getConnection();
			String sql = "select PROCESSINSTANCE_ from JBPM_VARIABLEINSTANCE where NAME_='docid' and STRINGVALUE_= '"+id+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String pid = "";
			if (rs.next()) {
				pid = rs.getString(1);
			}
			rs.close();
			st.close();
			outputJson("{success : true, pid :'"+pid+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}

	/**
	 * 导出Excel动作
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public String exportExcel() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = WebUtils.getRequestParam(request, "paraHeader");

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");

		String pzid = request.getParameter("pzid");
		String pfl = request.getParameter("pfl");

		String sqlWhere = request.getParameter("queryParamsSql");
		String hqlWhereTemp = "";
		if (StringUtils.isNotEmpty(sqlWhere)) {
			hqlWhereTemp = sqlWhere.replace("$", "%");
		}

		try {
			// 操作票票种对应数据库表名
			List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_CZP2TABLE");
			// 工作票票种对应数据库表名
			List<Dict> lstGzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_GZP2TABLE");
			// 全部票种
			List<Dict> lstAll = new ArrayList<Dict>();
			//临时票票种对应数据库表名
			List<Dict> lstLsp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
			String sql = "";
			// 如果pzid为空，则查看全厂票
			if (StringUtil.isEmpty(pzid)) {
				lstAll.addAll(lstGzp);
				lstAll.addAll(lstCzp);
				lstAll.addAll(lstLsp);
				sql = joinSql(lstAll, hqlWhereTemp);
			// 否则查看各分类票
			// 如果是工作票，则查看所有的工作票
			} else if ("gzp".equals(pzid)){
				sql = joinSql(lstGzp, hqlWhereTemp);
				// 如果是操作票，则查看所有的操作票
			} else if ("czp".equals(pzid)) {
				sql = joinSql(lstCzp, hqlWhereTemp);
			} else if("lsp".equals(pzid)){
				sql = joinSql(lstLsp, hqlWhereTemp);
				
			}else {
				Dict dict = null;
				if ("gzp".equals(pfl)) {
					dict = dictManager.findDictByCatalogNameWithKey("LP_GZP2TABLE", pzid);
				} else if("lsp".equals(pfl)){
					dict = dictManager.findDictByCatalogNameWithKey("LP_LSP2TABLE", pzid);
				} else {
					dict = dictManager.findDictByCatalogNameWithKey("LP_CZP2TABLE", pzid);
				}
				String tableName = dict.getValue();
				sql = joinSql(tableName, pzid, hqlWhereTemp);
			}
			sql = "select * from (" + sql + ")";
			List list = lpshManager.querySqlData(sql);

			// 调用导出方法
			export(list, paraHeader, paraDataIndex, paraWidth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 根据表名和查询条件 拼接sql语句
	 * 作者 : wangyun
	 * 时间 : 2010-10-27
	 * 参数 : 
	 * 		lstDict ： 票种对应的表名集合
	 * 		hqlWhereTemp ： 查询条件
	 * 返回值 : 
	 * 		sql : sql语句
	 */
	private String joinSql(List<Dict> lstDict, String hqlWhereTemp) {
		String sql = "";
		for (Dict dict : lstDict) {
			String nm = dict.getKey();
			String tableName = dict.getValue();
			sql += "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(c.start_, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
				+ "to_char(c.end_,'yyyy-MM-dd HH24:mi') as JHJSSJ, a.SHBM, a.SHR, to_char(a.shsj, 'yyyy-MM-dd') as SHSJ, a.PZ, a.SHZT, b.PMC, '"
				+ tableName+"' as TABLENAME "
				+ "from " + tableName + " a, "
				+ "TB_LP_LPPK b ,"
				+"jbpm_processinstance c,"
				+"jbpm_variableinstance d "
				+"where d.name_ = 'docid' "
				+"and d.stringvalue_ = a.id "
				+"and d.processinstance_ = c.id_ "
				+ "and (a.status = '合格' or a.status = '不合格') and b.nm = '"
				+ nm
				+ "' ";
			if (StringUtil.isNotEmpty(hqlWhereTemp)) {
				sql += " and " + hqlWhereTemp;
			}
			sql += " union all ";
		}
		sql = sql.substring(0, sql.lastIndexOf("union all "));
		return sql;
	}

	/**
	 * 
	 * 描述 : 根据表名和查询条件 拼接sql语句
	 * 作者 : wangyun
	 * 时间 : 2010-10-27
	 * 参数 : 
	 * 		tableName ： 票种对应的表名
	 * 		hqlWhereTemp ： 查询条件
	 * 返回值 : 
	 * 		sql : sql语句
	 */
	private String joinSql(String tableName, String nm, String hqlWhereTemp) {
	String sql = "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(c.start_, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
			+ "to_char(c.end_,'yyyy-MM-dd HH24:mi') as JHJSSJ, a.SHBM, a.SHR, to_char(a.shsj, 'yyyy-MM-dd') as SHSJ, a.PZ, a.SHZT, b.PMC, '"
			+ tableName+"' as TABLENAME "
			+ "from " + tableName + " a, "
			+ "TB_LP_LPPK b ,"
			+"jbpm_processinstance c,"
			+"jbpm_variableinstance d "
			+"where d.name_ = 'docid' "
			+"and d.stringvalue_ = a.id "
			+"and d.processinstance_ = c.id_ "
			+ "and (a.status = '合格' or a.status = '不合格') and b.nm = '"
			+ nm
			+ "' ";
		 if (StringUtil.isNotEmpty(hqlWhereTemp)) {
			 sql += " and " + hqlWhereTemp;
		 }
		 return sql;
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public LpshManager getLpshManager() {
		return lpshManager;
	}

	public void setLpshManager(LpshManager lpshManager) {
		this.lpshManager = lpshManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
