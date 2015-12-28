package com.jteap.system.doclib.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.doclib.model.DoclibCatalogField;

/**
 * 文档中心分类字段管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings("unchecked")
public class DoclibCatalogFieldManager extends
		HibernateEntityDao<DoclibCatalogField> {

	/**
	 * 根据资源简称，取得该模块的指定资源
	 * 
	 * @param module
	 *            模块
	 * @param opResName
	 *            资源名称
	 * @return
	 */
	// public Operation findOperationByShortName(DoclibCatalogFiled
	// catalogFiled,String sn) {
	// for (Resource op : catalogFiled.getChildRes()) {
	// if(op instanceof Operation){
	// Operation opx=(Operation) op;
	// if(opx.getSn().equals(sn))
	// return opx;
	// }
	// }
	// return null;
	// }
}
