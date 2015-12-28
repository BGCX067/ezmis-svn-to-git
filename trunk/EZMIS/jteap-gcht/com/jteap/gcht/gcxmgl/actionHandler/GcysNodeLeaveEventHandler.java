package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 工程验收 工作流节点离开事件
 * @author caihuiwen
 *
 */
public class GcysNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;
	
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String iszf = "";
		String trName = context.getTransition().getName();
		if ("项目验收".equals(trName)) {
			status = "已终结";
			iszf = "未支付";
			context.getContextInstance().setVariable("STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update TB_HT_YSD set status='"+status+"',iszf='"+iszf+"' where id='"+docid+"'";
			String yssql = "update TB_HT_KGQZ set isys='已验收' where id=(select kgqz_id from TB_HT_YSD where id='"+docid+"')";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.executeUpdate(yssql);
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
