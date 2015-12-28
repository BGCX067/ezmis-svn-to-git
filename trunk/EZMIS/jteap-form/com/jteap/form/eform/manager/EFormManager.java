package com.jteap.form.eform.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HZ2PY;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.model.EForm;

/**
 * 自定义表单管理对象
 * @author tanchang
 *
 */
@SuppressWarnings({"unchecked","unused"})
public class EFormManager extends HibernateEntityDao<EForm> {

	
	/**
	 * 根据内码编号取得属于该内码的所有表单的最大的版本号
	 * @param nm
	 * @return
	 */
	public int getMaxVerNo(String nm){
		String hql="select max(version) from EForm as f where f.nm=?";
		
		List<Integer> list=this.find(hql, new Object[]{nm});
		return list.get(0);
	}
	
	/**
	 *描述：根据表单标识取得表单对象并返回
	 *时间：2010-5-13
	 *作者：谭畅
	 *参数：sn 表单标识
	 *返回值:EForm 符合条件的表单对象，不存在返回NULL
	 *抛出异常：
	 */
	public EForm getEFormBySn(String sn){
		EForm eform = this.findUniqueBy("sn", sn);
		return eform;
	}
	
}
