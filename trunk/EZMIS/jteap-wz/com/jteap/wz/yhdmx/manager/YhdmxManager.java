/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.yhdmx.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.wzlysq.manager.WzlysqDetailManager;
import com.jteap.wz.wzlysq.model.WzlysqDetail;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.yhdmx.model.Yhdmx;


@SuppressWarnings("unchecked")
public class YhdmxManager extends HibernateEntityDao<Yhdmx>{
	private LydmxManager lydmxManager;
	
	private WzlysqDetailManager wzlysqDetailManager;
	
	private WzdaManager wzdaManager;
	
	public LydmxManager getLydmxManager() {
		return lydmxManager;
	}
	public void setLydmxManager(LydmxManager lydmxManager) {
		this.lydmxManager = lydmxManager;
	}
	public WzlysqDetailManager getWzlysqDetailManager() {
		return wzlysqDetailManager;
	}
	public void setWzlysqDetailManager(WzlysqDetailManager wzlysqDetailManager) {
		this.wzlysqDetailManager = wzlysqDetailManager;
	}
	
	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	public Collection<Yhdmx> findYhdmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Yhdmx as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	public Collection<Yhdmx> findYhdmxByYhdId(String yhdId){
		StringBuffer hql=new StringBuffer("from Yhdmx as g where ");
		Object args[]=null;
		hql.append("g.yhdgl.id=?");
		args=new String[]{yhdId};
		return find(hql.toString(),args);
	}
	/**
	 * 生成合计数据 显示grid中
	 * @param countList  要生成的数据对象集合
	 * @param countMap   要放入的map对象集合
	 * @param countFlat  判断是当前页数据还是合计数据 d 点击下一页时的当前页合计数据
	 * q 点击上一页时的当前页合计数据 h 总记录合计数据
	 * lvchao
	 */
	public void setCountValue(List<Yhdmx> countList,Map countMap,String countFlag){
		double sjhjSum = 0;
		double jhjeSum = 0;
		double sqdjSum = 0;
		double sqdjhjSum = 0;
		double seSum = 0;
		for(Yhdmx yhdmx:countList){
			if(yhdmx!=null){
				//税价合计总和
				sjhjSum = sjhjSum+ yhdmx.getSjhj();
//				//计划金额总和
				jhjeSum = jhjeSum + yhdmx.getYssl()*yhdmx.getWzdagl().getJhdj();
				//税前单价总和
				sqdjSum = (yhdmx.getSqdj()==null)?sqdjSum:yhdmx.getSqdj()+sqdjSum;

				BigDecimal one = new BigDecimal("1");
				//税前单价计算
				BigDecimal sqdjBd = new BigDecimal((yhdmx.getSqdj()==null)?0:yhdmx.getSqdj()*yhdmx.getYssl());
				sqdjhjSum = sqdjBd.divide(one,5,BigDecimal.ROUND_HALF_UP).divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue()+sqdjhjSum;
				//税额合计计算
				BigDecimal sjhjBd = new BigDecimal((yhdmx.getSqdj()==null)?0:yhdmx.getSqdj()*yhdmx.getYssl()*yhdmx.getSl());
				seSum = sjhjBd.divide(one,5,BigDecimal.ROUND_HALF_UP).divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue()+seSum;
				//System.out.println(sqdjhjSum);
			}
		}
		//添加到当前页合计数据 往后翻页
		if("d".equals(countFlag)){
			countMap.put("sjhj", sjhjSum+(Double)countMap.get("sjhj") );
			countMap.put("jhje", jhjeSum+(Double)countMap.get("jhje") );
			countMap.put("sqdj", sqdjSum+(Double)countMap.get("sqdj"));
			countMap.put("sqhj", sqdjhjSum+(Double)countMap.get("sqhj"));
			countMap.put("sehj", seSum+(Double)countMap.get("sehj"));
		}
		//添加到当前页合计数据 往前翻页
		if("q".equals(countFlag)){
			countMap.put("sjhj", (Double)countMap.get("sjhj")-sjhjSum);
			countMap.put("jhje", (Double)countMap.get("jhje")-jhjeSum);
			countMap.put("sqdj", (Double)countMap.get("sqdj")-sqdjSum);
			countMap.put("sqhj", (Double)countMap.get("sqhj")-sqdjhjSum);
			countMap.put("sehj", (Double)countMap.get("sehj")-seSum);
		}
		//添加总合计数据
		if("h".equals(countFlag)){
			//添加统计到合计数据
			countMap.put("sjhj", sjhjSum);
			countMap.put("jhje", jhjeSum);
			countMap.put("sqdj", sqdjSum);
			countMap.put("sqhj", sqdjhjSum);
			countMap.put("sehj", seSum);
		}
	}
	/**
	 * 通过验货单明细 查询该明细出库状态
	 * 0 自由入库 1 未出库 2 已出库 3部分出库
	 * @param xqjhmx
	 * @return
	 */
	public int findLyByYh(Yhdmx yhdmx){
		//没有采购计划 自由入库
		if(yhdmx.getCgjhmx()==null){
			return 0;
		}
		//根据验货单明细 找到需求计划明细
		XqjhDetail xqjhmx = yhdmx.getCgjhmx().getXqjhDetail();
		int flag =0;
		double pzsl = 0;
		//如果需求计划明细ID不为空 则是正常入库的验货单 否则是自由入库
		if(StringUtil.isNotEmpty(xqjhmx.getId())){
			List<WzlysqDetail> wzlysqDetailList = wzlysqDetailManager.find("from WzlysqDetail where xqjhDetail.id=?", xqjhmx.getId());
			//查询根据匹配需求计划的领用申请
			for(WzlysqDetail wzlysqDetail:wzlysqDetailList){
				List<Lydmx> lydmxList =lydmxManager.find("from Lydmx where wzlysqDetail.id=? and zt='1'", wzlysqDetail.getId());
				for(Lydmx lydmx:lydmxList){
					if(lydmx!=null){
						pzsl = pzsl + lydmx.getSjlysl();
					}
				}
			}
		}else{
			//如果当前库存为0则为已出库
			//if(yhdmx.getWzdagl().getDqkc()==0){
			//	flag =2;
			//}
			return flag;
		}
		if(pzsl==0){
			flag = 1;
		}else if(pzsl >= xqjhmx.getPzsl()){
			flag = 2;
		}else if(pzsl<xqjhmx.getPzsl()){
			flag = 3;
		}
		return flag;
	}
	
