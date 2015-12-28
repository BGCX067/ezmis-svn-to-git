package com.jteap.wz.sfjctj.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.sfjctj.excelxml.Workbook;
import com.jteap.wz.sfjctj.manager.SfjcManager;
import com.jteap.wz.sfjctj.model.ExportExcelModel;
import com.jteap.wz.sfjctj.model.Sfjc;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.yhdmx.model.Yhdmx;
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class SfjcAction extends AbstractAction{
	
	private SfjcManager sfjcManager;
	
	//存放收发资金到当前页的统计总和
	private static Map sfzjCountMap  ;
	//保存开始条数
	private static int saveStart;
	//保存列对象
	private String[] cols;
	private PhysicTableManager physicTableManager;
	
	private WzdaManager wzdaManager;
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return sfjcManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return  this.getCols();
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		try {
			//获取最近报表的年月
			sfjcManager.getSfjcByZj();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.isUseQueryCache=false;
		//仓库id
		String ck = request.getParameter("ckmc");
		//年份
		String nf = request.getParameter("nf");
		//月份
		String yf = request.getParameter("yf");
		//查询标记
		String flag = request.getParameter("flag");
		String ny = request.getParameter("ny");
		//判断是否实时查询
		String ssflag = request.getParameter("ssflag");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		//子查询sql语句存放
		StringBuffer subSql = new StringBuffer();
		//where条件语句存放
		StringBuffer whereSql = new StringBuffer();
		//hql.append(",com.jteap.wz.crkrzgl.model.Crkrzmx as mx,com.jteap.wz.crkrzgl.model.Crkrzgl as rz");
		 //System.out.println(hql+"------------");
		whereSql.append("where w.id = j.wzbm and k.id = w.kwbm ");
		//判断是否实时查询 动态换列对象
		if("ss".equals(ssflag)){
			subSql.append("(select t.wzbm,max(y.crksj) as crsj from tb_wz_ycrkrzmx t, tb_wz_ycrkrz y where y.id = t.crkrzid and y.crkqf = '2'");
			this.setCols(new String[]{"id","sqjcsl","zjllgcxm","zjllbz","sqjcje","bqsrsl","bqsrje","bqzcsl","bqzcje","bqjysl","bqjyje",
					 "wzmc","xhgg","jldw","jhdj","cwmc","yf"});
		}else{
			this.setCols(new String[]{"id","sqjcsl","sqjcje","bqsrsl","bqsrje","bqzcsl","bqzcje","bqjysl","bqjyje","bqzrsl","bqzrje",
					 "wzmc","xhgg","jldw","jhdj","cwmc","yf"});
		}
		if(StringUtil.isNotEmpty(ny)){
			String n = ny.split("年")[0];
			String y = ny.split("年")[1].split("月")[0];
			//HqlUtil.addCondition(hql, "nf",n);
			//HqlUtil.addCondition(hql, "yf",y);
			if("ss".equals(ssflag)){
				subSql.append(" and to_char(y.crksj,'yyyy-MM') = '"+n+"-"+y+"'");
			} 
			whereSql.append(" and j.nf = '"+n+"' and j.yf = '"+y+"'");
		}
		
		//判断是否功能按钮查询  --有出入库记录等相关查询
		if(StringUtil.isNotEmpty(flag)){
			//有出入库记录及有库存的
			if("crkkc".equals(flag)){
				whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0 or j.sqjcsl>0)");
				//HqlUtil.addCondition(hql,"bqsrsl","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_GREAT);
				//HqlUtil.addCondition(hql,"bqzcsl","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_GREAT);
				//HqlUtil.addCondition(hql,"wz.id",wzid,HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
			}
			//有库存的物资
			if("kc".equals(flag)){
				//HqlUtil.addCondition(hql,"wz.id",wzid,HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN);
				whereSql.append(" and (j.bqjysl>0 or j.sqjcsl>0)");
			}
			//没有出入库的物资
			if("ncrk".equals(flag)){
				whereSql.append(" and j.bqsrsl =0 and bqzcsl=0");
				//HqlUtil.addCondition(hql,"bqsrsl","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_EQ);
				//HqlUtil.addCondition(hql,"bqzcsl","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_EQ);
			}
			//月末库存为负的物资
			if("nkc".equals(flag)){
				whereSql.append(" and j.bqjysl <0");
				//HqlUtil.addCondition(hql,"bqjysl","0",HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER,HqlUtil.COMPARECHAR_LESS);
			}
		}else{
			whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0 or j.sqjcsl>0)");
		}
		//如果没指定年份 则返回数据库中最近报表的年份
		//if(StringUtil.isEmpty(ny)){
			if(StringUtil.isNotEmpty(nf)){
				//HqlUtil.addCondition(hql, "nf",nf);
				
				if("ss".equals(ssflag)){
					subSql.append(" and to_char(y.crksj,'yyyy-MM') = '"+nf+"-"+yf+"'");
				} 
				whereSql.append("and j.nf = '"+nf+"'");
			}else if(StringUtil.isEmpty(ny)){
				//HqlUtil.addCondition(hql, "nf",sfjcManager.getMaxYear());
				if("ss".equals(ssflag)){
					subSql.append(" and to_char(y.crksj,'yyyy-MM') = '"+sfjcManager.getMaxYear()+"-"+sfjcManager.getMaxMoth()+"'");
					
				} 
				whereSql.append("and j.nf = '"+sfjcManager.getMaxYear()+"'");
			}
			//如果没指定月份 则返回数据库中最近报表的月份
			if(StringUtil.isNotEmpty(yf)){
				//HqlUtil.addCondition(hql, "yf",yf);
				whereSql.append(" and j.yf = '"+yf+"' ");
			}else if(StringUtil.isEmpty(ny)){
				//HqlUtil.addCondition(hql, "yf",sfjcManager.getMaxMoth());
				whereSql.append(" and j.yf = '"+sfjcManager.getMaxMoth()+"'");
			}
		//}
		if(StringUtil.isNotEmpty(ck)){
			//HqlUtil.addCondition(hql, "ck.id",ck);
			whereSql.append(" and j.ck = '"+ck+"'");
		}
		
		if("ss".equals(ssflag)){
			subSql.append(" group by wzbm) ");
			hql.append(subSql);
			hql.append("g,");
			hql.append(subSql.toString().replace("and y.crkqf = '2'", ""));
			hql.append("z ");
			whereSql.append(" and z.wzbm = j.wzbm and j.wzbm = g.wzbm order by g.crsj desc");
			hql.append(whereSql);
		}else{
			//whereSql.append(" order by j.id desc");
			hql.append(whereSql);
		}
		if(StringUtil.isNotEmpty(sort)){
			HqlUtil.addOrder(hql, sort, dir);
		}
