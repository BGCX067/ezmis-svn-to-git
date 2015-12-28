package com.jteap.wz.xqjhsq.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 描述 : 
 * 作者 : caofei
 * 时间 : Oct 19, 2010
 * 参数 : 
 * 返回值 : 
 * 异常 : 
 */
public class XqjhsqNodeInitEventHandler extends InitNodeEventHandler {
	
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
			String flow_status = "";
			if ("填写申请".equals(taskNode.getName())) {
				status = "填写申请";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} 
//			else if ("分场专工签字".equals(taskNode.getName())) {
//				status = "待审批";
//				flow_status = "0";
//				context.getContextInstance().setVariable("FLOW_STATUS", status);
//			} 
			else if ("部门主任签字".equals(taskNode.getName())) {
				status = "部门主任签字";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("生技部专工签字".equals(taskNode.getName())) {
				status = "生技部专工签字";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("生技部主任签字".equals(taskNode.getName())) {
				status = "生技部主任签字";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("主管厂长签字".equals(taskNode.getName())) {
				status = "主管厂长签字";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} 
//			else if ("信息中心主任签字".equals(taskNode.getName())) {
//				status = "待审批";
//				flow_status = "0";
//				context.getContextInstance().setVariable("FLOW_STATUS", status);
//			}
			else if ("物资公司经理签字".equals(taskNode.getName())) {
				status = "物资公司经理签字";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			}

			String sql = "update tb_wz_xqjhsq set flow_status='"+status+"', status='"+flow_status+"' where id='"+docid+"'";
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
