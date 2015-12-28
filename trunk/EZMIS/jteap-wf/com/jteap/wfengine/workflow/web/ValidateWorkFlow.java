package com.jteap.wfengine.workflow.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jteap.core.utils.JSONUtil;
import com.jteap.wfengine.workflow.util.WFConstants;

public class ValidateWorkFlow {
	
	public static String VALIDATE_SUCCESS = "验证成功";
		
	public static String VALIDATE_FAILED_SINGLE = "验证失败，原因：流程未闭合";
	
	public static String VALIDATE_FAILED_STARTEND = "验证失败，原因：开始或者结束结点无效";
	
	public static String VALIDATE_FAILED_NOTCLOSE = "验证失败，原因：存在没有封闭的结点";
	
	public static String VALIDATE_FAILED_FORK = "验证失败，原因：环节分支没有名称或者默认路由错误";
	
	public static String VALIDATE_FAILED_FORKJOIN = "验证失败，原因：会签连线没有名称或者有包含条件或者默认路由为真";
	
	public static String VALIDATE_FAILED_TASKPOWER = "验证失败，原因：每个环节必须指定处理人";

	public static String VALIDATE_FAILED_SAMENAME = "验证失败，原因：存在相同的环节名称";
	
	Document document = null;

	Element mxGraphModel = null;

	Element root = null;

	//开始结点
	Element start = null;
	//结束结点
	Element end = null;
	//所有连线
	List<Element> edgelist = new ArrayList<Element>();
	//所有环节
	List<Element> tasklist = new ArrayList<Element>();
	//所有分支
	List<Element> forklist = new ArrayList<Element>();
	//所有汇集
	List<Element> joinlist = new ArrayList<Element>();
	
