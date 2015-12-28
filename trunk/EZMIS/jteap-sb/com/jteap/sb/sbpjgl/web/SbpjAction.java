package com.jteap.sb.sbpjgl.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.sb.common.chart.Pie3DChart;
import com.jteap.sb.common.chart.Pie3DSet;
import com.jteap.sb.sbpjgl.manager.SbpjCatalogManager;
import com.jteap.sb.sbpjgl.manager.SbpjManager;
import com.jteap.sb.sbpjgl.model.Sbpj;
import com.jteap.sb.sbpjgl.model.SbpjCatalog;

@SuppressWarnings({ "unchecked", "serial", "unused" })
public class SbpjAction extends AbstractAction {

	private SbpjManager sbpjManager;
	
	private SbpjCatalogManager sbpjCatalogManager;

	public SbpjManager getSbpjManager() {
		return sbpjManager;
	}

	public void setSbpjManager(SbpjManager sbpjManager) {
		this.sbpjManager = sbpjManager;
	}

	public SbpjCatalogManager getSbpjCatalogManager() {
		return sbpjCatalogManager;
	}

	public void setSbpjCatalogManager(SbpjCatalogManager sbpjCatalogManager) {
		this.sbpjCatalogManager = sbpjCatalogManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return sbpjManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","sbpjCatalog","pjfl","sbbm","sbmc","sbgg","scpjrq","scpjjb","scpjr","bcpjrq","bcpjjb","bcpjr","remark","time"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","sbpjCatalog","pjfl","sbbm","sbmc","sbgg","scpjrq","scpjjb","scpjr","bcpjrq","bcpjjb","bcpjr","remark"
		};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String flbm = request.getParameter("flbm");
		String sbbm = request.getParameter("sbbm");
		if (StringUtil.isNotEmpty(flbm)) {
			HqlUtil.addWholeCondition(hql, "sbpjCatalog.flbm like '"+flbm+"%'");
		}else if(StringUtil.isNotEmpty(sbbm)){
			HqlUtil.addWholeCondition(hql, "obj.sbbm = '"+sbbm+"'");
		}
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}
	
	/**
	 * 保存或修改设备基础台帐信息
	 * @return
	 */
	public String saveOrUpdateAction(){
		try {
			Sbpj sbpj = new Sbpj();
			String id = request.getParameter("id");
			String sbpjCatalogId = request.getParameter("sbpjCatalogId");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date scpjrq = (("").equals(request.getParameter("scpjrq"))?null:dateFormat.parse(request.getParameter("scpjrq")));
			Date bcpjrq = (("").equals(request.getParameter("bcpjrq"))?null:dateFormat.parse(request.getParameter("bcpjrq")));
			SbpjCatalog sbpjCatalog = sbpjCatalogManager.get(sbpjCatalogId);
			if(StringUtil.isNotEmpty(id)){
				sbpj.setId(id);
			}
//			if(StringUtil.isNotEmpty(tzflCatalogId)){
//				jctz.setTzflCatalogId(tzflCatalogId);
//			}
			sbpj.setSbpjCatalog(sbpjCatalog);
			sbpj.setPjfl(request.getParameter("pjfl"));
			sbpj.setSbbm(request.getParameter("sbbm"));
			sbpj.setSbmc(request.getParameter("sbmc"));
			sbpj.setSbgg(request.getParameter("sbgg"));
			sbpj.setScpjrq(scpjrq);
			sbpj.setScpjjb(request.getParameter("scpjjb"));
			sbpj.setScpjr(request.getParameter("scpjr"));
			sbpj.setBcpjrq(bcpjrq);
			sbpj.setBcpjjb(request.getParameter("bcpjjb"));
			sbpj.setBcpjr(request.getParameter("bcpjr"));
			sbpj.setRemark(request.getParameter("remark"));
			sbpjManager.save(sbpj);
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 根据条件查询相关合计及所占比率
	 * @return String
	 */
	public String showTotalandRatio() throws Exception{
		String queryParamsHql = StringUtil.isoToUTF8(request.getParameter("queryParams"));
		String hqlWhereTemp = "";
		if(StringUtils.isNotEmpty(queryParamsHql)){
			hqlWhereTemp = queryParamsHql.replace("$", " ");
		}
		String total,firstTotal,secondTotal,thirdTotal,wdTotal,firstRatio,secondRatio,thirdRatio,wdRatio = "";
		try {
			 total = String.valueOf(sbpjManager.getTotalByParamsHql(hqlWhereTemp));
			 firstTotal = String.valueOf(sbpjManager.getItemTotalByParamsHql("一类",hqlWhereTemp));
			 secondTotal = String.valueOf(sbpjManager.getItemTotalByParamsHql("二类",hqlWhereTemp));
			 thirdTotal = String.valueOf(sbpjManager.getItemTotalByParamsHql("三类",hqlWhereTemp));
			 wdTotal = String.valueOf(sbpjManager.getItemTotalByParamsHql("未定",hqlWhereTemp));
			 firstRatio = String.valueOf(sbpjManager.getRatioByParamsHql("一类",hqlWhereTemp));
			 secondRatio = String.valueOf(sbpjManager.getRatioByParamsHql("二类",hqlWhereTemp));
			 thirdRatio = String.valueOf(sbpjManager.getRatioByParamsHql("三类",hqlWhereTemp));
			 wdRatio = String.valueOf(sbpjManager.getRatioByParamsHql("未定",hqlWhereTemp));
			 String json = "{success:true,total:'" + total + "',firstTotal:'" + firstTotal +
			 "',secondTotal:'" + secondTotal + "',thirdTotal:'" + thirdTotal +
			 "',wdTotal:'" + wdTotal + "',firstRatio:'" + firstRatio + 
			 "',secondRatio:'" + secondRatio + "',thirdRatio:'" + thirdRatio + 
			 "',wdRatio:'" + wdRatio + "'}";
			 outputJson(json);
		} catch (Exception ex) {
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}
	
	/**
	 * 获取设备评级级别分类(拼装PieChart数据)
	 * @return String
	 */
	public String showSbpjjbResultAction() throws Exception{
		String queryParamsHql = StringUtil.isoToUTF8(request.getParameter("queryParams"));
		String hqlWhereTemp = "";
		if(StringUtils.isNotEmpty(queryParamsHql)){
			hqlWhereTemp = queryParamsHql.replace("$", " ");
		}
		double total,firstRatio,secondRatio,thirdRatio,wdRatio = 0.0;
		try {
			Pie3DChart p3Chart = new Pie3DChart();
			p3Chart.setCaption("设备评级构成");
			
			total = sbpjManager.getTotalByParamsHql(hqlWhereTemp);
			firstRatio = sbpjManager.getRatioByParamsHql("一类",hqlWhereTemp);
			secondRatio = sbpjManager.getRatioByParamsHql("二类",hqlWhereTemp);
			thirdRatio = sbpjManager.getRatioByParamsHql("三类",hqlWhereTemp);
			wdRatio = sbpjManager.getRatioByParamsHql("未定",hqlWhereTemp);
			
			//一类所占比率
			Pie3DSet p3Set1 = new Pie3DSet();
			p3Set1.setLabel("一类");
			p3Set1.setIsSliced("1");
			p3Set1.setValue(String.valueOf(Math.round(firstRatio * 100) / 100));
			p3Chart.getPie3DSet().add(p3Set1);
			
			//二类所占比率
			Pie3DSet p3Set2 = new Pie3DSet();
			p3Set2.setLabel("二类");
			p3Set2.setIsSliced("1");
			p3Set2.setValue(String.valueOf(Math.round(secondRatio * 100) / 100));
			p3Chart.getPie3DSet().add(p3Set2);
			
			//三类所占比率
			Pie3DSet p3Set3 = new Pie3DSet();
			p3Set3.setLabel("三类");
			p3Set3.setIsSliced("1");
			p3Set3.setValue(String.valueOf(Math.round(thirdRatio * 100) / 100));
			p3Chart.getPie3DSet().add(p3Set3);
			
			//未定所占比率
			Pie3DSet p3Set4 = new Pie3DSet();
			p3Set4.setLabel("未定");
			p3Set4.setIsSliced("1");
			p3Set4.setValue(String.valueOf(Math.round(wdRatio * 100) / 100));
			p3Chart.getPie3DSet().add(p3Set4);
			outputJson("{success:true,chartData:'"+p3Chart.toString()+"'}");
		} catch (Exception ex) {
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		
		return NONE;
	}

	public String exportExcelForSbbg() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = request.getParameter("paraHeader");
//		paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");
		paraHeader = "设备名称,设备规格,设备评级,备注";

		// 表索引信息（逗号表达式）
//		String paraDataIndex = request.getParameter("paraDataIndex");
		String paraDataIndex = "sbmc,sbgg,bcpjjb,remark";

		// 宽度(逗号表达式)
//		String paraWidth = request.getParameter("paraWidth");
		String paraWidth = "150,150,100,150";

		StringBuffer hql = getPageBaseHql();

		beforeShowList(request, response, hql);

		List list = getManager().createQuery(hql.toString()).list();

		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	
}