	/**
	 * 返回盘点物资的 jsonList
	 * @param hql 
	 * @param pjj
	 * @param sl
	 * @return
	 */
	public List<Map> getPdWz(String hql,double d_dj,double x_dj,int sl){
		List<Map> jsonList = new ArrayList<Map>();
		Map<String,Double> wzMap = new HashMap<String, Double>();
		List<String> wzidList = new ArrayList<String>();
		//根据条件查询出 相关的物资 将金额 数量 平均价算出来
		List<Yhdmx> yhdmxList = this.find(hql);
		for(Yhdmx yhdmx:yhdmxList){
			String wzid = yhdmx.getWzdagl().getId();
			//将id放入wzidList中
			if(!wzidList.contains(wzid)){
				wzidList.add(wzid);
			}
			if(wzMap.get(wzid+"_zje")==null){
				wzMap.put(wzid+"_zje",yhdmx.getSysl()*yhdmx.getSqdj());
			}else{
				wzMap.put(wzid+"_zje",wzMap.get(wzid+"_zje")+yhdmx.getSysl()*yhdmx.getSqdj());
			}
			if(wzMap.get(wzid+"_zsl")==null){
				wzMap.put(wzid+"_zsl",yhdmx.getSysl());
			}else{
				wzMap.put(wzid+"_zsl",wzMap.get(wzid+"_zsl")+yhdmx.getSysl());
			}
			if(wzMap.get(wzid+"_pjj")==null){
				wzMap.put(wzid+"_pjj",yhdmx.getSqdj());
			}else{
				wzMap.put(wzid+"_pjj",wzMap.get(wzid+"_zje")/wzMap.get(wzid+"_zsl"));
			}
		}
		
		for(int i =0;i<wzidList.size();i++){
			String wzid = wzidList.get(i);
			double jg = wzMap.get(wzid+"_pjj").doubleValue();
			if(x_dj!=0){
				if(jg>x_dj){
					wzidList.remove(wzid);
					i--;
				}
			}
			if(d_dj!=0){
				if(jg<d_dj){
					wzidList.remove(wzid);
					i--;
				}
			}
		}
		//如果集合数量 大于 要生成的数量 则随即删除 多余的ID
		if(sl!=0){
			if(wzidList.size()>sl){
				Random random = new Random();
				int size = wzidList.size()-sl;
				for(int i =0;i<size;i++){
					int index = random.nextInt(wzidList.size());
					wzidList.remove(index);
				}
			}
		}
		for(String wzid:wzidList){
			Map<String,String> jsonMap = new HashMap<String, String>();
			Wzda wzda =wzdaManager.findUniqueBy("id", wzid);
			//合计格式化 四舍五入
			BigDecimal hj = new BigDecimal(wzMap.get(wzid+"_zje"));
			BigDecimal one = new BigDecimal("1");
			double sjje = hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
			//单价格式化 四舍五入
			BigDecimal dj = new BigDecimal(wzMap.get(wzid+"_pjj"));
			double wzdj = dj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			jsonMap.put("wzid", wzda.getId());
			jsonMap.put("wzmc", wzda.getWzmc());
			jsonMap.put("xhgg", wzda.getXhgg());
			jsonMap.put("kw", wzda.getKw().getCwmc());
			jsonMap.put("pjj", wzdj+"");
			jsonMap.put("dqkc", wzMap.get(wzid+"_zsl")+"");
			jsonMap.put("dqje", sjje+"");
			jsonMap.put("zksj", wzdaManager.getZksj(wzid)+"");
			jsonList.add(jsonMap);
		}
		return jsonList;
	}
	
