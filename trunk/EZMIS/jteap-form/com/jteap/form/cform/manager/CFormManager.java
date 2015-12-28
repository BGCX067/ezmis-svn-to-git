package com.jteap.form.cform.manager;

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
import com.jteap.form.cform.model.CForm;

/**
 * 自定义表单管理对象
 * @author tanchang
 *
 */
@SuppressWarnings({"unchecked","unused"})
public class CFormManager extends HibernateEntityDao<CForm> {
	private DataSource dataSource;	//数据源
	

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 根据EForm相关参数创建CForm对象
	 * @param dj_name		表单标题
	 * @param djsn			表单简称
	 * @param creator		创建人
	 * @param creatDt		创建时间
	 * @param catalogId		分类编号
	 * @param inputs		输入域 逗号分隔
	 * @param eformBillId	关联到eform的表单编号		
	 */


	public void createNewEForm(String dj_name, String djsn,
			String creator, Date creatDt, String catalogId, String inputs,
			int eformBillId) {
		
		CForm cform=new CForm();
		cform.setCatalogId(catalogId);
		cform.setTitle(dj_name);
		cform.setEditableInputs(inputs);
		cform.setCreatDt(creatDt);
		cform.setCreator(creator);
		cform.setNewVer(true);
		cform.setSn(djsn);
		cform.setNm(UUIDGenerator.hibernateUUID());
		cform.setEformBillId(eformBillId);
		cform.setType(CForm.CFORM_TYPE_EFORM);
		
		String hql="select catalogName from CFormCatalog where id=?";
		List list=find(hql, new Object[]{catalogId});
		String catalogName=list.get(0).toString();
		String catalogDir=HZ2PY.getPy1(catalogName,false).toUpperCase();
		
		String eformUrl=catalogDir;
		cform.setEformUrl(eformUrl);
		this.save(cform);
	}

	/**
	 * 根据eform表单编号修改相关表单属性
	 * @param djid
	 * @param dj_name
	 * @param djsn
	 * @param inputs
	 */
	public void updateEForm(int djid, String dj_name, String djsn,
			String inputs) {
		
		CForm cform=this.findUniqueBy("eformBillId", djid);
		cform.setTitle(dj_name);
		cform.setSn(djsn);
		cform.setEditableInputs(inputs);
		
		this.save(cform);
	}
	
	
	public Long countCFormByCatalogId(String catalogId){
		String hql="select count(*) from CForm where catalogId=?";
		List list=this.find(hql,new Object[]{catalogId});
		Long count=(Long) list.get(0);
		return count;
		
	}
	
	
	/**
	 * 根据cform的编号删除数据库中的eform表单对象  FC_BILLZL
	 * 1.找出所有需要删除的自定义表单关联的eform表单编号
	 * 2.删除FC_BILLZL中所有eform编号对应的表单数据
	 * @param ids
	 * @throws SQLException 
	 */
	public void deleteEFormByCFormIds(String[] ids){
		//1.查询所有需要删除的cform对应的eformBillId
		StringBuffer hql=new StringBuffer("select eformBillId from CForm where id in (");
		List list=new ArrayList();
		for (String str : ids) {
			if(StringUtils.isNotEmpty(str)){
				hql.append("?,");
				list.add(str);
			}
		}
		
		hql.deleteCharAt(hql.length()-1);
		hql.append(")");
		List<Integer> resultList=find(hql.toString(),list.toArray());
		
		//2.构造删除FC_BILLZL表单的sql语句
		StringBuffer sqlSb=new StringBuffer("delete FC_BILLZL where djid in(");
		for (Integer djid : resultList) {
			sqlSb.append(""+djid+",");
		}
		sqlSb.deleteCharAt(sqlSb.length()-1);
		sqlSb.append(")");
		
		//获取数据库连接对象并执行删除操作
		Connection conn=null;
		Statement st=null;
		try{
			DataSource ds=(DataSource) SpringContextUtil.getBean("dataSource");
			conn=ds.getConnection();
			st=conn.createStatement();
			String sql=sqlSb.toString();
			System.out.println(sql);
			st.execute(sql);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				st.close();
				conn.close();
			}catch(Exception ex){}
		}	
	}
	
	
	/**
	 * 根据内码编号取得属于该内码的所有表单的最大的版本号
	 * @param nm
	 * @return
	 */
	public int getMaxVerNo(String nm){
		String hql="select max(version) from CForm as f where f.nm=?";
		
		List<Integer> list=this.find(hql, new Object[]{nm});
		return list.get(0);
	}
	/**
	 * 取得指定表的指定字段以prefix为前缀的最大流水号
	 * @param tn 		TableName
	 * @param fd		Field Name
	 * @param prefix	Value 前缀
	 * @param numFm		流水号格式####
	 * @return
	 * @throws SQLException 
	 */
	public String getMaxNo(String tn,String fd,String prefix,String numFm) throws Exception{
		String sql="select max("+fd+") from "+tn+" where "+fd+" like '"+prefix+""+StringUtil.repeat("?", numFm.length())+"'";
		DecimalFormat df = new DecimalFormat(StringUtil.repeat("0", numFm.length()));
		Connection conn=dataSource.getConnection();
		String result="";
		Statement st=conn.createStatement();
		try{
			ResultSet rs=st.executeQuery(sql);
			String value=null;
			if(rs.next()){
				Object obj=rs.getObject(1);
				if(obj!=null)
					value=obj.toString();
			}
			
			long lNo=0;
			if(value!=null){
				value=value.replace(prefix, "");
				lNo=Long.parseLong(value);
			}
			lNo++;
			result=df.format(lNo);
			
			rs.close();
			st.close();
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		return result;
	}
}
