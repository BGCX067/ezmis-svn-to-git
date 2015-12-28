package com.jteap.dgt.tyxx.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.dgt.tyxx.model.Tyxxk;
/**
 * 
 * @author lvchao
 *
 */
@SuppressWarnings("unchecked")
public class TyxxManager extends HibernateEntityDao<Tyxxk> {

	
	public List getBumenList(){
		
		String hql="select distinct(tuanzu) from tyxxk";
		return this.find(hql,"");
		
	}
	
}
