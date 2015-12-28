/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.cgjhgl.web;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.cgjhgl.manager.CgjhglManager;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings({ "unchecked", "serial", "unused" })
public class CgjhglAction extends AbstractAction {
	private CgjhglManager cgjhglManager;
	private CgjhmxManager cgjhmxManager;
	private YhdglManager yhdglManager;
	private YhdmxManager yhdmxManager;
	private XqjhDetailManager xqjhDetailManager;
	private XqjhManager xqjhManager;
	private XqjhsqDetailManager xqjhsqDetailManager;
	private XqjhsqManager xqjhsqManager;
	
	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

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

	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}

	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
	}

	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
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
	
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			//查询以生效的采购单（新建验货单使用）showType 以生效:1  未生效:0
			String showType = request.getParameter("showType");
			String yhdbh = request.getParameter("bh");
			
			if(StringUtils.isNotEmpty(yhdbh)){
				HqlUtil.addCondition(hql, "id", "select cgjhgl from Cgjhmx t where t.id " +
						"in(select cgjhmx from Yhdmx where yhdgl = " +
						"(select id from Yhdgl where bh = '"+yhdbh+"'))", HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
			}else if(StringUtils.isNotEmpty(showType) && "1".equals(showType)){
				String cgy = request.getParameter("cgy");
				String userLoginName = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
				//是否显示指定标志
				String zt = request.getParameter("zt");
				if(StringUtils.isNotEmpty(zt)){
					HqlUtil.addCondition(hql, "id", "select cgjhgl from Cgjhmx t where t.cgy='"+cgy+"' and t.dyszt = '2' and dysczr = '"+userLoginName+"' and  ((t.cgsl > t.dhsl and t.zt='1') or ( t.cgsl > t.dhsl and t.zt='3'))", HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
//					HqlUtil.addCondition(hql,"dyszt","1",HqlUtil.LOGIC_OR);
				}else{
					HqlUtil.addCondition(hql, "id", "select cgjhgl from Cgjhmx t where t.cgy='"+cgy+"' and ((t.cgsl > t.dhsl and t.zt='1') or ( t.cgsl > t.dhsl and t.zt='3'))", HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
				}
//				HqlUtil.addCondition(hql, "zt","1",HqlUtil.LOGIC_AND);
			}else{
				String loginName=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
				HqlUtil.addCondition(hql,"id","select cgjhgl from Cgjhmx t where t.cgy='"+loginName+"' and t.zt='0'",HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
				//HqlUtil.addCondition(hql, "jhy",loginName,HqlUtil.LOGIC_OR);
				HqlUtil.addCondition(hql, "zt","0",HqlUtil.LOGIC_AND);
			}
			
  			String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "bh", "desc");
		}
		//System.out.println(hql.toString());
	}
	
	/**
	 * 采购计划生效
	 */
	public String enableCgjh() throws Exception{
		String ids = request.getParameter("ids");
		String cglid = request.getParameter("cglid");
//		try {
//			if(StringUtil.isNotEmpty(ids)) {
//				List<Cgjhgl> cgjhgl = cgjhglManager.findByIds(ids.split(","));
//				for(Cgjhgl obj:cgjhgl){
//					obj.setZt("1"); //采购计划生效
//					obj.setSxsj(new Date());
//					Set<Cgjhmx> mxs = obj.getCgjhmxs();
//					for(Cgjhmx mx : mxs){
//						mx.setZt("1");
//					}
//					cgjhglManager.save(obj);
//				}
//				outputJson("{success:true}");
//			}
//		} catch (Exception e) {
//			String msg = e.getMessage();
//			e.printStackTrace();
//			outputJson("{success:false,msg:'" + msg + "'}");
//		}
//		return NONE;
		try {
			if(ids!=null && cglid!=null) {
				String[] array_id = ids.split(",");
				Map<String,String> map_id_sxsl = new HashMap<String,String>();
				for (int i = 0; i < array_id.length; i++) {
					map_id_sxsl.put(array_id[i], array_id[i]);
					//通过采购计划明细获得需求计划申请明细相关物资，修改状态为"已采购"
					Cgjhmx cgjhmx = cgjhmxManager.get(array_id[i]);
					String wzbm = cgjhmx.getWzdagl().getId();
					if(StringUtil.isNotEmpty(cgjhmx.getXqjhmx())){
						XqjhDetail xqjhDetail = xqjhDetailManager.get(cgjhmx.getXqjhmx());
						Xqjh xqjh = xqjhManager.get(xqjhDetail.getXqjh().getId());
						Xqjhsq xqjhsq = xqjhsqManager.get(xqjh.getXqjhsqbh());
						String xqjhsqId = xqjhsq.getId();
						String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ? and x.wzbm= ?";
						Object args[] = {xqjhsqId,wzbm};
						List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
						xqjhsqDetailList = xqjhsqManager.find(hql, args);
						for (int j = 0; j < xqjhsqDetailList.size(); j++) {
							XqjhsqDetail xqjhsqDetail = (XqjhsqDetail)xqjhsqDetailList.get(0);
							xqjhsqDetail.setStatus("已采购");
							xqjhsqDetailManager.save(xqjhsqDetail);
						}
					}
				}
				
				Cgjhgl cgjhgl = cgjhglManager.findUniqueBy("id", cglid);
				Set<Cgjhmx> cgjhmxs = cgjhgl.getCgjhmxs();
				//作为所有明细是不是全部生效
				boolean all_enable = true;
				for(Cgjhmx obj:cgjhmxs){
					if(map_id_sxsl.containsKey(obj.getId())){
						obj.setZt("1");
					}else{
						if("0".equals(obj.getZt())){
							all_enable = false;
						}
					}
				}
				
				if(all_enable){
					cgjhgl.setZt("1");
				}
				
				cgjhgl.setSxsj(new Date());
				cgjhgl.setCgjhmxs(cgjhmxs);
				cgjhglManager.save(cgjhgl);
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	@SuppressWarnings("deprecation")
	public void printCgjhgl(){
		String path=request.getSession().getServletContext().getRealPath("/jteap/wz/cgjhgl/cgjhd.xls");
		String id = request.getParameter("id");
		if(StringUtil.isNotEmpty(id)) {}
		java.io.BufferedOutputStream outs = null;
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
			Cgjhgl cgjhgl =  (Cgjhgl)this.getManager().get((Serializable)id);
			Set<Cgjhmx> mxs = cgjhgl.getCgjhmxs();
			int rowIndex=4;
			
			//编号
			HSSFCell bh = rs.getRow(1).getCell((short)1);
			bh.setCellValue(cgjhgl.getBh());
			//制定日期
			HSSFCell zdrq = rs.getRow(1).getCell((short)4);
			if(cgjhgl.getZdsj()!=null)
				zdrq.setCellValue(DateUtils.formatDate(cgjhgl.getZdsj(), "yyyy-MM-dd"));
			//生效日期
			HSSFCell sxsj = rs.getRow(1).getCell((short)6);
			if(cgjhgl.getSxsj()!=null)
				sxsj.setCellValue(DateUtils.formatDate(cgjhgl.getSxsj(), "yyyy-MM-dd"));
			//备注
			HSSFCell bz = rs.getRow(2).getCell((short)1);
			bz.setCellValue(cgjhgl.getBz());
//			bz.setEncoding(HSSFCell.ENCODING_UTF_16);
			for (Iterator<Cgjhmx> iterator = mxs.iterator(); iterator.hasNext();) {
				Cgjhmx cgjhmx = (Cgjhmx) iterator.next();
				rs.addMergedRegion(new Region(rowIndex,(short)0,rowIndex+1,(short)0));   //指定合并区域
				rs.addMergedRegion(new Region(rowIndex,(short)2,rowIndex+1,(short)2));
				rs.addMergedRegion(new Region(rowIndex,(short)3,rowIndex+1,(short)3));
				rs.addMergedRegion(new Region(rowIndex,(short)4,rowIndex+1,(short)4));
				rs.addMergedRegion(new Region(rowIndex,(short)5,rowIndex+1,(short)5));
				rs.addMergedRegion(new Region(rowIndex,(short)6,rowIndex+1,(short)6));
				rs.addMergedRegion(new Region(rowIndex,(short)7,rowIndex+1,(short)7));
				//第一列 序号
				HSSFCell ce = rs.getRow(rowIndex).createCell(0);
				HSSFCell ce1 = rs.getRow(rowIndex+1).createCell(0);
				ce.setCellStyle(cs);
				ce1.setCellStyle(cs);
				ce.setCellValue(cgjhmx.getXh());
				//第二列 物资名称规格
				HSSFCell wzmc = rs.getRow(rowIndex).getCell(1);
				wzmc.setCellStyle(cs);
				HSSFCell xhgg = rs.getRow(rowIndex+1).getCell(1);
				xhgg.setCellStyle(cs);
//				wzmc.setEncoding(HSSFCell.ENCODING_UTF_16);
//				xhgg.setEncoding(HSSFCell.ENCODING_UTF_16);
				wzmc.setCellValue(cgjhmx.getWzdagl().getWzmc());
				xhgg.setCellValue(cgjhmx.getWzdagl().getXhgg());
				//第三列 计划单价
				HSSFCell jhdj = rs.getRow(rowIndex).getCell(2);
				jhdj.setCellValue(cgjhmx.getWzdagl().getJhdj());
				rs.getRow(rowIndex).getCell(2).setCellStyle(cs);
				rs.getRow(rowIndex+1).getCell(2).setCellStyle(cs);
				//第四列 计量单位

				HSSFCell jldw = rs.getRow(rowIndex).getCell((short)3);
//				jldw.setEncoding(HSSFCell.ENCODING_UTF_16);

				jldw.setCellValue(cgjhmx.getWzdagl().getJldw());
				rs.getRow(rowIndex).getCell(3).setCellStyle(cs);
				rs.getRow(rowIndex+1).getCell(3).setCellStyle(cs);
				//第五列 采购数量
				HSSFCell cgsl = rs.getRow(rowIndex).getCell(4);
				cgsl.setCellValue(cgjhmx.getCgsl());
				rs.getRow(rowIndex).getCell(4).setCellStyle(cs);
				rs.getRow(rowIndex+1).getCell(4).setCellStyle(cs);
				//第六列 换算系数
				HSSFCell hsxs = rs.getRow(rowIndex).getCell(5);
				hsxs.setCellValue(cgjhmx.getHsxs());
				rs.getRow(rowIndex).getCell(5).setCellStyle(cs);
				rs.getRow(rowIndex+1).getCell(5).setCellStyle(cs);
				//第七列 计划到货日期
				HSSFCell jhdhrq = rs.getRow(rowIndex).createCell(6);
				HSSFCell jhdhrq1 = rs.getRow(rowIndex+1).createCell(6);
				jhdhrq.setCellValue(DateUtils.formatDate(cgjhmx.getJhdhrq(), "yyyy-MM-dd"));
				jhdhrq.setCellStyle(cs);
				jhdhrq1.setCellStyle(cs);
				//第八列 采购员

				HSSFCell cgy = rs.getRow(rowIndex).createCell((short)7);
				HSSFCell cgy1 = rs.getRow(rowIndex+1).createCell((short)7);
//				cgy.setEncoding(HSSFCell.ENCODING_UTF_16);
				cgy.setCellValue(cgjhmx.getPerson().getUserName());
				cgy.setCellStyle(cs);
				cgy1.setCellStyle(cs);
				
				rowIndex=rowIndex+2;
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			data = os.toByteArray();
			
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(("采购计划单_" + DateUtils.getDate("yyyy-MM-dd") + ".xls")
							.getBytes(), "iso-8859-1"));
			
			//开始输出
			outs = new java.io.BufferedOutputStream(response.getOutputStream());
			outs.write(data);
			outs.flush();
		}catch(Exception e){
			//e.printStackTrace();
		}finally{
			if(outs!=null){
				try {
					outs.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 删除操作，删除采购计划及明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Cgjhgl cgjhgl = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					cgjhgl = cgjhglManager.get((Serializable)cgjhKeys[i]);
					List<Cgjhmx> cgjhmx = cgjhmxManager.findBy("cgjhgl", cgjhgl);
					for(Cgjhmx mx:cgjhmx){
						cgjhmxManager.remove(mx);
					}
					//删除主表
					cgjhglManager.remove(cgjhgl);
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	/**
	 * 根据验货单，验货单明细生成采购单，采购单明细
	 * @return
	 * @throws Exception 
	 */
	public String createCgdAndMxAction() throws Exception{
		String yhdbh = request.getParameter("BH");
		Yhdgl yhdgl =yhdglManager.findYhdglByBh(yhdbh);
		String json = "";
		if(yhdgl!=null){
			Cgjhgl cgjh  = cgjhglManager.saveCgjhByYhd(yhdgl);
			List<Yhdmx> yhdmxList = (List<Yhdmx>) yhdmxManager.findYhdmxByYhdId(yhdgl.getId());
			cgjhmxManager.saveCgjhMxByYhdmx(yhdmxList, cgjh);
			json="{success:true,msg:'自由入库成功'}";
		}else{
			json="{success:false,msg:'自由入库失败'}";
		}
		try {
			this.outputJson(json);
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return cgjhglManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"zdsj",
			"sxsj",
			"cgjhmxs","xqjhDetail", "xqjh", "xqjhbh",
			"bz",
			"zt",
			"jhy",
			"person",
			"id",
			"userName",
			"userLoginName",
			"time",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"bh",
			"zdsj",
			"sxsj",
			"bz",
			"zt",
			"jhy",
			"id",
			"time",
		""};
	}
}
