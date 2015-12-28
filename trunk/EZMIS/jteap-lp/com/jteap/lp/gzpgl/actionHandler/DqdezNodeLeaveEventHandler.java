package com.jteap.lp.gzpgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 电气第二种票 节点离事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class DqdezNodeLeaveEventHandler extends LeaveNodeEventHandler {

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String trName = context.getTransition().getName();
		if ("工作结束".equals(trName)) {
			status = "合格";
			context.getContextInstance().setVariable("STATUS", status);
		}
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update TB_LP_GZP_DQDEZ set status='"+status+"' , shzt = '1' where id='"+docid+"'";
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
