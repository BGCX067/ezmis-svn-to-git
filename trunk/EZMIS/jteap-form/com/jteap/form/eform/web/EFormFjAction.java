package com.jteap.form.eform.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.eform.manager.EFormFjManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EFormFj;

/**
 * 自定义表单附件动作对象
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class EFormFjAction extends AbstractAction {
	private EFormFjManager eformFjManager;
	
	public EFormFjManager getEformFjManager() {
		return eformFjManager;
	}

	public void setEformFjManager(EFormFjManager eformFjManager) {
		this.eformFjManager = eformFjManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return eformFjManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","name","type","path","creator","creatdt","size","time","dt"};
	}

	/**
	 * 覆盖父类默认的sql查询语句，去掉大字段content，主要是如果大字段过大，导致列表显示慢，甚至可能引起out of memory
	 */
	@Override
	protected String getPageBaseHqlExt() {
		return "select new map(id as id,name as name,type as type,path as path,creator as creator,dt as dt,docid as docid,st as st,size as size) from " + getManager().getEntityClass().getName() + " as obj";	
	}

	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String docid = request.getParameter("docid");
		if(StringUtil.isNotEmpty(docid)){
			HqlUtil.addCondition(hql,"docid",docid);
		}
	}

	/**
	 * 
	 *描述：
	 *	根据EFORMFJ编号查找对应的附件对象并将该对象JSON化输出
	 *时间：2010-5-26
	 *作者：谭畅
	 *参数：@param id 根据附件编号
	 *返回值:{id:'',type:'',name:'',path:'',creator:'',dt:''}
	 *抛出异常：
	 * @throws Exception 
	 */
	public String getEFormFjByIdAction() throws Exception{
		try{
			String id = request.getParameter("id");
			if(StringUtil.isNotEmpty(id)){
				EFormFj fj = eformFjManager.findUniqueBy("id", id);
				if(fj!=null){
					String json = JSONUtil.objectToJson(fj, listJsonProperties());
					this.outputJson("{success:true,data:"+json+"}");
					return NONE;
				}
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		this.outputJson("{success:false,msg:'附件不存在'}");
		return NONE;
	}
	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 下载附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@LogMethod(name = "下载附件")
	public String downloadFj() throws BusinessException {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String id = request.getParameter("id");
			EFormFj fj = eformFjManager.get(id);
			String fileName = new String(fj.getName().toString().getBytes(), "ISO8859-1");
			
			response.reset();
			response.resetBuffer();
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition", "attachment;fileName=" + fileName + "");
			if(fj.getSt().equals("DB")){
				byte[] attachContent =fj.getContent();
				ByteArrayInputStream bais = new ByteArrayInputStream(attachContent);
				int n = (-1);
				byte[] buff = new byte[1024];
				while((n=bais.read(buff))>-1){
					out.write(buff,0,n);
				}
				out.flush();
				bais.close();
			}else{
				String path = fj.getPath();
				String repository = SystemConfig.getProperty("EFORM_FJ_REPOSITORY",request.getSession().getServletContext().getRealPath("/"));
				String filePath = repository+path;
				File file = new File(filePath);
				if(file.exists()){
					FileInputStream fis = new FileInputStream(file);
					int n = (-1);
					byte[] buff = new byte[1024];
					while((n=fis.read(buff))>-1){
						out.write(buff,0,n);
					}
					out.flush();
					fis.close();
				}
			}
		} catch (Exception uee) {
			//throw new BusinessException(uee.getCause());
		} finally {
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return NONE;
	}
	/**
	 * 下载附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@LogMethod(name = "下载图片附件")
	public String downloadImageFj() throws BusinessException {
		Map<String,String> mimeMap = new HashMap<String,String>();
		mimeMap.put("jpeg", "image/jpeg"); 
		mimeMap.put("jpg", "image/jpeg"); 
		mimeMap.put("jpe", "image/jpeg"); 
		mimeMap.put("gif", "image/gif"); 
		mimeMap.put("png", "image/png");
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String id = request.getParameter("id");
			EFormFj fj = eformFjManager.get(id);
			String fileName = new String(fj.getName().toString().getBytes(), "ISO8859-1");
			
			response.reset();
			response.resetBuffer();
			response.setContentType(mimeMap.get(fj.getType().toLowerCase()));
			response.setHeader("Content-disposition", "attachment;fileName=" + fileName + "");
			byte[] attachContent =fj.getContent();
			ByteArrayInputStream bais = new ByteArrayInputStream(attachContent);
			byte[] buff = new byte[1024];
			while(bais.read(buff)!=-1){
				out.write(buff);
				out.flush();
			}
		} catch (Exception uee) {
			//throw new BusinessException(uee.getCause());
		} finally {
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return NONE;
	}
	

	/**
	 * 删除指定的附件
	 * @return
	 * @throws Exception
	 */
	public String deleteEFormFjAction() throws Exception {
		String id = request.getParameter("id");
		try {
			EFormFj fj = eformFjManager.get(id);
			//如果附件的存储方式是采用文件系统的存储方式的话，那么删除记录之前先要删除附件文件
			if(fj.getSt().equals("FS")){
				String repository = SystemConfig.getProperty("EFORM_FJ_REPOSITORY",request.getSession().getServletContext().getRealPath("/"));
				String path = repository+fj.getPath();
				File file = new File(path);
				if(file.exists()){
					file.delete();
				}
			}
			eformFjManager.remove(fj);
			outputJson("{success:true}");
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	/**
	 * 删除临时附件，附件在没有提交表单之前，均数据临时附件
	 * 临时附件保存在repository+'/tmp'目录之下，而repository在配置文件中指定
	 * @return
	 * @throws Exception 
	 */
	public String deleteEFormTempFjAction() throws Exception{
		try{
			String fileId = request.getParameter("id");
			String repository = SystemConfig.getProperty("EFORM_FJ_REPOSITORY",request.getSession().getServletContext().getRealPath("/"));
			String tmpPath = repository+"/tmp";
			String tmpFilePath = tmpPath + "/"+fileId;
			File tmpFile = new File(tmpFilePath);
			if(tmpFile.exists()){
				tmpFile.delete();
			}
			this.outputJson("{success:true}");
		}catch(Exception ex){
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	
}
