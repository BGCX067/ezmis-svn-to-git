package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 工作流节点离开事件
 * 
 * @author caihuiwen
 * 
 */
public class JszfNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String iszf = "";
		String zfzt = "";
		String trName = context.getTransition().getName();
		// //流程开始
		// if("申请审批".equals(trName)){
		// status = "流程开始";
		// context.getContextInstance().setVariable("STATUS", status);
		// }
		// 流程结束
		if ("审批完成".equals(trName)) {
			status = "已终结";
			iszf = "已支付";
			zfzt = "支付完成";
			context.getContextInstance().setVariable("STATUS", status);
		}

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			String docid = (String) context.getContextInstance().getVariable(
					"docid");
			DataSource dataSource = (DataSource) SpringContextUtil
					.getBean("dataSource");
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = null;
			String zfztsql = "";
			String sql = "update TB_HT_GCXMJSZFD set status='" + status
					+ "' where id='" + docid + "'";

			/**
			 * 如果是验收项目支付，则为全额支付（一次性支付完成），则验收单支付状态为已支付，支付单支付状态为支付完成，分期数为1
			 * 如果是单独的支付单，可能为分期支付，如果金额未达到则支付单支付状态为欠款，达到则为支付完成，分期数为1.2.3
			 */
			String ysd = "select *　from TB_HT_YSD where id =(select gcys_id from TB_HT_GCXMJSZFD where id='"
					+ docid + "')";
			rs = st.executeQuery(ysd);
			if (rs.next()) { // 验收项目支付
				String iszfsql = "update TB_HT_YSD set iszf='"
						+ iszf
						+ "' where id=(select gcys_id from TB_HT_GCXMJSZFD where id='"
						+ docid + "')";
				zfztsql = "update TB_HT_GCXMJSZFD set zfzt='" + zfzt
						+ "' where id='" + docid + "'";
				st.executeUpdate(iszfsql);
			} else {// 支付项目
				String zfwc = "select xmhtje,yljyfkje from TB_HT_GCXMJSZFD where id='"
						+ docid + "'";
				ResultSet je = st.executeQuery(zfwc);
				int xmhtje = 0, yljyfkje = 0;
				while (je.next()) {
					xmhtje = Integer.parseInt(je.getString("xmhtje"));
					yljyfkje = Integer.parseInt(je.getString("yljyfkje"));
				}

				if (yljyfkje >= xmhtje) {
					zfzt = "支付完成";
				} else {
					zfzt = "欠款";
				}
				zfztsql = "update TB_HT_GCXMJSZFD set zfzt='" + zfzt
						+ "' where id='" + docid + "'";
			}

			st.executeUpdate(zfztsql);
			st.executeUpdate(sql);

			// rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}
