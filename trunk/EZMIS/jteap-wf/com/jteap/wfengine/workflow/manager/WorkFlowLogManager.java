package com.jteap.wfengine.workflow.manager;

import java.util.Date;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wfengine.workflow.model.WorkFlowLog;

public class WorkFlowLogManager extends HibernateEntityDao<WorkFlowLog> {

	public void addFlowLog(Long pi_id, String taskName, String taskActor, String taskActorLoginName, String nextTaksActor,
										String nextTaksName, String taskResult, String remark){
		
		WorkFlowLog workFlowLog = new WorkFlowLog();
		workFlowLog.setPi_id(pi_id.toString());
		workFlowLog.setPorcessDate(new Date());
		workFlowLog.setTaskName(taskName);
		workFlowLog.setTaskActor(taskActor);
		workFlowLog.setTaskLoginName(taskActorLoginName);
		workFlowLog.setNextTaksActor(nextTaksActor);
		workFlowLog.setNextTaksName(nextTaksName);
		workFlowLog.setTaskResult(taskResult);
		workFlowLog.setRemark(remark);
		
		save(workFlowLog);
	}
}
