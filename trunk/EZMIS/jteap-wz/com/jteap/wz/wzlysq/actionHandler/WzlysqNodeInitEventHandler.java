package com.jteap.wz.wzlysq.actionHandler;

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
public class WzlysqNodeInitEventHandler extends InitNodeEventHandler {
	
	private static final long serialVersionUID = 1742159021856916726L;

	@Override
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		TaskNode taskNode = (TaskNode) context.getNode();

		Connection conn = null;
		Statement st = null;
		try {
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			String docid = (String)context.getContextInstance().getVariable("docid");
			String status = "";
			String flow_status = "";
			if ("填写申请".equals(taskNode.getName())) {
				flow_status = "申请";
				status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", flow_status);
			} else if ("流程审批".equals(taskNode.getName())) {
				flow_status = "待审批";
				status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", flow_status);
			} 

			String sql = "update tb_wz_ylysq set flow_status='"+flow_status+"', zt='"+status+"' where id='"+docid+"'";
			
			st.addBatch(sql);
			st.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if(st!=null){
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
}
