package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 工程验收　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class GcysNodeInitEventHandler extends InitNodeEventHandler {

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
			if ("设备管辖班组起草".equals(taskNode.getName())) {
				status = "设备管辖班组起草";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("设备管辖部门签字".equals(taskNode.getName())) {
				status = "设备管辖部门审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部专工签字".equals(taskNode.getName())) {
				status = "生技部专工审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部主任签字".equals(taskNode.getName())) {
				status = "生技部主任审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("其他相关部门签字".equals(taskNode.getName())) { 
				status = "其他相关部门审批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			
			String sql = "update TB_HT_YSD set status='"+status+"' where id='"+docid+"'";
			String yssql = "update TB_HT_KGQZ set isys='流程中' where id=(select kgqz_id from TB_HT_YSD where id='"+docid+"')";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.executeUpdate(yssql);
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
