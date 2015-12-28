package com.jteap.jx.qxgl.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jteap.core.quartz.TransactionalQuartzTask;
import com.jteap.jx.qxgl.manager.QxtjManager;
/**
 * 缺陷统计 自动统计功能
 * @author lvchao
 *
 */
public class TimingJob extends TransactionalQuartzTask{
		
	private QxtjManager qxtjManager;

	@Override
	protected void executeTransactional(JobExecutionContext ctx)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		qxtjManager.saveQxtj("cn");
		qxtjManager.saveQxtj("xq");
		try {
			qxtjManager.exportExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public QxtjManager getQxtjManager() {
		return qxtjManager;
	}
	public void setQxtjManager(QxtjManager qxtjManager) {
		this.qxtjManager = qxtjManager;
	}
}
