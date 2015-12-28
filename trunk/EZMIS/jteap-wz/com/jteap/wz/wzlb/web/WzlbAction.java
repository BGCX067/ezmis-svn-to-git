/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzlb.web;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.kwwh.manager.KwwhManager;
import com.jteap.wz.kwwh.model.Kw;
import com.jteap.wz.wzlb.manager.WzlbManager;
import com.jteap.wz.wzlb.model.Wzlb;

@SuppressWarnings({ "unchecked", "serial"})
public class WzlbAction extends AbstractTreeAction<Wzlb>{

	private WzlbManager wzlbManager;
	
	private CkglManager ckglManager;
	
	private KwwhManager kwwhManager;

	public KwwhManager getKwwhManager() {
		return kwwhManager;
	}

	public void setKwwhManager(KwwhManager kwwhManager) {
		this.kwwhManager = kwwhManager;
	}

	public CkglManager getCkglManager() {
		return ckglManager;
	}

	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}

	public WzlbManager getWzlbManager() {
		return wzlbManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
//		System.out.println(bean);
		return ((Wzlb)bean).getChildWzlb();
	//	return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "flbbm";
	}

	public String showTreeAction() throws Exception{
		String parentId = request.getParameter("parentId");
		List<Wzlb> wzlbList = wzlbManager.findWzlbByParentId(parentId);
		List nodeList = new ArrayList();
		for(Wzlb lb:wzlbList){
			Map map =new HashMap();
			map.put("id",lb.getId());
			if(lb.getFlbbm()!=null)
				map.put("parentId",lb.getFlbbm().getId());
			
			map.put("sortNo",lb.getPxm());
			
			map.put("leaf",lb.getChildWzlb().size()==0);
			map.put("text",lb.getWzlbmc());
			map.put("expanded",false);
			nodeList.add(map);
		}
		String json =JSONUtil.listToJson(nodeList);
		this.outputJson(json);
		return NONE;	
	}
	
	/**
	 * 删除指定节点之前需要进行什么处理
	 * 传出参数决定是否删除
	 * 提供扩展
	 * 
	 * @param node
	 * @return 
	 *     如果返回null，表明可以删除，否则返回将会是删除不了的提示信息
	 */
	@Override
	protected String beforeDeleteNode(Object node) throws Exception{
		Wzlb wzlb = (Wzlb)node;
		if(wzlb.getWzda().size()>0){
			return "请先将该类别及子类别下的物资移出！";
		}else{
			return null;
		}
	}
	
	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId = request.getParameter("id");

	//	System.out.println(((Map.Entry)(request.getParameterMap().entrySet().iterator().next())).getKey());
		return wzlbManager.findWzlbByParentId(parentId);
	}
	
	@Override
	protected void dragMoveNodeProcess(Object obj, boolean parentChanged,
			String oldParentId, String newParentId) {
	//	System.out.println(wzlbManager);
	//	wzlbManager.getSessionFactory().evictCollection("com.jteap.wz.wzlb.model.Wzlb.childWzlb");
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "pxm";
	}

	@Override
	protected String getTextPropertyName( Class beanClass) {
		return "wzlbmc";
	}

	@Override
	 
	public HibernateEntityDao getManager() {
		return wzlbManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id",
			"flbbm",
			"wzlbmc",
			"bz",
			"pxm",
		""};
	}

	public void setWzlbManager(WzlbManager wzlbManager) {
		this.wzlbManager = wzlbManager;
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"flbbm",
			"wzlbmc",
			"bz",
			"pxm",
		""};
	}
	
	/**
	 * 通过物资类别编码获取当前物资对应仓库的缺省库位编码
	 */
	public String findCwxxAction() throws Exception{
		String wzlbbm = request.getParameter("wzlbbm");
		if(StringUtil.isNotEmpty(wzlbbm)){
			Wzlb wzlb = findWzlbByflbbm(wzlbbm);
			String ckmc = wzlb.getBz();
			Ckgl ckgl = ckglManager.findUniqueBy("ckmc", ckmc);
			String ckid = ckgl.getId();
			String hql = "from Kw as k where k.cwmc like '%缺省库位%' and k.ckid = ?";
			Object args[] = {ckid};
			Kw kw = (Kw)kwwhManager.findUniqueByHql(hql, args);
			this.outputJson("{success:true,cwbm:'"+kw.getId()+"',cwmc:'"+kw.getCwmc()+"'}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 通过wzlbbm获取指定仓库信息
	 */
	public Wzlb findWzlbByflbbm(String wzlbbm){
		Wzlb wzlb = wzlbManager.get(wzlbbm);
		if(wzlb.getFlbbm().getId().equals("20000000000000000000000000003492"))
			return wzlb;
		else
			return findWzlbByflbbm(wzlb.getFlbbm().getId());
	}
	
	
}

