package com.jteap.wz.tjrw.quartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jteap.core.quartz.TransactionalQuartzTask;
import com.jteap.core.utils.DateUtils;
import com.jteap.wz.tjrw.manager.TjrwManager;

public class AddTjrwJob extends TransactionalQuartzTask  {
	
	private TjrwManager tjrwManager;
	
	public void subTimingJob(){
 		
	}
	@Override
	protected void executeTransactional(JobExecutionContext ctx)
			throws JobExecutionException {
		Date curD=DateUtils.getPreDate(new Date());
		String curNian=DateUtils.getDate(curD, "yyyy");
		String curYue=DateUtils.getDate(curD, "MM");
		
		//库存统计
		//tjrwManager.addTjrw(curNian, curYue, "kctj");
		//收发资金统计
		tjrwManager.addTjrw(curNian, curYue,"25", "sfzj");
		//收发结存统计
		tjrwManager.addTjrw(curNian, curYue, "25","sfjc");
		////费用统计
		tjrwManager.addTjrw(curNian, curYue,"25", "fytj");
		
	}
	
	public TjrwManager getTjrwManager() {
		return tjrwManager;
	}
	public void setTjrwManager(TjrwManager tjrwManager) {
		this.tjrwManager = tjrwManager;
	}
}
