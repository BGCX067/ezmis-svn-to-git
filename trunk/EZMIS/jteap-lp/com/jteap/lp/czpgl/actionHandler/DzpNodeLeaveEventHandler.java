package com.jteap.lp.czpgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;

/**
 * 倒闸票 节点离事件
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class DzpNodeLeaveEventHandler extends LeaveNodeEventHandler {

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String trName = context.getTransition().getName();
		Object dtJhkssj = (Date)context.getContextInstance().getVariable("JHKSSJ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String jhkssj = sdf.format(dtJhkssj);
		if ("操作票完成".equals(trName)) {
			status = "合格";
			context.getContextInstance().setVariable("STATUS", status);
			context.getContextInstance().setVariable("JHJSSJ", context.getContextInstance().getVariable("JHKSSJ"));
		} 
		
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String sql = "update TB_LP_CZP_DZ set status='"
					+status+"' , shzt = '1',jhjssj=to_date('"+jhkssj+"','yyyy-MM-dd hh24:mi:ss') where id='"+docid+"'";
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
