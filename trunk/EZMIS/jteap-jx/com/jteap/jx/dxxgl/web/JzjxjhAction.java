package com.jteap.jx.dxxgl.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.JzjxjhManager;
import com.jteap.jx.dxxgl.model.Jzjxjh;

/**
 * 机组检修计划action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings({"serial","unchecked","unused"})
public class JzjxjhAction extends AbstractAction {

	private JzjxjhManager jzjxjhManager;

	// 检修任务书
	private File jxrws;
	private String jxrwsContentType;
	private String jxrwsFileName;
	
	// 检修项目
	private File jxxm;
	private String jxxmContentType;
	private String jxxmFileName;
	
	// 检修技术协议
	private File jxjsxy;
	private String jxjsxyContentType;
	private String jxjsxyFileName;
	
	// 其他附件1
	private File qtfj1;
	private String qtfj1ContentType;
	private String qtfj1FileName;
	
	// 其他附件2
	private File qtfj2;
	private String qtfj2ContentType;
	private String qtfj2FileName;

	/**
	 * 
	 * 描述 : 保存/修改 
	 * 作者 : wangyun
	 * 时间 : Aug 9, 2010
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateAction() throws Exception {
		String jhmc = request.getParameter("jhmc");
		String jxxz = request.getParameter("jxxz");
		String jz = request.getParameter("jz");
		String strQsrq = request.getParameter("qsrq");
		String strJsrq = request.getParameter("jsrq");
		String rgfy = request.getParameter("rgfy");
		String clfy = request.getParameter("clfy");
		String fyhj = request.getParameter("fyhj");
		String nrgy = request.getParameter("nrgy");
		String id = request.getParameter("id");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date qsrq = sdf.parse(strQsrq);
			Date jsrq = sdf.parse(strJsrq);
			
			Jzjxjh jzjxjh = null; 
			if (StringUtil.isEmpty(id)) {
				jzjxjh = new Jzjxjh();
			} else {
				jzjxjh = jzjxjhManager.get(id);
			}
			
			// 计划名称
			jzjxjh.setJhmc(jhmc);
			// 检修性质
			jzjxjh.setJxxz(jxxz);
			// 机组
			jzjxjh.setJz(jz);
			// 起始日期
			jzjxjh.setQsrq(qsrq);
			// 结束日期
			jzjxjh.setJsrq(jsrq);
			// 人工费用
			jzjxjh.setRgfy(StringUtil.isEmpty(rgfy)?0D:Double.parseDouble(rgfy));
			// 材料费用
			jzjxjh.setClfy(StringUtil.isEmpty(rgfy)?0D:Double.parseDouble(clfy));
			// 费用合计
			jzjxjh.setFyhj(StringUtil.isEmpty(rgfy)?0D:Double.parseDouble(fyhj));
			// 内容概要
			jzjxjh.setNrgy(nrgy);

			// 检修任务书
			if (jxrws != null) {
				byte[] byJxrws = FileUtils.readFileToByteArray(jxrws);
				jzjxjh.setJxrws(byJxrws);
				jzjxjh.setJxrwsMc(jxrwsFileName);
			}
			// 检修项目
			if (jxxm != null) {
				byte[] byJxxm = FileUtils.readFileToByteArray(jxxm);
				jzjxjh.setJxxm(byJxxm);
				jzjxjh.setJxxmMc(jxxmFileName);
			}
			// 检修技术协议
			if (jxjsxy != null) {
				byte[] byJxjsxy = FileUtils.readFileToByteArray(jxjsxy);
				jzjxjh.setJxjsxy(byJxjsxy);
				jzjxjh.setJxjsxyMc(jxjsxyFileName);
			}
			// 其他附件1
			if (qtfj1 != null) {
				byte[] byQtfj1 = FileUtils.readFileToByteArray(qtfj1);
				jzjxjh.setQtfj1(byQtfj1);
				jzjxjh.setQtfjMc1(qtfj1FileName);
			}
			// 其他附件2
			if (qtfj2 != null) {
				byte[] byQtfj2 = FileUtils.readFileToByteArray(qtfj2);
				jzjxjh.setQtfj2(byQtfj2);
				jzjxjh.setQtfjMc2(qtfj2FileName);
			}
			
			jzjxjhManager.save(jzjxjh);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 下载附件
	 * 作者 : wangyun
	 * 时间 : Aug 10, 2010
	 * 异常 : Exception
	 */
	public String downloadFileAction() {
		String id = request.getParameter("id");
		String name = request.getParameter("name");

		response.reset();
		response.resetBuffer();
		response.setContentType("application/x-msdownload");
		Jzjxjh jzjxjh = jzjxjhManager.get(id);
		byte[] attachContent = null;
		String fileName = "";
		if ("jxrws".equals(name)) {
			attachContent = jzjxjh.getJxrws();
			fileName = jzjxjh.getJxjsxyMc();
		} else if ("jxxm".equals(name)) {
			attachContent = jzjxjh.getJxxm();
			fileName = jzjxjh.getJxxmMc();
		} else if ("jxjsxy".equals(name)) {
			attachContent = jzjxjh.getJxjsxy();
			fileName = jzjxjh.getJxjsxyMc();
		} else if ("qtfj1".equals(name)) {
			attachContent = jzjxjh.getQtfj1();
			fileName = jzjxjh.getQtfjMc1();
		} else if ("qtfj2".equals(name)) {
			attachContent = jzjxjh.getQtfj2();
			fileName = jzjxjh.getQtfjMc2();
		}
		javax.servlet.ServletOutputStream out = null;
		try {
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;fileName=" + fileName + "");
			out = response.getOutputStream();
			out.write(attachContent);
			out.close();
			response.flushBuffer();
		} catch (Exception uee) {
			throw new BusinessException(uee.getCause());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				attachContent = null;
			} catch (IOException e) {
				throw new BusinessException(e.getCause());
			}
		}
		return NONE;
	}

	public String showJzjxjhTreeAction() {
		String parentId = request.getParameter("parentId");
		StringBuffer sb = new StringBuffer();
		try {
			// 展现年份
			if (StringUtil.isEmpty(parentId)) {
				Date dtNow = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String strThisYear = sdf.format(dtNow);
				String strLastYear = String.valueOf(Long.parseLong(strThisYear) - 1);
				String strBeforeLast = String.valueOf(Long.parseLong(strThisYear) - 2);
				sb.append("[{'id':'year@");
				sb.append(strThisYear);
				sb.append("','text':'");
				sb.append(strThisYear);
				sb.append("','expanded':true},{'id':'year@");
				sb.append(strLastYear);
				sb.append("','text':'");
				sb.append(strLastYear);
				sb.append("','expanded':true},{'id':'year@");
				sb.append(strBeforeLast);
				sb.append("','text':'");
				sb.append(strBeforeLast);
				sb.append("','expanded':true}]");
			} else {
				String[] strs = parentId.split("@");
				// 展现机组
				if (strs.length == 2) {
					String year = strs[1];
					List<String> lst = jzjxjhManager.findJzByYear(year);
					sb.append("[");
					for (String jz : lst) {
						sb.append("{'id':'year@");
						sb.append(year);
						sb.append("@");
						sb.append(jz);
						sb.append("','text':'");
						sb.append(jz);
						sb.append("','expanded':true},");
					}
					if (lst.size() > 0) {
						sb = new StringBuffer(sb.subSequence(0, sb.lastIndexOf(",")));
					}
					sb.append("]");
					// 展现计划
				} else {
					String year = strs[1];
					String jz = strs[2];
					sb.append("[");
					List<Object[]> lst = jzjxjhManager.findJhByJzAndYear(year, jz);
					for (Object[] jzjxjh : lst) {
						String id = (String) jzjxjh[0];
						String jhmc = (String) jzjxjh[1];
						sb.append("{'id':'");
						sb.append(id);
						sb.append("','text':'");
						sb.append(jhmc);
						sb.append("','cls':'x-tree-node-leaf','leaf':true},");
					}
					if (lst.size() > 0) {
						sb = new StringBuffer(sb.subSequence(0, sb.lastIndexOf(",")));
					}
					sb.append("]");
				}
			}
			this.outputJson(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtil.isNotEmpty(hqlWhere)) {
			hqlWhere = hqlWhere.replaceAll("@", "#");
			HqlUtil.addWholeCondition(hql, hqlWhere);
		}
	}

	@Override
	protected String getPageBaseHqlExt() {
		return "select new com.jteap.jx.dxxgl.model.Jzjxjh(id,jhmc,jxxz,jz,qsrq,jsrq,rgfy,clfy,nrgy) from "
				+ getManager().getEntityClass().getName() + " as obj";
	}

	@Override
	public HibernateEntityDao getManager() {
		return jzjxjhManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] {"id", "jhmc", "jxxz", "jz", "qsrq", "time", "jsrq", "rgfy", "clfy", "nrgy" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] {"id", "jhmc", "jxxz", "jz", "qsrq", "time", "jsrq", "rgfy", "clfy", "nrgy", "fyhj", "jxrwsMc", "jxxmMc", "jxjsxyMc", "qtfjMc1", "qtfjMc2" };
	}

	public JzjxjhManager getJzjxjhManager() {
		return jzjxjhManager;
	}

	public void setJzjxjhManager(JzjxjhManager jzjxjhManager) {
		this.jzjxjhManager = jzjxjhManager;
	}

	public File getJxrws() {
		return jxrws;
	}

	public void setJxrws(File jxrws) {
		this.jxrws = jxrws;
	}

	public String getJxrwsContentType() {
		return jxrwsContentType;
	}

	public void setJxrwsContentType(String jxrwsContentType) {
		this.jxrwsContentType = jxrwsContentType;
	}

	public File getJxxm() {
		return jxxm;
	}

	public void setJxxm(File jxxm) {
		this.jxxm = jxxm;
	}

	public String getJxxmContentType() {
		return jxxmContentType;
	}

	public void setJxxmContentType(String jxxmContentType) {
		this.jxxmContentType = jxxmContentType;
	}

	public File getJxjsxy() {
		return jxjsxy;
	}

	public void setJxjsxy(File jxjsxy) {
		this.jxjsxy = jxjsxy;
	}

	public String getJxjsxyContentType() {
		return jxjsxyContentType;
	}

	public void setJxjsxyContentType(String jxjsxyContentType) {
		this.jxjsxyContentType = jxjsxyContentType;
	}


	public File getQtfj1() {
		return qtfj1;
	}

	public void setQtfj1(File qtfj1) {
		this.qtfj1 = qtfj1;
	}

	public String getQtfj1ContentType() {
		return qtfj1ContentType;
	}

	public void setQtfj1ContentType(String qtfj1ContentType) {
		this.qtfj1ContentType = qtfj1ContentType;
	}


	public File getQtfj2() {
		return qtfj2;
	}

	public void setQtfj2(File qtfj2) {
		this.qtfj2 = qtfj2;
	}

	public String getQtfj2ContentType() {
		return qtfj2ContentType;
	}

	public void setQtfj2ContentType(String qtfj2ContentType) {
		this.qtfj2ContentType = qtfj2ContentType;
	}

	public String getJxrwsFileName() {
		return jxrwsFileName;
	}

	public void setJxrwsFileName(String jxrwsFileName) {
		this.jxrwsFileName = jxrwsFileName;
	}

	public String getJxxmFileName() {
		return jxxmFileName;
	}

	public void setJxxmFileName(String jxxmFileName) {
		this.jxxmFileName = jxxmFileName;
	}

	public String getJxjsxyFileName() {
		return jxjsxyFileName;
	}

	public void setJxjsxyFileName(String jxjsxyFileName) {
		this.jxjsxyFileName = jxjsxyFileName;
	}

	public String getQtfj1FileName() {
		return qtfj1FileName;
	}

	public void setQtfj1FileName(String qtfj1FileName) {
		this.qtfj1FileName = qtfj1FileName;
	}

	public String getQtfj2FileName() {
		return qtfj2FileName;
	}

	public void setQtfj2FileName(String qtfj2FileName) {
		this.qtfj2FileName = qtfj2FileName;
	}

}
