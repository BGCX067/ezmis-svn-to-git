package com.jteap.wz.tjrw.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.lydgl.manager.LydmxManager;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.wzlysq.manager.WzlysqDetailManager;
import com.jteap.wz.wzlysq.model.WzlysqDetail;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings( { "serial", "unchecked", "unused" })
public class TjrwUtils {
	/**
	 * 返回入库单 入库金额 合计Map
	 * @param nf
	 * @param yf
	 * @param ckid
	 * @return
	 */
	 public static Map<String,Double> getRkMap(String hql){
		 DecimalFormat decimalFormat = new DecimalFormat("###.00");
		 YhdmxManager yhdmxManager = (YhdmxManager) SpringContextUtil.getBean("yhdmxManager");
		 List<Yhdmx> yhdmxList = yhdmxManager.find(hql);
		 BigDecimal one = new BigDecimal(1);
		 Map<String,Double> countMap = new HashMap<String, Double>();
		 //入库单总张数
		 countMap.put("countNum",Double.parseDouble(yhdmxList.size()+""));
		//入库数量
		 countMap.put("countNums",0.0);
		 //初始化合计金额
		 countMap.put("countMoney", 0.0);
		 for(Yhdmx yhdmx:yhdmxList){
			 countMap.put("countNums",countMap.get("countNums")+yhdmx.getYssl());
			 BigDecimal hj = new BigDecimal(yhdmx.getSqdj()*yhdmx.getYssl()+yhdmx.getZf());
			 double oje = countMap.get("countMoney");
			 countMap.put("countMoney",  hj.divide(one,5, BigDecimal.ROUND_HALF_UP).divide(one,2, BigDecimal.ROUND_HALF_UP).doubleValue()+oje);
		 }
		 return countMap;
	 }

	/**
	 * 返回出库单 出库金额 合计Map
	 * @param nf
	 * @param yf
	 * @param ckid
	 * @return
	 */
	public static Map<String,Double> getCkMap(String hql){
		LydmxManager lydmxManager = (LydmxManager) SpringContextUtil.getBean("lydmxManager");
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		Map<String,Double> countMap = new HashMap<String, Double>();
		List<Lydmx> lydmxList = lydmxManager.find(hql);
		//出库单总张数
		countMap.put("countNum",Double.valueOf(lydmxList.size()+""));
		//实际领用数量
		countMap.put("countNums",Double.valueOf(0+""));
		//初始化合计金额
		countMap.put("countMoney",Double.valueOf(0+""));
		for(Lydmx lydmx:lydmxList){
			//返回物资的实际单价
			//double sjdj = TjrwUtils.getSjje(lydmx)/lydmx.getSjlysl();
			//如果是自由领用 则取平均价 否则是单价的税价合计
			//因为生效保存实际金额的时候已经进行格式化 所以取的时候就直接取了
			double oje = countMap.get("countMoney");
			countMap.put("countMoney",Double.valueOf(decimalFormat.format(lydmx.getSjje()))+oje);
			countMap.put("countNums",lydmx.getSjlysl()+countMap.get("countNums"));
		}
		return countMap;
	}
	
