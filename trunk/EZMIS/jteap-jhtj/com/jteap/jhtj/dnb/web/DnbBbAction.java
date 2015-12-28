package com.jteap.jhtj.dnb.web;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.dnb.manager.DnbBbManager;
import com.jteap.jhtj.dnb.model.Dnb;
import com.jteap.system.jdbc.manager.JdbcManager;
@SuppressWarnings({ "unchecked", "serial" })
public class DnbBbAction extends AbstractAction {
	private DnbBbManager dnbBbManager;
	private JdbcManager jdbcManager;
	
	/**
	 * 
	 * 描述:得到电能表数据
	 * 时间:2010 11 17
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getDnbsAction() throws Exception{
		String curDate=request.getParameter("curDate");
		if(StringUtil.isEmpty(curDate)){
			curDate=DateUtils.getDate("yyyy-MM-dd");
		}
		String curRi=DateUtils.getDate(DateUtils.StrToDate(curDate, "yyyy-MM-dd"),"dd");
		String riJson=this.dnbBbManager.getDnbJsonByDate(curDate);
		String yueJson=this.dnbBbManager.getDnbDyjJsonByDate(curDate);
		String yue1qJson=this.dnbBbManager.getDnbDyjCydl1QJsonByDate(curDate);
		String yue2qJson=this.dnbBbManager.getDnbDyjCydl2QJsonByDate(curDate);
		String firstDate="";
		if(curRi.equals("01"))
			firstDate="firstDate:true";
		outputJson("{success:true,data:["+riJson+"]"+(!yueJson.equals("")?(","+yueJson):"")+(!yue1qJson.equals("")?(","+yue1qJson):"")+(!yue2qJson.equals("")?(","+yue2qJson):"")+(!firstDate.equals("")?(","+firstDate):"")+"}");
//		System.out.println("{success:true,data:["+riJson+"]"+(!yueJson.equals("")?(","+yueJson):"")+(!yue1qJson.equals("")?(","+yue1qJson):"")+(!yue2qJson.equals("")?(","+yue2qJson):"")+(!firstDate.equals("")?(","+firstDate):"")+"}");
		return NONE;
	}
	
	/**
	 * 导出电能表数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void exportDnbsExcelAction() throws Exception{
		String curDate = request.getParameter("curDate");
		Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
		String curYmd=DateUtils.getDate(curD, "yyyy年MM月dd日");
		String endDate=DateUtils.getDate(curD,"dd日");
		
		BufferedOutputStream bufferedOutputStream = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/jteap/jhtj/dnbbb/dnlrbb.xls");
			File file = new File(path);
			InputStream inputStream = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			//表头style
			HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
			cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);			//居中
			cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	//垂直居中
			cellTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);		//下边框
			cellTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);			//左边框
			cellTitleStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);			//右边框
			cellTitleStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);			//上边框
			
			//表身style
			HSSFCellStyle cellContentStyle = workbook.createCellStyle();
			cellContentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);		//下边框
			cellContentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);			//左边框
			cellContentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);			//右边框
			cellContentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);			//上边框
			
			//最右侧列style
			HSSFCellStyle cellRightStyle = workbook.createCellStyle();
			cellRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);			//下边框
			cellRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);			//左边框
			cellRightStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);			//右边框
			cellRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);				//上边框
			
			//格式化
			HSSFDataFormat dataFormat = workbook.createDataFormat();
			
			//综合厂用电率 一期
			HSSFCellStyle cellYqStyle = workbook.createCellStyle();
			cellYqStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);				//下边框
			cellYqStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);				//左边框
			cellYqStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);				//右边框
			cellYqStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);				//上边框
			cellYqStyle.setDataFormat(dataFormat.getFormat("0.00%"));			//百分比
			
			//综合厂用电率 一期
			HSSFCellStyle cellEqStyle = workbook.createCellStyle();
			cellEqStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);				//下边框
			cellEqStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);				//左边框
			cellEqStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);			//右边框
			cellEqStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);				//上边框
			cellEqStyle.setDataFormat(dataFormat.getFormat("0.00%"));			//百分比
			
			
			//表头
			HSSFCell titleCell = sheet.getRow(2).createCell(0);
			titleCell.setCellValue("湖北能源集团鄂州发电公司"+curYmd+"电能量日报表");
			titleCell.setCellStyle(cellTitleStyle);
			
			//表身
			Map<String, Object> map = dnbBbManager.getDnbJsonByDateForExcel(curDate);
			for(Map.Entry<String, Object> entry : map.entrySet()){
				String[] keyArray = entry.getKey().split("-");
				int row = Integer.parseInt(keyArray[0]);
				int col = Integer.parseInt(keyArray[1]);
				
				HSSFCell contentCell = sheet.getRow(row).createCell(col);
				if(keyArray.length == 4){
					//CT:xxx PT:xxx 表号
					contentCell.setCellValue((String)entry.getValue());
					contentCell.setCellStyle(cellContentStyle);
				}else {
					contentCell.setCellValue((Double)entry.getValue());
					if(keyArray.length == 3){
						contentCell.setCellStyle(cellRightStyle);
					}else if(keyArray.length == 5){
						contentCell.setCellStyle(cellYqStyle);
					}else if(keyArray.length == 6){
						contentCell.setCellStyle(cellEqStyle);
					}else {
						contentCell.setCellStyle(cellContentStyle);
					}
				}
			}
			
			//表尾
			HSSFCell bottomCell = sheet.getRow(132).createCell(1);
			bottomCell.setCellValue("当月均(01日至"+endDate+")");
			bottomCell.setCellStyle(cellContentStyle);
			
			/**一期综合厂用电率 当日均*/
			HSSFCell zhcydlCell00 = sheet.getRow(128).createCell(7);
			//设置计算式  
			zhcydlCell00.setCellFormula("(1-(I6+I14+I22+I30+I38+I50+I46)/(I58+I62+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell00.setCellStyle(cellYqStyle);
			
			HSSFCell zhcydlCell08 = sheet.getRow(129).createCell(7);
			//设置计算式  
			zhcydlCell08.setCellFormula("(1-(I7+I15+I23+I31+I39+I51+I47)/(I59+I63+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell08.setCellStyle(cellYqStyle);
			
			HSSFCell zhcydlCell16 = sheet.getRow(130).createCell(7);
			//设置计算式  
			zhcydlCell16.setCellFormula("(1-(I8+I16+I24+I32+I40+I52+I48)/(I60+I64+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell16.setCellStyle(cellYqStyle);
			
			HSSFCell zhcydlCell24 = sheet.getRow(131).createCell(7);
			//设置计算式  
			zhcydlCell24.setCellFormula("(1-(I9+I17+I25+I33+I41+I53+I49)/(I61+I65+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell24.setCellStyle(cellYqStyle);
			
			/**二期综合厂用电率 当日均*/
			HSSFCell zhcydlCell200 = sheet.getRow(128).createCell(8);
			//设置计算式  
			zhcydlCell200.setCellFormula("(1-(I70+I78+I94+I102-I46)/(I118+I122+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell200.setCellStyle(cellEqStyle);
			
			HSSFCell zhcydlCell208 = sheet.getRow(129).createCell(8);
			//设置计算式  
			zhcydlCell208.setCellFormula("(1-(I71+I79+I95+I103-I47)/(I119+I123+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell208.setCellStyle(cellEqStyle);
			
			HSSFCell zhcydlCell216 = sheet.getRow(130).createCell(8);
			//设置计算式  
			zhcydlCell216.setCellFormula("(1-(I72+I80+I96+I104-I48)/(I120+I124+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell216.setCellStyle(cellEqStyle);
			
			HSSFCell zhcydlCell224 = sheet.getRow(131).createCell(8);
			//设置计算式  
			zhcydlCell224.setCellFormula("(1-(I73+I81+I97+I105-I49)/(I121+I125+1))");
			//设置强制执行再计算
			sheet.setForceFormulaRecalculation(true);
			zhcydlCell224.setCellStyle(cellEqStyle);
			
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] data = outputStream.toByteArray();
			
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(("电能量日报表_"+curYmd+".xls").getBytes(), "iso-8859-1"));
			
			//开始输出
			bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
			bufferedOutputStream.write(data);
			bufferedOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bufferedOutputStream != null){
				bufferedOutputStream.close();
			}
		}
	}
	
	//获取所有电能表
	public String getAllDnbIdAction(){
		String sql = "select elecid,name from elec.elec_param t order by elecid";
		try {
			List<Dnb> list = jdbcManager.querySqlData(sql);
			String json = JSONUtil.listToJson(list, new String[]{"ELECID","NAME"});
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	//根据表编号获取
	public String getChangeById(){
		String id = request.getParameter("elecid");
		String sql = "select * from elec.elec_change where elecid="+id;
		
		List list;
		try {
			list = jdbcManager.querySqlData(sql);
			if(list.size() > 0){
				Map map = (Map)list.get(0);
				String json = JSONUtil.mapToJson(map);
				//System.out.println(json);
				this.outputJson(json);
			}else {
				this.outputJson("{exits:false}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	//保存电能表报表
	public String saveDnbBbAction() throws Exception{
		try {
			String strDt = request.getParameter("strDt");
			this.dnbBbManager.saveDnbDateYgzlz(strDt);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	//根据表编号获取表信息
	public String getByIdAction(){
		String id = request.getParameter("elecid");
		String sql = "select elecbh,cts,pts from elec.elec_param t where elecid="+id;
		try {
			List list = jdbcManager.querySqlData(sql);
			if(list.size() > 0){
				Map map = (Map) list.get(0);
				String json = JSONUtil.mapToJson(map);
				this.outputJson(json);
			}else {
				this.outputJson("{exits:false}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return dnbBbManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	public DnbBbManager getDnbBbManager() {
		return dnbBbManager;
	}

	public void setDnbBbManager(DnbBbManager dnbBbManager) {
		this.dnbBbManager = dnbBbManager;
	}
	
}
