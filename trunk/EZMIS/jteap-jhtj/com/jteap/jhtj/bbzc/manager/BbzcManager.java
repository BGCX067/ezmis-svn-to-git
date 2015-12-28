package com.jteap.jhtj.bbzc.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.bbzc.model.BbIndex;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.jhtj.sjqx.model.Sjqx;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
@SuppressWarnings({ "unchecked", "serial" })
public class BbzcManager extends HibernateEntityDao<Bbzc> {
	private SjqxManager sjqxManager;
	private BbIndexManager bbIndexManager;
	public List<Bbzc> getRootList(){
		List<Bbzc> bbzcList=new ArrayList<Bbzc>();
		String hql="from Bbzc where parentBbzc=null";
		bbzcList=this.find(hql);
		return bbzcList;
	}
	
	public List<Bbzc> getBbzcList(){
		List<Bbzc> bbzcList=new ArrayList<Bbzc>();
		String hql="from Bbzc order by sortno";
		bbzcList=this.find(hql);
		return bbzcList;
	}
	
	/**
	 * 
	 * 描述:得到父节点集合
	 * 时间:2010 11 23
	 * 作者:童贝
	 * 参数:
	 * 返回值:List<Bbzc>
	 * 抛出异常:
	 */
	public List<Bbzc> getParentBbzcList(){
		List<Bbzc> bbzcList=new ArrayList<Bbzc>();
		String hql="from Bbzc where parentBbzc=null order by sortno ";
		bbzcList=this.find(hql);
		return bbzcList;
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
	public List<Bbzc> filterBbzcListByQx(Person person,List<Bbzc> bbzcList){
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
		
		List<Bbzc> removeList=new ArrayList<Bbzc>();
		for(Bbzc tj:bbzcList){
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
		bbzcList.removeAll(removeList);
		return bbzcList;
	}
	
	public List<BbIndex> filterBbIndexListByQx(Person person,List<BbIndex> bbzcList){
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
		
		List<BbIndex> removeList=new ArrayList<BbIndex>();
		for(BbIndex tj:bbzcList){
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
		bbzcList.removeAll(removeList);
		return bbzcList;
	}
	
	/**
	 * 
	 * 描述:得到所有的子分类
	 * 时间:2010 11 23
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getChildsBbzc(Bbzc bbzc,boolean isRoot,Person person){
		StringBuffer result=new StringBuffer();
		if(bbzc.getChildBbzc().size()==0){
			List<BbIndex> indexList=this.bbIndexManager.findBbindexListByFlid(bbzc.getId());
			//if(!isRoot){
			//	indexList=this.filterBbIndexListByQx(person, indexList);
			//}
			StringBuffer indexSf=this.getIndexJson(indexList);
			result.append("{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"children\":["+(indexSf==null?"":(indexSf.toString()))+"]},");
		}else{
			Set<Bbzc> childBbzc=bbzc.getChildBbzc();
			StringBuffer childFl=new StringBuffer();
			//子分类
			for(Bbzc curBbzc:childBbzc){
				childFl.append(this.getChildsBbzc(curBbzc, isRoot, person));
			}
			if(childFl.toString().length()>0){
				childFl.deleteCharAt(childFl.length()-1);
			}
			//自己下面的index
			List<BbIndex> indexList=this.bbIndexManager.findBbindexListByFlid(bbzc.getId());
			//if(!isRoot){
			//	indexList=this.filterBbIndexListByQx(person, indexList);
			//}
			StringBuffer indexSf=this.getIndexJson(indexList);
			//System.out.println("-----"+childFl.toString());
			result.append("{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"children\":["+childFl.toString()+(indexSf==null?"":(","+indexSf.toString()))+"]},");
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * 描述:将index转换成JSON
	 * 时间:2010 11 23
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public StringBuffer getIndexJson(List<BbIndex> indexList){
		StringBuffer children=new StringBuffer();
		if(indexList.size()>0){
			for(BbIndex index:indexList){
				children.append("{\"id\":\""+index.getId()+"\",\"text\":\""+index.getBbmc()+"\",\"expanded\":false,\"leaf\":true,\"type\":\"2\"},");
			}
			if(children.toString().length()>0){
				children.deleteCharAt(children.length()-1);
			}
			return children;
		}else{
			//String json="{\"id\":\""+bbzc.getId()+"\",\"text\":\""+bbzc.getFlmc()+"\",\"expanded\":true,\"leaf\":false,\"type\":\"1\",\"children\":[]}";
			return null;
		}
	}

	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}

	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}

	public BbIndexManager getBbIndexManager() {
		return bbIndexManager;
	}

	public void setBbIndexManager(BbIndexManager bbIndexManager) {
		this.bbIndexManager = bbIndexManager;
	}
}