//		System.out.println(hql.toString());
	}
	/**
	 * 显示列表动作
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public String showListAction() throws Exception {
		try {
			String ssflag = request.getParameter("ssflag");
			//正常方法
//			StringBuffer hql = new StringBuffer("select j.id,j.yf,j.sqjcsl,j.sqjcje,j.bqsrsl,j.bqsrje,j.bqzcsl,j.bqzcje,j.bqjysl,j.bqjyje,k.cwmc,w.wzmc,w.jhdj,w.jldw," +
//					"w.xhgg from tb_wz_twzsfjcb j, tb_wz_skwgl k,tb_wz_swzda w  ");
			//2011-10 临时方法
			StringBuffer hql = new StringBuffer("select j.id,j.yf,j.sqjcsl,j.sqjcje,j.bqsrsl,j.bqsrje,j.bqzcsl,j.bqzcje,j.bqjysl,j.bqjyje,j.bqzrsl,j.bqzrje,k.cwmc,w.wzmc,w.jhdj,w.jldw," +
			"w.xhgg from tb_wz_twzsfjcb j, tb_wz_skwgl k,tb_wz_swzda w  ");
			if("ss".equals(ssflag)){
				hql = new StringBuffer("select j.id,j.yf,j.sqjcsl,j.sqjcje,j.bqsrsl,j.bqsrje,j.bqzcsl,j.bqzcje,j.bqjysl,j.bqjyje,k.cwmc,w.wzmc,w.jhdj," +
						"w.jldw,w.xhgg,j.zjllgcxm,j.zjllbz from tb_wz_twzsfjcb j, tb_wz_skwgl k,tb_wz_swzda w, " );
			}
			// 扩展点1 针对hql进行预处理 事件 具体动作进行处理
			beforeShowList(request, response, hql);
			//System.out.println(hql+"-------加入条件后");
			Object pageFlag = request.getAttribute(PAGE_FLAG);
			if(pageFlag==null)
				pageFlag = request.getParameter(PAGE_FLAG);
			String json;
			if(pageFlag != null && pageFlag.toString().equals(PAGE_FLAG_NO)){
				//System.out.println("---------------------------------------------");
				Collection list = this.getManager().find(hql.toString(),showListHqlValues.toArray());
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:"
						+ json + "}";
			}else{
				json = getPageCollectionJson(hql.toString(),showListHqlValues.toArray());
			}
			this.outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return NONE;
	}
	/**
	 * 根据hql，分页查询
	 * 
	 * @param hql
	 * @return
	 */
	protected String getPageCollectionJson(String hql,Object... values) {
			Connection conn =null;
		try{
			// 每页大小
			String limit = request.getParameter("limit");
			String ssflag = request.getParameter("ssflag");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
	
			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			// 开始分页查询
			Page page = physicTableManager.pagedQueryTableData(hql,
					Integer.parseInt(start)+1, Integer.parseInt(limit));
			List list = (List) page.getResult();
			//List<Sfjc> sfjcList= (List)obj;
			//分页数据集合
			List jsonList = new ArrayList();
			for(int i = 0;i<list.size();i++){
				Map  objectMap = (HashMap)list.get(i);
				Map sfjcMap = new HashMap();
	        	sfjcMap.put("id",(String)objectMap.get("ID"));
	        	sfjcMap.put("wzmc",(String)objectMap.get("WZMC"));
	        	sfjcMap.put("xhgg",(String)objectMap.get("XHGG"));
	        	sfjcMap.put("jldw",(String)objectMap.get("JLDW"));
	        	sfjcMap.put("jhdj",objectMap.get("JHDJ"));
	        	sfjcMap.put("yf",(String)objectMap.get("YF"));
	        	sfjcMap.put("cwmc",(String)objectMap.get("CWMC"));
	        	sfjcMap.put("bqjyje",objectMap.get("BQJYJE"));
	        	sfjcMap.put("bqjysl",objectMap.get("BQJYSL"));
	        	sfjcMap.put("bqsrje",objectMap.get("BQSRJE"));
	        	sfjcMap.put("bqsrsl",objectMap.get("BQSRSL"));
	        	sfjcMap.put("bqzcje",objectMap.get("BQZCJE"));
	        	sfjcMap.put("bqzcsl",objectMap.get("BQZCSL"));
	        	sfjcMap.put("sqjcje",objectMap.get("SQJCJE"));
	        	sfjcMap.put("sqjcsl",objectMap.get("SQJCSL"));
	        	sfjcMap.put("bqzrsl",objectMap.get("BQZRSL"));
	        	sfjcMap.put("bqzrje",objectMap.get("BQZRJE"));
	        	if("ss".equals(ssflag)){
	        		sfjcMap.put("zjllgcxm",(String)objectMap.get("ZJLLGCXM"));
	        		sfjcMap.put("zjllbz",(String)objectMap.get("ZJLLBZ"));
	        	}
				jsonList.add(sfjcMap);
			}
			Map countMaps = new HashMap();
			if(jsonList.size()>0){
				//判断是否有前一页 没有则为第一页 则新建对象
				if(page.hasPreviousPage()==false){
					//保存开始条数
					saveStart = Integer.parseInt(start)+1;
					sfzjCountMap = new HashMap();
					sfzjCountMap.put("id",UUIDGenerator.hibernateUUID());
					sfzjCountMap.put("wzmc","<font color='blue'>到当前页总和：</font>");
					sfzjCountMap.put("bqjyje",0.0);
					sfzjCountMap.put("bqjysl",0.0);
					sfzjCountMap.put("bqsrje",0.0);
					sfzjCountMap.put("bqsrsl",0.0);
					sfzjCountMap.put("bqzcje",0.0);
					sfzjCountMap.put("bqzcsl",0.0);
					sfzjCountMap.put("sqjcje",0.0);
					sfzjCountMap.put("sqjcsl",0.0);
					sfzjCountMap.put("bqzrsl",0.0);
					sfzjCountMap.put("bqzrje",0.0);
					sfjcManager.setCountValue(null,list,null,sfzjCountMap,"d");
				}else{
					 //如果上一次保存的开始条数大于这次传来的开始条数，则是点击上一页
					 //否则 点击的是下一页
					 if(saveStart>Integer.parseInt(start)){
						Page pages = physicTableManager.pagedQueryTableData(hql,
								 saveStart, Integer.parseInt(limit));
						list = (List) page.getResult();
						sfzjCountMap.put("id",UUIDGenerator.hibernateUUID());
						sfzjCountMap.put("wzmc","<font color='blue'>到当前页总和：</font>");
						sfjcManager.setCountValue(null,list,null,sfzjCountMap,"q");
					 }else{
						 sfjcManager.setCountValue(null,list,null,sfzjCountMap,"d");
					 }
					 saveStart = Integer.parseInt(start);
					 //如果最后一页标志存在 则删除这个标志
					 if(sfzjCountMap.get("last")!=null){
						 sfzjCountMap.remove("last");
					 }
					
				}
			
				//如果有记录 则添加合计
				DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
				conn=dataSource.getConnection();
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(hql);
				//System.out.println(res.next());
				//System.out.println(hql+"```````````````");
				//添加合计
				countMaps.put("id",UUIDGenerator.hibernateUUID());
				countMaps.put("wzmc","<font color='red'>总合计：</font>");
				sfjcManager.setCountValue(res,null,null,countMaps,"h");
				//该页如果没有下一页  则是最后一页 加入标志
				if(!page.hasNextPage()){
					//sfjcManager.setCountValue(res,null,null, sfzjCountMap,"h");
					sfzjCountMap=countMaps;
					sfzjCountMap.put("last", "y");
				} 
			}else{
				//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				sfzjCountMap = new HashMap();
				countMaps = new HashMap();
			}
			// 将集合JSON化
			String json = JSONUtil.listToJson(jsonList, listJsonProperties());
			//到当前页的数据的json字符串
			String pageTotalJson = JSONUtil.mapToJson(sfzjCountMap);
			//System.out.println("-------"+pageTotalJson+"-----------");
			//总合计的json字符串
			String mainTotalJson = JSONUtil.mapToJson(countMaps) ;
			
			StringBuffer dataBlock = new StringBuffer();
			//long total = page.getTotalCount()+(page.getTotalPageCount()-1)*2+1;
			dataBlock.append("{totalCount:'" + page.getTotalCount()+ "',list:"
					+ json + ",pageTotal:"+pageTotalJson+",mainTotal:"+mainTotalJson+"}");
			//System.out.println(dataBlock.toString());
			return dataBlock.toString();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	/**
	 * 收发结存打印
	 */
	public InputStream exportSfjcExcel(List<Sfjc> list) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		//总条数
		int count = list.size();
		//总页数
		int page = count%47>0?count/47+1:count/47;

		// 由工作簿创建工作表
		HSSFSheet sheet = workbook.createSheet();

		// 设置excel每列宽度
		//sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(0, 0);
		sheet.setColumnWidth(1, 3800);
		sheet.setColumnWidth(2, 3800);
		sheet.setColumnWidth(3, 2300);
		sheet.setColumnWidth(4, 2300);
		sheet.setColumnWidth(5, 2800);
		sheet.setColumnWidth(6, 3300);
		sheet.setColumnWidth(7, 3300);
		sheet.setColumnWidth(8, 3300);
		sheet.setColumnWidth(9, 3300);
		sheet.setColumnWidth(10, 3300);
		sheet.setColumnWidth(11, 3300);
		sheet.setColumnWidth(12, 3300);
		sheet.setColumnWidth(13, 3300);

		HSSFPrintSetup hps = sheet.getPrintSetup();
		//hps.setLandscape(true); //横向打印
		hps.setVResolution((short) 300); // 打印状态
		hps.setPageStart((short) 0); // 起始页码
		hps.setHeaderMargin((double) 0.2); // 页眉
		hps.setFooterMargin((double) 0.6); // 页脚

		sheet.setMargin(HSSFSheet.LeftMargin, (short) 0.6); // 左页边距
		sheet.setMargin(HSSFSheet.RightMargin, (short) 0.2); // 右页边距
		sheet.setMargin(HSSFSheet.TopMargin, (short) 0.6); // 上边距
		sheet.setMargin(HSSFSheet.BottomMargin, (short) 0.6); // 下边距

		sheet.setHorizontallyCenter(true); // 水平居中
		// sheet.setVerticallyCenter(true); //垂直居中

		HSSFFont font2 = workbook.createFont();
		font2.setColor(HSSFColor.BLACK.index);
		font2.setFontHeight((short) 170); // 设置字体大小
		font2.setFontName("宋体"); // 设置单元格字体

		// 创建单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 插入数据样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// style2.setWrapText(true);// 自动换行

		// 合计单元格样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style3.setFillForegroundColor(HSSFColor.YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 设置边框
		// style.setBottomBorderColor(HSSFColor.RED.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// style.setFont(font);// 设置字体

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style.setWrapText(false);// 自动换行

		// 创建单元格样式
		HSSFCellStyle style4 = workbook.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);

		style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		style4.setFont(font2);

		// 设置单元格内容格式
		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style1.setWrapText(true);// 自动换行

		// NO验货单编号样式
		HSSFCellStyle style6 = workbook.createCellStyle();
		style6.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style6.setWrapText(true);// 自动换行

		// 第六行样式
		HSSFCellStyle style5 = workbook.createCellStyle();
		style5.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));

		style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平方向的对齐方式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式

		style5.setWrapText(true);// 自动换行

		// 列头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font.setFontHeight((short) 380); // 设置字体大小
		font.setFontName("宋体"); // 设置单元格字体
		font.setUnderline((byte) 1); // 设置下划线
		titleStyle.setFont(font);
		
		//标题月份
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平方向的对齐方式
		dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直方向的对齐方式
		HSSFFont font1 = workbook.createFont();
		font1.setColor(HSSFColor.BLACK.index);
		font1.setBoldweight(HSSFPrintSetup.A4_PAPERSIZE); // 打印字体加粗
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // Excel字体加粗
		font1.setFontHeight((short) 250); // 设置字体大小
		font1.setFontName("宋体"); // 设置单元格字体
