package com.jteap.sb.sbpjgl.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.sb.sbpjgl.model.Sbpj;

@SuppressWarnings("unused")
public class SbpjManager extends HibernateEntityDao<Sbpj> {

	/**
	 * 设备评级合计
	 * @param type("一类"、"二类"、"三类"、"未定")
	 * @return double
	 */	
	public double getTotal(String type){
		String hql = "from Sbpj as s where s.bcpjjb='"+type+"'";
		Object args[] = null;
		return this.find(hql, args).size();
	}
	
	/**
	 * 设备评级总合计
	 * @return double
	 */	
	public double getTotal(){
		String hql = "from Sbpj as s";
		Object args[] = null;
		return this.find(hql, args).size();
	}
	
	/**
	 * 所占比率
	 * @return double
	 */
	public double getRatio(String type){
		return (getTotal(type)/getTotal())*100;
	}
	
	/**
	 * 根据条件查询设备评级合计
	 * @return double
	 */
	public double getItemTotalByParamsHql(String type, String queryParamsHql){
		String hql = "";
		if(queryParamsHql == ""){
			hql = "from Sbpj as obj where obj.bcpjjb='"+type+"'";
		}else{
			hql = "from Sbpj as obj where obj.bcpjjb='"+type+"' and "+queryParamsHql;
		}
		Object args[] = null;
		return this.find(hql, args).size();
	}
	
	/**
	 * 根据条件查询设备评级总合计
	 * @return double
	 */
	public double getTotalByParamsHql(String queryParamsHql){
		String hql = "";
		if(queryParamsHql == ""){
			hql = "from Sbpj as obj";
		}else{
			hql = "from Sbpj as obj where "+queryParamsHql;
		}
		Object args[] = null;
		return this.find(hql, args).size();
	}
	
	/**
	 * 根据条件查询设备评级所占比率
	 * @return double
	 */
	public double getRatioByParamsHql(String type, String queryParamsHql){
		return (getItemTotalByParamsHql(type,queryParamsHql)/getTotalByParamsHql(queryParamsHql))*100;
	}
	
	
	
}
