/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.quartz;

import java.text.ParseException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 定期工作调度程序
 * @author caihuiwen
 */
public class DqgzScheduler {

	private static Log log = LogFactory.getLog(DqgzScheduler.class);
	
	private DictManager dictManager;
	private Scheduler scheduler;
	private boolean initComplete = false; // 是否已经初始化任务
	
	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public boolean getInitComplete() {
		return initComplete;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	/**
	 * 缺省构造函数需要初始化quartz
	 */
	public DqgzScheduler() {
		log.info("开始初始化定期工作!");
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			scheduler = sf.getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		log.info("初始化定期工作完成!");
	}

	/**
	 * 初始化定期工作的相关触发器和scheduler绑定
	 */
	public void startSchedulerJob() {
		
		if (!getInitComplete()) {

			log.info("开始初始化定期工作规律!");

			Trigger trigger = null;
			Collection<Dict> dicts = dictManager.findDictByUniqueCatalogName("dqgzgl");
			
			JobDetail jobDetail = new JobDetail("SchedulerJob", "run_manage",SchedulerJob.class);

			int i = 0;
			for (Dict dict : dicts) {
				//获取工作规律
				String gzgl = dict.getValue(); 
				try{
					trigger = new CronTrigger("trigger" + i, "triggerGroup",gzgl);
					trigger.setJobName(jobDetail.getName());
					trigger.setJobGroup(jobDetail.getGroup());
					trigger.getJobDataMap().put("gzgl", gzgl);
					
					if (i == 0) {
						scheduler.scheduleJob(jobDetail, trigger);
					} else {
						scheduler.scheduleJob(trigger);
					}
				}catch (ParseException e) {
					log.error("定期工作数据字典工作规律配置错误,初始化定期工作失败,请重启服务器!");
					return;
				} catch (SchedulerException e) {
					log.error("绑定定期工作触发器错误,初始化定期工作失败,请重启服务器!");
					return;
				}
				i++;
			}
			
			try {
				scheduler.start();
			} catch (SchedulerException e) {
				log.error(",初始化定期工作失败,请重启服务器!");
				return;
			}
			
			this.initComplete = true;
			log.info("初始化定期工作规律完成!");
		}
	}

	/**
	 * 关闭定期工作的任务
	 */
	public void shutdownSchedulerJob() {
		log.info("停止定期工作规律开始!");
		try {
			scheduler.shutdown();			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		log.info("停止定期工作规律完成!");
	}

}
