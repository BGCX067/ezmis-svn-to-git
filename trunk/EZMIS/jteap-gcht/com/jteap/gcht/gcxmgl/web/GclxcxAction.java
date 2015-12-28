package com.jteap.gcht.gcxmgl.web;

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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
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
import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.gcht.gcxmgl.manager.GclxcxManager;
import com.jteap.gcht.gcxmgl.model.Wtd;
import com.jteap.wz.wzlysq.model.Wzlysq;
import com.jteap.wz.wzlysq.model.WzlysqDetail;

/**
 * 工程立项查询Action
 * @author 王艺
 * @version 创建时间：Jun 12, 2012 2:13:17 PM
 * 类说明
 */

@SuppressWarnings( { "unchecked", "serial" })
public class GclxcxAction extends AbstractAction{
    private GclxcxManager gclxcxManager;
	
    
    
	public GclxcxManager getGclxcxManager() {
		return gclxcxManager;
	}

	public void setGclxcxManager(GclxcxManager gclxcxManager) {
		this.gclxcxManager = gclxcxManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return gclxcxManager;
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

	/**
	 * 描述 : 查看所有立项单 作者 : WANGYI
	 */
	@SuppressWarnings("unchecked")
	public String showListAction() throws Exception {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(start))
			start = "1";

		int Start = Integer.parseInt(start);
		int Limit = Integer.parseInt(limit);
		
		String hqlWhere = request.getParameter("queryParamsSql");
		
		String xmbh = request.getParameter("xmbh");
		String status = request.getParameter("status");
		String sqbmqz = request.getParameter("sqbmqz");
		String sql = "";
		try {
			sql = "select * from tb_ht_wtd wtd where wtd.xmbh != ' ' ";

			if (StringUtils.isNotEmpty(xmbh)) {
				sql += " and (wtd.xmbh like '%" + xmbh + "%')";
			}
			if (StringUtils.isNotEmpty(status)) {
				if(status.equals("1")){
					sql += " and (wtd.status = '已立项')";
				}else{
					sql += " and (wtd.status != '已立项')";
				}
			}
			if (StringUtils.isNotEmpty(sqbmqz)) {
				sql += " and (wtd.sqbmqz = '%" + sqbmqz + "%')";
			}
			if (StringUtils.isNotEmpty(hqlWhere)) {
				sql += hqlWhere;
			}
			
			sql += " order by cjsj desc";
			Page page = this.pagedQueryTableData(sql,Start,Limit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID", "XMBH", "XMMC", "XMLX","XMYJ","CBFS","GCYS","KGRQ","SQBMQZ","SQBM","CJSJ","ISKG","STATUS"});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			System.out.println(sql);
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 分页查询指定sql数据
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		String countSql = "select count(*) from ("+sql+")";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
		.getBean("dataSource");
		try{
			conn = dataSource.getConnection();
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(countSql);
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			
			if(count<1)
				return new Page();

			sql="SELECT * FROM (SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit-1)+") WHERE RN >= "+start;
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			ResultSetMetaData rsmeta=rs.getMetaData();
			
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmeta.getColumnCount();i++){
					Object obj=rs.getObject(i);
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					obj = StringUtil.clobToStringByDB(obj);
					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		
	}
	
	
	/**
	 * 
	 * 描述 : 获取具体一个流程实例 作者 : caofei 时间 : Oct 28, 2010 参数 : 返回值 : 异常 :
	 */
	public String showProcessinstance() throws Exception {
		String lxid = request.getParameter("lxid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select t.processinstance_ from jbpm_variableinstance t where t.name_='docid' and t.stringvalue_='"
					+ lxid + "'";
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
	public InputStream exportWtdExcel() throws Exception {
		String idsArr = request.getParameter("idsArr");
		// 所有被选中立项单id
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
				Wtd wtd = gclxcxManager.get(idsArray[i]);
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
					cell.setCellValue("工程项目编号");

					cell = row.createCell(1);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("申请状态");

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
					cell = row.createCell(7);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("是否开工");
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
				cell.setCellValue(wtd.getXmbh());
				cell = row.createCell(1);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getStatus());
				cell = row.createCell(2);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getSqbm());
				cell = row.createCell(3);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getSqbmqz());
				cell = row.createCell(4);
				cell.setCellStyle(style2);
				
				cell.setCellValue(wtd.getSqbmqzsj());
				cell = row.createCell(5);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getXmyj());
				cell = row.createCell(6);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getXmmc());
				cell = row.createCell(7);
				cell.setCellStyle(style2);
				cell.setCellValue(wtd.getIskg());
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
				bis = new BufferedInputStream(this.exportWtdExcel());
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
