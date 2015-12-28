package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 一般立项单　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class YblxdNodeInitEventHandler extends InitNodeEventHandler {

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
			if ("起草项目".equals(taskNode.getName())) {
				status = "起草项目";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("申请部门主任审批".equals(taskNode.getName())) {
				status = "部门主任审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部审批".equals(taskNode.getName())) {
				status = "生技部审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("主管副总经理审批".equals(taskNode.getName())) {
				status = "主管副总经理审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("总经理审批".equals(taskNode.getName())) {
				status = "总经理审批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			
			String sql = "update TB_HT_YBLXD set status='"+status+"' where id='"+docid+"'";
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
