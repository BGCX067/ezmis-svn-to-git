/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.yhdmx.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings({ "unchecked", "serial" })
public class YhdmxAction extends AbstractAction {
	private YhdmxManager yhdmxManager;
	private YhdglManager yhdglManager;
	private CgjhmxManager cgjhmxManager;
	private XqjhDetailManager xqjhDetailManager;

	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}

	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
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
			String zt = request.getParameter("zt");
			String hqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(zt)) {
				HqlUtil.addCondition(hql, "zt", zt);
			}
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "yhdgl.bh", "desc");
		}
		System.out.println(hql.toString());
	}

	/**
	 * 返回随即盘点物资
	 * 
	 * @return
	 */
	public String getPdWzAction() {
		try {
			String parWhere = request.getParameter("parWhere");
			String sl = request.getParameter("sl");
			String ddj = request.getParameter("ddj");
			String xdj = request.getParameter("xdj");
			StringBuffer hql = new StringBuffer("from Yhdmx t ");
			// 随即次数
			int r_sl = 0;
			// 物资单价
			double d_dj = 0;
			// 物资单价
			double x_dj = 0;
			if (StringUtils.isNotEmpty(sl)) {
				r_sl = Integer.valueOf(sl);
			}
			if (StringUtils.isNotEmpty(ddj)) {
				d_dj = Double.valueOf(ddj);
			}
			if (StringUtils.isNotEmpty(xdj)) {
				x_dj = Double.valueOf(xdj);
			}
			if (StringUtils.isNotEmpty(parWhere)) {
				HqlUtil.addWholeCondition(hql, parWhere);
			} else {
				hql.append("where 1=1");
			}
			hql.append(" and t.zt = '1' and t.sysl>0 order by t.wzdagl.dqkc desc");
			List<Map> list = yhdmxManager.getPdWz(hql.toString(), d_dj, x_dj,
					r_sl);
			String[] jsonPro = new String[] { "wzid", "wzmc", "xhgg", "kw",
					"pjj", "dqkc", "dqje", "zksj" };
			String json = JSONUtil.listToJson(list, jsonPro);
			json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("显示列表异常", e);
		}
		return NONE;
	}

	/**
	 * 删除验货单明细(返回到验货入库阶段)
	 * 
	 * @throws Exception
	 * 
	 */
	public String removeAction() throws Exception {
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			for (String id : ids.split(",")) {
				Yhdmx yhdmx = yhdmxManager.get(id);
				// 获得采购计划明细
				Cgjhmx cgjhmx = yhdmx.getCgjhmx();

				cgjhmx.setDhsl(cgjhmx.getCgsl() - yhdmx.getYssl());
				if (cgjhmx.getDhsl() <= 0) {
					// 设置采购计划明细状态
					cgjhmx.setZt("1");
				} else {
					// 设置采购计划明细状态
					cgjhmx.setZt("3");
				}
				// 如果采购计划主单 全部验货完 则改回状态
				if ("2".equals(cgjhmx.getCgjhgl().getZt())) {
					// 设置主单状态
					cgjhmx.getCgjhgl().setZt("1");
				}
				//
				// yhdmx.setZt("5");
				// yhdmxManager.save(yhdmx);
				Yhdgl yhdgl = yhdmx.getYhdgl();
				yhdgl.getYhdmxs().remove(yhdmx);
				// 是否入库标记
				boolean is_rk = true;
				// 循环所有明细
				for (Yhdmx yhdmxs : yhdgl.getYhdmxs()) {
					// 如果有没入库的 则修改状态 跳出循环
					if (!"1".equals(yhdmxs.getZt())) {
						is_rk = false;
						break;
					}
				}
				// 如果入库状态为true 则是入库完毕 则修改主单状态为入库
				if (is_rk) {
					yhdgl.setZt("1");
				}
				yhdglManager.save(yhdgl);
				yhdglManager.flush();
				// 如果该验货单明细的主验货单下没有明细了 则删除该主单
				if (yhdmx.getYhdgl().getYhdmxs().size() < 1) {
					yhdglManager.remove(yhdmx.getYhdgl());
					// yhdmx.getYhdgl().setZt("5");
					// yhdglManager.save(yhdmx.getYhdgl());
				} else {
					int i = 1;
					for (Yhdmx mx : yhdmx.getYhdgl().getYhdmxs()) {
						mx.setXh(i + "");
						i++;
						yhdmxManager.save(mx);
					}
				}
			}
		}
		// if(ids.split(",").length ==
		// yhdmxManager.get(ids.split(",")[0]).getYhdgl().getYhdmxs().size()){
		// Yhdgl yhdgl = yhdmxManager.get(ids.split(",")[0]).getYhdgl();
		// yhdgl.setZt("5");
		// yhdglManager.save(yhdgl);
		// }
		this.outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 对新增验货单的 明细到货数量进行过滤
	 * 
	 * @return
	 * @throws Exception
	 */
	public String beforeAddYhdAction() throws Exception {
		String cgjhmxid = request.getParameter("cgmxid");
		String dhsl = request.getParameter("dhsl");
		try {
			// 获取采购计划明细
			Cgjhmx cgjhmx = cgjhmxManager.findUniqueBy("id", cgjhmxid);
			// 如果为空 则是自由入库
			if (cgjhmx == null) {
				this.outputJson("{success:true}");
			} else {
				if (cgjhmx.getCgsl() - cgjhmx.getDhsl() < Double.valueOf(dhsl)) {
					double coum = cgjhmx.getCgsl() - cgjhmx.getDhsl();
					this.outputJson("{success:false,coum:" + coum + "}");
				} else {
					this.outputJson("{success:true}");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}

	/**
	 * 验货单修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editorYhdAction() throws Exception {
		String cgjhmxId = request.getParameter("cgmxid");
		String yssl = request.getParameter("yssl");
		String yhdmxid = request.getParameter("yhdmxid");
		try {
			// 获取验货单明细对象
			Yhdmx yhdmx = yhdmxManager.findUniqueBy("id", yhdmxid);
			// 获取修改后的 验收数量相差数字 如果修改后的数量大于则为正数，如果小于则为负数
			double sl = Double.valueOf(yssl) - yhdmx.getYssl();
			// 获取采购计划明细
			Cgjhmx cgjhmx = cgjhmxManager.findUniqueBy("id", cgjhmxId);
			// 如果采购计划明细中可验收数量小于差额 则 可验收数量不足
			if (cgjhmx.getCgsl() - cgjhmx.getDhsl() < sl) {
				if (cgjhmx.getCgjhgl().getBz().equals("自由入库")) {
					this.outputJson("{success:true");
				} else {
					double coum = yhdmx.getYssl() + cgjhmx.getCgsl()
							- cgjhmx.getDhsl();
					this.outputJson("{success:false,coum:" + coum + "}");
				}
			} else {
				// 大于的话 则可以继续验收
				// cgjhmx.setDhsl(cgjhmx.getDhsl()+sl);
				// cgjhmxManager.save(cgjhmx);
				this.outputJson("{success:true}");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}

	/**
	 * 新增一个验货单明细 待定方法
	 * 
	 * @return
	 */
	public String addYhdmx() {
		// String yhdid = request.getParameter("docid");
		// String record = request.getParameter("record");
		return NONE;
	}

	/**
	 * 删除操作，删除验货单及其明细
	 */
	public String removesAction() throws Exception {
		Connection conn = null;
		Statement st = null;
		String keys = request.getParameter("ids");
		Yhdgl yhd = null;
		try {
			DataSource dataSource = (DataSource) SpringContextUtil
					.getBean("dataSource");
			conn = dataSource.getConnection();
			st = conn.createStatement();
			String cgjhId = "";
			if (keys != null) {
				// 删除明细表
				String yhdKeys[] = keys.split(",");
				for (int i = 0; i < yhdKeys.length; i++) {
					Yhdmx yhdmx = yhdmxManager.findUniqueBy("id", yhdKeys[i]);
					// 自由入库 删除虚拟的采购计划
					if (StringUtils.isEmpty(yhdmx.getCgjhmx().getXqjhDetail()
							.getXqjh().getGcxm())) {
						// System.out.println("自由入库");
						// Cgjhgl cgjh = null;
						cgjhId = yhdmx.getCgjhmx().getCgjhgl().getId();
						st.execute("delete tb_wz_ycgjhmx t where t.id='"
								+ yhdmx.getCgjhmx().getId() + "'");

					} else {
						// 重新设置采购计划明细里的到货数量
						Cgjhmx cgjhmx = yhdmx.getCgjhmx();
						cgjhmx.setDhsl(cgjhmx.getDhsl() - yhdmx.getDhsl());
						cgjhmxManager.save(cgjhmx);
						// 重新设置需求计划明细里的到货数量
						XqjhDetail detail = yhdmx.getCgjhmx().getXqjhDetail();
						detail.setDhsl(detail.getDhsl() - yhdmx.getDhsl());
						xqjhDetailManager.save(detail);
					}
					yhd = yhdmx.getYhdgl();
					st.execute("delete tb_wz_yyhdmx t where t.id='"
							+ yhdmx.getId() + "'");
					// System.out.println("删除完毕");
				}
			}
			boolean falg = false;
			// 查询该验货单下是否存在自由物资
			for (Yhdmx yhdmx : yhd.getYhdmxs()) {
				if (StringUtils.isEmpty(yhdmx.getCgjhmx().getXqjhDetail()
						.getXqjh().getGcxm())) {
					falg = true;
					break;
				}
			}
			// 如果没有则改变状态
			if (falg == false) {
				// System.out.println("改变状态");
				// 自由入库虚拟的采购明细删除完毕。。
				if (cgjhId != "") {
					// System.out.println("删除采购计划主单");
					st.execute("delete tb_wz_ycgjh t where t.id='" + cgjhId
							+ "'");
				}
				yhd.setFlag("1");
				yhdglManager.save(yhd);
			}
			outputJson("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		} finally {
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}

	/**
	 * 物资盘底打印 重写导出excel方法 lvchao
	 */
	public String exportExcel() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = "物资名称,型号规格,库位,单价,当前库存,库存金额,在库时间";
		// 表索引信息（逗号表达式）
		String paraDataIndex = "wzmc,xhgg,kw,pjj,dqkc,dqje,zksj";
		// 宽度(逗号表达式)O
		String paraWidth = "130,130,100,80,80,100,80";
		// 选中物资导出
		String id = request.getParameter("id");
		// 随即导出
		String parWhere = request.getParameter("parWhere");
		String sl = request.getParameter("sl");
		String ddj = request.getParameter("ddj");
		String xdj = request.getParameter("xdj");
		StringBuffer hql = new StringBuffer("from Yhdmx t ");
		// 随即次数
		int r_sl = 0;
		// 物资单价
		double d_dj = 0;
		// 物资单价
		double x_dj = 0;
		if (StringUtils.isNotEmpty(sl)) {
			r_sl = Integer.valueOf(sl);
		}
		if (StringUtils.isNotEmpty(ddj)) {
			d_dj = Double.valueOf(ddj);
		}
		if (StringUtils.isNotEmpty(xdj)) {
			x_dj = Double.valueOf(xdj);
		}
		if (StringUtils.isNotEmpty(parWhere)) {
			HqlUtil.addWholeCondition(hql, parWhere);
		} else {
			hql.append("where 1=1");
		}
		hql.append(" and t.zt = '1' and t.sysl>0 order by t.wzdagl.dqkc desc");
		List<Map> list = new ArrayList<Map>();
		// 如果没有选中ID 则是随即导出
		if (StringUtils.isEmpty(id)) {
			list = yhdmxManager.getPdWz(hql.toString(), d_dj, x_dj, r_sl);
		} else {// 否则是 选择导出
			list = yhdmxManager.getPdWzByIds(hql.toString(), id);
		}
		if (list.size() < 1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		} else {
			// 调用导出方法
			export(list, paraHeader, paraDataIndex, paraWidth);
		}

		return NONE;
	}

	/**
	 * 验货单打印
	 */
	public InputStream exportYhdmxExcel() throws Exception {
		// 选定打印
		String id = request.getParameter("id");
		// 条件打印
		String parWhere = request.getParameter("parWhere");
		// String idsArray[] = id.split(",");
		HSSFWorkbook workbook = new HSSFWorkbook();

		// HSSFCellStyle style = workbook.createCellStyle();

		// style.setFillBackgroundColor(HSSFColor.BLUE.index);

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		sheet.setColumnWidth(0, 600);
		sheet.setColumnWidth(1, 2700);
		sheet.setColumnWidth(2, 2700);
		sheet.setColumnWidth(3, 1800);
		sheet.setColumnWidth(4, 1400);
		sheet.setColumnWidth(5, 2300);
		sheet.setColumnWidth(6, 2800);
		sheet.setColumnWidth(7, 2500);
		sheet.setColumnWidth(8, 2500);
		sheet.setColumnWidth(9, 2500);
		sheet.setColumnWidth(10, 2500);
		sheet.setColumnWidth(11, 2500);

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

		// 创建单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.WHITE.index);
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

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style.setWrapText(true);// 自动换行

		// 创建单元格样式
		HSSFCellStyle style4 = workbook.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);

		style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		style4.setFont(font2);

		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style1.setWrapText(true);// 自动换行

		// NO验货单编号样式
		HSSFCellStyle style6 = workbook.createCellStyle();
		style6.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style6.setWrapText(true);// 自动换行

		// 第六行样式
		HSSFCellStyle style5 = workbook.createCellStyle();
		style5.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style5.setWrapText(true);// 自动换行

		// 列头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font.setFontHeight((short) 380); // 设置字体大小
		font.setFontName("宋体"); // 设置单元格字体
		font.setUnderline((byte) 1); // 设置下划线
		titleStyle.setFont(font);

		HSSFRow row = null;
		HSSFCell cell = null;
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		DecimalFormat decimalFormat2 = new DecimalFormat("###.000000");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String yhdbh = "";
		int count = 0;
		int totalCount = 0;
		String yhdmxInfo = "";

		Set<String> set = new HashSet<String>();
		StringBuffer hql = new StringBuffer("from Yhdmx obj ");
		if (StringUtils.isNotEmpty(id)) {
			hql.append(" where obj.id in (" + id + ")");
		}
		if (StringUtils.isNotEmpty(parWhere)) {
			String hqlWhereTemp = parWhere.replace("%25", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		List<Yhdmx> yhdmxList = yhdmxManager.find(hql.toString());
		// 记录同一个验货单总共有几个明细
		// for (int i = 1; i <= idsArray.length; i++) {
		// if (idsArray[i - 1].length() == 34) {
		// Yhdmx yhdmx = this.yhdmxManager.get(idsArray[i - 1]);
		// set.add(yhdmx.getYhdgl().getBh());
		// }
		// }
		for (Yhdmx yhdmx : yhdmxList) {
			set.add(yhdmx.getYhdgl().getBh());
		}
		for (String bh : set) {
			totalCount = 0;
			for (Yhdmx yhdmx : yhdmxList) {
				if (bh.equals(yhdmx.getYhdgl().getBh())) {
					totalCount++;
				}
			}
			// for (int i = 1; i <= idsArray.length; i++) {
			// if (idsArray[i - 1].length() == 34) {
			// Yhdmx yhdmx = this.yhdmxManager.get(idsArray[i - 1]);
			// if(bh.equals(yhdmx.getYhdgl().getBh())){
			// totalCount++;
			// }
			// }
			// }
			yhdmxInfo += bh + "|" + totalCount + ",";
		}

		// 同一个验货单总的明细数量
		// String zbh = "";

		try {
			for (int i = 1; i <= yhdmxList.size(); i++) {
				// for(Yhdmx yhdmx:yhdmxList){
				// if (idsArray[i - 1].length() == 32) {
				Yhdmx yhdmx = yhdmxList.get(i - 1);

				if (yhdmx != null) {
					// 上间隔
					row = sheet.createRow(10 * i - 10);
					row.setHeight((short) 495);

					// 创建标题行
					row = sheet.createRow(10 * i - 9);
					row.setHeight((short) 500);// 设定行的高度
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellValue("湖北鄂州发电有限责任公司物资入库验收单");
					cell.setCellStyle(titleStyle);// 设置单元格样式
					sheet.addMergedRegion(new CellRangeAddress(10 * i - 9,
							10 * i - 9, 1, 11));

					// 第一行
					row = sheet.createRow(10 * i - 8);
					row.setHeight((short) 400);
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style5);
					cell.setCellValue("到货日期:");

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
							10 * i - 8, 2, 3));
					cell = row.createCell(2);
					cell.setCellStyle(style1);
					cell.setCellValue(DateUtils.formatDate(yhdmx.getYhdgl()
							.getDhrq(), "yyyy-MM-dd HH:mm:ss"));

					cell = row.createCell(3);
					cell.setCellStyle(style1);

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
							10 * i - 8, 4, 5));
					cell = row.createCell(4);
					cell.setCellStyle(style5);
					cell.setCellValue("验收日期:");

					cell = row.createCell(5);
					cell.setCellStyle(style1);

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
							10 * i - 8, 6, 7));
					cell = row.createCell(6);
					cell.setCellStyle(style1);
					cell.setCellValue(DateUtils.formatDate(yhdmx.getYhdgl()
							.getYsrq(), "yyyy-MM-dd HH:mm:ss"));

					cell = row.createCell(7);
					cell.setCellStyle(style1);

					if (i == 1) {
						yhdbh = yhdmx.getYhdgl().getBh();
						count++;
					} else {
						if (yhdmx.getYhdgl().getBh().equals(yhdbh)) {
							count++;
							yhdbh = yhdmx.getYhdgl().getBh();
						} else {
							count = 1;
							yhdbh = yhdmx.getYhdgl().getBh();
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(10 * i - 8,
							10 * i - 8, 8, 11));
					cell = row.createCell(8);
					cell.setCellStyle(style6);

					// String info = StringUtils.removeEnd(yhdmxInfo, ",");
					// String totalBh[] = info.split(",");
					// for (int j = 0; j < totalBh.length; j++) {
					// if(totalBh[j].substring(0,
					// 8).equals(yhdmx.getYhdgl().getBh())){
					// zbh = totalBh[j].substring(9);
					// }
					// }
					cell.setCellValue("NO:" + yhdmx.getYhdgl().getBh() + "-"
							+ yhdmx.getXh() + "/"
							+ yhdmx.getYhdgl().getYhdmxs().size());

					cell = row.createCell(9);
					cell.setCellStyle(style6);

					cell = row.createCell(10);
					cell.setCellStyle(style6);

					cell = row.createCell(11);
					cell.setCellStyle(style6);

					// 第二行
					row = sheet.createRow(10 * i - 7);
					row.setHeight((short) 500);
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue("仓库名称:");

					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getWzdagl().getKw().getCk()
							.getCkmc());

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 7,
							10 * i - 7, 3, 4));
					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue("采购计划:");

					cell = row.createCell(4);
					cell.setCellStyle(style);

					cell = row.createCell(5);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getCgjhmx().getCgjhgl().getBh());

					cell = row.createCell(6);
					cell.setCellStyle(style);
					cell.setCellValue("采购合同:");

					cell = row.createCell(7);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getYhdgl().getHtbh() == null ? ""
							: yhdmx.getYhdgl().getHtbh());

					cell = row.createCell(8);
					cell.setCellStyle(style);
					cell.setCellValue("发票号码:");

					cell = row.createCell(9);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getFpbh());

					cell = row.createCell(10);
					cell.setCellStyle(style);
					cell.setCellValue("运杂费:");

					cell = row.createCell(11);
					cell.setCellStyle(style4);
					cell.setCellValue(yhdmx.getZf() != null ? yhdmx.getZf() : 0);

					// 第三行
					row = sheet.createRow(10 * i - 6);
					row.setHeight((short) 500);
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue("物资代码:");

					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getWzdagl().getWzbh());

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
							10 * i - 6, 3, 4));
					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue("图号:");

					cell = row.createCell(4);
					cell.setCellStyle(style);

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
							10 * i - 6, 5, 6));
					cell = row.createCell(5);
					cell.setCellStyle(style);
					cell.setCellValue("");

					cell = row.createCell(6);
					cell.setCellStyle(style);

					cell = row.createCell(7);
					cell.setCellStyle(style);
					cell.setCellValue("供货单位:");

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 6,
							10 * i - 6, 8, 11));
					cell = row.createCell(8);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getYhdgl().getGhdw());

					cell = row.createCell(9);
					cell.setCellStyle(style);

					cell = row.createCell(10);
					cell.setCellStyle(style);

					cell = row.createCell(11);
					cell.setCellStyle(style);

					// 第四行
					row = sheet.createRow(10 * i - 5);
					row.setHeight((short) 500);
					sheet.addMergedRegion(new CellRangeAddress(10 * i - 5,
							10 * i - 5, 1, 2));
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue("物资名称及规格");

					cell = row.createCell(2);
					cell.setCellStyle(style);

					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue("单位");

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 5,
							10 * i - 5, 4, 5));
					cell = row.createCell(4);
					cell.setCellStyle(style);
					cell.setCellValue("实收数量");

					cell = row.createCell(5);
					cell.setCellStyle(style);

					cell = row.createCell(6);
					cell.setCellStyle(style);
					cell.setCellValue("实际单价");

					cell = row.createCell(7);
					cell.setCellStyle(style);
					cell.setCellValue("入库金额");

					cell = row.createCell(8);
					cell.setCellStyle(style);
					cell.setCellValue("进项税金");

					cell = row.createCell(9);
					cell.setCellStyle(style);
					cell.setCellValue("价税合计");

					cell = row.createCell(10);
					cell.setCellStyle(style);
					// cell.setCellValue("计划单价");
					cell.setCellValue("");

					cell = row.createCell(11);
					cell.setCellStyle(style);
					// cell.setCellValue("计划金额");
					cell.setCellValue("入库金额");

					// 第五行
					row = sheet.createRow(10 * i - 4);
					row.setHeight((short) 1000);
					sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
							10 * i - 4, 1, 2));
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getWzdagl().getWzmc()
							+ "\n"
							+ (yhdmx.getWzdagl().getXhgg() == null ? "" : yhdmx
									.getWzdagl().getXhgg()));

					cell = row.createCell(2);
					cell.setCellStyle(style);

					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue(yhdmx.getWzdagl().getJldw());

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 4,
							10 * i - 4, 4, 5));
					cell = row.createCell(4);
					cell.setCellStyle(style4);
					cell.setCellValue(decimalFormat.format(yhdmx.getYssl()));

					cell = row.createCell(5);
					cell.setCellStyle(style4);

					cell = row.createCell(6);
					cell.setCellStyle(style4);
					cell.setCellValue(decimalFormat2
							.format(yhdmx.getSqdj() == null ? 0.0 : yhdmx
									.getSqdj()));

					cell = row.createCell(7);
					cell.setCellStyle(style4);
					/**
					 * 如果采用decimalFormat.format() 则在四舍五入的时候如果小数点第三位是6的话才进位
					 * 5的话不进位 所以采用 BigDecimal a = new BigDecimal(sjhj);
					 * 因为保留两位小数时 在将double转换成bigDecimal时 有可能产生比如 34.455
					 * 转换完就是34.454999999 这样直接保留两位小数的话 就是34.45 但实际值应该是34.46
					 * 所以要先保留3位小数再保留两位小数 hj.divide(one,3,
					 * BigDecimal.ROUND_HALF_UP).divide(one,2,
					 * BigDecimal.ROUND_HALF_UP).doubleValue(); 保留3位的话
					 * 如果值是24.4445
					 * ,保留两位小数应该是24.44,但如果先保留3位小数则是24.445,再保留两位小数则是24.45,会改变值
					 * 所以建议保留5位小数后再保留两位小数，则两种情况都可适用 进行格式化
					 */
					double rkje = yhdmx.getYssl()
							* (yhdmx.getSqdj() == null ? 0.0 : yhdmx.getSqdj());

					BigDecimal hj = new BigDecimal(rkje);
					BigDecimal one = new BigDecimal(1);
					double sjje = hj.divide(one, 5, BigDecimal.ROUND_HALF_UP)
							.divide(one, 2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					// 根据财务部需求 入库金额 需加上杂费
					if (yhdmx.getZf() != null) {
						cell.setCellValue(sjje + yhdmx.getZf());
					} else {
						cell.setCellValue(sjje);
					}

					// 进项税金=税额
					cell = row.createCell(8);
					cell.setCellStyle(style4);
					double se = rkje
							* (yhdmx.getSl() == null ? 0.0 : yhdmx.getSl());
					/**
					 * 如果采用decimalFormat.format() 则在四舍五入的时候如果小数点第三位是6的话才进位
					 * 5的话不进位 所以采用 BigDecimal a = new BigDecimal(sjhj); 进行格式化
					 */
					BigDecimal a = new BigDecimal(se);
					double sjse = a.divide(one, 5, BigDecimal.ROUND_HALF_UP)
							.divide(one, 2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					cell.setCellValue(sjse);

					cell = row.createCell(9);
					cell.setCellStyle(style4);
					cell.setCellValue(yhdmx.getSjhj());

					cell = row.createCell(10);
					cell.setCellStyle(style4);
					// cell.setCellValue(decimalFormat.format(yhdmx.getWzdagl().getJhdj()));
					cell.setCellValue("");

					cell = row.createCell(11);
					cell.setCellStyle(style4);
					// cell.setCellValue(decimalFormat.format(yhdmx.getYssl() *
					// yhdmx.getWzdagl().getJhdj()));
					if (yhdmx.getZf() != null) {
						cell.setCellValue(sjje + yhdmx.getZf());
					} else {
						cell.setCellValue(sjje);
					}
					// System.out.println("----"+decimalFormat.format(sjhj));
					// System.out.println(hj.setScale(2, 2).doubleValue());
					// 间隔
					row = sheet.createRow(10 * i - 3);
					row.setHeight((short) 150);

					// 第六行
					row = sheet.createRow(10 * i - 2);
					row.setHeight((short) 350);
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(style5);
					cell.setCellValue("验收员:");

					cell = row.createCell(2);
					cell.setCellStyle(style1);
					cell.setCellValue(yhdmx.getYhdgl().getPersonBgy()
							.getUserName());

					sheet.addMergedRegion(new CellRangeAddress(10 * i - 2,
							10 * i - 2, 3, 6));
					cell = row.createCell(3);
					cell.setCellStyle(style1);
					cell.setCellValue("");

					cell = row.createCell(4);
					cell.setCellStyle(style1);

					cell = row.createCell(5);
					cell.setCellStyle(style1);

					cell = row.createCell(6);
					cell.setCellStyle(style1);

					cell = row.createCell(7);
					cell.setCellStyle(style5);
					cell.setCellValue("经理签字:");

					cell = row.createCell(8);
					cell.setCellStyle(style1);
					cell.setCellValue("");

					cell = row.createCell(9);
					cell.setCellStyle(style1);
					cell.setCellValue("");

					cell = row.createCell(10);
					cell.setCellStyle(style5);
					cell.setCellValue("采购员:");

					cell = row.createCell(11);
					cell.setCellStyle(style1);
					cell.setCellValue(yhdmx.getYhdgl().getPersonCgy()
							.getUserName());

					// 下间隔
					row = sheet.createRow(10 * i - 1);
					row.setHeight((short) 495);
				}
				// }
			}
			// }
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
	 * 采购计划明细导出
	 */
	public void exportSelectedExcelAction() throws IOException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader(
				"Content-Disposition",
				"attachment;filename="
						+ new String(
								("导出数据_" + System.currentTimeMillis() + ".xls")
										.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			try {
				bis = new BufferedInputStream(this.exportYhdmxExcel());
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

	@Override
	public HibernateEntityDao getManager() {
		return yhdmxManager;
	}

	@Override
	public String[] listJsonProperties() {
		String[] jsonLists=null;
		
		String lists = request.getParameter("lists");
		if(lists!=null&&lists.equals("yhdmx")){
			jsonLists=new String[]{
					"id","dhsl","fpbh","yssl","hsxs","sxsl","cgjldw","zf","jhdj","jhje",
					"sqdj","rkje","sl","se","sjhj","cgjhmx","cgjhgl","bh",
					"wzdagl","wzmc","xhgg","kw","cwmc","ck","ckmc"};
		}else{
			jsonLists =new String[] {"sjhj", "jhje", "cgjhmx", "xqjhDetail", "xqjh",
				"gclb", "gcxm", "sqbm", "ghdw", "xh", "sl", "zf", "wzdagl",
				"wzmc", "xhgg", "kw", "cwmc", "ck", "ckmc", "yhdbh", "tssl",
				"fpbh", "dhsl", "cgjldw", "sqdj", "yssl", "id", "jhdj", "hsxs",
				"yhdgl", "yhdmxs", "bh", "bz", "gclb", "gcxm", "dhrq", "ysrq",
				"htbh", "ghdw", "personCgy", "personBgy", "id", "userName",
				"userLoginName", "zt", "rksj", "remark", "cgjhmx", "cgjhgl",
				"bh", "time" };	
		}
		return jsonLists;
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ghdw", "xh", "sl", "zf", "wzdagl", "wzmc",
				"yhdbh", "tssl", "fpbh", "dhsl", "cgjldw", "sqdj", "yssl",
				"id", "jhdj", "hsxs", "zt", "remark", "time" };
	}
}
