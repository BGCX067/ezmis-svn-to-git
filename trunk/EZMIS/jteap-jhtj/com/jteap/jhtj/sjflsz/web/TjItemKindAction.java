package com.jteap.jhtj.sjflsz.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjKndJZ;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.system.dict.model.DictCatalog;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
@SuppressWarnings({ "unchecked", "serial" })
public class TjItemKindAction extends AbstractTreeAction<TjItemKind> {
	private TjItemKindManager tjItemKindManager;
	private SjqxManager sjqxManager;
	private PersonManager personManager;
	
	@Override
	protected Collection getChildren(Object bean) {
		TjItemKind kind=(TjItemKind)bean;
		Set<TjItemKind> set=kind.getChildKind();
		List<TjItemKind> childList=new ArrayList<TjItemKind>();
		childList.addAll(set);
		Person person = personManager.getCurrentPerson(sessionAttrs);
		if(!this.isCurrentRootUser()){
			childList=this.tjItemKindManager.filterKindListByQx(person, childList);
		}
		return childList;
	}
	
	/**
	 * 
	 *描述：根据唯一名称查找数据字典数据
	 *时间：2010-4-8
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDictAction() throws Exception{
		String uniqueName=request.getParameter("uniqueName");
		if(StringUtils.isNotEmpty(uniqueName)){
			StringBuffer result=new StringBuffer();
			String[] uniqueNames=uniqueName.split(",");
			for(int i=0;i<uniqueNames.length;i++){
				DictCatalog dc=this.tjItemKindManager.findUniqueBy(DictCatalog.class,"uniqueName",uniqueNames[i]);
				String json = JSONUtil.objectToJson(dc, new String[]{"id","uniqueName","dicts","key","value"});
				result.append(json+",");
			}
			if(!"".equals(result.toString())){
				result.deleteCharAt(result.length()-1);
			}
			outputJson("{success:true,list:["+result.toString()+"]}");
			//System.out.println(result.toString());
		}
		return NONE;
	}
	
	@Override
	public String saveUpdateAction() throws BusinessException {
		String id=request.getParameter("id");
		String kid=request.getParameter("kid");
		String kname=request.getParameter("kname");
		String flflag=request.getParameter("flflag");
		String sortno=request.getParameter("sortno");
		String jzcode=request.getParameter("jzcode");
		String parentId=request.getParameter("parentId");
		TjItemKind kind;
		if(StringUtils.isNotEmpty(id)){
			kind=this.tjItemKindManager.findUniqueBy("id", id);
			boolean isModify=this.tjItemKindManager.findTjInfoItemByKid(kind.getKid());
			if(!isModify){
				kind.setKname(kname);
				kind.setSortno(new Long(sortno));
				//查询以前是否存在机组，如果存在就删除
				List<TjKndJZ> jzList=this.tjItemKindManager.getTjKndJZByKid(kind.getKid());
				if(jzList.size()>0){
					this.tjItemKindManager.removeBatchInHql(TjKndJZ.class, "kid='"+kind.getKid()+"'");
					
				}
			}
		}else{
			kind=new TjItemKind();
			kind.setKid(kid);
			kind.setKname(kname);
			kind.setFlflag(new Long(flflag));
			kind.setSortno(new Long(sortno));
			if(StringUtils.isNotEmpty(parentId)){
				TjItemKind parentKind=this.tjItemKindManager.findUniqueBy("id", parentId);
				parentKind.getChildKind().add(kind);
				kind.setParentKind(parentKind);
				this.tjItemKindManager.save(parentKind);
			}
		}
		this.tjItemKindManager.save(kind);
		if(StringUtils.isNotEmpty(jzcode)){
			this.tjItemKindManager.saveJZs(jzcode, kind.getKid());
		}
		try {
			outputJson("{success:true}");
		} catch (Exception e) {
		}
		return NONE;
	}
	
	@Override
	public String showUpdateAction() throws Exception {
		String id=request.getParameter("id");
		TjItemKind kind=this.tjItemKindManager.get(id);
		if(kind!=null){
			String json="\"id\":\""+kind.getId()+"\",\"kid\":\""+kind.getKid()+"\",\"kname\":\""+kind.getKname()+"\",\"flflag\":\""+kind.getFlflag()+"\",\"sortno\":\""+kind.getSortno()+"\"";
			String jzcode=this.tjItemKindManager.getCheckJZsByKid(kind.getKid());
			if(StringUtils.isNotEmpty(jzcode)){
				json=json+",jzcode:'"+jzcode+"'";
			}
			System.out.println("{success:true,data:[{"+json+"}]}");
			outputJson("{success:true,data:[{"+json+"}]}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	@Override
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				TjItemKind kind=(TjItemKind) obj;
				map.put("leaf", kind.getChildKind().size()>0?false:true);
				map.put("expanded", true);
				map.put("flflag", kind.getFlflag());
				map.put("kid", kind.getKid());
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		List<TjItemKind> list = (List)roots;
		for(TjItemKind kind:list){
			for(TjItemKind k:kind.getChildKind()){
				System.out.println(k.getKname());
			}
		}
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		System.out.println(result.toString());
		//输出
		this.outputJson(result.toString());
		return NONE;
	}
	
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		String result=null;
		TjItemKind kind=(TjItemKind)node;
		boolean isDel=this.tjItemKindManager.findTjInfoItemByKid(kind.getKid());
		if(!isDel){
			result="该分类已经使用,删除失败!";
		}
		return result;
	}
	
	/**
	 * 
	 *描述：复位状态
	 *时间：2010-4-9
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String ghostAction() throws Exception{
		String kid=request.getParameter("kid");
		boolean isGhost=this.tjItemKindManager.ghostState(kid);
		outputJson("{success:"+isGhost+"}");
		return NONE;
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
		String kid = request.getParameter("kid");//分类ID
		TjItemKind kind=this.tjItemKindManager.findUniqueBy("kid", kid);
		if(kind!=null){
			outputJson("{unique:false}");
		}else{
			outputJson("{unique:true}");
		}
		//outputJson("{unique:true}");
		return NONE;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		List<TjItemKind> list=tjItemKindManager.getRootList();
//		Person person = personManager.getCurrentPerson(sessionAttrs);
//		if(!this.isCurrentRootUser()){
//			list=this.tjItemKindManager.filterKindListByQx(person, list);
//			System.out.println(list.size());
//		}
		return list;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	public HibernateEntityDao getManager() {
		return tjItemKindManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	public TjItemKindManager getTjItemKindManager() {
		return tjItemKindManager;
	}

	public void setTjItemKindManager(TjItemKindManager tjItemKindManager) {
		this.tjItemKindManager = tjItemKindManager;
	}

	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}

	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

}
