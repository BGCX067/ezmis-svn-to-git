package com.jteap.jx.qxgl.web;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.qxgl.manager.QxcxManager;
import com.jteap.jx.qxgl.manager.QxtjManager;

/**
 * 缺陷统计Action
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class QxtjActoin extends AbstractAction {

	private QxtjManager qxtjManager;
	private QxcxManager qxcxManager;
	private DataSource dataSource;

	
	@Override
	public String showListAction() throws Exception {
		try {
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";

			int iStart = Integer.parseInt(start);
			int iLimit = Integer.parseInt(limit);
			String fl = request.getParameter("fl");
			String sql = "select * from TB_JX_QXGL_QXTJ  a where a.id in (select max(t.id) from TB_JX_QXGL_QXTJ t where t.fl='"+fl+"' group by dctjq)";
			// 添加查询条件
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sql += " and " + hqlWhereTemp;
			}
			sql += "   order by a.dctjq desc";
			Page page = qxcxManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 缺陷统计查询
	 * 作者 : wangyun
	 * 时间 : Aug 4, 2010
	 * 异常 : Exception
	 */
	public String qxtjQueryAction() throws Exception {
		String dctjq = request.getParameter("dctjq");
		String fl = request.getParameter("fl");
		try {
			Map map = qxtjManager.getJsonByTjIdAndFl(dctjq, fl);
			String json = "";
			if(map!=null){
				json = "{success:true,data:["+JSONUtil.mapToJson(map)+"]}";
			}else{
				json = "{success:false,msg:'无此统计期的缺陷统计单'}";
			}
			//System.out.println(json);
			outputJson(json);	 
			//System.out.println(json);
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请与管理员联系'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 统计缺陷
	 * 作者 : wangyun
	 * 时间 : Aug 4, 2010
	 * 异常 : Exception
	 */
	public String getQxtjAction() throws Exception {
			String strdt = request.getParameter("strdt");
			String enddt = request.getParameter("enddt");
			String fl = request.getParameter("fl");
			List lst = null;
			if(fl.equals("cn")){
				lst = qxtjManager.findQxtj(strdt, enddt,null);
			}else{
				lst = qxtjManager.findQxtjByDljt(strdt, enddt,null);
			}
			
			String json = "{success:true,data:";
			String data = JSONUtil.listToJson(lst);
			json += data + "}";
			outputJson(json);
		return NONE;
	}
	
	public String testQxtj(){
		qxtjManager.saveQxtj("cn");
		qxtjManager.saveQxtj("xq");
		return NONE;
	}
//	/**
//	 * 
//	 * 描述 : 保存缺陷统计
//	 * 作者 : wangyun
//	 * 时间 : Aug 5, 2010
//	 * 异常 : Exception
//	 */
//	public String saveQxtjAction() throws Exception {
//		String data = request.getParameter("data");
//		String tbr = request.getParameter("tbr");
//		String byxqqk = request.getParameter("byxqqk");
//		String byzjqxqk = request.getParameter("byzjqxqk");
//		String ywzjqxxqqk = request.getParameter("ywzjqxxqqk");
//		Connection conn = null;
//		try {
//			// 查询是否做过缺陷本月缺陷统计
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//			String dctjq = sdf.format(new Date());
//			List<Qxtj> lstQxtj = qxtjManager.findByDctjq(dctjq);
//			if (lstQxtj.size() > 0) {
//				outputJson("{success:false,msg:'本月已经做过缺陷统计'}");
//				return NONE;
//			}
//			
//			conn = this.dataSource.getConnection();
//			conn.setAutoCommit(false);
//
//			List lst = JSONUtil.parseList(data);
//			List<String> lstSql = new ArrayList<String>();
//
//			// 遍历每个项目名称的MAP
//			for (int i = 0; i < lst.size(); i++) {
//				Map<String, String> map = (Map<String, String>) lst.get(i);
//				if (map.size() > 0) {
//					// sql语句字段名list
//					List<String> lstKey = new ArrayList<String>();
//					// sql语句值list
//					List<String> lstValue = new ArrayList<String>();
//					// 项目名称
//					String xmmc = "";
//					String zy = "";
//					for (String key : map.keySet()) {
//						String[] item = key.split("_");
//						if (item.length == 2) {
//							xmmc = item[0];
//							zy = item[1];
//						} else {
//							xmmc = item[0]+"_"+item[1];
//							zy = item[2];
//						}
//						lstKey.add(zy);
//						String value = map.get(key);
//						lstValue.add(value);
//					}
//
//					StringBuffer sb = new StringBuffer();
//					sb.append("insert into TB_JX_QXGL_QXTJ t(id,");
//					for (int j = 0; j < lstKey.size(); j++) {
//						String key = lstKey.get(j);
//						sb.append(key);
//						sb.append(",");
//					}
//					sb.append("tbr,DCTJQ,TJSJ,XMMC,BYXQQK,BYZJQXQK,YWZJQXXQQK) ");
//					
//					String id = UUIDGenerator.hibernateUUID();
//					sb.append("values('");
//					sb.append(id);
//					sb.append("',");
//					for (int j = 0; j < lstValue.size(); j++) {
//						String value = lstValue.get(j);
//						sb.append("'");
//						sb.append(value);
//						sb.append("',");
//					}
//					sb.append("'");
//					sb.append(tbr);
//					sb.append("','");
//					sb.append(dctjq);
//					sb.append("',sysdate,'");
//					sb.append(xmmc);
//					sb.append("','");
//					sb.append(byxqqk);
//					sb.append("','");
//					sb.append(byzjqxqk);
//					sb.append("','");
//					sb.append(ywzjqxxqqk);
//					sb.append("')");
//					
//					lstSql.add(sb.toString());
//				}
//			}
//			operateDb(conn, lstSql);
//			conn.commit();
//			this.outputJson("{success:true}");
//		} catch (Exception e) {
//			this.outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
//			conn.rollback();
//			e.printStackTrace();
//		} finally {
//			if (conn != null) {
//				conn.close();
//			}
//		}
//		return NONE;
//	}

//	/**
//	 * 
//	 * 描述 : 操作DB
//	 * 作者 : wangyun
//	 * 时间 : Aug 6, 2010
//	 * 参数 : 
//	 * 		conn ： 数据库连接
//	 * 		lstSql ： sql列表
//	 * 返回值 : 
//	 * 异常 : SQLException
//	 */
//	private void operateDb(Connection conn, List<String> lstSql) throws SQLException {
//		try {
//			Statement st = conn.createStatement();
//			for (String sql : lstSql) {
//				st.executeUpdate(sql);
//			}
//			st.close();
//		} catch (SQLException e) {
//			throw e;
//		}
//	}
	 
	@Override
	public HibernateEntityDao getManager() {
		return qxtjManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] {"ID","DCTJQ","TBR","TJSJ"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public QxtjManager getQxtjManager() {
		return qxtjManager;
	}

	public void setQxtjManager(QxtjManager qxtjManager) {
		this.qxtjManager = qxtjManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public QxcxManager getQxcxManager() {
		return qxcxManager;
	}

	public void setQxcxManager(QxcxManager qxcxManager) {
		this.qxcxManager = qxcxManager;
	}

}
