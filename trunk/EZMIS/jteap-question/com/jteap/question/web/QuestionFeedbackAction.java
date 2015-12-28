package com.jteap.question.web;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.question.manager.QuestionFeedbackManager;
import com.jteap.question.model.QuestionFeedback;

@SuppressWarnings("serial")
public class QuestionFeedbackAction extends AbstractAction {
	
	private QuestionFeedbackManager questionFeedbackManager;

	public QuestionFeedbackManager getQuestionFeedbackManager() {
		return questionFeedbackManager;
	}

	public void setQuestionFeedbackManager(
			QuestionFeedbackManager questionFeedbackManager) {
		this.questionFeedbackManager = questionFeedbackManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return questionFeedbackManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
				"id","createPerson","createDate","content","remark","time"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id","createPerson","createDate","content","remark"
			};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		HqlUtil.addOrder(hql, "createDate", "desc");
	}
	
	
	/**
	 * 保存或修改问题反馈信息
	 * @return
	 */
	public String saveOrUpdateAction(){
		try {
			QuestionFeedback questionFeedback = new QuestionFeedback();
			String id = request.getParameter("id");
//			questionFeedback = questionFeedbackManager.get(id);
			if(StringUtil.isNotEmpty(id)){
				questionFeedback.setId(id);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			questionFeedback.setCreatePerson(request.getParameter("createPerson"));
			questionFeedback.setCreateDate(dateFormat.parse(request.getParameter("createDate")));
			questionFeedback.setContent(request.getParameter("content"));
			questionFeedback.setRemark(request.getParameter("remark"));
			questionFeedbackManager.save(questionFeedback);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	


}
