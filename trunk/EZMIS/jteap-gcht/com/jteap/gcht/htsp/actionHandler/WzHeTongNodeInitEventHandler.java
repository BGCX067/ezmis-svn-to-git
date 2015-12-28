package com.jteap.gcht.htsp.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 物资合同审批　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class WzHeTongNodeInitEventHandler extends InitNodeEventHandler {

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
			if ("起草合同".equals(taskNode.getName())) {
				status = "起草合同";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("计划经营部副主任审批".equals(taskNode.getName())) {
				status = "申请审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("计划经营部主任审批".equals(taskNode.getName())) {
				status = "计划经营部副主任已审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("总工程师审批".equals(taskNode.getName())) {
				status = "计划经营部主任已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("主管副总经理审批".equals(taskNode.getName())) {
				status = "总工程师已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("总经理授权".equals(taskNode.getName())) {
				status = "主管副总经理已审批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			
			String sql = "update TB_HT_WZHT set status='"+status+"' where id='"+docid+"'";
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
