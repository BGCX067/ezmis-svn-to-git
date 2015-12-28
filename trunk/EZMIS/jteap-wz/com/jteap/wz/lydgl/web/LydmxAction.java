/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.lydgl.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.apache.poi.ss.util.CellRangeAddress;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.lydgl.manager.LydglManager;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydgl;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.yhdmx.manager.YhdmxManager;

@SuppressWarnings( { "serial", "unchecked" })
public class LydmxAction extends AbstractAction {
	private LydmxManager lydmxManager;
	private LydglManager lydglManager;
	private CgjhmxManager cgjhmxManager;
	private YhdmxManager yhdmxManager;
	
	public LydmxManager getLydmxManager() {
		return lydmxManager;
	}

	public void setLydmxManager(LydmxManager lydmxManager) {
		this.lydmxManager = lydmxManager;
	}
	
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	public LydglManager getLydglManager() {
		return lydglManager;
	}

	public void setLydglManager(LydglManager lydglManager) {
		this.lydglManager = lydglManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try {
			this.isUseQueryCache = false;
			String hqlWhere = request.getParameter("queryParamsSql");
			String ckgl = request.getParameter("ckgl");
			String zt = request.getParameter("zt");
			if (StringUtils.isNotEmpty(ckgl)) {
				 HqlUtil.addCondition(hql,"wzbm.kw.ckid",ckgl);
			}
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
			if("9".equals(zt)){
				HqlUtil.addCondition(hql, "zfzt", "1");
			}else{
				HqlUtil.addCondition(hql, "zfzt", "0");
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		HqlUtil.addOrder(hql, "zt", "asc");
		HqlUtil.addOrder(hql, "lydgl.lysj", "desc");
		if (!this.isHaveSortField()) {
			//HqlUtil.addOrder(hql, "xh", "asc");
		}
		//System.out.println(hql.toString());
	}
	//初始化领用单明细中的 实际金额
	public String initAction(){
		lydmxManager.initSjje();
		return NONE;
	}
	/**
	 * 显示列表动作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showListAction() throws Exception {
		try {
			StringBuffer hql = getPageBaseHql();

			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			beforeShowList(request, response, hql);
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
//			System.out.println(json);
			this.outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}
	/**
	 * 修改领用单明细工程项目工程类别
	 * @return
	 * @throws Exception
	 */
	public String updateGcxmAction() throws Exception{
		try{
			String gcxm = request.getParameter("gcxm");
			String gclb = request.getParameter("gclb");
			String ids = request.getParameter("ids");
			for(String id:ids.split(",")){
				Lydmx lydmx = lydmxManager.findUniqueBy("id", id);
				lydmx.setGclb(gclb);
				lydmx.setGcxm(gcxm);
				lydmxManager.save(lydmx);
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
	 * 根据hql，分页查询 lvchao
	 * 
	 * @param hql
	 * @return
	 */
	protected String getPageCollectionJson(String hql, Object... values) {
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// System.out.println(hql);
		// 开始分页查询
		Page page = getManager().pagedQueryWithStartIndex(hql,
				Integer.parseInt(start), Integer.parseInt(limit),
				isUseQueryCache, values);
		Collection obj = (Collection) page.getResult();
		// System.out.println(hql);
		List<Lydmx> lydmxList = (List) obj;
		Map countMaps = new HashMap();
		List<Lydmx> list = lydmxManager.find(hql);
		if (list.size() > 0) {
			// 添加合计
			countMaps.put("id", UUIDGenerator.hibernateUUID());
			countMaps.put("xh", "<font color='red'>总合计：</font>");
			lydmxManager.setCountValue(list, countMaps, "h");
		} else {
			countMaps = new HashMap();
		}
		
		// 分页数据集合
		List jsonList = new ArrayList();
		for (Lydmx lydmx : lydmxList) {
			Map lydmxMap = new HashMap();
			// "wzbm",
//			"xhgg", "jldw", "jhdj", "dqkc", "yfpsl", "pzlysl", "sjlysl",
//			"zt", "kw", "ck", "ckmc", "time","xqjhDetail" 
			lydmxMap.put("id", lydmx.getId());
			lydmxMap.put("xh", lydmx.getXh());
			lydmxMap.put("jhje", lydmx.getSjlysl()*lydmx.getWzbm().getJhdj());
			// lydmxMap.put("xqjhDetail",yhdmx.getCgjhmx().getXqjhDetail());
//			if(lydmx.getWzlysqDetail().getXqjhDetail()!=null){
//				lydmxMap.put("gclb",lydmx.getWzlysqDetail().getXqjhDetail().getXqjh().getGclb());
//				lydmxMap.put("gcxm",lydmx.getWzlysqDetail().getXqjhDetail().getXqjh().getGcxm());
//			}else{
//				lydmxMap.put("gclb","");
//				lydmxMap.put("gcxm","");
//			}
			lydmxMap.put("gclb",lydmx.getGclb());
			lydmxMap.put("gcxm",lydmx.getGcxm());
			// lydmxMap.put("sqbm",yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getSqbm());
			lydmxMap.put("lydqf", lydmx.getLydgl().getLydqf());
			lydmxMap.put("lydid", lydmx.getLydgl().getId());
			lydmxMap.put("lysj", (lydmx.getLysj()!=null)?DateUtils.formatDate(lydmx.getLysj(),"yyyy-MM-dd"):"");
			lydmxMap.put("lybm",lydmx.getLydgl().getLybm());
			lydmxMap.put("lysqbh", lydmx.getWzlysqDetail().getWzlysq().getBh());
			lydmxMap.put("llr", lydmx.getLydgl().getLlr());
			lydmxMap.put("czr", lydmx.getLydgl().getCzr());
			lydmxMap.put("sqsj", DateUtils.formatDate(lydmx.getWzlysqDetail().getWzlysq().getSqsj(),"yyyy-MM-dd"));
			lydmxMap.put("bh", lydmx.getLydgl().getBh());
			lydmxMap.put("wzbm", lydmx.getWzbm().getId());
			lydmxMap.put("xhgg", lydmx.getWzbm().getXhgg());
			lydmxMap.put("wzmc", lydmx.getWzbm().getWzmc());
			lydmxMap.put("jldw", lydmx.getWzbm().getJldw());
			lydmxMap.put("jhdj",lydmx.getWzbm().getJhdj());
			lydmxMap.put("dqkc", lydmx.getWzbm().getDqkc());
			lydmxMap.put("zt",lydmx.getZt());
			lydmxMap.put("ckmc", lydmx.getWzbm().getKw().getCk().getCkmc());
			lydmxMap.put("sjlysl", lydmx.getSjlysl());
			lydmxMap.put("pzlysl", lydmx.getPzlysl());
			lydmxMap.put("sjdj",lydmx.getSjje()/lydmx.getPzlysl());
			lydmxMap.put("xqjhmx",(lydmx.getWzlysqDetail().getXqjhDetail()!=null)?true:false);
//			lydmxMap.put("xqjhmx",lydmx.getWzlysqDetail().getXqjhDetail());
			lydmxMap.put("zfzt", lydmx.getZfzt());
			jsonList.add(lydmxMap);
		}
		String[] jsonProperties = new String[]{"id", "lysj", "lydqf", "gclb", "gcxm","lydid",
				"lybm", "lysqbh", "llr", "czr","sqsj", "bh", "wzbm",
				"xhgg", "wzmc", "xh","jldw", "jhdj", "dqkc","zt", "ckmc","pzlysl","sjdj","zfzt","xqjhmx"};
		// 将集合JSON化
		String json = JSONUtil.listToJson(jsonList, jsonProperties);
		// 总合计的json字符串
		String mainTotalJson = JSONUtil.mapToJson(countMaps);
		StringBuffer dataBlock = new StringBuffer();
		dataBlock.append("{totalCount:'" + page.getTotalCount() + "',list:"
				+ json + ",mainTotal:" + mainTotalJson + "}");
		return dataBlock.toString();
	}
	/**
	 * 撤销作废
	 * @return
	 * @throws Exception 
	 */
	public String rollBackAction() throws Exception{
		// TODO Auto-generated method stub
		//领用单id
		String lydid = request.getParameter("id");
		//领用单明细id
		String mxids = request.getParameter("ids");
		try{
			String[] mxid = mxids.split(",");
			//将明细的 状态改为未生效 已作废状态改为0
			for(String id :mxid){
				Lydmx lydmx = lydmxManager.findUniqueBy("id", id);
				//已作废
				lydmx.setZt("0");
				lydmx.setZfzt("0");
				lydmxManager.save(lydmx);
			}
			//取出领料单对象
			Lydgl lyd = lydglManager.findUniqueBy("id", lydid.split(",")[0]);
			boolean is_zf = false;
			//查询该主单下面是否有作废的明细 如果有 则已作废状态为true
			for(Lydmx lydmx:lyd.getLydmxs()){
				if("9".equals(lydmx.getZt())){
					is_zf = true;
					break;
				}
			}
			lyd.setZt("0");
			//如果已作废状态为fals 则将该主单作废状态改为0
			if(!is_zf){
				lyd.setZfzt("0");
			}
			lydglManager.save(lyd);
			outputJson("{success:true}");
		}catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		
		return NONE;
	}
	/**
	 * 领料单明细作废
	 */
	@Override
	public String removeAction() throws Exception {
		// TODO Auto-generated method stub
		//领用单id
		String lydid = request.getParameter("id");
		//领用单明细id
		String mxids = request.getParameter("ids");
		//0为作废 1为删除
		String flag = request.getParameter("flag");
		//是否删除标记
		boolean is_del = ("0".equals(flag))?false:true;
		try{
			String[] mxid = mxids.split(",");
			//取出领料单对象
			Lydgl lyd = lydglManager.findUniqueBy("id", lydid.split(",")[0]);
			//先删除领用单明细
			for(String id :mxid){
				Lydmx lydmx = lydmxManager.findUniqueBy("id", id);
				if(is_del){
					lyd.getLydmxs().remove(lydmx);
					lydmxManager.remove(lydmx);
				}else{
					//已作废
					lydmx.setZt("9");
					lydmx.setZfzt("1");
					lydmxManager.save(lydmx);
				}
				
			}
			//lydmxManager.removeBatch(mxid.split(","));
			
			//如果该领料单对象下没有明细 则删除该领料单
			if(lyd.getLydmxs().size()<=0){
				lydglManager.remove(lyd);
			}else{
				//是否已经领用完毕 默认领用完毕
				boolean is_ly = true;
				//是否有作废
				boolean is_zf = false;
				for(Lydmx lydmx:lyd.getLydmxs()){
					//如果有为作废的  则改作废状态为false 继续判断是否生效
					if("9".equals(lydmx.getZt())){
						is_zf = true;
					}
					//如果明细中有没有生效的 则改状态为false
					if("0".equals(lydmx.getZt())){
						is_ly = false;
					} 
				}
				//如果作废状态为true 则是全部作废 改主单状态为作废\
				if(is_zf){
					lyd.setZt("9");
					lyd.setZfzt("1");
				}else{
					lyd.setZfzt("0");
				}
				//如果生效状态为true
				if(is_ly){ 
					lyd.setZt("1");
				}else{
					lyd.setZt("0");
				}
				lydglManager.save(lyd);
			}
			outputJson("{success:true}");
		}catch (Exception ex) {
			ex.printStackTrace();
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		
		return NONE;
	}
	//导出Excel
	public String exportExcel() throws Exception {
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = "领用单编号,物资名称,型号规格,仓库名称,计量单位,实际单价,实际领用数量,实际金额," +
							"领用时间,操作人,领料人,工程类别,工程项目,领用部门";
		//System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex ="lydgl.bh,wzbm.wzmc,wzbm.xhgg,wzbm.kw.ck.ckmc,jldw,sjdj,sjlysl,sjje," +
							   "lydgl.lysj,lydgl.czr,lydgl.llr,gclb,gcxm,lydgl.lybm";
		//System.out.println(paraDataIndex);
		// 宽度(逗号表达式)
		String paraWidth = "100,100,80,60,70,70,70,80,70,100,100,100,100,100,100,100";
		//条件
		String hqlWhere  =  request.getParameter("parWhere");
		
		StringBuffer hql  = new StringBuffer("from Lydmx obj ");
		
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		HqlUtil.addOrder(hql, "obj.lydgl.lysj","desc" );
		List<Lydmx> lydmxs = lydmxManager.find(hql.toString());
		if (lydmxs.size() < 1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		}
		
		export(lydmxs, paraHeader, paraDataIndex, paraWidth);
	 
		// 调用导出方法
		return NONE;
	}
	
	//导出Excel
	public String exportExcelByLyd() throws Exception {
		String lydid = request.getParameter("lydid");
		String mxid  = request.getParameter("mxid");
//		System.out.println(lydid);
//		System.out.println(mxid);
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = "物资名称,型号规格,批准领用数量,领料人,领用时间";
		//System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex ="wzbm.wzmc,wzbm.xhgg,pzlysl,lydgl.llr,nowDate";
		//System.out.println(paraDataIndex);
		// 宽度(逗号表达式)
		String paraWidth = "100,100,60,70,100";
		//条件
		
		StringBuffer hql  = new StringBuffer("from Lydmx obj ");
		if(StringUtils.isNotEmpty(lydid)){
			hql.append(" where obj.lydgl.id in ("+lydid.substring(0,lydid.length()-1)+")");
		}
		if(StringUtils.isNotEmpty(mxid)){
			hql.append(" where obj.id in ("+mxid.substring(0,mxid.length()-1)+")");
		}
		hql.append(" and obj.zt = '0'");
		List<Lydmx> lydmxs = lydmxManager.find(hql.toString());
		if (lydmxs.size() < 1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out
					.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		}
		
		export(lydmxs, paraHeader, paraDataIndex, paraWidth);
	 
		// 调用导出方法
		return NONE;
	}
	/**
	 * 验货单打印
	 */
	public InputStream exportYhdmxExcel() throws Exception {
		//选定打印
		String id = request.getParameter("id");
		//条件打印
		String parWhere = request.getParameter("parWhere");
	//	String idsArray[] = id.split(",");
		HSSFWorkbook workbook = new HSSFWorkbook();

		// HSSFCellStyle style = workbook.createCellStyle();

		// style.setFillBackgroundColor(HSSFColor.BLUE.index);

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 2700);
		sheet.setColumnWidth(2, 7400);
		sheet.setColumnWidth(3, 800);
		sheet.setColumnWidth(4, 800);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 3000);

		// sheet.setColumnWidth(9, 500);
		// sheet.setColumnWidth(10, 1500);
		HSSFPrintSetup hps = sheet.getPrintSetup();
		// hps.setLandscape(true); //横向打印
		hps.setVResolution((short) 300); // 打印状态
		hps.setPageStart((short) 0); // 起始页码
		hps.setHeaderMargin((double) 0.2); // 页眉
		hps.setFooterMargin((double) 0.6); // 页脚

		sheet.setMargin(HSSFSheet.LeftMargin, (short) 0.6); // 左页边距
		sheet.setMargin(HSSFSheet.RightMargin, (short) 0.2); // 右页边距
		sheet.setMargin(HSSFSheet.TopMargin, (short) 0.6); // 上边距
		sheet.setMargin(HSSFSheet.BottomMargin, (short) 0.6); // 下边距

		sheet.setHorizontallyCenter(true); // 水平居中
		// sheet.setVerticallyCenter(true); //垂直居中

		HSSFFont font2 = workbook.createFont();
		font2.setColor(HSSFColor.BLACK.index);
		font2.setFontHeight((short) 170); // 设置字体大小
		font2.setFontName("宋体"); // 设置单元格字体

		// 创建单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 插入数据样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// style2.setWrapText(true);// 自动换行

		// 合计单元格样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 设置边框
		// style.setBottomBorderColor(HSSFColor.RED.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// style.setFont(font);// 设置字体

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style.setWrapText(true);// 自动换行

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
		style4.setFont(font2);

		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style1.setWrapText(true);// 自动换行

		// 设置单元格内容格式
		HSSFCellStyle styleleft = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		styleleft.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		styleleft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font1 = workbook.createFont();
		font1.setFontHeight((short) 160); // 设置字体大小
		styleleft.setFont(font1);
		styleleft.setWrapText(true);// 自动换行

		// NO验货单编号样式
		HSSFCellStyle style6 = workbook.createCellStyle();
		style6.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style6.setWrapText(true);// 自动换行

		// 第六行样式
		HSSFCellStyle style5 = workbook.createCellStyle();
		style5.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style5.setWrapText(true);// 自动换行

		// 列头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font.setFontHeight((short) 380); // 设置字体大小
		font.setFontName("宋体"); // 设置单元格字体
		font.setUnderline((byte) 1); // 设置下划线
		titleStyle.setFont(font);

		HSSFRow row = null;
		HSSFCell cell = null;
		DecimalFormat decimalFormat = new DecimalFormat("###.0000");
		@SuppressWarnings("unused")
		DecimalFormat decimalFormat2 = new DecimalFormat("###.00");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		StringBuffer hql = new StringBuffer("from Lydmx obj ");
		
		if(StringUtils.isNotEmpty(id)){
			hql.append(" where obj.id in ("+id+")");
		}
		if(StringUtils.isNotEmpty(parWhere)){
			String hqlWhereTemp = parWhere.replace("%25", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		List<Lydmx> lydmxList = lydmxManager.find(hql.toString());
		
		try {
			for (int i = 1; i <= lydmxList.size(); i++) {
					Lydmx lydmx = lydmxList.get(i-1);
					if (lydmx != null) {
						// 上间隔
//						row = sheet.createRow(10 * i - 10);
//						row.setHeight((short) 495);
						Date lysqsj = lydmx.getWzlysqDetail().getWzlysq().getSqsj();
						Date lysj = lydmx.getLysj();
						// 创建标题行
						row = sheet.createRow(10 * i - 9);
						row.setHeight((short) 500);// 设定行的高度
						cell = row.createCell(0);
						cell.setCellValue("湖北鄂州发电有限责任公司物资领料单");
						cell.setCellStyle(titleStyle);// 设置单元格样式
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 9,
								10 * i - 9, 0, 7));

						// 第一行
						row = sheet.createRow(10 * i - 8);
						row.setHeight((short) 400);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(styleleft);
						cell.setCellValue("申请时间:"
								+ DateUtils.formatDate(lysqsj, "yyyy-MM-dd"));

						cell = row.createCell(1);
						cell.setCellStyle(styleleft);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style6);
						cell.setCellValue("领用时间:"
								+ DateUtils.formatDate((lysj==null)?lysqsj:lysj,"yyyy-MM-dd"));

						cell = row.createCell(3);
						cell.setCellStyle(style5);

						cell = row.createCell(4);
						cell.setCellStyle(styleleft);

						// sheet.addMergedRegion(new CellRangeAddress(10*i-8,
						// 10*i-8, 5, 6));
						cell = row.createCell(5);
						cell.setCellStyle(styleleft);
						// cell.setCellValue(DateUtils.formatDate(new Date(),
						// "yyyy年MM月dd日"));
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 6, 7));
						cell = row.createCell(6);
						cell.setCellStyle(styleleft);
						cell.setCellValue("NO:" + lydmx.getLydgl().getBh());

						cell = row.createCell(7);
						cell.setCellStyle(style6);

						// cell = row.createCell(8);
						// cell.setCellStyle(style6);
						//					
						// cell = row.createCell(9);
						// cell.setCellStyle(style6);
						//					
						// cell = row.createCell(10);
						// cell.setCellStyle(style6);

						// 第二行
						row = sheet.createRow(10 * i - 7);
						row.setHeight((short) 500);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(style1);
						cell.setCellValue("仓库名称:"
								+ lydmx.getWzbm().getKw().getCk().getCkmc());

						cell = row.createCell(1);
						cell.setCellStyle(style);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);
						cell.setCellValue("使用方向:" + lydmx.getGclb());

						cell = row.createCell(3);
						cell.setCellStyle(style);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 4, 7));
						cell = row.createCell(4);
						cell.setCellStyle(style1);
						cell.setCellValue("工程项目：" + lydmx.getGcxm());

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);
						//					
						// cell = row.createCell(8);
						// cell.setCellStyle(style);
						// cell.setCellValue(yhdmx.getFpbh());
						//					
						// cell = row.createCell(9);
						// cell.setCellStyle(style);
						// cell.setCellValue("运杂费：");
						//					
						// cell = row.createCell(10);
						// cell.setCellStyle(style4);
						// cell.setCellValue(yhdmx.getZf() != null ?
						// yhdmx.getZf() ： 0);

						// 第三行
						row = sheet.createRow(10 * i - 6);
						row.setHeight((short) 500);
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
								10 * i - 6, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(style1);
						cell.setCellValue("领用申请编号：" + lydmx.getLydgl().getBh());

						cell = row.createCell(1);
						cell.setCellStyle(style);
						//					
						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);
						cell.setCellValue("领用部门：" + lydmx.getLydgl().getLybm());

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
								10 * i - 6, 3, 7));
						cell = row.createCell(3);
						cell.setCellStyle(styleleft);
						cell.setCellValue("摘要：" + lydmx.getGclb()
								+ "----" + lydmx.getGcxm());

						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 4, 5));
						cell = row.createCell(4);
						cell.setCellStyle(style);
						// cell.setCellValue("");

						cell = row.createCell(5);
						cell.setCellStyle(style);

						cell = row.createCell(6);
						cell.setCellStyle(style);

						cell = row.createCell(7);
						cell.setCellStyle(style);

						// cell = row.createCell(8);
						// cell.setCellStyle(style);
						//					
						// cell = row.createCell(9);
						// cell.setCellStyle(style);
						//					
						// cell = row.createCell(10);
						// cell.setCellStyle(style);

						// 第四行
						row = sheet.createRow(10 * i - 5);
						row.setHeight((short) 500);

						cell = row.createCell(0);
						cell.setCellStyle(style);
						cell.setCellValue("物资编码");

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 5,
								10 * i - 5, 1, 2));
						cell = row.createCell(1);
						cell.setCellStyle(style);
						cell.setCellValue("物资名称及规格");

						cell = row.createCell(2);
						cell.setCellStyle(style);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 5,
								10 * i - 5, 3, 4));
						cell = row.createCell(3);
						cell.setCellStyle(style);
						cell.setCellValue("单位");

						cell = row.createCell(4);
						cell.setCellStyle(style);

						cell = row.createCell(5);
						cell.setCellStyle(style);
						cell.setCellValue("领用数量");

						cell = row.createCell(6);
						cell.setCellStyle(style);
						cell.setCellValue("实际单价");

						cell = row.createCell(7);
						cell.setCellStyle(style);
						cell.setCellValue("实际金额");

						// cell = row.createCell(8);
						// cell.setCellStyle(style);
						// cell.setCellValue("价税合计");
						//					
						// cell = row.createCell(9);
						// cell.setCellStyle(style);
						// cell.setCellValue("计划单价");
						//					
						// cell = row.createCell(10);
						// cell.setCellStyle(style);
						// cell.setCellValue("计划金额");

						// 第五行
						row = sheet.createRow(10 * i - 4);
						row.setHeight((short) 1000);

						cell = row.createCell(0);
						cell.setCellStyle(style);
						cell.setCellValue(lydmx.getWzbm().getWzbh());

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
								10 * i - 4, 1, 2));
						cell = row.createCell(1);
						cell.setCellStyle(style1);
						cell.setCellValue(lydmx.getWzbm().getWzmc()
								+ "\n\n规格："
								+ (lydmx.getWzbm().getXhgg() == null ? ""
										: lydmx.getWzbm().getXhgg()));

						cell = row.createCell(2);
						cell.setCellStyle(style);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
								10 * i - 4, 3, 4));
						cell = row.createCell(3);
						cell.setCellStyle(style);
						cell.setCellValue(lydmx.getWzbm().getJldw());

						cell = row.createCell(4);
						cell.setCellStyle(style);

						cell = row.createCell(5);
						cell.setCellStyle(style4);
						cell.setCellValue(decimalFormat.format(lydmx
								.getPzlysl()));

						cell = row.createCell(6);
						cell.setCellStyle(style4);
						cell.setCellValue(decimalFormat2.format(lydmx.getSjje()/lydmx.getPzlysl()));
						

						cell = row.createCell(7);
						cell.setCellStyle(style4);
						cell.setCellValue(decimalFormat2.format(lydmx.getSjje()));
						

						// 间隔
						row = sheet.createRow(10 * i - 3);
						row.setHeight((short) 150);

						// 第六行
						row = sheet.createRow(10 * i - 2);
						row.setHeight((short) 350);

						cell = row.createCell(0);
						cell.setCellStyle(style5);

						cell = row.createCell(1);
						cell.setCellStyle(styleleft);

						cell = row.createCell(2);
						cell.setCellStyle(styleleft);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 2,
								10 * i - 2, 3, 5));
						cell = row.createCell(3);
						cell.setCellStyle(styleleft);
						cell.setCellValue("保管员：" + lydmx.getLydgl().getCzr());

						cell = row.createCell(4);
						cell.setCellStyle(styleleft);

						cell = row.createCell(5);
						cell.setCellStyle(styleleft);

						cell = row.createCell(6);
						cell.setCellStyle(style5);
						cell.setCellValue("领料人：");

						cell = row.createCell(7);
						cell.setCellStyle(styleleft);
						cell.setCellValue(lydmx.getLydgl().getLlr());
						//					
						// cell = row.createCell(8);
						// cell.setCellStyle(style1);
						// cell.setCellValue("");
						//					
						// cell = row.createCell(9);
						// cell.setCellStyle(style5);
						// cell.setCellValue("验收员：");
						//					
						// cell = row.createCell(10);
						// cell.setCellStyle(style1);
						// cell.setCellValue(yhdmx.getYhdgl().getPersonCgy().getUserName());
						//					

						// 下间隔
