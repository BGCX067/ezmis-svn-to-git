package com.jteap.system.resource.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.person.model.P2Res;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Module;
import com.jteap.system.resource.model.Operation;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.R2R;
/**
 * 模块管理器
 * 
 * @author tantyou
 * @date 2008-1-30
 */
@SuppressWarnings("unchecked")
public class ModuleManager extends HibernateEntityDao<Module>{
	
	/**
	 * 获取指定模块下的所有的操作
	 * Ugen
	 * 2008-1-22
	 * @param module
	 * @return
	 */
	public Collection<Operation> findAllOperationOfTheModule(Module module){
		
		String hql="from Resource as r where r.type=? and r.parentRes.id=? order by r.sortNo";
		Object[] values=new String[]{Resource.RES_TYPE_OPERATION,module.getId().toString()};
		return this.find(hql,values);
	}

	/**
	 * 获取模块
	 * 2008-1-16
	 * Ugen
	 * @param parentModule TODO
	 * @return
	 */
	public Collection<Module> findModule(String parentId) {
		String hsql = "from Module as m";
		Object[] args=null;
		if(parentId==null){
			hsql+=" where m.parentRes=null";
		}else{
			hsql+=" where m.parentRes.id=?";
			args=new String[]{parentId};
		}
		hsql+=" order by m.sortNo";
		return this.find(hsql,args);
	}
//	/**
//	 * 获取模块，只找可见的模块
//	 * 2008-1-16
//	 * Ugen
//	 * @param parentModule TODO
//	 * @return
//	 */
//	public Collection<Module> findModuleWithVisible(Module parentModule) {
//		String hsql = "from Module as m";
//		Object[] args=null;
//		if(parentModule==null){
//			hsql+=" where m.parentRes=null";
//		}else{
//			hsql+=" where m.parentRes.id=? and m.visiabled='1'";
//			args=new String[]{parentModule.getId().toString()};
//		}
//		hsql+=" order by m.sortNo";
//		return this.find(hsql,args);
//	}
//	
//	/**
//	 * 获取模块，取得拥有权限的模块
//	 * @param parentModule		父亲资源
//	 * @param permResources		拥有权限的所有资源
//	 * @return
//	 */
//	public Collection<Module> findModuleWithVisible(Module parentModule,Collection<Resource> permResources){
//		Collection<Module> tmp=findModuleWithVisible(parentModule);
//		Collection<Module> results=new ArrayList<Module>();
//		Object res[]=permResources.toArray();
//		for (Module module : tmp) {
//			if(ArrayUtils.isExist(res, module)>=0){
//				results.add(module);
//			}
//		}
//		return results;
//	}
	/**
	 * 根据资源简称，取得该模块的指定资源
	 * @param module  模块
	 * @param opResName	资源名称
	 * @return
	 */
	public Operation findOperationByShortName(Module module,String sn) {
		for (Resource op : module.getChildRes()) {
			if(op instanceof Operation){
				Operation opx=(Operation) op;
				if(opx.getSn().equals(sn))
					return opx;
			}
		}
		return null;
	}

	/**
	 * 根据用户取得当前用户权限的模块,这些模块是分级进行获取的
	 * @param person
	 * @param parentId
	 * @return
	 */
	public Collection findModuleByParentWithPerm(Person person, String parentId) {
		String hql;
		Collection<Module> modules;
		if(person.isRootPerson()){
			hql="from Module m where "+(StringUtil.isEmpty(parentId)?"m.parentRes is null":"m.parentRes.id='"+parentId+"'") + " and m.visiabled = '1'  order by m.sortNo";
			modules = this.find(hql);
			return modules;
		}else{
			Map<String,Module> modulesMap = new HashMap<String,Module>();
			//通过角色找到对应的资源
			findModuleByParentWithPermInR2R(person,parentId,modulesMap);
			//通过用户找到对应的资源
			findModuleByParentWithPermInP2Res(person,parentId,modulesMap);
			List<Module> moduleList = new ArrayList<Module>();
			moduleList.addAll(modulesMap.values());
			//排序
			Collections.sort(moduleList,new Comparator<Module>(){
				public int compare(Module o1, Module o2) {
					if(o1.getSortNo()>o2.getSortNo())
						return 1;
					else if(o1.getSortNo()<o2.getSortNo())
						return -1;
					else
						return 0;
				}});
			return moduleList;
		}
	}
	
