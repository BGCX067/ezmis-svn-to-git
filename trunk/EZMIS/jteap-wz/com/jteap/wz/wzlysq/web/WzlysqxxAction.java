package com.jteap.wz.wzlysq.web;

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
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzlysq.manager.WzlysqDetailManager;
import com.jteap.wz.wzlysq.manager.WzlysqManager;
import com.jteap.wz.wzlysq.model.Wzlysq;
import com.jteap.wz.wzlysq.model.WzlysqDetail;

/**
 * 物资领用查询Action
 * 
 * @author nicky
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class WzlysqxxAction extends AbstractAction {

	private WzlysqManager wzlysqManager;
	
	private WzlysqDetailManager wzlysqDetailManager;
	
	private WzdaManager wzdaManager;

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}

	public WzlysqDetailManager getWzlysqDetailManager() {
		return wzlysqDetailManager;
	}

	public void setWzlysqDetailManager(WzlysqDetailManager wzlysqDetailManager) {
		this.wzlysqDetailManager = wzlysqDetailManager;
	}

	public WzlysqManager getWzlysqManager() {
		return wzlysqManager;
	}

	public void setWzlysqManager(WzlysqManager wzlysqManager) {
		this.wzlysqManager = wzlysqManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return wzlysqManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "bh", "czr", "lybm", "gclb", "sqsj",
				"gcxm", "zt", "xqjhsqbh", "sqbmmc", "czyxm", "czy",
				"flow_status", "lydqf", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "bh", "czr", "lybm", "gclb", "sqsj",
				"gcxm", "zt", "xqjhsqbh", "sqbmmc", "czyxm", "czy",
				"flow_status", "lydqf"};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		String hqlWhere = request.getParameter("queryParamsSql");
		String flag = request.getParameter("flag");
		String dysczr = request.getParameter("dysczr");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		//库存管理 需求到货 初始化的时候调用
		if(StringUtils.isNotEmpty(flag)){
			List<WzlysqDetail> wzlysqDetailList = wzlysqDetailManager.find("from WzlysqDetail where xqjhDetail.dyszt!=null and xqjhDetail.dyszt !='0'");
			String id = "";
			for(WzlysqDetail wzlysq:wzlysqDetailList){
				id =id+ "'"+wzlysq.getWzlysq().getId()+"',";
			}
			if(wzlysqDetailList.size()==0){
				hql.append(" and 1!=1");
			}else{
				HqlUtil.addCondition(hql,"id",id.substring(0, id.length()-1), HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
			}
			
		}
		//库存管理 需求到货领用查询 查询的时候调用
		if(StringUtils.isNotEmpty(dysczr)){
			List<WzlysqDetail> wzlysqDetailList = wzlysqDetailManager.find("from WzlysqDetail where xqjhDetail.dysczr=?",dysczr);
			String id = "";
			for(WzlysqDetail wzlysq:wzlysqDetailList){
				id =id+ "'"+wzlysq.getWzlysq().getId()+"',";
			}
			if(wzlysqDetailList.size()==0){
				hql.append(" and 1!=1");
			}else{
				HqlUtil.addCondition(hql,"id",id.substring(0, id.length()-1), HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
			}
			
		}
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "bh", "desc");
		}
		System.out.println(hql.toString());
	}

	/**
	 * 
	 * 描述 : 获取具体一个流程实例 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : 异常 :
	 */
	public String showProcessinstance() throws Exception {
		String wzlysqid = request.getParameter("wzlysqid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select t.processinstance_ from jbpm_variableinstance t where t.name_='docid' and t.stringvalue_='"
					+ wzlysqid + "'";
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
	public InputStream exportXqjhsqExcel() throws Exception {
		String idsArr = request.getParameter("idsArr");
		// 所有被选中需求计划申请id
		String idsArray[] = idsArr.split(",");

		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 7000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 3500);
		sheet.setColumnWidth(6, 3500);

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
				Wzlysq wzlysq = wzlysqManager.get(idsArray[i]);
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
					if(("1").equals(wzlysq.getLydqf())){
						cell.setCellValue("物资领用申请编号");
					}else if(("2").equals(wzlysq.getLydqf())){
						cell.setCellValue("物资借料申请编号");
					}
					cell = row.createCell(1);
					cell.setCellStyle(titleStyle);
					if(("1").equals(wzlysq.getLydqf())){
						cell.setCellValue("领用申请状态");
					}else if(("2").equals(wzlysq.getLydqf())){
						cell.setCellValue("借料申请状态");
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
				
				if(i == 0){
					row = sheet.createRow(i + 1);
				}else{
					row = sheet.createRow(count);
				}
				row.setHeight((short) 300);
				cell = row.createCell(0);
				cell.setCellStyle(style2);
				cell.setCellValue(wzlysq.getBh());
				cell = row.createCell(1);
				cell.setCellStyle(style2);
				if (StringUtils.isNotEmpty(wzlysq.getZt())) {
					if (("0").equals(wzlysq.getZt())) {
						status = "待审批";
					} else if (("1").equals(wzlysq.getZt())) {
						status = "已完成";
					}
				} else {
					status = "草稿";
				}
				cell.setCellValue(status);
				cell = row.createCell(2);
				cell.setCellStyle(style2);
				cell.setCellValue(wzlysq.getSqbmmc());
				cell = row.createCell(3);
				cell.setCellStyle(style2);
				cell.setCellValue(wzlysq.getCzyxm());
				cell = row.createCell(4);
				cell.setCellStyle(style2);
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				cell.setCellValue(format.format(wzlysq.getSqsj()));
				cell = row.createCell(5);
				cell.setCellStyle(style2);
				cell.setCellValue(wzlysq.getGclb());
				cell = row.createCell(6);
				cell.setCellStyle(style2);
				cell.setCellValue(wzlysq.getGcxm());
				count++;
				//判断是否用子表数据
				String hql = "from WzlysqDetail as x where x.wzlysq.id = ?";
				Object args[] = {idsArray[i]};
				List<WzlysqDetail> wzlysqDetailList = new ArrayList<WzlysqDetail>();
				wzlysqDetailList = wzlysqDetailManager.find(hql, args);
				int xhcount = 0;
				for (int j = 0; j < wzlysqDetailList.size(); j++) {
					xhcount++;
					WzlysqDetail wzlysqDetail = (WzlysqDetail)wzlysqDetailList.get(j);
					if(j == 0){
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
						cell.setCellValue("申领数量");
						cell = row.createCell(4);
						cell.setCellStyle(style);
						cell.setCellValue("计量单位");
						cell = row.createCell(5);
						cell.setCellStyle(style);
						cell.setCellValue("价格");
						cell = row.createCell(6);
						cell.setCellStyle(style);
						cell.setCellValue("金额");
						count++;
					}
					//插入数据
					row = sheet.createRow(count);
					row.setHeight((short) 300);
					cell = row.createCell(0);
					cell.setCellStyle(style2);
					cell.setCellValue(xhcount);
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					cell.setCellValue(wzdaManager.get(wzlysqDetail.getWzbm()).getWzmc());
					cell = row.createCell(2);
					cell.setCellStyle(style2);
					cell.setCellValue(wzdaManager.get(wzlysqDetail.getWzbm()).getXhgg());
					cell = row.createCell(3);
					cell.setCellStyle(style2);
					cell.setCellValue(wzlysqDetail.getSqsl());
					cell = row.createCell(4);
					cell.setCellStyle(style2);
					cell.setCellValue(wzlysqDetail.getJldw());
					cell = row.createCell(5);
					cell.setCellStyle(style2);
					cell.setCellValue(wzlysqDetail.getJg());
					cell = row.createCell(6);
					cell.setCellStyle(style2);
					cell.setCellValue(wzlysqDetail.getJe());
					count++;
					if(j == wzlysqDetailList.size()-1){
						flag = true;
					}
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
