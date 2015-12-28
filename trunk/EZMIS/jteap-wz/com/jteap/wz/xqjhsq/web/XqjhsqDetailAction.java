/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.xqjhsq.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
import com.jteap.wz.base.WZContants;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.gdzc.manager.GdzcdjManager;
import com.jteap.wz.gdzc.manager.GdzcdjMxManager;
import com.jteap.wz.gdzc.model.Gdzcdj;
import com.jteap.wz.gdzc.model.GdzcdjMx;
import com.jteap.wz.lydgl.manager.LydglManager;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydgl;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.wzlysq.manager.WzlysqDetailManager;
import com.jteap.wz.wzlysq.model.WzlysqDetail;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhJDBCManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.*;
import com.jteap.wz.xqjhsq.model.*;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;

@SuppressWarnings("unused")
public class XqjhsqDetailAction extends AbstractAction {

	private static final long serialVersionUID = 5695088326034463627L;
	private XqjhsqDetailManager xqjhsqDetailManager;
	private XqjhsqManager xqjhsqManager;
	private XqjhsqJDBCManager xqjhsqJDBCManager;
	private XqjhManager xqjhManager;
	private XqjhDetailManager xqjhDetailManager;
	private XqjhJDBCManager xqjhJDBCManager;
	private LydmxManager lydmxManager;
	private WzlysqDetailManager wzlysqDetailManager;
	private LydglManager lydglManager;
	private WzdaManager wzdaManager;
	private GdzcdjMxManager gdzcdjMxManager;
	
	public XqjhJDBCManager getXqjhJDBCManager() {
		return xqjhJDBCManager;
	}

	public void setXqjhJDBCManager(XqjhJDBCManager xqjhJDBCManager) {
		this.xqjhJDBCManager = xqjhJDBCManager;
	}

	public XqjhsqJDBCManager getXqjhsqJDBCManager() {
		return xqjhsqJDBCManager;
	}

