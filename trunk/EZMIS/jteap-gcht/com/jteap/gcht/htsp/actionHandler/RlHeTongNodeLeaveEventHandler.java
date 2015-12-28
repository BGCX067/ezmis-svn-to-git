package com.jteap.gcht.htsp.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 燃料合同审批 工作流节点离开事件
 * @author caihuiwen
 *
 */
public class RlHeTongNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;
	
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String trName = context.getTransition().getName();
		if ("合同生效".equals(trName)) {
			status = "合同生效";
			context.getContextInstance().setVariable("STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update TB_HT_RLHT set status='"+status+"' where id='"+docid+"'";
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
