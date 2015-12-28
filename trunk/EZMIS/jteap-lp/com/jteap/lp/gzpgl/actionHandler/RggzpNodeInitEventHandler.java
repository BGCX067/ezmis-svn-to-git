package com.jteap.lp.gzpgl.actionHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 热工票　工作流初始化节点事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class RggzpNodeInitEventHandler extends InitNodeEventHandler {

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
			} else if ("工作票签发".equals(taskNode.getName())
					|| "工作票批准".equals(taskNode.getName())
					|| "安措执行".equals(taskNode.getName())
					|| "安措执行2".equals(taskNode.getName())) {
				status = "待许可";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("工作票许可".equals(taskNode.getName())) {
				status = "待许可";
				context.getContextInstance().setVariable("STATUS", status);
				context.getContextInstance().setVariable("YQZT", "");
				context.getContextInstance().setVariable("BGZT", "");
				clearYQ(conn, docid);
			} else if ("工作票终结".equals(taskNode.getName())) {
				status = "待终结";
				context.getContextInstance().setVariable("STATUS", status);
			}

			String sql = "update TB_LP_GZP_RGP set status='"+status+"' where id='"+docid+"'";
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
		String sql = "update TB_LP_GZP_RGP set YQZT='', BGZT='' where id='"+docid+"'";
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		st.close();
	}
	
}
