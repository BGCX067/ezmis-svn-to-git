package com.jteap.jx.dxxgl.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jx.dxxgl.model.Jzjxjh;

/**
 * 机组检修计划manager
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked"})
public class JzjxjhManager extends HibernateEntityDao<Jzjxjh> {

	/**
	 * 
	 * 描述 : 根据年份查找当年机组
	 * 作者 : wangyun
	 * 时间 : Aug 10, 2010
	 * 参数 : 
	 * 		year : 年份
	 * 返回值 :  List<Jzjxjh>
	 * 异常 : ParseException
	 * 
	 */
	public List<String> findJzByYear(String year) throws ParseException {
		String startYear = year + "-01-01";
		String endYear = year + "-12-31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dtStart = sdf.parse(startYear);
		Date dtEnd = sdf.parse(endYear);
		String hql = "select t.jz from Jzjxjh t where t.qsrq > ? and t.qsrq < ? group by (t.jz) order by t.jz";
		List<String> lst = this.find(hql, new Object[] {dtStart, dtEnd});
		return lst;
	}

	/**
	 * 
	 * 描述 : 根绝年份和机组查找计划
	 * 作者 : wangyun
	 * 时间 : Aug 10, 2010
	 * 参数 : 
	 * 		year : 年份
	 * 		jz ： 机组名
	 * 返回值 : List<Jzjxjh>
	 * 异常 : ParseException
	 * 
	 */
	public List<Object[]> findJhByJzAndYear(String year, String jz) throws ParseException {
		String startYear = year + "-01-01";
		String endYear = year + "-12-31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dtStart = sdf.parse(startYear);
		Date dtEnd = sdf.parse(endYear);
		String hql = "select t.id,t.jhmc from Jzjxjh t where t.qsrq > ? and t.qsrq < ? and t.jz=? order by t.jhmc";
		List<Object[]> lst = this.find(hql, new Object[] {dtStart, dtEnd, jz});
		return lst;
	}
}