	@SuppressWarnings("unchecked")
	public ValidateWorkFlow(String xml) {
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		mxGraphModel = document.getRootElement();
		root = mxGraphModel.element("root");

		List list = root.elements();
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if (e.getName().equals("start-state")) {
				start = e;
			}
			if (e.getName().equals("end-state")) {
				end = e;
			}
			if (e.getName().equals("Edge")) {
				edgelist.add(e);
			}
			if (e.getName().equals("task")) {
				tasklist.add(e);
			}
			if (e.getName().equals("fork")) {
				forklist.add(e);
			}
			if (e.getName().equals("join")) {
				joinlist.add(e);
			}
		}
	}

	/**
	 * 验证工作流
	 * @author 肖平松
	 * @version Sep 2, 2009
	 * @return
	 */
	public String validate() {
		String msg = ValidateWorkFlow.VALIDATE_SUCCESS;
		if(!this.validateSameName()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_SAMENAME;
		}
		else if (!this.validateSingle()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_SINGLE;
		}
		else if(!this.validateStartAndEnd()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_STARTEND;
		}
		else if(!this.validateNotClose()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_NOTCLOSE;
		}
		else if(!this.validateTaskPower()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_TASKPOWER;
		}
		else if(!this.validateFork()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_FORK;
		}
		else if(!this.validateForkJoin()){
			msg = ValidateWorkFlow.VALIDATE_FAILED_FORKJOIN;
		}
		return msg;
	}

	private boolean validateSameName() {
		for(int i = 0;i < tasklist.size();i++){
			Element task = tasklist.get(i);
			String name = task.attributeValue("name");
			if(name.equals(WFConstants.WF_STARTSTATE) || name.equals(WFConstants.WF_ENDSTATE))
				return false;
			for(int j = i+1;j < tasklist.size();j++){
				Element newTask = tasklist.get(j);
				String newName = newTask.attributeValue("name");
				if(name.equals(newName)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 验证环节，必须有处理人
	 * @author 肖平松
	 * @version Sep 3, 2009
	 * @return
	 */
	private Boolean validateTaskPower(){
		for(int i = 0;i < tasklist.size();i++){
			Element task = tasklist.get(i);
			if(task.attributeValue("taskPower") == null || 
				task.attributeValue("taskPower").equals("[]") ||
				task.attributeValue("taskPower").equals("")){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证分支汇集
	 * @author 肖平松
	 * @version Sep 3, 2009
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean validateForkJoin(){
		//如果会签的数量和汇集的数量不一致,则验证失败
		if(forklist.size() != joinlist.size()){
			return false;
		}
		for(int i = 0;i < forklist.size();i++){
			Element fork = forklist.get(i);
			for(int j = 0;j < edgelist.size();j++){
				Element edge = edgelist.get(j);
				Element mxcell = edge.element("mxCell");
				if(mxcell.attributeValue("source").equals(fork.attributeValue("id"))){
					if(edge.attributeValue("label") == null || edge.attributeValue("label").equals("")){
						return false;
					}
					String router = edge.attributeValue("router");
					//如果路由不为空
					if(router != null && !router.equals("")){
						HashMap routerMap = (HashMap) JSONUtil.parseObject(router);
						//如果默认路由为真
						if(routerMap.get("is_default_route") != null && routerMap.get("is_default_route").equals(true)){
							return false;
						}
						//如果有条件
						if(routerMap.get("condition") != null && !routerMap.get("condition").equals("")){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 验证环节分支，必须要有一个默认路由
	 * @author 肖平松
	 * @version Sep 2, 2009
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	private Boolean validateFork(){
		for(int i = 0;i < tasklist.size();i++){
			Element task = tasklist.get(i);
			String id = task.attributeValue("id");
			int forknum = 0;
			List tempedgelist = new ArrayList();
			for(int j = 0;j < edgelist.size();j++){
				Element edge = edgelist.get(j);
				Element mxcell = edge.element("mxCell");
				if(mxcell.attributeValue("source").equals(id)){
					forknum++;
					tempedgelist.add(edge);
				}
			}
			int trueNumber = 0;
			if(forknum >= 2){
				for(int k = 0;k < tempedgelist.size();k++){
					Element e = (Element) tempedgelist.get(k);
					if(e.attributeValue("router") == null)	return false;
					String router = e.attributeValue("router");
					if(router.equals(""))	return false;
					HashMap routerMap = (HashMap) com.jteap.core.utils.JSONUtil.parseObject(router);
					//每条线必须要有名称
					if(routerMap.get("name") == null || routerMap.get("name").equals("")){
						return false;
					}
					if(routerMap.get("is_default_route").equals(true)){
						trueNumber++;
					}
				}
				//必须要有一个默认路由
//				if(trueNumber == 0 || trueNumber >=2){
//					return false;
//				}
			}
		}
		return true;
	}
	
	/**
	 * 验证结点是否封闭
	 * @author 肖平松
	 * @version Sep 2, 2009
	 * @return
	 */
	private Boolean validateNotClose(){
		int sz = tasklist.size();
		int temp = 0;
		for (int j = 0; j < sz; j++) {
			Element et = tasklist.get(j);
			Boolean in = false;
			Boolean out = false;
			for (int i = 0; i < edgelist.size(); i++) {
				Element e = edgelist.get(i);
				Element mxcell = e.element("mxCell");

				String s = mxcell.attributeValue("source");
				String t = mxcell.attributeValue("target");
				
				if (et.attributeValue("id").equals(s)) {
					in = true;
				}
				else if(et.attributeValue("id").equals(t)){
					out = true;
				}
			}
			if(in == true && out == true){
				temp++;
			}
		}
		if(sz != temp)	return false;
		return true;
	}

	/**
	 * 验证是否有单独的结点或者线
	 * @author 肖平松
	 * @version Sep 2, 2009
	 * @return
	 */
	private Boolean validateSingle() {

		// 验证是否有孤立的线
		for (int i = 0; i < edgelist.size(); i++) {
			Element e = edgelist.get(i);
			Element mxcell = e.element("mxCell");
			if (mxcell.attributeValue("source") == null
					|| mxcell.attributeValue("target") == null) {
				return false;
			}
		}

		
		// 验证是否有孤立的结点
		int sz = tasklist.size();
		int temp = 0;
		for (int j = 0; j < sz; j++) {
			Element et = tasklist.get(j);
			for (int i = 0; i < edgelist.size(); i++) {
				Element e = edgelist.get(i);
				Element mxcell = e.element("mxCell");

				String s = mxcell.attributeValue("source");
				String t = mxcell.attributeValue("target");
				if (et.attributeValue("id").equals(s) || et.attributeValue("id").equals(t)) {
					temp++;
					break;
				}
			}
		}
		if(sz != temp)	return false;
		return true;
	}

	/**
	 * 验证开始结点和末尾结点是否存在，是否有多条线,是否孤立
	 * 
	 * @author 肖平松
	 * @version Sep 2, 2009
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean validateStartAndEnd() {

		if (start == null || end == null)
			return false;

		String startid = start.attributeValue("id");
		String endid = end.attributeValue("id");
		int startline = 0;
		int endline = 0;
		for (int i = 0; i < edgelist.size(); i++) {
			Element e = edgelist.get(i);
			Element mxcell = e.element("mxCell");
			if(mxcell.attributeValue("source") == null || mxcell.attributeValue("target") == null){
				continue;
			}
			if (mxcell.attributeValue("source").equals(startid)) {
				startline++;
			}
			if (mxcell.attributeValue("target").equals(endid)) {
				endline++;
			}
		}
		if (startline != 1 || endline == 0)
			return false;

		return true;
	}

}
