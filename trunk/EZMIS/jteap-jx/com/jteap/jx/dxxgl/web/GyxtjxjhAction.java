package com.jteap.jx.dxxgl.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.jx.dxxgl.manager.GyxtjxjhBjManager;
import com.jteap.jx.dxxgl.manager.GyxtjxjhManager;
import com.jteap.jx.dxxgl.manager.JxsbtzManager;
import com.jteap.jx.dxxgl.model.Gyxtjxjh;
import com.jteap.jx.dxxgl.model.GyxtjxjhBj;
import com.jteap.jx.dxxgl.model.Jxsbtz;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 公用系统检修计划Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class GyxtjxjhAction extends AbstractAction {

	private GyxtjxjhManager gyxtjxjhManager;
	private JxsbtzManager jxsbtzManager;
	private GyxtjxjhBjManager gyxtjxjhBjManager;
	private DictManager dictManager;
	
	/**
	 * 
	 * 描述 : 保存公用检修计划
	 * 作者 : wangyun
	 * 时间 : Aug 19, 2010
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateJhAction() throws Exception {
		String json = request.getParameter("json");
		String year = request.getParameter("year");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String nowYear = sdf.format(new Date());
			
			List<Map<String, String>> lstMap = JSONUtil.parseList(json);
			for (Map<String, String> map : lstMap) {
				String strScjxsj = "";
				String id = map.get("id");
				String sbid = map.get("sbid");
				String sbmc = map.get("sbmc");
				
				Gyxtjxjh gyxtjjxjh = null;
				if (StringUtil.isEmpty(id)) {
					gyxtjjxjh = new Gyxtjxjh();
				} else {
					gyxtjjxjh = gyxtjxjhManager.get(id);
				}
				
				for (String key : map.keySet()) {
					if ("id".equals(key) || "sbid".equals(key) || "sbmc".equals(key)) {
						continue;
					}
					String[] keys = key.split("_");
					String index = keys[1];
					String value = map.get(key);
					String monthMethod = "setMonth"+index;
					Class[] types = new Class[] {String.class};
					Method method = Gyxtjxjh.class.getMethod(monthMethod, types);
					method.invoke(gyxtjjxjh, value);
					
					if (StringUtil.isNotEmpty(value)) {
						strScjxsj = nowYear + "-" + index;
					}
				}
				
				// 设备ID
				gyxtjjxjh.setSbid(sbid);
				// 设备名称
				gyxtjjxjh.setSbmc(sbmc);
				// 计划年份
				gyxtjjxjh.setJhnf(year);
				
				// 上次检修时间
				if (StringUtil.isNotEmpty(strScjxsj)) {
					gyxtjjxjh.setScjxsj(strScjxsj);
				}
				gyxtjxjhManager.save(gyxtjjxjh);
			}
			outputJson("{success:true}");
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 导出公用系统检修计划
	 * 作者 : wangyun
	 * 时间 : Aug 21, 2010
	 * 异常 : Exception
	 * 
	 */
	public String exportExcelAction() throws Exception {
		String jhnf = request.getParameter("jhnf");

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String((jhnf + "年公用系统检修计划.xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			
			bis = new BufferedInputStream(getExcelInputStream(jhnf));
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 获得excel输出流（拼接excel表单）
	 * 作者 : wangyun
	 * 时间 : Aug 21, 2010
	 * 参数 : 
	 * 		jhnf ： 计划年份
	 * 返回值 : 
	 * 		InputStream ： excel输出流
	 * 异常 : Exception
	 * 
	 */
	private InputStream getExcelInputStream(String jhnf) throws Exception {
		HSSFWorkbook wb = null;
		InputStream is=null;
		
		try {
			String path = request.getSession().getServletContext().getRealPath("\\jteap\\jx\\dxxgl\\gyxtjxjh\\gyxtjxjh_template.xls");
			is=new FileInputStream(path);
			wb=new HSSFWorkbook(is);

			// 一级设备
			// 一级设备字体
			HSSFFont levelFont1 = wb.createFont();
			levelFont1.setFontHeightInPoints((short)14);
			levelFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			levelFont1.setFontName("宋体");
			// 一级设备样式
			HSSFCellStyle levelStyle1 = wb.createCellStyle();
			levelStyle1.setFont(levelFont1);
			levelStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			levelStyle1.setBorderBottom((short)1);
			levelStyle1.setBorderLeft((short)1);
			levelStyle1.setBorderRight((short)1);
			levelStyle1.setBorderTop((short)1);
			
			// 二级设备
			// 二级设备字体
			HSSFFont levelFont2 = wb.createFont();
			levelFont2.setFontHeightInPoints((short)12);
			levelFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			levelFont2.setFontName("宋体");
			// 二级设备样式
			HSSFCellStyle levelStyle2 = wb.createCellStyle();
			levelStyle2.setFont(levelFont2);
			levelStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			levelStyle2.setBorderBottom((short)1);
			levelStyle2.setBorderLeft((short)1);
			levelStyle2.setBorderRight((short)1);
			levelStyle2.setBorderTop((short)1);
			
			// 三级设备
			// 三级设备字体
			HSSFFont levelFont3 = wb.createFont();
			levelFont3.setFontHeightInPoints((short)12);
			levelFont3.setFontName("宋体");
			// 三级设备样式
			HSSFCellStyle levelStyle3 = wb.createCellStyle();
			levelStyle3.setFont(levelFont3);
			levelStyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			levelStyle3.setBorderBottom((short)1);
			levelStyle3.setBorderLeft((short)1);
			levelStyle3.setBorderRight((short)1);
			levelStyle3.setBorderTop((short)1);
			
			// 其他单元格样式
			// 其他单元格字体
			HSSFFont otherFont = wb.createFont();
			otherFont.setFontHeightInPoints((short)10);
			otherFont.setFontName("宋体");
			// 其他单元格样式
			HSSFCellStyle otherStyle = wb.createCellStyle();
			otherStyle.setFont(otherFont);
			otherStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			otherStyle.setBorderBottom((short)1);
			otherStyle.setBorderLeft((short)1);
			otherStyle.setBorderRight((short)1);
			otherStyle.setBorderTop((short)1);
			
			// 查找所有专业
			DictManager dictManager = (DictManager)SpringContextUtil.getBean("dictManager");
			List<Dict> dictList = (List<Dict>)dictManager.findDictByUniqueCatalogName("SSZY");
			// 遍历所有专业
			for (Dict dict : dictList) {
				String value = dict.getValue();
				String key = dict.getKey();
				HSSFSheet sheet = wb.getSheet(key);

				// 查找每个专业下所有设备
				List<Jxsbtz> lstSbtz = jxsbtzManager.findBy(Jxsbtz.class, "sszy", value);
				for (int i = 0; i < lstSbtz.size(); i++) {
					Jxsbtz jxsbtz = lstSbtz.get(i);
					String xmxh = jxsbtz.getXmxh();
					String xmjb = jxsbtz.getXmjb();
					String sbmc = jxsbtz.getSbmc();
					String jxzq = jxsbtz.getJxzq();
					String sbid = jxsbtz.getId();
					
					// 设置每个设备对应行号
					HSSFRow row = sheet.createRow(i+2);
					row.setHeightInPoints((float)14.25);
					HSSFCell xhCell = row.createCell(0);
//					xhCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					xhCell.setCellValue(xmxh);
					xhCell.setCellStyle(otherStyle);
					
			
					// 设置每个设备对应的设备名称单元格
					HSSFCell sbmcCell = row.createCell(1);
//					sbmcCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					sbmcCell.setCellValue(sbmc);
					if ("1".equals(xmjb)) {
						sbmcCell.setCellStyle(levelStyle1);
					} else if ("2".equals(xmjb)) {
						sbmcCell.setCellStyle(levelStyle2);
					} else {
						sbmcCell.setCellStyle(levelStyle3);
					}
					
					// 设置每个设备对应检修周期
					HSSFCell zqCell = row.createCell(2);
//					zqCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					zqCell.setCellValue(jxzq);
					zqCell.setCellStyle(otherStyle);
					
					// 获得每个项目对应的计划记录
					Gyxtjxjh gyxtjxjh =gyxtjxjhManager.findBySbAndYear(sbid, jhnf);
					
					String strScjxsj = gyxtjxjh.getScjxsj();

					// 设置上次检修时间
					HSSFCell jxsjCell = row.createCell(3);
//					jxsjCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					jxsjCell.setCellValue(strScjxsj);
					jxsjCell.setCellStyle(otherStyle);
					
					// 设置每个月份检修计划
					for (int j = 1; j < 13; j++) {
						String strGet = "getMonth" + j;
						Method method = Gyxtjxjh.class.getMethod(strGet, new Class[]{});
						Object obj = method.invoke(gyxtjxjh, new Object[]{});
						String motValue = "";
						if (obj != null) {
							motValue = obj.toString();
						}
						GyxtjxjhBj gyxtjxjhBj = gyxtjxjhBjManager.findByGyxtjxjh(sbid, String.valueOf(j), jhnf);
						String bjys = "";
						if (gyxtjxjhBj != null) {
							bjys = gyxtjxjhBj.getBjys();
						}
						if (StringUtil.isNotEmpty(bjys)) {
							bjys = dictManager.findDictByCatalogNameWithKey("YS2EXCEL", bjys).getValue();

							// 有颜色单元格样式
							// 有颜色单元格字体
							HSSFFont colorFont = wb.createFont();
							colorFont.setFontHeightInPoints((short)10);
							colorFont.setFontName("宋体");
							// 有颜色单元格样式
							HSSFCellStyle colorStyle = wb.createCellStyle();
							colorStyle.setFont(colorFont);
							colorStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							colorStyle.setFillForegroundColor(Short.parseShort(bjys));
							colorStyle.setFillBackgroundColor(Short.parseShort(bjys));
							colorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
							colorStyle.setBorderBottom((short)1);
							colorStyle.setBorderLeft((short)1);
							colorStyle.setBorderRight((short)1);
							colorStyle.setBorderTop((short)1);

							HSSFCell monthCell = row.createCell((3+j));
//							monthCell.setEncoding(HSSFCell.ENCODING_UTF_16);
							monthCell.setCellValue(motValue);
							monthCell.setCellStyle(colorStyle);
							colorFont = null;
							colorStyle = null;

							continue;
						}
						
						HSSFCell monthCell = row.createCell((3+j));
//						monthCell.setEncoding(HSSFCell.ENCODING_UTF_16);
						monthCell.setCellValue(motValue);
						monthCell.setCellStyle(otherStyle);
					}
				}
			}
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			byte[] content = os.toByteArray();
			InputStream ret = new ByteArrayInputStream(content);
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return gyxtjxjhManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public GyxtjxjhManager getGyxtjxjhManager() {
		return gyxtjxjhManager;
	}

	public void setGyxtjxjhManager(GyxtjxjhManager gyxtjxjhManager) {
		this.gyxtjxjhManager = gyxtjxjhManager;
	}

	public JxsbtzManager getJxsbtzManager() {
		return jxsbtzManager;
	}

	public void setJxsbtzManager(JxsbtzManager jxsbtzManager) {
		this.jxsbtzManager = jxsbtzManager;
	}

	public GyxtjxjhBjManager getGyxtjxjhBjManager() {
		return gyxtjxjhBjManager;
	}

	public void setGyxtjxjhBjManager(GyxtjxjhBjManager gyxtjxjhBjManager) {
		this.gyxtjxjhBjManager = gyxtjxjhBjManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

}
