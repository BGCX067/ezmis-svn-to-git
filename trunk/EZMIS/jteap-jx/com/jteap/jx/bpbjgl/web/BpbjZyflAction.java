package com.jteap.jx.bpbjgl.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jx.bpbjgl.manager.BpbjZyflManager;
import com.jteap.jx.bpbjgl.model.BpbjZyfl;
import com.jteap.jx.bpbjgl.model.Bpbjxx;

/**
 * 备品备件-专业分类action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class BpbjZyflAction extends AbstractTreeAction<BpbjZyfl> {

	private BpbjZyflManager bpbjZyflManager;

	@Override
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(BpbjZyfl.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				BpbjZyfl bpbjZyfl=(BpbjZyfl) obj;
				map.put("leaf", bpbjZyfl.getBpbjZyfls().size()>0?false:true);
				map.put("expanded", true);
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
	 * 
	 * 描述 : 添加专业分类
	 * 作者 : wangyun
	 * 时间 : Aug 16, 2010
	 * 异常 : Exception
	 * 
	 */
	public String addZyflAction() {
		String id = request.getParameter("id");
		String parentId=request.getParameter("parentId");
		String zymc=request.getParameter("zymc");
		String preSortNo = request.getParameter("preSortNo");
		
		try {
			BpbjZyfl bpbjZyfl = null;
			if (StringUtil.isEmpty(id) || id.lastIndexOf("ynode") >= 0) {
				bpbjZyfl = new BpbjZyfl();
			} else {
				bpbjZyfl = bpbjZyflManager.get(id);
			}
			bpbjZyfl.setZymc(zymc);
			bpbjZyfl.setSortNo(Integer.parseInt(preSortNo)+1000);
			if(StringUtils.isNotEmpty(parentId)){
				BpbjZyfl parentZy = bpbjZyflManager.get(parentId);
				bpbjZyfl.setParentZyfl(parentZy);
			}
			
			bpbjZyflManager.save(bpbjZyfl);
			outputJson("{success:true,id:'"+bpbjZyfl.getId()+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		BpbjZyfl bpbjZyfl = (BpbjZyfl)node;
		Set<Bpbjxx> bpbjxxs = bpbjZyfl.getBpbjxxs();
		if (bpbjxxs.size() > 0) {
			return "该专业分类下有备品备件信息，不能删除";
		}
		return null;
	}

	@Override
	public HibernateEntityDao getManager() {
		return bpbjZyflManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public BpbjZyflManager getBpbjZyflManager() {
		return bpbjZyflManager;
	}

	public void setBpbjZyflManager(BpbjZyflManager bpbjZyflManager) {
		this.bpbjZyflManager = bpbjZyflManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentZyfl";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// 根据父亲节点查询
		String parentId = request.getParameter("parentId");
		List<BpbjZyfl> lst = bpbjZyflManager.findRoots(parentId);
		return lst;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "zymc";
	}

}
