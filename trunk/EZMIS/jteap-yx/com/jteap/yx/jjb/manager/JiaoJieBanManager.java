/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.jjb.manager;

import java.util.ArrayList;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.jjb.model.JiaoJieBan;

/**
 * 交接班Manager
 * @author caihuiwen
 */
@SuppressWarnings("unchecked")
public class JiaoJieBanManager extends HibernateEntityDao<JiaoJieBan>{
	
	/**获取最前或最后 一个交接班信息
	 * @param type		first/last
	 * @param gwlb		7种岗位类别
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JiaoJieBan findFirstOrLast(String type, String gwlb){
		JiaoJieBan jiaoJieBan = null;
		
		if("1".equals(gwlb)){
			gwlb = "#1机长";
		}else if("2".equals(gwlb)){
			gwlb = "#2机长"; 
		}else if("3".equals(gwlb)){
			gwlb = "#3机长"; 
		}else if("4".equals(gwlb)){
			gwlb = "#4机长"; 
		}
		try {
			String hql = "from JiaoJieBan j where j.gwlb=?";
			if("first".equals(type)){
				hql = "from JiaoJieBan j where j.gwlb=? order by(j.doDate) asc";
			}else if("last".equals(type)){
				hql = "from JiaoJieBan j where j.gwlb=? order by(j.doDate) desc";
			}
			List<JiaoJieBan> list = this.find(hql,gwlb);
			if(list.size() > 0)
				jiaoJieBan = list.get(0); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jiaoJieBan;
	}
	
	/**
	 * 查询某一天的交接班记录
	 * @param ymd
	 * @return
	 */
	public List<JiaoJieBan> findByYmd(String ymd){
		List<JiaoJieBan> list = new ArrayList<JiaoJieBan>();
		String hql = "from JiaoJieBan t where t.jjbsj=? order by t.doDate";
		list = this.find(hql,ymd);
		return list;
	}
	
}
