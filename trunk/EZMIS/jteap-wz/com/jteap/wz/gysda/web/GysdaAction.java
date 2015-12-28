/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gysda.web;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.gysda.manager.GysdaManager;
import com.jteap.wz.gysda.model.Gysda;

@SuppressWarnings({ "unchecked", "serial" })
public class GysdaAction extends AbstractAction {
	private GysdaManager gysdaManager;
	public GysdaManager getGysdaManager() {
		return gysdaManager;
	}

	public void setGysdaManager(GysdaManager gysdaManager) {
		this.gysdaManager = gysdaManager;
	}
	
	/**
	 * 统计供应商供应额
	 */
	public void getMoneySum() {
		String path=request.getSession().getServletContext().getRealPath("/jteap/wz/gysda/moneySum.xls");
		try{
			byte[] data=null;
			HSSFWorkbook  wb=null;
			File excel=new File(path);
			InputStream is=new FileInputStream(excel);
			wb=new HSSFWorkbook(is);
			HSSFSheet rs = wb.getSheetAt(0);
		   
            HSSFCellStyle cs = wb.createCellStyle();//创建一个style
            HSSFFont littleFont = wb.createFont();//创建一个Font
            littleFont.setFontName("SimSun");
            littleFont.setFontHeightInPoints((short)10);
            cs.setFont(littleFont);//设置字体
            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);//水平居中
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

			
			List<Gysda> list =  this.getManager().getAll();
			for(int i=0;i<list.size();i++){
				Gysda gysda = list.get(i);
				//开始导入的行
				HSSFRow mcrow = rs.createRow(i+2);
				//第一列 供应商名称
				HSSFCell gysmc = mcrow.createCell(0);
//				gysmc.setEncoding(HSSFCell.ENCODING_UTF_16);
				gysmc.setCellValue(gysda.getGysmc());
				gysmc.setCellStyle(cs);
				//第二列 地址
				HSSFCell dz = mcrow.createCell(1);
//				dz.setEncoding(HSSFCell.ENCODING_UTF_16);
				dz.setCellValue(gysda.getDz());
				dz.setCellStyle(cs);
				//第三列 电话
				HSSFCell dh = mcrow.createCell(2);
//				dh.setEncoding(HSSFCell.ENCODING_UTF_16);
				dh.setCellValue(gysda.getDh());
				dh.setCellStyle(cs);
				//第四列 联系人
				HSSFCell lxr = mcrow.createCell(3);
//				lxr.setEncoding(HSSFCell.ENCODING_UTF_16);
				lxr.setCellValue(gysda.getLxr());
				lxr.setCellStyle(cs);
				//第五列 供应金额
				HSSFCell gyje = mcrow.createCell(4);
				gyje.setCellValue("10000");
				gyje.setCellStyle(cs);
				//第六列 实际供应金额
				HSSFCell sjgyje = mcrow.createCell(5);
				sjgyje.setCellValue("10000");
				sjgyje.setCellStyle(cs);
			}
				
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			data = os.toByteArray();
			
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(("供应商供应额统计_" + DateUtils.getDate("yyyy-MM-dd") + ".xls")
							.getBytes(), "iso-8859-1"));
			
			//开始输出
			java.io.BufferedOutputStream outs = new java.io.BufferedOutputStream(response.getOutputStream());
			outs.write(data);
			outs.flush();
			outs.close();
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		//使用缓存可能新增的内容无法查询，这里禁用缓存
		this.gysdaManager.useQueryCache(false);
		try{
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				System.out.println(hqlWhereTemp);
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "gysmc", "asc");
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return gysdaManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"bm",
			"id",
			"gysmc",
			"dz",
			"dh",
			"czh",
			"yzbm",
			"lxr",
			"yxdz",
			"zywz",
			"frdb",
			"khyh",
			"zh",
			"swdjh",
			"qtxx",
			"sfxydw",
			"sfsndw",
			"zjm",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"bm",
			"id",
			"gysmc",
			"dz",
			"dh",
			"czh",
			"yzbm",
			"lxr",
			"yxdz",
			"zywz",
			"frdb",
			"khyh",
			"zh",
			"swdjh",
			"qtxx",
			"sfxydw",
			"sfsndw",
			"zjm",
		""};
	}
}
