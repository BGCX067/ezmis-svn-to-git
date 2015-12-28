package com.jteap.jhtj.dnb.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.jhtj.dnb.model.Dnb;
import com.jteap.jhtj.dnb.model.DnbChange;
import com.jteap.jhtj.dnb.model.DnbData;

public class DnbBbManager extends HibernateEntityDao<com.jteap.jhtj.dnb.model.Dnb> {
	private DataSource dataSource;
	private DnbChangeManager dnbChangeManager;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void setDnbChangeManager(DnbChangeManager dnbChangeManager) {
		this.dnbChangeManager = dnbChangeManager;
	}
	
	/**
	 * 
	 * 描述:根据日期查询所有电能表的数据
	 * 时间:2010 11 16
	 * 作者:童贝
	 * 参数:
	 * 返回值:List<DnbData>
	 * 抛出异常:
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public String getDnbJsonByDate(String curDate) throws SQLException{
		StringBuffer result=new StringBuffer();
		Connection conn=null;
		try{
			conn=this.dataSource.getConnection();
			
			//所有电能表的ID
			String[] codes=DnbData.DNB_CODES.split(",");
			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curNian=DateUtils.getDate(curD, "yyyy");       //当前年
			String curYue=DateUtils.getDate(curD, "MM");		  //当前月
			String curYmd = DateUtils.formatDate(curD, "yyyy-MM-dd");
			
			String nextDateStr=DateUtils.endDate(curD);//当前时间的下一天
			Date nextDate=DateUtils.StrToDate(nextDateStr, "yyyy-MM-dd");
			String nextNian=DateUtils.getDate(nextDate, "yyyy");
			String nextYue=DateUtils.getDate(nextDate, "MM");
			
			for(String code:codes){
				//电表 ct、pt、表号
				int ct = 1;
				int pt = 1;
				String sql2="select * from elec.elec_param t where elecid='"+code+"'";
				Dnb dnb=this.operSql2(conn, sql2);
				if(dnb != null){
					result.append("{'CT-"+code+"':"+dnb.getCt()+","+
							"'PT-"+code+"':"+dnb.getPt()+","+
							"'CTS-"+code+"':'"+dnb.getCts()+"'," +
							"'PTS-"+code+"':'"+dnb.getPts()+"',"+
							"'ELECBH-"+code+"':'"+dnb.getElecbh()+"'},");
					ct = dnb.getCt();
					pt = dnb.getPt();
				}else {
					result.append("{'CT-"+code+"':"+1+","+
							"'PT-"+code+"':"+1+","+
							"'CTS-"+code+"':''," +
							"'PTS-"+code+"':'',"+
							"'ELECBH-"+code+"':''},");
				}
				
				//换表信息
				int oldCt = 1;
				int oldPt = 1;
				String hql = "from DnbChange t where t.elecId='"+code+"' and times like '"+curYmd+"%'";
				List<DnbChange> list = dnbChangeManager.find(hql);
				DnbChange dnbChange = null;
				Date changeDate = null;
				if(list.size() > 0){
					dnbChange = list.get(0);
					
					String[] ctsArray = dnbChange.getCtO().split("/");
					int ct1 = Integer.parseInt(ctsArray[0].trim());
					int ct2 = Integer.parseInt(ctsArray[1].trim());
					oldCt = ct1/ct2;
					
					String[] ptsArray = dnbChange.getPtO().split("/");
					int pt1 = Integer.parseInt(ptsArray[0].trim());
					int pt2 = Integer.parseInt(ptsArray[1].trim());
					oldPt = pt1/pt2;
					
					changeDate = DateUtils.parseDate(dnbChange.getTimes(), "yyyy-MM-dd HH:mm");
				}
				
				String sql00="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curYmd+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data00=this.operSql(conn, sql00);
				String sql08="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curYmd+" 8:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data08=this.operSql(conn, sql08);
				String sql16="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curYmd+" 16:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data16=this.operSql(conn, sql16);
				String sql24="select * from elec.elec_z_"+nextNian+nextYue+" where elecid="+code+" and times=to_date('"+nextDateStr+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data24=this.operSql(conn, sql24);
				
				Date date08 = DateUtils.parseDate(curYmd+" 08:00", "yyyy-MM-dd HH:mm");
				Date date16 = DateUtils.parseDate(curYmd+" 16:00", "yyyy-MM-dd HH:mm");
				
// 				【换表运算】
//				该块电表有功电量=老表有功电量+新表有功电量
//				老表有功电量=(（换表记录中的老表正向有功表码-换表记录中的老表反向有功表码）-（ELEC_Z_201012数据表中的1号正向有功表码-反向有功表码）)*换表记录中的老表CT*老表PT
//				新表有功电量=（（ELEC_Z_201012数据表中的当天24点正向有功表码-24点反向有功表码）-（换表记录中的新表正向有功表码-换表记录中的新表反向有功表码））*ELEC_PARAM中的CT*PT
				if(data00 != null && data08 != null){
					double total00 = 0d;
					
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() < date08.getTime()){
							//换表时间 00:00 ~ 08:00 进行换表运算
							double oldYgdl00 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data00.getPz() - data00.getPf()))*oldCt*oldPt;
							double newYgdl00 = ((data08.getPz() - data08.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
							total00 = oldYgdl00 + newYgdl00;
						}else{
							//换表时间 08:00 ~ 24:00 取旧表ct,pt
							total00 = ((data08.getPz() - data00.getPz()) - (data08.getPf() - data00.getPf()))*oldCt*oldPt;
						}
					}else {
						//无换表情况
						total00 = ((data08.getPz() - data00.getPz()) - (data08.getPf() - data00.getPf()))*ct*pt;
					}
					result.append("{'PZ-"+code+"-00':"+data00.getPz()+"," +
							"'PF-"+code+"-00':"+data00.getPf()+"," +
							"'QZ-"+code+"-00':"+data00.getQz()+"," +
							"'QF-"+code+"-00':"+data00.getQf()+","+
							"'TOTAL-"+code+"-00':"+total00+"},");
				}else {
					result.append("{'PZ-"+code+"-00':' '," +
							"'PF-"+code+"-00':''," +
							"'QZ-"+code+"-00':''," +
							"'QF-"+code+"-00':'',"+
							"'TOTAL-"+code+"-00':''},");
				}
				
				if(data08 != null && data16 != null){
					double total08 = 0d;
					
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() >= date08.getTime()){
							if(changeDate.getTime() < date16.getTime()){
								//换表时间 08:00 ~ 16:00 进行换表运算
								double oldYgdl08 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data08.getPz() - data08.getPf()))*oldCt*oldPt;
								double newYgdl08 = ((data16.getPz() - data16.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
								total08 = oldYgdl08 + newYgdl08;
							}else{
								//换表时间 16:00 ~ 24:00 取旧表的ct,pt
								total08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*oldCt*oldPt;
							}
						}else {
							//换表时间 0:00 ~ 08:00 取新表的ct,pt
							total08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*ct*pt;
						}
					}else {
						//无换表情况
						total08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*ct*pt;
					}
					
					result.append("{'PZ-"+code+"-08':"+data08.getPz()+"," +
							"'PF-"+code+"-08':"+data08.getPf()+"," +
							"'QZ-"+code+"-08':"+data08.getQz()+"," +
							"'QF-"+code+"-08':"+data08.getQf()+","+
							"'TOTAL-"+code+"-08':"+total08+"},");
				}else {
					result.append("{'PZ-"+code+"-08':' '," +
							"'PF-"+code+"-08':''," +
							"'QZ-"+code+"-08':''," +
							"'QF-"+code+"-08':'',"+
							"'TOTAL-"+code+"-08':''},");
				}
				
				if(data16 != null && data24 != null){
					double total16 = 0d;
					
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() >= date16.getTime()){
							//换表时间 16:00 ~ 24:00 进行换表运算
							double oldYgdl16 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data16.getPz() - data16.getPf()))*oldCt*oldPt;
							double newYgdl16 = ((data24.getPz() - data24.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
							total16 = oldYgdl16 + newYgdl16;
						}else {
							//换表时间 00:00 ~ 16:00 取新表ct,pt
							total16 = ((data24.getPz() - data16.getPz()) - (data24.getPf() - data16.getPf()))*ct*pt;
						}
					}else {
						//无换表情况
						total16 = ((data24.getPz() - data16.getPz()) - (data24.getPf() - data16.getPf()))*ct*pt;
					}
					
					result.append("{'PZ-"+code+"-16':"+data16.getPz()+"," +
							"'PF-"+code+"-16':"+data16.getPf()+"," +
							"'QZ-"+code+"-16':"+data16.getQz()+"," +
							"'QF-"+code+"-16':"+data16.getQf()+","+
							"'TOTAL-"+code+"-16':"+total16+"},");
				}else {
					result.append("{'PZ-"+code+"-16':' '," +
							"'PF-"+code+"-16':''," +
							"'QZ-"+code+"-16':''," +
							"'QF-"+code+"-16':'',"+
							"'TOTAL-"+code+"-16':''},");
				}
				
				if(data24 != null && data00 != null){
					double total24 = 0d;
					
					if(dnbChange != null){
						//只要当天有换表记录 24:00 的就得进行换表运算
						double oldYgdl24 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data00.getPz() - data00.getPf()))*oldCt*oldPt;
						double newYgdl24 = ((data24.getPz() - data24.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
						total24 = oldYgdl24 + newYgdl24;
					}else {
						//无换表运算
						total24 = ((data24.getPz() - data00.getPz()) - (data24.getPf() - data00.getPf()))*ct*pt;
					}
					
					result.append("{'PZ-"+code+"-24':"+data24.getPz()+"," +
							"'PF-"+code+"-24':"+data24.getPf()+"," +
							"'QZ-"+code+"-24':"+data24.getQz()+"," +
							"'QF-"+code+"-24':"+data24.getQf()+","+
							"'TOTAL-"+code+"-24':"+total24+"},");
				}else {
					result.append("{'PZ-"+code+"-24':' '," +
							"'PF-"+code+"-24':''," +
							"'QZ-"+code+"-24':''," +
							"'QF-"+code+"-24':'',"+
							"'TOTAL-"+code+"-24':''},");
				}
				
			}
			
			if(result.length()>0){
				result.deleteCharAt(result.length()-1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return result.toString();
	}
	
	/**
	 * 描述:根据日期查询所有电能表的数据 （为了导出Excel）
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDnbJsonByDateForExcel(String curDate) throws SQLException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		Connection conn=null;
		try{
			conn=this.dataSource.getConnection();
			
			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curNian=DateUtils.getDate(curD, "yyyy");       	//当前年
			String curYue=DateUtils.getDate(curD, "MM");		  	//当前月
			String curRi=DateUtils.getDate(curD, "dd");     		//当前日
			String curYmd = DateUtils.formatDate(curD, "yyyy-MM-dd");
			
			String nextDateStr=DateUtils.endDate(curD);				//当前时间的下一天
			Date nextDate=DateUtils.StrToDate(nextDateStr, "yyyy-MM-dd");
			String nextNian=DateUtils.getDate(nextDate, "yyyy");
			String nextYue=DateUtils.getDate(nextDate, "MM");
			
			//所有指标编码
			String[] codes=DnbData.DNB_CODES.split(",");
			for(int i=0; i<codes.length; i++){
				//指标编码
				String code = codes[i];
				//excel行号
				int row = DnbData.DNB_ROWS[i];
				
				//换表信息
				int oldCt = 1;
				int oldPt = 1;
				String hql = "from DnbChange t where t.elecId='"+code+"' and times like '"+curYmd+"%'";
				List<DnbChange> list = dnbChangeManager.find(hql);
				DnbChange dnbChange = null;
				Date changeDate = null;
				if(list.size() > 0){
					dnbChange = list.get(0);
					
					String[] ctsArray = dnbChange.getCtO().split("/");
					int ct1 = Integer.parseInt(ctsArray[0].trim());
					int ct2 = Integer.parseInt(ctsArray[1].trim());
					oldCt = ct1/ct2;
					
					String[] ptsArray = dnbChange.getPtO().split("/");
					int pt1 = Integer.parseInt(ptsArray[0].trim());
					int pt2 = Integer.parseInt(ptsArray[1].trim());
					oldPt = pt1/pt2;
					
					changeDate = DateUtils.parseDate(dnbChange.getTimes(), "yyyy-MM-dd HH:mm");
				}
				
				//表码率 ct*pt 、cts、pts、表号
				String sql2="select * from elec.elec_param t where elecid='"+code+"'";
				Dnb dnb=this.operSql2(conn, sql2);
				int ct = dnb.getCt();
				int pt = dnb.getPt();
				map.put(row+1+"-"+1+"-r"+"-r", dnb.getElecbh());
				map.put(row+2+"-"+1+"-r"+"-r", dnb.getCts());
				map.put(row+3+"-"+1+"-r"+"-r", dnb.getPts());
				
				String sql00="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data00=this.operSql(conn, sql00);
				String sql08="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 8:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data08=this.operSql(conn, sql08);
				String sql16="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 16:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data16=this.operSql(conn, sql16);
				String sql24="select * from elec.elec_z_"+nextNian+nextYue+" where elecid="+code+" and times=to_date('"+nextDateStr+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
				DnbData data24=this.operSql(conn, sql24);
				
				Date date08 = DateUtils.parseDate(curYmd+" 08:00", "yyyy-MM-dd HH:mm");
				Date date16 = DateUtils.parseDate(curYmd+" 16:00", "yyyy-MM-dd HH:mm");
				
				//0点
				if(data00 != null){
					map.put(row+"-"+3, data00.getPz());
					map.put(row+"-"+4, data00.getPf());
					map.put(row+"-"+5, data00.getQz());
					map.put(row+"-"+6, data00.getQf());
				}
				
				//8点
				if(data08 != null){
					map.put(row+1+"-"+3, data08.getPz());
					map.put(row+1+"-"+4, data08.getPf());
					map.put(row+1+"-"+5, data08.getQz());
					map.put(row+1+"-"+6, data08.getQf());
				}
				
				//16点
				if(data16 != null){
					map.put(row+2+"-"+3, data16.getPz());
					map.put(row+2+"-"+4, data16.getPf());
					map.put(row+2+"-"+5, data16.getQz());
					map.put(row+2+"-"+6, data16.getQf());
				}
				
				//24点
				if(data24 != null){
					map.put(row+3+"-"+3, data24.getPz());
					map.put(row+3+"-"+4, data24.getPf());
					map.put(row+3+"-"+5, data24.getQz());
					map.put(row+3+"-"+6, data24.getQf());
				}
				
// 				【换表运算】
//				该块电表有功电量=老表有功电量+新表有功电量
//				老表有功电量=(（换表记录中的老表正向有功表码-换表记录中的老表反向有功表码）-（ELEC_Z_201012数据表中的1号正向有功表码-反向有功表码）)*换表记录中的老表CT*老表PT
//				新表有功电量=（（ELEC_Z_201012数据表中的当天24点正向有功表码-24点反向有功表码）-（换表记录中的新表正向有功表码-换表记录中的新表反向有功表码））*ELEC_PARAM中的CT*PT
				Double ygzl00 = null;
				Double ygzl08 = null;
				Double ygzl16 = null;
				Double ygzl24 = null;
				
				if(data08 != null && data00 != null){
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() < date08.getTime()){
							//换表时间 00:00 ~ 08:00 进行换表运算
							double oldYgdl00 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data00.getPz() - data00.getPf()))*oldCt*oldPt;
							double newYgdl00 = ((data08.getPz() - data08.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
							ygzl00 = oldYgdl00 + newYgdl00;
						}else{
							//换表时间 08:00 ~ 24:00 取旧表ct,pt
							ygzl00 = ((data08.getPz() - data00.getPz()) - (data08.getPf() - data00.getPf()))*oldCt*oldPt;
						}
					}else {
						//无换表情况
						ygzl00 = ((data08.getPz() - data00.getPz()) - (data08.getPf() - data00.getPf()))*ct*pt;
					}
					
					map.put(row+"-"+8+"-r", ygzl00);
				}
				
				if(data16 != null && data08 != null){
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() >= date08.getTime()){
							if(changeDate.getTime() < date16.getTime()){
								//换表时间 08:00 ~ 16:00 进行换表运算
								double oldYgdl08 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data08.getPz() - data08.getPf()))*oldCt*oldPt;
								double newYgdl08 = ((data16.getPz() - data16.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
								ygzl08 = oldYgdl08 + newYgdl08;
							}else{
								//换表时间 16:00 ~ 24:00 取旧表的ct,pt
								ygzl08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*oldCt*oldPt;
							}
						}else {
							//换表时间 0:00 ~ 08:00 取新表的ct,pt
							ygzl08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*ct*pt;
						}
					}else {
						//无换表情况
						ygzl08 = ((data16.getPz() - data08.getPz()) - (data16.getPf() - data08.getPf()))*ct*pt;
					}
					
					map.put(row+1+"-"+8+"-r", ygzl08);
				}
				
				if(data24 != null && data16 != null){
					if(dnbChange != null && changeDate != null){
						if(changeDate.getTime() >= date16.getTime()){
							//换表时间 16:00 ~ 24:00 进行换表运算
							double oldYgdl16 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data16.getPz() - data16.getPf()))*oldCt*oldPt;
							double newYgdl16 = ((data24.getPz() - data24.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
							ygzl16 = oldYgdl16 + newYgdl16;
						}else {
							//换表时间 00:00 ~ 16:00 取新表ct,pt
							ygzl16 = ((data24.getPz() - data16.getPz()) - (data24.getPf() - data16.getPf()))*ct*pt;
						}
					}else {
						//无换表情况
						ygzl16 = ((data24.getPz() - data16.getPz()) - (data24.getPf() - data16.getPf()))*ct*pt;
					}
					
					map.put(row+2+"-"+8+"-r", ygzl16);
				}
				
				if(data24 != null && data00 != null){
					if(dnbChange != null){
						//只要当天有换表记录 24:00 的就得进行换表运算
						double oldYgdl24 = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data00.getPz() - data00.getPf()))*oldCt*oldPt;
						double newYgdl24 = ((data24.getPz() - data24.getPf())-(dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
						ygzl24 = oldYgdl24 + newYgdl24;
					}else {
						//无换表运算
						ygzl24 = ((data24.getPz() - data00.getPz()) - (data24.getPf() - data00.getPf()))*ct*pt;
					}
					
					map.put(row+3+"-"+8+"-r", ygzl24);
				}
				
				//日平均
				String jizuCodes[] = new String[]{"23","24","1","2"};
				for (int j = 0; j < jizuCodes.length; j++) {
					String jizuCode = jizuCodes[j];
					if(jizuCode.equals(code)){
						if(ygzl00 != null){
							map.put(128+"-"+(3+j), ygzl00/8);
						}
						if(ygzl08 != null){
							map.put(129+"-"+(3+j), ygzl08/8);
						}
						if(ygzl16 != null){
							map.put(130+"-"+(3+j), ygzl16/8);
						}
						if(ygzl24 != null){
							map.put(131+"-"+(3+j), ygzl24/24);
						}
					}
				}
				
				//当月均	1号到4号电表
				if(curRi.charAt(0)=='0'){
					curRi=curRi.substring(1);
				}
				String[] curYueJunCodes=new String[]{"23","24","1","2"};
				for (int j = 0; j < curYueJunCodes.length; j++) {
					String curYueCode = curYueJunCodes[j];
					Double fdlTotle=this.dllj(curDate, curYueCode, conn);
					if(fdlTotle != null){
						Double ri=new Double(curRi);
						fdlTotle=fdlTotle/(24*ri);
						
						map.put(132+"-"+(3+j), fdlTotle);
					}
				}
				
				//当月均 综合厂用电率一期
				String[] curYueJunYqCodes=new String[]{"19","20","15","16","17","18","21"};
				String[] curYueJunJzcodes=new String[]{"23","24"};
				Double fdlTotle=0d;
				
				for(String curYueCode:curYueJunYqCodes){
					Double dlljRs = this.dllj(curDate, curYueCode, conn);
					if(dlljRs != null){
						fdlTotle+=dlljRs;
					}
				}
				Double jzfdl=0d;
				for(String curYueCode:curYueJunJzcodes){
					Double dlljRs = this.dllj(curDate, curYueCode, conn);
					if(dlljRs != null){
						jzfdl+=dlljRs;
					}
				}
				Double res=0d;
				if(jzfdl!=0d){
					res=(1-fdlTotle/jzfdl);
					map.put(132+"-"+7+"-r-r-r", res);
				}
				
				//当月均 综合厂用电率二期
				String[] curYueJunEqCodes=new String[]{"3","5","7","9"};
				String curYueJun18Codes="18";
				String[] curYueJunEqJzcodes=new String[]{"1","2"};
				Double fdlTotle2=0d;
				for(String curYueCode:curYueJunEqCodes){
					Double dlljRs = this.dllj(curDate, curYueCode, conn);
					if(dlljRs != null){
						fdlTotle2+=dlljRs;
					}
				}
				
				Double dlljRs2 = this.dllj(curDate, curYueJun18Codes, conn);
				if(dlljRs2 != null){
					fdlTotle2=fdlTotle2-dlljRs2;
				}
				
				Double jzfdl2=0d;
				for(String curYueCode:curYueJunEqJzcodes){
					Double dlljRs = this.dllj(curDate, curYueCode, conn);
					if(dlljRs != null){
						jzfdl2+=dlljRs;
					}
				}
				Double res2=0d;
				if(jzfdl2!=0d){
					res2=(1-fdlTotle2/jzfdl2);
					map.put(132+"-"+8+"-r-r-r-r", res2);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return map;
	}
	
	/**
	 * 描述:根据日期查询所有电能表的数据 （为了定期计算、保存每天24点的有功增量总）
	 * @throws SQLException 
	 */
	public void saveDnbDateYgzlz(String nowDate) throws SQLException{
		Connection conn=null;
		Statement selStatement=null;
		ResultSet selRs=null;
		Statement statement = null;
		Statement insertStatement = null;
		//boolean isAutocommit = true;
		try{
			conn=this.dataSource.getConnection();
			//isAutocommit = conn.getAutoCommit();
			//conn.setAutoCommit(false);
			statement = conn.createStatement();
			selStatement=conn.createStatement();
			insertStatement = conn.createStatement();
			
			//当前时间
			String curDate=nowDate;
			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curNian=DateUtils.getDate(curD, "yyyy");
			String curYue=DateUtils.getDate(curD, "MM");
			//当前时间的前一天
			Date preDate=DateUtils.getPreDate(curD);
			String preDateStr=DateUtils.formatDate(preDate, "yyyy-MM-dd");
			String preNian=DateUtils.getDate(preDate, "yyyy");
			String preYue=DateUtils.getDate(preDate, "MM");
			String preRi=DateUtils.getDate(preDate, "dd");
			String selSql="select * from tj_dnb_ygzlz t where times=to_date('"+preDateStr+"','yyyy-MM-dd')";
			selRs=selStatement.executeQuery(selSql);
			if(!selRs.next()){
				StringBuffer sql = new StringBuffer("insert into tj_dnb_ygzlz(id,times,nian,yue,ri,value_19,value_25,value_20,value_26," +
						"value_15,value_30,value_16,value_28,value_17,value_29,value_18,value_21,value_22,value_23,value_24,value_31," +
						"value_3,value_4,value_5,value_6,value_11,value_12,value_7,value_8,value_9,value_10,value_13,value_14," +
				"value_1,value_2)\n");
				sql.append("values('"+UUIDGenerator.hibernateUUID()+"',to_date('"+preDateStr+"','yyyy-MM-dd'),"+preNian+","+preYue+","+preRi+"");
				
				//所有指标编码
				String[] codes=DnbData.DNB_CODES.split(",");
				for(int i=0; i<codes.length; i++){
					//指标编码
					String code = codes[i];
					
					//表码率 ct*pt
					String sql2="select * from elec.elec_param t where elecid='"+code+"'";
					Dnb dnb=this.operSql2(conn, sql2);
					int ct = dnb.getCt();
					int pt = dnb.getPt();
					
					//0点
					String sql00="select * from elec.elec_z_"+preNian+preYue+" where elecid="+code+" and times=to_date('"+preDateStr+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
					DnbData data00=this.operSql(conn, sql00);
					
					//24点
					String sql24="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
					DnbData data24=this.operSql(conn, sql24);
					
					String elec_sql = "select t.times from elec.elec_z t where t.times = to_date('"+preDateStr+"','yyyy-mm-dd') and t.elecid = "+code+"";
					ResultSet res = statement.executeQuery(elec_sql);
					if(!res.next()){
						//保存上网电量月报需要的数据
//						System.out.println("当天："+data24.getPz());
//						System.out.println("前天："+data00.getPz());
//						System.out.println("计算结果:"+(data24.getPz()-data00.getPz()));
						String insertSql = "insert into elec.elec_z select TO_DATE('"+preDateStr+"','YYYY-MM-DD') AS TIMES,ELECID,PZ,PF,QZ,QF,"+(data24.getPz()-data00.getPz())+" AS D_PZ,"+(data24.getPf()-data00.getPf())+" AS D_PF from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
						insertStatement.execute(insertSql);
					}
					if(data24 != null && data00 != null){
						//24点有功增量总
						double  ygzl24 = ((data24.getPz()-data00.getPz()) - (data24.getPf()-data00.getPf()))*ct*pt;
						sql.append(","+ygzl24);
					}
				}
				sql.append(")");
				statement.execute(sql.toString());
				//conn.commit();
			}
		}catch(Exception e){
			if(conn != null){
				conn.rollback();
			}
			e.printStackTrace();
		}finally{
			if(selRs!=null){
				selRs.close();
			}
			if(selStatement != null){
				selStatement.close();
			}
			if(statement != null){
				statement.close();
			}
			if(conn!=null){
				//conn.setAutoCommit(isAutocommit);
				conn.close();
			}
		}
	}
	
//	public void infoS(String nowDate){
//		Connection conn=null;
//		Statement st=null;
//		ResultSet res=null;
//		try{
//			conn = this.dataSource.getConnection();
//			st = conn.createStatement();
//			this.AA_cs(conn, st, res, nowDate);
//			System.out.println("执行完毕！");
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void AA_cs(Connection conn,Statement st,ResultSet res,String nowDate) throws SQLException{
//		System.out.println(nowDate);
//		String curDate=nowDate;
//		Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
//		String curNian=DateUtils.getDate(curD, "yyyy");
//		String curYue=DateUtils.getDate(curD, "MM");
//		//当前时间的前一天
//		Date preDate=DateUtils.getPreDate(curD);
//		String preDateStr=DateUtils.formatDate(preDate, "yyyy-MM-dd");
//		String preNian=DateUtils.getDate(preDate, "yyyy");
//		String preYue=DateUtils.getDate(preDate, "MM");
//		String[] codes=DnbData.DNB_CODES.split(",");
//		for(int i=0; i<codes.length; i++){
//			String code = codes[i];
//			//0点
//			String sql00="select * from elec.elec_z_"+preNian+preYue+" where elecid="+code+" and times=to_date('"+preDateStr+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
//			DnbData data00=this.operSql(conn, sql00);
//			
//			//24点
//			String sql24="select * from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
//			DnbData data24=this.operSql(conn, sql24);
//			String elec_sql = "select t.times from elec.elec_zCS t where t.times = to_date('"+preDateStr+"','yyyy-mm-dd') and t.elecid = "+code+"";
//			res = st.executeQuery(elec_sql);
//			if(!res.next()){
//				//保存上网电量月报需要的数据
//				if(data24!=null&&data00!=null){
//					String insertSql = "insert into elec.elec_zCS select TO_DATE('"+preDateStr+"','YYYY-MM-DD') AS TIMES,ELECID,PZ,PF,QZ,QF,"+(data24.getPz()-data00.getPz())+" AS D_PZ,"+(data24.getPf()-data00.getPf())+" AS D_PF from elec.elec_z_"+curNian+curYue+" where elecid="+code+" and times=to_date('"+curDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
//					st.execute(insertSql);
//				}else{
//					System.out.println(nowDate+"--null");
//				}
//			}
//		}
//		this.AA_cs(conn, st, res, preDateStr);
//	}
	/**
	 * 
	 * 描述:计算当月均
	 * 时间:2010 11 26
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getDnbDyjJsonByDate(String curDate) throws SQLException{
		StringBuffer result=new StringBuffer();
		Connection conn=null;
		try{
			conn=this.dataSource.getConnection();

			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curRi=DateUtils.getDate(curD, "dd");           //当前日
			if(curRi.charAt(0)=='0'){
				curRi=curRi.substring(1);
			}
			int flag=1;
			//1号到4号电表的编码
			String[] curYueJunCodes=new String[]{"23","24","1","2"};
			for(String curYueCode:curYueJunCodes){
				//String lastDate=DateUtils.getLastDate(curDate);
				//String sql="select sum(pz),sum(pf) from elec.elec_z_"+curNian+curYue+" where elecid="+curYueCode+" and times>=to_date('"+curNian+"-"+curYue+"-1"+" 00:00:00','yyyy-MM-dd HH24:mi:ss') and times<=to_date('"+nextDateStr+" 00:00:00','yyyy-MM-dd HH24:mi:ss') and to_char(times,'HH24:mi:ss')='00:00:00' order by times desc";
				Double fdlTotle=this.dllj(curDate, curYueCode, conn);
				if(fdlTotle == null){
					result.append("JZ"+flag+":'',");
				}else {
					Double ri=new Double(curRi);
					fdlTotle=fdlTotle/(24*ri);
					//此处用到自定义4,5函数。。
					result.append("JZ"+flag+":"+this.round(fdlTotle.toString(), 4,true)+",");
				}
				flag++;
			}
			if(result.length()>0){
				result.deleteCharAt(result.length()-1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return result.toString();
	}
	
	/**
	 * 
	 * 描述:计算当月均的1期厂用电率(1-(I9+I17+I25+I33+I41+I53+I49)/(I61+I65+1))
	 * 时间:2010 11 26
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getDnbDyjCydl1QJsonByDate(String curDate) throws SQLException{
		StringBuffer result=new StringBuffer();
		Connection conn=null;
		try{
			conn=this.dataSource.getConnection();

			//主表的编码
			String[] curYueJunCodes=new String[]{"19","20","15","16","17","18","21"};
			String[] curYueJunJzcodes=new String[]{"23","24"};
			Double fdlTotle=0d;
			for(String curYueCode:curYueJunCodes){
				Double dlljRs = this.dllj(curDate, curYueCode, conn);
				if(dlljRs != null){
					fdlTotle+=dlljRs;
				}
			}
			Double jzfdl=0d;
			for(String curYueCode:curYueJunJzcodes){
				Double dlljRs = this.dllj(curDate, curYueCode, conn);
				if(dlljRs != null){
					jzfdl+=dlljRs;
				}
			}
			Double res=0d;
			if(jzfdl!=0d){
//				res=this.round2(1-fdlTotle/jzfdl, 4)*100;
				res=(1-fdlTotle/jzfdl)*100;
				result.append("Q1:"+res.toString());
			}else {
				result.append("Q1:''");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return result.toString();
	}
	
	/**
	 * 
	 * 描述:计算当月均的2期厂用电率(1-(I9+I17+I25+I33+I41+I53+I49)/(I61+I65+1))
	 * 时间:2010 11 26
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getDnbDyjCydl2QJsonByDate(String curDate) throws SQLException{
		StringBuffer result=new StringBuffer();
		Connection conn=null;
		try{
			conn=this.dataSource.getConnection();
			//主表的编码
			String[] curYueJunCodes=new String[]{"3","5","7","9"};
			String curYueJun18Codes="18";
			String[] curYueJunJzcodes=new String[]{"1","2"};
			Double fdlTotle=0d;
			for(String curYueCode:curYueJunCodes){
				Double dlljRs = this.dllj(curDate, curYueCode, conn);
				if(dlljRs != null){
					fdlTotle+=dlljRs;
				}
			}
			
			Double dlljRs2 = this.dllj(curDate, curYueJun18Codes, conn);
			if(dlljRs2 != null){
				fdlTotle=fdlTotle-dlljRs2;
			}
			
			Double jzfdl=0d;
			for(String curYueCode:curYueJunJzcodes){
				Double dlljRs = this.dllj(curDate, curYueCode, conn);
				if(dlljRs != null){
					jzfdl+=dlljRs;
				}
			}
			Double res=0d;
			if(jzfdl!=0d){
//				res=this.round2(1-fdlTotle/jzfdl, 4)*100;
				res=(1-fdlTotle/jzfdl)*100;
				result.append("Q2:"+res.toString());
			}else {
				result.append("Q2:''");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return result.toString();
	}
	
	/**
	 * 
	 * 描述:单个编码从1号到选择的日期的电量的累积
	 * 时间:2010 11 27
	 * 作者:童贝
	 * 参数:
	 * 返回值:Double
	 * 抛出异常:
	 */
	@SuppressWarnings("unchecked")
	public Double dllj(String curDate,String curYueCode,Connection conn) throws SQLException{
		Double fdlTotle=0d;
		Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
		String curNian=DateUtils.getDate(curD, "yyyy");       //当前年
		String curYue=DateUtils.getDate(curD, "MM");		  //当前月
		//初始化日期，从本月1号开始
		String initDate=curNian+"-"+curYue+"-1";
		
		//获取下一天的日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curD);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		Date nextDate = calendar.getTime();
		String nextNian = DateUtils.getDate(nextDate, "yyyy");
		String nextYue = DateUtils.getDate(nextDate, "MM");
		String nextYmd = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
		
		String sql00="select * from elec.elec_z_"+curNian+curYue+" where elecid="+curYueCode+" and times=to_date('"+initDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
		String sql24="select * from elec.elec_z_"+nextNian+nextYue+" where elecid="+curYueCode+" and times=to_date('"+nextYmd+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by times desc";
		DnbData data00=this.operSql(conn, sql00);
		DnbData data24=this.operSql(conn, sql24);
		
		if(data00 == null || data24 == null){
			return null;
		}
		
		String sql = "select * from elec.elec_param t where elecid="+curYueCode;
		Dnb dnb = this.operSql2(conn, sql);
		int ct = dnb.getCt();
		int pt = dnb.getPt();
		
		//换表信息
		String hql = "from DnbChange t where t.elecId='"+curYueCode+"' and times like '"+curNian+"%'";
		List<DnbChange> list = dnbChangeManager.find(hql);
		DnbChange dnbChange = null;
		if(list.size() > 0){
			dnbChange = list.get(0);
		}
		
//		该块电表有功电量=老表有功电量+新表有功电量
//		老表有功电量=(（换表记录中的老表正向有功表码-换表记录中的老表反向有功表码）-（ELEC_Z_201012数据表中的1号正向有功表码-反向有功表码）)*换表记录中的老表CT*老表PT
//		新表有功电量=（（ELEC_Z_201012数据表中的当天24点正向有功表码-24点反向有功表码）-（换表记录中的新表正向有功表码-换表记录中的新表反向有功表码））*ELEC_PARAM中的CT*PT
		if(dnbChange != null){
			String[] ctsArray = dnbChange.getCtO().split("/");
			int ct1 = Integer.parseInt(ctsArray[0].trim());
			int ct2 = Integer.parseInt(ctsArray[1].trim());
			int oldCt = ct1/ct2;
			
			String[] ptsArray = dnbChange.getPtO().split("/");
			int pt1 = Integer.parseInt(ptsArray[0].trim());
			int pt2 = Integer.parseInt(ptsArray[1].trim());
			int oldPt = pt1/pt2;
			
			//计算的年月
			Date curNyDate = DateUtils.parseDate(curNian+"-"+curYue, "yyyy-MM");
			//换表的年月
			String changeNy = dnbChange.getTimes();
			changeNy = changeNy.substring(0,7);
			Date changeNyDate = DateUtils.parseDate(changeNy,"yyyy-MM");
			
			if(changeNyDate.getTime() > curNyDate.getTime()){
				//换表年月 > 计算年月 取旧表ct,pt
				fdlTotle = ((data24.getPz()-data24.getPf())-(data00.getPz()-data00.getPf()))*oldCt*oldPt;
			}else if(changeNyDate.getTime() == curNyDate.getTime()) {
				//换表年月 == 计算年月 进行换表运算
				Double oldYgdl = ((dnbChange.getPzO() - dnbChange.getPfO()) - (data00.getPz() - data00.getPf()))*oldCt*oldPt;
				Double newYgdl = ((data24.getPz() - data24.getPf()) - (dnbChange.getPzN() - dnbChange.getPfN()))*ct*pt;
				fdlTotle = oldYgdl + newYgdl;
			}else {
				//换表年月 < 计算年月 取新表ct,pt
				fdlTotle = ((data24.getPz()-data24.getPf())-(data00.getPz()-data00.getPf()))*ct*pt;
			}
			
		}else {
			//无换表情况
			fdlTotle = ((data24.getPz()-data24.getPf())-(data00.getPz()-data00.getPf()))*ct*pt;
		}
		
		return fdlTotle;
	}
	
	/**
	 * 
	 * 描述:根据sql查出结果
	 * 时间:2010 11 17
	 * 作者:童贝
	 * 参数:
	 * 返回值:DnbData
	 * 抛出异常:
	 */
	public DnbData operSql(Connection conn,String sql) throws SQLException{
		DnbData res=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				res=new DnbData();
				Date times=rs.getDate("TIMES");
				Double pz=rs.getDouble("PZ");
				Double pf=rs.getDouble("PF");
				Double qz=rs.getDouble("QZ");
				Double qf=rs.getDouble("QF");
				res.setPf(pf==null?0d:pf);
				res.setPz(pz==null?0d:pz);
				res.setQf(qf==null?0d:qf);
				res.setQz(qz==null?0d:qz);
				res.setTimes(times);
			}else{
				/*res=new DnbData();
				res.setPf(0d);
				res.setPz(0d);
				res.setQf(0d);
				res.setQz(0d);*/
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
		}
		return res;
	}
	
