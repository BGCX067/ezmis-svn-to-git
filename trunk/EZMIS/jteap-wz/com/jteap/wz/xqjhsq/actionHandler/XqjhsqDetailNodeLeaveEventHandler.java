package com.jteap.wz.xqjhsq.actionHandler;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;

import com.itextpdf.text.List;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;
import com.jteap.wz.lydgl.manager.LydglManager;
import com.jteap.wz.lydgl.model.Lydgl;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;

/**
 * 描述 : 需求计划申请起草以后，给需求计划明细done、isnew、iscancel、flag、xh赋值
 * 作者 : caofei
 * 时间 : Oct 27, 2010
 * 参数 : 
 * 返回值 : 
 * 异常 : 
 */
@SuppressWarnings({"unused","unchecked"})
public class XqjhsqDetailNodeLeaveEventHandler extends LeaveNodeEventHandler {
	
	private static final long serialVersionUID = 1452027625250484381L;

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String xqjhsqid = (String)context.getContextInstance().getVariable("docid");
		
		XqjhsqManager xqjhsqManager = (XqjhsqManager)SpringContextUtil.getBean("xqjhsqManager");
		Xqjhsq xqjhsq = xqjhsqManager.findUniqueBy("id", xqjhsqid);
		String yhdid = xqjhsq.getYhdid();
		
		String XQJHQF = (String)context.getVariable("XQJHQF");
		String trName = context.getTransition().getName();
		if("待审批".equals(trName)){
			if("2".equals(XQJHQF)){
				YhdglManager yhdglManager = (YhdglManager)SpringContextUtil.getBean("yhdglManager");
				Yhdgl yhdgl = yhdglManager.findUniqueBy("id", yhdid);
				//blzt【0】表示没有选择过的验货单；【1】表示已经选择过的验货单
				yhdgl.setBlzt("1");
				yhdglManager.save(yhdgl);
				yhdglManager.flush();
			}
		}
		Connection conn = null;
		try {
			String docid = (String)context.getContextInstance().getVariable("docid");
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			String xqjhsqDetailSql = "update tb_wz_xqjhsq_detail set done='0' where xqjhsqid='"+docid+"'";
			String xqjhsqSql = "update tb_wz_xqjhsq set is_update='1',qmzt='',is_back='1' where id='"+docid+"'";
			Statement st = conn.createStatement();
			st.executeUpdate(xqjhsqDetailSql);
			st.executeUpdate(xqjhsqSql);
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * 通过id获取lyd对象
	 */
	public String findLydId(String xqjhsqid){
		XqjhsqManager xqjhsqManager = (XqjhsqManager)SpringContextUtil.getBean("xqjhsqManager");
		String lydid = "";
		java.util.List list = new ArrayList();
		String hql = "from Xqjhsq as x where x.id=?";
		Object args[] = {xqjhsqid};
		list = xqjhsqManager.find(hql, args);
		for (int i = 0; i < list.size(); i++) {
			Xqjhsq xqjhsq = (Xqjhsq)list.get(0);
			lydid = xqjhsq.getLydid();
		}
		return lydid;
	}
	
}
