package com.jteap.wz.fytj.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.fytj.manager.FytjManager;
import com.jteap.wz.fytj.model.Fytj;
import com.jteap.wz.gclbgl.manager.ProjcatManager;
import com.jteap.wz.gcxmgl.manager.ProjManager;
import com.jteap.wz.tjny.manager.TjnyManager;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class FytjAction extends AbstractAction{
	/**
	 * 费用统计处理类
	 */
	private FytjManager fytjManager;
	private CkglManager ckglManager;
	
	private ProjcatManager projcatManager;
	private ProjManager projManager;
	
	//excel总列数
	private int colCount;
	//private Map<String,Float> countMap;
	/**
	 * 统计年月处理类
	 */
	private TjnyManager tjnyManager;
	//存放收发资金到当前页的统计总和
	private static Map FytjCountMap  ;
	//保存开始条数
	private static int saveStart;
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return fytjManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		this.isUseQueryCache=false;
		try {
			fytjManager.getFytjByZj();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String nf = request.getParameter("nf");
		String yf = request.getParameter("yf");
		String ny = request.getParameter("ny");
		 
		if(StringUtil.isNotEmpty(ny)){
			//System.out.println(ny);
			String n = ny.split("年")[0];
			String y = ny.split("年")[1].split("月")[0];
			HqlUtil.addCondition(hql, "nf",n);
			HqlUtil.addCondition(hql, "yf",y);
		}
		if(StringUtil.isNotEmpty(nf)){
			HqlUtil.addCondition(hql, "nf",nf);
		}else if(StringUtil.isEmpty(ny)){
			HqlUtil.addCondition(hql, "nf",fytjManager.getMaxYear());
		}
		if(StringUtil.isNotEmpty(yf)){
			HqlUtil.addCondition(hql, "yf",yf);
		}else if(StringUtil.isEmpty(ny)){
			HqlUtil.addCondition(hql, "yf",fytjManager.getMaxMoth());
		}
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
			String parentId = request.getParameter("parentId");
			
			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			beforeShowList(request, response, hql);
		    String jsonStr = fytjManager.getJson(hql);
//		    System.out.println(jsonStr);
			this.outputJson(jsonStr);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}
	//导出Excel
	public String exportExcel() throws Exception {
		colCount = fytjManager.getColcount();
		//System.out.println("总列数："+colCount);
		
		StringBuffer paraHeaderBuf = new StringBuffer("工程项目名称");
		StringBuffer paraWidthBuf = new StringBuffer("80");
		for(int i=0;i<colCount-1;i++){
			paraHeaderBuf.append(", ");
			paraWidthBuf.append(",80");
		}

		List<Ckgl> ckglList = ckglManager.getAll();
		for(Ckgl ckgl:ckglList){
			paraHeaderBuf.append(","+ckgl.getCkmc());
			paraWidthBuf.append(",70");
		}
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = paraHeaderBuf.toString();
		//System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex = "";
		//System.out.println(paraDataIndex);
		// 宽度(逗号表达式)
		String paraWidth = paraWidthBuf.toString();
		String nf  = request.getParameter("nf");
		String yf = request.getParameter("yf");
		
		String hql  = "from Fytj s where s.nf='"+nf+"' and s.yf='"+yf+"'"; 
		List<Fytj> fytjs =fytjManager.find(hql);
		if(fytjs.size()<1){
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		}
//		fytjManager.addCount(fytjs);
		export(fytjs, paraHeader, paraWidth,hql,nf,yf);
	 
		// 调用导出方法
		return NONE;
	}
	/**
	 * 导出操作
	 * 
	 * @param col
	 *            所要导出对象集合
	 * @param headers
	 *            Excel的头信息 （逗号表达式）
	 * @param dataIndexs
	 *            对应类属性名 （逗号表达式）
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void export(Collection col, String headers,
			String widths,String hql,String nf,String yf) throws IOException, SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("物资费用统计_"+nf+"-"+yf+".xls")
						.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(this.getExcelInputStream(nf,yf));
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
	
	private InputStream getExcelInputStream(String nf,String yf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, UnsupportedEncodingException {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 设置excel每列宽度
		sheet.setColumnWidth(0, 3732);
		sheet.setColumnWidth(1, 3595);
		sheet.setColumnWidth(2, 3937);
		sheet.setColumnWidth(3, 3047);
		sheet.setColumnWidth(4, 3047);
		sheet.setColumnWidth(5, 3047);
		sheet.setColumnWidth(6, 3047);
		sheet.setColumnWidth(8, 3047);
		sheet.setColumnWidth(9, 3047);
		sheet.setColumnWidth(10, 3047);
		sheet.setColumnWidth(11, 3047);
		sheet.setColumnWidth(12, 3047);
		
		//仓库列
		String[] cells = new String[]{"五金","电料","钢材","综合","工具","加工件","仪器","设备","备品","备品一"};
		//标题样式
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE);  //打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);     //Excel字体加粗
		font.setFontHeight((short)380);         //设置字体大小
		font.setFontName("宋体");                //设置单元格字体
		titleStyle.setFont(font);
		//普通文本样式
		HSSFCellStyle txtStyle = wb.createCellStyle();
		txtStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		txtStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		txtStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		// 创建标题行        第一行
		row=this.addRow(row, sheet, 0, 500);
		this.addCell(cell, row, nf+"年"+yf+"月新系统材料分配表", 0, titleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0,
				0, 0, 12));
		
		//副标题行 			第二行
		row=this.addRow(row, sheet, 1, 345);
		this.addCell(cell, row, "使用方向", 0, txtStyle);
		this.addCell(cell, row, "工程项目", 1, txtStyle);
		sheet.addMergedRegion(new CellRangeAddress(1,
				1, 1, 2));
		for(int i=0;i<cells.length;i++){
			this.addCell(cell, row, cells[i], i+3, txtStyle);
		}
		
		//添加数据行 设备日常维护		第三行
		row=this.addRow(row, sheet, 2, 420);
		//添加设备日常维护列
		this.addCell(cell, row, "设备日常维护", 0, txtStyle);
		sheet.addMergedRegion(new CellRangeAddress(2,
				6, 0, 0));
		//添加项目列
		this.addCell(cell, row, "发电设备维护(1#机组设备维护)", 1, txtStyle);
		sheet.addMergedRegion(new CellRangeAddress(2,
				2, 1,2));
		this.addValueCell(cells, "1#发电设备维护", row, cell,nf,yf, txtStyle);
		
		//添加数据行 设备日常维护		第四行
		this.addValueCell(cells, "2#发电设备维护", "发电设备维护(2#机组设备维护)", row, cell, sheet, 3,nf,yf,true, txtStyle);
		
		//添加数据行 设备日常维护		第五行
		this.addValueCell(cells,  "3#发电设备维护", "发电设备维护(3#机组设备维护)", row, cell, sheet, 4,nf,yf,true, txtStyle);
		
		//添加数据行 设备日常维护		第六行
		this.addValueCell(cells, "4#发电设备维护", "发电设备维护(4#机组设备维护)", row, cell, sheet, 5,nf,yf,true, txtStyle);
		//添加数据行 设备日常维护		第七行
		this.addValueCell(cells, "公用设施维护", "公用设施维护", row, cell, sheet, 6,nf,yf,true, txtStyle);
		//设备日常维护 相关项目添加结束
		
		//添加机组检修 开始
		row=this.addRow(row, sheet, 7, 420);
		this.addCell(cell, row, "机组检修", 0, txtStyle);
		sheet.addMergedRegion(new CellRangeAddress(7,
				22, 0, 0));
		//添加项目列
		this.addCell(cell, row, "1#机A级检修", 1, txtStyle);
		sheet.addMergedRegion(new CellRangeAddress(7,
				7, 1,2));
		this.addValueCell(cells, "1#A级检修", row, cell,nf,yf, txtStyle);
		int rowNum = 8;
		for(int i =1;i<=4;i++){
			for(int j=65;j<=68;j++){
				if(i==1&&j==65){
					
				}else{
					this.addValueCell(cells,  i+"#"+(char)j+"级检修", i+"#机"+(char)j+"级检修", row, cell, sheet, rowNum,nf,yf,true, txtStyle);
					rowNum++;
				}
			}
		}
		//添加机组检修结束
		//添加公用设施修理
		row=this.addRow(row, sheet, 23, 420);
		this.addCell(cell, row, "公用设施修理", 0, txtStyle);
		this.addValueCell(cells, "公用设备修理", "公用设施修理", row, cell, sheet, 23,nf,yf,false, txtStyle);
		//添加公用设施修理结束
		//添加技改
		row=this.addRow(row, sheet, 24, 420);
		this.addCell(cell, row, "在建工程", 0, txtStyle);
		this.addProcatCell(cells, "技术改造", "技改", row, cell, sheet, 24,nf,yf, txtStyle);
		//添加生产耗材
		row=this.addRow(row, sheet, 25, 420);
		this.addCell(cell, row, "材料款", 0, txtStyle);
		this.addProcatCell(cells, "生产耗材", "发电耗材", row, cell, sheet, 25,nf,yf, txtStyle);
		//添加消防系统
		row=this.addRow(row, sheet, 26, 420);
		this.addCell(cell, row, "安全生产管理", 0, txtStyle);
		this.addValueCell(cells, "消防系统", "消防系统", row, cell, sheet, 26,nf,yf,false, txtStyle);
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}
	
	
	
	/**
	 * 根据工程项目 新增行和列 并将工程项目的金额 赋值到单元格中
	 * @param cells
	 * @param proName
	 * @param showPro
	 * @param row
	 * @param cell
	 * @param sheet
	 * @param rowNum
	 * @param nf
	 * @param yf
	 */
	private void addValueCell(String[] cells,String proName,String showPro,HSSFRow row,HSSFCell cell,HSSFSheet sheet,
			int rowNum,String nf,String yf,boolean isNew,HSSFCellStyle style){
		String ckName = "";
		if(isNew){
			row=this.addRow(row, sheet, rowNum, 420);
		}
		this.addCell(cell, row, showPro, 1, style);
		sheet.addMergedRegion(new CellRangeAddress(rowNum,
				rowNum, 1,2));
		//根据项目和 仓库名称 年月 查询费用
		for(int i=0;i<cells.length;i++){
			ckName = cells[i];
			String hql = "from Fytj t where t.proj.projname='"+proName+"' and t.ckgl.ckmc like '%"+ckName+"%' and t.nf ='"+nf+"' and t.yf ='"+yf+"'";
			List<Fytj> fytjList = fytjManager.find(hql);
			if(fytjList.size()>0){
				Fytj fytj = fytjList.get(0);
				this.addCell(cell, row, fytj.getByje(), i+3, style);
			}else{
				this.addCell(cell, row, "", i+3, null);
			}
		}
	}
	/**
	 * 根据工程类别 新增行和列 并将工程类别的金额合计 赋值到单元格中
	 * @param cells
	 * @param proName
	 * @param showPro
	 * @param row
	 * @param cell
	 * @param sheet
	 * @param rowNum
	 * @param nf
	 * @param yf
	 */
	private void addProcatCell(String[] cells,String proName,String showPro,HSSFRow row,HSSFCell cell,
			HSSFSheet sheet,int rowNum,String nf,String yf,HSSFCellStyle style){
		String ckName = "";
		this.addCell(cell, row, showPro, 1, style);
		sheet.addMergedRegion(new CellRangeAddress(rowNum,
				rowNum, 1,2));
		//根据项目和 仓库名称 年月 查询费用
		for(int i=0;i<cells.length;i++){
			ckName = cells[i];
			String hql = "from Fytj t where t.proj.projcat.projcatname='"+proName+"' and t.proj.p_proj is null and t.ckgl.ckmc like '%"+ckName+"%' and t.nf ='"+nf+"' and t.yf ='"+yf+"'";
			List<Fytj> fytjList = fytjManager.find(hql);
			if(fytjList.size()>0){
				double sumJe = 0;
				for(Fytj fytj :fytjList){
					sumJe = sumJe+fytj.getByje();
				}
				this.addCell(cell, row,sumJe , i+3, style);
			}else{
				this.addCell(cell, row, "", i+3, style);
			}
		}
	}
	/**
	 * 根据工程项目 新增列 并将工程项目的金额 赋值到单元格中
	 * @param cells
	 * @param proName
	 * @param row
	 * @param cell
	 * @param nf
	 * @param yf
	 */
	private void addValueCell(String[] cells,String proName,HSSFRow row,HSSFCell cell,String nf,String yf,HSSFCellStyle style){
		String ckName = "";
		//根据项目和 仓库名称 年月 查询费用
		for(int i=0;i<cells.length;i++){
			ckName = cells[i];
			String hql = "from Fytj t where t.proj.projname='"+proName+"' and t.ckgl.ckmc like '%"+ckName+"%' and t.nf ='"+nf+"' and t.yf ='"+yf+"'";
			List<Fytj> fytjList = fytjManager.find(hql);
			if(fytjList.size()>0){
				Fytj fytj = fytjList.get(0);
				this.addCell(cell, row, fytj.getByje(), i+3, style);
			}else{
				this.addCell(cell, row, "", i+3, style);
			}
		}
		
	}
	/**
	 * 根据行数以及行高 新增一行
	 * @param row
	 * @param sheet
	 * @param rolNum
	 * @param rowHight
	 */
	private HSSFRow addRow(HSSFRow row,HSSFSheet sheet,int rolNum,int rowHight){
		row = sheet.createRow(rolNum);
		row.setHeight((short) rowHight);// 设定行的高度
		return row;
	}
	/**
	 * 根据列数，列值 新增一列 并设置字符串型列值
	 * @param cell
	 * @param row
	 * @param cellValue
	 * @param colNum
	 * @param style
	 */
	private void addCell(HSSFCell cell,HSSFRow row,String cellValue,int colNum,HSSFCellStyle style){
		cell = row.createCell(colNum);
		cell.setCellValue(cellValue);
		if(style!=null){
			cell.setCellStyle(style);// 设置单元格样式
		}
	}
	/**
	 * 根据列数，列值 新增一列 并设置数字型列值
	 * @param cell
	 * @param row
	 * @param cellValue
	 * @param colNum
	 * @param style
	 */
	private void addCell(HSSFCell cell,HSSFRow row,Double cellValue,int colNum,HSSFCellStyle style){
		cell = row.createCell(colNum);
		cell.setCellValue(cellValue);
		if(style!=null){
			cell.setCellStyle(style);// 设置单元格样式
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 导出excel (根据工程项目递归导出) 暂弃用
	 * @param col
	 * @param paraHeader
	 * @param paraWidth
	 * @param hql
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws UnsupportedEncodingException
	 
	private InputStream getExcelInputStream(Collection col, String paraHeader,
			String paraWidth,String hql) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, UnsupportedEncodingException {

		if ("".equals(paraHeader) || paraHeader == null) {

		}

		if ("".equals(paraWidth) || paraWidth == null) {

		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		HSSFRow row = sheet.createRow(0);
		String[] headers = paraHeader.split(",");
		String[] widths = paraWidth.split(",");
		
		//表头样式
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE);  //打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);     //Excel字体加粗
		font.setFontHeight((short)380);         //设置字体大小
		font.setFontName("宋体");                //设置单元格字体
		titleStyle.setFont(font);
		
		//列标题样式
		HSSFCellStyle titleStyle1 = wb.createCellStyle();
		// titleStyle.setBorderLeft((short)1);
		// titleStyle.setBorderRight((short)1);
		/*
		 * titleStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
		 * titleStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		  
		titleStyle1.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		titleStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titleStyle1.setFont(font);
		
		HSSFCell titleCell = row.createCell(0);
		row.setHeight((short) 500);// 设定行的高度
		titleCell.setCellValue("物资费用统计");
		titleCell.setCellStyle(titleStyle);// 设置单元格样式
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));
		
		row = sheet.createRow(1);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			sheet.setColumnWidth(i, (short) (Short
					.parseShort(widths[i]) * (short) 50));
			cell.setCellValue(headers[i]);
			cell.setCellStyle(titleStyle1);
		}
		//存储行列的map集合
		Map<String,Integer> rowColMap = new HashMap<String, Integer>();
		rowColMap.put("row",2);
		rowColMap.put("col",1);
		//存储年累计 月累计集合
		Map<String,Double> countMap = new HashMap<String, Double>();
		//工程类别集合
		List<Projcat> projCatList = projcatManager.findResourcesByProjcat();
		//countMap = new HashMap<String, Float>();
		for(Projcat projcat:projCatList){
			this.createRow(row, sheet, rowColMap, projcat,hql,countMap);
			//查出所有工程项目根节点
			List<Proj> projList  = projManager.find("from Proj as p where p.projcat.id =? and p.finished='0' and p.isviable is null and p.p_proj is null", projcat.getId());
			//循环所有工程项目根项目
			for(Proj proj : projList){
				this.createRow(row, sheet, rowColMap, proj,hql,countMap);
			}
			rowColMap.put("row", rowColMap.get("row")+1);
		}
		//添加合计
//		row = sheet.createRow(rowColMap.get("row"));
//		HSSFCell cell = row.createCell(0);
////		cell.setCellValue("合计");
//		HSSFCell cell1 = row.createCell(colCount+2);
////		cell1.setCellValue(countMap.get("ylj"));
//		HSSFCell cell2 = row.createCell(colCount+3);
//		cell2.setCellValue(countMap.get("nlj"));
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}*/
	/**
	 * excel 添加行 (暂弃用)
	 
	private void createRow(HSSFRow row,HSSFSheet sheet,Map<String,Integer> rowColMap,Object obj,String hql,Map<String,Double> countMap){
		row = sheet.createRow(rowColMap.get("row"));
		rowColMap.put("row", rowColMap.get("row")+1);
		if(obj.getClass().getName().indexOf("Projcat")!=-1){
			HSSFCell cell = row.createCell(0);
			Projcat projcat = (Projcat)obj;
			cell.setCellValue(projcat.getProjcatname());
		}else{
			Proj proj = (Proj)obj;
			if(proj.getC_proj().size()>0){
				HSSFCell cell = row.createCell(rowColMap.get("col"));
				cell.setCellValue(proj.getProjname());
				//添加月累计 年累计
				this.addCol(hql, proj, row,countMap);
				if("0".equals(proj.getIshow())){
					for(Proj pro : proj.getC_proj()){
						rowColMap.put("col",rowColMap.get("col")+1);
						this.createRow(row, sheet, rowColMap, pro,hql,countMap);
					}
				}
			}else{
				HSSFCell cell = row.createCell(rowColMap.get("col"));
				cell.setCellValue(proj.getProjname());
				//添加月累计 年累计
				this.addCol(hql, proj, row,countMap);
				
				rowColMap.put("col",1);
			}
		}
	}*/
	/**
	 * 根据工程项目 查询费用统计 添加每个仓库的金额
	 * @param hql
	 * @param id
	 * @param row
	 
	public void addCol(String hql,Proj proj,HSSFRow row,Map<String,Double> countMap){
		List<Ckgl> ckglList = ckglManager.getAll();
		for(int i=0;i<ckglList.size();i++){
			Ckgl ckgl = ckglList.get(i);
			List<Fytj> fylist = fytjManager.find(hql+" and s.proj.id = ? and s.ckgl.id = ?", proj.getId(),ckgl.getId());
			Fytj fytj = (fylist.size()>0)?fylist.get(0):null;
			if(fytj!=null){
				HSSFCell cell1 = row.createCell(colCount+(i));
				cell1.setCellValue(fytj.getByje());			
			}
		}
}*/
	public void setFytjManager(FytjManager fytjManager) {
		this.fytjManager = fytjManager;
	}

	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}

	public static Map getFytjCountMap() {
		return FytjCountMap;
	}

	public static void setFytjCountMap(Map fytjCountMap) {
		FytjCountMap = fytjCountMap;
	}

	public static int getSaveStart() {
		return saveStart;
	}

	public void setProjcatManager(ProjcatManager projcatManager) {
		this.projcatManager = projcatManager;
	}

	public void setProjManager(ProjManager projManager) {
		this.projManager = projManager;
	}

	public static void setSaveStart(int saveStart) {
		FytjAction.saveStart = saveStart;
	}

	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}
	
}
