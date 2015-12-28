/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.nsrinfo.web;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.nsrinfo.manager.NsrInfoManager;
import com.jteap.yx.nsrinfo.model.NsrInfo;
import com.jteap.yx.ywfl.model.YWFLZ;

/**
 * 纳税人基本信息Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unchecked", "unused", "serial" })
public class NsrInfoAction extends AbstractAction {
	

	/**
	 *描述：
	 *	列出指定纳税人指定目录下的所有目录和文件，并计算其大小并返回
	 *时间：2010-1-14
	 *作者：谭畅
	 *参数：
	 *	@request param nsrsbh	纳税人识别号
	 *	@request param curdir	当前目录路径 可以为空，如果没有该参数表明从当前纳税人的根目录开始
	 *返回值:
	 *	{success:1,total:6,data:[
	 *		{
	 *			name:'工商营业执照',
	 *			size:404591,		本目录或文件的大小 字节为单位
	 *			isdir:true,			是文件还是目录
	 *			count:9,			本目录以及子目录中包含的文件个数
	 *			path:'E:$92$YX$92$370112749888888$92$工商营业执照',  经过编码后的路径,作为本记录的唯一标识
	 *			_parent:null,		
	 *			_is_leaf:false		
	 *		},...]
	 *	}
	 *抛出异常：
	 *	@throws Exception 
	 */
	public String showNsrYxDirInfoAction() throws Exception{
		try{
			String nsrsbh = request.getParameter("nsrsbh");			//纳税人识别号
			String curdir	= request.getParameter("anode");
			if(StringUtil.isEmpty(nsrsbh))
				throw new BusinessException("查询影像库目录结构必须指定nsrsbh(纳税人识别号)");
			String yxUrl = SystemConfig.getProperty("YX_FILE_URL");	//影像库根目录
			String nsrRootDir = yxUrl +File.separator + nsrsbh;		//纳税人影像库根目录
			String _parent = (curdir==null?"null":"'"+curdir+"'");
			//如果没有curdir参数,则将纳税人的根目录作为当前目录
			curdir = (StringUtil.isNotEmpty(curdir)?StringUtil.decodeChars(curdir, "\\,/, ,%,&"):nsrRootDir);
			
			File curFile = new File(curdir);
			if(!curFile.exists() || !curFile.isDirectory())
				throw new BusinessException("影像库路径读取错误："+curdir);
			
			File[] fileList = curFile.listFiles();
			StringBuffer data = new StringBuffer();
			//遍历当前目录下的所有文件夹和文件
			for (File f : fileList) {
				String name = f.getName();
				//此处将会把文件的路径作为节点的ID展现，当节点展开的时候需要将路径作为curdir参数传递回来，由于路径中存在非法字符，需要转码，在接收到参数的时候需要进行对应的解码
				String path = StringUtil.encodeChars(f.getAbsolutePath(), "\\,/, ,%,&");
				long size = 0;				//本文件或目录的大小 文件大小和目录的大小计算方法不一样
				String _leaf = null;		//是否叶子节点
				int count = 0;				//当前目录下文件的个数
				if(f.isDirectory()){
					size = FileUtils.sizeOfDirectory(f);
					count = FileUtils.listFiles(f, null, true).size();
					_leaf="false";
				}else{
					size = f.length();
					count = 1;
					_leaf = "true";
				}
				String isDir = (f.isDirectory()?"true":"false");
				data.append("{name:'"+name+"',size:"+size+",isdir:"+f.isDirectory()+",count:"+count+",path:'"+path+"',_parent:"+_parent+",_is_leaf:"+_leaf+"},");
			}
			data.deleteCharAt(data.length()-1);
			String json = "{success:1,total:"+fileList.length+",data:["+data.toString()+"]}";
			System.out.println(json);
			this.outputJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return NONE;
	}

}
