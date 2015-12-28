package com.jteap.test.util;

import org.springframework.context.ApplicationContext;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class SpringDBTestCase extends AbstractTransactionalDataSourceSpringContextTests{
	//spring application context 
	protected ApplicationContext ctx;
	
	
	/**
	 * 指定spring配置文件的位置
	 */
	@Override
	protected String[] getConfigLocations() {
		String[] config = new String[]{"classpath:./applicationContext.xml","classpath:./*applicationContext*.xml"};
        return config;
	}

//	/**
//	 * 开始测试时初始化环境变量
//	 */
//	@Override
//	protected void onSetUp() throws Exception {
//		ctx=this.applicationContext;
//		super.onSetUp();
//	}
//	/**
//	 * 测试结束后
//	 */
//	@Override
//	protected void onTearDown() throws Exception {
//		super.onTearDown();
//	}
	
	
}
