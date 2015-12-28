/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.cgjhmx.manager;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings("unchecked")
public class CgjhmxManager extends HibernateEntityDao<Cgjhmx>{
	private YhdmxManager yhdmxManager;
	private WzdaManager wzdaManager;
	
	
	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}
	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}
	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}
	
	public Collection<Cgjhmx> findCgjhmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Cgjhmx as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	//根据验货单明细生成采购单明细
	public void saveCgjhMxByYhdmx(List<Yhdmx> yhdmxList,Cgjhgl cgjh){
		for(Yhdmx yhdmx:yhdmxList){
			//如果验货单明细没有采购明细 则是自由入库 需要虚拟个采购计划明细
			if(yhdmx.getCgjhmx()==null){
				Cgjhmx cgjhMx = new Cgjhmx();
				//String uuid = UUIDGenerator.hibernateUUID();
				//cgjhMx.setId(uuid);
				cgjhMx.setCgjhgl(cgjh);
				cgjhMx.setXh(yhdmx.getXh());
				cgjhMx.setJhdj(yhdmx.getWzdagl().getJhdj());
				cgjhMx.setCgjldw(yhdmx.getCgjldw());
				cgjhMx.setCgsl(yhdmx.getDhsl());
				cgjhMx.setHsxs(1);
				cgjhMx.setWzdagl(yhdmx.getWzdagl());
				//cgjhMx.setWzdagl(wzdaManager.get());
				//自由入库物资的计划到货时间就是当前入库时间（修改人：曹飞，修改时间：2011-06-02）
				cgjhMx.setJhdhrq(new Date());
				cgjhMx.setDhsl(0);
				cgjhMx.setCgfx("0");
				cgjhMx.setCgy(yhdmx.getYhdgl().getCgy());
				cgjhMx.setZt("2");
				cgjh.getCgjhmxs().add(cgjhMx);
				yhdmx.setCgjhmx(cgjhMx);
				this.getHibernateTemplate().saveOrUpdate(cgjh);
				this.getHibernateTemplate().update(yhdmx);
			}
// 			this.getHibernateTemplate().clear();
		}
		this.getHibernateTemplate().flush();
	}
}
