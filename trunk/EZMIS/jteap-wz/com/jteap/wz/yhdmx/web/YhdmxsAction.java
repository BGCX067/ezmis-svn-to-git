package com.jteap.wz.yhdmx.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.cgjhgl.manager.CgjhglManager;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings( { "unchecked", "serial" })
public class YhdmxsAction extends AbstractAction {
	private YhdmxManager yhdmxManager;
 
	private static Map pdmxCountMap = null;
	
	private CkglManager ckglManager;

	private CgjhmxManager cgjhmxManager;

	private CgjhglManager cgjhglManager;
	
	private XqjhManager xqjhManager;

	private static int saveStart = 0;

	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

	public CkglManager getCkglManager() {
		return ckglManager;
	}

	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try {
			this.isUseQueryCache = false;
			String ckgl = request.getParameter("ckgl");
			String jhy = request.getParameter("jhy");
			String sort = request.getParameter("sort");
			String dir = request.getParameter("dir");
			//班组
			String bz = request.getParameter("bz");
			String hqlWhere = request.getParameter("queryParamsSql");
			
			if (StringUtils.isNotEmpty(bz)) {
				HqlUtil.addCondition(hql, "yhdgl.bz", bz );
			}
		
			if (StringUtils.isNotEmpty(ckgl)) {
				HqlUtil.addCondition(hql, "wzdagl.kw.ckid", ckgl );
			}
			if (StringUtils.isNotEmpty(jhy)) {
				HqlUtil.addCondition(hql, "yhdgl.cgy", jhy );
			}
			if (StringUtils.isNotEmpty(hqlWhere)) {
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
			if (!this.isHaveSortField()) {
				//HqlUtil.addOrder(hql, "yhdgl.bh", "desc");
			}else{
				if("ghdw".equals(sort)){
					HqlUtil.addOrder(hql, "yhdgl.ghdw", dir);
				}else{
					HqlUtil.addOrder(hql, sort, dir);
				}
			}
			HqlUtil.addOrder(hql,"rksj", "desc");
			HqlUtil.addOrder(hql,"xh", "asc");
//			System.out.println(hql.toString());
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		
		//System.out.println(hql.toString());
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
			if (pageFlag == null)
				pageFlag = request.getParameter(PAGE_FLAG);
			String json;
			if (pageFlag != null && pageFlag.toString().equals(PAGE_FLAG_NO)) {
				Collection list = this.getManager().find(hql.toString(),
						showListHqlValues.toArray());
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:" + json + "}";
				
			} else {
				json = getPageCollectionJson(hql.toString(), showListHqlValues
						.toArray());
			}
//			System.out.println(json);
			this.outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}

