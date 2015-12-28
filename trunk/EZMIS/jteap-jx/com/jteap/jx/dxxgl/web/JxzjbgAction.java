package com.jteap.jx.dxxgl.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.FileUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.JxzjbgManager;
import com.jteap.jx.dxxgl.manager.JzjxjhManager;
import com.jteap.jx.dxxgl.model.Jxzjbg;

/**
 * 检修总结报告Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings({"serial", "unchecked"})
public class JxzjbgAction extends AbstractAction {

	private JxzjbgManager jxzjbgManager;
	private JzjxjhManager jzjxjhManager;
	
	// 报告附件
	private File bgfj;
	private String bgfjContentType;
	private String bgfjFileName;

	/**
	 * 显示总结报告详细信息
	 * 
	 * @throws Exception
	 */
	public String showUpdateAction() throws Exception {
		String jhId = request.getParameter("jhId");
		Jxzjbg jxzjbg = jxzjbgManager.findUniqueBy("jxjhId", jhId);
		String json = JSONUtil.objectToJson(jxzjbg, updateJsonProperties());
		outputJson("{success:true,data:"+json+"}");
		return NONE;
	}

	/**
	 * 保存
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String jhId = request.getParameter("jhId");
		String bgmc = request.getParameter("bgmc");
		String jxxz = request.getParameter("jxxz");
		String ssjz = request.getParameter("ssjz");
		String fzry = request.getParameter("fzry");
		String qsrq = request.getParameter("qsrq");
		String jsrq = request.getParameter("jsrq");
		String bgzy = request.getParameter("bgzy");
		String ysyj = request.getParameter("ysyj");
		String ysrq = request.getParameter("ysrq");
		String ysbm = request.getParameter("ysbm");
		String czwt = request.getParameter("czwt");
		String bzsm = request.getParameter("bzsm");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dtQsrq = null;
			if (StringUtil.isNotEmpty(qsrq)) {
				dtQsrq = sdf.parse(qsrq);
			}
			Date dtJsrq = null;
			if (StringUtil.isNotEmpty(jsrq)) {
				dtJsrq = sdf.parse(jsrq);
			}
			Date dtYsrq = null;
			if (StringUtil.isNotEmpty(ysrq)) {
				dtYsrq = sdf.parse(ysrq);
			}
			
			Jxzjbg jxzjbg = null;
			if (StringUtil.isEmpty(id)) {
				jxzjbg = new Jxzjbg();
			} else {
				jxzjbg = jxzjbgManager.get(id);
			}
			
			jxzjbg.setBgmc(bgmc);
			jxzjbg.setJxxz(jxxz);
			jxzjbg.setSsjz(ssjz);
			jxzjbg.setFzry(fzry);
			jxzjbg.setQsrq(dtQsrq);
			jxzjbg.setJsrq(dtJsrq);
			jxzjbg.setBgzy(bgzy);
			jxzjbg.setYsyj(ysyj);
			jxzjbg.setYsrq(dtYsrq);
			jxzjbg.setYsbm(ysbm);
			jxzjbg.setCzwt(czwt);
			jxzjbg.setBzsm(bzsm);
			jxzjbg.setJxjhId(jhId);
			if (bgfj != null) {
				byte[] byBgfj = FileUtils.readFileToByteArray(bgfj);
				jxzjbg.setBgfj(byBgfj);
				jxzjbg.setBgfjMc(bgfjFileName);
			}
			jxzjbgManager.save(jxzjbg);
			outputJson("{success:true,bgId:'"+jxzjbg.getId()+"'}");
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请联系管理员}");
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

		response.reset();
		response.resetBuffer();
		response.setContentType("application/x-msdownload");
		Jxzjbg jxzjbg = jxzjbgManager.get(id);
		byte[] attachContent = null;
		String fileName = "";
		attachContent = jxzjbg.getBgfj();
		fileName = jxzjbg.getBgfjMc();

		ServletOutputStream out = null;
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
	
	@Override
	public HibernateEntityDao getManager() {
		return jxzjbgManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "bgmc", "jxxz", "ssjz", "fzry", "qsrq", "time", "jsrq", "bgzy", "ysyj", "ysrq",
				"ysbm", "czwt", "bzsm", "bgfjMc", "jxjhId" };
	}

	public JxzjbgManager getJxzjbgManager() {
		return jxzjbgManager;
	}

	public void setJxzjbgManager(JxzjbgManager jxzjbgManager) {
		this.jxzjbgManager = jxzjbgManager;
	}

	public JzjxjhManager getJzjxjhManager() {
		return jzjxjhManager;
	}

	public void setJzjxjhManager(JzjxjhManager jzjxjhManager) {
		this.jzjxjhManager = jzjxjhManager;
	}

	public File getBgfj() {
		return bgfj;
	}

	public void setBgfj(File bgfj) {
		this.bgfj = bgfj;
	}

	public String getBgfjContentType() {
		return bgfjContentType;
	}

	public void setBgfjContentType(String bgfjContentType) {
		this.bgfjContentType = bgfjContentType;
	}

	public String getBgfjFileName() {
		return bgfjFileName;
	}

	public void setBgfjFileName(String bgfjFileName) {
		this.bgfjFileName = bgfjFileName;
	}

}
