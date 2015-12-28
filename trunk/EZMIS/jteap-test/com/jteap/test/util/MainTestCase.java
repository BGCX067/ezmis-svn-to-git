package com.jteap.test.util;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public abstract class MainTestCase extends TestCase {

	protected ApplicationContext ctx;
	protected final static String SESSION_FACTORY_NAME = "sessionFactory";
	private SessionFactory sessionFactory = null;

	@Override
	protected void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("**/*context*.xml");
		if (ctx == null) {
			throw new Exception("配置文件错误");
		}
		
		//保持Session在TestCase开始时打开状态
		sessionFactory=getSessionFactory();
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
	    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session)); 
	}

	@Override
	protected void tearDown() throws Exception {
		//TestCase执行玩之后再关闭Session
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory); 
	    SessionFactoryUtils.closeSession(sessionHolder.getSession()); 
	    
		ctx = null;
		super.tearDown();
	}
	
	/**
	 * 取得当前spring中配置的sessionFactory
	 * @return
	 */
	private SessionFactory getSessionFactory() 
	  { 
	    if (sessionFactory == null) 
	    {
	      sessionFactory = (SessionFactory) ctx.getBean(SESSION_FACTORY_NAME); 
	    } 
	    return sessionFactory; 
	  } 
	
	/**
	 * 清空Session,同步数据库
	 */
	protected void flushSession(){
		this.getSessionFactory().getCurrentSession().flush();
	}

}
