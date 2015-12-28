/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.cgjhmx.web;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
import com.jteap.wz.cgjhgl.manager.CgjhglManager;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

@SuppressWarnings( { "unchecked", "serial" })
public class CgjhmxAction extends AbstractAction {
	private CgjhmxManager cgjhmxManager;
	private CgjhglManager cgjhglManager;
	private PersonManager personManager;
	private RoleManager roleManager;
	private XqjhDetailManager xqjhDetailManager;
	private XqjhsqManager xqjhsqManager;

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try {
			this.isUseQueryCache = false;
			String userLoginName = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
			String hqlWhere = request.getParameter("queryParamsSql");
			String sqbm = request.getParameter("sqbm");
			String czr = request.getParameter("czr");
			hql.append(" where 1 =1");
//			if(StringUtils.isNotEmpty(sqbm)){
//				HqlUtil.addCondition(hql, "xqjhDetail.xqjh.sqbm",sqbm);
//				List<Cgjhmx> cgjhmxList = new ArrayList<Cgjhmx>();
//				cgjhmxList = cgjhmxManager.getAll();
//				for (int i = 0; i < cgjhmxList.size(); i++) {
//					Cgjhmx cgjhmx = cgjhmxList.get(i);
//					if(StringUtils.isNotEmpty(cgjhmx.getXqjhDetail().getId())){
//						XqjhDetail xqjhDetail = cgjhmx.getXqjhDetail();
//						Xqjh xqjh = xqjhDetail.getXqjh();
//						if(xqjh != null){
//							Xqjhsq xqjhsq = xqjhsqManager.get(xqjh.getXqjhsqbh());
//							if(xqjhsq.getSqbmmc().equals(sqbm)){
//								cgjhmxId += "'"+cgjhmx.getId()+"',";
//							}
//						}
//					}
//				}
//				if(StringUtils.isNotEmpty(cgjhmxId)){
//					cgjhmxId = "(" + StringUtils.removeEnd(cgjhmxId, ",") + ")";
//				}
//			}
			if(StringUtil.isNotEmpty(czr)){
				HqlUtil.addCondition(hql, "dysczr",userLoginName);
			}
			// 采购计划明细表中的状态如果为1表示采购计划已经生效
			String zt = request.getParameter("zt");
			if ("1".equals(zt)) {
				HqlUtil.addCondition(hql, "zt", "1");
				HqlUtil.addCondition(hql,"zt", "3", HqlUtil.LOGIC_OR);
				HqlUtil.addCondition(hql, "cgsl", "dhsl", HqlUtil.LOGIC_AND,
						HqlUtil.TYPE_NUMBER, HqlUtil.COMPARECHAR_GREAT);
			} else if ("0".equals(zt)) {
				String loginName = (String) this.sessionAttrs
						.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
				HqlUtil.addCondition(hql, "cgy", loginName);
				HqlUtil.addCondition(hql, "zt", "0");
			} else {
//				String loginName = (String) this.sessionAttrs
//				.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
//				HqlUtil.addCondition(hql, "cgy", loginName);
//				HqlUtil.addCondition(hql, "zt", "0");
			}
			
			if(StringUtils.isNotEmpty(sqbm)){
				hql.append("  and obj.id in (select t.id"+
								 " from Cgjhmx t,XqjhDetail  mx,Xqjh jh "+
								" where  mx.id = t.xqjhmx  "+
								" and jh.id = mx.xqjh.id "+
								" and jh.sqbm = '"+sqbm+"' )"); 
			}
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "cgjhgl.bh", "desc");
		}
		//System.out.println(hql.toString());
	}

	/**
	 * 物资汇总
	 */
	public void sumWzAction() {
		String ids = request.getParameter("ids");
		String path = request.getSession().getServletContext().getRealPath(
				"/jteap/wz/cgjhgl/wzSum_new.xls");
		java.io.BufferedOutputStream outs = null;
		try {
			if (StringUtil.isNotEmpty(ids)) {

				List<Cgjhmx> cgjhmxs = this.cgjhmxManager.findByIds(ids
						.split(","));
				// 将查询出来的明细集合按照采购员来分组
				Map map_cgmx = this.list2Map(cgjhmxs);
				Iterator<String> keys = map_cgmx.keySet().iterator();

				HSSFWorkbook wb = null;
				File excel = new File(path);
				InputStream is = new FileInputStream(excel);
				wb = new HSSFWorkbook(is);
				// 设置列宽
				HSSFSheet rs = wb.getSheetAt(0);
				rs.setColumnWidth(0, 512 * 10);
				rs.setColumnWidth(1, 512 * 5);
				rs.setColumnWidth(2, 512 * 5);
				rs.setColumnWidth(3, 512 * 5);
				rs.setColumnWidth(4, 512 * 5);
				int currentRowIndex = 0;

				// 创建标题样式
				HSSFFont litle_font = wb.createFont();// 创建一个Font
				litle_font.setFontName("SimSun");
				litle_font.setFontHeightInPoints((short) 16);
				litle_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				HSSFCellStyle title_cs = wb.createCellStyle();// 创建一个style
				title_cs.setFont(litle_font);
				title_cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
				title_cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中

				// 创建数据样式
				HSSFCellStyle data_cs = wb.createCellStyle();// 创建一个style
				HSSFFont data_font = wb.createFont();// 创建一个Font
				data_font.setFontName("SimSun");
				data_font.setFontHeightInPoints((short) 10);
				data_cs.setFont(data_font);// 设置字体
				data_cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
				data_cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居中
				data_cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
				data_cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
				data_cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
				data_cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框

				// 创建数字数据样式
				HSSFCellStyle number_cs = wb.createCellStyle();// 创建一个style
				HSSFFont number_font = wb.createFont();// 创建一个Font
				HSSFDataFormat df = wb.createDataFormat();

				number_font.setFontName("SimSun");
				number_font.setFontHeightInPoints((short) 10);
				number_cs.setFont(data_font);// 设置字体
				number_cs.setDataFormat(df.getFormat("#,##0.00"));
				number_cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
				number_cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平居中
				number_cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
				number_cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
				number_cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
				number_cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框

				while (keys.hasNext()) {
					String cgy = (String) keys.next();

					rs.addMergedRegion(new CellRangeAddress(currentRowIndex,
							currentRowIndex, 0, 4));

					HSSFRow title_row = rs.createRow(currentRowIndex);
					title_row.setHeight((short) (512 * 2));
					HSSFCell title_cell = title_row.createCell(0);
					title_cell.setCellStyle(title_cs);
					title_cell.setCellValue("采购物资汇总");
					currentRowIndex++;

					HSSFRow cgyRow = rs.createRow(currentRowIndex);
					HSSFCell cgyCell = cgyRow.createCell(3);
					cgyCell.setCellValue("采购员：");
					HSSFCell cgyCellValue = cgyRow.createCell(4);
					cgyCellValue.setCellValue(personManager.findUniqueBy(
							"userLoginName", cgy).getUserName());
					currentRowIndex++;

					// 开始导入的行
					HSSFRow headRow = rs.createRow(currentRowIndex);

					HSSFCell head_wzmc = headRow.createCell(0);
					head_wzmc.setCellValue("物资名称");
					head_wzmc.setCellStyle(data_cs);

					HSSFCell head_jhdj = headRow.createCell(1);
					head_jhdj.setCellValue("计划单价");
					head_jhdj.setCellStyle(data_cs);

					HSSFCell head_jldw = headRow.createCell(2);
					head_jldw.setCellValue("计量单位");
					head_jldw.setCellStyle(data_cs);

					HSSFCell head_cgsl = headRow.createCell(3);
					head_cgsl.setCellValue("采购数量");
					head_cgsl.setCellStyle(data_cs);

					HSSFCell head_cgje = headRow.createCell(4);
					head_cgje.setCellValue("采购金额");
					head_cgje.setCellStyle(data_cs);
					currentRowIndex++;

					List<Cgjhmx> mxs = (List) map_cgmx.get(cgy);
					double totalMoney = 0d;
					for (Cgjhmx obj : mxs) {
						// 开始导入的行
						HSSFRow mcrow = rs.createRow(currentRowIndex);
						// 第一列 物资名称
						HSSFCell wzmc = mcrow.createCell(0);
						// wzmc.setEncoding(HSSFCell.ENCODING_UTF_16);
						wzmc.setCellValue(obj.getWzdagl().getWzmc());
						wzmc.setCellStyle(data_cs);
						// 第二列 计划单价
						HSSFCell jhdj = mcrow.createCell(1);
						// jhdj.setEncoding(HSSFCell.ENCODING_UTF_16);
						jhdj.setCellValue(obj.getWzdagl().getJhdj());
						jhdj.setCellStyle(number_cs);
						// 第三列 计量单位
						HSSFCell jldw = mcrow.createCell(2);
						// jldw.setEncoding(HSSFCell.ENCODING_UTF_16);
						jldw.setCellValue(obj.getWzdagl().getJldw());
						jldw.setCellStyle(data_cs);
						// 第四列 采购数量
						HSSFCell cgsl = mcrow.createCell(3);
						// cgsl.setEncoding(HSSFCell.ENCODING_UTF_16);
						cgsl.setCellValue(obj.getCgsl());
						cgsl.setCellStyle(number_cs);
						// 第五列 采购金额
						HSSFCell cgje = mcrow.createCell(4);
						// cgje.setEncoding(HSSFCell.ENCODING_UTF_16);
						cgje.setCellValue(obj.getJe());
						cgje.setCellStyle(number_cs);
						totalMoney += Double.valueOf(obj.getJe());
						currentRowIndex++;
					}
					// currentRowIndex++;
					HSSFRow mcrow = rs.createRow(currentRowIndex);
					HSSFCell label = mcrow.createCell(3);
					// label.setEncoding(HSSFCell.ENCODING_UTF_16);
					label.setCellValue("总采购金额:");
					label.setCellStyle(data_cs);
					HSSFCell sum = mcrow.createCell(4);
					// sum.setEncoding(HSSFCell.ENCODING_UTF_16);
					sum.setCellValue(totalMoney);
					sum.setCellStyle(number_cs);
					currentRowIndex += 3;
				}
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				wb.write(os);
				byte[] data = os.toByteArray();

				response.reset();
				response
						.setContentType("application/vnd.ms-excel;charset=utf-8");
				response
						.setHeader(
								"Content-Disposition",
								"attachment;filename="
										+ new String(
												("采购物资汇总_"
														+ DateUtils
																.getDate("yyyy-MM-dd") + ".xls")
														.getBytes(),
												"iso-8859-1"));

				// 开始输出
				outs = new java.io.BufferedOutputStream(response
						.getOutputStream());
				outs.write(data);
				outs.flush();
				outs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将查询出来的采购明细集合换转成以采购员来分组的Map集合
	 * 
	 * @param cgjhmxs
	 * @return
	 */
	public Map list2Map(List<Cgjhmx> cgjhmxs) {
		Map<String, List<Cgjhmx>> result = new HashMap<String, List<Cgjhmx>>();
		if (cgjhmxs != null) {
			for (Cgjhmx obj : cgjhmxs) {
				if (result.containsKey(obj.getCgy())) {
					List newmxs = result.get(obj.getCgy());
					newmxs.add(obj);
				} else {
					List<Cgjhmx> newmxs = new ArrayList<Cgjhmx>();
					newmxs.add(obj);
					result.put(obj.getCgy(), newmxs);
				}
			}
		}
		return result;
	}

	/**
	 * 采购物资汇总
	 */
	/*
	 * @SuppressWarnings("deprecation") public void sumWz(){ //String
	 * path=request.getSession().getServletContext().getRealPath("/jteap/wz/cgjhgl/wzSum.xls");
	 * String
	 * path=request.getSession().getServletContext().getRealPath("/jteap/wz/cgjhgl/wzSum_new.xls");
	 * String startDt = request.getParameter("startDt"); String endDt =
	 * request.getParameter("endDt"); double totalMoney = 0d; int rowIndex=3;
	 * //采购总金额行号 java.io.BufferedOutputStream outs = null; try{ byte[]
	 * data=null; HSSFWorkbook wb=null; File excel=new File(path); InputStream
	 * is=new FileInputStream(excel); wb=new HSSFWorkbook(is); HSSFSheet rs =
	 * wb.getSheetAt(0); HSSFCellStyle cs = wb.createCellStyle();//创建一个style
	 * HSSFFont littleFont = wb.createFont();//创建一个Font
	 * littleFont.setFontName("SimSun");
	 * littleFont.setFontHeightInPoints((short)10);
	 * cs.setFont(littleFont);//设置字体
	 * cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
	 * cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);//水平居中
	 * cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
	 * cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	 * cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	 * cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	 * 
	 * if(StringUtil.isEmpty(startDt) || StringUtil.isEmpty(endDt)) return;
	 * String hql = "FROM Cgjhmx obj where to_char(obj.cgjhgl.sxsj,'yyyy-mm-dd')
	 * between '"+startDt+"' and '"+endDt+"' order by obj.cgy desc"; List<Cgjhmx>
	 * list = this.getManager().createQuery(hql).list(); HSSFFont f =
	 * wb.createFont();//创建一个Font f.setFontName("SimSun");
	 * f.setFontHeightInPoints((short)16);
	 * f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); HSSFCellStyle cs1 =
	 * wb.createCellStyle();//创建一个style cs1.setFont(f);
	 * cs1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
	 * cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
	 * rs.addMergedRegion(new CellRangeAddress(0,0,0,4)); rs.setColumnWidth(0,
	 * 512*10); HSSFRow r = rs.getRow(0); r.setHeight((short)(512*2)); HSSFCell
	 * c = r.getCell(0); c.setCellStyle(cs1); c.setCellValue("采购物资汇总");
	 * //rs.getRow(0).setHeight((short)1000);
	 * 
	 * for(int i=0;i<list.size();i++){ rowIndex++; Cgjhmx cgjhmx = list.get(i);
	 * //开始导入的行 HSSFRow mcrow = rs.createRow(i+3); //第一列 物资名称 HSSFCell wzmc =
	 * mcrow.createCell((short)0); //
	 * wzmc.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * wzmc.setCellValue(cgjhmx.getWzdagl().getWzmc()); wzmc.setCellStyle(cs);
	 * //第二列 计划单位 HSSFCell jhdj = mcrow.createCell((short)1); //
	 * jhdj.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * jhdj.setCellValue(cgjhmx.getJhdj()); jhdj.setCellStyle(cs); //第三列 计量单位
	 * HSSFCell jldw = mcrow.createCell((short)2); //
	 * jldw.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * jldw.setCellValue(cgjhmx.getWzdagl().getJldw()); jldw.setCellStyle(cs);
	 * //第四列 采购数量 HSSFCell cgsl = mcrow.createCell((short)3); //
	 * cgsl.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * cgsl.setCellValue(cgjhmx.getCgsl()); cgsl.setCellStyle(cs); //第五列 采购金额
	 * HSSFCell cgje = mcrow.createCell((short)4); //
	 * cgje.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * cgje.setCellValue(cgjhmx.getJe()); cgje.setCellStyle(cs); totalMoney +=
	 * Double.valueOf(cgjhmx.getJe()); } HSSFRow mcrow = rs.createRow(rowIndex);
	 * HSSFCell label = mcrow.createCell((short)3); //
	 * label.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * label.setCellValue("总采购金额:"); label.setCellStyle(cs); HSSFCell sum =
	 * mcrow.createCell((short)4); // sum.setEncoding(HSSFCell.ENCODING_UTF_16);
	 * sum.setCellValue(totalMoney); sum.setCellStyle(cs);
	 * 
	 * ByteArrayOutputStream os = new ByteArrayOutputStream(); wb.write(os);
	 * data = os.toByteArray();
	 * 
	 * response.reset();
	 * response.setContentType("application/vnd.ms-excel;charset=utf-8");
	 * response.setHeader("Content-Disposition", "attachment;filename=" + new
	 * String(("采购物资汇总_" + DateUtils.getDate("yyyy-MM-dd") + ".xls")
	 * .getBytes(), "iso-8859-1"));
	 * 
	 * //开始输出 outs = new
	 * java.io.BufferedOutputStream(response.getOutputStream());
	 * outs.write(data); outs.flush(); outs.close(); }catch(Exception e){
	 * e.printStackTrace(); }finally{ if(outs!=null){ try { outs.close(); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } } }
	 */

	/**
	 * 得到相关角色人员
	 */
	public void showPersons() {
		String type = request.getParameter("type");
		String userLoginName = request.getParameter("userLoginName");
		String[] roles = { "计划员", "采购员", "仓库保管员" };
		Collection<Person> list = null;
		String json = "";
		if (StringUtil.isNotEmpty(type)) {
			int idx = Integer.parseInt(type);
			Role r = roleManager.findUniqueBy("rolename", roles[idx]);
			list = personManager.findPersonByRoleIds((String) r.getId());
		} else if (StringUtil.isNotEmpty(userLoginName)) {
			list = personManager.findBy("userLoginName", userLoginName);
		} else {
			list = personManager.getAll();
		}
		// 将集合JSON化
		json = JSONUtil.listToJson(list, new String[] { "userName", "id",
				"userLoginName" });
		try {
			outputJson("{list:" + json + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return cgjhmxManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "cgjhgl", "bh", "jhy", "sxsj", "zdsj",
				"bz", "cgjhbh", "wzbm", "xh", "jhdj", "cgjldw", "cgsl", "hsxs",
				"jhdhrq", "dhsl", "cgfx", "cgy", "zt", "je", "time", "wzdagl",
				"dqkc", "wzmc", "xhgg", "kw", "cwmc", "ck", "ckmc", "person",
				"id", "userName", "userLoginName", "ckgl", "ckbm", "ckmc",
				"xqjhDetail", "xqjhsq", "xqjhsqbh", "czyxm", "xqjh", "gclb","dyszt", "gcxm", "sqbm", "xqjhsqDetail", "remark", "sqbmmc", "" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "cgjhgl", "cgjhbh", "wzbm", "xh", "jhdj",
				"cgjldw", "cgsl", "hsxs", "jhdhrq", "dhsl", "cgfx", "cgy",
				"zt", "time" };
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
		sheet.setColumnWidth(0, 1300);
		sheet.setColumnWidth(1, 2300);
		sheet.setColumnWidth(2, 2300);
		sheet.setColumnWidth(3, 8500);
		sheet.setColumnWidth(4, 1300);
		sheet.setColumnWidth(5, 2000);
		sheet.setColumnWidth(6, 1600);
		sheet.setColumnWidth(7, 2600);
		sheet.setColumnWidth(8, 2000);
		sheet.setColumnWidth(9, 1600);
		sheet.setColumnWidth(10, 3000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 3000);
		
		HSSFPrintSetup hps = sheet.getPrintSetup();  
		hps.setLandscape(true);           //横向打印
		hps.setVResolution((short) 300);  //打印状态
		hps.setPageStart((short)0);       //起始页码
		hps.setHeaderMargin((double)0.1); //页眉
		hps.setFooterMargin((double)0.6);  //页脚
		
		sheet.setMargin(HSSFSheet.LeftMargin,(short)0.1); //左页边距
		sheet.setMargin(HSSFSheet.RightMargin,(short)0.8); //右页边距
//		sheet.setMargin(HSSFSheet.TopMargin,(short)0.8); //上边距
//		sheet.setMargin(HSSFSheet.BottomMargin, (short)0.6); //下边距
		
		sheet.setHorizontallyCenter(true);  //水平居中
		
		
		// 创建字体样式
//		HSSFFont font = workbook.createFont();
//		font.setFontName("Verdana");
//		font.setBoldweight((short) 100);
//		font.setFontHeight((short) 300);
//		font.setColor(HSSFColor.BLUE.index);

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
		//style2.setWrapText(true);// 自动换行
		
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

//		style.setFont(font);// 设置字体

		// 创建标题行
//		HSSFRow titleRow = null;
//		titleRow = sheet.createRow(0);
//		titleRow.setHeight((short) 300);// 设定行的高度
//		HSSFCell titleCell = null;
//		titleCell = titleRow.createCell(0);
//		titleCell.setCellValue("测试PIO");
//		titleCell.setCellStyle(style);// 设置单元格样式
		// 合并单元格(startRow，endRow，startColumn，endColumn)
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

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

		HSSFRow row = null;
		HSSFCell cell = null;
		int count = 0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < idsArray.length; i++) {
				Cgjhmx cgjhmx = cgjhmxManager.get(idsArray[i]);
					if(i == 0){
						// 创建行
						row = sheet.createRow(count);
						
						// 创建单元格,设置每个单元格的值（作为表头）
						// 创建单元格,设置每个单元格的值（作为表头）
						cell = row.createCell(0);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("序号");
						
						cell = row.createCell(1);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("采购编号");
						
						cell = row.createCell(2);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("申请编号");
						
						cell = row.createCell(3);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("物资名称规格");
						
						cell = row.createCell(4);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("单位");
						
						cell = row.createCell(5);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("采购数量");
						
					//	cell = row.createCell(6);
					//	cell.setCellStyle(titleStyle);
					//	cell.setCellValue("计划员");
						
						cell = row.createCell(6);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("采购员");
						
						cell = row.createCell(7);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("生效时间");
						
						cell = row.createCell(8);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("申请部门");
						
						cell = row.createCell(9);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("申请人");
						
						cell = row.createCell(10);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("工程项目");
						
						cell = row.createCell(11);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("工程类别");
						
						cell = row.createCell(12);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("备注");
						count++;
					}
					//插入数据
					row = sheet.createRow(count);
					row.setHeight((short) 300);
					cell = row.createCell(0);
					cell.setCellStyle(style2);
					cell.setCellValue(count);
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					cell.setCellValue(cgjhmx.getCgjhgl().getBh());
					cell = row.createCell(2);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						cell.setCellValue(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getXqjhsqbh());
					}else{
						cell.setCellValue("");
					}
					cell = row.createCell(3);
					cell.setCellStyle(style2);
					if(cgjhmx.getWzdagl().getXhgg() == null){
						cell.setCellValue(cgjhmx.getWzdagl().getWzmc());
					}else{
						cell.setCellValue(cgjhmx.getWzdagl().getWzmc()+"("+cgjhmx.getWzdagl().getXhgg()+")");
					}
					
					cell = row.createCell(4);
					cell.setCellStyle(style2);
					cell.setCellValue(cgjhmx.getWzdagl().getJldw());
					
					cell = row.createCell(5);
					cell.setCellStyle(style2);
					cell.setCellValue(cgjhmx.getCgsl());
					
					//cell = row.createCell(6);
					//cell.setCellStyle(style2);
					//cell.setCellValue(cgjhmx.getCgjhgl().getPerson().getUserName());//计划员
					
					cell = row.createCell(6);
					cell.setCellStyle(style2);
					cell.setCellValue(cgjhmx.getPerson().getUserName());//采购员
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					cell = row.createCell(7);
					cell.setCellStyle(style2);
					if(cgjhmx.getCgjhgl().getSxsj() != null){
						cell.setCellValue(format.format(cgjhmx.getCgjhgl().getSxsj()));
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(8);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						cell.setCellValue(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getSqbmmc());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(9);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						cell.setCellValue(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getCzyxm());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(10);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						cell.setCellValue(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getGcxm());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(11);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						cell.setCellValue(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getGclb());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(12);
					cell.setCellStyle(style2);
					if(cgjhmx.getXqjhDetail().getXqjh().getXqjhsq() != null){
						Set<XqjhsqDetail> xqjhsqDetailSet = new HashSet<XqjhsqDetail>();
						xqjhsqDetailSet = cgjhmx.getXqjhDetail().getXqjh().getXqjhsq().getXqjhsqDetail();
						@SuppressWarnings("unused")
						Iterator<XqjhsqDetail> it = xqjhsqDetailSet.iterator();
						for (Iterator iterator = xqjhsqDetailSet.iterator(); iterator
						.hasNext();) {
							XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) iterator
							.next();
							if(cgjhmx.getWzdagl().getXhgg() != null && xqjhsqDetail.getXhgg() != null){
								if(cgjhmx.getWzdagl().getWzmc().equals(xqjhsqDetail.getWzmc()) && cgjhmx.getWzdagl().getXhgg().equals(xqjhsqDetail.getXhgg())){
									cell.setCellValue(xqjhsqDetail.getRemark());
								}
							}
						}
					}else{
						cell.setCellValue("");
					}
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
	
	/**
	 * 删除操作，删除采购计划及明细
	 */
	@Override
	public String removeAction() throws Exception {
		String loginName = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME);
		String keys = request.getParameter("ids");
		Cgjhgl cgjhgl = null;
		Cgjhmx cgjhmx = null;
		int count = 0;
 		try {
			if (StringUtils.isNotEmpty(keys)) {
				//删除明细表
				String cgjhmxId[]=keys.split(","); 
				for(int i=0;i<cgjhmxId.length;i++){
					cgjhmx = cgjhmxManager.get((Serializable)cgjhmxId[i]);
					//'4'表示已删除采购计划明细
					cgjhmx.setZt("4");
					cgjhmx.setBz(loginName+"删除此单据");
//					if(cgjhmx != null){
						cgjhmxManager.save(cgjhmx);
//					}
				}
				cgjhgl = cgjhmxManager.get(keys.split(",")[0]).getCgjhgl();
				Set<Cgjhmx> set = new HashSet<Cgjhmx>();
				set = cgjhgl.getCgjhmxs();
				@SuppressWarnings("unused")
				Iterator<Cgjhmx> iter = set.iterator();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					Cgjhmx cgjhmx2 = (Cgjhmx) iterator.next();
					if(("4").equals(cgjhmx2.getZt())){
						count++;
					}
				}
				//表明删除的明细数量等于采购计划明细数量，此时删除主单
				if(count == set.size()){
					//'3'表示已删除采购计划
					cgjhgl.setZt("3");
					cgjhglManager.save(cgjhgl);
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	public CgjhglManager getCgjhglManager() {
		return cgjhglManager;
	}

	public void setCgjhglManager(CgjhglManager cgjhglManager) {
		this.cgjhglManager = cgjhglManager;
	}
}
