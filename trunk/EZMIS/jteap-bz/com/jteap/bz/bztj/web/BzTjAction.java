package com.jteap.bz.bztj.web;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.bz.bztj.manager.BzTjManager;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.jx.qxgl.manager.QxtjManager;
@SuppressWarnings({"unchecked","serial"})
public class BzTjAction extends AbstractAction{
	//班组统计处理类
	private BzTjManager bztjManager;
	private QxtjManager qxtjManager;
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return qxtjManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[]{"ID","STRDT","ENDDT","TXR","TXSJ"};
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
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

			String sql = "select * from tb_bz_bztj  a where a.id in (select max(id) from tb_bz_bztj group by TXSJ) ";
			// 添加查询条件
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sql += " and " + hqlWhereTemp;
			}
			//System.out.println(sql);
			Page page = bztjManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			//System.out.println(json);
			outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	 
	/**
	 * 查询班组记录统计
	 * @return
	 * @throws Exception 
	 */
	public String getBzjlCount() throws Exception{
		//开始时间
		String startDt = request.getParameter("tjkssj");
		//结束时间
		String endDt = request.getParameter("tjjssj");
		List list = bztjManager.getbztjCount(startDt,endDt);
		String json = "{success:true,data:";
		String data = JSONUtil.listToJson(list);
		json += data + "}";
		try {
			outputJson(json);
		} catch (Exception e) {
			outputJson("{success:false,msg:'操作失败，请稍后重试！'}");
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 保存班组统计记录
	 * @return
	 * @throws Exception
	 */
	public String saveBztjAction() throws Exception {
		String data = request.getParameter("data");
		String staDt = request.getParameter("tjkssj");
		String endDt = request.getParameter("tjjssj");
		String tbr = request.getParameter("txr");
		Connection conn = null;
		Statement st = null;
		try {
			
//			boolean tjexit = bztjManager.findByqssj(staDt,endDt);
//			if (tjexit) {
//				outputJson("{success:false,msg:'本时间段已经做过班组统计'}");
//				return NONE;
//			}
			DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
			conn =   dataSource.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			List lst = JSONUtil.parseList(data);

			// 遍历每个项目名称的MAP
			for (int i = 0; i < lst.size(); i++) {
				Map<String, String> map = (Map<String, String>) lst.get(i);
				if (map.size() > 0) {
				 
					StringBuffer sb = new StringBuffer();
					sb.append("insert into TB_BZ_BZTJ t(id,");
					for(int k=1;k<=14;k++){
						sb.append("sj"+k+",");
					}
					sb.append("strdt,enddt,jh,txr,");
					sb.append("bz,txsj) ");
					
					String id = UUIDGenerator.hibernateUUID();
					sb.append("values('");
					sb.append(id);
					sb.append("',");
					for (String key : map.keySet()) {
						if(key.indexOf("SJ_")!=-1){
							sb.append("'");
							sb.append(map.get(key));
							sb.append("',");
						}
					}
					sb.append("to_date('"+staDt+"','yyyy-mm-dd'),to_date('"+endDt+"','yyyy-mm-dd'),'"+bztjManager.getJhDt(staDt, endDt)+"','"+tbr+"','"+map.get("bzid")+"',sysdate)");
					st.addBatch(sb.toString());
				}
			}
			st.executeBatch();
			conn.commit();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			conn.rollback();
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}

	public BzTjManager getBztjManager() {
		return bztjManager;
	}

	public void setBztjManager(BzTjManager bztjManager) {
		this.bztjManager = bztjManager;
	}

	public QxtjManager getQxtjManager() {
		return qxtjManager;
	}

	public void setQxtjManager(QxtjManager qxtjManager) {
		this.qxtjManager = qxtjManager;
	}
	
}
