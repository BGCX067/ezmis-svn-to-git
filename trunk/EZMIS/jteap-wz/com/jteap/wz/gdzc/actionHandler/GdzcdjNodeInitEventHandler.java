package com.jteap.wz.gdzc.actionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
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
public class GdzcdjNodeInitEventHandler extends InitNodeEventHandler {
	
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
			String flow_status = "";
//			String czrxm = (String) context.getVariable("CZRXM");
//			String czr = (String) context.getVariable("CZR");
//			String sqbm = (String) context.getVariable("SQBM");
			String zcbm = (String)context.getVariable("ZCBM");
			if ("申请审批".equals(taskNode.getName())) {
				status = "申请";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("部门领导审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("生技部专工审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("生技部主任审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("总工审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("副总经理审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("总经理审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("计划经营部审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("计划员购买".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				
				this.createXq(zcbm);
				
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			} else if ("总会计师审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				
				//this.createXq(zcbm);
//				this.saveGdzcMx(zcbm);
				
				context.getContextInstance().setVariable("FLOW_STATUS", status);
			}else if ("财务审批".equals(taskNode.getName())) {
				status = "待审批";
				flow_status = "0";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
//				this.saveGdzcMx(zcbm);
			}

			String sql = "update TB_WZ_GDZCDJ set flow_status='"+status+"', zt='"+flow_status+"' where id='"+docid+"'";
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
	/**
	 * 根据验货单 保存固定资产明细 相关数据
	 * @param zcbm
	 */
	private void saveGdzcMx(String zcbm) {
		// TODO Auto-generated method stub
		Connection conn = null;
		DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
		try{
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			StringBuffer sql = new StringBuffer("select t.* from tb_wz_yyhdmx t where t.cgdmx in");
			sql.append("(select c.id from tb_wz_ycgjhmx c where c.xqjhmx in");
			sql.append("(select x.id from tb_wz_xqjh_detail x where x.xqjhbh = ");
			sql.append("(select id from tb_wz_xqjh where xqjhsqbh =");
			sql.append("(select id from tb_wz_xqjhsq where gdzcid = '"+zcbm+"'))))");
			ResultSet rs = st.executeQuery(sql.toString());
			while(rs.next()){
				
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 创建需求计划申请 以及需求计划申请明细
	 * @param zcbm 
	 * @throws SQLException
	 */
	private void createXq(String zcbm) throws SQLException{
		Connection conn = null;
		DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
		try{
			conn = dataSource.getConnection();
			String sqbh = this.calculate("XQJHSQBH","TB_WZ_XQJHSQ");
			String uuid = UUIDGenerator.hibernateUUID();
			String date = DateUtils.getDate("yyyy-MM-dd");
			//根据资产编码 查出资产等级主单 
			String sql = "select * from tb_wz_gdzcdj where zcbm = '"+zcbm+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			//先删除固定资产对应的 需求计划申请 避免重复添加
			Statement delSt = conn.createStatement();
			delSt.addBatch("DELETE TB_WZ_XQJHSQ_DETAIL T WHERE T.XQJHSQID = (SELECT ID FROM TB_WZ_XQJHSQ WHERE GDZCID = '"+zcbm+"')");
			delSt.addBatch("DELETE TB_WZ_XQJHSQ T WHERE T.GDZCID = '"+zcbm+"'");
			delSt.executeBatch();
			delSt.close();
			//////////////////////////////////
			String zdid  = "";
			while(rs.next()){
				zdid = rs.getString("id");
				//虚拟需求计划申请主单
				StringBuffer insetSQ = new StringBuffer();
				insetSQ.append("insert into TB_WZ_XQJHSQ(ID,GCXM,GCLB,SQSJ,CZR,STATUS,")
					   .append("XQJHSQBH,CZYXM,SQBMMC,FLOW_STATUS,IS_BACK,IS_UPDATE,XQJHQF,SFYRZ,BLZT,DYSZT,GDZCID) VALUES (")
					   .append("'"+uuid+"','固定资产','固定资产',to_date('"+date+"','yyyy-mm-dd'),'"+rs.getString("CZY")+"','1','"+sqbh+"','"+rs.getString("CZYXM")+"','"+rs.getString("sqbm")+"',")
					   .append("'已完成','1','1','3','true','1','0','"+zdid+"')");
			
				Statement st1 = conn.createStatement();
				st1.execute(insetSQ.toString());	
				st1.close();
			}
			//查询 固定资产登记 明细
			String sql1 = "select * from tb_wz_gdzcdjmx where gdzcbm = '"+zdid+"'";
			Statement st1= conn.createStatement();
			ResultSet rs1 = st1.executeQuery(sql1);
			while(rs1.next()){
				//虚拟需求计划申请明细
				StringBuffer insetSQ = new StringBuffer();
				insetSQ.append("insert into tb_wz_xqjhsq_detail(ID,XQJHSQID,XH,WZMC,XHGG,SQSL,JLDW,GJDJ,DONE,ISNEW,REMARK,STATUS,DYSZT,is_cancel,C_FLAG,GDZCMXID) VALUES")
					   .append("('"+UUIDGenerator.hibernateUUID()+"','"+uuid+"','"+rs1.getString("xh")+"','"+rs1.getString("gdzcmc")+"',")
					   .append("'"+rs1.getString("ggxh")+"','"+rs1.getString("sl")+"','"+rs1.getString("dw")+"','"+rs1.getString("dj")+"',")
					   .append("'0','1','固定资产','已完成','0','1','0','"+rs1.getString("id")+"')");
				Statement st2 = conn.createStatement();
				st2.execute(insetSQ.toString());	
				st2.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		
	}
	/**
	 * 根据列名，表名返回当前表最大序号
	 * @param colName
	 * @param tbName
	 * @return
	 */
	private String calculate(String colName,String tbName) {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select max("+colName+") from "+tbName;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if (StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getNumberInstance();
					nformat.setMinimumIntegerDigits(8);
					int max = Integer.parseInt(ckbmMax) + 1;
					retValue = nformat.format(max).replaceAll(",", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retValue;
	}

}
