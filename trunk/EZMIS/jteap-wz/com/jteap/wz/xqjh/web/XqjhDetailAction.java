package com.jteap.wz.xqjh.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wz.cgjhgl.manager.CgjhglManager;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhJDBCManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

/**
 * 描述 : 需求计划明细Action 作者 : caofei 时间 : Oct 21, 2010 参数 : 返回值 : 异常 :
 */
@SuppressWarnings("serial")
public class XqjhDetailAction extends AbstractAction {

	private XqjhDetailManager xqjhDetailManager;

	private XqjhJDBCManager xqjhJDBCManager;

	private WzdaManager wzdaManager;

	private CgjhglManager cgjhglManager;

	private CgjhmxManager cgjhmxManager;

	private XqjhManager xqjhManager;

	private PersonManager personManager;

	private XqjhsqManager xqjhsqManager;

	private XqjhsqDetailManager xqjhsqDetailManager;

	public XqjhsqDetailManager getXqjhsqDetailManager() {
		return xqjhsqDetailManager;
	}

	public void setXqjhsqDetailManager(XqjhsqDetailManager xqjhsqDetailManager) {
		this.xqjhsqDetailManager = xqjhsqDetailManager;
	}

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

	public CgjhglManager getCgjhglManager() {
		return cgjhglManager;
	}

