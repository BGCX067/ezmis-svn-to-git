package com.jteap.wz.tjrw.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jteap.core.quartz.TransactionalQuartzTask;
import com.jteap.wz.tjrw.manager.TjrwManager;

/**
 * 定时新建报表任务
 * @author lvchao
 *
 */
public class TimingJob extends TransactionalQuartzTask  {
	
	private TjrwManager tjrwManager;
	
	/**
	 * 调用tjjkManager里的监控方法
	 */
	public void subTimingJob(){
 		
	}
	@Override
	protected void executeTransactional(JobExecutionContext ctx)
			throws JobExecutionException {
		//System.out.println(new Date());
 		tjrwManager.tjTimingJob();
	}
	public TjrwManager getTjrwManager() {
		return tjrwManager;
	}
	public void setTjrwManager(TjrwManager tjrwManager) {
		this.tjrwManager = tjrwManager;
	}
}
