package com.jteap.lp.gzpgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 动火工作票　工作流初始化节点事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class DhgzpNodeInitEventHandler extends InitNodeEventHandler {

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
				status = "工作票申请";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("运行许可".equals(taskNode.getName()) || 
					"动火设备系统图".equals(taskNode.getName())) {
				status = "待审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("动火审批".equals(taskNode.getName())) {
				status = "待许可";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("动火开工".equals(taskNode.getName())) {
				status = "待开工";
				context.getContextInstance().setVariable("STATUS", status);
			}
			String sql = "update TB_LP_GZP_DH set status='"+status+"' where id='"+docid+"'";
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
