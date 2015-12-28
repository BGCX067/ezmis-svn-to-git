package com.jteap.system.doclib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.web.SpringContextUtil;

import freemarker.template.Template;

public class HtmlSupport {

	// 生成静态页面存放相对地址
//	private static String HTML_PATH = SystemConfig.getProperty("CMS.CONTENT.HTML.HTML_PATH","/html");
	// 静态页面后缀：.html
//	private static String HTML_SUFFIX = SystemConfig.getProperty("CMS.CONTENT.HTML.HTML_PATH",".html");
	
	
	/**
	 * 生成静态HTML
	 */
	public static void toHTML(Map<String, Object> data, String temName,String htmlName) {
		try {
			ServletContext context = ServletActionContext.getServletContext();
			String templatePath = SystemConfig.getProperty("CMS.CONTENT.HTML.TEMPLATE_PATH","/jteap/system/doclib/template/");
			String templateEncoding = SystemConfig.getProperty("CMS.CONTENT.HTML.TEMPLATE_ENCODING","utf-8");
			FreeMarkerConfigurer freemarkerConfig = (FreeMarkerConfigurer) SpringContextUtil.getBean("freemarkerConfig");
			// 指定模版路径
			freemarkerConfig.setTemplateLoaderPaths(new String[]{templatePath});
			Template template = freemarkerConfig.getConfiguration().getTemplate(temName, templateEncoding);
			// 静态页面路径
			String htmlPath = context.getRealPath("") + htmlName;
			//System.out.println(htmlPath+"----------------------");
			File htmlFile = new File(htmlPath);   
			File dir = htmlFile.getParentFile();
			dir.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), templateEncoding));
			// 处理模版
			template.process(data, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//
//	/**
//	 * 删除静态HTML
//	 * 
//	 * @throws Exception
//	 */
//	public static void deleteHTML(ServletContext context, String id)
//			throws Exception {
//		try {
//			// 静态页面文件名
//			String htmlName = id + HTML_SUFFIX;
//			// 静态页面路径
//			String htmlPath = context.getRealPath("/") + "/" + htmlName;
//			File htmlFile = new File(htmlPath);
//			if (htmlFile.exists()) {
//				htmlFile.delete();
//			}
//		} catch (RuntimeException e) {
//			throw new Exception();
//		}
//	}
}