	/**
	 * 根据hql，分页查询 lvchao
	 * 
	 * @param hql
	 * @return
	 */
	protected String getPageCollectionJson(String hql, Object... values) {
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// System.out.println(hql);
		// 开始分页查询
		Page page = getManager().pagedQueryWithStartIndex(hql,
				Integer.parseInt(start), Integer.parseInt(limit),
				isUseQueryCache, values);
		Collection obj = (Collection) page.getResult();
		List<Yhdmx> yhdmxList = (List) obj;
		// 分页数据集合
		List jsonList = new ArrayList();
		for (Yhdmx yhdmx : yhdmxList) {
			Map yhdmxMap = new HashMap();

			yhdmxMap.put("id", yhdmx.getId());
			yhdmxMap.put("xh", yhdmx.getXh());
			yhdmxMap.put("jhje", yhdmx.getYssl()*yhdmx.getWzdagl().getJhdj());
			// yhdmxMap.put("xqjhDetail",yhdmx.getCgjhmx().getXqjhDetail());
			if(yhdmx.getCgjhmx()==null){
				yhdmxMap.put("gclb","");
				yhdmxMap.put("gcxm","");
				yhdmxMap.put("bz", "");
			}else{
				yhdmxMap.put("gclb",yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getGclb());
				yhdmxMap.put("gcxm",yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getGcxm());
				yhdmxMap.put("bz", yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getSqbm());
			}
			// yhdmxMap.put("sqbm",yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getSqbm());
			yhdmxMap.put("ghdw", yhdmx.getYhdgl().getGhdw());
			yhdmxMap.put("sl", yhdmx.getSl());
			yhdmxMap.put("zf", yhdmx.getZf());
			yhdmxMap.put("wzmc", yhdmx.getWzdagl().getWzmc());
			yhdmxMap.put("wzid", yhdmx.getWzdagl().getId());
			yhdmxMap.put("xhgg", yhdmx.getWzdagl().getXhgg());
			yhdmxMap.put("ckmc", yhdmx.getWzdagl().getKw().getCk().getCkmc());
			yhdmxMap.put("cwmc", yhdmx.getWzdagl().getKw().getCwmc());
			yhdmxMap.put("yhdmxs", yhdmx.getYhdgl().getYhdmxs());
			yhdmxMap.put("cuserName", yhdmx.getYhdgl().getPersonCgy()
					.getUserName());
			yhdmxMap.put("cid", yhdmx.getYhdgl().getPersonCgy().getId());
			yhdmxMap.put("cloginName", yhdmx.getYhdgl().getPersonCgy()
					.getUserLoginName());
			yhdmxMap.put("buserName", yhdmx.getYhdgl().getPersonBgy()
					.getUserName());
			yhdmxMap.put("bid", yhdmx.getYhdgl().getPersonBgy().getId());
			yhdmxMap.put("bloginName", yhdmx.getYhdgl().getPersonBgy()
					.getUserLoginName());
			yhdmxMap.put("zt", yhdmx.getZt());
			yhdmxMap.put("yhdid", yhdmx.getYhdgl().getId());
			yhdmxMap.put("bh", yhdmx.getYhdgl().getBh());
			yhdmxMap.put("ysrq", yhdmx.getYhdgl().getYsrq());
			yhdmxMap.put("htbh", yhdmx.getYhdgl().getHtbh());
			yhdmxMap.put("ghdw", yhdmx.getYhdgl().getGhdw());
			yhdmxMap.put("tssl", yhdmx.getTssl());
			yhdmxMap.put("fpbh", yhdmx.getFpbh());
			yhdmxMap.put("dhsl", yhdmx.getDhsl());
			yhdmxMap.put("cgjldw", yhdmx.getCgjldw());
			yhdmxMap.put("sqdj", yhdmx.getSqdj());
			yhdmxMap.put("jhdj", yhdmx.getWzdagl().getJhdj());
			yhdmxMap.put("hsxs", yhdmx.getHsxs());
			yhdmxMap.put("rksj", yhdmx.getRksj());
			yhdmxMap.put("dhsj", yhdmx.getYhdgl().getDhrq());
			yhdmxMap.put("yssl", yhdmx.getYssl());
			yhdmxMap.put("sjhj", yhdmx.getSjhj());
			yhdmxMap.put("flag", yhdmxManager.findLyByYh(yhdmx));
			yhdmxMap.put("se",(yhdmx.getSqdj()==null)?0:yhdmx.getSqdj()*yhdmx.getYssl()*yhdmx.getSl());
			yhdmxMap.put("sqhj", (yhdmx.getSqdj()==null)?0:yhdmx.getSqdj()*yhdmx.getYssl());
			jsonList.add(yhdmxMap);
		}
		Map countMaps = new HashMap();
		if (jsonList.size() > 0) {
//			暂时不需要每页合计 暂时注释添加每页合计代码
			// 判断是否有前一页 没有则为第一页 则新建对象
			if (page.hasPreviousPage() == false) {
				// 保存开始条数
				saveStart = Integer.parseInt(start);
				pdmxCountMap = new HashMap();
				pdmxCountMap.put("id", UUIDGenerator.hibernateUUID());
				pdmxCountMap.put("xh", "<font color='blue'>到当前页总和：</font>");
				pdmxCountMap.put("sjhj", new Double(0.0));
				pdmxCountMap.put("jhje", new Double(0.0));
				pdmxCountMap.put("sqdj", new Double(0.0));
				pdmxCountMap.put("sqhj", new Double(0.0));
				pdmxCountMap.put("sehj", new Double(0.0));
				yhdmxManager.setCountValue(yhdmxList, pdmxCountMap, "d");
			} else {
				// 如果上一次保存的开始条数大于这次传来的开始条数，则是点击上一页
				// 否则 点击的是下一页
				if (saveStart > Integer.parseInt(start)) {
					Page pages = getManager().pagedQueryWithStartIndex(hql,
							saveStart, Integer.parseInt(limit),
							isUseQueryCache, values);
					obj = (Collection) pages.getResult();
					yhdmxList = (List) obj;
					yhdmxManager.setCountValue(yhdmxList, pdmxCountMap, "q");
				} else {
					yhdmxManager.setCountValue(yhdmxList, pdmxCountMap, "d");
				}
				saveStart = Integer.parseInt(start);
				// 如果最后一页标志存在 则删除这个标志
				if (pdmxCountMap.get("last") != null) {
					pdmxCountMap.remove("last");
				}

			}

			// 如果有记录 则添加合计
			List list = yhdmxManager.find(hql);
			if (list.size() > 0) {
				// 添加合计
				countMaps.put("id", UUIDGenerator.hibernateUUID());
				countMaps.put("xh", "<font color='red'>总合计：</font>");
				yhdmxManager.setCountValue(list, countMaps, "h");
				// 该页如果没有下一页 则是最后一页 加入标志
				if (!page.hasNextPage()) {
					yhdmxManager.setCountValue(list, pdmxCountMap, "h");
					pdmxCountMap.put("last", "y");
				}
			}
		} else {
			pdmxCountMap = new HashMap();
			countMaps = new HashMap();
		}
		//System.out.println(jsonList.size());
		// 将集合JSON化
		String json = JSONUtil.listToJson(jsonList, this.listJsonProperties());
//		// 到当前页的数据的json字符串
		String pageTotalJson = JSONUtil.mapToJson(pdmxCountMap);
		// 总合计的json字符串
		String mainTotalJson = JSONUtil.mapToJson(countMaps);
		StringBuffer dataBlock = new StringBuffer();
		dataBlock.append("{totalCount:'" + page.getTotalCount() + "',list:"
				+ json + ",mainTotal:"
				+ mainTotalJson + ",pageTotal:"+pageTotalJson+"}");
		return dataBlock.toString();
	}

