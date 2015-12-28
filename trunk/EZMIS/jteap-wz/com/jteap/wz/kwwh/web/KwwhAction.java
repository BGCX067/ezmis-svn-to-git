/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.kwwh.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.kwwh.manager.KwwhManager;
import com.jteap.wz.kwwh.model.Kw;
import com.jteap.wz.wzda.manager.WzdaManager;
@SuppressWarnings("unchecked")
public class KwwhAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2438948295515515125L;
	private KwwhManager kwwhManager;
	private WzdaManager wzdaManager;
	
	
	public String getKWNodeAction() throws Exception {
		String ckid = request.getParameter("ckid");
		String parentId = request.getParameter("parentId");
		
		List<Map> nodeList = new ArrayList<Map>();
		
		//取出所有的库位信息
		List<Kw> kwList = kwwhManager.getKWList(ckid,parentId);
		Iterator<Kw> kwit = kwList.iterator();
		int  sort = 0;
		while(kwit.hasNext()){
			sort++;
			Kw kw = kwit.next();
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("id", kw.getId());
			map.put("text", kw.getCwmc());
			map.put("sortNo", sort);
			map.put("bz", kw.getBz());
			map.put("wzzjm", kw.getWzzjm());	
			map.put("_parent",kw.getPkw()!=null?kw.getPkw().getId():null);
			map.put("_is_leaf",kw.getCkw()==null || kw.getCkw().size()<1); 
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
		List<Kw> kwList = kwwhManager.findKwByParentId(parentId);
		List nodeList = new ArrayList();
		int  sort = 0;
		for(Kw kw:kwList){
			sort++;
			Map map =new HashMap();
			map.put("id",kw.getId());
			if(kw.getPkw()!=null)
				map.put("parentId",kw.getPkw().getId());
			
			map.put("sortNo",sort);
			
			map.put("leaf",kw.getCkw()==null || kw.getCkw().size()<1);
			map.put("text",kw.getCwmc());
			map.put("expanded",false);
			nodeList.add(map);
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
	public String delKWAction() throws Exception{
		String kwid = request.getParameter("id");
		try{
			//取得该库位对象
			Kw kw = kwwhManager.findUniqueBy("id", kwid);
			//不能删除缺省库位
			if(kw.getCwmc().indexOf("缺省库位")!=-1){
				this.outputJson("{success:false,msg:'缺省库位不能删除!'}");
			}else{
				//删除库位
				//kwwhManager.delKwById(kwid);
				Kw qskw = kwwhManager.findUniqueBy("cwmc",kw.getCwmc().substring(0,2)+"-缺省库位");
				if(qskw==null){
					this.outputJson("{success:false,msg:'未发现缺省库位,不能删除!'}");
				}else{
					//先修改 该库位下的子库位下的物资
					for(Kw k:kw.getCkw()){
						wzdaManager.updateKw(k, qskw);
					}
					//然后修改该库位下的物资
					wzdaManager.updateKw(kw, qskw);
					//修改完毕后删除该库位
					kwwhManager.remove(kw);
					this.outputJson("{success:true}");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id",
			"cwmc",
			"wzzjm",
			"ckbm",
			"sxxjbbm",
			"bz",
			"ck",
			"id",
			"ckmc",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"cwmc",
			"wzzjm",
			"ckbm",
			"sxxjbbm",
			"bz",
		""};
	}

	public KwwhManager getKwwhManager() {
		return kwwhManager;
	}

	public void setKwwhManager(KwwhManager kwwhManager) {
		this.kwwhManager = kwwhManager;
	}
	@Override
	public HibernateEntityDao<?> getManager() {
		// TODO Auto-generated method stub
		return kwwhManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	
}

