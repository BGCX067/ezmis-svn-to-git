/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gcxmgl.web;
import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.gclbgl.manager.ProjcatManager;
import com.jteap.wz.gclbgl.model.Projcat;
import com.jteap.wz.gcxmgl.manager.ProjManager;
import com.jteap.wz.gcxmgl.model.Proj;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
@SuppressWarnings("unchecked")
public class ProjAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2438948295515515125L;
	private ProjManager projManager;
	private ProjcatManager projcatManager;
	
	public String getProjNodeAction() throws Exception {
		String projcatid = request.getParameter("projcatid");
		String parentId = request.getParameter("parentId");
		
		List<Map> nodeList = new ArrayList<Map>();
		
		//取出所有的库位信息
		List<Proj> projList = projManager.getProjList(projcatid,parentId);
		Iterator<Proj> projit = projList.iterator();
		int  sort = 0;
		while(projit.hasNext()){
			sort++;
			Proj proj = projit.next();
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("id", proj.getId());
			map.put("projcode", proj.getProjcode());
			map.put("text", proj.getProjname());
			map.put("sortNo", sort);
			map.put("_parent",proj.getP_proj()!=null?proj.getP_proj().getId():null);
			map.put("_is_leaf",proj.getC_proj()==null || proj.getC_proj().size()<1); 
			map.put("taskdesc",proj.getTaskdesc());
			map.put("target",proj.getTarget());
			map.put("execdept",proj.getExecdept());
			map.put("starttime_plan",proj.getStarttime_plan());
			map.put("endtime_plan",proj.getEndtime_plan());
			map.put("starttime_fact",proj.getStarttime_fact());
			map.put("endtime_fact",proj.getEndtime_fact());
			map.put("fundlimit",proj.getFundlimit());
			map.put("fundused",proj.getFundused());
			map.put("mfundlimit",proj.getMfundlimit());
			map.put("mfundused",proj.getMfundused());
			map.put("judge",proj.getJudge());
			map.put("finished",proj.getFinished());
			map.put("limiton",proj.getLimiton());
			map.put("timelimit",proj.getTimelimit());
			map.put("levelcode",proj.getLevelcode());
			map.put("gcxmbh",proj.getGcxmbh());
			map.put("pprojfee",proj.getPprojfee());
			map.put("aprojfee",proj.getAprojfee());
			map.put("needplan",proj.getNeedplan());
			map.put("c_c_sgdw",proj.getC_c_sgdw());
			map.put("c_c_fybm",proj.getC_c_fybm());
			nodeList.add(map);
		}
		//排序出所有库位的		
		String json =JSONUtil.listToJson(nodeList);
		this.outputJson("{success:true,data:"+json+"}");
		return NONE;

//		//取出所有的仓库信息
//		return NONE;
	}
	
	public String showTreeAction() throws Exception{
		String parentId = request.getParameter("parentId");
		String isProjcat = request.getParameter("isProjcat");
		List nodeList = new ArrayList();
		if(StringUtil.isEmpty(parentId)){
			List<Projcat> parojcatList = projcatManager.findResourcesByProjcat();
			int  sort = 0;
			for(Projcat projcat:parojcatList){
				sort++;
				Map map =new HashMap();
				map.put("id",projcat.getId());
				//if(proj.getP_proj()!=null)
				//	map.put("parentId",proj.getP_proj().getId());
				
				map.put("sortNo",sort);
				
				map.put("leaf",false);
				map.put("text",projcat.getProjcatname());
				map.put("expanded",false);
				nodeList.add(map);
			}
		}else{
			List<Proj> projList = null;
			if("1".equals(isProjcat)){
				projList = projManager.find("from Proj as p where p.projcat.id =? and p.finished='0' and p.projcat.predefined is null and p.isviable is null and p.p_proj is null", parentId);
			}else{
				projList = projManager.findProjByParentId(parentId);	
			}
			int  sort = 0;
			for(Proj proj:projList){
				sort++;
				Map map =new HashMap();
				map.put("id",proj.getId());
				if(proj.getP_proj()!=null)
					map.put("parentId",proj.getP_proj().getId());
				
				map.put("sortNo",sort);
				if(StringUtil.isNotEmpty(proj.getTaskdesc())){
					map.put("qtip", proj.getProjname()+"说明:主要包括【"+proj.getTaskdesc()+"】");
				}
				map.put("leaf",proj.getC_proj()==null || proj.getC_proj().size()<1);
				map.put("text",proj.getProjname());
				map.put("projcatId",proj.getProjcat().getId());
				map.put("projcatName",proj.getProjcat().getProjcatname());
				map.put("expanded",false);
				nodeList.add(map);
			}
		}
		String json =JSONUtil.listToJson(nodeList);
		this.outputJson(json);
		return NONE;	
	}

	/**
	 * 根据自定义表单formSn和记录ID删除自定义表单记录
	 * @return
	 * @throws Exception 
	 */
	public String delProjAction() throws Exception{
		String projid = request.getParameter("id");
		try{
			Proj proj = projManager.get(projid);
			if(proj.getC_proj().size()>0){
				for(Proj c_proj:proj.getC_proj()){
					c_proj.setIsviable("0");
					projManager.save(c_proj);
				}
			}
			proj.setIsviable("0");
			projManager.save(proj);
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	@Override
	public String[] listJsonProperties() {
		return new String[]{
				"projcode",
				"projcatcode",
				"gc__projcode",
				"projname",
				"taskdesc",
				"target",
				"execdept",
				"starttime_plan",
				"endtime_plan",
				"starttime_fact",
				"endtime_fact",
				"fundlimit",
				"fundused",
				"mfundlimit",
				"mfundused",
				"judge",
				"finished",
				"limiton",
				"timelimit",
				"levelcode",
				"gcxmbh",
				"pprojfee",
				"aprojfee",
				"needplan",
				"c_c_sgdw",
				"c_c_fybm"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"projcode",
				"projcatcode",
				"gc__projcode",
				"projname",
				"taskdesc",
				"target",
				"execdept",
				"starttime_plan",
				"endtime_plan",
				"starttime_fact",
				"endtime_fact",
				"fundlimit",
				"fundused",
				"mfundlimit",
				"mfundused",
				"judge",
				"finished",
				"limiton",
				"timelimit",
				"levelcode",
				"gcxmbh",
				"pprojfee",
				"aprojfee",
				"needplan",
				"c_c_sgdw",
				"c_c_fybm"};
	}
	
	/**
	 * 通过工程项目id查询工程内容（需求计划申请选择工程项目时，弹出提示框供其参考）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showGznrAction() throws Exception{
		String projid = request.getParameter("projid");
		try{
			//判断该库位下是否有物资
			Proj proj = projManager.get(projid);
			this.outputJson("{success:true,taskdesc:'"+proj.getTaskdesc()+"'}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}


	@Override
	public HibernateEntityDao<?> getManager() {
		// TODO Auto-generated method stub
		return projManager;
	}

	public ProjManager getProjManager() {
		return projManager;
	}

	public void setProjManager(ProjManager projManager) {
		this.projManager = projManager;
	}

	public ProjcatManager getProjcatManager() {
		return projcatManager;
	}

	public void setProjcatManager(ProjcatManager projcatManager) {
		this.projcatManager = projcatManager;
	}
	
}

