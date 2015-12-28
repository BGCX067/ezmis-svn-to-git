package com.jteap.jhtj.bbzc.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.system.person.manager.PersonManager;
@SuppressWarnings({ "unchecked", "serial" })
public class BbzcAction extends AbstractTreeAction {
	private BbzcManager bbzcManager;
	private PersonManager personManager;
	
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		String parentId=request.getParameter("parentId");
		if(StringUtils.isNotEmpty(parentId)){
			Bbzc parentBbzc=this.bbzcManager.get(parentId);
			Bbzc curBbzc=(Bbzc)obj;
			parentBbzc.getChildBbzc().add(curBbzc);
			curBbzc.setParentBbzc(parentBbzc);
			this.bbzcManager.save(parentBbzc);
		}
	}
	
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		String result=null;
		Bbzc bbzc=(Bbzc)node;
		if(bbzc.getChildBbzc().size()>0){
			result="请先删除子节点数据!";
			return result;
		}
		BbIndex index=this.bbzcManager.findUniqueBy(BbIndex.class,"flid",bbzc.getId());
		if(index!=null){
			result="该分类已有报表数据,请先删除报表数据!";
		}
		return result;
	}
	
	/**
	 * 
	 *描述：验证连接名称是否唯一
	 *时间：2010-4-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String validateNameUniqueAction() throws Exception {
		String flbm = request.getParameter("flbm");//分类ID
		Bbzc bbzc=this.bbzcManager.findUniqueBy("flbm", flbm);
		if(bbzc!=null){
			outputJson("{unique:false}");
		}else{
			outputJson("{unique:true}");
		}
		//outputJson("{unique:true}");
		return NONE;
	}
	
	@Override
	protected Collection getChildren(Object bean) {
		Bbzc bbzc=(Bbzc)bean;
		Set<Bbzc> zcSet= bbzc.getChildBbzc();
		List<Bbzc> zcList=new ArrayList<Bbzc>(zcSet);
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	zcList=this.bbzcManager.filterBbzcListByQx(person, zcList);
		//}
		return zcList;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "flmc";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		List<Bbzc> zcList=bbzcManager.getRootList();
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	zcList=this.bbzcManager.filterBbzcListByQx(person, zcList);
		//}
		return zcList;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "flmc";
	}

	@Override
	public HibernateEntityDao getManager() {
		return bbzcManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","flbm","flmc","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","flbm","flmc","sortno"};
	}

	public BbzcManager getBbzcManager() {
		return bbzcManager;
	}

	public void setBbzcManager(BbzcManager bbzcManager) {
		this.bbzcManager = bbzcManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

}
