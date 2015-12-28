package com.jteap.jx.qxgl.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.jx.qxgl.model.Qxtj;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.yx.zbjl.manager.ZhiBanJiLuManager;

/**
 * 缺陷统计manager
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class QxtjManager extends HibernateEntityDao<Qxtj> {
	
	/**
	 * 
	 * 描述 : 根据电厂统计期以及统计分类查询缺陷统计
	 * 作者 : wangyun
	 * 时间 : Aug 6, 2010
	 * 参数 : 
	 * 		dctjq ： 电厂统计期
	 * 返回值 : 
	 * 		List<Qxtj> : 缺陷统计列表
	 * 异常 : ParseException
	 */
	public List<Qxtj> findByDctjq(String dctjq,String fl) throws ParseException {
		String hql = "from Qxtj as obj where obj.dctjq='"+dctjq+"' and obj.fl = '"+fl+"'";
		List<Qxtj> lst = this.find(hql);
		return lst;
	}
	/**
	 * 用于每月 28号 执行缺陷统计时 自动导出一份 excel保存 供用户下载查看
	 * @throws Exception
	 */
    public void exportExcel() throws Exception
	    {
	        // Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
	        String paraHeader = "缺陷单编号,机组编号,设备名称,缺陷名称,责任班组,消缺人,缺陷分类,发现人,发现时间,消缺时间,缺陷状态,转发结果";
	        // paraHeader = new String(paraHeader.getBytes("ISO-8859-1"), "UTF-8");

	        // 表索引信息（逗号表达式）
	        String paraDataIndex = "QXDBH,JZBH,SBMC,QXMC,ZRBM,XQR,QXFL,FXR,FXSJ,XQSJ,STATUS,JXBQRJG";

	        // 宽度(逗号表达式)
	        String paraWidth = "130,60,180,200,70,70,70,70,150,150,70,300";
	        DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
	        
	        //获取当前年月
	        Date curD=DateUtils.getPreDate(new Date());
    		String curNian=DateUtils.getDate(curD, "yyyy");
    		String curYue=DateUtils.getDate(curD, "MM");
    		
	        Connection conn = null;
	        try
	        {
	            conn = dataSource.getConnection();
	            StringBuffer sbSql = new StringBuffer();
	            sbSql.append("select a.*,");
	            sbSql.append("b.processinstance_,");
	            sbSql.append("c.id_,");
	            sbSql.append("c.version_,");
	            sbSql.append("c.start_,");
	            sbSql.append("c.end_,");
	            sbSql.append("d.flow_name,");
	            sbSql.append("d.id as FLOW_CONFIG_ID,");
	            sbSql.append("d.flow_form_id ");
	            sbSql.append("from tb_jx_qxgl_qxd a,");
	            sbSql.append("jbpm_variableinstance b,");
	            sbSql.append("jbpm_processinstance c,");
	            sbSql.append("tb_wf_flowconfig d,");
	            sbSql.append("tb_wf_log e ");
	            sbSql.append("where b.name_ = 'docid' ");
	            sbSql.append("and b.stringvalue_ = a.id ");
	            sbSql.append("and b.processinstance_ = c.id_ ");
	            sbSql.append("and c.processdefinition_ = d.pd_id ");
	            sbSql.append("and e.pi_id = c.id_ ");
	            sbSql.append("and a.status is not null and a.status !='申请' ");
	            sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
	    		sbSql.append(" and a.fxsj <= to_date('"+curNian+"-"+curYue+"-25 00:00','yyyy-MM-dd hh24:mi') and a.fxsj >=" +
	            		"add_months(to_date('"+curNian+"-"+curYue+"-25 00:00','yyyy-MM-dd hh24:mi'),-1)") ;
	            String sql = sbSql.toString();

	            Statement st = conn.createStatement();
	            ResultSet rs = st.executeQuery(sql);

	            List list = new ArrayList();
	            ResultSetMetaData rsmeta = rs.getMetaData();

	            while (rs.next())
	            {
	                Map map = new HashMap();
	                for (int i = 1; i <= rsmeta.getColumnCount(); i++)
	                {
	                    Object obj = rs.getObject(i);
	                    // 针对oracle timestamp日期单独处理，转换成date
	                    if (obj instanceof oracle.sql.TIMESTAMP)
	                    {
	                        obj = ((oracle.sql.TIMESTAMP) obj).dateValue()
	                                .toString()
	                                + " "
	                                + ((oracle.sql.TIMESTAMP) obj).timeValue()
	                                        .toString();
	                    }

	                    map.put(rsmeta.getColumnName(i), obj);
	                }
	                list.add(map);
	            }
	            rs.close();

	            // 调用导出方法
	            export(list, paraHeader, paraDataIndex, paraWidth);
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	        finally
	        {
	            if (conn != null)
	            {
	                conn.close();
	            }
	        }
	    }
    
    /**
	 * 获得组装好数据了的inputStream
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private InputStream getExcelInputStream(Collection col, String paraHeader,
			String paraDataIndex, String paraWidth) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, UnsupportedEncodingException {

		if ("".equals(paraHeader) || paraHeader == null) {

		}

		if ("".equals(paraDataIndex) || paraDataIndex == null) {

		}

		if ("".equals(paraWidth) || paraWidth == null) {

		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		HSSFRow row = sheet.createRow(0);
		String[] headers = paraHeader.split(",");
		String[] dataIndexs = paraDataIndex.split(",");
		String[] widths = paraWidth.split(",");

		HSSFCellStyle titleStyle = wb.createCellStyle();
		// titleStyle.setBorderLeft((short)1);
		// titleStyle.setBorderRight((short)1);
		/*
		 * titleStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
		 * titleStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		 */
		titleStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titleStyle.setFont(font);

		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			sheet.setColumnWidth((short) i, (short) (Short
					.parseShort(widths[i]) * (short) 50));
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(titleStyle);

		}

		//
		int count = 1;
		for (Iterator ite = col.iterator(); ite.hasNext();) {
			Object obj = ite.next();
			row = sheet.createRow(count);
			for (int i = 0; i < dataIndexs.length; i++) {
				Object returnValue = BeanUtils.getProperty(obj, dataIndexs[i]);
				HSSFCell cell = row.createCell((short) i);
//				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(returnValue == null ? "" : returnValue
						.toString());
			}
			count++;
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}
	/**
	 * 用于每月 28号 执行缺陷统计时 自动导出一份 excel保存 供用户下载查看
	 * @param col
	 * @param headers
	 * @param dataIndexs
	 * @param widths
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void export(Collection col, String headers, String dataIndexs,
				String widths) throws IOException, SecurityException,
				IllegalArgumentException, NoSuchFieldException,
				NoSuchMethodException, IllegalAccessException,
				InvocationTargetException {

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(this.getExcelInputStream(col,
						headers, dataIndexs, widths));
				String fileUrl = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
				bos = new BufferedOutputStream(new FileOutputStream(fileUrl+"userfiles/excel/缺陷查询.xls"));

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
	 * 根据id以及分类返回json字符串
	 * @return
	 * @throws ParseException 
	 */
	public Map getJsonByTjIdAndFl(String dctjq,String fl) throws ParseException{
		//StringBuffer sb = new StringBuffer();
		Map dataMap = new HashMap<String, String>();
		// 查询缺陷统计
		List<Qxtj> lst = this.findByDctjq(dctjq,fl);
		if (lst.size() > 0) {
			//sb.append("{success:true,data:[{'");
			for (Qxtj qxtj : lst) {
				String xmmc = qxtj.getXmmc();
				// 锅炉
				String gl = qxtj.getGl();
				// 汽机
				String qj = qxtj.getQj();
				// 电气
				String dq = qxtj.getDq();
				// 燃料
				//String rl = qxtj.getRl();
				// 热工
				String rg = qxtj.getRg();
				// 合计
				String hj = qxtj.getHj();
				// 年总计
				String nzj = qxtj.getNzj();
				
				dataMap.put(xmmc+"_GL", gl == null?"":gl);
				dataMap.put(xmmc+"_QJ", qj == null?"":qj);
				dataMap.put(xmmc+"_DQ", dq == null?"":dq);
				//dataMap.put(xmmc+"_RL", rl == null?"":rl);
				dataMap.put(xmmc+"_RG", rg == null?"":rg);
				dataMap.put(xmmc+"_HJ", hj == null?"":hj);
				dataMap.put(xmmc+"_NZJ", nzj == null?"":nzj);
				dataMap.put("STR_DT",qxtj.getTjkssj());
				dataMap.put("END_DT",qxtj.getTjjssj());
			}
		}else{
			//sb.append("{success:false,msg:'无此统计期的缺陷统计单'}");
			return null;
		}
		//System.out.println(sb.toString());
		return dataMap;
	}
	/**
	 * 根据开始时间 结束时间查询缺陷
	 * 厂内统计
	 * @param dt1 开始时间
	 * @param dt2 结束时间
	 */
	public List findQxtj(String dt1,String dt2,Map dataMap){
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rsByfsqxs = null;
		ResultSet rsXcbyqxs = null;
		ResultSet rsZzxc = null;
		ResultSet rsSqqx = null;
		ResultSet rsZj_dtjtd = null;
		ResultSet rsZj_djxcl = null;
		ResultSet rsZj_dbp = null;
		ResultSet rsZj_qt = null;
		ResultSet rsZdqxs = null;
		ResultSet rsDgxqcs = null;
		ResultSet rsByqjzqxs = null;
		ResultSet rsXcbyqqx = null;
		ResultSet rsYjsxqs = null;
		ResultSet rsJxyqnxq = null;
		ResultSet rsJxyqn = null;
		List<Map> lst = new ArrayList<Map>();
		try {
			DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			//取得上一期的年累计
			Map<String,String> oldDataMap = this.getOldDataMap("cn");
			// 调用存储过程
			cs = conn.prepareCall("{call GetQxtj(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			// 预设输出结果的类型
			for (int i = 1; i < 16; i++) {
				cs.registerOutParameter(i, OracleTypes.CURSOR);
			}
			cs.registerOutParameter(16, OracleTypes.VARCHAR);
			cs.registerOutParameter(17, OracleTypes.VARCHAR);
			//如果为空 则是定时任务触发
			cs.setString(16, dt1);
			cs.setString(17, dt2);
			
			cs.execute();
			
			// 本月发生缺陷数
			rsByfsqxs = (ResultSet) cs.getObject(1);
			Map<String, String> mapByfsqxs = new HashMap<String, String>();
			this.setQxValue(rsByfsqxs, "BYFSQXS_", mapByfsqxs, dataMap,oldDataMap);
			lst.add(mapByfsqxs);

			// 消除本月缺陷数
			rsXcbyqxs = (ResultSet) cs.getObject(2);
			Map<String, String> mapXcbyqxs = new HashMap<String, String>();
			this.setQxValue(rsXcbyqxs, "XCBYQXS_", mapXcbyqxs, dataMap,oldDataMap);
			lst.add(mapXcbyqxs);

			// 正在消除
			rsZzxc = (ResultSet) cs.getObject(3);
			Map<String, String> mapZzxc = new HashMap<String, String>();
			this.setQxValue(rsZzxc, "ZZXC_", mapZzxc, dataMap,oldDataMap);
			lst.add(mapZzxc);

			// 申请缺陷
			rsSqqx = (ResultSet) cs.getObject(4);
			Map<String, String> mapSqqx = new HashMap<String, String>();
			this.setQxValue(rsSqqx, "SQQX_", mapSqqx, dataMap,oldDataMap);
			lst.add(mapSqqx);

			
			// 转接_待停机停电
			rsZj_dtjtd = (ResultSet) cs.getObject(5);
			Map<String, String> mapZj_dtjtd = new HashMap<String, String>();
			this.setQxValue(rsZj_dtjtd, "ZJ_DTJTD_", mapZj_dtjtd, dataMap,oldDataMap);
			lst.add(mapZj_dtjtd);
			
			// 转接_待检修处理
			rsZj_djxcl = (ResultSet) cs.getObject(6);
			Map<String, String> mapZj_djxcl = new HashMap<String, String>();
			this.setQxValue(rsZj_djxcl, "ZJ_DJXCL_", mapZj_djxcl, dataMap,oldDataMap);
			lst.add(mapZj_djxcl);
			
			// 转接_待备品
			rsZj_dbp = (ResultSet) cs.getObject(7);
			Map<String, String> mapZj_dbp = new HashMap<String, String>();
			this.setQxValue(rsZj_dbp, "ZJ_DBP_", mapZj_dbp, dataMap,oldDataMap);
			lst.add(mapZj_dbp);
			
			// 转接_其他
			rsZj_qt = (ResultSet) cs.getObject(8);
			Map<String, String> mapZj_qt = new HashMap<String, String>();
			this.setQxValue(rsZj_qt, "ZJ_QT_", mapZj_qt, dataMap,oldDataMap);
			lst.add(mapZj_qt);
			
			// 重大缺陷数
			rsZdqxs = (ResultSet) cs.getObject(9);
			Map<String, String> mapZdqxs = new HashMap<String, String>();
			this.setQxValue(rsZdqxs, "ZDQXS_", mapZdqxs, dataMap,oldDataMap);
			lst.add(mapZdqxs);
			
			// 低谷消缺次数
			rsDgxqcs = (ResultSet) cs.getObject(10);
			Map<String, String> mapDgxqcs = new HashMap<String, String>();
			this.setQxValue(rsDgxqcs, "DGXQCS_", mapDgxqcs, dataMap,oldDataMap);
			lst.add(mapDgxqcs);
			
			// 本月前接转消缺数
			rsByqjzqxs = (ResultSet) cs.getObject(11);
			Map<String, String> mapByqjzqxs = new HashMap<String, String>();
			this.setQxValue(rsByqjzqxs, "BYQJZXQS_", mapByqjzqxs, dataMap,oldDataMap);
			lst.add(mapByqjzqxs);

			// 本月消除延期缺陷数
			rsXcbyqqx = (ResultSet) cs.getObject(12);
			Map<String, String> mapXcbyqqx = new HashMap<String, String>();
			this.setQxValue(rsXcbyqqx, "XCBYQQX_", mapXcbyqqx, dataMap,oldDataMap);
			lst.add(mapXcbyqqx);
			
			// 月及时消缺数
			rsYjsxqs = (ResultSet) cs.getObject(13);
			Map<String, String> mapYjsxqs = new HashMap<String, String>();
			this.setQxValue(rsYjsxqs, "YJSQXS_", mapYjsxqs, dataMap,oldDataMap);
			//lst.add(mapYjsxqs);
			
			rsJxyqnxq = (ResultSet) cs.getObject(14);
			Map<String, String> mapJxyqnxq = new HashMap<String, String>();
			this.setQxValue(rsJxyqnxq, "JXYQNXQ_", mapJxyqnxq, dataMap,oldDataMap);
			//lst.add(mapJxyqnxq);
			
			// 转接_待停机停电
			rsJxyqn = (ResultSet) cs.getObject(15);
			Map<String, String> mapJxyqn = new HashMap<String, String>();
			this.setQxValue(rsJxyqn, "JXYQN_", mapJxyqn, dataMap,oldDataMap);
			//lst.add(mapJxyqn);
			
			// 月及时消缺率
			Map<String, String> mapYjsxql = new HashMap<String, String>();
			for (String key : mapByfsqxs.keySet()) {
				String itemByfsqxs = mapByfsqxs.get(key);
				String zy = key.substring(8);
				String itemByjsxqs = (String)mapYjsxqs.get("YJSQXS_"+zy);
				String itemJxyqnxq = (String)mapJxyqnxq.get("JXYQNXQ_"+zy);
				String itemJxyqn = (String)mapJxyqn.get("JXYQN_"+zy);
				if (StringUtil.isEmpty(itemByjsxqs)) {
					itemByjsxqs = "0";
				}
				if (StringUtil.isEmpty(itemJxyqnxq)) {
					itemJxyqnxq = "0";
				}
				if (StringUtil.isEmpty(itemJxyqn)) {
					itemJxyqn = "0";
				}
				Float per = new Float(0);
				if (StringUtil.isNotEmpty(itemByfsqxs)) {
					per = (Float.parseFloat(itemByjsxqs)+Float.parseFloat(itemJxyqnxq)+Float.parseFloat(itemJxyqn)) / Float.parseFloat(itemByfsqxs) * 100;
					per = (float)Math.round(per*100)/100;
					String strPer = per.toString() + "%";
					mapYjsxql.put("YJSXQL_"+zy, strPer);
					if(dataMap!=null){
						dataMap.put("YJSXQL_"+zy, strPer);
					}
				}
			}
			lst.add(mapYjsxql);
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
	 return lst;
}
	/**
	 * 根据开始时间 结束时间查询缺陷
	 * 报电力集团
	 * @param dt1 开始时间
	 * @param dt2 结束时间
	 */
	public List findQxtjByDljt(String dt1,String dt2,Map dataMap){
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rsByfsqxs = null;
		ResultSet rsByjsxqs = null;
		ResultSet rsJxyqnxq = null;
		ResultSet rsJxyqwxq = null;
		ResultSet rsJxyqn = null;
		ResultSet rsJxyqw = null;
		ResultSet rsYsbhg = null;
		ResultSet rsJxwxq = null;
		ResultSet rsYqsqqx = null;
		ResultSet rsByyq = null;
		ResultSet rsByxcyqqxs = null;
		ResultSet rsBywcsyqx = null;
		ResultSet rsByxqqx100 = null;
		ResultSet rsByxqqx200 = null;
		ResultSet rsYxqs = null;
		List<Map> lst = new ArrayList<Map>();
		try {
			DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			//取得上一期的年累计
			Map<String,String> oldDataMap = this.getOldDataMap("xq");
			// 调用存储过程
			cs = conn.prepareCall("{call GetQxtjBytj(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			// 预设输出结果的类型
			for (int i = 1; i < 16; i++) {
				cs.registerOutParameter(i, OracleTypes.CURSOR);
			}
			cs.registerOutParameter(16, OracleTypes.VARCHAR);
			cs.registerOutParameter(17, OracleTypes.VARCHAR);
			//如果为空 则是定时任务触发
			cs.setString(16, dt1);
			cs.setString(17, dt2);
			
			cs.execute();
			
			// 本月发生缺陷数
			rsByfsqxs = (ResultSet) cs.getObject(1);
			Map<String, String> mapByfsqxs = new HashMap<String, String>();
			this.setQxValue(rsByfsqxs, "BYFSQXS_", mapByfsqxs, dataMap,oldDataMap);
			lst.add(mapByfsqxs);
			
			// 消除本月缺陷数 
			rsByjsxqs = (ResultSet) cs.getObject(2);
			Map<String, String> mapByjsxqs = new HashMap<String, String>();
			this.setQxValue(rsByjsxqs, "BYJSXQS_", mapByjsxqs, dataMap,oldDataMap);
			lst.add(mapByjsxqs);
			
			// 正在消除
			rsJxyqnxq = (ResultSet) cs.getObject(3);
			Map<String, String> mapJxyqnxq = new HashMap<String, String>();
			this.setQxValue(rsJxyqnxq, "JXYQNXQ_", mapJxyqnxq, dataMap,oldDataMap);
			lst.add(mapJxyqnxq);
			
			// 申请缺陷
			rsJxyqwxq = (ResultSet) cs.getObject(4);
			Map<String, String> mapJxyqwxq = new HashMap<String, String>();
			this.setQxValue(rsJxyqwxq, "JXYQWXQ_", mapJxyqwxq, dataMap,oldDataMap);
			lst.add(mapJxyqwxq);
			
			// 转接_待停机停电
			rsJxyqn = (ResultSet) cs.getObject(5);
			Map<String, String> mapJxyqn = new HashMap<String, String>();
			this.setQxValue(rsJxyqn, "JXYQN_", mapJxyqn, dataMap,oldDataMap);
			lst.add(mapJxyqn);
			
			// 转接_待检修处理
			rsJxyqw = (ResultSet) cs.getObject(6);
			Map<String, String> mapJxyqw = new HashMap<String, String>();
			this.setQxValue(rsJxyqw, "JXYQW_", mapJxyqw, dataMap,oldDataMap);
			lst.add(mapJxyqw);
			
			// 转接_待备品
			rsYsbhg = (ResultSet) cs.getObject(7);
			Map<String, String> mapYsbhg = new HashMap<String, String>();
			this.setQxValue(rsYsbhg, "YSBHG_", mapYsbhg, dataMap,oldDataMap);
			lst.add(mapYsbhg);
			
			// 转接_其他
			rsJxwxq = (ResultSet) cs.getObject(8);
			Map<String, String> mapJxwxq = new HashMap<String, String>();
			this.setQxValue(rsJxwxq, "JXWXQ_", mapJxwxq, dataMap,oldDataMap);
			lst.add(mapJxwxq);
			
			// 重大缺陷数
			rsYqsqqx = (ResultSet) cs.getObject(9);
			Map<String, String> mapYqsqqx = new HashMap<String, String>();
			this.setQxValue(rsYqsqqx, "YQSQQX_", mapYqsqqx, dataMap,oldDataMap);
			lst.add(mapYqsqqx);
			
			// 低谷消缺次数
			rsByyq = (ResultSet) cs.getObject(10);
			Map<String, String> mapByyq = new HashMap<String, String>();
			this.setQxValue(rsByyq, "BYYQ_", mapByyq, dataMap,oldDataMap);
			lst.add(mapByyq);
			
			// 本月前接转消缺数
			rsByxcyqqxs = (ResultSet) cs.getObject(11);
			Map<String, String> mapByxcyqqxs = new HashMap<String, String>();
			this.setQxValue(rsByxcyqqxs, "BYXCYQQXS_", mapByxcyqqxs, dataMap,oldDataMap);
			lst.add(mapByxcyqqxs);
			
			// 本月消除延期缺陷数
			rsBywcsyqx = (ResultSet) cs.getObject(12);
			Map<String, String> mapBywcsyqx = new HashMap<String, String>();
			this.setQxValue(rsBywcsyqx, "BYWCSYQX_", mapBywcsyqx, dataMap,oldDataMap);
			lst.add(mapBywcsyqx);
			
			// 本月消除延期缺陷数
			rsByxqqx100 = (ResultSet) cs.getObject(13);
			Map<String, String> mapByxqqx100 = new HashMap<String, String>();
			this.setQxValue(rsByxqqx100, "BYXQQX100_", mapByxqqx100, dataMap,oldDataMap);
			lst.add(mapByxqqx100);
			
			// 本月消除延期缺陷数
			rsByxqqx200 = (ResultSet) cs.getObject(14);
			Map<String, String> mapByxqqx200 = new HashMap<String, String>();
			this.setQxValue(rsByxqqx200, "BYXQQX200_", mapByxqqx200, dataMap,oldDataMap);
			lst.add(mapByxqqx200);
			
			// 本月消缺数
			rsYxqs = (ResultSet) cs.getObject(15);
			Map<String, String> mapYxqs = new HashMap<String, String>();
			this.setQxValue(rsYxqs, "YXQS_", mapYxqs, dataMap,oldDataMap);
			
			// 检修部月及时消缺率
			Map<String, String> mapJxjsxql = new HashMap<String, String>();
			for (String key : mapByfsqxs.keySet()) {
				String itemByfsqxs = mapByfsqxs.get(key);
				String zy = key.substring(8);
				String itemByjsxqs = (String)mapByjsxqs.get("BYJSXQS_"+zy);
				String itemJxyqnxq = (String)mapJxyqnxq.get("JXYQNXQ_"+zy);
				String itemJxyqn = (String)mapJxyqn.get("JXYQN_"+zy);
				if (StringUtil.isEmpty(itemByjsxqs)) {
					itemByjsxqs = "0";
				}
				if (StringUtil.isEmpty(itemJxyqnxq)) {
					itemJxyqnxq = "0";
				}
				if (StringUtil.isEmpty(itemJxyqn)) {
					itemJxyqn = "0";
				}
				
				Float per = new Float(0);
				if (StringUtil.isNotEmpty(itemByfsqxs)) {
					per = (Float.parseFloat(itemByjsxqs)+Float.parseFloat(itemJxyqnxq)+Float.parseFloat(itemJxyqn)) / Float.parseFloat(itemByfsqxs) * 100;
					per = (float)Math.round(per*100)/100;
					String strPer = per.toString() + "%";
					mapJxjsxql.put("JXJSXQL_"+zy, strPer);
					if(dataMap!=null){
						dataMap.put("JXJSXQL_"+zy, strPer);
					}
				}
			}
			lst.add(mapJxjsxql);
			
			// 发电部月及时消缺率
			Map<String, String> mapFdjsxql = new HashMap<String, String>();
			for (String key : mapByfsqxs.keySet()) {
				String itemByfsqxs = mapByfsqxs.get(key);
				String zy = key.substring(8);
				String itemByjsxqs = (String)mapByjsxqs.get("BYJSXQS_"+zy);
				if (StringUtil.isEmpty(itemByjsxqs)) {
					itemByjsxqs = "0";
				}
				Float per = new Float(0);
				if (StringUtil.isNotEmpty(itemByfsqxs)) {
					per = Float.parseFloat(itemByjsxqs) / Float.parseFloat(itemByfsqxs) * 100;
					per = (float)Math.round(per*100)/100;
					String strPer = per.toString() + "%";
					mapFdjsxql.put("FDJSXQL_"+zy, strPer);
					if(dataMap!=null){
						dataMap.put("FDJSXQL_"+zy, strPer);
					}
				}
			}
			lst.add(mapFdjsxql);
			// 检修部消缺率
			Map<String, String> mapJxxql = new HashMap<String, String>();
			for (String key : mapByfsqxs.keySet()) {
				String itemByfsqxs = mapByfsqxs.get(key);
				String zy = key.substring(8);
				String itemXcbyqxs = (String)mapYxqs.get("YXQS_"+zy);
				if (StringUtil.isEmpty(itemXcbyqxs)) {
					itemXcbyqxs = "0";
				}
				Float per = new Float(0);
				if (StringUtil.isNotEmpty(itemByfsqxs)) {
					per = Float.parseFloat(itemXcbyqxs) / Float.parseFloat(itemByfsqxs) * 100;
					per = (float)Math.round(per*100)/100;
					String strPer = per.toString() + "%";
					mapJxxql.put("JXXQL_"+zy, strPer);
					if(dataMap!=null){
						dataMap.put("JXXQL_"+zy, strPer);
					}
				}
			}
			lst.add(mapJxxql);
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
	 return lst;
}
	/**
	 * 保存缺陷统计
	 */
	public void saveQxtj(String flag){
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		StringBuffer sqlSb  = new StringBuffer("select t.id from tb_sys_role t");
		
		//根据类型返回缺陷统计
		List lst =null;
		if("cn".equals(flag)){
			lst = this.findQxtj("0", "0",null);
			sqlSb.append(" where t.role_sn = 'qxd'");
		}
		if("xq".equals(flag)){
			lst = this.findQxtjByDljt("0","0",null);
			sqlSb.append(" where t.role_sn = 'qxxd'");
		}
		ZhiBanJiLuManager zbjlManager = (ZhiBanJiLuManager)SpringContextUtil.getBean("zhiBanJiLuManager");
		String roleIds = zbjlManager.findRolIds(sqlSb.toString());
		PersonManager personManager = (PersonManager)SpringContextUtil.getBean("personManager");
		List<Person> personList = (List) personManager.findPersonByRoleIds(roleIds);
		try{
		conn = dataSource.getConnection();
		List<String> lstSql = new ArrayList<String>();
		// 遍历每个项目名称的MAP
		for (int i = 0; i < lst.size(); i++) {
			Map<String, String> map = (Map<String, String>) lst.get(i);
			if (map.size() > 0) {
				// sql语句字段名list
				List<String> lstKey = new ArrayList<String>();
				// sql语句值list
				List<String> lstValue = new ArrayList<String>();
				// 项目名称
				String xmmc = "";
				String zy = "";
				for (String key : map.keySet()) {
					String[] item = key.split("_");
					if (item.length == 2) {
						xmmc = item[0];
						zy = item[1];
					} else {
						xmmc = item[0]+"_"+item[1];
						zy = item[2];
					}
					lstKey.add(zy);
					String value = map.get(key);
					lstValue.add(value);
				}
	
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("insert into TB_JX_QXGL_QXTJ t(id,");
				for (int j = 0; j < lstKey.size(); j++) {
					String key = lstKey.get(j);
					sbsql.append(key);
					sbsql.append(",");
				}
				sbsql.append("tbr,DCTJQ,TJSJ,XMMC,FL,TJKSSJ,TJJSSJ) ");
				String id = UUIDGenerator.hibernateUUID();
				sbsql.append("values('");
				sbsql.append(id);
				sbsql.append("',");
				for (int j = 0; j < lstValue.size(); j++) {
					String value = lstValue.get(j);
					sbsql.append("'");
					sbsql.append(value);
					sbsql.append("',");
				}
				sbsql.append("'");
				if(personList.size()>0){
					sbsql.append(personList.get(0).getUserName());
				}else{
					sbsql.append("系统管理员");
				}
				
				sbsql.append("',");
				sbsql.append("to_char(sysdate,'yyyy-mm')");
				sbsql.append(",sysdate,'");
				sbsql.append(xmmc);
				sbsql.append("','"+flag);//to_date(to_char(add_months(trunc(sysdate),-1),'yyyy-mm')||
				sbsql.append("',to_date(to_char(add_months(trunc(sysdate),-1),'yyyy-mm')||'-25 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				sbsql.append(",to_date(to_char(sysdate,'yyyy-mm')||'-25 00:00:00','yyyy-mm-dd hh24:mi:ss')");//to_date(to_char(sysdate,'yyyy-mm')||
				sbsql.append(")");
				
				//System.out.println("sql:"+sbsql.toString());
				lstSql.add(sbsql.toString());
			}
		}
		operateDb(conn, lstSql);
		conn.commit();
		}catch (Exception e) {
			// TODO: handle exception
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
	}
	
	/**
	 * 
	 * 描述 : 操作DB
	 * 作者 : wangyun
	 * 时间 : Aug 6, 2010
	 * 参数 : 
	 * 		conn ： 数据库连接
	 * 		lstSql ： sql列表
	 * 返回值 : 
	 * 异常 : SQLException
	 */
	private void operateDb(Connection conn, List<String> lstSql) throws SQLException {
		try {
			Statement st = conn.createStatement();
			for (String sql : lstSql) {
				st.executeUpdate(sql);
			}
			st.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	/**
	 * 根据记录集 将对应缺陷项目的 统计值 放入相应的Map中
	 * @param rs  记录集
	 * @param xmmc 项目名称 
	 * @param map  数据map
	 * @param dataMap 用于导出的数据Map
	 * @param tmpRg 热工合计
	 * @param tmpRy 燃运合计
	 * @param tmpDq 电器合计
	 * @throws SQLException
	 */
	public void setQxValue(ResultSet rs,String xmmc,Map map,Map dataMap,Map<String,String> oldDataMap) throws SQLException{
		int tmpRg = 0;
		int tmpRy = 0;
		int tmpDq = 0;
		int tmpQj = 0;
		int tmpGl = 0;
		int tmpHj = 0;
		int tmpNzj = 0;
		int tmpNull = 0;
		int tmpTj = 0;
		
		while (rs != null && rs.next()) {
			//如果为空 则是检修办公室发出的缺陷 不参加统计
			String count = rs.getString(2);
			if(rs.getString(1)!=null){
				String zrbm = rs.getString(1).toUpperCase();
				String xmmcs = "";
				if(this.getQxzy(zrbm)!=null){
					xmmcs =xmmc+ this.getQxzy(zrbm);
					if (xmmcs.indexOf("_RG") != -1) {
						tmpRg += Integer.valueOf(count); 
					} else if (xmmcs.indexOf("_RY") != -1) {
						tmpRy += Integer.valueOf(count); 
					} else if(xmmcs.indexOf("_DQ") != -1){
						tmpDq += Integer.valueOf(count); 
					}else if(xmmcs.indexOf("_GL")!=-1){
						tmpGl += Integer.valueOf(count);
					}else if(xmmcs.indexOf("_QJ")!=-1){
						tmpQj += Integer.valueOf(count);
					}else if(xmmcs.indexOf("_HJ")!=-1){
						tmpHj += Integer.valueOf(count);
					}else if(xmmcs.indexOf("_NZJ")!=-1){
						tmpNzj += Integer.valueOf(count);
					}else if(xmmcs.indexOf("_TJ")!=-1){
						tmpTj +=Integer.valueOf(count);
					}
				}
			}else{
				//缺陷专业为空的 要在合计中剔除这一项
				tmpNull+=Integer.valueOf(count);
			}
		}
		map.put(xmmc+"HJ",(tmpHj == 0 ? "" : String.valueOf(tmpHj-tmpRy-tmpTj)));
		if(oldDataMap.get(xmmc)!=null){
			map.put(xmmc+"NZJ",(tmpNzj == 0 ? "" :String.valueOf(Integer.valueOf(oldDataMap.get(xmmc))+tmpHj-tmpRy-tmpTj)));
		}else{
			map.put(xmmc+"NZJ",(tmpNzj == 0 ? "" :String.valueOf(tmpNzj-tmpRy-tmpTj)));
		}
		map.put(xmmc+"GL",(tmpGl == 0 ? "" : String.valueOf(tmpGl)));
		map.put(xmmc+"QJ",(tmpQj == 0 ? "" : String.valueOf(tmpQj)));
		map.put(xmmc+"DQ",(tmpDq == 0 ? "" : String.valueOf(tmpDq)));
		map.put(xmmc+"RG",(tmpRg == 0 ? "" : String.valueOf(tmpRg)));
		
		if(dataMap!=null){
			//存储数据map
			dataMap.put(xmmc+"HJ",(tmpHj == 0 ? "" : String.valueOf(tmpHj-tmpRy-tmpTj)));
			dataMap.put(xmmc+"NZJ",(tmpNzj == 0 ? "" : String.valueOf(tmpNzj-tmpRy-tmpTj)));
			dataMap.put(xmmc+"GL",(tmpGl == 0 ? "" : String.valueOf(tmpGl)));
			dataMap.put(xmmc+"QJ",(tmpQj == 0 ? "" : String.valueOf(tmpQj)));
			dataMap.put(xmmc+"DQ",(tmpDq == 0 ? "" : String.valueOf(tmpDq)));
			dataMap.put(xmmc+"RG",(tmpRg == 0 ? "" : String.valueOf(tmpRg)));
			//dataMap.put(xmmc+"RL",(tmpRy == 0 ? "" : String.valueOf(tmpRy)));
		}
		
	}
	/**
	 * 过滤燃运缺陷
	 * @param oldQxzy 需要过滤缺陷专业
	 * @return 缺陷专业
	 */
	private String getQxzy(String oldQxzy){
		if(oldQxzy.equals("RYDS")||oldQxzy.equalsIgnoreCase("RYJW") ){
			return "RY";
		}  
		return oldQxzy;
	}
	
	/**
	 * 根据统计分类 查询上一统计期 项目名称对应的年累计集合
	 * @param fl
	 * @return
	 */
	private Map<String,String> getOldDataMap(String fl){
		Connection conn = null;
		Statement st = null;
		ResultSet rs  = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		Map<String,String> oldDataMap = new HashMap<String, String>();
		//日期类型
		Calendar cl = Calendar.getInstance();
		try{
			conn = dataSource.getConnection();
			st = conn.createStatement();//to_char(add_months(trunc(2011-02),-1),'yyyy-mm')
			rs = st.executeQuery("select xmmc,nzj from tb_jx_qxgl_qxtj where dctjq = to_char(add_months(trunc(sysdate),-1),'yyyy-mm') and fl = '"+fl+"'");
			while(rs.next()){
				oldDataMap.put(rs.getString(1)+"_", rs.getString(2));
				//如果当前月份是一月份 则统计年统计时不需要加上上期统计
				if(cl.get(Calendar.MONTH)+1==1){
					oldDataMap.put(rs.getString(1)+"_", "0");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
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
		return oldDataMap;
	}
}
