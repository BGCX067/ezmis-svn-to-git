package com.jteap.sb.sbydgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 异动报告 工作流节点离开事件
 * @author caofei
 *
 */
public class YdbgNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String type = (String) context.getContextInstance().getVariable("TYPE");
		String trName = context.getTransition().getName();
		if ("复核完毕".equals(trName)) {
			status = "已完成";
			context.getContextInstance().setVariable("STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update tb_sb_sbydgl_ydxx set status='"+status+"', type='"+type+"' where id='"+docid+"'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
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
