package com.jteap.yx.aqyxfx.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jteap.core.quartz.TransactionalQuartzTask;
import com.jteap.yx.aqyxfx.manager.BkhdlTjManager;
import com.jteap.yx.aqyxfx.manager.YyqkfxManager;
/**
 * 被考核电量统计定时任务
 * @author lvchao
 *
 */
public class TimingJob extends TransactionalQuartzTask {
	private BkhdlTjManager bkhdlTjManager;
	private YyqkfxManager yyqkfxManagerl;
	@Override
	protected void executeTransactional(JobExecutionContext ctx)
			throws JobExecutionException {
		// TODO Auto-generated method stub
//		Calendar cd = Calendar.getInstance();
//		int year = cd.get(Calendar.YEAR);
//		int month = cd.get(Calendar.MONTH);
//		String dctjq = "";
//		if(month<10){
//			dctjq=year+"-0"+month;
//		}else{
//			dctjq = year +"-"+month;
//		}
//		bkhdlTjManager.saveBkhdl(dctjq);
//		yyqkfxManagerl.saveYyqxfx();
	}
	public BkhdlTjManager getBkhdlTjManager() {
		return bkhdlTjManager;
	}
	public void setBkhdlTjManager(BkhdlTjManager bkhdlTjManager) {
		this.bkhdlTjManager = bkhdlTjManager;
	}
	public YyqkfxManager getYyqkfxManagerl() {
		return yyqkfxManagerl;
	}
	public void setYyqkfxManagerl(YyqkfxManager yyqkfxManagerl) {
		this.yyqkfxManagerl = yyqkfxManagerl;
	}

}