	/**
	 * 验货单打印
	 */
	public void yhdmxPrintAction() {
		String id = request.getParameter("id");
		String path = request.getSession().getServletContext().getRealPath(
				"/jteap/wz/yhrkmx/yhd.xls");
		java.io.BufferedOutputStream outs = null;
		try {
			Yhdmx yhdmx = this.yhdmxManager.findUniqueBy("id", id);

			if (yhdmx != null) {
				HSSFWorkbook wb = null;
				File excel = new File(path);
				InputStream is = new FileInputStream(excel);
				wb = new HSSFWorkbook(is);
				// 获取工作空间
				HSSFSheet rs = wb.getSheetAt(0);
				// 设置验货日期
				rs.getRow(1).getCell(2).setCellValue(
						DateUtils.formatDate(yhdmx.getYhdgl().getYsrq(),
								"yyyy-MM-dd hh:mm:ss"));
				// 制单日期
				rs.getRow(1).getCell(6).setCellValue(
						DateUtils.formatDate(new Date(), "yyyy年MM月dd日"));
				// NO
				rs.getRow(1).getCell(8).setCellValue(
						"NO:" + yhdmx.getYhdgl().getBh() + "-1/1");

				// 仓库
				rs.getRow(2).getCell(2).setCellValue(
						yhdmx.getWzdagl().getKw().getCk().getCkmc());
				// 采购计划
				rs.getRow(2).getCell(5).setCellValue(
						yhdmx.getCgjhmx().getCgjhgl().getBh());
				// 采购合同
				rs.getRow(2).getCell(7)
						.setCellValue(yhdmx.getYhdgl().getHtbh());
				// 发票号码
				rs.getRow(2).getCell(9).setCellValue(yhdmx.getFpbh());
				// 运杂费
				rs.getRow(2).getCell(11).setCellValue(
						yhdmx.getZf() != null ? yhdmx.getZf() : 0);

				// 物资代码
				rs.getRow(3).getCell(2).setCellValue(yhdmx.getWzdagl().getId());
				// 图号
				rs.getRow(3).getCell(5).setCellValue("");
				// 供货单位
				rs.getRow(3).getCell(8)
						.setCellValue(yhdmx.getYhdgl().getGhdw());

				// 运杂费
				rs.getRow(5).getCell(1).setCellValue(
						yhdmx.getWzdagl().getWzmc() + "\n"
								+ yhdmx.getWzdagl().getXhgg());
				// 单位
				rs.getRow(5).getCell(3).setCellValue(
						yhdmx.getWzdagl().getJldw());
				// 实收数量
				rs.getRow(5).getCell(4).setCellValue(yhdmx.getYssl());
				// 实际单价
				rs.getRow(5).getCell(6).setCellValue(
						yhdmx.getSqdj() != null ? yhdmx.getSqdj() : 0.0);
				// 入库金额
				rs.getRow(5).getCell(7).setCellValue(yhdmx.getJhje());
				// 进项税金
				rs.getRow(5).getCell(8).setCellValue(
						yhdmx.getSl() != null ? yhdmx.getSl() : 0.0);
				// 税价合计
				rs.getRow(5).getCell(9).setCellValue(yhdmx.getSjhj());
				// 计划单价
				rs.getRow(5).getCell(10).setCellValue(
						yhdmx.getWzdagl().getJhdj());
				// 计划金额
				rs.getRow(5).getCell(11).setCellValue(
						yhdmx.getYssl() * yhdmx.getWzdagl().getJhdj());

				// 单位
				rs.getRow(6).getCell(2).setCellValue(
						yhdmx.getYhdgl().getPersonBgy().getUserName());
				// 单位
				rs.getRow(6).getCell(10).setCellValue(
						yhdmx.getYhdgl().getPersonCgy().getUserName());

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
												("验货单_"
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
			// }
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
	 * 重写导出excel方法 lvchao
	 */
	public String exportExcel() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = "";
		// System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex = "";
		// System.out.println(paraDataIndex);
		// System.out.println(paraDataIndex);
		// 宽度(逗号表达式)O
		String paraWidth = "";
		//判断是否物资经理
		String role  = request.getParameter("role");
		//条件
		String hqlWhere  =  request.getParameter("parWhere");
		//如果是物资经理
		if(StringUtils.isNotEmpty(role)){
			paraHeader = "状态,物资名称型号规格,验收数量,计量单位,税前单价,税前合计,税率,税价合计,申请部门,工程项目,入库时间,供应单位";
			paraDataIndex = "zt,wzmc,yssl,cgjldw,sqdj,sqhj,sl,sjhj,sqbm,gcxm,rksj,ghdw";
			paraWidth = "60,200,60,60,100,50,50,100,70,100,100,150";
		}else{
			paraHeader = "状态,验货单编号,物资名称型号规格,库位,仓库,计划员,验收数量,计量单位,计划单价,计划金额,税前单价,税前合计,税率,税价合计,入库时间,保管员,发票编号,工程项目,供应单位";
			paraDataIndex = "zt,bh,wzmc,cwmc,ckmc,cuserName,yssl,cgjldw,jhdj,jhje,sqdj,sqhj,sl,sjhj,rksj,buserName,gcxm,htbh,ghdw";
			paraWidth = "60,100,200,100,100,100,60,60,60,80,100,50,50,100,100,60,100,80,150";
		}
		StringBuffer hql  = new StringBuffer("from Yhdmx obj order by obj.rksj desc");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		List<Yhdmx> yhdmxs = yhdmxManager.find(hql.toString());
		List list = new ArrayList();
		if (yhdmxs.size() < 1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out
					.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("###.00");
			for (Yhdmx yhdmx : yhdmxs) {
				Map yhdmxMap = new HashMap();
				yhdmxMap.put("id", yhdmx.getId());
				yhdmxMap.put("jhje", yhdmx.getYssl()*yhdmx.getWzdagl().getJhdj());
				yhdmxMap.put("sqbm", yhdmx.getCgjhmx().getXqjhDetail()
						.getXqjh().getSqbm());
				yhdmxMap.put("gcxm",yhdmx.getCgjhmx().getXqjhDetail().getXqjh().getGcxm());
				yhdmxMap.put("ghdw", yhdmx.getGhdw());
				yhdmxMap.put("sl", yhdmx.getSl());
				yhdmxMap.put("zf", yhdmx.getZf());
				yhdmxMap.put("wzmc", yhdmx.getWzdagl().getWzmc()+"("+yhdmx.getWzdagl().getXhgg()+")");
				yhdmxMap.put("ckmc", yhdmx.getWzdagl().getKw().getCk()
						.getCkmc());
				yhdmxMap.put("cwmc", yhdmx.getWzdagl().getKw().getCwmc());
				yhdmxMap.put("cuserName", yhdmx.getYhdgl().getPersonCgy()
						.getUserName());
				yhdmxMap.put("cloginName", yhdmx.getYhdgl().getPersonCgy()
						.getUserLoginName());
				yhdmxMap.put("buserName", yhdmx.getYhdgl().getPersonBgy()
						.getUserName());
				yhdmxMap.put("bid", yhdmx.getYhdgl().getPersonBgy().getId());
				yhdmxMap.put("bloginName", yhdmx.getYhdgl().getPersonBgy()
						.getUserLoginName());
				if ("2".equals(yhdmx.getZt())) {
					yhdmxMap.put("zt", "未入库");
				}
				if ("1".equals(yhdmx.getZt())) {
					yhdmxMap.put("zt", "已入库");
				}
				if("0".equals(yhdmx.getZf())){
					yhdmxMap.put("zt","未生效");
				}
				//yhdmxMap.put("yhdid", yhdmx.getYhdgl().getId());
				yhdmxMap.put("bh", yhdmx.getYhdgl().getBh());
				yhdmxMap.put("ysrq", yhdmx.getYhdgl().getYsrq());
				yhdmxMap.put("htbh", yhdmx.getYhdgl().getHtbh());
				yhdmxMap.put("ghdw", yhdmx.getYhdgl().getGhdw());
				yhdmxMap.put("tssl", yhdmx.getTssl());
				yhdmxMap.put("fpbh", yhdmx.getFpbh());
				yhdmxMap.put("dhsl", yhdmx.getDhsl());
				yhdmxMap.put("cgjldw", yhdmx.getCgjldw());
				yhdmxMap.put("sqdj", yhdmx.getSqdj()==null||yhdmx.getSqdj()==0?yhdmx.getSqdj():decimalFormat.format(yhdmx.getSqdj()));
				//计算税前合计
				double rkje = yhdmx.getYssl()
				* (yhdmx.getSqdj() == null ? 0.0 : yhdmx
						.getSqdj());
				BigDecimal hj = new BigDecimal(rkje);
				BigDecimal one = new BigDecimal(1);
				double sjje = hj.divide(one,5, BigDecimal.ROUND_HALF_UP).divide(one,2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//根据财务部需求 入库金额 需加上杂费
				if(yhdmx.getZf()!=null){
					yhdmxMap.put("sqhj", sjje+yhdmx.getZf());
				}else{
					yhdmxMap.put("sqhj", sjje);
				}
				yhdmxMap.put("jhdj", yhdmx.getWzdagl().getJhdj());
				yhdmxMap.put("hsxs", yhdmx.getHsxs());
				yhdmxMap.put("rksj", yhdmx.getRksj());
				yhdmxMap.put("yssl", yhdmx.getYssl());
				yhdmxMap.put("sjhj", yhdmx.getSjhj()==null||yhdmx.getSjhj()==0?yhdmx.getSjhj():decimalFormat.format(yhdmx.getSjhj()));
				list.add(yhdmxMap);
			}
		}

		//yhdmxManager.setCountValue(yhdmxs, list);
		export(list, paraHeader, paraDataIndex, paraWidth);

		// 调用导出方法
		return NONE;
	}
	
	/**
	 *  待验收明细导出
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
		sheet.setColumnWidth(13, 3000);
		
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
		
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		HSSFRow row = null;
		HSSFCell cell = null;
		int count = 0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < idsArray.length; i++) {
				Yhdmx yhdmx = yhdmxManager.get(idsArray[i]);
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
						cell.setCellValue("验货单编号");
						
						cell = row.createCell(2);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("物资名称及规格");
						
						cell = row.createCell(3);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("验收数量");
						
						cell = row.createCell(4);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("计量单位");
						
						cell = row.createCell(5);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("税前单价");
						
						cell = row.createCell(6);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("税率");
						
						cell = row.createCell(7);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("稅价合计");
						
						cell = row.createCell(8);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("验收日期");
						
						cell = row.createCell(9);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("保管员");
						
						cell = row.createCell(10);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("工程项目");
						
						cell = row.createCell(11);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("工程类别");
						
						cell = row.createCell(12);
						cell.setCellStyle(titleStyle);
						cell.setCellValue("班组");
						
						cell = row.createCell(13);
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
					cell.setCellValue(yhdmx.getYhdgl().getBh());
					cell = row.createCell(2);
					cell.setCellStyle(style2);
					if(yhdmx.getWzdagl().getXhgg() == null){
						cell.setCellValue(yhdmx.getWzdagl().getWzmc());
					}else{
						cell.setCellValue(yhdmx.getWzdagl().getWzmc()+"("+yhdmx.getWzdagl().getXhgg()+")");
					}
					cell = row.createCell(3);
					cell.setCellStyle(style2);
					cell.setCellValue(yhdmx.getYssl());
					
					cell = row.createCell(4);
					cell.setCellStyle(style2);
					cell.setCellValue(yhdmx.getWzdagl().getJldw());
					
					cell = row.createCell(5);
					cell.setCellStyle(style2);
					if(yhdmx.getSqdj() == null){
						cell.setCellValue(yhdmx.getSqdj());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(6);
					cell.setCellStyle(style2);
					if(yhdmx.getSl() == null){
						cell.setCellValue(yhdmx.getSl());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(7);
					cell.setCellStyle(style2);
					if(yhdmx.getSjhj() != null){
						cell.setCellValue(yhdmx.getSjhj());
					}else{
						cell.setCellValue("");
					}
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					cell = row.createCell(8);
					cell.setCellStyle(style2);
					if(yhdmx.getYhdgl().getYsrq() != null){
						cell.setCellValue(format.format(yhdmx.getYhdgl().getYsrq()));
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(9);
					cell.setCellStyle(style2);
					cell.setCellValue(yhdmx.getYhdgl().getPersonBgy().getUserName());
					
					cell = row.createCell(10);
					cell.setCellStyle(style2);
					if(StringUtils.isNotEmpty(yhdmx.getYhdgl().getGcxm())){
						cell.setCellValue(yhdmx.getYhdgl().getGcxm());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(11);
					cell.setCellStyle(style2);
					if(StringUtils.isNotEmpty(yhdmx.getYhdgl().getGclb())){
						cell.setCellValue(yhdmx.getYhdgl().getGclb());
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell(12);
					cell.setCellStyle(style2);
					if(StringUtils.isNotEmpty(yhdmx.getYhdgl().getBz())){
						cell.setCellValue(yhdmx.getYhdgl().getBz());
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
	 * 修改物资单价
	 * @return
	 * @throws Exception 
	 */
	public String updateWzdj() throws Exception{
		String ids = request.getParameter("ids");
		String dj = request.getParameter("dj");
		try{
			List<Yhdmx> yhdmxList = yhdmxManager.find("from Yhdmx where id in ("+ids.substring(0,ids.length()-1)+")");
			for(Yhdmx yhdmx:yhdmxList){
				yhdmx.setSqdj(Double.parseDouble(dj));
				yhdmxManager.save(yhdmx);
			}
			this.outputJson("{success:true}");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	@Override
	public HibernateEntityDao getManager() {
		return yhdmxManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "sjhj", "jhje", "cgjhmx", "gclb", "gcxm", "sqbm",
				"ghdw", "xh", "sl", "zf", "wzmc","wzid", "xhgg", "cwmc", "ckmc",
				"yhdmxs", "cuserName", "cid", "cuserLoginName", "buserName",
				"bid", "buserLoginName", "zt","flag","sqhj", "yhdid", "bh","bz", "ysrq", "htbh",
				"ghdw", "tssl", "fpbh", "dhsl", "se","cgjldw", "sqdj", "yssl", "id",
				"jhdj", "hsxs", "rksj", "time","dhsj","time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ghdw", "xh", "sl", "zf", "wzdagl", "wzmc",
				"yhdbh", "tssl", "fpbh", "dhsl", "cgjldw", "sqdj", "yssl",
				"id", "jhdj", "hsxs", "zt", "time" };
	}

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	public CgjhglManager getCgjhglManager() {
		return cgjhglManager;
	}

	public void setCgjhglManager(CgjhglManager cgjhglManager) {
		this.cgjhglManager = cgjhglManager;
	}

}
