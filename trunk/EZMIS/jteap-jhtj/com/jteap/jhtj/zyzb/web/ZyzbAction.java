package com.jteap.jhtj.zyzb.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.chart.DualYMSColumn3DAndLineChart;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjwh.model.KeyModel;
import com.jteap.jhtj.zyzb.manager.ZyzbManager;
import com.jteap.jhtj.zyzb.model.Zyzb;
@SuppressWarnings({ "unchecked", "serial" })
public class ZyzbAction extends AbstractAction {
	private ZyzbManager zyzbManager;
	
	/**
	 * 
	 *描述：得到左边指标分类的列表
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getLeftViewAction() throws Exception{
		String zbfl = StringUtils.defaultIfEmpty(request.getParameter("zbfl"),"1");
		List list = zyzbManager.getZbjdList(zbfl);
		request.setAttribute("list", list);
		return "getLeftView";
	}

	/**
	 * 
	 *描述：显示图形的主页
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getZbjdViewIndexAction() throws Exception{
		String id=request.getParameter("id");
		String zbfl="";
		String fxfs="";
		Zyzb zyzb=null;
		if(StringUtils.isNotEmpty(id)){
			zyzb=this.zyzbManager.get(id);
		}else{
			zbfl = StringUtils.defaultIfEmpty(request.getParameter("zbfl"),"1");
			List<Zyzb> zyzbList=this.zyzbManager.getZbjdList(zbfl);
			zyzb=zyzbList.get(0);
		}
		zbfl=zyzb.getZbfl();
		fxfs=zyzb.getFxfs();
		
		request.setAttribute("id",id);
		request.setAttribute("zbfl",zbfl);
		request.setAttribute("fxfs",fxfs);
		return "getZbjdViewIndexAction";
	}
	
	/**
	 * 
	 *描述：动态生成查询面板
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String dynaAddSearPanelAction() throws Exception{
		List<KeyModel> yearList=this.zyzbManager.getYearList();
		List<KeyModel> entiyList=this.zyzbManager.getEntiyList();
		
		String yearJson=JSONUtil.listToJson(yearList,new String[]{"displayValue","value"});
		String entiyJson=JSONUtil.listToJson(entiyList,new String[]{"displayValue","value"});

		String defaultNian=yearList.get(0).getValue();
		String defaultEntiy=entiyList.get(0).getValue();
		
		String json="{success:true,"+TjInterFace.NIAN+":["+yearJson+"]"+",ENTIY:["+entiyJson+"],"+TjInterFace.NIAN+"value:'"+defaultNian+"',ENTIYvalue:'"+defaultEntiy+"'}";
		outputJson(json);
		return NONE;
	}
	
	/**
	 * 
	 *描述：生成图形
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String generateChartAction() throws Exception{
		String keyList=request.getParameter("keyList");
		String id=request.getParameter("id");
		//String zbfl=request.getParameter("zbfl");
		String fxfs=request.getParameter("fxfs");
		
		Zyzb zyzb=this.zyzbManager.get(id);
		
		Map<String, String> keyMap=new HashMap<String, String>();
		String[] keys=keyList.split("!");
		for(String key:keys){
			String[] nameAndValue=key.split(",");
			if(nameAndValue.length==2){
				keyMap.put(nameAndValue[0],nameAndValue[1]);
			}
		}
		String nian=keyMap.get(TjInterFace.NIAN);
		StringBuffer fields=new StringBuffer();
		//同期对比
		if("1".equals(fxfs)){
			DualYMSColumn3DAndLineChart chart=this.zyzbManager.generateTQChart(keyMap, zyzb);
			fields.append(nian+","+nian+"!"+new Integer(Integer.parseInt(nian)-1)+","+new Integer(Integer.parseInt(nian)-1)+"!同期比,同期比");
			outputJson("{success:true,chartData:'"+chart.toString()+"',tableData:'"+chart.getTableToXml()+"',fields:'"+fields.toString()+"'}");
		}else if("2".equals(fxfs)){
		//机组对比
			List<KeyModel> entiyList=this.zyzbManager.getEntiyList();
			int flag=0;
			for(KeyModel model:entiyList){
				fields.append(model.getDisplayValue()+","+model.getDisplayValue()+"!");
			}
			for(KeyModel model:entiyList){
				if(flag>0){
					fields.append(model.getDisplayValue()+"占全厂比率,"+model.getDisplayValue()+"占全厂比率!");
				}
				flag++;
			}
			if(!fields.toString().equals("")){
				fields.deleteCharAt(fields.length()-1);
			}
			DualYMSColumn3DAndLineChart chart=this.zyzbManager.generateJZChart(nian, zyzb, entiyList);
			outputJson("{success:true,chartData:'"+chart.toString()+"',tableData:'"+chart.getTableToXml()+"',fields:'"+fields.toString()+"'}");
		}else if("3".equals(fxfs)){
		//指标关联
			String name=zyzb.getName();
			String[] names=name.split("-");
			for(String item:names){
				fields.append(item+","+item+"!");
			}
			if(!fields.toString().equals("")){
				fields.deleteCharAt(fields.length()-1);
			}
			DualYMSColumn3DAndLineChart chart=this.zyzbManager.generateZBGLChart(keyMap, zyzb);
			outputJson("{success:true,chartData:'"+chart.toString()+"',tableData:'"+chart.getTableToXml()+"',fields:'"+fields.toString()+"'}");
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return zyzbManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public ZyzbManager getZyzbManager() {
		return zyzbManager;
	}

	public void setZyzbManager(ZyzbManager zyzbManager) {
		this.zyzbManager = zyzbManager;
	}

}
