package com.jteap.system.doclib.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.io.FileUtils;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;


/**
 * 文档附件上传之前验证上传文件的大小
 * @author 彭永贵
 * 2009-00-28
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class ValidateFileServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ValidateFileServlet() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * 提取客户端上传的文件的文件名，文件大小，返回客户端。
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String sMaxSize = SystemConfig.getProperty("DOCLIB.FILEUPLOAD.MAXSIZE");
		Long lMaxSize = sMaxSize==""?0L:Long.parseLong(sMaxSize);
		String json = "" ;
		try {
			json = uploadValidate(request,lMaxSize);
			outputJson(request,response,"{success:true,"+json+"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 附件上传之前验证文件大小
	 * @param request
	 * @param sMaxSize
	 * @return
	 * @throws IOException
	 * @throws FileUploadException
	 */
	private String uploadValidate(HttpServletRequest request,Long lMaxSize) throws IOException,
    FileUploadException {
		
		FileItemFactory factory = new DiskFileItemFactory();
		// 通过该工厂对象创建ServletFileUpload对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		String json = "" ;
		String fileName = "" ;
		for (Iterator i = items.iterator(); i.hasNext();) {
		    FileItem fileItem = (FileItem) i.next();
		    StringBuffer strBuffer = new StringBuffer();
		    // 如果该FileItem不是表单域
		    if (!fileItem.isFormField()) {
		    	fileName= fileItem.getName().substring(
		                fileItem.getName().lastIndexOf("\\") + 1);	     
		       
		    	strBuffer.append("fdList:" + "{'filename':'");
		    	strBuffer.append(fileName);
		    	strBuffer.append("','filesize':");
		    	strBuffer.append(fileItem.getSize());
		    	strBuffer.append(",");
		    	strBuffer.append("'maxsize':");
		    	strBuffer.append(lMaxSize);
		    	strBuffer.append("}");
		    	json = strBuffer.toString();
		    }	    
		}	 
		return json ;
		
	}
	
	/**
	 * 
	 * @param request
	 * @param sMaxSize
	 * @return
	 * @throws IOException
	 * @throws FileUploadException
	 */
	private boolean upload(HttpServletRequest request,Long lMaxSize) throws IOException,
    FileUploadException {
		
		FileItemFactory factory = new DiskFileItemFactory();
		// 通过该工厂对象创建ServletFileUpload对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		boolean bUpload = false ;
		long completedSize = 0L ;
		for (Iterator i = items.iterator(); i.hasNext();) {
		    FileItem fileItem = (FileItem) i.next();
		    // 如果该FileItem不是表单域
		    if (!fileItem.isFormField()) {
		        String fileName = fileItem.getName().substring(
		                fileItem.getName().lastIndexOf("\\") + 1);	     
		        File file = new File("C:\\", fileName);
		        InputStream in = fileItem.getInputStream();
		        FileOutputStream out = new FileOutputStream(file);
		        byte[] buffer = new byte[1024]; // To hold file contents
		        int n;
		        while ((n = in.read(buffer)) != -1) { 
		        	out.write(buffer, 0, n);
		            completedSize += (long) n;
		            if(completedSize>lMaxSize){       //判断文件大小，当文件到达上传最大值时停止上传
		            	return bUpload ;
		            }
		        }
		        out.close();
		        //byte[] fileContent = FileUtils.readFileToByteArray(file);      
		        bUpload = true ;
		        fileItem.delete();// 内存中删除该数据流
		        file.delete();
		        bUpload = true ;     
		    }
		}	
		return bUpload ;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param json
	 * @throws Exception
	 */
	private void outputJson(HttpServletRequest request,HttpServletResponse response,String json) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		String cb = request.getParameter("callback");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String outJson = json;
		if (StringUtil.isNotEmpty(cb)) {
			outJson = cb + "(" + json + ");";
		}
		//log.info("输出JSON:" + outJson);
		out.print(outJson);
	}


	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
