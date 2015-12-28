/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.lydgl.manager;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.tjrw.util.TjrwUtils;

@SuppressWarnings("unchecked")
public class LydmxManager extends HibernateEntityDao<Lydmx>{
	
	public Collection<Lydmx> findLydmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Lydmx as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	public void initSjje(){
		List<Lydmx> lydmxs = this.find("from Lydmx t where t.zt = '0'");
		for(Lydmx lydmx:lydmxs){
			lydmx.setSjje(TjrwUtils.getSjdj(lydmx.getWzlysqDetail().getId()));
			this.save(lydmx);
		}
		List<Lydmx> lydmxs1 = this.find("from Lydmx t where t.zt = '1'");
		System.out.println(lydmxs1.size());
		for(Lydmx lydmx:lydmxs1){
			lydmx.setSjje(TjrwUtils.getSjje(lydmx));
			System.out.println(lydmx.getSjje());
			this.save(lydmx);
		}
	}
	/**
	 * 生成合计数据 显示grid中
	 * @param countList  要生成的数据对象集合
	 * @param countMap   要放入的map对象集合
	 * @param countFlat  判断是当前页数据还是合计数据 d 点击下一页时的当前页合计数据
	 * q 点击上一页时的当前页合计数据 h 总记录合计数据
	 * lvchao
	 */
	public void setCountValue(List<Lydmx> countList,Map countMap,String countFlag){
		double jhjeSum = 0;
		double sjjeSum = 0;
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		for(Lydmx lydmx:countList){
			if(lydmx.getZt().equals("0")){
				jhjeSum = jhjeSum + lydmx.getPzlysl()*lydmx.getWzbm().getJhdj();
				sjjeSum = sjjeSum + Double.valueOf(decimalFormat.format(lydmx.getSjje()));
			}else{
				jhjeSum = jhjeSum + lydmx.getSjlysl()*lydmx.getWzbm().getJhdj();
				sjjeSum = sjjeSum + Double.valueOf(decimalFormat.format(lydmx.getSjje()));
			}
		}
		//添加总合计数据
		if("h".equals(countFlag)){
			//添加统计到合计数据
			countMap.put("jhje", jhjeSum);
			countMap.put("sjje", sjjeSum);
		}
	}
}
