package com.jteap.jhtj.bbcx.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.services.io.ArrayInputStream;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbcx.manager.BbcxManager;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.jhtj.bbzz.manager.BbzzManager;
import com.jteap.jhtj.bbzz.model.Bbzz;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjwh.manager.TjSjwhManager;
import com.jteap.jhtj.sjwh.model.KeyModel;
@SuppressWarnings({ "unchecked", "serial" })
public class BbcxAction extends AbstractTreeAction { 
	private BbzzManager bbzzManager;
	private BbzcManager bbzcManager; 
	private BbIndexManager bbIndexManager;
	private BbcxManager bbcxManager;
	private TjSjwhManager tjSjwhManager;
	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		List<Bbzc> connList=this.bbzcManager.getBbzcList();
		StringBuffer result=new StringBuffer();
		for(Bbzc bbzc:connList){
			StringBuffer children=new StringBuffer();
			List<BbIndex> indexList=this.bbIndexManager.findBbindexListByFlid(bbzc.getId());
			if(indexList.size()>0){
				for(BbIndex index:indexList){
					children.append("{\"id\":\""+index.getId()+"\",\"text\":\""+index.getBbmc()+"\",\"expanded\":false,\"leaf\":true,\"type\":\"2\"},");
				}
				if(children.toString().length()>0){
					children.deleteCharAt(children.length()-1);
				}
				String json="{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"children\":["+children.toString()+"]}";
				result.append(json+",");
			}else{
				String json="{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"children\":[]}";
				result.append(json+",");
			}
		}
		if(result.length()>0){
			result.deleteCharAt(result.length()-1);
		}
		//输出
		this.outputJson("["+result.toString()+"]");
		return NONE;
	}
	
	/**
	 * 暂时写法
	 */
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String bbindexid=request.getParameter("bbindexid");
		String curDate=request.getParameter("curDate");
		//从综合查询入口访问报表
		String bblx = request.getParameter("bblx");
		if(StringUtils.isNotEmpty(curDate)){
			String[] dates=curDate.split("!");
			StringBuffer keys=new StringBuffer();
			for(String key:dates){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					keys.append(nameAndValue[1]+"_");
				}
			}
			if(!keys.toString().equals("")){
				keys.deleteCharAt(keys.length()-1);
			}
			HqlUtil.addCondition(hql,"key", keys.toString(),HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		//从综合查询入口中访问报表
		if(StringUtils.isNotEmpty(bblx)){
			List<BbIndex> bbindexList = bbIndexManager.find("from BbIndex t where t.bbbm = ?",bblx);
			if(bbindexList.size()>0){
				BbIndex bbindex = bbindexList.get(0);
				bbindexid = bbindex.getId();
			}
		} 
		HqlUtil.addCondition(hql, "bbindexid",bbindexid);
		HqlUtil.addCondition(hql, "status","1");
		HqlUtil.addOrder(hql, "sortno", "desc");
	}
	/**
	 * 从综合查询模块 查询指定类型的报表
	 * @return
	 * @throws Exception
	 */
	public String bbcxByZhcxAction() throws Exception{
		StringBuffer hqlBf = new StringBuffer("from Bbzz obj ");
		String bbindexid = "";
		String bblx = request.getParameter("bblx");
		//报表时间
		String curDt = request.getParameter("curDt");
		//从综合查询入口中访问报表
		if(StringUtils.isNotEmpty(bblx)){
			List<BbIndex> bbindexList = bbIndexManager.find("from BbIndex t where t.bbbm = ?",bblx);
			if(bbindexList.size()>0){
				BbIndex bbindex = bbindexList.get(0);
				bbindexid = bbindex.getId();
			}
			HqlUtil.addCondition(hqlBf, "bbindexid",bbindexid);
		} 
		//时间
		if(StringUtils.isNotEmpty(curDt)){
			curDt = curDt.replace("-", "_");
			HqlUtil.addCondition(hqlBf, "key",curDt);
		} 
		HqlUtil.addCondition(hqlBf, "status","1");
		HqlUtil.addOrder(hqlBf, "sortno", "desc");
		List<Bbzz> bbzzList = bbzzManager.find(hqlBf.toString());
		if(bbzzList.size()>0){
			Bbzz bbzz = bbzzList.get(0);
			String id = bbzz.getId();
			String curDate = bbzz.getKey().replace("_", "-");
			this.outputJson("{success:true,bbid:'"+id+"',curDate:'"+curDate+"'}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	/**
	 * 读取Excel大字段
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readExcelBlobAction() throws IOException {
		String id = request.getParameter("id");
		Bbzz bbzz = this.bbzzManager.get(id);
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=excel.xls");
		OutputStream outStream = response.getOutputStream();
		ArrayInputStream ais = new ArrayInputStream(bbzz.getBbnr());
		byte[] buf = new byte[1024];
		int bytes = 0;
		while ((bytes = ais.read(buf)) != -1)
			outStream.write(buf, 0, bytes);
		ais.close();
		outStream.close();
		return NONE;
	}
	
	
	/**
	 * 
	 *描述：动态生成查询面板的条件
	 *时间：2010-4-20
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String dynaAddSearPanelAction() throws Exception{
		StringBuffer findWhere=new StringBuffer();
		StringBuffer defaultValue=new StringBuffer();

		List<KeyModel> year=this.tjSjwhManager.getYearList();
		String yearJson=JSONUtil.listToJson(year,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.NIAN+":["+yearJson+"],");

		List<KeyModel> month=this.tjSjwhManager.getMonthList();
		String monthJson=JSONUtil.listToJson(month,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.YUE+":["+monthJson+"]");
		
		String defaultYear=DateUtils.getDate("yyyy");
		defaultValue.append(TjInterFace.NIAN+"value:'"+defaultYear+"',");
		String defaultMonth=DateUtils.getDate("MM");
		defaultValue.append(TjInterFace.YUE+"value:'"+(defaultMonth.charAt(0)=='0'?defaultMonth.substring(1):defaultMonth)+"'");

		String json="{success:true,"+findWhere.toString()+","+defaultValue.toString()+"}";
		outputJson(json);
		return NONE;
	}
	
	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return null;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return null;
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return null;
	}

	@Override
	public HibernateEntityDao getManager() {
		return this.bbcxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","cname","key","zbrq","zzrname","zzrcname","status","zbrq","time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","cname","key","zbrq","zzrname","zzrcname","status","zbrq","time"};
	}

	public BbzzManager getBbzzManager() {
		return bbzzManager;
	}

	public void setBbzzManager(BbzzManager bbzzManager) {
		this.bbzzManager = bbzzManager;
	}

	public BbzcManager getBbzcManager() {
		return bbzcManager;
	}

	public void setBbzcManager(BbzcManager bbzcManager) {
		this.bbzcManager = bbzcManager;
	}

	public BbIndexManager getBbIndexManager() {
		return bbIndexManager;
	}

	public void setBbIndexManager(BbIndexManager bbIndexManager) {
		this.bbIndexManager = bbIndexManager;
	}

	public BbcxManager getBbcxManager() {
		return bbcxManager;
	}

	public void setBbcxManager(BbcxManager bbcxManager) {
		this.bbcxManager = bbcxManager;
	}

	public TjSjwhManager getTjSjwhManager() {
		return tjSjwhManager;
	}

	public void setTjSjwhManager(TjSjwhManager tjSjwhManager) {
		this.tjSjwhManager = tjSjwhManager;
	}


}
