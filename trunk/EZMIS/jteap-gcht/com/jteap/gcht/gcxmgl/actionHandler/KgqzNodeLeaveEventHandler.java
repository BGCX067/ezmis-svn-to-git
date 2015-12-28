package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 开工签证 工作流节点离开事件
 * @author caihuiwen
 *
 */
public class KgqzNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;
	
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String isys = "";
		String trName = context.getTransition().getName();
		if ("项目开工".equals(trName)) {
			status = "已终结";
			isys = "未验收";
			context.getContextInstance().setVariable("STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update TB_HT_KGQZ set status='"+status+"',isys='"+isys+"' where id='"+docid+"'";
			String kgsql = "update TB_HT_WTD set iskg='已开工' where id =(select lxid from TB_HT_KGQZ where id='"+docid+"')";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.executeUpdate(kgsql);
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
