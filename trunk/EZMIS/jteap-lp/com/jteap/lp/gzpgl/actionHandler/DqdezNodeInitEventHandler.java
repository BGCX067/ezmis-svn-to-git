package com.jteap.lp.gzpgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 电气第二种票　工作流初始化节点事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class DqdezNodeInitEventHandler extends InitNodeEventHandler {

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
			} else if ("工作票批准".equals(taskNode.getName())) {
				status = "待批准";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("工作票许可".equals(taskNode.getName())) {
				status = "待许可";
				context.getContextInstance().setVariable("STATUS", status);
			}  else if ("工作结束".equals(taskNode.getName())) {
				status = "待结束";
				context.getContextInstance().setVariable("STATUS", status);
			}

			String sql = "update TB_LP_GZP_DQDEZ set status='"+status+"' where id='"+docid+"'";
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
