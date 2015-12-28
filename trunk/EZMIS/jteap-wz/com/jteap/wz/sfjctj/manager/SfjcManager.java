package com.jteap.wz.sfjctj.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.util.Assert;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.sfjctj.model.ExportExcelModel;
import com.jteap.wz.sfjctj.model.Sfjc;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjny.model.Tjny;
import com.jteap.wz.tjrw.model.Tjrw;
import com.jteap.wz.tjrw.util.TjrwUtils;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;

/**
 * 物资收发结存统计处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class SfjcManager extends HibernateEntityDao<Sfjc>{
	/**
	 * 统计年月处理类
	 */
	private TjnyManager tjnyManager;
	/**
	 * 物资档案处理类
	 */
	private WzdaManager wzdaManager;
	//返回报表最近月份
	private int maxYear;
	//返回报表最近年份
	private int maxMoth;
	
	/**
	 * 返回当前报表的 合计
	 * @param nf 年份
	 * @param yf 月份
	 * @param ck 仓库
	 * @param flag 功能查询标记
	 * @param wzid 物资id
	 * @return 
	 * @throws Exception 
	 * 废弃方法
	 */
//	public void showCount(String nf,String yf,String ck,String flag,String wzid,Map countMap,String countFlag,List list) {
//		//查询当前报表中 最近的 报表
//		try {
//			this.getSfjcByZj();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		StringBuffer hql = new StringBuffer( "from Sfjc obj  where 1=1 ");
//		hql.append((nf !=null)?" and obj.nf="+nf:" and obj.nf="+this.getMaxYear());
//		hql.append((yf!=null)?" and obj.yf ="+yf:" and obj.yf ="+this.getMaxMoth());
//		hql.append((ck!=null)?" and obj.ck.id='"+ck+"'":"");
//		if(flag!=null){
//			if("crkkc".equals(flag)){
//				wzid = wzid.replace("-", "'");
//				wzid = wzid.substring(0,wzid.length()-1);
//				HqlUtil.addCondition(hql,"BQSRSL","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_GREAT);
//				HqlUtil.addCondition(hql,"BQZCSL","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_GREAT);
//				HqlUtil.addCondition(hql,"wz.id",wzid,HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
//			}
//			//有库存的物资
//			if("kc".equals(flag)){
//				wzid = wzid.replace("-", "'");
//				wzid = wzid.substring(0,wzid.length()-1);
//				HqlUtil.addCondition(hql,"wz.id",wzid,HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
//			}
//			//没有出入库的物资
//			if("ncrk".equals(flag)){
//				HqlUtil.addCondition(hql,"BQSRSL","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_EQ);
//				HqlUtil.addCondition(hql,"BQZCSL","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_EQ);
//			}
//			//月末库存为负的物资
//			if("nkc".equals(flag)){
//				HqlUtil.addCondition(hql,"BQJYSL","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_LESS);
//			}
//		}
//		//如果map为空 则是导出excel使用
//		if(countMap==null){
//			 this.setCountValue(list,countMap,countFlag);
//		}else{
//			List<Sfjc> sfjcList =this.find(hql.toString());
//			 this.setCountValue(sfjcList,countMap,countFlag);
//		}
//	}
	 
	/**
	 * 处理数据合计功能
	 */
	public void setCountValue(ResultSet res,List list,List<Sfjc> sfjcList,Map<String,Double> countMap,String countFlag){
		Sfjc sf = new Sfjc();
		DecimalFormat decimalFormat = new DecimalFormat("###.000");
		if(res!=null){
			//使用sql统计方式 
			this.getCountBySql(sf,res);
		}else{
			//使用sql统计方式 分页
			this.getCountBySqlPage(sf, list);
		}
	 
		//添加到当前页合计数据 往后翻页
		if("d".equals(countFlag)){
			countMap.put("bqjyje",Double.valueOf(decimalFormat.format(sf.getBqjyje()))+Double.valueOf(decimalFormat.format(countMap.get("bqjyje"))));
			countMap.put("bqjysl",Double.valueOf(decimalFormat.format(sf.getBqjysl()))+Double.valueOf(decimalFormat.format(countMap.get("bqjysl"))));
			countMap.put("bqsrje",Double.valueOf(decimalFormat.format(sf.getBqsrje()))+Double.valueOf(decimalFormat.format(countMap.get("bqsrje"))));
			countMap.put("bqsrsl",Double.valueOf(decimalFormat.format(sf.getBqsrsl()))+Double.valueOf(decimalFormat.format(countMap.get("bqsrsl"))));
			countMap.put("bqzcje",Double.valueOf(decimalFormat.format(sf.getBqzcje()))+Double.valueOf(decimalFormat.format(countMap.get("bqzcje"))));
			countMap.put("bqzcsl",Double.valueOf(decimalFormat.format(sf.getBqzcsl()))+Double.valueOf(decimalFormat.format(countMap.get("bqzcsl"))));
			countMap.put("sqjcje",Double.valueOf(decimalFormat.format(sf.getSqjcje()))+Double.valueOf(decimalFormat.format(countMap.get("sqjcje"))));
			countMap.put("sqjcsl",Double.valueOf(decimalFormat.format(sf.getSqjcsl()))+Double.valueOf(decimalFormat.format(countMap.get("sqjcsl"))));
			//本期转入金额 本期转入数量
			countMap.put("bqzrje",Double.valueOf(decimalFormat.format(sf.getBqzrje()))+Double.valueOf(decimalFormat.format(countMap.get("bqzrje"))));
			countMap.put("bqzrsl",Double.valueOf(decimalFormat.format(sf.getBqzrsl()))+Double.valueOf(decimalFormat.format(countMap.get("bqzrsl"))));
		}
		//添加到当前页合计数据 往前翻页
		if("q".equals(countFlag)){
			countMap.put("bqjyje",Double.valueOf(decimalFormat.format(countMap.get("bqjyje")))-Double.valueOf(decimalFormat.format(sf.getBqjyje())));
			countMap.put("bqjysl",Double.valueOf(decimalFormat.format(countMap.get("bqjysl")))-Double.valueOf(decimalFormat.format(sf.getBqjysl())));
			countMap.put("bqsrje",Double.valueOf(decimalFormat.format(countMap.get("bqsrje")))-Double.valueOf(decimalFormat.format(sf.getBqsrje())));
			countMap.put("bqsrsl",Double.valueOf(decimalFormat.format(countMap.get("bqsrsl")))-Double.valueOf(decimalFormat.format(sf.getBqsrsl())));
			countMap.put("bqzcje",Double.valueOf(decimalFormat.format(countMap.get("bqzcje")))-Double.valueOf(decimalFormat.format(sf.getBqzcje())));
			countMap.put("bqzcsl",Double.valueOf(decimalFormat.format(countMap.get("bqzcsl")))-Double.valueOf(decimalFormat.format(sf.getBqzcsl())));
			countMap.put("sqjcje",Double.valueOf(decimalFormat.format(countMap.get("sqjcje")))-Double.valueOf(decimalFormat.format(sf.getSqjcje())));
			countMap.put("sqjcsl",Double.valueOf(decimalFormat.format(countMap.get("sqjcsl")))-Double.valueOf(decimalFormat.format(sf.getSqjcsl())));
			//本期转入金额 本期转入数量
			countMap.put("bqzrje",Double.valueOf(decimalFormat.format(countMap.get("bqzrje")))-Double.valueOf(decimalFormat.format(sf.getBqzrje())));
			countMap.put("bqzrsl",Double.valueOf(decimalFormat.format(countMap.get("bqzrsl")))-Double.valueOf(decimalFormat.format(sf.getBqzrsl())));
		}
		//添加总合计数据
		if("h".equals(countFlag)){
			//添加统计到合计数据
			countMap.put("bqjyje",Double.valueOf(decimalFormat.format(sf.getBqjyje())));
			countMap.put("bqjysl",Double.valueOf(decimalFormat.format(sf.getBqjysl())));
			countMap.put("bqsrje",Double.valueOf(decimalFormat.format(sf.getBqsrje())));
			countMap.put("bqsrsl",Double.valueOf(decimalFormat.format(sf.getBqsrsl())));
			countMap.put("bqzcje",Double.valueOf(decimalFormat.format(sf.getBqzcje())));
			countMap.put("bqzcsl",Double.valueOf(decimalFormat.format(sf.getBqzcsl())));
			countMap.put("sqjcje",Double.valueOf(decimalFormat.format(sf.getSqjcje())));
			countMap.put("sqjcsl",Double.valueOf(decimalFormat.format(sf.getSqjcsl())));
			countMap.put("bqzrje",Double.valueOf(decimalFormat.format(sf.getBqzrje())));
			countMap.put("bqzrsl",Double.valueOf(decimalFormat.format(sf.getBqzrsl())));
		}
	}
	//根据SQL记录集统计总合计
	public void getCountBySql(Sfjc sf,ResultSet res){
		try{
			//System.out.println(res.next());
			DecimalFormat decimalFormat = new DecimalFormat("###.000");
			while(res.next()){
				//System.out.println(res.getDouble(3));
				if(sf.getSqjcsl()!=null){	
					sf.setSqjcsl(Double.valueOf(decimalFormat.format(res.getDouble(3)))+Double.valueOf(decimalFormat.format(sf.getSqjcsl())));
				}else{
					sf.setSqjcsl(res.getDouble(3));
				}
				if(sf.getSqjcje()!=null){
					sf.setSqjcje(Double.valueOf(decimalFormat.format(res.getDouble(4)))+Double.valueOf(decimalFormat.format(sf.getSqjcje())));
				}else{
					sf.setSqjcje(res.getDouble(4));
				}
				if(sf.getBqsrsl()!=null){
					sf.setBqsrsl(Double.valueOf(decimalFormat.format(res.getDouble(5)))+Double.valueOf(decimalFormat.format(sf.getBqsrsl())));
				}else{
					sf.setBqsrsl(res.getDouble(5));
				}
				if(sf.getBqsrje()!=null){
					sf.setBqsrje(Double.valueOf(decimalFormat.format(res.getDouble(6)))+Double.valueOf(decimalFormat.format(sf.getBqsrje())));
				}else{
					sf.setBqsrje(res.getDouble(6));
				}
				if(sf.getBqzcsl()!=null){
					sf.setBqzcsl(Double.valueOf(decimalFormat.format(res.getDouble(7)))+Double.valueOf(decimalFormat.format(sf.getBqzcsl())));
				}else{
					sf.setBqzcsl(res.getDouble(7));
				}
				if(sf.getBqzcje()!=null){
					sf.setBqzcje(Double.valueOf(decimalFormat.format(res.getDouble(8)))+Double.valueOf(decimalFormat.format(sf.getBqzcje())));
				}else{
					sf.setBqzcje(res.getDouble(8));
				}
				if(sf.getBqjysl()!=null){
					sf.setBqjysl(Double.valueOf(decimalFormat.format(res.getDouble(9)))+Double.valueOf(decimalFormat.format(sf.getBqjysl())));
				}else{
					sf.setBqjysl(res.getDouble(9));
				}
				if(sf.getBqjyje()!=null){
					sf.setBqjyje(Double.valueOf(decimalFormat.format(res.getDouble(10)))+Double.valueOf(decimalFormat.format(sf.getBqjyje())));
				}else{
					sf.setBqjyje(res.getDouble(10));
				}
				
			 	 //本期转入数量
	        	 if(sf.getBqzrsl()!=null){
	        		if(res.getDouble(11)==0.0){
	        			sf.setBqzrsl(Double.valueOf(0.0)+Double.valueOf(decimalFormat.format(sf.getBqzrsl())));
	        		}else if(sf.getBqzrsl()==0.0){
//	        			System.out.println("----------->"+res.getDouble(11));
//	        			System.out.println("===========>"+sf.getBqzrsl());
//	        			System.out.println("-----total---->"+res.getDouble(11)+sf.getBqzrsl());
	        			sf.setBqzrsl(Double.valueOf(decimalFormat.format(res.getDouble(11)))+Double.valueOf(0.0));
	        		}else{
	        			sf.setBqzrsl(Double.valueOf(decimalFormat.format(res.getDouble(11)))+Double.valueOf(decimalFormat.format(sf.getBqzrsl())));
	        		}
				 }else{
					sf.setBqzrsl(res.getDouble(11));
				 }
	        	 //本期转入金额
	        	 if(sf.getBqzrje()!=null){
	        		 if(res.getDouble(12)==0.0){
	        			 sf.setBqzrje(Double.valueOf(0.0)+Double.valueOf(decimalFormat.format(sf.getBqzrje())));
	        		 }else if(sf.getBqzrje()==0.0){
	        			 sf.setBqzrje(Double.valueOf(decimalFormat.format(res.getDouble(12)))+Double.valueOf(0.0));
	        		 }else{
	        			 sf.setBqzrje(Double.valueOf(decimalFormat.format(res.getDouble(12)))+Double.valueOf(decimalFormat.format(sf.getBqzrje())));
	        		 }
				 }else{
					sf.setBqzrje(res.getDouble(12));
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//根据SQL记录集统计总合计
	public void getCountBySqlPage(Sfjc sf,List list){
		DecimalFormat decimalFormat = new DecimalFormat("###.0000");
		 Double bqjyje = null;
		 Double bqjysl = null;
		 Double bqsrje = null;
		 Double bqsrsl = null;
		 Double bqzcje = null;
		 Double bqzcsl = null;
		 Double sqjcje = null;
		 Double sqjcsl = null;
		 Double bqzrsl = null;
		 Double bqzrje = null;
		for(int i = 0;i<list.size();i++){
			Map objectMap = (HashMap)list.get(i);
			Map sfjcMap = new HashMap();
	    	 bqjyje = (objectMap.get("BQJYJE")==null)?0:((BigDecimal) objectMap.get("BQJYJE")).doubleValue();
	    	 if(sf.getBqjyje()!=null){
				sf.setBqjyje(Double.valueOf(decimalFormat.format(bqjyje))+Double.valueOf(decimalFormat.format(sf.getBqjyje())));
			 }else{
				 sf.setBqjyje(bqjyje);
			 }
	    	 
        	 bqjysl = (objectMap.get("BQJYSL")==null)?0:((BigDecimal) objectMap.get("BQJYSL")).doubleValue();
        	 if(sf.getBqjysl()!=null){
				sf.setBqjysl(Double.valueOf(decimalFormat.format(bqjysl))+Double.valueOf(decimalFormat.format(sf.getBqjysl())));
			 }else{
				sf.setBqjysl(bqjysl);
			 }
        	 
        	 bqsrje = (objectMap.get("BQSRJE")==null)?0:((BigDecimal) objectMap.get("BQSRJE")).doubleValue();
        	 if(sf.getBqsrje()!=null){
				sf.setBqsrje(Double.valueOf(decimalFormat.format(bqsrje))+Double.valueOf(decimalFormat.format(sf.getBqsrje())));
			 }else{
				sf.setBqsrje(bqsrje);
			 }
        	 
        	 bqsrsl = (objectMap.get("BQSRSL")==null)?0:((BigDecimal) objectMap.get("BQSRSL")).doubleValue();
        	 if(sf.getBqsrsl()!=null){
				sf.setBqsrsl(Double.valueOf(decimalFormat.format(bqsrsl))+Double.valueOf(decimalFormat.format(sf.getBqsrsl())));
			 }else{
				sf.setBqsrsl(bqsrsl);
			 }
        	 
        	 bqzcje = (objectMap.get("BQZCJE")==null)?0:((BigDecimal) objectMap.get("BQZCJE")).doubleValue();
        	 if(sf.getBqzcje()!=null){
				sf.setBqzcje(Double.valueOf(decimalFormat.format(bqzcje))+Double.valueOf(decimalFormat.format(sf.getBqzcje())));
			 }else{
				sf.setBqzcje(bqzcje);
			 }
        	 
        	 bqzcsl = (objectMap.get("BQZCSL")==null)?0:((BigDecimal) objectMap.get("BQZCSL")).doubleValue();
        	 if(sf.getBqzcsl()!=null){
				sf.setBqzcsl(Double.valueOf(decimalFormat.format(bqzcsl))+Double.valueOf(decimalFormat.format(sf.getBqzcsl())));
			 }else{
				sf.setBqzcsl(bqzcsl);
			 }
        	 
        	 sqjcje = (objectMap.get("SQJCJE")==null)?0:((BigDecimal) objectMap.get("SQJCJE")).doubleValue();
        	 if(sf.getSqjcje()!=null){
				sf.setSqjcje(Double.valueOf(decimalFormat.format(sqjcje))+Double.valueOf(decimalFormat.format(sf.getSqjcje())));
			 }else{
				sf.setSqjcje(sqjcje);
			 }
        	 
        	 sqjcsl = (objectMap.get("SQJCSL")==null)?0:((BigDecimal) objectMap.get("SQJCSL")).doubleValue();
        	 if(sf.getSqjcsl()!=null){
				sf.setSqjcsl(Double.valueOf(decimalFormat.format(sqjcsl))+Double.valueOf(decimalFormat.format(sf.getSqjcsl())));
			 }else{
				sf.setSqjcsl(sqjcsl);
			 }
        	 //本期转入数量
        	 bqzrsl = (objectMap.get("BQZRSL")==null)?0:((BigDecimal) objectMap.get("BQZRSL")).doubleValue();
        	 if(sf.getBqzrsl()!=null){
				sf.setBqzrsl(Double.valueOf(decimalFormat.format(bqzrsl))+Double.valueOf(decimalFormat.format(sf.getBqzrsl())));
			 }else{
				sf.setBqzrsl(bqzrsl);
			 }
        	 //本期转入金额
        	 bqzrje = (objectMap.get("BQZRJE")==null)?0:((BigDecimal) objectMap.get("BQZRJE")).doubleValue();
        	 if(sf.getBqzrje()!=null){
				sf.setBqzrje(Double.valueOf(decimalFormat.format(bqzrje))+Double.valueOf(decimalFormat.format(sf.getBqzrje())));
			 }else{
				sf.setBqzrje(bqzrje);
			 }
		}
	}
	//根据Hql记录集统计总合计
	public void getCountByHql(Sfjc sf,List<Sfjc> sfjcList){
		for(Sfjc sfjc:sfjcList){
			if(sf.getSqjcsl()!=null){
				sf.setSqjcsl(sfjc.getSqjcsl()+sf.getSqjcsl());
			}else{
				sf.setSqjcsl(sfjc.getSqjcsl());
			}
			if(sf.getSqjcje()!=null){
				sf.setSqjcje(sfjc.getSqjcje()+sf.getSqjcje());
			}else{
				sf.setSqjcje(sfjc.getSqjcje());
			}
			if(sf.getBqsrsl()!=null){
				sf.setBqsrsl(sfjc.getBqsrsl()+sf.getBqsrsl());
			}else{
				sf.setBqsrsl(sfjc.getBqsrsl());
			}
			if(sf.getBqsrje()!=null){
				sf.setBqsrje(sfjc.getBqsrje()+sf.getBqsrje());
			}else{
				sf.setBqsrje(sfjc.getBqsrje());
			}
			if(sf.getBqzcsl()!=null){
				sf.setBqzcsl(sfjc.getBqzcsl()+sf.getBqzcsl());
			}else{
				sf.setBqzcsl(sfjc.getBqzcsl());
			}
			if(sf.getBqzcje()!=null){
				sf.setBqzcje(sfjc.getBqzcje()+sf.getBqzcje());
			}else{
				sf.setBqzcje(sfjc.getBqzcje());
			}
			if(sf.getBqjysl()!=null){
				sf.setBqjysl(sfjc.getBqjysl()+sf.getBqjysl());
			}else{
				sf.setBqjysl(sfjc.getBqjysl());
			}
			if(sf.getBqjyje()!=null){
				sf.setBqjyje(sfjc.getBqjyje()+sf.getBqjyje());
			}else{
				sf.setBqjyje(sfjc.getBqjyje());
			}
		}
	}
	/**
	 * 新增物资收发结存
	 * 传入参数 当前年份 当前月份 上一月年份 上一月月份
	 * @param nf
	 * @param yf
	 */
	public void addSfjc(String staRi,Tjrw tjrw){
		//System.out.println("开始执行");
		//本期收入数量，本期收入金额，本期支出数量，本期支出金额
		double bqsrsl=0;
		double bqzcsl=0;
		double bqsrje = 0;
		double bqzcje = 0;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int count = 0;
		String nf = tjrw.getNf();
		String yf = tjrw.getYf();
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			//查询是否已经生成报表
			List nylist = tjnyManager.find("from Tjny t where t.ny = '"+nf+"年"+yf+"月' and t.bblb = '"+tjrw.getRwlb()+"'");
			//如果有 则删除重建
			if(nylist.size()>0){
				for(int i=0;i<nylist.size();i++){
					tjnyManager.remove(nylist.get(i));
				}
			} 
			Tjny tjny = new Tjny();
			//先保存报表年月对象
			tjny.setNy(nf+"年"+yf+"月");
			tjny.setBblb(tjrw.getRwlb());
			tjnyManager.save(tjny); 
			
			
			//查询所有物资
			String sql = "select da.id,ck.id from tb_wz_swzda da,tb_wz_skwgl kw,tb_wz_ckgl ck where kw.ckid = ck.id and da.kwbm=kw.id order by ck.id";
			rs = st.executeQuery(sql);
		   int i =0;
			while (rs.next()) {
				count++;
				//物资id
				String wzid = rs.getString(1);
				//仓库Id
				String ckid = rs.getString(2);
				//创建一个物资结存对象
				Sfjc sfjc = new Sfjc();
				//入库Map
				Map<String,Double> rkMap = this.getRkMap(tjrw,staRi,wzid);
				//出库Map
				Map<String,Double> ckMap = this.getCkMap(tjrw,staRi, wzid);
				//当前物资 支出数量 金额 sql
//				String cksql = this.getCrkSQL(nf, yf, wzid,"2");
//				//当前物资 收入数量 金额 sql
//				String rksql = this.getCrkSQL(nf, yf, wzid,"1");
				//获取需求计划
				String xqjhsql = this.getXqsqId(wzid);
				st = conn.createStatement();
				ResultSet rsxq = st.executeQuery(xqjhsql);
				while(rsxq.next()){
					String xqid = rsxq.getString(1);
					if(xqid!=null){
						//获取需求班组以及需求项目
						String xmSql = this.getGcBz(xqid);
						st = this.getSta(st, conn);
						ResultSet rsgc = st.executeQuery(xmSql);
						while(rsgc.next()){
							sfjc.setZjllgcxm(rsgc.getString(1));
							sfjc.setZjllbz(rsgc.getString(2));
							sfjc.setZjllgclb(rsgc.getString(3));
						}
					}
					break;
				}
				st.close();
				//添加仓库
				Ckgl ck = (Ckgl)this.find("from Ckgl c where c.id='"+ckid+"'").get(0);
				sfjc.setCk(ck);
				//添加物资
				Wzda wz = (Wzda)this.find("from Wzda w where w.id='"+wzid+"'").get(0);
				sfjc.setWz(wz);
				
				//出库
//				st = this.getSta(st, conn);
//				ResultSet rsck = st.executeQuery(cksql);
//				while(rsck.next()){
//					bqzcsl = rsck.getFloat(1);
//				}
				bqzcsl = ckMap.get("countNums") ;
				bqzcje = ckMap.get("countMoney") ;
				//入库
//				st = this.getSta(st, conn);
//				ResultSet rsrk = st.executeQuery(rksql);
//				while(rsrk.next()){
//					bqsrsl = rsrk.getFloat(1);
//				}
				bqsrsl = rkMap.get("countNums") ;
				bqsrje = rkMap.get("countMoney") ;
				//添加本期支出数量
				sfjc.setBqzcsl(bqzcsl);
				//添加本期支出金额
				sfjc.setBqzcje(ckMap.get("countMoney") );
				//添加本期收入数量
				sfjc.setBqsrsl(bqsrsl);
				//添加本期收入金额
				sfjc.setBqsrje(rkMap.get("countMoney") );
				//添加上期结余数量，结余金额
				//取得上个月的报表对象
				String hql = "from Sfjc s where s.wz.id='"+wzid+"' and to_date(s.tjsj,'yyyy-mm') = add_months(to_date('"+nf+"-"+yf+"','yyyy-mm'),-1)";
//				if(ck.getCkmc().equals("备品库")){
//					System.out.println("备品库："+i++);
//				}
				List list = this.find(hql);
//				if(wz.getId().equals("8a65808d2fbb679d012fbde2c4cd010b")||wz.getId().equals("8a65808d2fbb679d012fbde150b7010a")){
//					System.out.println(wz.getWzmc()+":"+hql);
//					System.out.println("上期结存数量："+list.size());
//				}
				//转入金额
				double zrje = wz.getOldwzkc()*(wz.getSjdj()+wz.getJhdj());
				//格式化
				BigDecimal zb = new BigDecimal(zrje);
				BigDecimal one = new BigDecimal(1);
				zrje = zb.divide(one,3,BigDecimal.ROUND_HALF_UP).doubleValue();
				//如果上个月没有记录--仅供开发阶段使用
				if(list.size()<1){
					sfjc.setSqjcsl(0.0);
					sfjc.setSqjcje(0.0);
					if("2011".equals(nf)&&"10".equals(yf)){
						sfjc.setBqzrsl(wz.getOldwzkc());
						sfjc.setBqzrje(zrje);
						//本期结余数量
						sfjc.setBqjysl(bqsrsl-bqzcsl+wz.getOldwzkc());
						//本期结余金额
						sfjc.setBqjyje(bqsrje-bqzcje+zrje);
					}else{
						//本期结余数量
						sfjc.setBqjysl(bqsrsl-bqzcsl);
						//本期结余金额
						sfjc.setBqjyje(bqsrje-bqzcje);
					}
				}else{
					Sfjc sfjcq = (Sfjc)list.get(0);
					//将上个月的结余数量，金额添加进去
					sfjc.setSqjcsl(sfjcq.getBqjysl());
					sfjc.setSqjcje(sfjcq.getBqjyje());
					//如果本期支出数量 等于上期结余的数量 且 本期收入数量为0的时候 则本期支出金额 就等于上期结余金额
					if(sfjcq.getBqjysl()==bqzcsl&&bqsrsl==0){
						//本期支出金额 就为上期结余金额
						sfjc.setBqzcje(sfjcq.getBqjyje());
						if("2011".equals(nf)&&"10".equals(yf)){
							sfjc.setBqzrsl(wz.getOldwzkc());
							sfjc.setBqzrje(zrje);
							//本期结余数量
							sfjc.setBqjysl(bqzcsl-sfjcq.getBqjysl()+wz.getOldwzkc());
							sfjc.setBqjyje(sfjcq.getBqjyje()-sfjcq.getBqjyje()+zrje);
						}else{
							//本期结余数量 就为本期支出数量-上期结余数量
							sfjc.setBqjysl(bqzcsl-sfjcq.getBqjysl());
							//本期结余金额 就为上期结余金额 -上期结余金额
							sfjc.setBqjyje(sfjcq.getBqjyje()-sfjcq.getBqjyje());
						}
					}else
					//如果本期收入数量 = 本期支出数量 并且上期结余数量为0 则本期支出金额 等于本期收入金额
					if(bqsrsl==bqzcsl&&sfjcq.getBqjysl()==0){
						sfjc.setBqzcje(bqsrje);
						if("2011".equals(nf)&&"10".equals(yf)){
							//2011-10 月份统计方法
							//本期转入数量 转入金额
							sfjc.setBqzrsl(wz.getOldwzkc());
							sfjc.setBqzrje(zrje);
							//结余数量 结余金额
							sfjc.setBqjysl(wz.getOldwzkc()+bqsrsl-bqzcsl);
							sfjc.setBqjyje((bqsrje-bqzcje+zrje<=0.01)?0:bqsrje-bqzcje+zrje);
						}else{
							//正常统计方法
							sfjc.setBqjysl(bqsrsl-bqzcsl);
							sfjc.setBqjyje((bqsrje-bqzcje<=0.01)?0:bqsrje-bqzcje);
						}
					}else{
						if("2011".equals(nf)&&"10".equals(yf)){
							//2011-10月统计使用 以后注释 使用正常统计方法
							sfjc.setBqzrsl(wz.getOldwzkc());
							sfjc.setBqzrje(zrje);
							//结余数量 结余金额
							sfjc.setBqjysl(wz.getOldwzkc()+bqsrsl-bqzcsl+sfjcq.getBqjysl());
							sfjc.setBqjyje((bqsrje-bqzcje+sfjcq.getBqjyje()+zrje<=0.01)?0:bqsrje-bqzcje+sfjcq.getBqjyje()+zrje); 
						}else{
							//本期结余数量 正常统计方法 2011-10月份统计注释
							sfjc.setBqjysl(bqsrsl-bqzcsl+sfjcq.getBqjysl());
							//本期结余金额
							sfjc.setBqjyje((bqsrje-bqzcje+sfjcq.getBqjyje()<=0.01)?0:bqsrje-bqzcje+sfjcq.getBqjyje());
						}
					}
				}
				//年份
				sfjc.setNf(nf);
				sfjc.setYf(yf);
				//添加统计年月
				sfjc.setTjny(tjny);
				sfjc.setTjsj(tjrw.getNf()+"-"+tjrw.getYf());
				///修改统计任务对象的备注信息
				st = this.getSta(st, conn);
				//System.out.println("update tb_wz_tjrw t set t.bz = '正在统计:"+ck.getCkmc()+"仓库"+wz.getWzmc()+"' where t.id = '"+tjrw.getId()+"'");
				st.executeUpdate("update tb_wz_tjrw t set t.bz = '正在统计:"+ck.getCkmc()+"仓库"+wz.getWzmc().replace("'", " ")+"' where t.id = '"+tjrw.getId()+"'");
				this.save(sfjc);
				st.close();
			}
			System.out.println("执行完毕");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{	
			try {
				if(rs != null){
					rs.close();
				}
				if(st!=null){
					st.close();
				}
				if(conn != null ){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 返回最近报表年月
	 */
	public void getSfjcByZj(){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			//查询所有物资
			String sql = "select max(nf),max(yf) from Tb_Wz_Twzsfjcb";
			rs = st.executeQuery(sql);
			while (rs.next()){
				this.maxYear = rs.getInt(1);
				this.maxMoth = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 根据仓库Id 年份月份 返回当前仓库 指定年月的月末库存
	 * @param wzid
	 * @param nf
	 * @param yf
	 * @return
	 */
	public double getYmkc(String wzid,int nf,int yf){
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		String sql = "SELECT T.YMKC FROM TB_WZ_YMKC T WHERE T.ID = '"+wzid+"' AND " +
				"TO_DATE(TO_CHAR(T.SJ,'yyyy-mm'),'YYYY-MM') = TO_DATE('"+nf+"-"+yf+"','yyyy-mm')";
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			res = st.executeQuery(sql);
			while(res.next()){
				return res.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	 /**
	 * 
	 *描述：通过sql进行分页查询
	 *时间：2010-8-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean pagedQueryTableData(String sql, int start, int limit,int total,BufferedWriter writer,List columnList,String id) throws Exception {
		
		DataSource dataSource=(DataSource)SpringContextUtil.getBean("dataSource");
		Connection conn=dataSource.getConnection();
		try{
			Statement st=conn.createStatement();
			if(total<1)
				return false;

			sql="SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit-1)+") WHERE RN >= "+start;
			//System.out.println("id:"+id+",start:"+start+",limit:"+limit+",sql查询开始,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			ResultSet rs=st.executeQuery(sql);
			//System.out.println("id:"+id+",start:"+start+",limit:"+limit+",sql查询结束,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			ResultSetMetaData rsmeta=rs.getMetaData();
			HashMap<String, String> columnMap=new HashMap<String, String>();
			for(int i=1;i<=rsmeta.getColumnCount();i++){
				columnMap.put(rsmeta.getColumnName(i).toUpperCase(), rsmeta.getColumnName(i).toUpperCase());
			}
			//System.out.println("id:"+id+",start:"+start+",limit:"+limit+",写入数据开始,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			while(rs.next()){
				StringBuffer data=new StringBuffer();
				data.append("<Row>");
				for(Object obj:columnList){
					if(obj instanceof ExportExcelModel){
						ExportExcelModel model=(ExportExcelModel)obj;
						
						Object value= null;
						String proName="";
						if(StringUtils.isNotEmpty(model.getColumncode())){
							proName=model.getColumncode();
						}else if(StringUtils.isEmpty(model.getColumncode())&&StringUtils.isNotEmpty(model.getColumncode())){
							proName=model.getColumncode();
						}else{
							continue;
						}
						if("id".equals(proName.toLowerCase())||"jlid".equals(proName.toLowerCase())){
							continue;
						}
						if(columnMap.containsKey(proName.toUpperCase())){
							value=rs.getObject(proName.toUpperCase());
						}
						value=this.specialField(rs, proName, value==null?"":value.toString());
						data.append("<Cell ss:StyleID='"+getExcelStyleByDbType(model.getColumntype())+"'><Data ss:Type='"+this.getExcelTypeByDbType(model.getColumntype())+"'>"+getExcelValueByDbType(model.getColumntype(), value)+"</Data></Cell>");
					} 
				}
				data.append("</Row>");
				System.out.println("data："+data.toString());
				writer.append(data.toString());
				writer.flush();
			}
			//System.out.println("id:"+id+",start:"+start+",limit:"+limit+",写入数据结束,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			rs.close();
			st.close();
			return true;
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
	}
	/**
	 * 
	 * 描述:通过hql进行分页查询
	 * 时间:2010 11 25
	 * 作者:童贝
	 * 参数:
	 * 返回值:boolean
	 * 抛出异常:
	 * @throws IOException 
	 */
	public boolean pagedQueryTableDataByHql(String hql, int start, int limit,int total,BufferedWriter writer,List columnList,String id) throws Exception{
		Assert.hasText(hql);
		Assert.isTrue(start >= 0, "pageNo should start from 1");
		// Count查询
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		getHibernateTemplate().setCacheQueries(false);
		List countlist = getHibernateTemplate().find(countQueryString);
		Integer totalCount = (Integer) countlist.get(0);
		if (totalCount < 1)
			return false;
		Query query = createQuery(hql);
//		query.setCacheable(true);
		List list = query.setFirstResult(start).setMaxResults(start+limit-1).list();
		Page page= new Page(start, total, start+limit-1, list);
		Collection objs = (Collection) page.getResult();
		
		for(Object obj:objs){
			StringBuffer data=new StringBuffer();
			data.append("<Row>");
			for(Object column:columnList){
				if(column instanceof ExportExcelModel){
					try{
						ExportExcelModel model=(ExportExcelModel)column;
						String proName="";
						proName=model.getColumncode();
			 
						if(!proName.equals(model.getColumncode())){
							proName=model.getColumncode();
						}
						Field field=BeanUtils.getDeclaredField(obj, proName);
						if(field!=null){
							Object value=BeanUtils.getProperty2(obj,proName);
							data.append("<Cell ss:StyleID='"+getExcelStyleByDbType(model.getColumntype())+"'><Data s:Type='"+this.getExcelTypeByDbType(model.getColumntype())+"'>"+getExcelValueByDbType(model.getColumntype(), value)+"</Data></Cell>");
						}
					}catch(Exception e){
						//System.out.println("跑一场了");
						data.append("<Cell ss:StyleID='"+getExcelStyleByDbType("")+"'><Data ss:Type='"+this.getExcelTypeByDbType("")

+"'>"+getExcelValueByDbType("", "")+"</Data></Cell>");
					}
				}
			}
			data.append("</Row>");
			writer.append(data.toString());
			writer.flush();
		}
		return true;
	}
	
	public String specialField(ResultSet rs,String fieldName,String value) throws SQLException {
			return value;
	}
	
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}
	
	
	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 *
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	
	
	public String getExcelTypeByDbType(String dbtype){
		String result=new String();
		if("VARCHAR2".equals(dbtype)){
			result="String";
		}else if("NUMBER".equals(dbtype)){
			result="Number";
		}else if("DATE".equals(dbtype)){
			result="String";
		}else{
			result="String";
		}
		return result;
	}
	
	public String getExcelStyleByDbType(String dbtype){
		String result=new String();
		if("VARCHAR2".equals(dbtype)){
			result="s21";
		}else if("NUMBER".equals(dbtype)){
			result="s21";
		}else if("DATE".equals(dbtype)){
			result="s21";//s22
		}else{
			result="s21";
		}
		return result;
	}
	public String getExcelValueByDbType(String dbtype,Object value){
		if(value==null){
			return "";
		}
		String result=new String();
		if("VARCHAR2".equals(dbtype)){
			result=value.toString();
		}else if("NUMBER".equals(dbtype)){
			result=value.toString();
		}else if("DATE".equals(dbtype)){
			result=value.toString();
		}else{
			result=value.toString();
		}
		return result;
	}

	/**
	 * 返回出入库日志
	 * 参数：年份，月份，物资id，出入库类别
	 * 废弃方法
	 * @return
	 */
	public String getCrkSQL(String nf,String yf,String id,String crklb){
		return "select sum(mx.CRKSL) from tb_wz_ycrkrzmx mx,tb_wz_ycrkrz rz where mx.CRKRZID = rz.id " +
				"and rz.CRKQF = '"+crklb+"' and mx.WZBM = '"+id+"' and rz.crksj > add_months(to_date('"+nf+"-"+yf+"-25','yyyy-mm-dd'),-1) and rz.crksj < to_date('"+nf+"-"+yf+"-25','yyyy-mm-dd')";
	}
	
	/**
	 * 返回入库 集合
	 */
	public Map<String,Double> getRkMap(Tjrw tjrw,String staRi,String id){
		String hql = "from Yhdmx y where y.wzdagl.id='"+id+"' " +
		" and  y.rksj >= add_months(to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+staRi+"','yyyy-mm-dd'),-1)" +
		" and  y.rksj < to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+tjrw.getRi()+"', 'yyyy-mm-dd') and y.zt = '1'";
		Map<String,Double> countMap = TjrwUtils.getRkMap(hql);
		return countMap;
		
	}
	
	/**
	 * 返回出库 集合
	 */
	public Map<String,Double> getCkMap(Tjrw tjrw,String staRi,String id){
		String hql = "from Lydmx l where l.wzbm.id='"+id+"' " +
		" and l.lysj >= add_months(to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+staRi+"', 'yyyy-mm-dd'),-1) " +
		" and l.lysj < to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+tjrw.getRi()+"', 'yyyy-mm-dd')" +
		" and l.zt = '1' ";
		Map<String,Double> countMap = TjrwUtils.getCkMap(hql);
		return countMap;
	}
	
	/**
	 * 根据物资id取出需求申请id
	 * @param wzid
	 * @return
	 */
	public String getXqsqId(String wzid){
		return "select t.XQJHBH from TB_WZ_XQJH_DETAIL t where t.WZBM='"+wzid+"' order by t.XYSJ desc";
	}
	/**
	 * 根据需求申请id 取出相关工程项目以及班组
	 * @return
	 */
	public String getGcBz(String xqid){
		return "select t.GCXM,t.SQBM,t.GCLB from TB_WZ_XQJH t where t.id='"+xqid+"'";
	}
	/**
	 * 获得 Statemen 如果st 不等于空 则先关闭 再打开
	 * @return
	 * @throws SQLException 
	 */
	public Statement getSta(Statement st,Connection conn) throws SQLException{
		if(st !=null){
			st.close();
		}
		return conn.createStatement();
	}
	
	
	public int getMaxYear() {
		return maxYear;
	}
	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}
	public int getMaxMoth() {
		return maxMoth;
	}
	public void setMaxMoth(int maxMoth) {
		this.maxMoth = maxMoth;
	}

	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	
}
