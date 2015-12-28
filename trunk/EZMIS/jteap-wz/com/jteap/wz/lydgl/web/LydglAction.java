/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.lydgl.web;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.StringSubstitution;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.manager.CrkrzmxManager;
import com.jteap.wz.crkrzgl.model.Crkrzgl;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
import com.jteap.wz.lydgl.manager.LydglJDBCManager;
import com.jteap.wz.lydgl.manager.LydglManager;
import com.jteap.wz.lydgl.model.Lydgl;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.tjrw.util.TjrwUtils;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.wzlysq.model.WzlysqDetail;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;



@SuppressWarnings({ "unchecked", "serial", "unused" })
public class LydglAction extends AbstractAction {
	private LydglManager lydglManager;
	private LydglJDBCManager lydglJDBCManager;
	private CrkrzglManager crkrzglManager;
	private CrkrzmxManager crkrzmxManager;
	private XqjhDetailManager xqjhDetailManager;
	private XqjhManager xqjhManager;
	private WzdaManager wzdaManager;
	private PersonManager personManager;

	public CrkrzglManager getCrkrzglManager() {
		return crkrzglManager;
	}

	public void setCrkrzglManager(CrkrzglManager crkrzglManager) {
		this.crkrzglManager = crkrzglManager;
	}

	public CrkrzmxManager getCrkrzmxManager() {
		return crkrzmxManager;
	}

	public void setCrkrzmxManager(CrkrzmxManager crkrzmxManager) {
		this.crkrzmxManager = crkrzmxManager;
	}

	public LydglManager getLydglManager() {
		return lydglManager;
	}

