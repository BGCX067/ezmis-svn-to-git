package com.jteap.jhtj.dynamicconnect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.jhtj.ljydy.manager.AppSystemConnManager;
import com.jteap.jhtj.ljydy.model.AppSystemConn;

import java.util.*;
@SuppressWarnings({ "unchecked", "serial" })
public class InitServlet extends HttpServlet {
	@Override
	public void init() throws ServletException {
		AppSystemConnManager appSystemService=(AppSystemConnManager)SpringContextUtil.getBean("appSystemConnManager");
		//取得所有的连接
		List<AppSystemConn> list=appSystemService.getAll();
		DynamicConnectionManager dcManager=DynamicConnectionManager.getInstance();
		Iterator<AppSystemConn> it=list.iterator();
		//配置文件读取最大连接
		String maxConnection="100";
		while(it.hasNext()){
			AppSystemConn appConn=it.next();
			dcManager.addConnectionManager(appConn.getClassName(), appConn.getUrl(), appConn.getUserId(), appConn.getUserPwd(), new Integer(maxConnection));
		}
	}
}
