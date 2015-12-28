package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 开工签证　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class KgqzNodeInitEventHandler extends InitNodeEventHandler {

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
			if ("项目负责部门起草".equals(taskNode.getName())) {
				status = "起草项目";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("项目负责部门主任审批".equals(taskNode.getName())) {
				status = "项目负责部门主任审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("计划经营部审批".equals(taskNode.getName())) {
				status = "计划经营部审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("安监部审批".equals(taskNode.getName())) {
				status = "安监部审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("综合事务部审批".equals(taskNode.getName())) {
				status = "综合事务部审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("生技部指定技术负责人".equals(taskNode.getName())) {
				status = "技术负责人审批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("项目实施部门主任确定开工".equals(taskNode.getName())) {
				status = "项目实施部门主任审批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			
			String sql = "update TB_HT_KGQZ set status='"+status+"' where id='"+docid+"'";
			String kgsql = "update TB_HT_WTD set iskg='流程中' where id =(select lxid from TB_HT_KGQZ where id='"+docid+"')";
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
