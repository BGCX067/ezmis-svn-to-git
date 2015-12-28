package com.jteap.wz.xqjhsq.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;

@SuppressWarnings( { "serial", "unchecked" })
public class XqjhsqfpxxAction extends AbstractAction {

	private XqjhsqManager xqjhsqManager;
	private XqjhsqDetailManager xqjhsqDetailManager;
	private XqjhsqJDBCManager xqjhsqJDBCManager;

	public XqjhsqJDBCManager getXqjhsqJDBCManager() {
		return xqjhsqJDBCManager;
	}

	public void setXqjhsqJDBCManager(XqjhsqJDBCManager xqjhsqJDBCManager) {
		this.xqjhsqJDBCManager = xqjhsqJDBCManager;
	}

	public XqjhsqDetailManager getXqjhsqDetailManager() {
		return xqjhsqDetailManager;
	}

	public void setXqjhsqDetailManager(XqjhsqDetailManager xqjhsqDetailManager) {
		this.xqjhsqDetailManager = xqjhsqDetailManager;
	}

	/**
	 * 描述 : 查看待生效需求计划 作者 : leichi
	 */
	@SuppressWarnings("unchecked")
	public String showDsxXqjhAction() throws Exception {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(start))
			start = "1";

		int Start = Integer.parseInt(start);
		int Limit = Integer.parseInt(limit);
		
		String jhy = request.getParameter("jhy");
		String sqbm = request.getParameter("sqbm");
		String is_cancel = request.getParameter("is_cancel");
		String xqjhsqbh =  request.getParameter("xqjhsqbh");
		String wzda = request.getParameter("wzda");
		String xhgg = request.getParameter("xhgg");
		try {
			String sql = "SELECT DISTINCT wz_xqjhsq.id,wz_xqjhsq.xqjhsqbh,"
					+ " wz_xqjhsq.gclb,"
					+ " wz_xqjhsq.gcxm,"
					+ " wz_xqjhsq.sqbm,"
					+ " wz_xqjhsq.sqsj,"
					+ " wz_xqjhsq.xqjhqf,"
					+ " wz_xqjhsq.czy,"
					+ " wz_xqjhsq.czyxm,"
					+ " wz_xqjhsq.fpsj,"
					+ " wz_xqjhsq.sqbmmc,"
					+ " wz_xqjhsq.status,"
					+ " wz_xqjhsq.flow_status,"
					+ " wz_xqjhsq.is_back,"
					+ " wz_xqjhsq_detail.is_cancel,"
					+ " wz_xqjhsq_detail.jhy,"
					+ " wz_xqjhsq_detail.wzmc,"
					+ " wz_xqjhsq_detail.xhgg,"
					+ " wz_xqjhsq_detail.sqsl,"
					+ " wz_xqjhsq_detail.jldw,"
					+ " wz_xqjhsq_detail.gjdj"
					+ " FROM tb_wz_xqjhsq wz_xqjhsq, tb_wz_xqjhsq_detail wz_xqjhsq_detail"
					+ " WHERE (wz_xqjhsq.id = wz_xqjhsq_detail.xqjhsqId)"
					+ " and wz_xqjhsq.flow_status = '已完成'"
					+ " and wz_xqjhsq.status = '1'"
					//+ " and wz_xqjhsq_detail.done = '0'"
					+ " and wz_xqjhsq_detail.c_flag = '1'";
			if (StringUtils.isNotEmpty(jhy)) {
				sql += " and (wz_xqjhsq_detail.jhy = '" + jhy + "')";
			}
			if (StringUtils.isNotEmpty(sqbm)) {
				sql += " and (wz_xqjhsq.sqbmmc = '" + sqbm + "')";
			}
			if(StringUtils.isNotEmpty(is_cancel)){
				sql += " and (wz_xqjhsq_detail.is_cancel = '"+is_cancel+"')";
			}
			if(StringUtils.isNotEmpty(xqjhsqbh)){
				sql += " and (wz_xqjhsq.xqjhsqbh like '%"+xqjhsqbh+"%')";
			}
			if(StringUtils.isNotEmpty(wzda)){
				sql += " and (wz_xqjhsq_detail.wzmc = '"+wzda+"')";
			}
			if(StringUtils.isNotEmpty(xhgg)){
				sql += " and (wz_xqjhsq_detail.xhgg like '%"+xhgg+"%')";
			}
			sql += " order by wz_xqjhsq.xqjhsqbh desc";
			Page page = this.pagedQueryTableData(sql,Start,Limit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM","XQJHQF",
							"SQSJ", "CZY", "CZYXM", "IS_BACK", "JHY", "WZMC", "XHGG", "SQSL", "JLDW", "GJDJ", "time",
							"STATUS", "C_LCBH", "GCXMBH", "C_FLAG", "FPSJ",
							"SQBMMC", "FLOW_STATUS","IS_CANCEL"});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 分页查询指定sql数据
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		String countSql = "select count(*) from ("+sql+")";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
		.getBean("dataSource");
		try{
			conn = dataSource.getConnection();
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(countSql);
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			
			if(count<1)
				return new Page();

			sql="SELECT * FROM (SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit-1)+") WHERE RN >= "+start;
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			ResultSetMetaData rsmeta=rs.getMetaData();
			
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmeta.getColumnCount();i++){
					Object obj=rs.getObject(i);
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					obj = StringUtil.clobToStringByDB(obj);
					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID","XQJHQF",
				/*
				 * "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj", "czy",
				 * "czyxm", "time", "status", "clcbh", "gcxmbh", "cflag",
				 * "fpsj", "sqbmmc"
				 */
				"ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM", "SQSJ", "CZY",
				"CZYXM", "time", "STATUS", "IS_BACK", "C_LCBH", "GCXMBH",
				"C_FLAG", "FPSJ", "SQBMMC", "FLOW_STATUS"

		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID","XQJHQF",
				/*
				 * "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj", "czy",
				 * "czyxm", "time", "status", "clcbh", "gcxmbh", "cflag",
				 * "fpsj", "sqbmmc"
				 */
				"ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM", "SQSJ", "CZY",
				"CZYXM", "TIME", "STATUS", "IS_BACK", "CLCBH", "GCXMBH",
				"CFLAG", "FPSJ", "SQBMMC", "FLOW_STATUS"

		};
	}

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

}
