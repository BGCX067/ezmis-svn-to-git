package com.jteap.wz.gdzc.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 描述 : 
 * 作者 : 
 * 时间 : 
 * 参数 : 
 * 返回值 : 
 * 异常 : 
 */
public class GdzcdjNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String flow_status = "";
		String trName = context.getTransition().getName();
		if ("end".equals(trName)) {
			status = "已完成";
			flow_status = "1";
			context.getContextInstance().setVariable("FLOW_STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String xqjhsqSql = "update TB_WZ_GDZCDJ set flow_status='"+status+"', zt='"+flow_status+"' where id='"+docid+"'";
			//String xqjhsqDetailSql = "update tb_wz_xqjhsq_detail set c_flag='0' where xqjhsqid='"+docid+"'";
			Statement st = conn.createStatement();
			st.executeUpdate(xqjhsqSql);
			//st.executeUpdate(xqjhsqDetailSql);
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
