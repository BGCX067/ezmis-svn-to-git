package com.jteap.system.doclib.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.doclib.manager.DoclibAttachManager;
import com.jteap.system.doclib.manager.DoclibCatalogManager;
import com.jteap.system.doclib.manager.DoclibManager;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibAttach;
import com.jteap.system.doclib.model.DoclibCatalog;

@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibAttachAction extends AbstractAction {

	private DoclibAttachManager doclibAttachManager;

	private DoclibCatalogManager doclibCatalogManager;

	private DoclibManager doclibManager;

	private File excelFile;				//附件文件
	
	public DoclibManager getDoclibManager() {
		return doclibManager;
	}

	public void setDoclibManager(DoclibManager doclibManager) {
		this.doclibManager = doclibManager;
	}

	public DoclibCatalogManager getDoclibCatalogManager() {
		return doclibCatalogManager;
	}

	public void setDoclibCatalogManager(
			DoclibCatalogManager doclibCatalogManager) {
		this.doclibCatalogManager = doclibCatalogManager;
	}

	public DoclibAttachManager getDoclibAttachManager() {
		return doclibAttachManager;
	}

	public void setDoclibAttachManager(DoclibAttachManager doclibAttachManager) {
		this.doclibAttachManager = doclibAttachManager;
	}
	
	
	

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	@Override
	public HibernateEntityDao getManager() {
		return doclibAttachManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "name", "type", "doclibSize" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "name", "type", "doclibSize" };
	}

	/**
	 * 显示附件列表
	 */
	public String showUpdateAction() throws Exception {

		String id = request.getParameter("id");
		///System.out.println("----------->id<----------" + id);
		Doclib doclib = this.doclibManager.findDoclibById(id);
		String doclibCatalogId = doclib.getDoclibCatalog().getId();
		//System.out.println("===========>doclibCatalogId<=========="
			//	+ doclibCatalogId);
		String hql = "select * from DoclibCatalog as g where g.id=?";
		DoclibCatalog doclibCatalog = doclibCatalogManager.findUniqueBy("id",
				doclibCatalogId);

		List list = new ArrayList();
		getCatalogField(list, doclibCatalog);
		String json = JSONUtil.listToJson(list, new String[] { "id", "name",
				"type", "doclibSize" });
		outputJson("{success:true,fdlist:" + json + "}");
		//System.out.println("===========>getCatalogFieldJson<============"
		//		+ json);
		return NONE;
	}

	/**
	 * 下载附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String downloadAttach() throws Exception {

		String id = request.getParameter("id");
	//	System.out.println("----------->id<----------" + id);
		response.reset();
		response.resetBuffer();
		response.setContentType("application/x-msdownload");
		DoclibAttach doclibAttach = doclibAttachManager.get(id);
		byte[] attachContent = doclibAttach.getContent();
		javax.servlet.ServletOutputStream out = null;
		try {
			String fileName = new String(doclibAttach.getName().toString()
					.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;fileName="
					+ fileName + "");
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

	/**
	 * 在线打开附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String openAttach() throws Exception {

		String id = request.getParameter("attachId");
		//System.out.println("----------->id<----------" + id);
		response.reset();
		response.resetBuffer();
		response.setContentType("application/octet-stream");
		DoclibAttach doclibAttach = doclibAttachManager.get(id);
		byte[] attachContent = doclibAttach.getContent();
		javax.servlet.ServletOutputStream out = null;
		try {
			String fileName = new String(doclibAttach.getName().toString()
					.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "inline  ;fileName="
					+ fileName + "");
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
	
	/**
	 *保存在线修改的文档附件内容
	 * @return
	 * @throws Exception
	 */
	public String modifyAttachOnLine() throws Exception {

		String id = request.getParameter("attachId");
		//System.out.println("----------->id<----------" + id);
		DoclibAttach doclibAttach = doclibAttachManager.get(id);
		try {
			byte[] attachContent = FileUtils.readFileToByteArray(excelFile);
			doclibAttach.setContent(attachContent);
			doclibAttachManager.save(doclibAttach);
			outputJson("{success:true}");
		} catch (Exception uee) {
			throw new BusinessException(uee.getCause());
		} finally {
			
		}
		return NONE;
	}
	
	
	public String validateAttachUpload() throws Exception {

	//	System.out.println("文件上传大小：" + request.getContentLength());
		//System.out.println("内容类型:" + request.getContentType());
		// MultiPartRequestWrapper
		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 递归获得所有扩展字段对象
	 */
	public void getCatalogField(List list, DoclibCatalog doclibCatalog) {
		Collection fields = doclibCatalog.getFields();
		list.addAll(fields);
		if (doclibCatalog.getParent() != null) {
			getCatalogField(list, doclibCatalog.getParent());
		}
	}
}
