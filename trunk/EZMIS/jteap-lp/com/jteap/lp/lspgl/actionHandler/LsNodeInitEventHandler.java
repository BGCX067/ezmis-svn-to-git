package com.jteap.lp.lspgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;
/**
 * 临时票 节点离事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class LsNodeInitEventHandler extends InitNodeEventHandler{
	@Override
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		TaskNode taskNode = (TaskNode) context.getNode();

		Connection conn = null;
		try {
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			
			String docid = (String)context.getContextInstance().getVariable("docid");
			String status = "";
			if ("开票".equals(taskNode.getName())) {
				status = "临时票票申请";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("临时票批准".equals(taskNode.getName())) {
				status = "待批准";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("安措执行".equals(taskNode.getName())) {
				status = "待许可";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("临时票终结".equals(taskNode.getName())) {
				status = "待终结";
				context.getContextInstance().setVariable("STATUS", status);
			}

			String sql = "update TB_LP_LSP_LSGZP set status='"+status+"' where id='"+docid+"'";
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
