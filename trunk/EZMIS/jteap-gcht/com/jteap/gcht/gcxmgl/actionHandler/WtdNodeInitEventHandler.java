package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 委托单　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class WtdNodeInitEventHandler extends InitNodeEventHandler {

	private static final long serialVersionUID = 1742159021856916726L;
	//起草项目,申请部门领导审批,生技部专工审批,生技部主任审批,计划部专工审批
	//,计划部主任审批,二期计划部主任审批,总工程师审批,分管副总经理审批,总经理审批
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
			} else if ("申请部门领导审批".equals(taskNode.getName())) {
				status = "申请部门领导审批";
			} else if ("生技部专工审批".equals(taskNode.getName())) {
				status = "生技部专工审批";
			}else if ("生技部主任审批".equals(taskNode.getName())) {
				status = "生技部主任审批";
			}else if ("计划部专工审批".equals(taskNode.getName())) {
				status = "计划部专工审批";
			}else if ("计划部主任审批".equals(taskNode.getName())) {
				status = "计划部主任审批";
			}else if ("二期计划部主任审批".equals(taskNode.getName())) {
				status = "二期计划部主任审批";
			}else if ("总工程师审批".equals(taskNode.getName())) {
				status = "总工程师审批";
			}else if ("总经济师审批".equals(taskNode.getName())) {
				status = "总经济师审批";
			} else if ("分管副总经理审批".equals(taskNode.getName())) {
				status = "分管副总经理审批";
			} else if ("总经理审批".equals(taskNode.getName())) {
				status = "总经理审批";
			}
			context.getContextInstance().setVariable("STATUS", status);
			String sql = "update TB_HT_WTD set status='"+status+"' where id='"+docid+"'";
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