	/**
	 * 返回选择的物资 jsonList
	 * @param hql 
	 * @param pjj
	 * @param sl
	 * @return
	 */
	public List<Map> getPdWzByIds(String hql,String ids){
		List<Map> jsonList = new ArrayList<Map>();
		Map<String,Double> wzMap = new HashMap<String, Double>();
		List<String> wzidList = new ArrayList<String>();
		String[] id = ids.split(",");
		//根据条件查询出 相关的物资 将金额 数量 平均价算出来
		List<Yhdmx> yhdmxList = this.find(hql);
		for(Yhdmx yhdmx:yhdmxList){
			String wzid = yhdmx.getWzdagl().getId();
			for(int i=0;i<id.length;i++){
				if(id[i].equals(wzid)){
					if(!wzidList.contains(wzid)){
						wzidList.add(id[i]);
					}
					if(wzMap.get(wzid+"_zje")==null){
						wzMap.put(wzid+"_zje",yhdmx.getSysl()*yhdmx.getSqdj());
					}else{
						wzMap.put(wzid+"_zje",wzMap.get(wzid+"_zje")+yhdmx.getSysl()*yhdmx.getSqdj());
					}
					if(wzMap.get(wzid+"_zsl")==null){
						wzMap.put(wzid+"_zsl",yhdmx.getSysl());
					}else{
						wzMap.put(wzid+"_zsl",wzMap.get(wzid+"_zsl")+yhdmx.getSysl());
					}
					if(wzMap.get(wzid+"_pjj")==null){
						wzMap.put(wzid+"_pjj",yhdmx.getSqdj());
					}else{
						wzMap.put(wzid+"_pjj",wzMap.get(wzid+"_zje")/wzMap.get(wzid+"_zsl"));
					}
				}
			}
		}
		for(String wzid:wzidList){
			Map<String,String> jsonMap = new HashMap<String, String>();
			Wzda wzda =wzdaManager.findUniqueBy("id", wzid);
			//合计格式化 四舍五入
			BigDecimal hj = new BigDecimal(wzMap.get(wzid+"_zje"));
			BigDecimal one = new BigDecimal("1");
			double sjje = hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
			//单价格式化 四舍五入
			BigDecimal dj = new BigDecimal(wzMap.get(wzid+"_pjj"));
			double wzdj = dj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			jsonMap.put("wzid", wzda.getId());
			jsonMap.put("wzmc", wzda.getWzmc());
			jsonMap.put("xhgg", wzda.getXhgg());
			jsonMap.put("kw", wzda.getKw().getCwmc());
			jsonMap.put("pjj", wzdj+"");
			jsonMap.put("dqkc", wzMap.get(wzid+"_zsl")+"");
			jsonMap.put("dqje", sjje+"");
			jsonMap.put("zksj", wzdaManager.getZksj(wzid)+"");
			jsonList.add(jsonMap);
		}
		//System.out.println(jsonList.size());
		return jsonList;
	}
}