	public void setLydglManager(LydglManager lydglManager) {
		this.lydglManager = lydglManager;
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
	
	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String hqlWhere = request.getParameter("queryParamsSql");
//			//默认状态为未生效
			String zt = "0";
			zt = request.getParameter("zt");
			HqlUtil.addCondition(hql, "zt",zt);
			
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "bh", "desc");
		}
	}
	
	/**
	 * 
	 * 描述 : 新物资系统领用单数据 
	 * 作者 : caofei
	 * 时间 : Mar 8, 2011
	 * 参数 : 
	 * 返回值 : 
	 * 异常 :
	 */
	public String showListBySqlAction()throws Exception{
		
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String loginName=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		String zf = request.getParameter("zf");
		try {
			StringBuffer sbSql = new StringBuffer();

			sbSql.append("SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,to_char(lyd.lysj,'yyyy-mm-dd') as LYDATE,lyd.lybm,lyd.zt,lyd.bh,sq.bh as SQBH,lyd.LYDQF as LYDQF,lyd.lczt FROM tb_wz_ylyd lyd,tb_wz_ylysq sq WHERE ");
			sbSql.append("(lyd.lysqbh = sq.id) AND ");
			if(StringUtils.isEmpty(zf)){
				sbSql.append("(lyd.zt = '0') AND ");
			}else{
				sbSql.append("(lyd.zfzt = '1') AND ");
			}
			sbSql.append("lyd.id in (select DISTINCT lydbh from tb_wz_ylydmx a ,tb_wz_swzda b,tb_wz_skwgl c where ");
			sbSql.append("a.wzbm=b.id and b.kwbm =c.id and ");
			sbSql.append("c.ckid in (select id from tb_wz_ckgl where ckgly = '");
			sbSql.append(loginName);
			sbSql.append("')) order by lyd.bh desc");
			String sql = sbSql.toString();
			System.out.println(sql);
			Page page = lydglJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[]{
					"ID",
					"LYSQBH",
					"CZR",
					"LLR",
					"GCLB",
					"GCXM",
					"LYDATE",
					"LYBM",
					"ZT",
					"BH",
					"SQBH",
					"LYDQF",
					"LCZT"});

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 老物资系统领用单数据 
	 * 作者 : caofei
	 * 时间 : Mar 8, 2011
	 * 参数 : 
	 * 返回值 : 
	 * 异常 :
	 */
	public String showHistoryListBySqlAction() throws Exception {
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String loginName=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		
		try {
			StringBuffer sbSql = new StringBuffer();

//			sbSql.append("SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,lyd.lysj,lyd.lybm,lyd.zt,lyd.bh,sq.bh as SQBH,lyd.LYDQF as LYDQF,lyd.lczt FROM tb_wz_ylyd lyd,tb_wz_ylysq sq WHERE ");
			sbSql.append("SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,to_char(lyd.lysj,'yyyy-mm-dd') as LYTIME,lyd.lybm,lyd.zt,lyd.bh,lyd.LYDQF as LYDQF,lyd.lczt FROM tb_wz_ylyd lyd WHERE ");
//			sbSql.append("(lyd.lysqbh = sq.id) AND ");
			sbSql.append("(lyd.zt = '0') AND (lyd.lysqbh is null) AND ");
			sbSql.append("lyd.id in (select DISTINCT lydbh from tb_wz_ylydmx a ,tb_wz_swzda b,tb_wz_skwgl c where ");
			sbSql.append("a.wzbm=b.id and b.kwbm =c.id and ");
			sbSql.append("c.ckid in (select id from tb_wz_ckgl where ckgly='");
			sbSql.append(loginName);
			sbSql.append("'))");
			//String sql = "SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,lyd.lysj,lyd.lybm,lyd.zt,lyd.bh,lyd.LYDQF as LYDQF,lyd.lczt FROM tb_wz_ylyd lyd WHERE (lyd.zt = '0') AND lyd.id in (select DISTINCT lydbh from tb_wz_ylydmx a ,tb_wz_swzda b,tb_wz_skwgl c where a.wzbm=b.id and b.kwbm =c.id and c.ckid in (select id from tb_wz_ckgl where ckgly='0157')) union SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,lyd.lysj,lyd.lybm,lyd.zt,lyd.bh,lyd.LYDQF as LYDQF,lyd.lczt FROM tb_wz_ylyd lyd,tb_wz_ylysq sq WHERE (lyd.lysqbh = sq.id) AND (lyd.zt = '0') AND lyd.id in (select DISTINCT lydbh from tb_wz_ylydmx a ,tb_wz_swzda b,tb_wz_skwgl c where a.wzbm=b.id and b.kwbm =c.id and c.ckid in (select id from tb_wz_ckgl where ckgly='0157'))";
			String sql = sbSql.toString();
			Page page = lydglJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[]{
					"ID",
					//"LYSQBH",
					"CZR",
					"LLR",
					"GCLB",
					"GCXM",
					"LYTIME",
					"LYBM",
					"ZT",
					"BH",
					//"SQBH",
					"LYDQF",
					"LCZT"});

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	

	
	public String showSelectListBySqlAction() throws Exception{
		
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
//		String loginName=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		
		String bm = (String)this.request.getParameter("bm");
		String gclb = (String)this.request.getParameter("gclb");
		String gcxm = (String)this.request.getParameter("gcxm");
		String lydId = request.getParameter("lydid");
		try {
			StringBuffer sbSql = new StringBuffer();

			sbSql.append("SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,to_char(lyd.lysj,'yyyy-MM-dd') as LYSJ,lyd.lybm,lyd.zt,lyd.bh,sq.bh as SQBH,lyd.LYDQF as LYDQF FROM tb_wz_ylyd lyd,tb_wz_ylysq sq WHERE ");
			sbSql.append("(lyd.lysqbh = sq.id) and ");
			sbSql.append("lyd.LYDQF = '2' and ");
			sbSql.append("sq.LYBM='"+bm+"' and ");
			sbSql.append("sq.GCLB='"+gclb+"' and ");
			sbSql.append("sq.GCXM='"+gcxm+"' ");
//			sbSql.append("(lyd.zt = '0') AND ");
//			sbSql.append("lyd.id in (select DISTINCT lydbh from tb_wz_ylydmx a ,tb_wz_swzda b,tb_wz_skwgl c where ");
//			sbSql.append("a.wzbm=b.id and b.kwbm =c.id and ");
//			sbSql.append("c.ckid in (select id from tb_wz_ckgl where ckgly='");
//			sbSql.append(loginName);
//			sbSql.append("'))");
			//如果有领用单ID
			if(lydId!=""){
				sbSql = new StringBuffer();
				sbSql.append("SELECT DISTINCT lyd.id,lyd.lysqbh,lyd.czr,lyd.llr,lyd.gclb,lyd.gcxm,to_char(lyd.lysj,'yyyy-MM-dd') as LYSJ,lyd.lybm,lyd.zt,lyd.bh,sq.bh as SQBH,lyd.LYDQF as LYDQF FROM tb_wz_ylyd lyd,tb_wz_ylysq sq ");
				sbSql.append("WHERE lyd.id = '"+lydId+"' and lyd.lysqbh = sq.id");
			}
			String sql = sbSql.toString();
			Page page = lydglJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[]{
					"ID",
					"LYSQBH",
					"CZR",
					"LLR",
					"GCLB",
					"GCXM",
					"LYSJ",
					"LYBM",
					"ZT",
					"BH",
					"SQBH",
					"LYDQF"});

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 领用单生效
	 */
	public String enableLydgl() throws Exception{
		
		//选中的明细ID和ID对应的实际领用数量
		String ids = request.getParameter("ids");
		//选中的主单ID
		String lydid = request.getParameter("lydid");
		//补料状态
		String  blzt = request.getParameter("type");
		//记录需要补需求计划的 领用明细id
		String lydmxids = request.getParameter("mxids");
		try {

			if(ids!=null && lydid!=null){
				
				//当前登录名
				String curPersonLoginName=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
				//当前登录人中文名称
				String curPersonName = (String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME);
				//获取最大出入库明细编号
				String crkmxbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
				//获取最大出入库编号
				String crkbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");
				
				String[] array_id = ids.split(",");
				Map<String,String> map_id_sjsl = new HashMap<String,String>();
				for (int i = 0; i < array_id.length; i++) {
					String[] idAndSjsl = array_id[i].split("=");
					map_id_sjsl.put(idAndSjsl[0], idAndSjsl[1]);
				}
				
				Lydgl lydgl = lydglManager.findUniqueBy("id", lydid);
				Set<Lydmx> lydmxs = lydgl.getLydmxs();
				List<Lydmx> new_lydmxs = new ArrayList<Lydmx>();
				//作为所有明细是不是全部生效
				boolean all_enable = true;
				
				//自动构建出入库信息
				Crkrzgl crkrzgl = new Crkrzgl();
				//获取主单对应的明细
				Set crkrzmxs = crkrzgl.getCrkrzmxs();
				String ckbm = "";
				
				int new_index = 0;
				//是否自由领用
				///boolean is_zyly = false;
				for (Lydmx obj:lydmxs) {
					if(map_id_sjsl.containsKey(obj.getId())){
						//实际领用数量
						double sjlysl = Double.parseDouble(map_id_sjsl.get(obj.getId()));
						//批准数量
						double pzsl = obj.getPzlysl();
						//剩余数量
						double sysl = pzsl-sjlysl;	
						//物资档案
						Wzda wzda = obj.getWzbm();
						//如果对应的需求计划明细没有的话 则是自由领用
//						if(obj.getWzlysqDetail().getXqjhDetail()==null){
//							is_zyly = true;
//						}
						//这里为物资部分数量生效
						if(sysl>0){
							Lydmx lydmx = new Lydmx();
							BeanUtils.copyProperties(lydmx, obj);
							//id设置为null，因为主键生成策略是自动生成uuid
							lydmx.setId(null);
							//新生成的明细单的批准数量和实际领用数量相等
							lydmx.setPzlysl(sjlysl);
							lydmx.setSjlysl(sjlysl);
							lydmx.setZt("1");
							lydmx.setXh(String.valueOf(new_lydmxs.size()+lydmxs.size()+1));
							lydmx.setLydgl(lydgl);
							
//							//放入工程类别 工程项目
//							lydmx.setGclb(lydgl.getGclb());
//							lydmx.setGcxm(lydgl.getGcxm());
							//如果有库存则按照老物资价格出库
//							if(wzda.getOldkc()>0.0){
					 
							//实际金额
							lydmx.setSjje(TjrwUtils.getSjje(lydmx));
//							}else{
//								//实际金额
//								lydmx.setSjje(TjrwUtils.getSjje(lydmx));
//							}
							//设置领用时间
							lydmx.setLysj(new Date());
							//原有的明细单批准数量要减去实际生效数量
							obj.setPzlysl(obj.getPzlysl()-sjlysl);
							//设置实际领用数量
							//obj.setSjlysl(sjlysl);
							
							new_lydmxs.add(lydmx);
							
							//有新明细生成则一定主单为未生效
							all_enable = false;
						}else{
							obj.setZt("1");
							obj.setSjlysl(sjlysl);
					 
							obj.setSjje(TjrwUtils.getSjje(obj));
//							//放入工程类别 工程项目
//							obj.setGclb(lydgl.getGclb());
//							obj.setGcxm(lydgl.getGcxm());
							//设置领用时间
							obj.setLysj(new Date());
						}
						
						//修改对应领用申请明细信息
						WzlysqDetail wzlysqDetail = obj.getWzlysqDetail();
						//修改领用申请对应的需求计划信息
						XqjhDetail detail = wzlysqDetail.getXqjhDetail();
						if(detail!=null){
							//设置需求计划明细的领用数量  累加实际领用数量
							detail.setLysl(detail.getLysl()+sjlysl);
							//detail.setSlsl(detail.getSlsl()+obj.getPzlysl());
						}
						//设置物资领用申请明细的领用数量  累加实际领用数量
						wzlysqDetail.setLysl(wzlysqDetail.getLysl()+sjlysl);
						
						//设置物资档案的当前库存量  减去实际领用数量
						wzda.setDqkc(wzda.getDqkc()-sjlysl);
						//设置物资档案的已分配数量 减去实际领用数量
						if(wzda.getYfpsl()>sjlysl){
							wzda.setYfpsl(wzda.getYfpsl()-sjlysl);
						}
						//************插入出入库明细信息（出库）**********开始
						Crkrzmx crk = new Crkrzmx();
						ckbm = obj.getWzbm().getKw().getCkid();
						//Wzda wzda = yhdmx.getWzdagl();
						//wzda.setDqkc(wzda.getDqkc()+yhdmx.getYssl().intValue());
						//double cPJJ = yhdmx.getSqdj()+(yhdmx.getSqdj()*yhdmx.getSl()/yhdmx.getYssl());
						//wzda.setPjj((wzda.getPjj()+cPJJ)/2);
						new_index++;
						crk.setCrkrzgl(crkrzgl);
						crk.setWzda(obj.getWzbm());
						crk.setCrksl(sjlysl);
						//修改计划单价为物资档案的计划单价 修改日期：2011-5-31
						crk.setCrkjg(obj.getWzbm().getJhdj());
						//crk.setCrkdh(this.getMaxNum(crkrzglManager, "select max(crkdh) from Crkrzmx"));
						crk.setXh(String.valueOf(new_index));
						crk.setCrkdh(crkmxbh);
						crkmxbh = this.getNextNum(crkmxbh);
						//crkrzmxs = new HashSet();
						crkrzmxs.add(crk);
						//************插入出入库明细信息（出库）**********结束
					}else{
						if("0".equals(obj.getZt())){
							all_enable = false;
						}
					}
				}
	
				//crkrzgl.setCrkdh(this.getMaxNum(crkrzglManager, "select max(crkdh) from Crkrzgl"));
				crkrzgl.setCrkdh(crkbh);
				crkbh = this.getNextNum(crkbh);
				crkrzgl.setCrksj(new Date());
				crkrzgl.setCkbh(ckbm);
				crkrzgl.setCzr(curPersonLoginName);
				crkrzgl.setCzrxm(curPersonName);
				crkrzgl.setCrklb("11");
				crkrzgl.setCrkqf("2");
				crkrzgl.setXgdjbh(lydgl.getBh());
				//******************插入出入库信息（出库）******************结束
				
				//加入新的领料单明细
				lydmxs.addAll(new_lydmxs);
				
				//如果所有的明细都生效了那么主单自动生效
				if(all_enable){
					lydgl.setZt("1");
				}
	
				//持久化
				lydgl.setLysj(new Date());
				lydgl.setCzr(curPersonName);
				lydgl.setLydmxs(lydmxs);
				
				crkrzglManager.save(crkrzgl);
				lydglManager.save(lydgl);
				// 需要填补申请计划
				if (("1").equals(blzt)) {
					draftNewWorkFlowInstance(lydgl,lydmxids);
				}
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			outputJson("{success:false,msg:'"+msg+"'}");
		}
		
		return NONE;
	}
	//修改领用单明细 判断库存量是否充足
	public String editLydmxAction(){
		try{
			//获取采购明细ID
			String xqjhmxId = request.getParameter("id");
			//领用数量
			String lysl = request.getParameter("lysl");
			//物资id
			//String wzid = request.getParameter("wzid");
			//如果需求明细id为空 则是自由领用 则判断物资的库存量
			if("".equals(xqjhmxId)){
				/*
				Wzda wzda = wzdaManager.get(wzid);
				if(wzda.getDqkc()<Double.parseDouble(lysl)){
					this.outputJson("{success:false,msg:'当前可领用数量不足！',sl:"+wzda.getDqkc()+"}");
				}*/
			}else{//否则 则是判断需求计划明细中的采购数量
				XqjhDetail xqjhmx = xqjhDetailManager.get(xqjhmxId);
				if(xqjhmx!=null){
					//未领用数量 = 采购数量-这次领用数量-已领用数量
					double wlysl = xqjhmx.getPzsl()-Double.parseDouble(lysl)-xqjhmx.getSlsl();
					if(wlysl<0){
						this.outputJson("{success:false,msg:'当前可领用数量不足！',sl:"+(xqjhmx.getPzsl()-xqjhmx.getSlsl())+"}");
					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 起草一个新的流程实例 通过Request输入参数 docid 业务对象编号 recordJson 业务对象数据 curNodeName
	 * 当前处理环节 curActors 当前处理人 flowConfigId 流程配置编号
	 * 
	 * @return 表单数据
	 *         {"nextActors":"0845,0490,0963,0223,0390~0845","token":45974,"creatdt":"2009-10-22
	 *         17:40:46","pid":45973,"ver":1,"tid":45975,"flowName":"项目审批","nextNodes":"部门经理审批~总经理审批","creator":"root"}
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String draftNewWorkFlowInstance(Lydgl lydgl,String xqjhmxids) throws Exception {
		// //领用单id
		// String lysqbh =
		// (String)context.getContextInstance().getVariable("docid");
		//		
		// String lydid = findLydId(lysqbh);

		// 自由入库验货单id
		String yhdid = lydgl.getId();

		// 自由入库验货单所属班组
		String groupName = lydgl.getLybm();

		// 补料计划申请编号
		XqjhsqJDBCManager xqjhsqJDBCManager = (XqjhsqJDBCManager) SpringContextUtil
				.getBean("xqjhsqJDBCManager");
		String bljhsqBH = xqjhsqJDBCManager.getBljhsqBHMax(3);

		// 业务数据编号
		String docid = UUIDGenerator.hibernateUUID();
		insertXqjhsq(docid, yhdid, "2", bljhsqBH,xqjhmxids);
		String taskName = "填写申请";
		// 借料申请起草人
		String userName = personManager.findPersonByUserName(lydgl.getLlr()).get(0).getUserLoginName();
		String userLoginName = userName;

		// 借料申请起草人对象
		String personId = findPersonIdByLoginName(userLoginName);

		// 获取起草人所属班组人员
		String qcrGroups = findAdminGroupIdsOfThePerson(groupName);

		String actorLoginName = userLoginName; // 补料需求计划起草人
		String actor = findPersonName(actorLoginName); // 操作人中文名(刘枫)

		String taskActor = qcrGroups;
		// 流程配置编号
//		String flowConfigNm = "8a65808d2f95e26c012f95e9e8fa001b"; // 补料计划申请内码(只有重新定义流程才需要更改该值)
//		String flowConfigId = findNewFlowConfig(flowConfigNm);

		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringContextUtil
				.getBean("flowConfigManager");
		List<FlowConfig> flowConfigList = new ArrayList<FlowConfig>();
		flowConfigList = flowConfigManager.getAll();
		FlowConfig flowConfig = null;
		for (int i = 0; i < flowConfigList.size(); i++) {
			flowConfig = flowConfigList.get(i);
			if(("物资补料计划申请").equals(flowConfig.getName())){
				if(flowConfig.isNewVer()){
					break;
				}
			}
		}
//		FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
		ProcessInstance pi = draftNewProcess(flowConfig, docid, userLoginName);

		HashMap map = new HashMap();
		map.put("pid", pi.getId());

		// 表单类型
		String formType = flowConfig.getFormtype();
		HashMap recordmap = null;
		// 获得业务数据的MAP
		// EFORM
		if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
			String formSn = "TB_WZ_BLJHSQ"; // 补料计划申请SN

			EFormManager eformManager = (EFormManager) SpringContextUtil
					.getBean("eformManager");
			EForm eform = eformManager.findUniqueBy("sn", formSn);
			DefTableInfo defTabInfo = eform.getDefTable();
			String tableName = defTabInfo.getTableCode();
			String schema = defTabInfo.getSchema();

			JdbcManager jdbcManager = (JdbcManager) SpringContextUtil
					.getBean("jdbcManager");
			recordmap = (HashMap) jdbcManager.getRecById(docid, schema,
					tableName);
			pi.getContextInstance().setVariables(recordmap);
			// CFORM
		}
		// else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
		// String recordJson =
		// (String)context.getContextInstance().getVariable("recordJson");
		// recordmap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
		// pi.getContextInstance().setVariables(recordmap);
		// }

		if (pi != null) {
			pi.getContextInstance().setTransientVariable(
					WFConstants.WF_VAR_LOGINNAME, userLoginName);

			JbpmOperateManager jbpmOperateManager = (JbpmOperateManager) SpringContextUtil
					.getBean("jbpmOperateManager");
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi
					.getId(), userLoginName);
			if (ti != null) {
				// 下一结点处理人
				// String nextActors =
				// jbpmOperateManager.getNextTaskActorByNode(ti);
				String nextActors = qcrGroups;
				// 下一结点名称
				String nextNodes = "物资补料计划申请";
				map.put("nextNodes", nextNodes);
				map.put("nextActors", nextActors);

				map.put("tid", ti.getId());
				map.put("token", ti.getToken().getId());
			}

			// 流程起草人
			String creator = userLoginName;
			// 流程起草时间
			Date creatdt = (Date) pi.getContextInstance()
					.getVariable("creatdt");
			// 流程名称
			String flowName = "物资补料计划申请";
			map.put("creator", creator);
			map.put("creatdt", DateUtils.formatDate(creatdt));
			map.put("flowName", flowName);
			map.put("ver", pi.getProcessDefinition().getVersion());
		}

		// 计算待办主题并保存待办对象
		String cf = "物资补料计划申请:" + bljhsqBH;
		// cf = evalTodoTopic(cf, recordmap);

		TaskToDoManager taskToDoManager = (TaskToDoManager) SpringContextUtil
				.getBean("taskToDoManager");
		// 创建流程申请代办
		taskToDoManager.saveToDo(flowConfig, pi, actor, taskActor, taskName,
				cf, docid, pi.getRootToken().getId() + "", null, userLoginName);

		String nextActorIds = (String) map.get("nextActors"); // 下一个环节处理人
		String nextActorNames = changeToChineseName(nextActorIds); // 下一个环节处理人中文名称

		WorkFlowLogManager workFlowLogManager = (WorkFlowLogManager) SpringContextUtil
				.getBean("workFlowLogManager");
		// 记录流程日志
		workFlowLogManager.addFlowLog(pi.getId(), taskName, actor,
				actorLoginName, nextActorNames, (String) map.get("nextNodes"),
				"起草流程", "");

		String returnJson = JSONUtil.mapToJson(map);
		returnJson = "{success:true,data:" + returnJson + "}";
		// PrintWriter out = getOut();
		// out.print(returnJson);
		return null;
	}

	/**
	 * 根据流程配置编号，起草一个新的流程实例
	 * 
	 * @param flowConfigId
	 * @throws Exception
	 */
	public ProcessInstance draftNewProcess(FlowConfig flowConfig, String docid,
			String userLoginName) throws Exception {
		if (flowConfig.getPd_id() == null) {
			throw new BusinessException("流程还没发布，请先发布流程！");
		}

		String userName = userLoginName;

		JbpmOperateManager jbpmOperateManager = (JbpmOperateManager) SpringContextUtil
				.getBean("jbpmOperateManager");
		long pid = jbpmOperateManager.createProcessInstance(flowConfig.getNm(),
				userName);
		ProcessInstance pi = jbpmOperateManager.getJbpmTemplate()
				.findProcessInstance(pid);
		pi.getContextInstance().setVariable(WFConstants.WF_VAR_DOCID, docid);
		// if (StringUtil.isNotEmpty(flowConfig.getFormId())) {
		// request.setAttribute("formId", flowConfig.getFormId());
		// request.setAttribute("type", flowConfig.getFormtype());
		// }
		// request.setAttribute("processPageUrl", flowConfig.getProcess_url());

		return pi;
	}

	/**
	 * 待办主题计算
	 * 
	 * @author 肖平松
	 * @version Oct 28, 2009
	 * @param cf
	 * @param map
	 * @return
	 */
	private String evalTodoTopic(String cf, HashMap map) {
		if (StringUtil.isEmpty(cf))
			return "";
		String regex = "\\$\\{[^\\}\\{\"']*\\}"; // 匹配${}公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while (mt.find()) {
			String group = mt.group();
			String topic = group.substring(group.indexOf("{") + 1,
					group.lastIndexOf("}")).toUpperCase();
			Object value = map.get(topic);
			if (value == null) {
				value = "";
			}
			if (value instanceof Date) {
				value = DateUtils.formatDate((Date) value,
						"yyyy-MM-dd HH:mm:ss");
			}
			cf = mt.replaceFirst(value + "");
			mt = p.matcher(cf);
		}
		return cf;
	}

	/**
	 * 将登陆名称转换为用户中文名
	 * 
	 * @param nextActors
	 *            要转换的处理人字符串(例如:0001,0002~0003,0004)
	 * @return
	 */
	private String changeToChineseName(String userLoginNames) {
		PersonManager personManager = (PersonManager) SpringContextUtil
				.getBean("personManager");
		String nextActor = userLoginNames;
		if (StringUtil.isNotEmpty(userLoginNames)) {
			String tempActor = "";
			String[] nextTasksActorTemp = userLoginNames.split("~");
			for (int j = 0; j < nextTasksActorTemp.length; j++) {
				String[] nextTasksActorArray = nextTasksActorTemp[j].split(",");
				for (int i = 0; i < nextTasksActorArray.length; i++) {
					if (nextTasksActorArray[i]
							.equals(Constants.ADMINISTRATOR_ACCOUNT)) {
						tempActor += Constants.ADMINISTRATOR_NAME + ",";
					} else {
						tempActor += personManager.findPersonByLoginName(
								nextTasksActorArray[i]).getUserName()
								+ ",";
					}
				}
			}
			nextActor = tempActor.substring(0, tempActor.length() - 1);
		}
		return nextActor;
	}

	/**
	 * 获取当前用户登录名
	 */
	private String getCurUserLoginName() {
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME)
				.toString();
	}

	/**
	 * 通过起草人登录名获取起草人id
	 */
	public String findPersonIdByLoginName(String userLoginName) {
		Connection conn = null;
		String personId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.id from tb_sys_person p where p.login_name = '"
					+ userLoginName + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				personId = rs.getString(1);
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
		return personId;
	}

	/**
	 * 通过起草人id获取所属班组下所有用户名
	 */
	public String findAdminGroupIdsOfThePerson(String groupName) {
		Connection conn = null;
		StringBuffer returnValue = new StringBuffer();
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.login_name from tb_sys_person p "
					+ "where p.id in (select t.personid from tb_sys_person2group t "
					+ "where groupid in (select g.id from tb_sys_group g where g.groupname = '"
					+ groupName + "'))";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				returnValue.append(rs.getString(1) + ",");
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
		return returnValue.toString().substring(0,
				returnValue.toString().length() - 1);
	}

	/**
	 * 通过登录名获取中文名称
	 */
	public String findPersonName(String userLoingName) {
		Connection conn = null;
		String personName = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.personname from tb_sys_person p where p.login_name = '"
					+ userLoingName + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				personName = rs.getString(1);
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
		return personName;
	}

	/**
	 * 匹配一条补料计划申请(与当前自由入库验货单对应)
	 */
	public void insertXqjhsq(String id, String yhdid, String xqjhqf,
			String bljhsqBH,String lydmxids) {
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "insert into tb_wz_xqjhsq(id,lydid,xqjhqf,xqjhsqbh,blzt,lydmxids) values('"
				+ id
				+ "','"
				+ yhdid
				+ "','3','"
				+ bljhsqBH
				+ "','2','"+lydmxids+"')";
			PreparedStatement psts = conn.prepareStatement(sql);
			psts.execute();
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
	}

	/**
	 * 通过FlowConfig里面的内码和是否最新版本获取最新Flowconfig对象
	 */
	public String findNewFlowConfig(String flowConfigNm) {
		Connection conn = null;
		String flowConfigId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select * from tb_wf_flowconfig t where t.FLOW_NM = '"
					+ flowConfigNm + "' and t.IS_NEW_VER = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				flowConfigId = rs.getString("id");
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
		return flowConfigId;
	}

	/**
	 * 通过lysqbh获取id
	 */
	public String findLydId(String lydbh) {
		Connection conn = null;
		String lydId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select l.id from tb_wz_ylyd l where l.lysqbh = '"
					+ lydbh + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				lydId = rs.getString("id");
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
		return lydId;
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return lydglManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"lysqbh",
			"czr",
			"llr",
			"gclb",
			"gcxm",
			"lysj",
			"lybm",
			"zt",
			"wzlysq",
			"bh",
			"lczt",
			"time"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"bh",
				"czr",
				"llr",
				"gclb",
				"gcxm",
				"lysj",
				"lybm",
				"zt"
				};
	}

	public LydglJDBCManager getLydglJDBCManager() {
		return lydglJDBCManager;
	}

	public void setLydglJDBCManager(LydglJDBCManager lydglJDBCManager) {
		this.lydglJDBCManager = lydglJDBCManager;
	}
	
	/**
	 * 获取目标表中最大的编号，生成新的编号，规则：最大编号+1
	 * @param entityManager
	 * @param hql
	 * @return
	 * @throws Exception 
	 */
	private String getMaxNum(String sql) throws Exception{
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			//String sql = sql;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
			 		int max = Integer.parseInt(ckbmMax)+1;
			 		retValue = nformat.format(max).replaceAll(",", "");
				}
			}
		} catch (Exception e) {
			throw e;
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
	
	/**
	 * 获取下一位编号
	 * @param num
	 * @return
	 */
	private String getNextNum(String num){
		NumberFormat nformat = NumberFormat.getIntegerInstance();
		int nextnum = Integer.parseInt(num)+1;
		nformat.setMinimumIntegerDigits(8);
		return nformat.format(nextnum).replaceAll(",", "");
	}

	
}
