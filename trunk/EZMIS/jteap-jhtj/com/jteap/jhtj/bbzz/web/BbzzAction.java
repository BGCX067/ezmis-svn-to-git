package com.jteap.jhtj.bbzz.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.services.io.ArrayInputStream;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbsj.model.BbSjTs;
import com.jteap.jhtj.bbsjydy.manager.BbIOManager;
import com.jteap.jhtj.bbsjydy.model.BbIO;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.jhtj.bbzz.manager.BbzzManager;
import com.jteap.jhtj.bbzz.model.Bbzz;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
@SuppressWarnings({ "unchecked", "serial" })
public class BbzzAction extends AbstractTreeAction {
	private BbzzManager bbzzManager;
	private BbzcManager bbzcManager;
	private BbIndexManager bbIndexManager;
	private BbIOManager bbIOManager;
	private PersonManager personManager;
	private File excelFile;
	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		List<Bbzc> connList=this.bbzcManager.getBbzcList();
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	connList=this.bbzcManager.filterBbzcListByQx(person, connList);
		//}
		StringBuffer result=new StringBuffer();
		for(Bbzc bbzc:connList){
			StringBuffer children=new StringBuffer();
			List<BbIndex> indexList=this.bbIndexManager.findBbindexListByFlid(bbzc.getId());
			//if(!this.isCurrentRootUser()){
			//	indexList=this.bbzcManager.filterBbIndexListByQx(person, indexList);
			//}
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
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String bbindexid=request.getParameter("bbindexid");
		HqlUtil.addCondition(hql, "bbindexid",bbindexid);
		//报表名
		String cname=request.getParameter("cname");
		cname=StringUtil.isoToUTF8(cname);
		if(StringUtils.isNotEmpty(cname)){
			HqlUtil.addCondition(hql, "cname",cname,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		//生成状态
		String status=request.getParameter("status");
		if(StringUtils.isNotEmpty(status)){
			HqlUtil.addCondition(hql, "status",status,HqlUtil.LOGIC_AND);
		}
		//制作日期
		String zbrq=request.getParameter("zbrq");
		if(StringUtils.isNotEmpty(zbrq)){
			HqlUtil.addWholeCondition(hql, "to_char(obj.zbrq,'yyyy-mm-dd')='"+zbrq+"'");
		}
		HqlUtil.addOrder(hql, "sortno", "desc");
	}
	
	/**
	 * 
	 *描述：查找接口并初始化接口
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findInterfaceAction() throws Exception{
		String bbindexid=request.getParameter("bbindexid");
		if(StringUtils.isNotEmpty(bbindexid)){
			//取出报表模板用到的bbio
			BbSjTs ts=this.bbzzManager.findUniqueBy(BbSjTs.class, "bbindexid", bbindexid);
			if(ts!=null){
				String xml=ts.getExcelDataItemXml();
				List<BbIO> ioList=this.bbzzManager.getBbioListByXml(xml);
				//存放接口
				Map<String, String> ifMap=new HashMap<String, String>();
				for(BbIO io:ioList){
					String sql=io.getSqlstr();
					this.bbzzManager.findInterfaceBySql(ifMap, sql);
				}
				//有接口的情况下
				if(ifMap.size()>0){
					String data=this.bbzzManager.InterfaceTransform(ifMap);
					outputJson("{success:true,data:'"+data+"'}");
				}else{
					outputJson("{success:false}");
				}
			}else{
				outputJson("{success:false}");
			}
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：报表制作
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String generateBbAction() throws Exception{
		String bbindexid=request.getParameter("bbindexid");
		String sqlValue=request.getParameter("sqlValue");
		if(StringUtils.isNotEmpty(bbindexid)&&StringUtils.isNotEmpty(sqlValue)){
			String json=this.bbzzManager.generateBb(bbindexid, sqlValue);
			outputJson("{success:true,data:["+json+"]}");
			//System.out.println(json);
		}else{
			outputJson("{success:true,data:[]}");
		}
		return NONE;
	}
	/**
	 * 制作并返回报表到综合查询页面
	 * @return
	 * @throws Exception 
	 */
	public String generateBbByZhcxAction() throws Exception{
		String bbbm=request.getParameter("bbbm");
		BbIndex bbindex = bbIndexManager.findUniqueBy("bbbm",bbbm);
		String curDt=request.getParameter("curDt");//"RI,28!NIAN,2011!YUE,8"
		String[] dt = null;
		String curDate = "";
	
		if(bbindex!=null){
			if(StringUtil.isEmpty(curDt)){
				//查询报表制作 返回当前排序 倒序第一的报表
				StringBuffer hqlBf = new StringBuffer("from Bbzz obj ");
				HqlUtil.addCondition(hqlBf, "bbindexid",bbindex.getId());
//				HqlUtil.addCondition(hqlBf, "status","1");
				HqlUtil.addOrder(hqlBf, "sortno", "desc");
				List<Bbzz> bbzzList = bbzzManager.find(hqlBf.toString());
				if(bbzzList.size()>0){
					Bbzz bbzz = bbzzList.get(0);
					curDate = bbzz.getKey().replace("_", "-");
				}
			}else{
				curDate = curDt;
			}
			dt = curDate.split("-");
			curDt = "RI,"+dt[2]+"!NIAN,"+dt[0]+"!YUE,"+dt[1];
			String json=this.bbzzManager.generateBb(bbindex.getId(), curDt);
			outputJson("{success:true,data:["+json+"],curDate:'"+curDate+"'}");
		}
		return NONE;
	}
	
	@Override
	public String saveUpdateAction() throws BusinessException {
		String bbindexid=request.getParameter("bbindexid");
		String sqlValue=request.getParameter("sqlValue");
		String id=request.getParameter("id");
		
		try {
			Bbzz bbzz=null;
			if(StringUtils.isEmpty(id)){
				BbIndex index=this.bbIndexManager.get(bbindexid);
				String cname=index.getBbmc();
				String key=this.bbzzManager.getInterfaceValue(sqlValue);
				if(StringUtils.isNotEmpty(key)){
					cname=cname+"_"+key;
				}
				bbzz=new Bbzz();	
				
				bbzz.setCname(cname);
				bbzz.setKey(key);
				bbzz.setBbindexid(bbindexid);
				Person person=this.personManager.getCurrentPerson(sessionAttrs);
				bbzz.setZzrname(person.getUserLoginName());
				bbzz.setZzrcname(person.getUserName());
				bbzz.setZbrq(new Date());
				bbzz.setStatus("2");
				bbzz.setSortno(this.bbzzManager.getSortno());
				
				//如果有数据则表示是重新制作
				boolean isExsit=this.bbzzManager.findBbzzIsExistByBbindexidAndKey(bbindexid, key);
				if(!isExsit){
					//删除原来制作的
					this.bbzzManager.removeBatchInHql(Bbzz.class, "bbindexid='"+bbindexid+"' and key='"+key+"'");
				}
			}else{
				bbzz=this.bbzzManager.get(id);
			}
			bbzz.setBbnr(FileUtils.readFileToByteArray(excelFile));
			this.bbzzManager.save(bbzz);
			//新模板
		} catch (IOException e) {
			throw new BusinessException("保存Excel表单时出现异常:SDS");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：查看报表数据是否存在
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String isExistAction() throws Exception{
		String bbindexid=request.getParameter("bbindexid");
		String sqlValue=request.getParameter("sqlValue");
		String key=this.bbzzManager.getInterfaceValue(sqlValue);
		boolean res=this.bbzzManager.findBbzzIsExistByBbindexidAndKey(bbindexid, key);
		outputJson("{success:true,res:"+res+"}");
		return NONE;
	}
	
	/**
	 * 查看报表是否已经生成
	 * @return
	 * @throws Exception
	 */
	public String isScStateAction() throws Exception{
		String bbindexid=request.getParameter("bbindexid");
		String sqlValue=request.getParameter("sqlValue");
		String key=this.bbzzManager.getInterfaceValue(sqlValue);
		boolean res=this.bbzzManager.findBbzzIsStateByBbindexidAndKey(bbindexid, key);
		outputJson("{success:true,res:"+res+"}");
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
	 *描述：更新报表状态
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String updateStatusAction() throws Exception{
		String id=request.getParameter("id");
		String status=request.getParameter("status");
		if(StringUtils.isNotEmpty(id)){
			Bbzz bbzz=this.bbzzManager.get(id);
			bbzz.setStatus(status);
			this.bbzzManager.save(bbzz);
			outputJson("{success:true}");
		}else{
			outputJson("{success:false,msg:'更新状态失败!'}");
		}
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
		return bbzzManager;
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
	public BbIOManager getBbIOManager() {
		return bbIOManager;
	}

	public void setBbIOManager(BbIOManager bbIOManager) {
		this.bbIOManager = bbIOManager;
	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
}
