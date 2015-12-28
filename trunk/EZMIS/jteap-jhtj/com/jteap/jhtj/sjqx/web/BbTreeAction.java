package com.jteap.jhtj.sjqx.web;

import java.util.Collection;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.jhtj.sjqx.model.Sjqx;
@SuppressWarnings({ "unchecked", "serial" })
public class BbTreeAction extends AbstractTreeAction {
	private BbzcManager bbzcManager;
	private BbIndexManager bbIndexManager;
	private SjqxManager sjqxManager;
	
	
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


	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		String roleid=request.getParameter("roleid");
		List<Sjqx> qxList=this.sjqxManager.findBy("roleid", roleid);
		
		List<Bbzc> connList=this.bbzcManager.getBbzcList();
		StringBuffer result=new StringBuffer();
		for(Bbzc bbzc:connList){
			StringBuffer children=new StringBuffer();
			
			boolean checked=false;
			for(Sjqx sjqx:qxList){
				if(bbzc.getId().equals(sjqx.getQxid())){
					checked=true;
					break;
				}
			}
			List<BbIndex> indexList=this.bbIndexManager.findBbindexListByFlid(bbzc.getId());
			if(indexList.size()>0){
				for(BbIndex index:indexList){
					boolean indexChecked=false;
					for(Sjqx sjqx:qxList){
						if(index.getId().equals(sjqx.getQxid())){
							indexChecked=true;
							break;
						}
					}
					children.append("{\"id\":\""+index.getId()+"\",\"text\":\""+index.getBbmc()+"\",\"expanded\":false,\"leaf\":true,\"type\":\"2\",\"ccCheck\":true,\"checked\":"+indexChecked+"},");
				}
				if(children.toString().length()>0){
					children.deleteCharAt(children.length()-1);
				}
				String json="{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"ccCheck\":true,\"checked\":"+checked+",\"children\":["+children.toString()+"]}";
				result.append(json+",");
			}else{
				
				String json="{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"ccCheck\":true,\"checked\":"+checked+",\"children\":[]}";
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
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}


	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}


	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}

}
