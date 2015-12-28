package com.jteap.jx.qxgl.actionHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 缺陷申请单　工作流初始化节点事件
 * @author wangyun
 *
 */
public class RyQxdNodeInitEventHandler extends InitNodeEventHandler {

	private static final long serialVersionUID = 1742159021856916726L;

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
			if ("缺陷单申请".equals(taskNode.getName())) {
				status = "申请";
			} else if ("检修班受理".equals(taskNode.getName())) {
				status = "待受理";
			} else if ("检修部确认".equals(taskNode.getName())) {
				status = "待确认";
			} else if ("消缺".equals(taskNode.getName())) {
				String nodeId = taskNode.getId()+"";
				String pid = context.getToken().getProcessInstance().getId()+"";
				String preNodeName = (String)context.getContextInstance().getVariable(nodeId+"_"+pid+"_preNodeName");
				if ("批准延期".equals(preNodeName)) {
					status = "已延期";
				} else {
					status = "待消缺";
					context.getContextInstance().setVariable("YQBZ", "");
					clearYQ(conn, docid);
				}
			} else if ("生技部审批".equals(taskNode.getName())) {
				status = "申请延期";
			} else if ("验收".equals(taskNode.getName())) {
				status = "待验收";
			}
			context.getContextInstance().setVariable("STATUS", status);

			String sql = "update TB_JX_QXGL_QXD set status='"+status+"' where id='"+docid+"'";
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

	private void clearYQ(Connection conn, String docid) throws SQLException {
		String sql = "update TB_JX_QXGL_QXD set YQBZ='' where id='"+docid+"'";
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		st.close();
	}
	
}
