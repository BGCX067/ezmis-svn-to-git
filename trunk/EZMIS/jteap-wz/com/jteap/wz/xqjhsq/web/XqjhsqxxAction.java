package com.jteap.wz.xqjhsq.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

/**
 * 需求计划申请查询Action
 * 
 * @author nicky
 * 
 */
@SuppressWarnings( { "unchecked", "serial", "unused" })
public class XqjhsqxxAction extends AbstractAction {

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

	@Override
	public HibernateEntityDao getManager() {
		return xqjhsqManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj",
				"czy", "czyxm", "flowStatus", "isBack", "isUpdate", "xgsjsz",
				"scsjsz", "xqjhqf", "xjsjsz", "lydid", "qmzt", "xqjhsqDetail",
				"sqbmmc", "status", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj",
				"czy", "czyxm", "flowStatus", "isBack", "isUpdate", "xgsjsz",
				"scsjsz", "xqjhqf", "xjsjsz", "lydid", "qmzt", "xqjhsqDetail",
				"sqbmmc", "status" };
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		String wzmc = request.getParameter("wzmc");
		String xhgg = request.getParameter("xhgg");
		String xqjhsqId = "";
		if (StringUtils.isNotEmpty(wzmc)) {
			List<Xqjhsq> xqjhsqList = new ArrayList<Xqjhsq>();
			// 获取所有的需求计划申请主单
			xqjhsqList = xqjhsqManager.getAll();
			for (int i = 0; i < xqjhsqList.size(); i++) {
				Xqjhsq xqjhsq = xqjhsqList.get(i);
				Set<XqjhsqDetail> xqjhsqDetailSet = new HashSet<XqjhsqDetail>();
				xqjhsqDetailSet = xqjhsq.getXqjhsqDetail();
				Iterator<XqjhsqDetail> it = xqjhsqDetailSet.iterator();
				for (Iterator iterator = xqjhsqDetailSet.iterator(); iterator
						.hasNext();) {
					XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) iterator.next();
					if(StringUtils.isNotEmpty(xhgg)){
						if (xqjhsqDetail.getWzmc().contains(wzmc)&&xqjhsqDetail.getXhgg().contains(xhgg)) {
							xqjhsqId += "'" + xqjhsq.getId() + "',";
							break;
						}
					}else{
						if (xqjhsqDetail.getWzmc().contains(wzmc)) {
							xqjhsqId += "'" + xqjhsq.getId() + "',";
							break;
						}
					}
				}
			}
			if (StringUtils.isNotEmpty(xqjhsqId)) {
				xqjhsqId = "(" + StringUtils.removeEnd(xqjhsqId, ",") + ")";
			}
		}else 
		if (StringUtils.isNotEmpty(xhgg)) {
			List<Xqjhsq> xqjhsqList = new ArrayList<Xqjhsq>();
			// 获取所有的需求计划申请主单
			xqjhsqList = xqjhsqManager.getAll();
			for (int i = 0; i < xqjhsqList.size(); i++) {
				Xqjhsq xqjhsq = xqjhsqList.get(i);
				Set<XqjhsqDetail> xqjhsqDetailSet = new HashSet<XqjhsqDetail>();
				xqjhsqDetailSet = xqjhsq.getXqjhsqDetail();
				Iterator<XqjhsqDetail> it = xqjhsqDetailSet.iterator();
				for (Iterator iterator = xqjhsqDetailSet.iterator(); iterator
						.hasNext();) {
					XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) iterator.next();
					if(StringUtils.isNotEmpty(xqjhsqDetail.getXhgg())){
						if (xqjhsqDetail.getXhgg().contains(xhgg)) {
							xqjhsqId += "'" + xqjhsq.getId() + "',";
							break;
						}
					}
				}
			}
			if (StringUtils.isNotEmpty(xqjhsqId)) {
				xqjhsqId = "(" + StringUtils.removeEnd(xqjhsqId, ",") + ")";
			}
		}
		if(StringUtils.isNotEmpty(wzmc)){
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				if (StringUtils.isNotEmpty(xqjhsqId)) {
					hqlWhereTemp += " and obj.id in " + xqjhsqId;
				}else{
					hqlWhereTemp += " and 1!=1";
				}
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}else if(StringUtils.isNotEmpty(xhgg)){
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				if (StringUtils.isNotEmpty(xqjhsqId)) {
					hqlWhereTemp += " and obj.id in " + xqjhsqId;
				}else{
					hqlWhereTemp += " and 1!=1";
				}
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}else{
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "xqjhsqbh", "desc");
		}
	}

	/**
	 * 
	 * 描述 : 获取具体一个流程实例 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : 异常 :
	 */
	public String showProcessinstance() throws Exception {
		String xqjhsqid = request.getParameter("xqjhsqid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select t.processinstance_ from jbpm_variableinstance t where t.name_='docid' and t.stringvalue_='"
					+ xqjhsqid + "'";
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
		sheet.setColumnWidth(0, 2300);
		sheet.setColumnWidth(1, 4700);
		sheet.setColumnWidth(2, 8500);
		sheet.setColumnWidth(3, 2300);
		sheet.setColumnWidth(4, 2500);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3300);

		HSSFPrintSetup hps = sheet.getPrintSetup();
		hps.setVResolution((short) 300); // 打印状态
		hps.setPageStart((short) 0); // 起始页码
		hps.setHeaderMargin((double) 0.2); // 页眉
		hps.setFooterMargin((double) 0.6); // 页脚

		sheet.setMargin(HSSFSheet.LeftMargin, (short) 0.5); // 左页边距
		sheet.setMargin(HSSFSheet.RightMargin, (short) 0.1); // 右页边距
		// sheet.setMargin(HSSFSheet.TopMargin,(short)0.8); //上边距
		// sheet.setMargin(HSSFSheet.BottomMargin, (short)0.6); //下边距

		sheet.setHorizontallyCenter(true); // 水平居中

		// 创建字体样式
		// HSSFFont font = workbook.createFont();
		// font.setFontName("Verdana");
		// font.setBoldweight((short) 100);
		// font.setFontHeight((short) 300);
		// font.setColor(HSSFColor.BLUE.index);

		// 创建单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
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
		style3.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setFillForegroundColor(HSSFColor.YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
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

		// 创建标题行
		// HSSFRow titleRow = null;
		// titleRow = sheet.createRow(0);
		// titleRow.setHeight((short) 300);// 设定行的高度
		// HSSFCell titleCell = null;
		// titleCell = titleRow.createCell(0);
		// titleCell.setCellValue("测试PIO");
		// titleCell.setCellStyle(style);// 设置单元格样式
		// 合并单元格(startRow，endRow，startColumn，endColumn)
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style1.setWrapText(true);// 自动换行

		// 列头样式
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
		double slsum = 0.0;
		double jesum = 0.0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < idsArray.length; i++) {
				Xqjhsq xqjhsq = xqjhsqManager.get(idsArray[i]);
				if (i == 0 || flag == true) {
					// 创建行
					if (i == 0) {
						row = sheet.createRow(0);
					} else if (flag == true) {
						row = sheet.createRow(count);
					}

					// 创建单元格,设置每个单元格的值（作为表头）
					cell = row.createCell(0);
					cell.setCellStyle(titleStyle);
					if (("1").equals(xqjhsq.getXqjhqf())) {
						cell.setCellValue("申请编号");
					} else if (("2").equals(xqjhsq.getXqjhqf())) {
						cell.setCellValue("申请编号");
					}
					cell = row.createCell(1);
					cell.setCellStyle(titleStyle);
					if (("1").equals(xqjhsq.getXqjhqf())) {
						cell.setCellValue("需求申请状态");
					} else if (("2").equals(xqjhsq.getXqjhqf())) {
						cell.setCellValue("补料申请状态");
					}
					cell = row.createCell(2);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("申请部门");
					cell = row.createCell(3);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("申请人");
					cell = row.createCell(4);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("申请时间");
					cell = row.createCell(5);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("工程类别");
					cell = row.createCell(6);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("工程项目");
					flag = false;
					count++;
				}

				if (i == 0) {
					row = sheet.createRow(i + 1);
				} else {
					row = sheet.createRow(count);
				}
				row.setHeight((short) 300);
				cell = row.createCell(0);
				cell.setCellStyle(style2);
				cell.setCellValue(xqjhsq.getXqjhsqbh());
				cell = row.createCell(1);
				cell.setCellStyle(style2);
				if (StringUtils.isNotEmpty(xqjhsq.getStatus())) {
					if (("0").equals(xqjhsq.getStatus())) {
						status = "待审批";
					} else if (("1").equals(xqjhsq.getStatus())) {
						status = "已完成";
					}
				} else {
					status = "草稿";
				}
				cell.setCellValue(status);
				cell = row.createCell(2);
				cell.setCellStyle(style2);
				cell.setCellValue(xqjhsq.getSqbmmc());
				cell = row.createCell(3);
				cell.setCellStyle(style2);
				cell.setCellValue(xqjhsq.getCzyxm());
				cell = row.createCell(4);
				cell.setCellStyle(style2);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				cell.setCellValue(format.format(xqjhsq.getSqsj()));
				cell = row.createCell(5);
				cell.setCellStyle(style2);
				cell.setCellValue(xqjhsq.getGclb());
				cell = row.createCell(6);
				cell.setCellStyle(style2);
				cell.setCellValue(xqjhsq.getGcxm());
				count++;
				// 判断是否用子表数据
				String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ?";
				Object args[] = { idsArray[i] };
				List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
				xqjhsqDetailList = xqjhsqDetailManager.find(hql, args);
				int xhcount = 0;
				for (int j = 0; j < xqjhsqDetailList.size(); j++) {
					xhcount++;
					XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) xqjhsqDetailList
							.get(j);
					if (j == 0) {
						// 创建行
						row = sheet.createRow(count);

						// 创建单元格,设置每个单元格的值（作为表头）
						cell = row.createCell(0);
						cell.setCellStyle(style);
						cell.setCellValue("序号");
						cell = row.createCell(1);
						cell.setCellStyle(style);
						cell.setCellValue("物资名称");
						cell = row.createCell(2);
						cell.setCellStyle(style);
						cell.setCellValue("型号规格");
						cell = row.createCell(3);
						cell.setCellStyle(style);
						cell.setCellValue("申请数量");
						cell = row.createCell(4);
						cell.setCellStyle(style);
						cell.setCellValue("计量单位");
						sheet.addMergedRegion(new CellRangeAddress(count,
								count, 5, 6));
						cell = row.createCell(5);
						cell.setCellStyle(style);
						cell.setCellValue("备注");
						cell = row.createCell(6);
						cell.setCellStyle(style);

						// cell = row.createCell(5);
						// cell.setCellStyle(style);
						// cell.setCellValue("金额");
						// cell = row.createCell(6);
						// cell.setCellStyle(style);
						// cell.setCellValue("备注");
						count++;
					}
					// 插入数据
					row = sheet.createRow(count);
					row.setHeight((short) 300);
					cell = row.createCell(0);
					cell.setCellStyle(style2);
					cell.setCellValue(xhcount);
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getWzmc());
					cell = row.createCell(2);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getXhgg());
					cell = row.createCell(3);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getSqsl());
					cell = row.createCell(4);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getJldw());
					sheet.addMergedRegion(new CellRangeAddress(count, count, 5,
							6));
					cell = row.createCell(5);
					cell.setCellStyle(style2);
					cell.setCellValue(xqjhsqDetail.getRemark());
					cell = row.createCell(6);
					cell.setCellStyle(style2);

					// cell = row.createCell(5);
					// cell.setCellStyle(style2);
					// cell.setCellValue(xqjhsqDetail.getJe());
					// cell = row.createCell(6);
					// cell.setCellStyle(style2);
					// cell.setCellValue(xqjhsqDetail.getRemark());
					slsum += xqjhsqDetail.getSqsl();
					jesum += xqjhsqDetail.getJe();
					count++;
					if (j == xqjhsqDetailList.size() - 1) {
						flag = true;
					}
				}
			}
			// //申请数量，金额合计
			// row = sheet.createRow(count);
			//			
			// // 创建单元格,设置每个单元格的值（作为表头）
			// cell = row.createCell(0);
			// cell.setCellStyle(style3);
			// cell.setCellValue("合计:");
			// cell = row.createCell(1);
			// cell.setCellStyle(style3);
			// cell.setCellValue(" ");
			// cell = row.createCell(2);
			// cell.setCellStyle(style3);
			// cell.setCellValue(" ");
			// cell = row.createCell(3);
			// cell.setCellStyle(style3);
			// cell.setCellValue(slsum);
			// cell = row.createCell(4);
			// cell.setCellStyle(style3);
			// cell.setCellValue(" ");
			// cell = row.createCell(5);
			// cell.setCellStyle(style3);
			// cell.setCellValue(jesum);
			// cell = row.createCell(6);
			// cell.setCellStyle(style3);
			// cell.setCellValue(" ");
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

}
