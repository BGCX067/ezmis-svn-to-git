package com.jteap.sb.sbydgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 异动通知单　工作流初始化节点事件
 * @author caofei
 *
 */
public class YdtzdNodeInitEventHandler extends InitNodeEventHandler {

	private static final long serialVersionUID = 1742159021856916726L;

	@Override
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		TaskNode taskNode = (TaskNode) context.getNode();

		Connection conn = null;
		try {
			DataSource dataSource = (DataSource) SpringContextUtil
					.getBean("dataSource");
			conn = dataSource.getConnection();

			String docid = (String) context.getContextInstance().getVariable(
					"docid");
			String status = "";
			if ("异动基本信息".equals(taskNode.getName())) {
				status = "申请";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("检修部会审".equals(taskNode.getName())
					|| "发电部会审".equals(taskNode.getName())) {
				status = "待审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部审核".equals(taskNode.getName())) {
				status = "待审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("公司领导审批".equals(taskNode.getName())) {
				status = "待审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("执行情况".equals(taskNode.getName())) {
				status = "待执行";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("复核".equals(taskNode.getName())) {
				status = "待复核";
				context.getContextInstance().setVariable("STATUS", status);
			}

			String sql = "update tb_sb_sbydgl_ydxx set status='" + status
					+ "' where id='" + docid + "'";
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
