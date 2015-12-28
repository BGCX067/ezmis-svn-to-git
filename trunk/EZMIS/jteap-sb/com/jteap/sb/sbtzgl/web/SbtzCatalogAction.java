package com.jteap.sb.sbtzgl.web;

import java.util.Collection;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.sb.sbtzgl.manager.SbtzCatalogManager;
import com.jteap.sb.sbtzgl.model.SbtzCatalog;

/**
 * 设备台帐分类Action
 * @author caofei
 */
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class SbtzCatalogAction extends AbstractTreeAction<SbtzCatalog> {

	private SbtzCatalogManager sbtzCatalogManager;
	
	public void setSbtzCatalogManager(SbtzCatalogManager sbtzCatalogManager) {
		this.sbtzCatalogManager = sbtzCatalogManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		SbtzCatalog catalog = (SbtzCatalog) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId = request.getParameter("parentId");
		return sbtzCatalogManager.findCatalogByParentId(parentId);
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "flmc";
	}

	@Override
	public HibernateEntityDao getManager() {
		return sbtzCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "flmc", "flbm", "sortno" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "flmc", "flbm", "sortno" };
	}
	
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(SbtzCatalog.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				SbtzCatalog sbtzCatalog=(SbtzCatalog) obj;
				map.put("flbm",sbtzCatalog.getFlbm());
				map.put("flmc",sbtzCatalog.getFlmc());
			}
		});
		
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	/**
	 * 新建分类节点动作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveOrUpdateCatalogAction() throws Exception {
		String flmc = request.getParameter("nodeName");
		String parentId = request.getParameter("parentId");
		String id = request.getParameter("id");

		try {
			SbtzCatalog catalog = null;

			if (StringUtils.isNotEmpty(id)) {
				catalog = sbtzCatalogManager.get(id);
			} else {
				catalog = (SbtzCatalog) this.creatBlankObject();
				if (StringUtils.isNotEmpty(parentId)) {
					SbtzCatalog parentCatalog = sbtzCatalogManager.get(parentId);
					//hql = "from SbtzCatalog s where s.parent.id='"+parentId+"' order by s.flbm desc";
					//同级存在节点
					if(sbtzCatalogManager.getSbtzCatalogByHql(parentId) != null){
						catalog.setFlbm(getRandom(true, sbtzCatalogManager.getSbtzCatalogByHql(parentId).getFlbm(), ""));
					}else{
						catalog.setFlbm(getRandom(false, "", parentCatalog.getFlbm()));
					}
					catalog.setParent(parentCatalog);
				}else{
				   // hql = "from SbtzCatalog s where s.parent.id is null order by s.flbm desc";
					//同级存在节点
					if(sbtzCatalogManager.getSbtzCatalogByHql("") != null){
						catalog.setFlbm(getRandom(true, sbtzCatalogManager.getSbtzCatalogByHql("").getFlbm(),""));
					}else{
						catalog.setFlbm(getRandom(false,"",""));
					}
				}
			}
			catalog.setFlmc(flmc);
			sbtzCatalogManager.save(catalog);
			outputJson("{success:true,id:'" + catalog.getId() + "'}");
		} catch (Exception ex) {
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}
	
	
	/**
	 * 返回分类编码
	 * @param status(是否存在同级节点)
	 * @param lastBm(最大同级节点分类编码)
	 * @param parentBm(父节点分类编码)
	 * @return String
	 */
	public String getRandom(boolean status, String lastBm, String parentBm){
		String flbm = "";
		if(status == false){
			if(parentBm != ""){
				flbm = parentBm + "01";
			}else{
				flbm = "01";
			}
		}else{
			flbm = String.valueOf(Integer.valueOf(lastBm) + 1);
			if(flbm.valueOf(0) != lastBm.valueOf(0)){
				flbm = "0" + flbm;
			}else{
				//flbm = flbm;
				return flbm;
			}
		}
		return flbm;
	}
	

	public SbtzCatalogManager getSbtzCatalogManager() {
		return sbtzCatalogManager;
	}
	
}
