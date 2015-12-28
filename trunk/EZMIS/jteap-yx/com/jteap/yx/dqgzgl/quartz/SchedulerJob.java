/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.yx.dqgzgl.manager.DqgzHandleManager;
import com.jteap.yx.dqgzgl.model.DqgzHandle;
import com.jteap.yx.dqgzgl.model.DqgzSet;

/**
 * 根据定期工作设置的工作规律开始执行相应的工作任务
 * @author caihuiwen
 */
@SuppressWarnings("unchecked")
public class SchedulerJob implements Job {
	
	private static Log log = LogFactory.getLog(SchedulerJob.class);
	
	public SchedulerJob() {
		log.info("工作服务类初始化成功!");
	}
	
//	/**
//     * we do a different flushmode than in the codebase
//     * here
//     */
//    protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
//            Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//            session.setFlushMode(FlushMode.COMMIT);
//            return session;
//    }
//    
	/**
	 * Perform actual closing of the Hibernate Session,
	 * catching and logging any cleanup exceptions thrown.
	 * @param session the Hibernate Session to close (may be <code>null</code>)
	 * @see org.hibernate.Session#close()
	 */
	public void closeSession(Session session) {
		session.flush();
		if (session != null) {
			try {
				session.close();
//				session.disconnect();
			}catch (HibernateException ex) {
				
			}
			catch (Throwable ex) {
				
			}
		}
	}
	
	/**
	 * 初始化hibernate session并绑定到事务线程上,代码摘抄自HibernateInterceptor
	 */
	public Session openSession(){
		SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		boolean existingTransaction = (sessionHolder != null) && (sessionHolder.containsSession(session));
		if (existingTransaction) {
			log.debug("Found thread-bound Session for HibernateInterceptor");
		} else if (sessionHolder != null) {
			sessionHolder.addSession(session);
		} else {
			TransactionSynchronizationManager.bindResource(sessionFactory,new SessionHolder(session));
		}
		return session;
	}
	
	public void destorySession(){
		SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		boolean existingTransaction = (sessionHolder != null) && (sessionHolder.containsSession(session));
		if (existingTransaction) {
			log.debug("Not closing pre-bound Hibernate Session after HibernateInterceptor");
		} else {
			SessionFactoryUtils.closeSession(session);
			if ((sessionHolder == null) || (sessionHolder.doesNotHoldNonDefaultSession()))
				TransactionSynchronizationManager.unbindResource(sessionFactory);
		}
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DqgzHandleManager dqgzHandleManager = (DqgzHandleManager) SpringContextUtil.getBean("dqgzHandleManager");
		if(dqgzHandleManager != null){
			Session session = null;
			try {
				session = openSession();
				//获取工作规律
				String gzgl = context.getMergedJobDataMap().get("gzgl").toString();
				
				Query query = session.createQuery("from DqgzSet t where t.gzgl=?");
				query.setString(0, gzgl);
				List<DqgzSet> list = query.list();
				
				for (DqgzSet dqgzSet : list) {
					//根据班次【夜班,白班,中班】 循环发送工作设置
					String[] bcsArray = dqgzSet.getBc().split(",");
					for (int i = 0; i < bcsArray.length; i++) {
						DqgzHandle dqgzHandle = new DqgzHandle();
						dqgzHandle.setBc(bcsArray[i]);
						dqgzHandle.setDqgzSetId(dqgzSet.getId());
						dqgzHandle.setFzbm(dqgzSet.getFzbm());
						dqgzHandle.setFzgw(dqgzSet.getFzgw());
						dqgzHandle.setGzgl(dqgzSet.getGzgl());
						dqgzHandle.setDqgzzy(dqgzSet.getDqgzzy());
						dqgzHandle.setDqgzNr(dqgzSet.getDqgzNr());
						dqgzHandle.setDqgzCreateDt(new Date());
						dqgzHandle.setStatus("未完成");
						
						dqgzHandleManager.hanldSend_saveDeals(dqgzHandle);
					}
				}
				log.info("定期工作 ---> " + gzgl);
			}catch(Exception e) {
				//在web服务启动时,因为 Quartz表达式等待时间过短、 加载顺序原因 有些文件还没被加载,却在这里调用,所以抛出异常, 此为正常.
				e.printStackTrace();
			}finally{
				if(session != null){
					closeSession(session);
				}
			}
		}
	}

}