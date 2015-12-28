package com.jteap.jx.dxxgl.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.JzjxjhManager;
import com.jteap.jx.dxxgl.manager.JzjxxmManager;
import com.jteap.jx.dxxgl.model.Jzjxjh;
import com.jteap.jx.dxxgl.model.Jzjxxm;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 机组检修项目action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial","unchecked", "unchecked", "deprecation"})
public class JzjxxmAction extends AbstractAction {

	private JzjxxmManager jzjxxmManager;
	private JzjxjhManager jzjxjhManager;
	private DictManager dictManager;
	
	/**
	 * 
	 * 描述 : 保存机组检修项目
	 * 作者 : wangyun
	 * 时间 : Aug 11, 2010
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String sszy = request.getParameter("sszy");
		String xmmc = request.getParameter("xmmc");
		String jlr = request.getParameter("jlr");
		String jlsj = request.getParameter("jlsj");
		String remark = request.getParameter("remark");
		String jhId = request.getParameter("jhId");
		try {
			
			Jzjxxm jzjxxm = null;
			if (StringUtil.isEmpty(id)) {
				jzjxxm = new Jzjxxm();
				Jzjxjh jzjxjh = jzjxjhManager.get(jhId);
				jzjxxm.setJzjxjh(jzjxjh);
			} else {
				jzjxxm = jzjxxmManager.get(id);
			}
			
			jzjxxm.setSszy(sszy);
			jzjxxm.setXmmc(xmmc);
			jzjxxm.setJlr(jlr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date dtJlsj = sdf.parse(jlsj);
			jzjxxm.setJlsj(dtJlsj);
			jzjxxm.setRemark(remark);
			jzjxxmManager.save(jzjxxm);
			outputJson("{success:true}");
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 生成机组检修项目报表
	 * 作者 : wangyun
	 * 时间 : Aug 11, 2010
	 * 异常 : IOException
	 * 
	 */
	public String exportExcelAction() throws IOException {
		String jhId = request.getParameter("jhId");
		Jzjxjh jzjxjh = jzjxjhManager.get(jhId);

		Date qsrq = jzjxjh.getQsrq();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 检修计划年份
		String year = sdf.format(qsrq);
		// 机组
		String jz = jzjxjh.getJz();
		// 计划名称
		String jhmc = jzjxjh.getJhmc();
		// 标题
		String title = year + jz + jhmc;

		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String((title + ".xls").getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(getExcelInputStream(jzjxjh, title));
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 得到excel输出流
	 * 作者 : wangyun
	 * 时间 : Aug 11, 2010
	 * 参数 : 
	 * 		jzjxjh ： 机组检修计划
	 * 返回值 : 
	 * 异常 :
	 */
	private InputStream getExcelInputStream(Jzjxjh jzjxjh, String title) {
		Date qsrq = jzjxjh.getQsrq();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date jsrq = jzjxjh.getJsrq();
		// 开始日期String型
		String strQsrq = sdf.format(qsrq);
		// 结束日期String型
		String strJsrq = sdf.format(jsrq);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		
		// 标题字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short)16);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontName("宋体");
		// 标题样式
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setBorderBottom((short)1);
		titleStyle.setBorderLeft((short)1);
		titleStyle.setBorderRight((short)1);
		titleStyle.setBorderTop((short)1);

		// 栏目头字体
		HSSFFont colTileFont = wb.createFont();
		colTileFont.setFontHeightInPoints((short)11);
		colTileFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		colTileFont.setFontName("宋体");
		// 栏目头样式
		HSSFCellStyle colTitleStyle = wb.createCellStyle();
		colTitleStyle.setFont(colTileFont);		
		colTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		colTitleStyle.setBorderBottom((short)1);
		colTitleStyle.setBorderLeft((short)1);
		colTitleStyle.setBorderRight((short)1);
		colTitleStyle.setBorderTop((short)1);
		
		// 专业字体
		HSSFFont zyFont = wb.createFont();
		zyFont.setFontHeightInPoints((short)11);
		zyFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		zyFont.setFontName("宋体");
		// 专业样式
		HSSFCellStyle zyStyle = wb.createCellStyle();
		zyStyle.setFont(zyFont);
		zyStyle.setBorderBottom((short)1);
		zyStyle.setBorderLeft((short)1);
		zyStyle.setBorderRight((short)1);
		zyStyle.setBorderTop((short)1);
		
		// 普通字体
		HSSFFont normalFont = wb.createFont();
		normalFont.setFontHeightInPoints((short)11);
		normalFont.setFontName("宋体");
		// 普通样式
		HSSFCellStyle normalStyle = wb.createCellStyle();
		normalStyle.setFont(normalFont);
		normalStyle.setBorderBottom((short)1);
		normalStyle.setBorderLeft((short)1);
		normalStyle.setBorderRight((short)1);
		normalStyle.setBorderTop((short)1);

		// 设置标题单元格
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints((float)27.0);
		// 合并标题单元格
		Region titleRegion = new Region(0,(short)0,0,(short)11);
		sheet.addMergedRegion(titleRegion);
		HSSFCell titleCell = titleRow.createCell(0);
//		titleCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		titleCell.setCellValue(title);
		titleCell.setCellStyle(titleStyle);

		// 检修时间
		HSSFRow jxsjRow = sheet.createRow(1);
		jxsjRow.setHeightInPoints((float)13.5);
		// 合并检修时间单元格
		Region jxsjRegion = new Region(1,(short)0,1,(short)11);
		sheet.addMergedRegion(jxsjRegion);
		HSSFCell jxsjCell = jxsjRow.createCell(0);
		String jxsj = "计划检修时间：" + strQsrq + "-" + strJsrq;
//		jxsjCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		jxsjCell.setCellValue(jxsj);
		jxsjCell.setCellStyle(normalStyle);
		setRegionStyle(sheet, jxsjRegion, normalStyle);

		
		// 栏目头
		HSSFRow colTitleRow = sheet.createRow(2);
		colTitleRow.setHeightInPoints((float)13.5);
		
		// 序号
		HSSFCell xhTitleCell = colTitleRow.createCell(0);
//		xhTitleCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		xhTitleCell.setCellValue("序号");
		xhTitleCell.setCellStyle(colTitleStyle);
		
		// 项目名称
		Region xmmcTitleRegion = new Region(2,(short)1,2,(short)9);
		sheet.addMergedRegion(xmmcTitleRegion);
		HSSFCell xmmcTitleCell = colTitleRow.createCell(1);
//		xmmcTitleCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		xmmcTitleCell.setCellValue("项目名称");
		xmmcTitleCell.setCellStyle(colTitleStyle);
		setRegionStyle(sheet, xmmcTitleRegion, colTitleStyle);
		
		// 备注
		Region bzTitleRegion = new Region(2,(short)10,2,(short)11);
		sheet.addMergedRegion(bzTitleRegion);
		HSSFCell bzTitleCell = colTitleRow.createCell(10);
//		bzTitleCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		bzTitleCell.setCellValue("备注");
		bzTitleCell.setCellStyle(colTitleStyle);
		setRegionStyle(sheet, bzTitleRegion, colTitleStyle);
	
		// 获得所有专业
		List<Dict> lstDict = (List<Dict>) dictManager.findDictByUniqueCatalogName("JXXM_SSZY");
		// 起始行
		int rowStart = 3;
		// 遍历所有专业
		for (Dict dict : lstDict) {
			// 专业名
			String value = dict.getValue();
			// 根据所属专业和计划获得所有项目
			List<Jzjxxm> lstJzjxxm = jzjxxmManager.findBySszyAndJh(value, jzjxjh);
			if (lstJzjxxm.size() > 0) {
				// 专业分类
				HSSFRow zyRow = sheet.createRow(rowStart);
				zyRow.setHeightInPoints((float)13.5);
				Region zyRegion = new Region(rowStart,(short)0,rowStart,(short)11);
				sheet.addMergedRegion(zyRegion);
				HSSFCell zyCell = zyRow.createCell(0);
//				zyCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				zyCell.setCellValue(value);
				zyCell.setCellStyle(zyStyle);
				setRegionStyle(sheet, zyRegion, zyStyle);
				
				int i = 0;
				// 遍历该专业下所有项目
				for (i = 0; i < lstJzjxxm.size(); i++) {
					Jzjxxm jzjxxm = lstJzjxxm.get(i);
					String xmmc = jzjxxm.getXmmc();
					String bz = jzjxxm.getRemark();
					int rowNow = rowStart + i + 1;
					// 栏目
					HSSFRow colRow = sheet.createRow(rowNow);
					colRow.setHeightInPoints((float)13.5);
					// 序号
					HSSFCell xhCell = colRow.createCell(0);
//					xhCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					xhCell.setCellValue(i + 1);
					xhCell.setCellStyle(normalStyle);
					// 项目名称
					Region xmmcRegion = new Region(rowNow,(short)1,rowNow,(short)9);
					sheet.addMergedRegion(xmmcRegion);
					HSSFCell xmmcCell = colRow.createCell(1);
//					xmmcCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					xmmcCell.setCellValue(xmmc);
					xmmcCell.setCellStyle(normalStyle);
					setRegionStyle(sheet, xmmcRegion, normalStyle);
					// 备注
					Region bzRegion = new Region(rowNow,(short)10,rowNow,(short)11);
					sheet.addMergedRegion(bzRegion);
					HSSFCell bzCell = colRow.createCell(10);
//					bzCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					bzCell.setCellValue(bz);
					bzCell.setCellStyle(normalStyle);
					setRegionStyle(sheet, bzRegion, normalStyle);
				}
				rowStart += i + 1;
			}
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

	private void setRegionStyle(HSSFSheet sheet, Region region , HSSFCellStyle cs) {
		int rowFrom = region.getRowFrom();
		int rowTo = region.getRowTo();
		int colFrom = region.getColumnFrom();
		int colTo = region.getColumnTo();
        for (int i = rowFrom; i <= rowTo; i ++) {
            HSSFRow row = sheet.getRow(i);
            for (int j = colFrom; j <= colTo; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                	cell = row.createCell(j);
                }
                cell.setCellStyle(cs);
            }
        }
	}

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String queryParamsSql = request.getParameter("queryParamsSql");
		if (StringUtil.isNotEmpty(queryParamsSql)) {
			HqlUtil.addWholeCondition(hql, queryParamsSql);
		}
		String jhId = request.getParameter("jhId");
		if (StringUtil.isNotEmpty(jhId)) {
			HqlUtil.addCondition(hql, "jzjxjh.id", jhId);
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return jzjxxmManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] {"id", "sszy", "xmmc", "jlr", "jlsj", "time", "remark"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] {"id", "sszy", "xmmc", "jlr", "jlsj", "time", "remark", "jzjxjh", "id"};
	}

	public JzjxxmManager getJzjxxmManager() {
		return jzjxxmManager;
	}

	public void setJzjxxmManager(JzjxxmManager jzjxxmManager) {
		this.jzjxxmManager = jzjxxmManager;
	}

	public JzjxjhManager getJzjxjhManager() {
		return jzjxjhManager;
	}

	public void setJzjxjhManager(JzjxjhManager jzjxjhManager) {
		this.jzjxjhManager = jzjxjhManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

}