	/**
	 * 
	 * 描述:根据sql查出结果
	 * 时间:2010 11 17
	 * 作者:童贝
	 * 参数:
	 * 返回值:DnbData
	 * 抛出异常:
	 */
	public Dnb operSql2(Connection conn,String sql) throws SQLException{
		Dnb res=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				res=new Dnb();
				int ct=rs.getInt("CT");
				int pt=rs.getInt("PT");
				String cts=rs.getString("CTS");
				String pts=rs.getString("PTS");
				String elecbh=rs.getString("ELECBH");
				
				res.setCt(ct);
				res.setPt(pt);
				res.setCts(cts);
				res.setPts(pts);
				res.setElecbh(elecbh);
			}else{
//				res=new Dnb();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
		}
		return res;
	}
	
	public double round(String v, int scale,boolean isAdd){
		double result=0d;
		String value=v;
		if(value.indexOf(".")>-1){
			String zs=value.substring(0,value.indexOf("."));
			String xs=value.substring(value.indexOf(".")+1);
			//判断小数位数是否比保留的位数要大
			if(xs.length()>scale){
				String lastNum=xs.substring(xs.length()-1);
				Integer num=Integer.parseInt(lastNum);
				if(num>=5){
					String jyNum=xs.substring(xs.length()-2,xs.length()-1);
					Integer jy=Integer.parseInt(jyNum);
					jy=jy+1;
					String res=zs+"."+xs.substring(0,xs.length()-1);
					return round(res,scale,true);
				}else{
					if(isAdd){
						String jyNum=xs.substring(xs.length()-1);
						Integer jy=Integer.parseInt(jyNum);
						jy=jy+1;
						String res=zs+"."+xs.substring(0,xs.length()-1)+jy;
						return round(res,scale,false);
					}else{
						String res=value.substring(0,value.length()-1);
						return round(res,scale,false);
					}
				}
			}else{
				if(isAdd){
					String jyNum=xs.substring(xs.length()-1);
					Integer jy=Integer.parseInt(jyNum);
					jy=jy+1;
					String res="";
					if(jy==10){
						res=new Double(round2(new Double(v).doubleValue(), scale)).toString();
					}else{
						res=zs+"."+xs.substring(0,xs.length()-1)+jy;
					}
					return Double.parseDouble(res);
				}else{
					result=new Double(v).doubleValue();
				}
			}
			
		}else{
			result=new Double(v).doubleValue();
		}
		return result;
	}
	
	
	public double round2(double v, int scale){
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, BigDecimal.ROUND_UP).doubleValue();
	}
	
	public static long compareStringTime(String t1, String t2, String parrten) {
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		Date dt2 = formatter.parse(t2, pos1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt1);
		long day1 = calendar.getTimeInMillis();
		calendar.setTime(dt2);
		long day2 = calendar.getTimeInMillis();
		
		return (day1 - day2);
	}
	
}