//		font1.setUnderline((byte) 1); // 设置下划线
		dateStyle.setFont(font1);

		HSSFRow row = null;
		HSSFCell cell = null;
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		DecimalFormat decimalFormat2 = new DecimalFormat("###.000000");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String yhdbh = "";
		String ny = list.get(0).getNf()+"年"+list.get(0).getYf()+"月";
		//列标题
		String[] colTitle = {"物资名称","型号规格","计量单位","计划单价","库位","上期结存数量","上期结存余额","本期收入数量","本期收入金额"
				,"本期支出数量","本期支出金额","本期结余数量","本期结余金额"}; 
		int countRow = 0;
		//本页合计
		Map<String,Double> pageCount = new HashMap<String, Double>();
		//到当前页合计
		Map<String,Double> allCount = new HashMap<String, Double>();
		//初始化
		allCount.put("sqjcje",0.0);
		allCount.put("bqsrje",0.0);
		allCount.put("bqzcje",0.0);
		allCount.put("bqjyje",0.0);
		try {
			//循环页数
			for(int i=0;i<page;i++){
				//初始化
				pageCount.put("sqjcje",0.0);
				pageCount.put("bqsrje",0.0);
				pageCount.put("bqzcje",0.0);
				pageCount.put("bqjyje",0.0);
				// 上间隔
				row = sheet.createRow(countRow);
				row.setHeight((short) 600);
				cell = row.createCell(0);
				countRow++;
				
				//标题栏
				row = sheet.createRow(countRow);
				cell = row.createCell(0);
				cell = row.createCell(1);
				cell.setCellValue(ny);
				cell.setCellStyle(dateStyle);// 设置单元格样式
				cell = row.createCell(2);
				cell.setCellValue("物资收发结存表(实际金额)");
				cell.setCellStyle(titleStyle);// 设置单元格样式
				sheet.addMergedRegion(new CellRangeAddress(countRow,
						countRow, 2, 13));
				countRow++;
				//第二行
				row = sheet.createRow(countRow);
				cell = row.createCell(0);
				//循环添加列标题
				for(int l=1;l<=13;l++){
					cell = row.createCell(l);
					cell.setCellValue(colTitle[l-1]);
					cell.setCellStyle(style);
				}
				countRow++;
				//循环添加内容
				for(int j=0;j<47;j++){
					if(i*47+j<count){
						Sfjc sfjc = list.get(i*47+j);
						Wzda wz = sfjc.getWz();
						row = sheet.createRow(countRow);
						
						cell = row.createCell(0);
						
						//第一列 物资名称
						cell = row.createCell(1);
						cell.setCellValue(wz.getWzmc());
						cell.setCellStyle(style);
						
						//第二列 型号规格
						cell = row.createCell(2);
						cell.setCellValue(wz.getXhgg());
						cell.setCellStyle(style);
						
						//第三列 计量单位
						cell = row.createCell(3);
						cell.setCellValue(wz.getJldw());
						cell.setCellStyle(style);
						
						//第四列 计划单价
						cell = row.createCell(4);
						cell.setCellValue((wz.getJhdj()==0)?"0.00":decimalFormat.format(wz.getJhdj()));
						cell.setCellStyle(style4);
						
						//第五列 库位
						cell = row.createCell(5);
						cell.setCellValue(wz.getKw().getCwmc());
						cell.setCellStyle(style);
						
						//第六列 上期结存数量
						cell = row.createCell(6);
						cell.setCellValue((sfjc.getSqjcsl()==0)?"0.00":decimalFormat.format(sfjc.getSqjcsl()));
						cell.setCellStyle(style4);
						
						//第七列 上期结存金额
						cell = row.createCell(7);
						cell.setCellValue((sfjc.getSqjcje()==0)?"0.00":decimalFormat.format(sfjc.getSqjcje()));
						cell.setCellStyle(style4);
						
						pageCount.put("sqjcje", sfjc.getSqjcje()+pageCount.get("sqjcje"));
						
						//第八列  本期收入数量
						cell = row.createCell(8);
						cell.setCellValue((sfjc.getBqsrsl()==0)?"0.00":decimalFormat.format(sfjc.getBqsrsl()));
						cell.setCellStyle(style4);
						
						//第九列 本期收入金额
						cell = row.createCell(9);
						cell.setCellValue((sfjc.getBqsrje()==0)?"0.00":decimalFormat.format(sfjc.getBqsrje()));
						pageCount.put("bqsrje", sfjc.getBqsrje()+pageCount.get("bqsrje"));
						cell.setCellStyle(style4);
						
						//第十列  本期支出数量
						cell = row.createCell(10);
						cell.setCellValue((sfjc.getBqzcsl()==0)?"0.00":decimalFormat.format(sfjc.getBqzcsl()));
						cell.setCellStyle(style4);
						
						//第十一列 本期支出金额
						cell = row.createCell(11);
						cell.setCellValue((sfjc.getBqzcje()==0)?"0.00":decimalFormat.format(sfjc.getBqzcje()));
						pageCount.put("bqzcje",sfjc.getBqzcje()+pageCount.get("bqzcje"));
						cell.setCellStyle(style4);
						
						//第十二列 本期结余数量
						cell = row.createCell(12);
						cell.setCellValue((sfjc.getBqjysl()==0)?"0.00":decimalFormat.format(sfjc.getBqjysl()));
						cell.setCellStyle(style4);
						
						//第十三列  本期结余金额
						cell = row.createCell(13);
						cell.setCellValue((sfjc.getBqjyje()==0)?"0.00":decimalFormat.format(sfjc.getBqjyje()));
						pageCount.put("bqjyje",sfjc.getBqjyje()+pageCount.get("bqjyje"));
						cell.setCellStyle(style4);
						
						countRow++;
					}
				}
				//将值累加
				allCount.put("sqjcje", allCount.get("sqjcje")+pageCount.get("sqjcje"));
				allCount.put("bqsrje", allCount.get("bqsrje")+pageCount.get("bqsrje"));
				allCount.put("bqzcje", allCount.get("bqzcje")+pageCount.get("bqzcje"));
				allCount.put("bqjyje", allCount.get("bqjyje")+pageCount.get("bqjyje"));
				String countStr ="";
				//不是最后一页
				if(i != page-1){
					//添加空行
					row = sheet.createRow(countRow);
					row.setHeight((short)500);
					countRow++;
					
					//添加本页金额合计
					row = sheet.createRow(countRow);
					cell = row.createCell(0);
					//循环添加列标题
					for(int l=1;l<=13;l++){
						cell = row.createCell(l);
						cell.setCellStyle(style);
						switch(l){
							case 1: cell.setCellValue("本页金额合计");break;
							case 7: cell.setCellValue((pageCount.get("sqjcje")==0)?"0.00":decimalFormat.format(pageCount.get("sqjcje")));break;
							case 9: cell.setCellValue((pageCount.get("bqsrje")==0)?"0.00":decimalFormat.format(pageCount.get("bqsrje")));break;
							case 11: cell.setCellValue((pageCount.get("bqzcje")==0)?"0.00":decimalFormat.format(pageCount.get("bqzcje")));break;
							case 13: cell.setCellValue((pageCount.get("bqjyje")==0)?"0.00":decimalFormat.format(pageCount.get("bqjyje")));break;
						}
					}
					countRow++;
					countStr = "到本页金额合计";
				}else{
					countStr = "总合计";
				}
				//总合计
				row = sheet.createRow(countRow);
				//循环添加列标题
				for(int l=1;l<=13;l++){
					cell = row.createCell(0);
					cell = row.createCell(l);
					cell.setCellStyle(style);
					switch(l){
						case 1: cell.setCellValue(countStr);break;
						case 7: cell.setCellValue((allCount.get("sqjcje")==0)?"0.00":decimalFormat.format(allCount.get("sqjcje")));break;
						case 9: cell.setCellValue((allCount.get("bqsrje")==0)?"0.00":decimalFormat.format(allCount.get("bqsrje")));break;
						case 11: cell.setCellValue((allCount.get("bqzcje")==0)?"0.00":decimalFormat.format(allCount.get("bqzcje")));break;
						case 13: cell.setCellValue((allCount.get("bqjyje")==0)?"0.00":decimalFormat.format(allCount.get("bqjyje")));break;
					}
				}
				countRow++;
				row = sheet.createRow(countRow);
				
				sheet.addMergedRegion(new CellRangeAddress(countRow,
						countRow, 2, 3));
				cell = row.createCell(2);
				cell.setCellValue("制表人:"+request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_NAME));
				
				sheet.addMergedRegion(new CellRangeAddress(countRow,
						countRow, 6, 7));
				cell = row.createCell(6);
				cell.setCellValue("打印日期:"+DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
				
				sheet.addMergedRegion(new CellRangeAddress(countRow,
						countRow, 9, 10));
				cell = row.createCell(9);
				cell.setCellValue("第"+(i+1)+"页 共"+page+"页");
				countRow++;
				
				// 下间隔
//				row = sheet.createRow(countRow);
//				row.setHeight((short) 795);
//				cell = row.createCell(0);
//				countRow++;
			}
			
	} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				workbook.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}
	
	/**
	 * 采购计划明细导出
	 */
	public void exportSelectedExcelAction() throws IOException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		String nf  = request.getParameter("nf");
		String yf = request.getParameter("yf");
		String ckid = request.getParameter("ckid");
		String flag = request.getParameter("flag");
		Connection con = null;
		StringBuffer subSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		//查询数据Sql语句
		StringBuffer sql = new StringBuffer("from Sfjc j  where 1=1 " );
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		sql.append(" and j.nf = '"+nf+"'and j.yf = '"+yf+"'");

		//显示列标题数组
		String[] hander = new String[]{"物资名称","型号规格","计量单位","计划单价","库位名称","最近领用工程项目",
				"最近领用班组","上期结存数量","上期结存金额","本期收入数量","本期收入金额",
				"本期支出数量","本期支出金额","本期结余数量","本期结余金额"};
		
		//查询合计数据sql语句