	public void setXqjhsqJDBCManager(XqjhsqJDBCManager xqjhsqJDBCManager) {
		this.xqjhsqJDBCManager = xqjhsqJDBCManager;
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	private PersonManager personManager;
	private RoleManager roleManager;
	private DictManager dictManager;

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	// public void beforeShowList(HttpServletRequest request,
	// HttpServletResponse response, StringBuffer hql){
	// this.isUseQueryCache = false;
	// String xqjhsqId = request.getParameter("xqjhsqId");
	// String jhy = request.getParameter("jhy");
	// String jhy = "";
	// for (Iterator iterator =
	// xqjhsqJDBCManager.findXqjhsqDetailById(xqjhsqId).iterator();
	// iterator.hasNext();) {
	// XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) iterator.next();
	//			
	// jhy = xqjhsqDetail.getJhy();
	// };
	// String userLoginName = sessionAttrs.get(
	// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
	//		
	//		
	// HqlUtil.addCondition(hql, "xqjhsq.id", xqjhsqId);
	// HqlUtil.addCondition(hql, "jhy", null);
	//		
	//		
	// if(StringUtil.isNotEmpty(jhy)){
	// HqlUtil.addCondition(hql, "jhy", userLoginName);
	// }
	// }

	/**
	 * 描述 : 需求计划申请分配明细列表 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String showFpListAction() throws Exception {
		try {
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			StringBuffer hql = getPageBaseHql();

			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			this.isUseQueryCache = false;
			String xqjhsqId = request.getParameter("xqjhsqId");
			HqlUtil.addCondition(hql, "xqjhsq.id", xqjhsqId);
			HqlUtil.addCondition(hql, "jhy", null);
			HqlUtil.addCondition(hql, "cflag", "0");

			Object pageFlag = request.getAttribute(PAGE_FLAG);
			if (pageFlag == null)
				pageFlag = request.getParameter(PAGE_FLAG);
			String json;
			if (pageFlag != null && pageFlag.toString().equals(PAGE_FLAG_NO)) {
				Collection list = this.getManager().find(hql.toString(),
						showListHqlValues.toArray());
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:" + json + "}";

			} else {
				json = getPageCollectionJson(hql.toString(), showListHqlValues
						.toArray());
			}
			this.outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}

	/**
	 * 描述 : 需求计划申请生效明细列表 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String showSxListAction() throws Exception {
		try {
			StringBuffer hql = getPageBaseHql();

			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			this.isUseQueryCache = false;
			String xqjhsqId = request.getParameter("xqjhsqId");
			String userLoginName = sessionAttrs.get(
					Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
			HqlUtil.addCondition(hql, "xqjhsq.id", xqjhsqId);
			HqlUtil.addCondition(hql, "jhy", userLoginName);
			HqlUtil.addCondition(hql, "done", "0");

			Object pageFlag = request.getAttribute(PAGE_FLAG);
			if (pageFlag == null)
				pageFlag = request.getParameter(PAGE_FLAG);
			String json;
			if (pageFlag != null && pageFlag.toString().equals(PAGE_FLAG_NO)) {
				Collection list = this.getManager().find(hql.toString(),
						showListHqlValues.toArray());
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:" + json + "}";

			} else {
				json = getPageCollectionJson(hql.toString(), showListHqlValues
						.toArray());
			}
			this.outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}

	/**
	 * 分配需求计划申请给相关人员
	 * 
	 * @throws Exception
	 */
	public String doFpXqjhsq() throws Exception {
		String ops = request.getParameter("ops");
		String isBack = request.getParameter("isBack");
		if (StringUtils.isNotEmpty(ops)) {
			List<XqjhsqDetail> detailList = new ArrayList<XqjhsqDetail>();
			List<Map<String, String>> opList = JSONUtil.parseList(ops);// 取得提交的需求计划申请条目
			if (opList.size() > 0) {
				Xqjhsq sq = null;
				for (Map<String, String> opMap : opList) {
					String id = opMap.get("id");
					XqjhsqDetail detail = xqjhsqDetailManager.get(id);
					detail.setJhy(opMap.get("jhy"));// 取得分配的计划员
					detail.setCflag("1");
					sq = detail.getXqjhsq();
					detailList.add(detail);
				}
				sq.setFpsj(new Date());// 设置分配时间
				sq.setStatus("1");// 设置需求计划申请状态（"0"表示"流程中","1"表示"已批准"）
				sq.setIsBack(isBack);//设备分配的需求计划申请是否可以回退到计划主管("1"表示"允许","0"表示"不允许")
				xqjhsqDetailManager.saveOrUpdateAll(detailList);// 保存所有分配内容
				xqjhsqDetailManager.save(sq);// 保存需求计划申请
				this.outputJson("{success:true}");
			}
		}
		return NONE;
	}

	/**
	 * 需求计划生效处理
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doSxXqjhsq() throws Exception {

		String ops = request.getParameter("ops");
		int xh =1;
		// String ycl =
		// dictManager.findDictByCatalogNameWithKey("WZXQJHSQ","已处理").getValue();
		if (StringUtils.isNotEmpty(ops)) {
			List<XqjhsqDetail> detailList = new ArrayList<XqjhsqDetail>();
			Set<XqjhDetail> xqjhDetailList = new HashSet<XqjhDetail>();
			List<Map<String, String>> opList = JSONUtil.parseList(ops);// 取得提交的需求计划申请条目
			if (opList.size() > 0) {
				Xqjhsq sq = null;
				Xqjh xqjh = new Xqjh();
				for (Map<String, String> opMap : opList) {
					XqjhDetail xqjhDetail = new XqjhDetail();
					String id = opMap.get("id");
					XqjhsqDetail detail = xqjhsqDetailManager.get(id);
					detail.setIsCancel(opMap.get("isCancel"));
//					if(Double.valueOf(String.valueOf(opMap.get("sqsl"))) != Double.valueOf(detail.getSqsl())){
//						detail.setSqsl(Double.valueOf(detail.getSqsl()) - Double.valueOf(String.valueOf(opMap.get("sqsl"))));
//						detail.setDone("0");
//					}else{
//						detail.setDone("1");
//					}
					detail.setDone("1");
					sq = detail.getXqjhsq();
					//如果未作废 则生成需求计划明细
					if(opMap.get("isCancel").equals("1")){
					xqjh.setXqjhsqbh(sq.getId());
					xqjh.setGclb(sq.getGclb());
					xqjh.setGcxm(sq.getGcxm());
					xqjh.setSqbm(sq.getSqbmmc());
					xqjh.setSqsj(sq.getSqsj());
					xqjh.setStatus("0");
					xqjh.setSxsj(new Date());
					xqjh.setOperator(detail.getJhy());
					xqjh.setXqjhbh(xqjhJDBCManager.getMaxBH());
					//将申请中的待验收状态及待验收操作人保存到需求计划主单中
					xqjh.setDysczr(sq.getDysczr());
					xqjh.setDyszt(sq.getDyszt());
					//将明细中的待验收状态及待验收操作人保存到需求计划明细中					
					xqjhDetail.setDysczr(detail.getDysczr());
					xqjhDetail.setDyszt(detail.getDyszt());
					
					xqjhDetail.setXqjh(xqjh);
					xqjhDetail.setWzbm(detail.getWzbm());
					xqjhDetail.setPzsl(Double.valueOf(String.valueOf(opMap.get("sqsl"))));
					//计量单位为 物资表中的计量单位
					xqjhDetail.setJldw(wzdaManager.get(detail.getWzbm()).getJldw());
					//修改计划单价为物资档案的计划单价 修改日期：2011-5-31
					xqjhDetail.setJhdj(wzdaManager.get(detail.getWzbm()).getJhdj());
					xqjhDetail.setXysj(detail.getXysj());
					xqjhDetail.setGhs(detail.getProvider());
					xqjhDetail.setStatus("1");
					//添加序号 在需求计划明细生成采购中 配合物资编码 做为唯一标示
					xqjhDetail.setXh(xh+"");
					xh++;
					String lydmxidandsl = detail.getJlmxidandsl();
					if(StringUtil.isNotEmpty(lydmxidandsl)){
						String[] arrs = lydmxidandsl.split(",");
						Lydmx lydmx = lydmxManager.get(arrs[0]);		
						double pzsl = Double.parseDouble(arrs[1]);		//批准领用数量
						double sjsl = Double.parseDouble(arrs[2]);		//实际领用数量
						xqjhDetail.setSlsl(pzsl);
						xqjhDetail.setLysl(sjsl);
						WzlysqDetail wzlysqDetail = lydmx.getWzlysqDetail();
						wzlysqDetail.setXqjhDetail(xqjhDetail);
						if(!"1".equals(lydmx.getLydgl().getLydqf())){
							Lydgl lydgl = lydmx.getLydgl();
							Set mxs = lydgl.getLydmxs();
							Iterator<Lydmx> it = mxs.iterator();
							boolean wqll = true;
							while(it.hasNext()){
								Lydmx mx = it.next();
								if(mx.getWzlysqDetail().getXqjhDetail()==null){
									wqll = false;
									break;
								}
							}
							if(wqll){
								lydmx.getLydgl().setLydqf("1");					//改为领料单
							}
							lydglManager.save(lydgl);
						}
						wzlysqDetailManager.save(wzlysqDetail);
					}
					
					// xqjhDetail.setCgqzykcsl(detail.get)
					// xqjhDetail.setCgsl(detail.getc)
					// xqjhDetail.setDhsl(detail.g)
					// xqjhDetail.setLysl(detail.get)
					// xqjhDetail.setSlsl(slsl)
					// xqjhDetail.setZt(zt);
					xqjhDetailList.add(xqjhDetail);
					detailList.add(detail);
				}}
				if(xqjhDetailList.size()>0){
					xqjh.setXqjhDetail(xqjhDetailList);
//					if(true)
//						throw new RuntimeException();
					// xqjhManager.save(xqjh); //保存一条新需求计划
					// xqjhDetailManager.saveOrUpdateAll(xqjhDetailList);
					xqjhDetailManager.save(xqjh); // 保存需求计划
					// sq.setStatus(ycl);
					xqjhsqDetailManager.saveOrUpdateAll(detailList);// 保存所有分配内容
					xqjhsqDetailManager.flush();
				}
				String hql = "from XqjhsqDetail";
				Object args[] = null;
				List<XqjhsqDetail> xqjhsqDetailCount = xqjhsqDetailManager.find(hql, args);
				int xqjhsqDetailSize = xqjhsqDetailCount.size();
				String hqls = "from XqjhsqDetail as d where d.done = ?";
				Object condition[] = { "1" };
				List<XqjhsqDetail> xqjhDetailDoneCount = xqjhDetailManager.find(hqls, condition);
				int xqjhsqDetailDoneSize = xqjhDetailDoneCount.size();
				if (xqjhsqDetailSize == xqjhsqDetailDoneSize) {
					sq.setStatus("2");
				}
				xqjhsqDetailManager.save(sq);// 保存需求计划申请
				this.outputJson("{success:true}");
			}
		}
		return NONE;
	}

	/**
	 * 需求计划回退至计划主管处理
	 * 
	 * @throws Exception
	 */
	public String doCallBackToFp() throws Exception {
		String xqjhsqid = request.getParameter("xqjhsqid");
		String xqjhsqDetailId[] = request.getParameter("id").split(",");
		Xqjhsq xqjhsq = xqjhsqManager.get(xqjhsqid);
		xqjhsq.setFpsj(null);
		// xqjhsq.setStatus("0");

		for (int i = 0; i < xqjhsqDetailId.length; i++) {
			XqjhsqDetail xqjhsqDetail = xqjhsqDetailManager
					.get(xqjhsqDetailId[i]);
			xqjhsqDetail.setJhy(null);
			xqjhsqDetail.setCflag("0");
			xqjhsqDetailManager.save(xqjhsqDetail);
		}

		xqjhsqManager.save(xqjhsq);// 保存需求计划申请
		// xqjhsqDetailManager.saveOrUpdateAll(xqjhsq.getXqjhsqDetail());//保存需求计划条目
		this.outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 新物资处理
	 * 
	 * @throws Exception
	 */
	public String doHandleWz() throws Exception {
		String new_goods_match ="";
		String xqjhsqid = request.getParameter("xqjhsqId");
		String wzdaId = request.getParameter("wzdaId");
		XqjhsqDetail detail = xqjhsqDetailManager.get(xqjhsqid);
		Wzda wzda = wzdaManager.get(wzdaId);
		new_goods_match=this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString()+":将需求计划物资{"
		+detail.getWzmc()+":"+detail.getXhgg()+"}匹配为{"+wzda.getWzmc()+":"+wzda.getXhgg()+"}";
		detail.setWzbm(wzdaId);
		detail.setIsnew("0");
		//新物资匹配记录
		detail.setNew_goods_match(new_goods_match);
		//将需求计划的物资型号改为匹配的物资型号
		detail.setXhgg(wzda.getXhgg());
		String gdzcmxId =detail.getGdzcmxid(); 
		if(gdzcmxId!=null){
			GdzcdjMx gdzcmx = gdzcdjMxManager.findUniqueBy("id", gdzcmxId);
			gdzcmx.setWzbm(wzdaId);
			gdzcdjMxManager.save(gdzcmx);
		}
		xqjhsqDetailManager.save(detail);
		this.outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 得到相关角色人员
	 */
	public void showPersons() {
		String type = request.getParameter("type");
		String[] roles = WZContants.roles;
		Collection<Person> list = null;
		String json = "";
		if (StringUtil.isNotEmpty(type)) {
			int idx = Integer.parseInt(type);
			Role r = roleManager.findUniqueBy("rolename", roles[idx]);
			list = personManager.findPersonByRoleIds((String) r.getId());
		} else
			list = personManager.getAll();
		// 将集合JSON化
		json = JSONUtil.listToJson(list, new String[] { "userLoginName",
				"userName" });
		try {
			outputJson("{list:" + json + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 描述 : 需求计划申请分配明细【需求计划申请分配】Excel导出 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String exportExcelForXqjhsqmx() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String xqjhsqId = request.getParameter("xqjhsqId");
		String paraHeader = request.getParameter("paraHeader");
//		paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");
		paraHeader = "计划员,物资名称,型号规格,申请数量,计量单位,估计单价,金额,需用时间,是否作废";

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");
//		String paraDataIndex = "sbmc,sbgg,bcpjjb,remark";

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
//		String paraWidth = "150,150,100,150";

		StringBuffer hql = new StringBuffer("from com.jteap.wz.xqjhsq.model.XqjhsqDetail as obj where xqjhsq.id='"+xqjhsqId+"' and obj.cflag='0'");

		beforeShowList(request, response, hql);
		

		List list = getManager().createQuery(hql.toString()).list();
		List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
		for (int i = 0; i < list.size(); i++) {
			XqjhsqDetail xqjhsqDetail = (XqjhsqDetail)list.get(i);
			if(xqjhsqDetail.getIsCancel().equals("1")){
				xqjhsqDetail.setIsCancel("未作废");
			}else{
				xqjhsqDetail.setIsCancel("已作废");
			}
			if(xqjhsqDetail.getJhy() != null){
				xqjhsqDetail.setJhy(personManager.findPersonByLoginName(xqjhsqDetail.getJhy()).getUserName());
			}
			xqjhsqDetailList.add(xqjhsqDetail);
		}

		// 调用导出方法
		export(xqjhsqDetailList, paraHeader, paraDataIndex, paraWidth);
		
		List<XqjhsqDetail> xqjhsqDetailList2 = new ArrayList<XqjhsqDetail>();
		for (int i = 0; i < list.size(); i++) {
			XqjhsqDetail xqjhsqDetail = (XqjhsqDetail)list.get(i);
			if(xqjhsqDetail.getIsCancel().equals("未作废")){
				xqjhsqDetail.setIsCancel("1");
			}else{
				xqjhsqDetail.setIsCancel("0");
			}
			xqjhsqDetail.setJhy(null);
			xqjhsqDetailList2.add(xqjhsqDetail);
		}

		return NONE;
	}
	
	/**
	 * 更新需求计划申请明细【需求计划申请分配】信息
	 * 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String updateDataAction() throws Exception {
		String gridData = request.getParameter("gridData");
		List<Map<String, String>> list = JSONUtil.parseList(gridData);
//		try {
			for (Map<String,String> map : list) {
				String id = map.get("id");
				XqjhsqDetail xqjhsqDetail = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				xqjhsqDetail = xqjhsqDetailManager.get(id);
				xqjhsqDetail.setJhy(map.get("jhy"));
				xqjhsqDetail.setSqsl(Double.valueOf(String.valueOf(map.get("sqsl"))));
				xqjhsqDetail.setGjdj(Double.valueOf(String.valueOf(map.get("gjdj"))));
				if(map.get("xysj")==null){
					xqjhsqDetail.setXysj(null);
				}else{
					xqjhsqDetail.setXysj(DateUtils.StrToDate(map.get("xysj"),"yyyy-MM-dd"));
				}
				xqjhsqDetailManager.save(xqjhsqDetail);
			}
			this.outputJson("{success:true}");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return NONE;
	}
	
	/**
	 * 描述 : 需求计划申请生效明细【需求计划申请生效】Excel导出 作者 : caofei 时间 : Oct 26, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unchecked")
	public String exportXqjhsqSxDetailAction() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String xqjhsqId = request.getParameter("xqjhsqId");
		String paraHeader = request.getParameter("paraHeader");
//		paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");
		paraHeader = "是否作废,计划员,物资名称,型号规格,申请数量,计量单位,估计单价,金额,需用时间";

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");
//		String paraDataIndex = "sbmc,sbgg,bcpjjb,remark";

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
//		String paraWidth = "150,150,100,150";

		StringBuffer hql = new StringBuffer("from com.jteap.wz.xqjhsq.model.XqjhsqDetail as obj where xqjhsq.id='"+xqjhsqId+"' and obj.done='0'");

		beforeShowList(request, response, hql);
		

		List list = getManager().createQuery(hql.toString()).list();
		List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
		for (int i = 0; i < list.size(); i++) {
			XqjhsqDetail xqjhsqDetail = (XqjhsqDetail)list.get(i);
			if(xqjhsqDetail.getIsCancel().equals("1")){
				xqjhsqDetail.setIsCancel("未作废");
			}else{
				xqjhsqDetail.setIsCancel("已作废");
			}
			xqjhsqDetail.setJhyGh((xqjhsqDetail.getJhy()));
			xqjhsqDetail.setJhy(personManager.findPersonByLoginName(xqjhsqDetail.getJhy()).getUserName());
			xqjhsqDetailList.add(xqjhsqDetail);
		}

		// 调用导出方法
		export(xqjhsqDetailList, paraHeader, paraDataIndex, paraWidth);
		
		List<XqjhsqDetail> xqjhsqDetailList2 = new ArrayList<XqjhsqDetail>();
		for (int i = 0; i < list.size(); i++) {
			XqjhsqDetail xqjhsqDetail = (XqjhsqDetail)list.get(i);
			if(xqjhsqDetail.getIsCancel().equals("未作废")){
				xqjhsqDetail.setIsCancel("1");
			}else{
				xqjhsqDetail.setIsCancel("0");
			}
			xqjhsqDetail.setJhy(xqjhsqDetail.getJhyGh());
			xqjhsqDetailList2.add(xqjhsqDetail);
		}

		return NONE;
	}
	/**
	 * 根据需求计划申请ID获取需求计划申请明细
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String findXqjhsqDetailByXqjhsqIdAction() throws Exception{
		try{
			//获取需求计划申请id
			String xqjhsqId = request.getParameter("id");
			String hql = "from XqjhsqDetail x where x.xqjhsq.id = '"+xqjhsqId+"' and (x.dyszt != '2' or x.dyszt is null)";
			List<XqjhsqDetail> xqjhsqMxList = xqjhsqDetailManager.find(hql);
			// 将集合JSON化
			String json = JSONUtil.listToJson(xqjhsqMxList, new String[] {
					"id","wzmc","xhgg","sqsl","xqjhsq","gcxm","gclb","sqbmmc" });
			this.outputJson("{list:" + json
					+ "}");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
	
		return NONE;
	}
	/**
	 * 修改验收单状态以及操作人
	 * @return
	 * @throws Exception 
	 */
	public String updateXqjhsqDetailDysztAction() throws Exception{
		try{
			//获取当前登录人工号
			String userLoginName = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
			String ids = request.getParameter("ids");
			Xqjhsq xqjhsq = null;
			//遍历传过来的明细id依次改变状态值以及操作人
			for(String id:ids.split(",")){
				XqjhsqDetail xqjhsqmx = xqjhsqDetailManager.findUniqueBy("id", id);
				xqjhsqmx.setDysczr(userLoginName);
				xqjhsqmx.setDyszt("2");
				xqjhsq = xqjhsqmx.getXqjhsq();
				xqjhsqDetailManager.save(xqjhsqmx);
			}
			//如果传过来的id长度等于该主单下所有明细的长度 则为全部指定 否则 则为部分指定
			if(xqjhsq.getXqjhsqDetail().size()==ids.split(",").length){
				xqjhsq.setDyszt("2");
			}else{
				xqjhsq.setDyszt("1");
			}
			//保存主单的操作人姓名
			xqjhsq.setDysczr(userLoginName);
			//保存主单
			xqjhsqManager.save(xqjhsq);
			this.outputJson("{success:true}");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	/**
	 * 更新需求计划申请明细【需求计划申请生效】信息
	 * 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String updateXqjhsqSxDetailDataAction() throws Exception {
		String gridData = request.getParameter("gridData");
		List<Map<String, String>> list = JSONUtil.parseList(gridData);
//		try {
			for (Map<String,String> map : list) {
				String id = map.get("id");
				XqjhsqDetail xqjhsqDetail = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				xqjhsqDetail = xqjhsqDetailManager.get(id);
				xqjhsqDetail.setJhy(map.get("jhy"));
				xqjhsqDetail.setSqsl(Double.valueOf(String.valueOf(map.get("sqsl"))));
				xqjhsqDetail.setGjdj(Double.valueOf(String.valueOf(map.get("gjdj"))));
				if(map.get("xysj")==null){
					xqjhsqDetail.setXysj(null);
				}else{
					xqjhsqDetail.setXysj(DateUtils.StrToDate(map.get("xysj"),"yyyy-MM-dd"));
				}
				xqjhsqDetailManager.save(xqjhsqDetail);
			}
			this.outputJson("{success:true}");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return NONE;
	} 
	
	
	/**
	 * 采购计划明细导出
	 */
	public void exportSelectedExcelAction() throws IOException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("导出数据_" + System.currentTimeMillis() + ".xls")
						.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			try {
				bis = new BufferedInputStream(this.exportXqjhsqExcel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (final IOException e) {
			System.out.println("IOException.");
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	
	/**
	 * 选择性导出Excel
	 */
	@SuppressWarnings("deprecation")
	public InputStream exportXqjhsqExcel() throws Exception {
		String idsArr = request.getParameter("idsArr");
		// 所有被选中需求计划申请id
		String idsArray[] = idsArr.split(",");
		// FileOutputStream fout = null;
		// try {
		// fout = new FileOutputStream(new File("F:/data.xls"));
		// } catch (FileNotFoundException e1) {
		// e1.printStackTrace();
		// }
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// HSSFCellStyle style = workbook.createCellStyle();

		// style.setFillBackgroundColor(HSSFColor.BLUE.index);

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 1300);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 3500);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 2500);
		sheet.setColumnWidth(5, 2000);
		sheet.setColumnWidth(6, 2500);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 2800);
		sheet.setColumnWidth(10, 3000);
		
		HSSFPrintSetup hps = sheet.getPrintSetup();  
		hps.setLandscape(true);           //横向打印
		hps.setVResolution((short) 300);  //打印状态
		hps.setPageStart((short)0);       //起始页码
		hps.setHeaderMargin((double)0.1); //页眉
		hps.setFooterMargin((double)0.6);  //页脚
		
		sheet.setMargin(HSSFSheet.LeftMargin,(short)0.1); //左页边距
		sheet.setMargin(HSSFSheet.RightMargin,(short)0.8); //右页边距
//		sheet.setMargin(HSSFSheet.TopMargin,(short)0.8); //上边距
//		sheet.setMargin(HSSFSheet.BottomMargin, (short)0.6); //下边距
		
		sheet.setHorizontallyCenter(true);  //水平居中
		
		
		// 创建字体样式
//		HSSFFont font = workbook.createFont();
//		font.setFontName("Verdana");
//		font.setBoldweight((short) 100);
//		font.setFontHeight((short) 300);
//		font.setColor(HSSFColor.BLUE.index);

		// 创建单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		//插入数据样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//style2.setWrapText(true);// 自动换行
		
		//合计单元格样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setFillForegroundColor(HSSFColor.YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 设置边框
//		style.setBottomBorderColor(HSSFColor.RED.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);

//		style.setFont(font);// 设置字体

		// 创建标题行
//		HSSFRow titleRow = null;
//		titleRow = sheet.createRow(0);
//		titleRow.setHeight((short) 300);// 设定行的高度
//		HSSFCell titleCell = null;
//		titleCell = titleRow.createCell(0);
//		titleCell.setCellValue("测试PIO");
//		titleCell.setCellStyle(style);// 设置单元格样式
		// 合并单元格(startRow，endRow，startColumn，endColumn)
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style1.setWrapText(true);// 自动换行
		
		//列头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titleStyle.setFont(font);
		
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);// 垂直方向的对齐方式
		
		// 创建单元格样式
		HSSFCellStyle style4 = workbook.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);

		style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		
		DecimalFormat decimalFormat = new DecimalFormat("###.00");

		HSSFRow row = null;
		HSSFCell cell = null;
		int count = 0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < idsArray.length; i++) {
				XqjhsqDetail xqjhsqDetail = xqjhsqDetailManager.get(idsArray[i]);
					if(i == 0){
						// 创建行
						row = sheet.createRow(count);
						
						// 创建单元格,设置每个单元格的值（作为表头）
						// 创建单元格,设置每个单元格的值（作为表头）
						cell = row.createCell(0);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("序号");
						
						cell = row.createCell(1);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("需求计划申请编号");
						
						cell = row.createCell(2);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("物资名称");
						
						cell = row.createCell(3);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("规格规格");
						
						cell = row.createCell(4);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("计划员");
						
						cell = row.createCell(5);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("申请数量");
						
						cell = row.createCell(6);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("计量单位");
						
						cell = row.createCell(7);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("估计单价");
						
						cell = row.createCell(8);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("金额");
						
						cell = row.createCell(9);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("需用时间");
						
						cell = row.createCell(10);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("备注");
						
						count++;
					}
					//插入数据
					row = sheet.createRow(count);
					row.setHeight((short) 300);
					cell = row.createCell(0);
					cell.setCellStyle(style2);
					cell.setCellValue(count);
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getXqjhsq().getXqjhsqbh());
					cell = row.createCell(2);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getWzmc());
					
					cell = row.createCell(3);
					cell.setCellStyle(style2);
					if(StringUtils.isNotEmpty(xqjhsqDetail.getXhgg())){
						cell.setCellValue(xqjhsqDetail.getXhgg());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(4);
					cell.setCellStyle(style2);
					if(xqjhsqDetail.getJhy() != null){
						cell.setCellValue(personManager.findPersonByLoginName(xqjhsqDetail.getJhy()).getUserName());
					}
					
					cell = row.createCell(5);
					cell.setCellStyle(style4);
					cell.setCellValue(xqjhsqDetail.getSqsl());
					
					cell = row.createCell(6);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getJldw());
					
					cell = row.createCell(7);
					cell.setCellStyle(style4);
					cell.setCellValue(decimalFormat.format(xqjhsqDetail.getGjdj()));
					
					cell = row.createCell(8);
					cell.setCellStyle(style4);
					cell.setCellValue(decimalFormat.format(xqjhsqDetail.getSqsl()*xqjhsqDetail.getGjdj()));
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					cell = row.createCell(9);
					cell.setCellStyle(style2);
					if(xqjhsqDetail.getXysj() != null){
						cell.setCellValue(format.format(xqjhsqDetail.getXysj()));
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(10);
					cell.setCellStyle(style2);
					if(StringUtils.isNotEmpty(xqjhsqDetail.getRemark())){
						cell.setCellValue(xqjhsqDetail.getRemark());
					}else{
						cell.setCellValue("");
					}
					
					count++;
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				workbook.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}
	
	

	public void setXqjhsqDetailManager(XqjhsqDetailManager xqjhsqDetailManager) {
		this.xqjhsqDetailManager = xqjhsqDetailManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return xqjhsqDetailManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "xqjhsqid", "wzbm", "xh", "wzmc", "xhgg",
				"sqsl", "jldw", "gjdj", "provider", "xysj", "time", "done",
				"isnew", "jhy", "je", "sfdh", "isCancel", "cflag", "remark",

				"dyszt" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "xqjhsqid", "wzbm", "xh", "wzmc", "xhgg",
				"sqsl", "jldw", "gjdj", "provider", "xysj", "done", "isnew",
				"jhy", "sfdh", "isCancel", "cflag" };
	}

	public XqjhsqDetailManager getXqjhsqDetailManager() {
		return xqjhsqDetailManager;
	}

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public LydmxManager getLydmxManager() {
		return lydmxManager;
	}

	public void setLydmxManager(LydmxManager lydmxManager) {
		this.lydmxManager = lydmxManager;
	}

	public WzlysqDetailManager getWzlysqDetailManager() {
		return wzlysqDetailManager;
	}

	public void setWzlysqDetailManager(WzlysqDetailManager wzlysqDetailManager) {
		this.wzlysqDetailManager = wzlysqDetailManager;
	}

	public LydglManager getLydglManager() {
		return lydglManager;
	}

	public void setLydglManager(LydglManager lydglManager) {
		this.lydglManager = lydglManager;
	}

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}

	public void setGdzcdjMxManager(GdzcdjMxManager gdzcdjMxManager) {
		this.gdzcdjMxManager = gdzcdjMxManager;
	}
	 
}
