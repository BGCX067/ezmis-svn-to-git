package com.jteap.codegen;

import java.io.File;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.jteap.codegen.provider.EFormProvider;
import com.jteap.core.web.SpringContextUtil;



/**
 * JTEAP平台代码生成客户端
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public class JteapGenerator {
	
	protected ApplicationContext ctx;
	protected final static String SESSION_FACTORY_NAME = "sessionFactory";
	private SessionFactory sessionFactory = null;
	
	public JteapGenerator() throws Exception{
		setUp();
	}
	Generator g = new Generator();
	public void clean() throws IOException{
		g.clean();
	}
	
	public void setUp() throws Exception{
		ctx = new ClassPathXmlApplicationContext("**/*context*.xml");
		if (ctx == null) {
			throw new Exception("配置文件错误");
		}
		SpringContextUtil.setApplicationContext(ctx);
		
		//保持Session在TestCase开始时打开状态
		sessionFactory=getSessionFactory();
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
	    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session)); 
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
	
	/**
	 * 产生服务器端代码
	 * @param tableName			表名
	 * @param systemName		系统名称
	 * @param moduleName		模块名称
	 * @throws Exception
	 */
	public void generateServerSide(String eformSN,String systemName,String moduleName,String className,boolean isTree) throws Exception {
		g.templateRootDir = new File("code_template/server").getAbsoluteFile();
		g.modelProvider = new EFormProvider(eformSN,className,isTree);
		g.generateByModelProvider(systemName,moduleName);
	}
	

}
