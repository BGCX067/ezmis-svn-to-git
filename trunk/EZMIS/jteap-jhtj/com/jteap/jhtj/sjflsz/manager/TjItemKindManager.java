package com.jteap.jhtj.sjflsz.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjKndJZ;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.jhtj.sjqx.model.Sjqx;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.dict.model.DictCatalog;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
@SuppressWarnings({ "unchecked", "serial" })
public class TjItemKindManager extends HibernateEntityDao<TjItemKind> {
	private SjqxManager sjqxManager;
	public List<TjItemKind> getRootList(){
		List<TjItemKind> kindList=new ArrayList<TjItemKind>();
		String hql="from TjItemKind where parentKind=null";
		kindList=this.find(hql);
		return kindList;
	}
	
	/**
	 * 
	 *描述：得到所有的机组
	 *时间：2010-4-8
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<Dict> getDictsByUniqueName(String uniqueName){
		DictCatalog catalog=this.findUniqueBy(DictCatalog.class, "uniqueName", uniqueName);
		String hql="from Dict where catalog.id='"+catalog.getId()+"' order by sortNo";
		List<Dict> dictList=this.find(hql);
		return dictList;
	}
	
	public void saveJZs(String jzs,String kid){
		String[] jzArray=jzs.split(",");
		for(int i=0;i<jzArray.length;i++){
			String[] oneItem=jzArray[i].split("_");
			if(oneItem.length==2){
				TjKndJZ jz=new TjKndJZ();
				jz.setKid(kid);
				jz.setJz(oneItem[1]);
				jz.setJzname(oneItem[0]);
				this.save(jz);
			}
		}
	}
	
	public String getCheckJZsByKid(String kid){
		StringBuffer result=new StringBuffer();
		String hql="from TjKndJZ where kid='"+kid+"'";
		List<TjKndJZ> jzList=this.find(hql);
		if(jzList.size()>0){
			for(TjKndJZ jz:jzList){
				result.append(jz.getJzname()+"_"+jz.getJz()+",");
			}
			if(result.length()>0){
				result.deleteCharAt(result.length()-1);
			}
		}else{
			return null;
		}
		return result.toString();
	}
	
	
	public List<TjKndJZ> getTjKndJZByKid(String kid){
		 List<TjKndJZ> list=new ArrayList<TjKndJZ>();
		 String hql="from TjKndJZ where kid='"+kid+"'";
		 list = this.find(hql);
		 return list;
	}
	
	
	/**
	 * 功能说明:判断分类编码是否在tjInfoItem中用到
	 * @author 童贝
	 * @version Apr 27, 2009
	 * @param kid
	 * @return
	 */
	public boolean findTjInfoItemByKid(String kid){
		boolean bool=true;
		String hql="from TjInfoItem tj where tj.kid='"+kid+"'";
		List<TjInfoItem> list=this.find(hql);
		if(list.size()>0){
			bool=false;
		}
		return bool;
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
	public boolean ghostState(String kid){
		String hql="from TjItemKind where kid='"+kid+"'";
		List<TjItemKind> list=this.find(hql);
		if(list.size()>0){
			TjItemKind kind=list.get(0);
			kind.setDflag(new Long(1));
			this.save(kind);
			return true;
		}
		return false;
	}
	
	/**
	 * 功能说明:是否包含机组
	 * @author 童贝
	 * @version Jun 2, 2009
	 * @param kid
	 * @return
	 */
	public boolean isIncludeJz(String kid){
		String hql="from TjKndJZ jz where jz.kid='"+kid+"'";
		List list=this.find(hql);
		if(list.size()>0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 功能说明:根据数据分类查找机组
	 * @author 童贝
	 * @version Apr 27, 2009
	 * @param kid
	 * @return
	 */
	public List<TjKndJZ> findTjKndJZByKid(String kid){
		String hql="from TjKndJZ jz where jz.kid='"+kid+"'";
		return this.find(hql);
	}
	
	
	/**
	 * 功能说明:判断可以操作数据维护模块
	 * @author 童贝
	 * @version Jun 25, 2009
	 * @return
	 */
	public boolean findOperateState(String kid){
		String hql="from TjItemKind where kid='"+kid+"'";
		List<TjItemKind> list=this.find(hql);
		if(list.size()>0){
			TjItemKind state=list.get(0);
			if(state.getDflag().intValue()==2){
				return false;
			}else{
				state.setDflag(new Long(2));
				this.save(state);
			}
		}
		return true;
	}
	
	
	/**
	 * 功能说明:更新状态
	 * @author 童贝
	 * @version Jun 25, 2009
	 * @param username
	 */
	public boolean updateOperateState(String kid){
		String hql="from TjItemKind where kid='"+kid+"'";
		List<TjItemKind> list=this.find(hql);
		if(list.size()>0){
			TjItemKind kind=list.get(0);
			kind.setDflag(new Long(1));
			this.save(kind);
			return true;
		}
		return false;
	}
	
	/**
	 * 根据角色过滤权限
	 *描述：
	 *时间：2010-5-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<TjItemKind> filterKindListByQx(Person person,List<TjItemKind> kindList){
		Set<P2Role> p2rSet=person.getRoles();
		Iterator<P2Role> it=p2rSet.iterator();
		List<Sjqx> qxList=new ArrayList<Sjqx>();
		Set<Sjqx> qxSet=new HashSet<Sjqx>();
		while(it.hasNext()){
			P2Role p2r=it.next();
			Role role=p2r.getRole();
			List<Sjqx> sjList=sjqxManager.findBy("roleid", role.getId());
			qxSet.addAll(sjList);
		}
		qxList.addAll(qxSet);
		
		List<TjItemKind> removeList=new ArrayList<TjItemKind>();
		for(TjItemKind tj:kindList){
			boolean isExt=false;
			for(Sjqx sjqx:qxList){
				String id=tj.getId();
				if(id.equals(sjqx.getQxid())){
					isExt=true;
					break;
				}
			}
			if(!isExt){
				removeList.add(tj);
			}
		}
		kindList.removeAll(removeList);
		return kindList;
	}

	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}

	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}
	
}
