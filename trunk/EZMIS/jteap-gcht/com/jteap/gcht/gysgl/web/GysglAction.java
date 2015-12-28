package com.jteap.gcht.gysgl.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.gcxmgl.model.Wtd;
import com.jteap.gcht.gysgl.manager.GysglManager;
import com.jteap.gcht.ztbgl.manager.ZtbglManager;

/**
 * 供应商管理Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unused", "unchecked"})
public class GysglAction extends AbstractAction {

	private GysglManager gysglManager;

	

	public GysglManager getGysglManager() {
		return gysglManager;
	}

	public void setGysglManager(GysglManager gysglManager) {
		this.gysglManager = gysglManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	/**
	 * 
	 * 描述 : 供应商显示列表Action
	 * 作者 : wangyun
	 * 时间 : 2010-11-17
	 * 
	 */
	public String showGysxxAction() {
		String flbm = request.getParameter("flbm");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = "20";

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		// 排序条件
		String orderSql = "";
		if (StringUtil.isNotEmpty(sort)) {
			orderSql = " order by " + sort + " " + dir;
		}

		// 查询条件
		String sqlWhere = request.getParameter("queryParamsSql");
		String sqlWhereTemp = "";
		if (StringUtils.isNotEmpty(sqlWhere)) {
			if (StringUtil.isNotEmpty(flbm)) {
				if("01".equals(flbm)){
					sqlWhereTemp += " and a.gyslb ='固定供应商'";
				}else{
					sqlWhereTemp += " and a.gyslb ='临时供应商'";
				}
			}
			sqlWhereTemp = sqlWhere.replace("$", "%");
		} else {
			if (StringUtil.isNotEmpty(flbm)) {
				if("01".equals(flbm)){
					sqlWhereTemp += "  a.gyslb ='固定供应商'";
				}else{
					sqlWhereTemp += "  a.gyslb ='临时供应商'";
				}
			}
		}

		try {
			List<Map<String, Object>> list = gysglManager.findGysxxList(sqlWhereTemp, orderSql);

			// 分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if (limitIndex > list.size()) {
				limitIndex = list.size();
			}
			List<Map<String, Object>> pageList = list.subList(startIndex, limitIndex);

			String[] arrayJson = new String[] { "id", "gysmc", "frdb", "qylx", "catalogId", "gyslb", "swlxr", "swlxrdh", "gysdz", "khyh", "yhzh","yffzr","yffzrlxfs","yfsgfzr","yfsgfzrlxfs"};
			String json = JSONUtil.listToJson(pageList, arrayJson);
			json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 选择性导出Excel
	 */
	public InputStream exportGysExcel() throws Exception {
		String idsArr = request.getParameter("idsArr");
		// 所有被选中供應商id
		String idsArray[] = idsArr.split(",");

		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 9000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6500);
		sheet.setColumnWidth(6, 7500);
		sheet.setColumnWidth(7, 7500);

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

		String status = "";
		HSSFRow row = null;
		HSSFCell cell = null;
		int count = 0;
		boolean flag = false;
//		double slsum = 0.0;
//		double jesum = 0.0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < idsArray.length; i++) {
				Map<String, Object> gysxx = gysglManager.findGysxxById(idsArray[i]);
				if(i == 0 || flag == true){
					// 创建行
					if(i == 0){
						row = sheet.createRow(0);
					}else if(flag == true){
						row = sheet.createRow(count);
					}
					
					// 创建单元格,设置每个单元格的值（作为表头）
					cell = row.createCell(0);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("供应商名称");

					cell = row.createCell(1);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("供应商类别");

					cell = row.createCell(2);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("商务联系人");
					cell = row.createCell(3);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("商务联系电话");
					cell = row.createCell(4);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("开户行");
					cell = row.createCell(5);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("银行账号");
					cell = row.createCell(6);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("地址");
					cell = row.createCell(7);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("备注");
					flag = false;
					count++;
				}
				
				if(i == 0){
					row = sheet.createRow(i + 1);
				}else{
					row = sheet.createRow(count);
				}
				row.setHeight((short) 300);
				cell = row.createCell(0);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("gysmc")==null?"":gysxx.get("gysmc").toString());
				cell = row.createCell(1);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("gyslb")==null?"":gysxx.get("gyslb").toString());
				cell = row.createCell(2);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("swlxr")==null?"":gysxx.get("swlxr").toString());
				cell = row.createCell(3);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("swlxrdh")==null?"":gysxx.get("swlxrdh").toString());
				cell = row.createCell(4);
				cell.setCellStyle(style2);
				
				cell.setCellValue(gysxx.get("khyh")==null?"":gysxx.get("khyh").toString());
				cell = row.createCell(5);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("yhzh")==null?"":gysxx.get("yhzh").toString());
				cell = row.createCell(6);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("gysdz")==null?"":gysxx.get("gysdz").toString());
				cell = row.createCell(7);
				cell.setCellStyle(style2);
				cell.setCellValue(gysxx.get("bz")==null?"":gysxx.get("bz").toString());
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
	
	public void exportSelectedExcelAction() throws IOException, SecurityException,
			IllegalArgumentException, NoSuchFieldException,
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
				bis = new BufferedInputStream(this.exportGysExcel());
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
}
