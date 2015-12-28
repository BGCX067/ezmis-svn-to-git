package com.jteap.jhtj.zyzb.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.jhtj.chart.DualYMSColumn3DAndLineCategorie;
import com.jteap.jhtj.chart.DualYMSColumn3DAndLineChart;
import com.jteap.jhtj.chart.DualYMSColumn3DAndLineDataSet;
import com.jteap.jhtj.chart.DualYMSColumn3DAndLineSet;
import com.jteap.jhtj.chart.MSLineApply;
import com.jteap.jhtj.chart.MSLineStyle;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjwh.model.KeyModel;
import com.jteap.jhtj.zyzb.model.Zyzb;
import com.jteap.system.dict.model.Dict;
@SuppressWarnings({ "unchecked", "serial" })
public class ZyzbManager extends HibernateEntityDao<Zyzb> {
	private TjItemKindManager tjItemKindManager;
	private DataSource dataSource;
	public static void main(String[] args) {
		System.out.println("\u51FA\u8D27\u5904\u7406");
	}
	
	/**
	 * 根据配置的pageid取得指标节点的List
	 */
	public List<Zyzb> getZbjdList(String zbfl){
		String hsql = "from Zyzb where zbfl='"+zbfl+"' order by order";
		try{
			return this.find(hsql);
		}catch(Exception e){	//在使用find的时候未找到数据会抛异常
			return null;
		}
	}
	
