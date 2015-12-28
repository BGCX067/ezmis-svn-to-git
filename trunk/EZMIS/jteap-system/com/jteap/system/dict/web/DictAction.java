package com.jteap.system.dict.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.dict.manager.DictCatalogManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.dict.model.DictCatalog;

/**
 * 数据字典的Action处理类
 * @author Jeery
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DictAction extends AbstractTreeAction<Dict>{
	private DictCatalogManager dictCatalogManager;

	private DictManager dictManager;

	@Override
	public HibernateEntityDao getManager() {
		return dictManager;
	}
	
	/**
	 * 列表之前的预处理
	 */
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try{
			String catalogId=request.getParameter("catalogId");
			String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));	
			if(StringUtils.isNotEmpty(catalogId)){
				HqlUtil.addCondition(hql, "catalog.id", catalogId);
			}

			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
			
			HqlUtil.addOrder(hql, "sortNo", "asc");
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * 保存修改之前做的扩展操作
	 */
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		Dict dict=(Dict) obj;
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			DictCatalog dictCatalog = dictCatalogManager.get(catalogId);
			dict.setCatalog(dictCatalog);
		}
		
	}

	/**
	 * 
	 *描述：根据Catalog Unique Name查询对应字典列表并输出列表
	 *时间：2010-5-21
	 *作者：谭畅
	 *参数：@param catalog
	 *返回值: list:[{key:'',value:''},{}]
	 *抛出异常：
	 * @throws Exception 
	 */
	public String listDictByUniqueCatalogAction() throws Exception{
		try{
			String catalogName = request.getParameter("catalog");
			if(StringUtil.isNotEmpty(catalogName)){
				Collection<Dict> dicts = dictManager.findDictByUniqueCatalogName(catalogName);
				String json = JSONUtil.listToJson(dicts, this.listJsonProperties());
				this.outputJson("{success:true,list:"+json+"}");
			}else{
				this.outputJson("{success:false,msg:请求参数不符合规则}");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return NONE;
	}
	
	/**
	 * 将指定字典从当前类型移动到新的类型中去，仅仅是改变字典类型而已
	 * @return
	 * @throws Exception
	 */
	public String moveDictsToTheCatalogAction() throws Exception{
		//到哪个类型
		String tocatalogId=request.getParameter("tocatalogId");
		//那些字典
		String dictIds=request.getParameter("dictIds");
		String[] ids=dictIds.split(",");
		
		DictCatalog toDictCatalog=dictManager.get(DictCatalog.class,tocatalogId);
		
		int i=0;
		String msg="";
		for (String dictId : ids) {
			try{
				Dict dict=dictManager.get(Dict.class,dictId);
				dict.setCatalog(toDictCatalog);
				dictManager.save(dict);
				i++;
			}catch(Exception ex){
				throw new BusinessException(ex);
			}
		}
		this.outputJson("{success:true,copy:"+i+",msg:'"+msg+"'}");
		return NONE;
	}
	
	/**
	 * 将指定字典从当前类型复制到新的类型中去
	 * @return
	 * @throws Exception
	 */
	public String copyDictsToTheCatalogAction() throws Exception{
		//到哪个类型
		String tocatalogId=request.getParameter("tocatalogId");
		//那些字典
		String dictIds=request.getParameter("dictIds");
		String[] ids=dictIds.split(",");
		
		DictCatalog toDictCatalog=dictManager.get(DictCatalog.class,tocatalogId);
		
		int i=0;
		String msg="";
		for (String dictId : ids) {
			try{
				Dict oldDict=dictManager.get(Dict.class,dictId);
				Dict newDict=new Dict();
				BeanUtils.copyProperties(newDict, oldDict);
				newDict.setCatalog(toDictCatalog);
				newDict.setId(null);
				dictManager.save(newDict);
				i++;
			}catch(Exception ex){
				throw new BusinessException(ex);
			}
		}
		this.outputJson("{success:true,copy:"+i+",msg:'"+msg+"'}");
		return NONE;
	}
	
	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String []{"key","value","remark","id","sortNo"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String []{"catalog","catalogName","key","value","remark","id","sortNo"};
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "catalog";
	}

	@Override
	protected Collection getRootObjects() {
		return null;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "key";
	}
	
	public DictCatalogManager getDictCatalogManager() {
		return dictCatalogManager;
	}

	public void setDictCatalogManager(DictCatalogManager DictCatalogManager) {
		this.dictCatalogManager = DictCatalogManager;
	}
}
