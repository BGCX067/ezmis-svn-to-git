/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.sfzjtj.web;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.sfzjtj.manager.SfzjManager;
import com.jteap.wz.sfzjtj.model.Sfzj;
import com.jteap.wz.tjny.manager.TjnyManager;
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class SfzjAction extends AbstractAction{
	
	private SfzjManager sfzjManager;
	private CkglManager ckglManager;
	private TjnyManager tjnyManager;
	
	//存放收发资金到当前页的统计总和
	private static Map sfzjCountMap  ;
	//保存开始条数
	private static int saveStart;
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return sfzjManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return  new String[]{"id","yckc","rkd","rkje","ckd","ckje","ymkc","ckmc","yf","zrje"};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		this.isUseQueryCache=false;
		try {
			sfzjManager.getSfzjByZj();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ck = request.getParameter("ckmc");
		String nf = request.getParameter("nf");
		String yf = request.getParameter("yf");
		String ny = request.getParameter("ny");
		 
		if(StringUtil.isNotEmpty(ny)){
			String n = ny.split("年")[0];
			String y = ny.split("年")[1].split("月")[0];
			HqlUtil.addCondition(hql, "nf",n);
			HqlUtil.addCondition(hql, "yf",y);
		}
		if(StringUtil.isNotEmpty(nf)){
			HqlUtil.addCondition(hql, "nf",nf);
		}else if(StringUtil.isEmpty(ny)){
			HqlUtil.addCondition(hql, "nf",sfzjManager.getMaxYear());
		}
		if(StringUtil.isNotEmpty(yf)){
			HqlUtil.addCondition(hql, "yf",yf);
		}else if(StringUtil.isEmpty(ny)){
			HqlUtil.addCondition(hql, "yf",sfzjManager.getMaxMoth());
		}
		if(StringUtil.isNotEmpty(ck)){
			HqlUtil.addCondition(hql, "ck.id",ck);
		}
		 
		//按年倒序
		HqlUtil.addOrder(hql, "nf", "desc");
		//按月倒序
		HqlUtil.addOrder(hql, "yf", "desc");
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
			if(pageFlag==null)
				pageFlag = request.getParameter(PAGE_FLAG);
			String json;
			if(pageFlag != null && pageFlag.toString().equals(PAGE_FLAG_NO)){
				Collection list = this.getManager().find(hql.toString(),showListHqlValues.toArray());
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:"
						+ json + "}";
				
			}else{
				json = getPageCollectionJson(hql.toString(),showListHqlValues.toArray());
			}
			this.outputJson(json);
//			System.out.println(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}
	/**
	 * 根据hql，分页查询
	 * 
	 * @param hql
	 * @return
	 */
	protected String getPageCollectionJson(String hql,Object... values) {
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// 开始分页查询
		Page page = getManager().pagedQueryWithStartIndex(hql,
				Integer.parseInt(start), Integer.parseInt(limit),isUseQueryCache,values);
		Collection obj = (Collection) page.getResult();
		List<Sfzj> sfzjList= (List)obj;
		//分页数据集合
		List jsonList = new ArrayList();
		for(Sfzj sfzj:sfzjList){
			Map sfzjMap = new HashMap();
			sfzjMap.put("id", sfzj.getId());
			sfzjMap.put("ckmc", sfzj.getCk().getCkmc());
			sfzjMap.put("yf", sfzj.getYf());
			sfzjMap.put("yckc",sfzj.getYckc().doubleValue());
			sfzjMap.put("rkd", sfzj.getRkd());
			sfzjMap.put("rkje",sfzj.getRkje().doubleValue());
			sfzjMap.put("ckd",sfzj.getCkd());
			sfzjMap.put("ckje",sfzj.getCkje().doubleValue());
			sfzjMap.put("ymkc",sfzj.getYmkc().doubleValue());
			//转入金额 2011-10月份统计使用 后续统计注释
			
			sfzjMap.put("zrje",(sfzj.getZrje()==null)?0:sfzj.getZrje().doubleValue());
			jsonList.add(sfzjMap);
		}
		Map countMaps = new HashMap();
		if(jsonList.size()>0){
			//判断是否有前一页 没有则为第一页 则新建对象
			if(page.hasPreviousPage()==false){
				//保存开始条数
				saveStart = Integer.parseInt(start);
				sfzjCountMap = new HashMap();
				sfzjCountMap.put("id",UUIDGenerator.hibernateUUID());
				sfzjCountMap.put("ckmc","<font color='blue'>到当前页总和：</font>");
				sfzjCountMap.put("yckc",0.0);
				sfzjCountMap.put("rkd",0.0);
				sfzjCountMap.put("rkje",0.0);
				sfzjCountMap.put("ckd",0.0);
				sfzjCountMap.put("ckje",0.0);
				sfzjCountMap.put("zrje",0.0);
				sfzjCountMap.put("ymkc",0.0);
				//转入金额 2011-10月份统计使用 后续统计注释
				sfzjCountMap.put("zrje",0.0);
				sfzjManager.setCountValue(sfzjList,sfzjCountMap,"d");
			}else{
				 //如果上一次保存的开始条数大于这次传来的开始条数，则是点击上一页
				 //否则 点击的是下一页
				 if(saveStart>Integer.parseInt(start)){
					Page pages = getManager().pagedQueryWithStartIndex(hql,
							 saveStart, Integer.parseInt(limit),isUseQueryCache,values);
					 obj = (Collection) pages.getResult();
					 sfzjList= (List)obj;
					 sfzjManager.setCountValue(sfzjList,sfzjCountMap,"q");
				 }else{
					 sfzjManager.setCountValue(sfzjList,sfzjCountMap,"d");
				 }
				 saveStart = Integer.parseInt(start);
				 //如果最后一页标志存在 则删除这个标志
				 if(sfzjCountMap.get("last")!=null){
					 sfzjCountMap.remove("last");
				 }
				
			}
		
			//如果有记录 则添加合计
			List list = sfzjManager.find(hql);
			if(list.size()>0){
				//添加合计
				countMaps.put("id",UUIDGenerator.hibernateUUID());
				countMaps.put("ckmc","<font color='red'>总合计：</font>");
				sfzjManager.setCountValue(list, countMaps,"h");
				//该页如果没有下一页  则是最后一页 加入标志
				if(!page.hasNextPage()){
					sfzjManager.setCountValue(list, sfzjCountMap,"h");
					sfzjCountMap.put("last", "y");
				} 
			}
		}else{
			sfzjCountMap = new HashMap();
			countMaps = new HashMap();
		}
		
		// 将集合JSON化
		String json = JSONUtil.listToJson(jsonList, listJsonProperties());
		//到当前页的数据的json字符串
		String pageTotalJson = JSONUtil.mapToJson(sfzjCountMap);
		//System.out.println("-------"+pageTotalJson+"-----------");
		//总合计的json字符串
		String mainTotalJson = JSONUtil.mapToJson(countMaps) ;
		
		StringBuffer dataBlock = new StringBuffer();
		long total = page.getTotalCount()+(page.getTotalPageCount()-1)*2+1;
		dataBlock.append("{totalCount:'" + page.getTotalCount()+ "',list:"
				+ json + ",pageTotal:"+pageTotalJson+",mainTotal:"+mainTotalJson+"}");
		//System.out.println(dataBlock.toString());
		return dataBlock.toString();
	}
	
	//导出Excel
	public String exportExcel() throws Exception {
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = WebUtils.getRequestParam(request, "paraHeader");
		//System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");
		//System.out.println(paraDataIndex);
		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		
		String nf  = request.getParameter("nf");
		String yf = request.getParameter("yf");
		String ckid = request.getParameter("ckid");
		String hql  = "from Sfzj s where s.nf='"+nf+"' and yf='"+yf+"'";
		//如果仓库id为空
		if(StringUtil.isNotEmpty(ckid)&&!"undefined".equals(ckid)){
			hql = hql+" and ck.id = '"+ckid+"'";
		}
		List<Sfzj> sfzjs =sfzjManager.find(hql);
		//System.out.println(hql);
		//System.out.println(sfzjs.size());
		if(sfzjs.size()<1){
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		}
		
		sfzjManager.setCountValue(sfzjs, null,"");
		export(sfzjs, paraHeader, paraDataIndex.replace("ckmc", "ck.ckmc"), paraWidth,nf,yf);
	 
		// 调用导出方法
		return NONE;
	}
	/**
	 *  封面导出
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
				bis = new BufferedInputStream(this.exportFmExcel());
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
	 * 资金表封面打印
	 */
	public InputStream exportFmExcel() throws Exception {
		//选定打印
		String id = request.getParameter("id");
		//条件打印
//		String parWhere = request.getParameter("parWhere");
	//	String idsArray[] = id.split(",");
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 3700);
		sheet.setColumnWidth(2, 6400);
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


		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
 
		HSSFRow row = null;
		HSSFCell cell = null;
		@SuppressWarnings("unused")
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		StringBuffer hql = new StringBuffer("from Sfzj obj ");
		
		if(StringUtils.isNotEmpty(id)){
			hql.append(" where obj.id in ("+id+")");
		}
		List<Sfzj> sfzjList = sfzjManager.find(hql.toString());
		
		try {
			for (int i = 1; i <= sfzjList.size(); i++) {
					Sfzj sfzj = sfzjList.get(i-1);
					if (sfzj != null) {
						// 上间隔
						// 创建标题行
						row = sheet.createRow(10 * i - 9);
						row.setHeight((short) 500);// 设定行的高度
						cell = row.createCell(0);
						cell.setCellStyle(style1);// 设置单元格样式
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 9,
								10 * i - 9, 0, 7));

						// 第一行
						row = sheet.createRow(10 * i - 8);
						row.setHeight((short) 400);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(style1);

						cell = row.createCell(1);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);

						cell = row.createCell(3);
						cell.setCellStyle(style1);

						cell = row.createCell(4);
						cell.setCellStyle(style1);

						cell = row.createCell(5);
						cell.setCellStyle(style1);
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
								10 * i - 8, 6, 7));
						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);

						// 第二行
						row = sheet.createRow(10 * i - 7);
						row.setHeight((short) 500);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(style1);

						cell = row.createCell(1);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);

						cell = row.createCell(3);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
								10 * i - 7, 4, 7));
						cell = row.createCell(4);
						cell.setCellStyle(style1);

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);

						// 第三行
						row = sheet.createRow(10 * i - 6);
						row.setHeight((short) 500);
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
								10 * i - 6, 0, 1));
						cell = row.createCell(0);
						cell.setCellStyle(style1);
						cell.setCellValue("仓库名称：" +sfzj.getCk().getCkmc());

						cell = row.createCell(1);
						cell.setCellStyle(style1);
						//					
						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);
						cell.setCellValue("月初库存：" +sfzj.getYckc());

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
								10 * i - 6, 3, 7));
						cell = row.createCell(3);
						cell.setCellStyle(style1);
						cell.setCellValue("月末库存：" + sfzj.getYmkc());

						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 4, 5));
						cell = row.createCell(4);
						cell.setCellStyle(style1);
						// cell.setCellValue("");

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);


						// 第四行
						row = sheet.createRow(10 * i - 5);
						row.setHeight((short) 500);
						sheet.addMergedRegion(new CellRangeAddress(10 * i - 5,
								10 * i - 5, 0, 2));
						cell = row.createCell(0);
						cell.setCellStyle(style1);
						cell.setCellValue("入库单：" +sfzj.getRkd()+" 张           入库金额："+sfzj.getRkje()+" 元");
						
						cell = row.createCell(1);
						cell.setCellStyle(style1);
						//					
						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 2, 3));
						cell = row.createCell(2);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
								10 * i - 6, 3, 7));
						cell = row.createCell(3);
						cell.setCellStyle(style1);
						cell.setCellValue("出库单：" +sfzj.getCkd()+" 张           出库金额："+sfzj.getCkje()+" 元");

						// sheet.addMergedRegion(new CellRangeAddress(10*i-6,
						// 10*i-6, 4, 5));
						cell = row.createCell(4);
						cell.setCellStyle(style1);
						// cell.setCellValue("");

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);

						// 第五行
						row = sheet.createRow(10 * i - 4);
						row.setHeight((short) 1000);

						cell = row.createCell(0);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
								10 * i - 4, 1, 2));
						cell = row.createCell(1);
						cell.setCellStyle(style1);

						cell = row.createCell(2);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
								10 * i - 4, 3, 4));
						cell = row.createCell(3);
						cell.setCellStyle(style1);

						cell = row.createCell(4);
						cell.setCellStyle(style1);

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);
						

						cell = row.createCell(7);
						cell.setCellStyle(style1);
						

						// 间隔
						row = sheet.createRow(10 * i - 3);
						row.setHeight((short) 150);

						// 第六行
						row = sheet.createRow(10 * i - 2);
						row.setHeight((short) 350);

						cell = row.createCell(0);
						cell.setCellStyle(style1);

						cell = row.createCell(1);
						cell.setCellStyle(style1);

						cell = row.createCell(2);
						cell.setCellStyle(style1);

						sheet.addMergedRegion(new CellRangeAddress(10 * i - 2,
								10 * i - 2, 3, 5));
						cell = row.createCell(3);
						cell.setCellStyle(style1);

						cell = row.createCell(4);
						cell.setCellStyle(style1);

						cell = row.createCell(5);
						cell.setCellStyle(style1);

						cell = row.createCell(6);
						cell.setCellStyle(style1);

						cell = row.createCell(7);
						cell.setCellStyle(style1);
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
	 * 导出Excel 
	 * @param col
	 * @param headers
	 * @param dataIndexs
	 * @param widths
	 * @param nf
	 * @param yf
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void export(Collection col, String headers, String dataIndexs,
			String widths,String nf,String yf) throws IOException, SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("物资收发资金动态表_" +nf+ "-"+yf+".xls")
						.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(this.getExcelInputStream(col,
					headers, dataIndexs, widths));
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
	 * 获得组装好数据了的inputStream
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private InputStream getExcelInputStream(Collection col, String paraHeader,
			String paraDataIndex, String paraWidth) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, UnsupportedEncodingException {

		if ("".equals(paraHeader) || paraHeader == null) {

		}

		if ("".equals(paraDataIndex) || paraDataIndex == null) {

		}

		if ("".equals(paraWidth) || paraWidth == null) {

		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		HSSFRow row = sheet.createRow(0);
		String[] headers = paraHeader.split(",");
		String[] dataIndexs = paraDataIndex.split(",");
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
		
		
		//设置列标题样式
		HSSFCellStyle titleStyle1 = wb.createCellStyle();
		// titleStyle.setBorderLeft((short)1);
		// titleStyle.setBorderRight((short)1);
		/*
		 * titleStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
		 * titleStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		 */
		titleStyle1.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		titleStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titleStyle1.setFont(font);
	
		HSSFCell cell1 = row.createCell(0);
		row.setHeight((short) 500);// 设定行的高度
		cell1.setCellValue("收发资金动态表");
		cell1.setCellStyle(titleStyle);// 设置单元格样式
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));
		
		row = sheet.createRow(1);
		for (int i = 0; i <headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			sheet.setColumnWidth(i,(Short
					.parseShort(widths[i]) * (short) 50));
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(titleStyle1);

		}

		//
		int count = 2;
		for (Iterator ite = col.iterator(); ite.hasNext();) {
			Object obj = ite.next();
			row = sheet.createRow(count);
			for (int i = 0; i < dataIndexs.length; i++) {
				Object returnValue = BeanUtils.getProperty(obj, dataIndexs[i]);
				HSSFCell cell = row.createCell(i);
//				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(returnValue == null ? "" : returnValue
						.toString());
			}
			count++;
		}

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

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public SfzjManager getSfzjManager() {
		return sfzjManager;
	}

	public void setSfzjManager(SfzjManager sfzjManager) {
		this.sfzjManager = sfzjManager;
	}

	public CkglManager getCkglManager() {
		return ckglManager;
	}

	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}

	public Map getSfzjCountMap() {
		return sfzjCountMap;
	}

	public static void setSfzjCountMap(Map sfzjCountMap) {
		SfzjAction.sfzjCountMap = sfzjCountMap;
	}

	public static void setSaveStart(int saveStart) {
		SfzjAction.saveStart = saveStart;
	}

	public static int getSaveStart() {
		return saveStart;
	}
 
}