	/**
	 * 
	 *描述：得到年列表
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getYearList(){
		List<KeyModel> yearList=new ArrayList<KeyModel>();
		String year=DateUtils.getTime("yyyy");
		Integer iyear=new Integer(year);
		for(int i=iyear;i>=iyear-10;i--){
			KeyModel key=new KeyModel();
			key.setDisplayValue(""+i);
			key.setValue(""+i);
			yearList.add(key);
		}
		return yearList;
	}
	/**
	 * 
	 *描述：得到实体列表
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getEntiyList(){
		List<KeyModel> entiyList=new ArrayList<KeyModel>();
		KeyModel qcModel=new KeyModel();
		qcModel.setDisplayValue("全厂");
		qcModel.setValue("QC");
		entiyList.add(qcModel);
		List<Dict> dictList=this.tjItemKindManager.getDictsByUniqueName("JZMC");
		for(Dict dict:dictList){
			KeyModel model=new KeyModel();
			model.setDisplayValue(dict.getKey());
			model.setValue(dict.getValue());
			entiyList.add(model);
		}
		return entiyList;
	}
	
	/**
	 * 
	 *描述：创建同期比图形
	 *时间：2010-5-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public DualYMSColumn3DAndLineChart generateTQChart(Map<String, String> keyMap,Zyzb zyzb) throws Exception{
		DualYMSColumn3DAndLineChart chart=new DualYMSColumn3DAndLineChart();
		chart.setCaption(zyzb.getName());
		chart.setBaseFontSize("12");
		chart.setShowValues("0");
		
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			String entiy=keyMap.get("ENTIY");
			String nian=keyMap.get(TjInterFace.NIAN);
			Integer sNian=Integer.parseInt(nian);
			for(int i=sNian;i>=sNian-1;i--){
				DualYMSColumn3DAndLineDataSet dataset=new DualYMSColumn3DAndLineDataSet();
				dataset.setSeriesName(i+"年");
				for(int j=1;j<13;j++){
					if(i==sNian){
						DualYMSColumn3DAndLineCategorie categorie =new DualYMSColumn3DAndLineCategorie();
						categorie.setLabel(j+"月");
						chart.getDualYMSColumn3DAndLineCategories().getDualYMSColumn3DAndLineCategories().add(categorie);
					} 
					
					//组合查询语句
					String sql="select "+zyzb.getZblx()+" from ";
					if(entiy.equals("QC")){
						sql=sql+"DATA_QC_YUE where NIAN="+i+" and YUE="+j;
					}else{
						sql=sql+"DATA_JZ_YUE where JZ='"+entiy+"' and NIAN="+i+" and YUE="+j;
					}
					
					state=conn.createStatement();
					rs=state.executeQuery(sql);
					DualYMSColumn3DAndLineSet set=new DualYMSColumn3DAndLineSet();
					if(rs.next()){
						Double obj=rs.getDouble(zyzb.getZblx());
						set.setValue((obj==null?"0":obj.toString()));
						dataset.getDualYMSColumn3DAndLineSets().add(set);
					}else{
						set.setValue("0");
						dataset.getDualYMSColumn3DAndLineSets().add(set);
					}
				}
				chart.getDualYMSColumn3DAndLineDataSets().add(dataset);
			}
			
			if(chart.getDualYMSColumn3DAndLineDataSets().size()==2){
				DualYMSColumn3DAndLineDataSet tqDataset=new DualYMSColumn3DAndLineDataSet();
				tqDataset.setSeriesName("同期比");
				tqDataset.setParentYAxis("S");
				
				DualYMSColumn3DAndLineDataSet curDataset=chart.getDualYMSColumn3DAndLineDataSets().get(0);
				DualYMSColumn3DAndLineDataSet proDataset=chart.getDualYMSColumn3DAndLineDataSets().get(1);
				for(int i=0;i<curDataset.getDualYMSColumn3DAndLineSets().size();i++){
					DualYMSColumn3DAndLineSet curSet=curDataset.getDualYMSColumn3DAndLineSets().get(i);
					DualYMSColumn3DAndLineSet proSet=proDataset.getDualYMSColumn3DAndLineSets().get(i);
					DualYMSColumn3DAndLineSet tqSet=new DualYMSColumn3DAndLineSet();
					tqSet.setValue(this.computeTqValue(curSet.getValue(), proSet.getValue()).toString()+"%");
					tqDataset.getDualYMSColumn3DAndLineSets().add(tqSet);
				}
				chart.getDualYMSColumn3DAndLineDataSets().add(tqDataset);
			}
			
			//样式
			MSLineStyle style=new MSLineStyle();
			style.setName("captionFont");
			MSLineApply apply=new MSLineApply();
			apply.setStyles("captionFont");
			
			chart.getMSLineStyles().getMSLineDefinition().setMSLineStyle(style);
			chart.getMSLineStyles().getMSLineApplication().getMSLineApplys().add(apply);
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return chart;
	}
	
	/**
	 * 
	 *描述：创建机组对比图形
	 *时间：2010-5-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public DualYMSColumn3DAndLineChart generateJZChart(String nian,Zyzb zyzb,List<KeyModel> entiyList) throws Exception{
		DualYMSColumn3DAndLineChart chart=new DualYMSColumn3DAndLineChart();
		chart.setCaption(zyzb.getName());
		chart.setBaseFontSize("12");
		chart.setShowValues("0");
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			//全厂记录
			List<String> qcValueList=new ArrayList<String>();
			int flag=0;//记录标题和全厂数据
			for(KeyModel model:entiyList){
				//机组或全厂
				String entiy=model.getValue();
				
				DualYMSColumn3DAndLineDataSet dataset=new DualYMSColumn3DAndLineDataSet();
				dataset.setSeriesName(model.getDisplayValue());
				for(int i=1;i<13;i++){
					//组合查询语句
					String sql="select "+zyzb.getZblx()+" from ";
					if(entiy.equals("QC")){
						sql=sql+"DATA_QC_YUE where NIAN="+nian+" and YUE="+i;
					}else{
						sql=sql+"DATA_JZ_YUE where JZ='"+entiy+"' and NIAN="+nian+" and YUE="+i;
					}
					
					state=conn.createStatement();
					rs=state.executeQuery(sql);
					DualYMSColumn3DAndLineSet set=new DualYMSColumn3DAndLineSet();
					String value="0";
					if(rs.next()){
						Object obj=rs.getObject(1);
						value=(obj==null?"0":obj.toString());
					}
					set.setValue(value);
					dataset.getDualYMSColumn3DAndLineSets().add(set);
					if(0==flag){
						//写入标题
						DualYMSColumn3DAndLineCategorie categorie =new DualYMSColumn3DAndLineCategorie();
						categorie.setLabel(i+"月");
						chart.getDualYMSColumn3DAndLineCategories().getDualYMSColumn3DAndLineCategories().add(categorie);
						//记录全厂数据
						qcValueList.add(value);
					} 
				}
				chart.getDualYMSColumn3DAndLineDataSets().add(dataset);
				flag++;
			}
			//临时存放数据集
			List<DualYMSColumn3DAndLineDataSet> zylDatasetList=new ArrayList<DualYMSColumn3DAndLineDataSet>();
			//创建占全厂比率
			for(int j=1;j<chart.getDualYMSColumn3DAndLineDataSets().size();j++){
				DualYMSColumn3DAndLineDataSet dataset=chart.getDualYMSColumn3DAndLineDataSets().get(j);
				
				DualYMSColumn3DAndLineDataSet zylDataset=new DualYMSColumn3DAndLineDataSet();
				zylDataset.setSeriesName(dataset.getSeriesName()+"占全厂比率");
				zylDataset.setParentYAxis("S");
				
				List<DualYMSColumn3DAndLineSet> sets=dataset.getDualYMSColumn3DAndLineSets();
				for(int i=0;i<sets.size();i++){
					DualYMSColumn3DAndLineSet zylSet=new DualYMSColumn3DAndLineSet();
					DualYMSColumn3DAndLineSet jzSet=sets.get(i);
					String jzValue=jzSet.getValue();
					String qcValue=qcValueList.get(i);
					//占有率
					Double zyl=this.computeJzValue(jzValue, qcValue);
					zylSet.setValue(zyl+"%");
					zylDataset.getDualYMSColumn3DAndLineSets().add(zylSet);
				}
				zylDatasetList.add(zylDataset);
			}
			for(DualYMSColumn3DAndLineDataSet dataset:zylDatasetList){
				chart.getDualYMSColumn3DAndLineDataSets().add(dataset);
			}
			
			//样式
			MSLineStyle style=new MSLineStyle();
			style.setName("captionFont");
			MSLineApply apply=new MSLineApply();
			apply.setStyles("captionFont");
			
			chart.getMSLineStyles().getMSLineDefinition().setMSLineStyle(style);
			chart.getMSLineStyles().getMSLineApplication().getMSLineApplys().add(apply);
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return chart;
	}
	
	/**
	 * 
	 *描述：创建指标关联对比图形
	 *时间：2010-5-18
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public DualYMSColumn3DAndLineChart generateZBGLChart(Map<String, String> keyMap,Zyzb zyzb) throws Exception{
		DualYMSColumn3DAndLineChart chart=new DualYMSColumn3DAndLineChart();
		chart.setCaption(zyzb.getName());
		chart.setBaseFontSize("12");
		chart.setShowValues("0");
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			//条件
			String entiy=keyMap.get("ENTIY");
			String nian=keyMap.get(TjInterFace.NIAN);
			//名字
			String name=zyzb.getName();
			String[] names=name.split("-");
			//字段列表
			String zblx=zyzb.getZblx();
			String[] zblxs=zblx.split("-");
			for(int i=0;i<zblxs.length;i++){
				DualYMSColumn3DAndLineDataSet dataset=new DualYMSColumn3DAndLineDataSet();
				dataset.setSeriesName(names[i]);
				if(i!=0){
					dataset.setParentYAxis("S");
				}
				for(int j=1;j<13;j++){
					if(i==0){
						DualYMSColumn3DAndLineCategorie categorie =new DualYMSColumn3DAndLineCategorie();
						categorie.setLabel(j+"月");
						chart.getDualYMSColumn3DAndLineCategories().getDualYMSColumn3DAndLineCategories().add(categorie);
					} 
					
					//组合查询语句
					String sql="select "+zyzb.getZblx()+" from ";
					if(entiy.equals("QC")){
						sql=sql+"DATA_QC_YUE where NIAN="+nian+" and YUE="+j;
					}else{
						sql=sql+"DATA_JZ_YUE where JZ='"+entiy+"' and NIAN="+nian+" and YUE="+j;
					}
					
					state=conn.createStatement();
					rs=state.executeQuery(sql);
					DualYMSColumn3DAndLineSet set=new DualYMSColumn3DAndLineSet();
					if(rs.next()){
						Object obj=rs.getObject(0);
						set.setValue((obj==null?"0":obj.toString()));
					}else{
						set.setValue("0");
					}
					dataset.getDualYMSColumn3DAndLineSets().add(set);
				}
				chart.getDualYMSColumn3DAndLineDataSets().add(dataset);
			}
			
			//样式
			MSLineStyle style=new MSLineStyle();
			style.setName("captionFont");
			MSLineApply apply=new MSLineApply();
			apply.setStyles("captionFont");
			
			chart.getMSLineStyles().getMSLineDefinition().setMSLineStyle(style);
			chart.getMSLineStyles().getMSLineApplication().getMSLineApplys().add(apply);
		}catch(Exception e){
			throw e;
		}finally{
			if (conn != null) {
				conn.close();
			}
		}
		return chart;
	}
	
	
	/**
	 * 
	 *描述：计算同期比
	 *时间：2010-5-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Double computeTqValue(String curValue,String proValue){
		Double curYear=new Double(curValue);
		Double proYear=new Double(proValue);
		Double tq=0d;
		if(proYear!=0d){
			tq=((curYear-proYear)/proYear)*100;
		}else{
			return tq;
		}
		return this.round(tq, 2, false);
	}
	
	/**
	 * 
	 *描述：计算机组占全厂占有率
	 *时间：2010-5-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Double computeJzValue(String jzValue,String qcValue){
		Double jz=new Double(jzValue);
		Double qc=new Double(qcValue);
		Double zyl=0d;
		if(qc!=0d){
			zyl=(jz/qc)*100;
		}else{
			return zyl;
		}
		return this.round(zyl, 2, false);
	}
	
	
	public double round(double v, int scale,boolean isAdd){
		double result=v;
		String value=Double.toString(v);
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
					return round(Double.parseDouble(res),scale,true);
				}else{
					if(isAdd){
						String jyNum=xs.substring(xs.length()-1);
						Integer jy=Integer.parseInt(jyNum);
						jy=jy+1;
						String res=zs+"."+xs.substring(0,xs.length()-1)+jy;
						return round(Double.parseDouble(res),scale,false);
					}else{
						String res=value.substring(0,value.length()-1);
						return round(Double.parseDouble(res),scale,false);
					}
				}
			}else{
				if(isAdd){
					String jyNum=xs.substring(xs.length()-1);
					Integer jy=Integer.parseInt(jyNum);
					jy=jy+1;
					String res="";
					if(jy==10){
						res=new Double(round2(v, scale)).toString();
					}else{
						res=zs+"."+xs.substring(0,xs.length()-1)+jy;
					}
					return Double.parseDouble(res);
				}else{
					result=v;
				}
			}
			
		}else{
			result=v;
		}
		return result;
	}
	
	
	public double round2(double v, int scale){
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, BigDecimal.ROUND_UP).doubleValue();
	}

	public TjItemKindManager getTjItemKindManager() {
		return tjItemKindManager;
	}

	public void setTjItemKindManager(TjItemKindManager tjItemKindManager) {
		this.tjItemKindManager = tjItemKindManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}