	public void setCgjhglManager(CgjhglManager cgjhglManager) {
		this.cgjhglManager = cgjhglManager;
	}

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}

	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return xqjhDetailManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID", "XQJH", "WZBM", "XH", "PZSL", "JLDW",
				"JHDJ", "XYSJ", "GHS", "FREE", "CGSL", "DHSL", "LYSL", "SLSL",
				"STATUS", "TIME" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ID", "XQJH", "WZBM", "XH", "PZSL", "JLDW",
				"JHDJ", "XYSJ", "GHS", "FREE", "CGSL", "DHSL", "LYSL", "SLSL",
				"STATUS", "TIME" };
	}

	// 取得Page
	public Page getPage(String sql) throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		return xqjhJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
	}

	/**
	 * 
	 * 描述 : 需求计划待处理列表 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String showXqjhDetailListAction() throws Exception {

		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();

		try {
			// 修改计划单价为物资档案的计划单价 修改日期：2011-5-31
			String sql = "select t.*, h.personname as czyName from (SELECT a.id, a.xqjhbh, e.xqjhsqbh,e.id as xqjhsqid, a.sxsj, a.operator, d.personname, a.sqbm, e.czy, a.gclb, a.gcxm, "
					+ " b.wzbm, c.wzmc, c.xhgg, b.id as xqjhmxid, b.pzsl,b.xh, b.dyszt,b.jldw, c.jhdj, b.xysj, b.free, b.cgsl, b.status"
					+ " FROM tb_wz_xqjh a, tb_wz_xqjh_detail b, tb_wz_swzda c, tb_sys_person d, tb_wz_xqjhsq e"
					+ " WHERE a.id = b.xqjhbh AND a.operator = d.Login_Name AND a.xqjhsqbh = e.id AND b.wzbm = c.id AND (b.pzsl > b.cgsl + b.free)"
					+ " AND (a.operator = '"
					+ userLoginName
					+ "')) t, tb_sys_person h where t.czy=h.login_name  order by t.xqjhbh desc";
			Page page = this.getPage(sql);

			String json = JSONUtil.listToJson((List<Map>) page.getResult(),
					new String[] { "ID", "XQJHBH", "XQJHSQBH", "SXSJ",
							"OPERATOR", "PERSONNAME", "SQBM", "CZY", "GCLB",
							"GCXM", "WZBM", "WZMC", "XHGG", "XQJHMXID", "PZSL",
							"JLDW", "JHDJ", "XYSJ", "FREE", "CGSL", "STATUS",
							"CZYNAME", "XQJHSQID","DYSZT","XH" });

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
//			System.out.println(json);
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 获取具体一个流程实例 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : 异常 :
	 */
	public String showProcessinstance() throws Exception {
		String xqjhsqId = request.getParameter("sqjhsqid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select t.processinstance_ from jbpm_variableinstance t where t.name_='docid' and t.stringvalue_='"
					+ xqjhsqId + "'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			String processinstance = "";
			while (rs.next()) {
				processinstance = rs.getString("processinstance_");
			}
			outputJson("{success:true,processinstance:'" + processinstance
					+ "'}");
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
	/**
	 * 回退至需求计划申请生效前
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String RollbackAction() throws Exception{
		try{
			String[] wzbms = request.getParameter("wzbms").split(",");
			String[] sqbhs = request.getParameter("xqjhsqbhs").split(",");
			//要回退的需求计划明细id
			String xqjhmxids = request.getParameter("xqjhmxids");
			List<XqjhDetail> xqjhmxList = xqjhDetailManager.find("from XqjhDetail mx where mx.id in("+xqjhmxids+")");
			//首先根据传过来的需求计划明细id删除明细
			for(XqjhDetail xqmx : xqjhmxList){
				Xqjh xqjh = xqmx.getXqjh();
				
				//如果该明细的主单明细数量为1 则直接删除主单
				xqjh.getXqjhDetail().remove(xqmx);
				if(xqjh.getXqjhDetail().size()>0){
					xqjhManager.save(xqjh);
				}else{
					xqjhManager.remove(xqjh);
				}
//				xqjhDetailManager.save(xqmx);
//				if(xqmx.getXqjh().getXqjhDetail().size()==xqjhmxids.split(",").length){
//					System.out.println("删除主单");
//					xqjhManager.remove(xqmx.getXqjh());
//				}else{
//					System.out.println("删除明细");
//					xqjhDetailManager.remove(xqmx);
//				}
			}
			//循环 申请编号 数组，因为一个编号对应一个物资编码，
			//存放在数组的位置也是相同，所以只用遍历申请编号的下标就能获取对应的物资编码
			for(int i = 0;i<sqbhs.length;i++){
				List<XqjhsqDetail> xqjhsqList = xqjhsqDetailManager.find( "from XqjhsqDetail x where x.xqjhsq.xqjhsqbh='"+sqbhs[i]+"' and x.wzbm = '"+wzbms[i]+"'");
				//根据物资编码 查出对应的物资档案信息
//				Wzda wzda =wzdaManager.findUniqueBy("id", wzbms[i]);
				//遍历查出来的需求计划申请明细集合
				for(XqjhsqDetail xqsq:xqjhsqList){
					//如果该申请的物资名称和物资档案的物资名称相同
//					if(xqsq.getWzmc().equals(wzda.getWzmc())){
						//且型号规格同时为null(因为Null的话 不能用equals 对比 所以采用这种方法) 就是需求申请选择的老物资 
//						if(StringUtils.isEmpty(xqsq.getXhgg()) && StringUtils.isEmpty(wzda.getXhgg())){
////							xqsq.setIsnew("0");
//							//如果同时不为null 则判断型号规格是否相等 如果相等 则是 老物资 如不然 则是新物资 改变状态
//						}else if(StringUtils.isNotEmpty(xqsq.getXhgg()) && StringUtils.isNotEmpty(wzda.getXhgg())){
////							if(xqsq.getXhgg().equals(wzda.getXhgg())){
////								xqsq.setIsnew("1");
////							}else{
//								xqsq.setIsnew("1");
//								xqsq.setWzbm("");
////							}
//						}else{//这种情况是 其中有一个为null 一个不为Null  肯定也就是新物资了
//							xqsq.setIsnew("1");
//							xqsq.setWzbm("");
//						}
//					}else{
//						xqsq.setIsnew("1");
//						xqsq.setWzbm("");
//					}
					xqsq.setIsnew("1");
					xqsq.setWzbm("");
					xqsq.setDone("0");
					xqjhDetailManager.save(xqsq);
				}
			}
			this.outputJson("{success:true}");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	/**
	 * 
	 * 描述 : 数量分配（需求计划明细列表） 作者 : caofei 时间 : Oct 29, 2010 参数 : 返回值 : 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String showXqjhDetailForSlfpListAction() throws Exception {
		String sqjhid = request.getParameter("sqjhid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			// 修改计划单价为 物资档案的计划单价 修改日期：2011-5-31
			String sql = "SELECT a.id, a.xqjhbh, b.wzmc, b.xhgg, b.dqkc, b.yfpsl, c.ckmc, a.wzbm, a.xh, a.pzsl, a.jldw, b.jhdj, a.free, a.cgsl,"
					+ " a.dhsl, a.lysl, a.slsl, b.pjj, b.kwbm "
					+ "FROM tb_wz_xqjh_detail a, tb_wz_swzda b, tb_wz_ckgl c, tb_wz_skwgl d "
					+ "WHERE a.wzbm = b.id and b.kwbm = d.id and c.id = d.ckid and (a.xqjhbh = '"
					+ sqjhid + "')";
			Page page = this.getPage(sql);

			String json = JSONUtil.listToJson((List<Map>) page.getResult(),
					new String[] { "ID", "XQJHBH", "WZMC", "XHGG", "DQKC",
							"YFPSL", "CKMC", "WZBM", "XH", "PZSL", "JLDW",
							"JHDJ", "FREE", "CGSL", "DHSL", "LYSL", "SLSL",
							"PJJ", "KWBM" });

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
	/**
	 * 初始化需求计划明细领用数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String initXqjhDetail(){
		LydmxManager lydmxManager = (LydmxManager) SpringContextUtil.getBean("lydmxManager");
		//查询所有已经生效的需求计划明细id
		List<XqjhDetail> xqjhmxList = xqjhDetailManager.find("from XqjhDetail a where a.status = '1'");
		for(XqjhDetail xqmx:xqjhmxList){
			double lysl = 0;
			//根据需求计划明细id去查询物资领用明细 获得实际领用数量
			List<Lydmx> lydmxList = lydmxManager.find("from Lydmx y where y.zt = '1' and y.wzlysqDetail.xqjhDetail.id = '"+xqmx.getId()+"'");
			for(Lydmx lydmx:lydmxList){
				lysl += lydmx.getSjlysl();
			}
			xqmx.setLysl(lysl);
			xqjhDetailManager.save(xqmx);
		}
		System.out.println("初始化完毕");
		return NONE;
	}
	/**
	 * 描述 : 出库量调整 作者 : caofei 时间 : Oct 29, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unused")
	public String KclSettingAction() throws Exception {
		String xqjhDetailId = request.getParameter("xqjhDetailId");
		XqjhDetail xqjhDetail = xqjhDetailManager.get(xqjhDetailId);
		String wzbm = request.getParameter("wzbm");
		Double fpsl = Double.valueOf(request.getParameter("fpsl"));
		try {
			Wzda wzda = wzdaManager.get(wzbm);
			Double dqkc = wzda.getDqkc(); // 当前库存
			Double yfpsl = wzda.getYfpsl(); // 已分配数量
			Double freeKc = (dqkc - yfpsl); // 自由库存量
			if (fpsl > freeKc) { // 分配数量大于自由库存量
				this.outputJson("{success:true,'flag':false}");
			} else {
				if (fpsl > xqjhDetail.getPzsl()) { // 分配数量大于批准数量
					this.outputJson("{success:false,'flag':false}");
				} else {
					wzda.setYfpsl(yfpsl + fpsl);
					wzdaManager.save(wzda);
					xqjhDetail.setFree(fpsl + xqjhDetail.getFree());
					xqjhDetailManager.save(xqjhDetail);
					this.outputJson("{success:true,'flag':true}");
				}
			}
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 描述 : 需求计划生成采购计划（初始化列表） 作者 : caofei 时间 : Oct 31, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unused")
	public String showXqjhSccgjhListAction() throws Exception {
		String xqjhid[] = request.getParameter("sqjhid").split(",");
		String wzbm[] = request.getParameter("wzbm").split(",");
		String xh[] = request.getParameter("xh").split(",");
		
		String type = request.getParameter("type");
		String isChecked = "";
		if (StringUtil.isNotEmpty(request.getParameter("isChecked"))) {
			isChecked = request.getParameter("isChecked");
		}
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String xqjhDetailId = ""; // 需求计划明细id
			String wzdaid = "";
			String xqjhbh = ""; // 需求计划主单id
			String wzmcgg = ""; // 物资名称规格
			// String xhgg = "";
			String ckmc = "";
			double kykc = 0.0; // 可用库存
			String jldw = "";
			double jhdj = 0.0;
			double pzsl = 0.0;
			double free = 0.0;
			double cgsl = 0.0;
			Map<String, List<Object>> map = new HashMap<String, List<Object>>();
			StringBuffer result = new StringBuffer();
			String sql = "";
			StringBuffer sbtmp = new StringBuffer();
			for (int i = 0; i < xqjhid.length; i++) {
//				if (sbtmp.indexOf(wzbm[i]) >= 0) {
//					continue;
//				} else {
					sbtmp.append(",").append(wzbm[i]);
//				}

				if (type.equals("all")) {
					// 计划单价取物资档案中的计划单价 修改日期：2011-5-31
					sql = "SELECT DISTINCT a.id, a.xqjhbh, a.wzbm, a.xh, a.pzsl, a.jldw, b.jhdj, a.free, b.dqkc,"
							+ " b.yfpsl, a.cgsl, d.ckmc, b.wzmc, b.xhgg"
							+ " FROM tb_wz_xqjh_detail a, tb_wz_swzda b, tb_wz_skwgl c, tb_wz_ckgl d"
							+ " WHERE ( a.wzbm = b.id ) and  (b.kwbm = c.id) and (c.ckid = d.id) and ( a.pzsl > a.free + a.cgsl) and"
							+ " ( a.xqjhbh = '"
							+ xqjhid[i]
							+ "' ) and ( a.wzbm='"
							+ wzbm[i]
							+ "' ) and (a.xh ='"+xh[i]+"') order by a.wzbm";
				} else if (type.equals("wcgx")) {
					// 计划单价取物资档案中的计划单价 修改日期：2011-5-31
					sql = "SELECT DISTINCT a.id, a.xqjhbh, a.wzbm, a.xh, a.pzsl, a.jldw, b.jhdj, a.free, b.dqkc,"
							+ " b.yfpsl, a.cgsl, d.ckmc, b.wzmc, b.xhgg"
							+ " FROM tb_wz_xqjh_detail a, tb_wz_swzda b, tb_wz_skwgl c, tb_wz_ckgl d"
							+ " WHERE ( a.wzbm = b.id ) and  (b.kwbm = c.id) and (c.ckid = d.id) and ( a.pzsl > a.free + a.cgsl) and"
							+ " ( a.xqjhbh = '"
							+ xqjhid[i]
							+ "' ) and ( a.wzbm='"
							+ wzbm[i]
							+ "' ) and (a.xh ='"+xh[i]+"') order by a.wzbm";
				}
				PreparedStatement pst = conn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					xqjhDetailId = rs.getString("id");
					wzdaid = rs.getString("wzbm");
					xqjhbh = xqjhManager.get(rs.getString("xqjhbh"))
							.getXqjhbh();
					wzmcgg = rs.getString("wzmc") + "(" + rs.getString("xhgg")
							+ ")";
					ckmc = rs.getString("ckmc");
					kykc = (rs.getDouble("dqkc") - rs.getDouble("yfpsl"));
					jldw = rs.getString("jldw");
					jhdj = rs.getDouble("jhdj");
					pzsl = rs.getDouble("pzsl");
					free = rs.getDouble("free");
					cgsl = rs.getDouble("cgsl");
					Object objArray[] = new Object[11];
					objArray[0] = wzdaid;
					objArray[1] = wzmcgg;
					objArray[2] = ckmc;
					objArray[3] = kykc;
					objArray[4] = xqjhbh;
					objArray[5] = jldw;
					objArray[6] = jhdj;
					objArray[7] = pzsl;
					objArray[8] = free;
					objArray[9] = cgsl;
					objArray[10] = xqjhDetailId; // 需求计划明细ID
					if (!map.containsKey(wzdaid)) {
						List<Object> list = new ArrayList<Object>();
						list.add(objArray);
						map.put(wzdaid, list);
					} else {
						List<Object> lists = map.get(wzdaid);
						lists.add(objArray);
						map.put(wzdaid, lists);
					}
				}
			}
			this.request.setAttribute("map", map);
			this.request.setAttribute("sqjhid", request.getParameter("sqjhid"));
			this.request.setAttribute("wzbm", request.getParameter("wzbm"));
			this.request.setAttribute("isChecked", isChecked);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return SUCCESS;
	}

	/**
	 * 
	 * 描述 : 生成采购计划 作者 : caofei 时间 : Nov 1, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public String sccgjhAction() throws Exception {
		String ops[] = request.getParameter("ops").split(",");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String cgy = "";
		String xqjhDetailId = "";
		String wzbm = "";
		String dyszt = "2";
		boolean is_all = false;
		boolean flag = false;
		try {
			for (int i = 1; i <= ops.length / 3; i++) {
				// 判断每一个xqjhsqDetail对应的xqjhsq_detail中cgjhmx是否存在，如果有就往cgjhmx中插入xqjhmx（修改人：曹飞
				// 修改时间：2011-06-02）
				XqjhDetail xqjhDetail = xqjhDetailManager.get(ops[3 * i - 2]);
				Xqjhsq xqjhsq = xqjhsqManager.get(xqjhDetail.getXqjh()
						.getXqjhsqbh());
				Set<XqjhsqDetail> xqjhsqDetailSet = new HashSet<XqjhsqDetail>();
				xqjhsqDetailSet = xqjhsq.getXqjhsqDetail();
				Iterator<XqjhsqDetail> it = xqjhsqDetailSet.iterator();
				for (XqjhsqDetail xqjhsqDetail : xqjhsqDetailSet) {
					if (StringUtils.isNotEmpty(xqjhsqDetail.getCgjhmxid())) {
						if (xqjhsqDetail.getWzbm().equals(xqjhDetail.getWzbm())) {
							Cgjhmx cgjhmx = cgjhmxManager.get(xqjhsqDetail
									.getCgjhmxid());
							cgjhmx.setXqjhmx(ops[3 * i - 2]);
							cgjhmxManager.save(cgjhmx);
							xqjhDetail.setCgsl(xqjhDetail.getPzsl());
							flag = true;
							break;
						}
					}
				}
				// ////////////////////////////
				if (!flag) {
					cgy = ops[3 * i - 3];
					xqjhDetailId = ops[3 * i - 2];
					wzbm = ops[3 * i - 1];
					Object objArray[] = new Object[3];
					objArray[0] = cgy;
					objArray[1] = xqjhDetailId;
					objArray[2] = wzbm;
					if (!map.containsKey(cgy)) {
						List<Object> list = new ArrayList<Object>();
						list.add(objArray);
						map.put(cgy, list);
					} else {
						List<Object> lists = map.get(cgy);
						lists.add(objArray);
						map.put(cgy, lists);
					}
				}
				flag = false;
			}
			String wzbmTmp = "";
			String maxBHTmp = xqjhDetailManager.getMaxNum();
			for (Object key : map.keySet()) {
				List objs = map.get(key);
				List<XqjhDetail> detailList = new ArrayList<XqjhDetail>();
				Set<Cgjhmx> cgjhMxList = new HashSet<Cgjhmx>();
				Xqjh xqjh = null;
				Cgjhgl cgjh = new Cgjhgl();
				int tmpCount = 0;
				for (int j = 0; j < objs.size(); j++) {
					Cgjhmx cgjhmx = new Cgjhmx();

					XqjhDetail detail = xqjhDetailManager.get(((Object[]) objs
							.get(j))[1].toString());
					String wzbms = ((Object[]) objs.get(j))[2].toString();
					Wzda wzda = wzdaManager.get(wzbms);
					detail.setCgsl(detail.getPzsl());
					xqjh = detail.getXqjh();
					cgjh.setBh(maxBHTmp);
					maxBHTmp = this.getNextNum(maxBHTmp);
					cgjh.setZdsj(new Date());
					cgjh.setJhy(xqjh.getOperator());

					Double cgsl = detail.getPzsl();
					int cgmxXH = 0;
					for (; tmpCount < objs.size(); tmpCount++) {
						String tmpWzbm = ((Object[]) objs.get(tmpCount))[2]
								.toString();
						Double tmpCgsl = detail.getPzsl();
						if (j == tmpCount) {
							cgjh.setZt("0");
							cgjhmx.setWzdagl(wzda);
							cgjhmx.setCgsl(tmpCgsl);
							cgjhmx.setCgjldw((detail.getJldw()));
							//将需求计划明细中状态 赋到采购计划明细中
							cgjhmx.setDysczr(detail.getDysczr());
							cgjhmx.setDyszt(detail.getDyszt());
							//如果为2则为指定 则改变状态
							if("2".equals(detail.getDyszt())){
								is_all = true;
							}
							//如为0则为 未指定
							if("0".equals(detail.getDyszt())){
								dyszt="1";
							}
							cgjhmx.setHsxs(1);
							// 修改计划单价为 物资档案的计划单价 修改日期：2011-5-31
							cgjhmx.setJhdj(wzda.getJhdj());
							cgjhmx.setXh(String.valueOf(++cgmxXH));
							cgjhmx.setDhsl(detail.getDhsl());
							cgjhmx.setCgy(((Object[]) objs.get(j))[0]
									.toString());
							cgjhmx.setZt("0");
							cgjhmx.setCgfx("0");
							cgjhmx
									.setJhdhrq(simpleDateFormat.parse(DateUtils
											.getNextMonth(simpleDateFormat
													.format(new Date()),
													"yyyy-MM-dd")));
							cgjhmx.setCgjhgl(cgjh);
							cgjhmx.setXqjhmx(detail.getId());
//						} else if (wzbms.equals(tmpWzbm)) {
//							cgjhmx.setCgsl(cgsl + tmpCgsl);
						} else {
							break;
						}
					}
					j = tmpCount - 1;
					cgjhMxList.add(cgjhmx);
					detailList.add(detail);
				}
				//如果存在指定 则添加状态
				if(is_all){
					cgjh.setDyszt(dyszt);
				}else{
					//不存在 则添加状态为0
					cgjh.setDyszt("0");
				}
				cgjh.setCgjhmxs(cgjhMxList);
				cgjhmxManager.save(cgjh); // 保存采购计划
				xqjhDetailManager.saveOrUpdateAll(detailList);// 保存所有需求计划明细内容
				xqjhDetailManager.flush();
				xqjhDetailManager.save(xqjh); // 保存需求计划
			}
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}

	/**
	 * 描述 : 需求计划明细Excel导出 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String exportXqjhDetailAction() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String xqjhMxIdArray = request.getParameter("xqjhMxIdArray");
		String xqjhDetailIdArray[] = xqjhMxIdArray.split(",");

		String paraHeader = request.getParameter("paraHeader");
		// paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");
		paraHeader = "单据编号,物资名称规格,计量单位,批准数量,计划单价,需用时间,申请部门,申请人,操作员,状态,工程类别";

		// 表索引信息（逗号表达式）
		// String paraDataIndex = request.getParameter("paraDataIndex");
		String paraDataIndex = "XQJHBH,WZMC,JLDW,PZSL,JHDJ,XYSJ,SQBM,CZYNAME,OPERATOR,STATUS,GCLB";

		// 宽度(逗号表达式)
		// String paraWidth = request.getParameter("paraWidth");
		String paraWidth = "80,160,60,80,70,100,70,70,70,50,100";

		Connection conn = null;
		@SuppressWarnings("unused")
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = null;
			List<Map<String, Object>> objsList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < xqjhDetailIdArray.length; j++) {
				// 修改计划单价 为物资档案的计划单价 修改日期;2011-5-31
				String sql = "select t.*, h.personname as czyName from (SELECT a.id, a.xqjhbh, a.xqjhsqbh, a.sxsj, a.operator, d.personname, a.sqbm, e.czy, a.gclb, a.gcxm, "
						+ " b.wzbm, c.wzmc, c.xhgg, b.id as xqjhmxid, b.pzsl, b.jldw, c.jhdj, b.xysj, b.free, b.cgsl, b.status"
						+ " FROM tb_wz_xqjh a, tb_wz_xqjh_detail b, tb_wz_swzda c, tb_sys_person d, tb_wz_xqjhsq e"
						+ " WHERE a.id = b.xqjhbh AND b.id = '"
						+ xqjhDetailIdArray[j]
						+ "' AND a.operator = d.Login_Name AND a.xqjhsqbh = e.id AND b.wzbm = c.id AND (b.pzsl > b.cgsl + b.free)"
						+ " AND (a.operator = '"
						+ userLoginName
						+ "')) t, tb_sys_person h where t.czy=h.login_name  order by t.xqjhbh desc";
				rs = st.executeQuery(sql);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData rsmeta = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
						Object obj = rs.getObject(i);
						// 针对oracle timestamp日期单独处理，转换成date
						if (obj instanceof oracle.sql.TIMESTAMP) {
							obj = ((oracle.sql.TIMESTAMP) obj).dateValue()
									.getTime();
						} else if (rsmeta.getColumnName(i).equals("OPERATOR")) {
							map.put(rsmeta.getColumnName(i), personManager
									.findPersonByLoginName(String.valueOf(obj))
									.getUserName());
						} else if (rsmeta.getColumnName(i).equals("STATUS")) {
							if (String.valueOf(obj).equals("1")) {
								map.put(rsmeta.getColumnName(i), "未作废");
							} else {
								map.put(rsmeta.getColumnName(i), "已作废");
							}
						} else if (rsmeta.getColumnName(i).equals("WZMC")) {
							map.put(rsmeta.getColumnName(i), obj + "("
									+ rs.getObject("xhgg") + ")");
						} else {
							obj = StringUtil.clobToStringByDB(obj);
							map.put(rsmeta.getColumnName(i), obj);
						}
					}
					list.add(map);
				}
				objsList.addAll(list);
			}
			export(objsList, paraHeader, paraDataIndex, paraWidth);
			rs.close();
		} catch (Exception ex) {
			throw ex;
		} finally {
			conn.close();
		}
		// 调用导出方法
		return NONE;
	}

	/**
	 * 描述 : 需求计划明细汇总Excel导出 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String exportXqjhDetailHzAction() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String xqjhMxIdArray = request.getParameter("xqjhMxIdArray");
		String xqjhDetailIdArray[] = xqjhMxIdArray.split(",");

		String paraHeader = request.getParameter("paraHeader");
		// paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");
		paraHeader = "单据编号,物资名称规格,计量单位,批准数量,金额";

		// 表索引信息（逗号表达式）
		// String paraDataIndex = request.getParameter("paraDataIndex");
		String paraDataIndex = "XQJHBH,WZMC,JLDW,PZSL,MONEY";

		// 宽度(逗号表达式)
		// String paraWidth = request.getParameter("paraWidth");
		String paraWidth = "80,200,100,150,150";

		Connection conn = null;
		@SuppressWarnings("unused")
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = null;
			List<Map<String, Object>> objsList = new ArrayList<Map<String, Object>>();
			double totalMoney = 0.0;
			double totalSl = 0.0;
			Map<String, Object> hzMap = new HashMap<String, Object>();
			for (int j = 0; j < xqjhDetailIdArray.length; j++) {
				// 修改计划单价为 物资档案的计划单价 修改日期 ： 2011-5-31
				String sql = "select t.*,t.pzsl*t.jhdj as money, h.personname as czyName from (SELECT a.id, a.xqjhbh, a.xqjhsqbh, a.sxsj, a.operator, d.personname, a.sqbm, e.czy, a.gclb, a.gcxm, "
						+ " b.wzbm, c.wzmc, c.xhgg, b.id as xqjhmxid, b.pzsl, b.jldw, c.jhdj, b.xysj, b.free, b.cgsl, b.status"
						+ " FROM tb_wz_xqjh a, tb_wz_xqjh_detail b, tb_wz_swzda c, tb_sys_person d, tb_wz_xqjhsq e"
						+ " WHERE a.id = b.xqjhbh AND b.id = '"
						+ xqjhDetailIdArray[j]
						+ "' AND a.operator = d.Login_Name AND a.xqjhsqbh = e.id AND b.wzbm = c.id AND (b.pzsl > b.cgsl + b.free)"
						+ " AND (a.operator = '"
						+ userLoginName
						+ "')) t, tb_sys_person h where t.czy=h.login_name  order by t.xqjhbh desc";
				rs = st.executeQuery(sql);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData rsmeta = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
						Object obj = rs.getObject(i);
						// 针对oracle timestamp日期单独处理，转换成date
						if (obj instanceof oracle.sql.TIMESTAMP) {
							obj = ((oracle.sql.TIMESTAMP) obj).dateValue()
									.getTime();
						} else if (rsmeta.getColumnName(i).equals("WZMC")) {
							map.put(rsmeta.getColumnName(i), obj + "("
									+ rs.getObject("xhgg") + ")");
						} else if (rsmeta.getColumnName(i).equals("PZSL")) {
							map.put(rsmeta.getColumnName(i), Double
									.valueOf(String.valueOf(obj)));
							totalSl += Double.valueOf(String.valueOf(obj));
						} else if (rsmeta.getColumnName(i).equals("MONEY")) {
							map.put(rsmeta.getColumnName(i), Double
									.valueOf(String.valueOf(obj)));
							totalMoney += Double.valueOf(String.valueOf(obj));
						} else {
							obj = StringUtil.clobToStringByDB(obj);
							map.put(rsmeta.getColumnName(i), obj);
						}
					}
					list.add(map);
				}
				objsList.addAll(list);
			}
			hzMap.put("MONEY", totalMoney);
			hzMap.put("PZSL", totalSl);
			hzMap.put("JLDW", "合计:");
			objsList.add(hzMap);
			export(objsList, paraHeader, paraDataIndex, paraWidth);
			rs.close();
		} catch (Exception ex) {
			throw ex;
		} finally {
			conn.close();
		}
		// 调用导出方法
		return NONE;
	}

	public XqjhJDBCManager getXqjhJDBCManager() {
		return xqjhJDBCManager;
	}

	public void setXqjhJDBCManager(XqjhJDBCManager xqjhJDBCManager) {
		this.xqjhJDBCManager = xqjhJDBCManager;
	}

	private String getNextNum(String num) {
		NumberFormat nformat = NumberFormat.getIntegerInstance();
		int nextnum = Integer.parseInt(num) + 1;
		nformat.setMinimumIntegerDigits(8);
		return nformat.format(nextnum).replaceAll(",", "");
	}

}