	/**
	 * 根据角色和资源的关联查询当前用户的权限资源，并将资源放入modules集合中
	 * @param person
	 * @param parentId
	 * @param modules
	 */
	private void findModuleByParentWithPermInR2R(Person person,
			String parentId, Map<String,Module> modulesMap) {
		String hql="from R2R o where "+(StringUtil.isEmpty(parentId)?"o.resource.parentRes is null":"o.resource.parentRes.id='"+parentId+"'");
		StringBuffer sb = new StringBuffer();
		Set<P2Role> p2rSet = person.getRoles();
		for (P2Role p2r : p2rSet) {
			sb.append(" o.role.id = '"+p2r.getRole().getId()+"' or");
		}
		if(sb.length()>0){
			sb.delete(sb.length()-3, sb.length());
			hql = hql + " and ("+sb.toString()+")";
			List<R2R> r2rList = this.find(hql);
			StringBuffer sb2 = new StringBuffer();
			for (R2R r2r : r2rList) {
				sb2.append(" id ='"+r2r.getResource().getId().toString()+"' or");
			}
			if(sb2.length()>0){
				sb2.delete(sb2.length()-3,sb2.length());
				hql = "from Module m where ("+sb2.toString()+") and m.visiabled = '1'";
				List<Module> modules = this.find(hql);
				for (Module module : modules) {
					modulesMap.put(module.getId().toString(),module);
				}
			}
		}
		
	}
	/**
	 * 根据指定用户和资源的关联查询当前用户的权限资源,放入modules集合中
	 * @param person
	 * @param parentId
	 * @param modules
	 */
	private void findModuleByParentWithPermInP2Res(Person person,String parentId,Map<String,Module> modulesMap){
		String hql="";
		List<P2Res> p2ResList=null;
		if(StringUtils.isNotEmpty(parentId)){
			hql= "from P2Res o where o.person.id='"+person.getId().toString()+"' and o.resource.parentRes.id = '"+parentId+"'";
			p2ResList = this.find(hql);
		}else{
			hql= "from P2Res o where o.person.id='"+person.getId().toString()+"' and o.resource.parentRes.id = null";
			p2ResList = this.find(hql);
		}
		 
		StringBuffer sb = new StringBuffer();
		for (P2Res p2res : p2ResList) {
			sb.append(" id ='"+p2res.getResource().getId().toString()+"' or");
		}
		if(sb.length()>0){
			sb.delete(sb.length()-3,sb.length());
			hql = "from Module m where ("+sb.toString()+") and m.visiabled = '1'";
			List<Module> modules = this.find(hql);
			for (Module module : modules) {
				modulesMap.put(module.getId().toString(),module);
			}
		}
	}

	/**
	 * 根据编号删除一个模块
	 * @param moduleId
	 */
	public void deleteModule(Module module){
		Resource parentModule = (Resource) module.getParentRes();
		module.setParentRes(null);

		if (parentModule != null){
			parentModule.getChildRes().remove(module);
			if(parentModule.getLeafModule() != null && parentModule.getLeafModule().equals(false)){
				boolean hasModule = false;
				for (Resource childRes : parentModule.getChildRes()) {
					if(childRes instanceof Module){
						hasModule = true;
						break;
					}
				}
				if(!hasModule){
					parentModule.setLeafModule(true);
				}
			}
			this.save(parentModule);
		}
		_cascadeDelModule(module);
	}
	
	private void _cascadeDelModule(Module m){
		if(m.getLeafModule()!= null && !m.getLeafModule()){
			Collection<Module> res = this.findModule(m.getId().toString());
			for (Module module : res) {
				_cascadeDelModule(module);
			}
		}
		this.remove(m);
	}

}