	/**
	 * 返回本期导入 金额 合计Map
	 * @param nf
	 * @param yf
	 * @param ckid
	 * @return
	 */
	public static Map<String,Double> getJeMap(String hql){
		WzdaManager wzdaManager = (WzdaManager) SpringContextUtil.getBean("wzdaManager");
		Map<String,Double> countMap = new HashMap<String, Double>();
		List<Wzda> wzdaList = wzdaManager.find(hql);
		//出库单总张数
//		countMap.put("countNum",Double.valueOf(lydmxList.size()+""));
		//实际领用数量
		countMap.put("countNums",Double.valueOf(0+""));
		//初始化合计金额
		countMap.put("countMoney",Double.valueOf(0+""));
		for(Wzda wzda:wzdaList){
			//返回物资的实际单价
			//double sjdj = TjrwUtils.getSjje(lydmx)/lydmx.getSjlysl();
			//如果是自由领用 则取平均价 否则是单价的税价合计
			//因为生效保存实际金额的时候已经进行格式化 所以取的时候就直接取了
			double oje = countMap.get("countMoney");
			double nje = wzda.getOldwzkc()*(wzda.getSjdj()+wzda.getJhdj());
			BigDecimal hj = new BigDecimal(nje);
			BigDecimal one = new BigDecimal("1");
			countMap.put("countMoney",hj.divide(one,3,BigDecimal.ROUND_HALF_UP).doubleValue()+oje);
//			countMap.put("countNums",lydmx.getSjlysl()+countMap.get("countNums"));
		}
		return countMap;
	}
//	/**
//	 * 根据领用单明细 返回这个领用单所对应的验货单 取实际价格
//	 * 如果是自由领用 则取该物资的平均单价
//	 * @param lydmx
//	 * @return
//	 */
//	public static double getSjdj(Lydmx lydmx){
//		if(lydmx!=null){
//			return TjrwUtils.getSjdj(lydmx.getWzlysqDetail().getId());
//		}else{
//			return 0;
//		}
//	}
	/**
	 * 根据领用单明细获取实际金额
	 * 用于 物资统计 以及领用单生效 保存实际金额
	 */
	public static double getSjje(Lydmx lydmx){
		double sjje = 0;
		BigDecimal one = new BigDecimal("1");
		if(lydmx!=null){
			YhdmxManager yhdmxManager = (YhdmxManager) SpringContextUtil.getBean("yhdmxManager");
			
			WzdaManager wzdaManager=(WzdaManager)SpringContextUtil.getBean("wzdaManager");
			//获得领用数量
			double lysl = lydmx.getSjlysl();
			//物资对象
			Wzda wzda =lydmx.getWzbm();
			//如果有老物资库存 则优先出库老物资库存
			if(wzda.getOldkc()>0){
				//如果领用数量大于老物资库存 则部分实际金额按老物资库存金额取
				if(lysl>=wzda.getOldkc()){
					//设置新的领用数量
					lysl = lysl-wzda.getOldkc();
					//取得老物资实际金额
					BigDecimal hj = new BigDecimal(wzda.getOldkc()*(wzda.getJhdj()+wzda.getSjdj()));
					//将实际金额累计
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					//清空老物资库存
					wzda.setOldkc(0);
				}else{
					//设置新的老物资库存
					 wzda.setOldkc(wzda.getOldkc()-lysl);
					 //设置实际金额
					BigDecimal hj = new BigDecimal(lysl*(wzda.getJhdj()+wzda.getSjdj()));
					//将实际金额累计
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					//清空领用数量
					lysl = 0;
				}
			}
			//保存物资对象
			wzdaManager.save(wzda);
			//获得物资ID
			String wzid = wzda.getId();
			//根据物资id 遍历出 验货单中已经入库 并且有剩余数量的验货单明细
			String hql = "from Yhdmx where wzdagl.id = '"+wzid+"' and sysl>0 and zt = '1' order by rksj asc";
			
			List<Yhdmx> yhdmxList = yhdmxManager.find(hql);
			//遍历集合
			for(Yhdmx yhdmx:yhdmxList){
				//如果领用数量为0 则跳出循环
				if(lysl==0){
					break;
				}else if(yhdmx.getSysl()==lysl){//如果领用数量 等于 明细的剩余数量  
					//实际金额保留两位小数 以和入库单金额对上号
					BigDecimal hj = new BigDecimal(yhdmx.getSqdj()*lysl);
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					yhdmx.setSysl(0.0);
					lysl = 0;
				}else if(yhdmx.getSysl()<lysl){//如果剩余数量 小于领用数量
					BigDecimal hj = new BigDecimal(yhdmx.getSysl()*yhdmx.getSqdj());
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					lysl = lysl - yhdmx.getSysl();
					yhdmx.setSysl(0.0);
				}else if(yhdmx.getSysl()>lysl){//剩余数量大于 领用数量
					BigDecimal hj = new BigDecimal(yhdmx.getSqdj()*lysl);
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					yhdmx.setSysl(yhdmx.getSysl()-lysl);
					lysl = 0;
				}
				yhdmxManager.save(yhdmx);
			}
		}
		return sjje;
	}
	/**
	 * 返回物资领用实际金额 用于领料申请生效
	 * @param lysqmxid
	 * @return
	 */
	public static double getSjdj(String lysqmxid){
		CgjhmxManager cgjhmxManager = (CgjhmxManager) SpringContextUtil.getBean("cgjhmxManager");
		YhdmxManager yhdmxManager = (YhdmxManager) SpringContextUtil.getBean("yhdmxManager");
		WzdaManager wzdaManager = (WzdaManager) SpringContextUtil.getBean("wzdaManager");
		WzlysqDetailManager wzlysqDetailManager = (WzlysqDetailManager) SpringContextUtil.getBean("wzlysqDetailManager");
		//单个商品税前单价
		double sjje = 0;
		BigDecimal one = new BigDecimal("1");
		WzlysqDetail lysqmx = wzlysqDetailManager.get(lysqmxid);
		if(lysqmx!=null){
			double lysl = lysqmx.getLysl();
			String wzid = lysqmx.getWzbm();
			String hql = "from Yhdmx where wzdagl.id = '"+wzid+"' and sysl>0 and zt =1 order by rksj asc";
			List<Yhdmx> yhdmxList = yhdmxManager.find(hql);
			for(Yhdmx yhdmx:yhdmxList){
				if(lysl==0){
					break;
				}else if(yhdmx.getSysl()==lysl){
					//实际金额保留两位小数 以和入库单金额对上号
					BigDecimal hj = new BigDecimal(yhdmx.getSqdj()*lysl);
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					lysl = lysl - yhdmx.getSysl();
				}else if(yhdmx.getSysl()<lysl){
					BigDecimal hj = new BigDecimal(yhdmx.getSysl()*yhdmx.getSqdj());
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					lysl = lysl - yhdmx.getSysl();
				}else if(yhdmx.getSysl()>lysl){
					BigDecimal hj = new BigDecimal(yhdmx.getSqdj()*lysl);
					sjje = sjje+ hj.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
					lysl = 0;
				}
			}
		}
		return sjje;
	}
}
