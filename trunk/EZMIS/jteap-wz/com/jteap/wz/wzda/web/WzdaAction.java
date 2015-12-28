/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzda.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.velocity.VelocityConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.util.ExcelUtil;
import com.jteap.wz.util.ExcelUtils;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.model.XqjhDetail;



@SuppressWarnings("unchecked")
public class WzdaAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WzdaManager wzdaManager;
	
	private VelocityConfig velocityConfig;
	
	public void setVelocityConfig(VelocityConfig velocityConfig) {
		this.velocityConfig = velocityConfig;
	}
	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager =  wzdaManager;
	}
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
		//	System.out.println(request.getParameter("wzmc"));
			this.isUseQueryCache = false;
			String wzlbbm = request.getParameter("wzlbbm");//物资类别编码
			if(StringUtils.isNotEmpty(wzlbbm))
				HqlUtil.addCondition(hql, "wzlb.id", wzlbbm);
			String wzdaId =request.getParameter("docid");
			if(StringUtils.isNotEmpty(wzdaId)){
				HqlUtil.addCondition(hql, "id", wzdaId);
			}
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
			String hqlQuery = request.getParameter("query");
			if(StringUtils.isNotEmpty(hqlQuery)){
				//String hqlWhereTemp = hqlQuery.replace("$", "%");
				HqlUtil.addWholeCondition(hql, "wzmc like '"+hqlQuery+"%'");
			}

		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		HqlUtil.addOrder(hql, "dqkc", "desc");
		//if(!this.isHaveSortField()){
		HqlUtil.addOrder(hql, "wzmc", "asc");
		//}
		
	}
	/**
	 * 初始化　物资档案型号规格
	 * @return
	 */
	public String initWzdaAction(){
		String[] a = {"Ａ","Ｂ","Ｃ","Ｄ","Ｅ","Ｆ","Ｇ","Ｈ","Ｉ","Ｊ","Ｋ","Ｌ","Ｍ","Ｎ",
				"Ｏ","Ｐ","Ｑ","Ｒ","Ｓ","Ｔ","Ｕ","Ｖ","Ｗ","Ｘ","Ｙ","Ｚ"};
		String[] b = {"ａ","ｂ","ｃ","ｄ","ｅ","ｆ","ｇ","ｈ","ｉ","ｊ","ｋ","ｌ","ｍ","ｎ",
				"ｏ","ｐ","ｑ","ｒ","ｓ","ｔ","ｕ","ｖ","ｗ","ｘ","ｙ","ｚ"};
		String[] n = {"０","１","２","３","４","５","６","７","８","９"};
		
		char c= 97;
		//小写字母
		for(int i=0;i<a.length;i++){
			List<Wzda> wzdaList = wzdaManager.find("from Wzda a where a.xhgg like '%"+a[i]+"%'");
			for(Wzda w:wzdaList){
				w.setXhgg(w.getXhgg().replace(a[i],String.valueOf(c)));
				wzdaManager.save(w);
			}
			c= 96;
		}
		//大写字母
		c= 65;
		for(int j=0;j<b.length;j++){
			List<Wzda> wzdaList = wzdaManager.find("from Wzda a where a.xhgg like '%"+b[j]+"%'");
			for(Wzda w:wzdaList){
				w.setXhgg(w.getXhgg().replace(b[j],String.valueOf(c)));
				wzdaManager.save(w);
			}
			c++;
		}
		//数字
		c= 48;
		for(int k=0;k<n.length;k++){
			List<Wzda> wzdaList = wzdaManager.find("from Wzda a where a.xhgg like '%"+n[k]+"%'");
			for(Wzda w:wzdaList){
				w.setXhgg(w.getXhgg().replace(n[k],String.valueOf(c)));
				wzdaManager.save(w);
			}
			c++;
		}

		return NONE;
	}
	//批量修改物资类别
	public String updateWzlbAction() throws Exception{
		String ids = request.getParameter("ids");
		String newwzlb = request.getParameter("newwzlb");//新的物资类别
		if(StringUtils.isNotEmpty(ids)){
			ids = StringUtils.removeEnd(ids, ",");
			ids = ids.replaceAll(",", "','");
			wzdaManager.updateWzdalb(ids, newwzlb);
			this.outputJson("{success:true}");
		}		
		return NONE;	
	}
	//批量修改物资库位
	public String updateKWAction() throws Exception{
		String ids = request.getParameter("ids");
		if(StringUtils.isNotEmpty(ids)){
			ids = StringUtils.removeEnd(ids, ",");
			ids = ids.replaceAll(",", "','");
			String kw = request.getParameter("kw");
			wzdaManager.updateKW(ids, kw);
			this.outputJson("{success:true}");
		}
		return NONE;	
	}
	//批量修改特殊分类
	public String updateTsflAction() throws Exception{
		String ids = request.getParameter("ids");
	
		if(StringUtils.isNotEmpty(ids)){
			ids = StringUtils.removeEnd(ids, ",");
			ids = ids.replaceAll(",", "','");
			String tsfl = request.getParameter("tsfl");
			wzdaManager.updateTsfl(ids, tsfl);
			this.outputJson("{success:true}");
		}
		return NONE;	
	}
	//批量修改当前库存
	public String updateDqkcAction() throws Exception{
		String ids = request.getParameter("ids");
		if(StringUtils.isNotEmpty(ids)){
			ids = StringUtils.removeEnd(ids, ",");
			ids = ids.replaceAll(",", "','");
			String dqkc = request.getParameter("dqkc");
			if(StringUtil.isNotEmpty(dqkc)){
				double kcl = Double.valueOf(dqkc);
				wzdaManager.updateDqkc(ids, kcl);
			}
			this.outputJson("{success:true}");
		}
		return NONE;	
	}
	/**
	 * 返回有库存的物资id
	 * @return
	 * @throws Exception 
	 */
	public String getYckAction() throws Exception{
		this.outputJson("{success:true,ids:'"+wzdaManager.getYkcids()+"'}");
		return NONE;
	}
	//导出物资选项卡
	public String exportWzCardAction() throws Exception{
		String ids = request.getParameter("ids");
		if(StringUtils.isNotEmpty(ids)){
			ids = ids.trim();
			String[] id = StringUtil.splitByWholeSeparator(ids, ",");
			List<Wzda> wzdaList = wzdaManager.findByIds(id);
			//模板输出
			VelocityEngine ve =  velocityConfig.getVelocityEngine();
			StringWriter writer = new StringWriter();
			
			VelocityContext context = new VelocityContext();
			context.put("wzdaList", wzdaList);
	        
			Template template = ve.getTemplate("wzda.vm","UTF-8");
	    
	        template.merge(context, writer);
	        
	        response.reset();
			response.setContentType("application/vnd.ms-word;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(("物资信息_" + DateUtils.getDate("yyyy-MM-dd") + ".doc")
							.getBytes(), "iso-8859-1"));
			
			//开始输出
			java.io.BufferedOutputStream outs = new java.io.BufferedOutputStream(response.getOutputStream());
			outs.write(writer.toString().getBytes("UTF-8"));
			outs.flush();
			outs.close();
		}
		return NONE;	
	}
	
	/**
	 * 物资卡片打印
	 */
	public InputStream exportYhdmxExcel() throws Exception {
		//选定打印
		String id = request.getParameter("id");
		String bz = request.getParameter("bz");
		String[] bzArr =null;
		Map<String,String> bzMap = new HashMap<String, String>();
		if(StringUtils.isNotEmpty(bz)){
			bzArr= bz.split(",");
			for(String b:bzArr){
				String[] bzid = b.split(":");
				bzMap.put(bzid[0], bzid[1]);
			}
		}
		//条件打印
		String parWhere = request.getParameter("parWhere");
//		String idsArray[] = id.split(",");
		HSSFWorkbook workbook = new HSSFWorkbook();

		// HSSFCellStyle style = workbook.createCellStyle();

		// style.setFillBackgroundColor(HSSFColor.BLUE.index);

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();
		int cellNum = 0;
		// 设置excel每列宽度
		for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				sheet.setColumnWidth(cellNum, 1730);//宽度和实际excel比例是1：288.333
				cellNum++;
				sheet.setColumnWidth(cellNum, 3000);
				cellNum++;
				sheet.setColumnWidth(cellNum, 1730);
				cellNum++;
				sheet.setColumnWidth(cellNum, 1730);
				cellNum++;
				sheet.setColumnWidth(cellNum, 370);
				cellNum++;
			}
		}
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

		HSSFRow row = null;
		HSSFCell cell = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		StringBuffer hql = new StringBuffer("from Wzda obj ");
		if(StringUtils.isNotEmpty(id)){
			hql.append(" where obj.id in ("+id+")");
		}
		if(StringUtils.isNotEmpty(parWhere)){
			String hqlWhereTemp = parWhere.replace("%25", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		List<Wzda> wzdaList = wzdaManager.find(hql.toString());
		
		//同一个验货单总的明细数量
//		String zbh = "";
		
		try {
			boolean is_New = false;
			for (int i = 1; i <= wzdaList.size(); i++) {
				Wzda wzda = wzdaList.get(i-1);
				
				bz = (bzMap.get(wzda.getId())==null)?"":bzMap.get(wzda.getId());
				//打印物资卡片 A4纸张 如果为奇数 则是新起一栏写行
//				System.out.println("取模"+i%4);
//				System.out.println("取值"+i/4);
				if(i%5!=0){
					if(i==1){
						this.createNewExcel(i, row, cell, sheet,workbook,wzda,true,(i%5-1)*5,9,bz);
					}else{
						if(is_New){
							this.createNewExcel(i/5+1, row, cell, sheet,workbook,wzda,true,(i%5-1)*5,9,bz);
							is_New = false;	
						}else{
//							System.out.println("来了"+i/4+1);
							this.createNewExcel(i/5+1, row, cell, sheet,workbook,wzda,false,(i%5-1)*5,9,bz);
						}
					}
				}else{
					is_New = true;	
					//如果是第四个模板 则下一次应新起一行
					this.createNewExcel(i/5, row, cell, sheet,workbook,wzda,false,20,9,bz);
				}
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
	 * 新增excel行
	 * 吕超
	 * @param i
	 * @param row 
	 * @param cell
	 * @param sheet
	 * @param wzda 物资档案对象 
	 * @param isNew 是否新起行
	 * @param sta 开始列数
	 */
	private void createNewExcel(int i,HSSFRow row,HSSFCell cell,HSSFSheet sheet,HSSFWorkbook workbook,Wzda wzda,boolean isNew,int sta,int rowCount,String bz){
		HSSFFont font1 = workbook.createFont();
		font1.setColor(HSSFColor.BLACK.index);
		font1.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font1.setFontHeight((short) 160); // 设置字体大小
		font1.setFontName("宋体"); // 设置单元格字体
		
		
		//物资名称样式
		HSSFCellStyle style5 = workbook.createCellStyle();
		style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		style5.setFont(font1);
		style5.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style5.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style5.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style5.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

		// 列头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font.setFontHeight((short) 220); // 设置字体大小
		font.setFontName("宋体"); // 设置单元格字体
		titleStyle.setFont(font);

		int rowNum = rowCount;
		
		// 创建标题行
		//cell集合
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap<String, Object>();
//	 
//		
//		System.out.println("i"+i);
		
		if(i%6==1&&i/6>=1){
//			// 上间隔
			ExcelUtil.addRow(rowCount* i - rowNum, 1047, row, cell, sheet, null,isNew,sta);
		}else{
			ExcelUtil.addRow(rowCount* i - rowNum, 198, row, cell, sheet, null,isNew,sta);
		}
		
		rowNum--;
		
		// 第一行
		//物资编码行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value","物资编码");
//		style5.setBorderLeft(HSSFCellStyle.BORDER_THICK);
//		style5.setBorderTop(HSSFCellStyle.BORDER_THICK);
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value", wzda.getWzbh());
//		style5.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value", "计量单位");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value", wzda.getJldw());
//		style5.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		map.put("style",style5);
		list.add(map);

		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
		
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		
		rowNum--;
		
		//物资名称行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "物资名称");
//		style5.setBorderRight(HSSFCellStyle.BORDER_THICK);
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",wzda.getWzmc());
		map.put("style",style5);
		map.put("address", new CellRangeAddress(rowCount * i - rowNum,
				rowCount * i - rowNum, sta+1, sta+3));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
		
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//型号规格行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "型号规格");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",wzda.getXhgg());
		map.put("style",style5);
		map.put("address", new CellRangeAddress(rowCount * i - rowNum,
				rowCount * i - rowNum, sta+1, sta+3));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
		
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//仓库名称 库存量行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value","存放位置");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value", wzda.getKw().getCwmc());
//		style5.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value", "仓库");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",wzda.getKw().getCk().getCkmc());
//		style5.setBorderRight(HSSFCellStyle.BORDER_THICK);
		map.put("style",style5);
		list.add(map);

		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
		
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//物资类别行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "物资类别");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",(wzda.getWzlb().getFlbbm()==null)?"":wzda.getWzlb().getFlbbm().getWzlbmc());
		map.put("style",style5);
		map.put("address", new CellRangeAddress(rowCount * i - rowNum,
				rowCount * i - rowNum, sta+1, sta+3));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
	
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//物资类别行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",wzda.getWzlb().getWzlbmc());
		map.put("style",style5);
		map.put("address", new CellRangeAddress(rowCount * i - rowNum,
				rowCount * i - rowNum, sta+1, sta+3));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
	
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//班组
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "班组");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value",bz);
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("value","库存量");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("######");
		map.put("value",decimalFormat.format(wzda.getDqkc()));
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
	
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		rowNum--;
		
		//物资类别行
		list = new ArrayList<Map>();
		
		map = new HashMap<String, Object>();
		map.put("value", "条形码");
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		//map.put("value",wzda.getWzlb().getWzlbmc());
		map.put("style",style5);
		map.put("address", new CellRangeAddress(rowCount * i - rowNum,
				rowCount * i - rowNum, sta+1, sta+3));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("style",style5);
		list.add(map);
		
		//列间隔
		map = new HashMap<String, Object>();
		list.add(map);
		ExcelUtil.addRow(rowCount* i - rowNum, 267, row, cell, sheet, list,isNew,sta);
		
//		//备注行
//		list = new ArrayList<Map>();
//		
//		map = new HashMap<String, Object>();
//		map.put("value", "备注");
////		style5.setBorderLeft(HSSFCellStyle.BORDER_THICK);
////		style5.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//		map.put("style",style5);
//		list.add(map);
//		
//		map = new HashMap<String, Object>();
//		map.put("value",wzda.getBz());
////		style5.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
//		map.put("style",style5);
//		map.put("address", new CellRangeAddress(rowCount * i - 2,
//				rowCount * i - 2, sta+1, sta+3));
//		list.add(map);
//		
//		map = new HashMap<String, Object>();
//		map.put("style",style5);
//		list.add(map);
//		
//		map = new HashMap<String, Object>();
//		map.put("style",style5);
//		list.add(map);
//		
//		//列间隔
//		map = new HashMap<String, Object>();
//		list.add(map);
//		
//		ExcelUtil.addRow(rowCount* i - 2, 375, row, cell, sheet, list,isNew,sta);
		
//		if(i%28==1){
//			// 上间隔
//			list = new ArrayList<Map>();
//			ExcelUtil.addRow(rowCount* i - rowNum-1, 737, row, cell, sheet, null,isNew,sta);
//		}
	
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

	
	//导出物资档案Excel
	public String exportWzDAExcel() throws Exception{
		List list;
		try {
			StringBuffer hql = getPageBaseHql();

			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			beforeShowList(request, response, hql);
			
			list = this.getManager().find(hql.toString(),showListHqlValues.toArray());
			
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		response.reset();

		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("物资信息"+DateUtils.getDate("yyyy-MM-dd")+".xls")
						.getBytes(), "iso-8859-1"));
	
		HSSFWorkbook wb = new HSSFWorkbook();
		ExcelUtils.fillSheet(wb.createSheet(), list, Wzda.class);
		wb.write(response.getOutputStream());
		return NONE;
	}
	/**
	 * 对物资档案已分配数量进行初始化
	 * @return
	 */
	public String initWzda(){
		XqjhDetailManager xqjhDetailManager = (XqjhDetailManager) SpringContextUtil.getBean("xqjhDetailManager");
		LydmxManager lydmxManager = (LydmxManager) SpringContextUtil.getBean("lydmxManager");
		//查询所有已分配数量大于0的
		String hql = " from Wzda a";
		List<Wzda> wzdaList = wzdaManager.find(hql);
		for(Wzda wzda:wzdaList){
			double pzsl = 0;
			double lysl = 0;
			//查询需求计划明细生效的批准数量
			List<XqjhDetail> xqmxList = xqjhDetailManager.find("from XqjhDetail y where y.wzbm = '"+wzda.getId()+"' and y.status ='1'");
			for(XqjhDetail xqjhmx:xqmxList){
				pzsl += xqjhmx.getPzsl();
			}
			//查询已领用的数量
			List<Lydmx> lydmxList = lydmxManager.find("from Lydmx y where y.wzbm.id = ? and y.zt ='1'",wzda.getId());
			for(Lydmx lydmx:lydmxList){
				lysl += lydmx.getSjlysl();
			}
			//设置最新的已分配数量
			wzda.setYfpsl(pzsl-lysl);
			wzdaManager.save(wzda);
		}
//		System.out.println("初始化完毕");
		return NONE;
	}
	//查询指定id物资
	public String findWzdaActionById() throws Exception{
		String id = request.getParameter("docid");
		if(StringUtil.isNotEmpty(id)){
			Wzda wzda = wzdaManager.get(id);
			this.outputJson("{success:true,wzdaid:'"+wzda.getId()+"',wzmc:'"+wzda.getWzmc()+"',xhgg:'"+wzda.getXhgg()+"'}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao<?> getManager() {
		return wzdaManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"wzlb",
			"wzlbmc",
			"wzmc",
			"zjm",
			"xhgg",
			"jldw",
			"jhdj",
			"pjj",
			"zgcbde",
			"zdcbde",
			"dqkc",
			"yfpsl",
			"abcfl",
			"tsfl",
			"zyf",
			"kw",
			"cwmc",
			"wzzjm",
			"ck",
			"ckmc",
			"czy",
			"cskc",
			"csjg",
			"bz",
			"cfwz",
			"dyx",
			"tjm",
			"wzmcxh",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"wzlb",
			"wzlbmc",
			"wzmc",
			"zjm",
			"xhgg",
			"jldw",
			"jhdj",
			"pjj",
			"zgcbde",
			"zdcbde",
			"dqkc",
			"yfpsl",
			"abcfl",
			"tsfl",
			"zyf",
			"kw",
			"cwmc",
			"wzzjm",
			"czy",
			"cskc",
			"csjg",
			"bz",
			"cfwz",
			"dyx",
			"tjm",
		""};
	}
}
