package com.jteap.jhtj.bbsj.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.derby.iapi.services.io.ArrayInputStream;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbsj.manager.BbSjTsManager;
import com.jteap.jhtj.bbsj.model.BbSjTs;
import com.jteap.jhtj.bbsjydy.manager.BbSjzdManager;
import com.jteap.jhtj.bbsjydy.model.BbSjzd;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
@SuppressWarnings({ "unchecked", "serial" })
public class BbSjTsAction extends AbstractTreeAction<BbSjTs> {
	private BbSjTsManager bbSjManager;
	private BbzcManager bbzcManager;
	private BbIndexManager bbIndexManager;
	private PersonManager personManager;
	private BbSjzdManager bbSjzdManager;
	
	private File excelFile;
	
	public BbIndexManager getBbIndexManager() {
		return bbIndexManager;
	}

	public void setBbIndexManager(BbIndexManager bbIndexManager) {
		this.bbIndexManager = bbIndexManager;
	}

	public BbzcManager getBbzcManager() {
		return bbzcManager;
	}

	public void setBbzcManager(BbzcManager bbzcManager) {
		this.bbzcManager = bbzcManager;
	}

	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		//得到所有父节点的集合
		List<Bbzc> connList=this.bbzcManager.getParentBbzcList();
		Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	connList=this.bbzcManager.filterBbzcListByQx(person, connList);
		//}
		StringBuffer result=new StringBuffer();
		for(Bbzc bbzc:connList){
			result.append(this.bbzcManager.getChildsBbzc(bbzc, this.isCurrentRootUser(), person));
		}
		if(result.length()>0){
			result.deleteCharAt(result.length()-1);
		}
		//System.out.println(result.toString());
		//输出
		this.outputJson("["+result.toString()+"]");
		return NONE;
	}
	
	
	/**
	 * 读取Excel大字段
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readExcelBlobAction() throws IOException {
		String id = request.getParameter("indexid");
		BbIndex index = this.bbIndexManager.get(id);
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=excel.xls");
		OutputStream outStream = response.getOutputStream();
		ArrayInputStream ais = new ArrayInputStream(index.getBbmb());
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
	 *描述：查找报表模板中的模板是否存在数据，查找数据填数中是否有数据
	 *时间：2010-4-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String isExitsDataAction() throws Exception{
		String indexid=request.getParameter("indexid");
		//模板
		BbIndex index = this.bbIndexManager.get(indexid);
		//填数
		BbSjTs ts=this.bbSjManager.findUniqueBy("bbindexid", indexid);
		boolean isTs=false;
		String json="";
		if(ts!=null){
			isTs=true;
			json=JSONUtil.objectToJson(ts,new String[]{"excelDataItemXml"});
			System.out.println(json);
		}
		if(index.getBbmb()==null){
			outputJson("{success:false,isTs:"+isTs+",data:["+json+"]}");
		}else{
			outputJson("{success:true,isTs:"+isTs+",data:["+json+"]}");
		}
		return NONE;
	}
	
	/**
	 * 保存或更新
	 * @return
	 * @throws BusinessException
	 */
	public String saveOrUpdateExcelCFormAction() throws BusinessException {
		String indexid=request.getParameter("id");//模板ID
		String excelDataItemXml=request.getParameter("excelDataItemXml");//模板ID
		String editableInputs=request.getParameter("editableInputs");//模板ID
		BbIndex index=this.bbIndexManager.get(indexid);
		try {
			//新模板
			index.setBbmb(FileUtils.readFileToByteArray(excelFile));
			
			//数据填数
			BbSjTs ts=this.bbSjManager.findUniqueBy("bbindexid", indexid);
			if(ts==null){
				ts=new BbSjTs();
			}
			ts.setBbindexid(indexid);
			ts.setExcelDataItemXml(excelDataItemXml);
			ts.setEditableInputs(editableInputs);
			this.bbIndexManager.save(index);
			this.bbSjManager.save(ts);
		} catch (IOException e) {
			throw new BusinessException("保存Excel表单时出现异常:SDS");
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述:根据定义的数据源查找数据字段列表
	 * 时间:Oct 29, 2010
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String showSjzdListAction() throws Exception{
		String bbioid=request.getParameter("bbioid");
		List<BbSjzd> sjzdList=bbSjzdManager.getSjzdListByBBioid(bbioid);
		String json = JSONUtil.listToJson(sjzdList, new String[]{"id","fname","cfname","ftype","forder"});
		json = "{totalCount:'" + sjzdList.size() + "',list:"
				+ json + "}";
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
		return bbSjManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public BbSjTsManager getBbSjManager() {
		return bbSjManager;
	}

	public void setBbSjManager(BbSjTsManager bbSjManager) {
		this.bbSjManager = bbSjManager;
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

	public BbSjzdManager getBbSjzdManager() {
		return bbSjzdManager;
	}

	public void setBbSjzdManager(BbSjzdManager bbSjzdManager) {
		this.bbSjzdManager = bbSjzdManager;
	}
}
