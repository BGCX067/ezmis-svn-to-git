package com.jteap.wz.yhdgl.web;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import com.jteap.form.eform.web.EFormAction;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings("serial")
public class YhdglEFormAction extends EFormAction {
	private YhdmxManager yhdmxManager;
	
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void eformBeforeSave(Connection conn, String docid,
			Map<String, Element> diMap, Map<String, Object> dataMap,
			boolean isNew) throws Exception {
		String sql = "UPDATE TB_WZ_YCGJHMX SET DHSL = DHSL+%1%,ZT='3' WHERE ID=%2%";
		String sql1 = "UPDATE TB_WZ_XQJH_DETAIL SET DHSL = DHSL+%1% WHERE ID=(SELECT XQJHMX FROM TB_WZ_YCGJHMX WHERE ID=%2%)";
		
		Map<String,List<Map>> subList = new HashMap<String,List<Map>>();
		Statement pst = null;
		Statement pst1 = null;
		try{
			for(String fd:dataMap.keySet()){
				Object obj = dataMap.get(fd);
				if(obj instanceof List){
					subList.put(fd, (List<Map>) obj);
					continue;	//过滤掉子表数据
				} 
			}
			
			if(subList.size()>0){
				for(String id : subList.keySet()){
					List sub = subList.get(id);
					for (int i = 0; i < sub.size(); i++) {
						Map subFd = (Map)sub.get(i);
						pst = conn.createStatement();
						pst1 = conn.createStatement();
						double sl = 0;
						//如果ID不为空 则是修改页面调用此方法
						if(subFd.get("ID")!=null){
							//获取新增时 验货单的到货数量 和 现在子表里的验收数量对比
							Yhdmx yhdmx = yhdmxManager.findUniqueBy("id", subFd.get("ID").toString());
							if(yhdmx!=null){
								//自由入库 则将虚拟的采购计划明细 到货数量 验收数量改为一致
								if("自由入库".equals(yhdmx.getCgjhmx().getCgjhgl().getBz())){
									System.out.println("自由入库");
									String sql2 = "UPDATE TB_WZ_YCGJHMX SET CGSL = %1%,DHSL = %1%,ZT='3' WHERE ID=%2%";
									Statement pst2 = conn.createStatement();
									pst2.execute(sql2.replace("%1%", subFd.get("YSSL").toString())
											.replace("%2%", "'"+(String)subFd.get("CGDMX")+"'"));
								}else{
									sl = Double.valueOf(subFd.get("YSSL").toString())-yhdmx.getYssl();
								}
							}else{
								sl = Double.valueOf(subFd.get("YSSL").toString());
							}
						}else{
							//否则 是添加页面调用此方法 则数量就为验收数量
							sl = Double.valueOf(subFd.get("YSSL").toString());
						}
						pst.execute(sql.replace("%1%",sl+"")
										.replace("%2%", "'"+(String)subFd.get("CGDMX")+"'"));
						pst1.execute(sql1.replace("%1%", subFd.get("YSSL").toString())
										.replace("%2%", "'"+(String)subFd.get("CGDMX")+"'"));
					}
				}
			}
		}finally{
			if(pst!=null){
				pst.close();
			}
			if(pst1!=null){
				pst1.close();
			}
		}
	}
}
