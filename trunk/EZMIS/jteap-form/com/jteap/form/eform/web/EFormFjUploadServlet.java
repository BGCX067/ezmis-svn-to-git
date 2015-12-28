package com.jteap.form.eform.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.UUIDGenerator;

/**
 * EForm附件上传处理Servlet
 * @author 谭畅
 *
 */
@SuppressWarnings({"serial","unchecked"})
public class EFormFjUploadServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EFormFjUploadServlet() {
		super();
	}
  
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String repository = SystemConfig.getProperty("EFORM_FJ_REPOSITORY",request.getSession().getServletContext().getRealPath("/"));
		String path = repository+"/tmp";
		File repositoryFile = new File(path);
		if(!repositoryFile.exists())
			repositoryFile.mkdirs();
		
		//附件存放目录
//		File pathFile = getRepositoryPath(request);
		FileItemFactory factory = new DiskFileItemFactory(1000,repositoryFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8"); // Deal with Chinese/Japanese/....
		String result = null;
		String fileId = null;
		try {
			List items = upload.parseRequest(request);
			if (items != null) {
				Iterator itr = items.iterator();
				while (itr.hasNext()) {
					DiskFileItem item = (DiskFileItem) itr.next();
					if (item.isFormField()) {
						continue;
					} else {
						File f = item.getStoreLocation();
						fileId = UUIDGenerator.hibernateUUID();
						File realFile = new File(repositoryFile,fileId);
						f.renameTo(realFile);
					}
				}
			}
		} catch (Exception e) {
			result = "{success:false}";
			e.printStackTrace();
		}
		if(fileId!=null){
			result = "{success:true,fileId:'"+fileId+"'}";
		}
		response.getOutputStream().println(result);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
	}
}
