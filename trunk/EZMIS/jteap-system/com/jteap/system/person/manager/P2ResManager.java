package com.jteap.system.person.manager;

import java.util.ArrayList;
import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.model.P2Res;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Resource;

/**
 * person to role 人员资源中间对象管理器
 * 
 * @author tantyou
 * @date 2008-2-18
 */
@SuppressWarnings("unchecked")
public class P2ResManager extends HibernateEntityDao<P2Res> {
	
	/**
	 * 直接给人员指定资源
	 * @param person	用户
	 * @param res		指定的资源
	 * @param indicator 指定人
	 */
	public void setupResourceDirect(Person person,Resource res,String indicator) throws Exception{
		if(isResourceExist(person, res)){
			return;
		}
		P2Res p2res=new P2Res();
		p2res.setPerson(person);
		p2res.setResource(res);
		p2res.setIndicator(indicator);
		this.save(p2res);
		this.flush();
		//递归设定父亲资源也具有权限
		if(res.getParentRes()!=null){
			setupResourceDirect(person,res.getParentRes(),indicator);
		}
	}

	
	/**
	 * 从人员所拥有的资源中移除指定的资源，如果没有同级的其他资源，则级联删除父亲资源
	 * @param person
	 * @param res
	 */
	public void removeResource(Person person,Resource res) throws Exception{
		Resource parentRes=res.getParentRes();
		boolean isParentExist=(parentRes!=null);
		
		String path=isParentExist?res.getParentRes().getPathWithText():"";
		String hql="delete P2Res as p2r where p2r.person.id='"+person.getId()+"' and p2r.resource.id='"+res.getId()+"'";
		createQuery(hql).executeUpdate();
		boolean bOtherResExist=false;
		if(isParentExist){
			Collection<P2Res> allRes=findAllP2ResOfThePerson(person);
			for (P2Res tmpRes : allRes) {				
				String tmpPath=tmpRes.getResource().getPathWithText();
				if(!tmpPath.equals(path) && tmpPath.indexOf(path)>=0){
					bOtherResExist=true;
					break;
				}
			}
			if(!bOtherResExist){
				removeResource(person,parentRes);
			}
		}
	}
	
//	/**
//	 * 批量删除人员的权限
//	 * @param personIds
//	 */
//	public void batchDelP2ResByPersonIds(String[] personIds){
//		String hql="delete P2Res as p2res where";
//		for(int i=0;i<personIds.length;i++){
//			String personId=personIds[i];
//			if(i>0)
//				hql+=" or";
//			hql+=" p2res.person.id='"+personId+"'";
//		}
//		this.createQuery(hql).executeUpdate();
//	}
	
	/**
	 * 判断人员是否已经存在指定的资源
	 * @param person
	 * @param res
	 * @return 
	 */
	public boolean isResourceExist(Person person,Resource res) throws Exception{
		String hql="from P2Res as p2r where p2r.person.id='"+person.getId()+"' and p2r.resource.id='"+res.getId()+"'";
		return this.find(hql).size()>0;
	}
	
	/**
	 * 查询出该用户所拥有的所有资源中间对象
	 * @param person
	 * @return
	 */

	public Collection<P2Res> findAllP2ResOfThePerson(Person person) throws Exception{
		String hql="from P2Res as p2r where p2r.person.id='"+person.getId()+"'";
		return find(hql); 
	}
	
	/**
	 * 取得指定人员的所有直接资源
	 * @param person
	 * @return
	 */
	public Collection<Resource> findAllDirResourceOfThePerson(Person person) throws Exception{
		Collection <P2Res> p2ress=findAllP2ResOfThePerson(person);
		Collection<Resource> results=new ArrayList<Resource>();
		for (P2Res p2res : p2ress) {
			results.add(p2res.getResource());
		}
		return results;
	}
	
	/**
	 * 删除指定资源的所有人员关联
	 * @param resourceId
	 * @throws Exception
	 */
	public void deleteByResourceId(String resourceId) throws Exception{
		String hql="delete P2Res as p2res where p2res.resource.id='"+resourceId+"'";
		this.createQuery(hql).executeUpdate();
	}
	
	
}