//						row = sheet.createRow(10 * i - 1);
//						row.setHeight((short) 495);
					}
//				}
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
	/**
	 * 根据领用明细ID 返回是否有自由领用的物资
	 * @return
	 * @throws Exception 
	 */
	public String getZylyZtAction() throws Exception{
		String ids = request.getParameter("ids");
		for(String id:ids.split(",")){
			Lydmx lydmx = lydmxManager.get(id);
			if(lydmx!=null){
				if(lydmx.getWzlysqDetail().getXqjhDetail()==null){
					this.outputJson("{success:true}");
				}
			}
		}
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
				bis = new BufferedInputStream(this.exportYhdmxExcel());
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

	@Override
	public HibernateEntityDao getManager() {
		return lydmxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "lydgl", "lysj", "lydqf", "gclb", "gcxm",
				"lybm", "lysqbh", "llr", "czr", "wzlysqDetail","wzlysq","sqsj","time", "bh", "wzbm",
				"xhgg", "wzmc", "xh","zfzt",
				// "wzbm",
				"xhgg", "jldw", "jhdj", "dqkc", "yfpsl", "pzlysl", "sjlysl",
				"zt", "kw", "ck", "ckmc", "time","xqjhDetail" };
	} 
	 
	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "xh", "wzbm", "jldw", "jhdj", "pzlysl",
				"sjlysl", "zje", "" };
	}
}
