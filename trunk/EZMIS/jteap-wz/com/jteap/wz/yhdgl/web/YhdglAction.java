/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.yhdgl.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
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
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.manager.CrkrzmxManager;
import com.jteap.wz.crkrzgl.model.Crkrzgl;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;
import com.jteap.wz.yhdgl.manager.YhdJdbcManager;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class YhdglAction extends AbstractAction {
	private YhdglManager yhdglManager;
	private YhdmxManager yhdmxManager;
	private CgjhmxManager cgjhmxManager;
	private CrkrzglManager crkrzglManager;
	private CrkrzmxManager crkrzmxManager;
	private PersonManager personManager;
	private XqjhDetailManager xqjhDetailManager;
	private XqjhManager xqjhManager;
	private XqjhsqDetailManager xqjhsqDetailManager;
	private XqjhsqManager xqjhsqManager;
	private YhdJdbcManager yhdJdbcManager;

	public YhdJdbcManager getYhdJdbcManager() {
		return yhdJdbcManager;
	}

	public void setYhdJdbcManager(YhdJdbcManager yhdJdbcManager) {
		this.yhdJdbcManager = yhdJdbcManager;
	}

	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

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

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}

	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try {
			this.isUseQueryCache = false;
			String hqlWhere = StringUtil.isoToUTF8(request
					.getParameter("queryParamsSql"));
			String sort = request.getParameter("sort");
			String dir = request.getParameter("dir");
			// 默认状态为未生效
			String zt = "0";
			zt = request.getParameter("zt");
			HqlUtil.addCondition(hql, "zt", zt);
			if ("0".equals(zt)) {
				HqlUtil.addCondition(hql, "bgy", this.sessionAttrs
						.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME));
			} else if ("2".equals(zt)) {
				HqlUtil.addCondition(hql, "id",
						"select yhdgl from Yhdmx t where t.zt='2'",
						HqlUtil.LOGIC_OR, HqlUtil.TYPE_IN);
				HqlUtil.addCondition(hql, "cgy", this.sessionAttrs
						.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME));
			} else {

			}

			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		
			if (!this.isHaveSortField()) {
				HqlUtil.addOrder(hql, "bh", "desc");
			}else{
				if("htbh".equals(sort)){
					HqlUtil.addOrder(hql, "yhdmxs.cgjhmx.cgjhgl.bh", dir);
				}else{
					HqlUtil.addOrder(hql, sort, dir);
				}
			}
			System.out.println(hql.toString());
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/*     */   public String showListAction()
	/*     */     throws Exception
	/*     */   {
	/*     */     try
	/*     */     {
	/* 101 */       StringBuffer hql = getPageBaseHql();
	/*     */ 
	/* 104 */       beforeShowList(this.request, this.response, hql);
	/* 105 */       Object pageFlag = this.request.getAttribute("PAGE_FLAG");
	/* 106 */       if (pageFlag == null)
	/* 107 */         pageFlag = this.request.getParameter("PAGE_FLAG");
	/*     */       String json;
	/* 109 */       if ((pageFlag != null) && (pageFlag.toString().equals("PAGE_FLAG_NO"))) {
	/* 110 */         Collection list = getManager().find(hql.toString(), this.showListHqlValues.toArray());
	/* 111 */        json = JSONUtil.listToJson(list, listJsonProperties());
	/* 112 */         json = "{totalCount:'" + list.size() + "',list:" + 
	/* 113 */           json + "}";
	/*     */       }
	/*     */       else {
	/* 116 */         json = getPageCollectionJson(hql.toString(), this.showListHqlValues.toArray());
	/*     */       }
	/* 118 */       outputJson(json);
	/*     */     } catch (Exception ex) {
	/* 120 */       throw new BusinessException("显示列表异常", ex);
	/*     */     }
	/* 122 */     return "none";
	/*     */   }

	/**
	 * 验货单生效
	 */
	public String enableYhdAction() throws Exception {
		// 选中的明细ID和ID对应的实际生效数量
		String ids = request.getParameter("ids");
		// 选中的主单ID
		String yhdid = request.getParameter("yhdid");
		//type（'1'表示需要补需求计划；'0'表示不需要不需求计划）
		String type = request.getParameter("type");
		try {
			if (ids != null && yhdid != null) {
				String[] array_id = ids.split(",");
				Map<String, String> map_id_sxsl = new HashMap<String, String>();
				for (int i = 0; i < array_id.length; i++) {
					String[] idAndSxsl = array_id[i].split("=");
					map_id_sxsl.put(idAndSxsl[0], idAndSxsl[1]);
					// 通过采购计划明细获得需求计划申请明细相关物资，修改状态为"已采购"
					Yhdmx yhdmx = yhdmxManager.get(idAndSxsl[0]);
					Cgjhmx cgjhmx = cgjhmxManager
							.get(yhdmx.getCgjhmx().getId());
					//将对应采购计划明细状态修改为"2"(已到货) 如果到货数量等于采购数量 则修改状态值
					if(cgjhmx.getDhsl()>=cgjhmx.getCgsl()){
						cgjhmx.setZt("2");
						cgjhmxManager.save(cgjhmx);  
					}
					String wzbm = cgjhmx.getWzdagl().getId();
					if (StringUtil.isNotEmpty(cgjhmx.getXqjhmx())) {
						XqjhDetail xqjhDetail = xqjhDetailManager.get(cgjhmx
								.getXqjhmx());
						Xqjh xqjh = xqjhManager.get(xqjhDetail.getXqjh()
								.getId());
						Xqjhsq xqjhsq = xqjhsqManager.get(xqjh.getXqjhsqbh());
						String xqjhsqId = xqjhsq.getId();
						String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ? and x.wzbm= ?";
						Object args[] = { xqjhsqId, wzbm };
						List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
						xqjhsqDetailList = xqjhsqManager.find(hql, args);
						for (int j = 0; j < xqjhsqDetailList.size(); j++) {
							XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) xqjhsqDetailList
									.get(0);
							xqjhsqDetail.setStatus("可申领");
							xqjhsqDetailManager.save(xqjhsqDetail);
						}
					}
				}
				// List<Yhdmx> yhmxs =
				// yhdmxManager.findByIds((String[])map_id_sxsl.keySet().toArray());
				Yhdgl yhdgl = yhdglManager.findUniqueBy("id", yhdid);
				Set<Yhdmx> yhmxs = yhdgl.getYhdmxs();
				List<Yhdmx> new_yhdmxs = new ArrayList<Yhdmx>();
				String bz = "";
				// 作为所有明细是不是全部生效
				boolean all_enable = true;
				for (Yhdmx obj : yhmxs) {
					if (map_id_sxsl.containsKey(obj.getId())) {
						// 实际生效数量
						double sxsl = Double.parseDouble(map_id_sxsl.get(obj
								.getId()));
						// 验收数量
						double yssl = obj.getYssl();
						// 剩余数量
						double sysl = yssl - sxsl;
						// 这里为物资部分数量生效
						if (sysl > 0) {
							Yhdmx yhdmx = new Yhdmx();
							BeanUtils.copyProperties(yhdmx, obj);
							// id设置为null，因为主键生成策略是自动生成uuid
							yhdmx.setId(null);
							// 到货数量和验收数量统一设置为实际生效数量
							yhdmx.setDhsl(sxsl);
							yhdmx.setYssl(sxsl);
							yhdmx.setZt("2");
							//将验货单明细BL_ZT、JL_ZT设置为"0"(供借料、补料申请选择使用),【0】表示未选择过；【1】表示已经选择
							yhdmx.setBlzt("0");
							yhdmx.setJlzt("0");
							yhdmx.setXh(String.valueOf(new_yhdmxs.size()
									+ yhmxs.size() + 1));
							yhdmx.setYhdgl(yhdgl);

							// 原有的明细单到货数量要减去实际生效数量
							obj.setDhsl(obj.getDhsl() - sxsl);
							// 设置生效后的验收数量
							obj.setYssl(sysl);

							new_yhdmxs.add(yhdmx);
							// 有新明细生成则一定主单为未生效
							all_enable = false;
						} else {
							obj.setZt("2");
						}
						//加入班组
						bz = obj.getCgjhmx().getXqjhDetail().getXqjh().getSqbm();
					} else {
						if ("0".equals(obj.getZt())) {
							all_enable = false;
						}
					}
				}

				// 加入新的验货单明细
				yhmxs.addAll(new_yhdmxs);

				// 如果所有的明细都生效了那么主单自动生效
				if (all_enable) {
					yhdgl.setZt("2");
				}

				yhdgl.setYsrq(new Date());
				yhdgl.setYhdmxs(yhmxs);
				//放入班组
				if(StringUtil.isNotEmpty(bz)){
					yhdgl.setBz(bz);
				}
				//将验货单BL_ZT、JL_ZT设置为"0"(供借料、补料申请选择使用),【0】表示未选择过；【1】表示已经选择
				yhdgl.setBlzt("0");
				yhdgl.setJlzt("0");
				yhdglManager.save(yhdgl);
				// 自由入库生效以后，需要起草一个补料计划申请流程(flag表示区分是正常入库验货单【1】,自由入库验货单【2】)
				if (("2").equals(yhdgl.getFlag()) && ("1").equals(type)) {
					draftNewWorkFlowInstance(yhdgl);
				}
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	/**
	 * 入库生效
	 */
	public String enableRKAction() throws Exception {
		// 选中的明细ID和ID对应的实际生效数量
		String ids = request.getParameter("ids");
		// 选中的主单ID
		String yhdid = request.getParameter("yhdid");
		try {
			if (ids != null && yhdid != null) {
				// 将获取的明细ID和对应生效的数量组装成一个Map，只生效Map中有的明细
				String[] array_id = ids.split(",");
				Map<String, String> map_id_sxsl = new HashMap<String, String>();
				for (int i = 0; i < array_id.length; i++) {
					String[] idAndSxsl = array_id[i].split("=");
					map_id_sxsl.put(idAndSxsl[0], idAndSxsl[1]);
				}

				// 获取主单
				Yhdgl yhdgl = yhdglManager.findUniqueBy("id", yhdid);
				// 获取主单对应的明细单
				Set<Yhdmx> yhdmxs = yhdgl.getYhdmxs();
				// 临时存放由于只生效部分数量而产生的新的明细单
				List<Yhdmx> new_yhdmxs = new ArrayList<Yhdmx>();
				// 获取当先出入库和出入库明细中最大的编号
				String crkmxbh = this
						.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
				String crkbh = this
						.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");

				// 入库后自动生成出入库日志单
				Crkrzgl crkrzgl = new Crkrzgl();
				Set crkrzmxs = crkrzgl.getCrkrzmxs();
				// 出库编码
				String ckbm = "";
				// 出入库明细的序号
				int index = 1;
				// 作为所有明细是不是全部生效
				boolean all_enable = true;
				for (Yhdmx obj : yhdmxs) {
					if (map_id_sxsl.containsKey(obj.getId())) {
						// 实际生效数量
						double sxsl = Double.parseDouble(map_id_sxsl.get(obj
								.getId()));
						// 验收数量
						double yssl = obj.getYssl();
						// 剩余数量
						double sysl = yssl - sxsl;

						// 物资平均价
						double ppdj = 0;

						if (sysl > 0) {
							Yhdmx yhdmx = new Yhdmx();
							BeanUtils.copyProperties(yhdmx, obj);
							// id设置为null，因为主键生成策略是自动生成uuid
							yhdmx.setId(null);
							// 到货数量和验收数量统一设置为实际生效数量
							yhdmx.setDhsl(sxsl);
							yhdmx.setYssl(sxsl);
							yhdmx.setZt("1");
							yhdmx.setXh(String.valueOf(new_yhdmxs.size()
									+ yhdmxs.size() + 1));
							yhdmx.setYhdgl(yhdgl);
							yhdmx.setRksj(new Date());
							yhdmx.setSysl(sxsl);

							// 原有的明细单到货数量要减去实际生效数量
							obj.setDhsl(obj.getDhsl() - sxsl);
							// 设置生效后的验收数量
							obj.setYssl(sysl);
							// 清空未生效部分的发票号码，税前价，税率，杂费
							obj.setFpbh("");
							obj.setSqdj(0.0);
							obj.setSl(0.0);
							obj.setZf(0.0);

							new_yhdmxs.add(yhdmx);

							// 有新明细生成则一定主单为未生效
							all_enable = false;
						} else {
							// 全数生效只用修改明细的状态
							obj.setZt("1");
							obj.setRksj(new Date());
							obj.setSysl(sxsl);
						}

						// 入库后重新修改物资的当前库存以及平均价格
						Wzda wzda = obj.getWzdagl();
						wzda.setDqkc(wzda.getDqkc() + sxsl);
						ppdj = obj.getSqdj()
								+ (obj.getSqdj()
										* (obj.getSl() != null ? obj.getSl()
												: 0) / sxsl);
						wzda.setPjj((wzda.getPjj() + ppdj) / 2);

						// 设置并保存出入库明细单信息
						Crkrzmx crk = new Crkrzmx();
						ckbm = obj.getWzdagl().getKw().getCkid();
						crk.setCrkrzgl(crkrzgl);
						crk.setWzda(obj.getWzdagl());
						crk.setCrksl(sxsl);
						//修改计划单价为物资档案的计划单价 修改日期：2011-5-31
						crk.setCrkjg(obj.getWzdagl().getJhdj());
						crk.setCrkdh(crkmxbh);
						crkmxbh = this.getNextNum(crkmxbh);
						crk.setXh(String.valueOf(index));
						index++;
						crkrzmxs.add(crk);
					} else {
						if (!"1".equals(obj.getZt())) {
							all_enable = false;
						}
					}
				}
				// 设置并保存出入库主单信息
				crkrzgl.setCrkdh(crkbh);
				crkbh = this.getNextNum(crkbh);
				crkrzgl.setCrksj(new Date());
				crkrzgl.setCkbh(ckbm);
				String curPersonId = (String) this.sessionAttrs
						.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
				String curPersonName = (String) this.sessionAttrs
						.get(Constants.SESSION_CURRENT_PERSON_NAME);
				crkrzgl.setCzr(curPersonId);
				crkrzgl.setCzrxm(curPersonName);
				crkrzgl.setCrklb("01");
				crkrzgl.setCrkqf("1");
				crkrzgl.setXgdjbh(yhdgl.getBgy());

				// 加入新的验货单明细
				yhdmxs.addAll(new_yhdmxs);

				// 如果所有的明细都生效了那么主单自动生效
				if (all_enable) {
					yhdgl.setZt("1");
				}

				// 持久化
				yhdgl.setYsrq(new Date());
				yhdgl.setYhdmxs(yhdmxs);
				crkrzglManager.save(crkrzgl);
				yhdglManager.save(yhdgl);
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
			throw e;
		}
		return NONE;
	}

	/**
	 * 获取目标表中最大的编号，生成新的编号，规则：最大编号+1
	 * 
	 * @param entityManager
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	private String getMaxNum(String sql) throws Exception {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			// String sql = sql;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if (StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
					int max = Integer.parseInt(ckbmMax) + 1;
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
	 * 删除操作，删除验货单及其明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Yhdgl yhdgl = null;
		boolean delYhd = true;
		boolean sxYhd = false;
		try {
			if (keys != null) {
				// 删除明细表
				String yhdKeys[] = keys.split(",");
				for (int i = 0; i < yhdKeys.length; i++) {
					yhdgl = yhdglManager.get((Serializable) yhdKeys[i]);
					List<Yhdmx> yhdmx = yhdmxManager.findBy("yhdgl", yhdgl);
					for (Yhdmx mx : yhdmx) {
						List<Cgjhmx> cgmx = cgjhmxManager
								.findByIds(new String[] { mx.getCgjhmx()
										.getId() });
						for (Cgjhmx cmx : cgmx) {
							double num = cmx.getDhsl();
							cmx.setDhsl(num - mx.getDhsl());
							cgjhmxManager.save(cmx);
							if (cmx.getXqjhmx() != null
									&& !cmx.getXqjhmx().equals("")) {
								XqjhDetail detail = cmx.getXqjhDetail();
								detail.setDhsl(detail.getDhsl() - mx.getDhsl());
								xqjhDetailManager.save(detail);
							}
						}
						//如果明细中有已入库的 验货明细 则不能删除 （吕超 2011-9-27）
						if("1".equals(mx.getZt())){
							delYhd = false;
						}else if("2".equals(mx.getZt())){ //如果有已经生效的待验收不能删除
							sxYhd = true;
							delYhd = false;
						}else{
							yhdmxManager.remove(mx);
						}
					}
					// 如果没有已生效 或已入库的 入库单 则主单
					if(delYhd){
						yhdglManager.remove(yhdgl);
					}else{ //否则 修改主单的状态
						if(sxYhd){ //如果有还未入库的 则改状态为 未入库
							yhdgl.setZt("2");
						}else{
							yhdgl.setZt("1");
						}
						yhdglManager.save(yhdgl);
					}
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	@SuppressWarnings("deprecation")
	public void printCgjhglAction() {
		String path = request.getSession().getServletContext().getRealPath(
				"/jteap/wz/cgjhgl/cgjhd.xls");
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
		}
		java.io.BufferedOutputStream outs = null;
		try {
			byte[] data = null;
			HSSFWorkbook wb = null;
			File excel = new File(path);
			InputStream is = new FileInputStream(excel);
			wb = new HSSFWorkbook(is);
			HSSFSheet rs = wb.getSheetAt(0);
			HSSFCellStyle cs = wb.createCellStyle();// 创建一个style
			HSSFFont littleFont = wb.createFont();// 创建一个Font
			littleFont.setFontName("SimSun");
			littleFont.setFontHeightInPoints((short) 10);
			cs.setFont(littleFont);// 设置字体
			cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居中
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			Cgjhgl cgjhgl = (Cgjhgl) this.getManager().get((Serializable) id);
			Set<Cgjhmx> mxs = cgjhgl.getCgjhmxs();
			int rowIndex = 4;

			// 编号
			HSSFCell bh = rs.getRow(1).getCell((short) 1);
			bh.setCellValue(cgjhgl.getBh());
			// 制定日期
			HSSFCell zdrq = rs.getRow(1).getCell((short) 4);
			if (cgjhgl.getZdsj() != null)
				zdrq.setCellValue(DateUtils.formatDate(cgjhgl.getZdsj(),
						"yyyy-MM-dd"));
			// 生效日期
			HSSFCell sxsj = rs.getRow(1).getCell((short) 6);
			if (cgjhgl.getSxsj() != null)
				sxsj.setCellValue(DateUtils.formatDate(cgjhgl.getSxsj(),
						"yyyy-MM-dd"));
			// 备注
			HSSFCell bz = rs.getRow(2).getCell((short) 1);
			bz.setCellValue(cgjhgl.getBz());
			// bz.setEncoding(HSSFCell.ENCODING_UTF_16);
			for (Iterator<Cgjhmx> iterator = mxs.iterator(); iterator.hasNext();) {
				Cgjhmx cgjhmx = (Cgjhmx) iterator.next();
				rs.addMergedRegion(new Region(rowIndex, (short) 0,
						rowIndex + 1, (short) 0)); // 指定合并区域
				rs.addMergedRegion(new Region(rowIndex, (short) 2,
						rowIndex + 1, (short) 2));
				rs.addMergedRegion(new Region(rowIndex, (short) 3,
						rowIndex + 1, (short) 3));
				rs.addMergedRegion(new Region(rowIndex, (short) 4,
						rowIndex + 1, (short) 4));
				rs.addMergedRegion(new Region(rowIndex, (short) 5,
						rowIndex + 1, (short) 5));
				rs.addMergedRegion(new Region(rowIndex, (short) 6,
						rowIndex + 1, (short) 6));
				rs.addMergedRegion(new Region(rowIndex, (short) 7,
						rowIndex + 1, (short) 7));
				// 第一列 序号
				HSSFCell ce = rs.getRow(rowIndex).createCell(0);
				HSSFCell ce1 = rs.getRow(rowIndex + 1).createCell(0);
				ce.setCellStyle(cs);
				ce1.setCellStyle(cs);
				ce.setCellValue(cgjhmx.getXh());
				// 第二列 物资名称规格
				HSSFCell wzmc = rs.getRow(rowIndex).getCell(1);
				wzmc.setCellStyle(cs);
				HSSFCell xhgg = rs.getRow(rowIndex + 1).getCell(1);
				xhgg.setCellStyle(cs);
				// wzmc.setEncoding(HSSFCell.ENCODING_UTF_16);
				// xhgg.setEncoding(HSSFCell.ENCODING_UTF_16);
				wzmc.setCellValue(cgjhmx.getWzdagl().getWzmc());
				xhgg.setCellValue(cgjhmx.getWzdagl().getXhgg());
				// 第三列 计划单价
				HSSFCell jhdj = rs.getRow(rowIndex).getCell(2);
				jhdj.setCellValue(cgjhmx.getWzdagl().getJhdj());
				rs.getRow(rowIndex).getCell(2).setCellStyle(cs);
				rs.getRow(rowIndex + 1).getCell(2).setCellStyle(cs);
				// 第四列 计量单位

				HSSFCell jldw = rs.getRow(rowIndex).getCell((short) 3);
				// jldw.setEncoding(HSSFCell.ENCODING_UTF_16);

				jldw.setCellValue(cgjhmx.getWzdagl().getJldw());
				rs.getRow(rowIndex).getCell(3).setCellStyle(cs);
				rs.getRow(rowIndex + 1).getCell(3).setCellStyle(cs);
				// 第五列 采购数量
				HSSFCell cgsl = rs.getRow(rowIndex).getCell(4);
				cgsl.setCellValue(cgjhmx.getCgsl());
				rs.getRow(rowIndex).getCell(4).setCellStyle(cs);
				rs.getRow(rowIndex + 1).getCell(4).setCellStyle(cs);
				// 第六列 换算系数
				HSSFCell hsxs = rs.getRow(rowIndex).getCell(5);
				hsxs.setCellValue(cgjhmx.getHsxs());
				rs.getRow(rowIndex).getCell(5).setCellStyle(cs);
				rs.getRow(rowIndex + 1).getCell(5).setCellStyle(cs);
				// 第七列 计划到货日期
				HSSFCell jhdhrq = rs.getRow(rowIndex).createCell(6);
				HSSFCell jhdhrq1 = rs.getRow(rowIndex + 1).createCell(6);
				jhdhrq.setCellValue(DateUtils.formatDate(cgjhmx.getJhdhrq(),
						"yyyy-MM-dd"));
				jhdhrq.setCellStyle(cs);
				jhdhrq1.setCellStyle(cs);
				// 第八列 采购员

				HSSFCell cgy = rs.getRow(rowIndex).createCell((short) 7);
				HSSFCell cgy1 = rs.getRow(rowIndex + 1).createCell((short) 7);
				// cgy.setEncoding(HSSFCell.ENCODING_UTF_16);
				cgy.setCellValue(cgjhmx.getPerson().getUserName());
				cgy.setCellStyle(cs);
				cgy1.setCellStyle(cs);

				rowIndex = rowIndex + 2;
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			data = os.toByteArray();

			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename="
							+ new String(("采购计划单_"
									+ DateUtils.getDate("yyyy-MM-dd") + ".xls")
									.getBytes(), "iso-8859-1"));

			// 开始输出
			outs = new java.io.BufferedOutputStream(response.getOutputStream());
			outs.write(data);
			outs.flush();
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private String getNextNum(String num) {
		NumberFormat nformat = NumberFormat.getIntegerInstance();
		int nextnum = Integer.parseInt(num) + 1;
		nformat.setMinimumIntegerDigits(8);
		return nformat.format(nextnum).replaceAll(",", "");
	}

	@Override
	public HibernateEntityDao getManager() {
		return yhdglManager;
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
	public String draftNewWorkFlowInstance(Yhdgl yhdgl) throws Exception {
		// //领用单id
		// String lysqbh =
		// (String)context.getContextInstance().getVariable("docid");
		//		
		// String lydid = findLydId(lysqbh);

		// 自由入库验货单id
		String yhdid = yhdgl.getId();

		// 自由入库验货单所属班组
		String groupName = yhdgl.getBz();

		// 补料计划申请编号
		XqjhsqJDBCManager xqjhsqJDBCManager = (XqjhsqJDBCManager) SpringContextUtil
				.getBean("xqjhsqJDBCManager");
		String bljhsqBH = xqjhsqJDBCManager.getBljhsqBHMax(2);

		// 业务数据编号
		String docid = UUIDGenerator.hibernateUUID();
		insertXqjhsq(docid, yhdid, "2", bljhsqBH);
		String taskName = "填写申请";
		// 借料申请起草人
		String userLoginName = yhdgl.getBgy();

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
		if(StringUtil.isEmpty( returnValue.toString())){
			return returnValue.toString();
		}else{
			return returnValue.toString().substring(0,
					returnValue.toString().length() - 1);
		}
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
			String bljhsqBH) {
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "insert into tb_wz_xqjhsq(id,yhdid,xqjhqf,xqjhsqbh) values('"
					+ id
					+ "','"
					+ yhdid
					+ "','"
					+ xqjhqf
					+ "','"
					+ bljhsqBH
					+ "')";
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
	public String[] listJsonProperties() {
		String lists=request.getParameter("lists");
		if(lists!=null&&lists.equals("yhd")){
			return new String[]{"bh","htbh","ghdw","personBgy","id","userName","personCgy","id","userName","zt","id","dhrq","time"};
		}else 
		return new String[] { "ysrq", "ghdw", "cgy", "bz", "flag", "personCgy",
				"personBgy", "id", "userName", "zt", "id", "bgy", "bh", "htbh",
				"dhrq", "yhdmxs", "cgjhmx", "xqjhDetail", "xqjh", "sqbm","cgjhgl", "bh",
				"time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ysrq", "ghdw", "cgy", "zt", "id", "bgy", "bh",
				"htbh", "dhrq", "time" };
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

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
}