//		StringBuffer countDataSql = new StringBuffer("select sum(j.sqjcsl),sum(j.sqjcje),sum(j.bqsrsl)," +
//				"sum(j.bqsrje),sum(j.bqzcsl),sum(j.bqzcje),sum(j.bqjysl),sum(j.bqjyje) from tb_wz_twzsfjcb j ");
//		countDataSql.append(" where  j.nf = '"+nf+"'and j.yf = '"+yf+"'");
		//添加仓库信息
		if(!"undefined".equals(ckid)){
			sql.append(" and j.ck.id = '"+ckid+"'");
		//	countDataSql.append(" and j.ck = '"+ckid+"'");
		}
	
		if(StringUtil.isNotEmpty(flag)){
			//有出入库记录及有库存的
			if("crkkc".equals(flag)){
				whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0)");
			}
			//有库存的物资
			if("kc".equals(flag)){
				whereSql.append(" and j.bqjysl>0");
			}
			//没有出入库的物资
			if("ncrk".equals(flag)){
				whereSql.append(" and j.bqsrsl =0 and bqzcsl=0");
			}
			//月末库存为负的物资
			if("nkc".equals(flag)){
				whereSql.append(" and j.bqjysl <0");
			}
		}else{
			whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0 or j.sqjcsl>0)");
		}
		sql.append(whereSql);
		//添加排序 按库位排序
		sql.append(" order by j.wz.kw.cwmc desc");
		List<Sfjc> list = sfjcManager.find(sql.toString());
		
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");

		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("导出数据_" + System.currentTimeMillis() + ".xls")
						.getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			try {
				bis = new BufferedInputStream(this.exportSfjcExcel(list));
			} catch (Exception e) {
				e.printStackTrace();
			}
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (final IOException e) {
			System.out.println("IOException.");
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	/**
	 * 
	 *描述：导出Excel前准备动作
	 *作者：吕超
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String exportExcel() throws Exception {
		String nf  = request.getParameter("nf");
		String yf = request.getParameter("yf");
		String ckid = request.getParameter("ckid");
		String flag = request.getParameter("flag");
		Connection con = null;
		StringBuffer subSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		//查询数据Sql语句
		StringBuffer sql = new StringBuffer("select w.wzmc,w.xhgg,w.jldw,w.jhdj,k.cwmc,j.zjllgcxm,j.zjllbz," +
				"j.sqjcsl,j.sqjcje,j.bqsrsl,j.bqsrje,j.bqzcsl,j.bqzcje,j.bqjysl,j.bqjyje" +
		" from tb_wz_twzsfjcb j, tb_wz_skwgl k,tb_wz_swzda w  where w.id = j.wzbm and k.id = w.kwbm " );
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		sql.append(" and j.nf = '"+nf+"'and j.yf = '"+yf+"'");

		//显示列标题数组
		String[] hander = new String[]{"物资名称","型号规格","计量单位","计划单价","库位名称","最近领用工程项目",
				"最近领用班组","上期结存数量","上期结存金额","本期收入数量","本期收入金额",
				"本期支出数量","本期支出金额","本期结余数量","本期结余金额"};
		
		//查询合计数据sql语句
		StringBuffer countDataSql = new StringBuffer("select sum(j.sqjcsl),sum(j.sqjcje),sum(j.bqsrsl)," +
				"sum(j.bqsrje),sum(j.bqzcsl),sum(j.bqzcje),sum(j.bqjysl),sum(j.bqjyje) from tb_wz_twzsfjcb j ");
		countDataSql.append(" where  j.nf = '"+nf+"'and j.yf = '"+yf+"'");
		//添加仓库信息
		if(!"undefined".equals(ckid)){
			sql.append(" and j.ck = '"+ckid+"'");
			countDataSql.append(" and j.ck = '"+ckid+"'");
		}
		
		if(StringUtil.isNotEmpty(flag)){
			//有出入库记录及有库存的
			if("crkkc".equals(flag)){
				whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0)");
			}
			//有库存的物资
			if("kc".equals(flag)){
				whereSql.append(" and j.bqjysl>0");
			}
			//没有出入库的物资
			if("ncrk".equals(flag)){
				whereSql.append(" and j.bqsrsl =0 and bqzcsl=0");
			}
			//月末库存为负的物资
			if("nkc".equals(flag)){
				whereSql.append(" and j.bqjysl <0");
			}
		}else{
			whereSql.append(" and (j.bqsrsl > 0 or j.bqzcsl >0 or j.bqjysl>0)");
		}
		sql.append(whereSql);
		//添加排序 按库位排序
		sql.append(" order by k.cwmc desc");
		//取出总条数的sql语句
		StringBuffer countSql = new StringBuffer("select count(*) from ("+sql+")");
		//存放列模型集合
		List list = new ArrayList();
		//总条数
		int count=0;
		//合计数据list
		List countList = new ArrayList();
		try{
			DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
			con = dataSource.getConnection();
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(sql.toString());
			ResultSetMetaData rem =  res.getMetaData();
			int countColumn  = rem.getColumnCount();
			for(int i=1;i<=countColumn;i++){
				ExportExcelModel mod = new ExportExcelModel();
				mod.setColumncode(rem.getColumnLabel(i));
				mod.setColumnname(hander[i-1]);
				mod.setColumntype(rem.getColumnClassName(i));
				list.add(mod);
			}
			//获取总条数
			res = st.executeQuery(countSql.toString());
			res.next();
			count = res.getInt(1);
			
			//获取总计数据
			res = st.executeQuery(countDataSql.toString());
			res.next();
			rem =  res.getMetaData();
			countList.add("合计：");
			//添加空值充填物资相关信息
			for(int i =0;i<6;i++){
				countList.add("");
			}
			//添加查询出来的合计数据
			for(int i =1;i<=rem.getColumnCount();i++){
				countList.add(res.getString(i));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(con!=null){
				con.close();
			}
		}
		String fileName="物资收发结存"+nf+"年"+yf+"月统计报表";
		//export(col, headers, dataIndexs, widths);
		this.exportExcelAction(list, new Integer(count), sql.toString(),countList,fileName);
		return null;
	}
	/**
	 * 
	 *描述：导出Excel动作
	 *时间：2010-8-9
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public  Object exportExcelAction(List columnList,Integer listSize,String sql,List countDateList,String fileName) throws Exception {
		//System.out.println("导出开始,id:"+id+",时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
		String tempPath=request.getSession().getServletContext().getRealPath("/temp");
		try{
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".zip").getBytes(), "iso-8859-1"));
			
			Workbook workbook=new Workbook();
			//为了处理大数据量选择分部保存
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempPath+File.separator+fileName+".xls"),"utf-8"));
			//System.out.println(tempPath+File.separator+id+".xls");
			//System.out.println("webroot"+workbook.getHead());
			writer.write(workbook.getHead());
			writer.flush();
			
			//得到列模型对象
			//List columnList=this.getExportColumns(request,response);
			//得到总记录数
			//int listSize = this.getExcelTotal(request,response);
			//List dataList=this.getExportData(request,response);
			//计算分页数
			int sheetCount=0;
			//一页显示的行记录数
			int maxRow=this.getMaxSheetRow(request,response);
			//最后一页显示的行记录数
			int lastRow=0;
			
			int indexRow=1;
			int indexLastRow=0;

			if(listSize<=maxRow){
				sheetCount=1;
				maxRow=listSize;
			}else{
				sheetCount=listSize/maxRow;
				if(listSize%maxRow>0){
					sheetCount+=1;
				}
				lastRow=listSize%maxRow;
			}
			
			indexLastRow=maxRow;
			//得到查询的sql语句
			//String sql=this.getExportDataSql(request,response,indexRow,this.getResRow(),listSize);
			System.out.println(sheetCount);
			for(int i = 1;i <= sheetCount;i++){
				String sheetStart=" <Worksheet ss:Name='Sheet"+i+"'><Table ss:ExpandedColumnCount='"+(columnList.size()+2)+"' ss:ExpandedRowCount='"+(maxRow+10)+"' x:FullColumns='1' x:FullRows='1' ss:DefaultColumnWidth='100' ss:DefaultRowHeight='14.25'><Column ss:AutoFitWidth='0' ss:Width='81'/><Column ss:Index='5' ss:Width='116.25'/>";
				writer.append(sheetStart.toString());
				writer.flush();
				//写入列名
				StringBuffer title=new StringBuffer();
				title.append("<Row>");
				for(Object obj:columnList){
					if(obj instanceof ExportExcelModel){
						ExportExcelModel model=(ExportExcelModel)obj;
						String columnName="";
						if(StringUtils.isNotEmpty(model.getColumncode())){
							columnName=model.getColumncode();
						}else if(StringUtils.isEmpty(model.getColumncode())&&StringUtils.isNotEmpty(model.getColumncode())){
							columnName=model.getColumncode();
						}else{
							continue;
						}
						if("id".equals(columnName.toLowerCase())||"jlid".equals(columnName.toLowerCase())){
							continue;
						}
						title.append(" <Cell ss:StyleID='s21'><Data ss:Type='String'>"+model.getColumnname()+"</Data></Cell>");
					} 
				}
				title.append("</Row>");
				//System.out.println("列对象："+title.toString());
				writer.append(title.toString());
				writer.flush();
				//每次请求的记录条数
				int resRow=this.getResRow();
				
				if(indexRow+resRow>listSize && resRow<=listSize){
					sfjcManager.pagedQueryTableData(sql, indexRow, this.getResRow(), listSize, writer,columnList,fileName);
				}else{
					sfjcManager.pagedQueryTableData(sql, indexRow, this.getResRow(), listSize, writer,columnList,fileName);
				}
				
				for(int j=0;j<maxRow;j++){
					int curRowres=this.getResRow();
					//取出的条数超过最大记录数
					if(resRow+this.getResRow()>maxRow){
						curRowres=maxRow-resRow;
					}
					if(resRow<(j+1)){
						sfjcManager.pagedQueryTableData(sql, indexRow, this.getResRow(), listSize, writer,columnList,fileName);
						resRow=resRow+this.getResRow();
					}
				}
				StringBuffer countData = new StringBuffer("<Row>");
				for(int k = 0;k<countDateList.size();k++){
					countData.append("<Cell ss:StyleID='s21'><Data ss:Type='String'>"+(String)countDateList.get(k)+"</Data></Cell>");
				}
				countData.append("</Row>");
				//System.out.println(countData.toString());
				writer.append(countData);
				writer.flush();
				
				String sheetEnd="</Table> <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'><Selected/><Panes><Pane><Number>3</Number><ActiveRow>5</ActiveRow><ActiveCol>4</ActiveCol></Pane></Panes><ProtectObjects>False</ProtectObjects><ProtectScenarios>False</ProtectScenarios></WorksheetOptions></Worksheet>";
				//System.out.println("end:"+sheetEnd);
				writer.append(sheetEnd);
				writer.flush();
				
				indexRow=indexLastRow;
				if(indexLastRow+maxRow<=listSize){
					indexLastRow=indexLastRow+maxRow;
				}else{
					indexLastRow=lastRow;
					maxRow=lastRow;
				}
			}
			
			String end="</Workbook>";
			writer.append(end);
			writer.flush();
			writer.close();
			//打包
			//System.out.println("id:"+fileName+",打包开始,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			this.packFile(tempPath, fileName);
			//System.out.println("id:"+fileName+",打包结束,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			//加载打好包的文件
			File zipFile=new File(tempPath+File.separator+fileName+".zip");
			BufferedInputStream reader=new BufferedInputStream(new FileInputStream(zipFile));
			
			byte[] buff = new byte[2048000];
			int bytesRead;
			//System.out.println("id:"+fileName+",向客户端输出开始,时间:"+DateUtils.getDate(new Date(), "yyyy-MM-dd-HH-mm-ss"));
			//开始输出
			java.io.BufferedOutputStream outs = new java.io.BufferedOutputStream(response.getOutputStream());
			while (-1 != (bytesRead = reader.read(buff, 0, buff.length))) {
				outs.write(buff, 0, bytesRead);
				//System.out.println(bytesRead);
				outs.flush();
			}
			outs.close();
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//删除打包文件
			File zipFile=new File(tempPath+File.separator+fileName+".zip");
			if(zipFile!=null && zipFile.isFile()){
				zipFile.delete();
				//System.out.println("删除成功!");
			}
			//System.out.println("导出结束id:"+fileName+",时间:"+DateUtils.getDate(new Date(), "yyyyMMddHHmmss"));
		}
		return null;
	}
	
	/**
	 * 
	 *描述：返回一页显示的行数,如果记录超过一页将按照该方法的返回值进行分页(扩展方法,子类可以选择性实现)
	 *时间：2010-8-9
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Integer getMaxSheetRow(HttpServletRequest request, HttpServletResponse response){
		return 65535;
	}
	
	/**
	 * 
	 *描述：一次取的记录数
	 *时间：2010-8-19
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Integer getResRow(){
		return 65535;
	}
	 /**
	 * 在列表中显示的时候,设定对象需要json化的属性
	 * 
	 * @return
	 */
	public List listProperties(){
		return null;
	}
	
	
	/**
	 * 
	 *描述：把文件打包
	 *时间：2010-8-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public synchronized boolean packFile(String path,String fileName){
		try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(path+File.separator+fileName+".zip");
            org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[2048000];
            File file=new File(path+File.separator+fileName+".xls");
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, 1024);
            org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(fileName+".xls");
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
            if(file.isFile()){
            	file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public void setSfjcManager(SfjcManager sfjcManager) {
		this.sfjcManager = sfjcManager;
	}

	public static Map getSfzjCountMap() {
		return sfzjCountMap;
	}

	public static void setSfzjCountMap(Map sfzjCountMap) {
		SfjcAction.sfzjCountMap = sfzjCountMap;
	}

	public static int getSaveStart() {
		return saveStart;
	}

	public static void setSaveStart(int saveStart) {
		SfjcAction.saveStart = saveStart;
	}

	public SfjcManager getSfjcManager() {
		return sfjcManager;
	}

	public String[] getCols() {
		return cols;
	}

	public void setCols(String[] cols) {
		this.cols = cols;
	}

	public PhysicTableManager getPhysicTableManager() {
		return physicTableManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	
}
