package com.jteap.dgt.fjdgztj.web;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.HdjyManager;
import com.jteap.dgt.fjdgztj.manager.TzzlManager;
import com.jteap.dgt.fjdgztj.model.Hdjy;
import com.jteap.dgt.fjdgztj.model.Tzzl;
import com.jteap.form.eform.manager.EFormFjManager;
import com.jteap.form.eform.model.EFormFj;
import com.jteap.system.doclib.manager.DoclibAttachManager;
import com.jteap.system.doclib.manager.DoclibCatalogManager;
import com.jteap.system.doclib.manager.DoclibManager;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.opensymphony.xwork2.Action;

/**
 * 工会处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class GhAction extends AbstractAction{
	//活动剪影处理类
	private HdjyManager hdjyManager;
	//通知专栏处理类
	private TzzlManager tzzlManager;
	//表单处理类
	private EFormFjManager eformFjManager;
	//文档相关处理类
	private DoclibManager doclibManager;
	private DoclibCatalogManager doclibCatalogManager;
	private DoclibAttachManager doclibAttachManager;
	private DoclibCatalog doclibCatalog;
	private Doclib doclib;
	private ByteArrayInputStream input;
	
	
	private List list;
	//文档集合
	private List doclist;
	//模块标记
	private String catalogCode;
	private String id;
	/**
	 * 根据模块标记 返回该模块的所有文档列表
	 * @return
	 */
	public String getDoclibCatalogTop5(){
		int count = 0;
		if("ghwj".equals(catalogCode)||"dsdt".equals(catalogCode)){
			count=10;
		}else{
			count=6;
		}
		list=doclibManager.findDoclibListByCatalogId(catalogCode,count);
		return "top5List";
	}
	/**
	 *返回活动剪影的对象信息集合
	 */
	public String getGhObjectsAction(){
		list =hdjyManager.getAll();
		String hql = "from Tzzl t order by t.scsj desc";
		
		Page page = tzzlManager.pagedQueryWithStartIndex(hql.toString(),
				0, 14,isUseQueryCache,showListHqlValues.toArray());
		Collection obj = (Collection) page.getResult();
		List<Tzzl> tzzlList= (List)obj;
		List ghjjList = doclibManager.find("from Doclib t where t.doclibCatalog.catalogCode='ghjj' order by t.createdt desc");
		Doclib ghjj = null;
		if(ghjjList.size()>0){
			ghjj = (Doclib)ghjjList.get(0);
		}
		
		//ServletActionContext.getRequest().setAttribute("list",list);
	 
		ServletActionContext.getRequest().setAttribute("ghjj", ghjj);
		ServletActionContext.getRequest().setAttribute("tzzlList", tzzlList);
		return "index";
	}
	/**
	 * 根据id返回单个通知专栏
	 * @return
	 */
	public String getTzzlObject(){
		//根据id取得剪影对象
		Tzzl tz  = tzzlManager.get(id);
		ServletActionContext.getRequest().setAttribute("Doclib", tz);
		return "content";
	}
	/**
	 * 根据id返回单个活动剪影
	 * @return
	 */
	public String getHdjyObject(){
		//根据id取得剪影对象
		Hdjy hd  = hdjyManager.get(id);
		list = doclibAttachManager.getAttachByDoclib(id);
		//根据id取得剪影附件集合
		String hql = "from EFormFj as fj where fj.docid = ?";
		list = eformFjManager.find(hql,id);
		//传入页面
		ServletActionContext.getRequest().setAttribute("model","hdjy");
		ServletActionContext.getRequest().setAttribute("Doclib", hd);
		return "content";
	}
	/**
	 * 返回活动剪影标题图片
	 */
	public String getHdImg(){
		String hql ="from EFormFj fj where fj.id=?";
		List list =eformFjManager.find(hql,id.subSequence(3, id.length()));
		if(list.size()>0){
			//附件表根据Id查询标题图片
			EFormFj eformfj = (EFormFj)list .get(0);
			this.setInput(new ByteArrayInputStream(eformfj.getContent()));
		} 
		return Action.SUCCESS;
	}
	/**
	 * 返回最新工会简介
	 * @return
	 */
//	public String getGhjj(){
//		//查询所有公会简介文档
//		list = doclibManager.findDoclibListByCatalogId(catalogCode,0);
//		//返回第一个最新的文档
//		Doclib doc = (Doclib) list.get(0);
//		id = doc.getId();
//		//取得文档内容
//		doclib = doclibManager.findDoclibById(id);
//		//取得文档附件信息
//		list = doclibAttachManager.getAttachByDoclib(id);
//		ServletActionContext.getRequest().setAttribute("list", list);
//		return "ghjj";
//		
//	}
	/**
	 * 根据文档ID返回该文档内容
	 * @return
	 */
	public String getDoclibContent(){
		list = doclibAttachManager.getAttachByDoclib(id);
		doclib = doclibManager.findDoclibById(id);
		return "content";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DoclibManager getDoclibManager() {
		return doclibManager;
	}

	public void setDoclibManager(DoclibManager doclibManager) {
		this.doclibManager = doclibManager;
	}

	public DoclibCatalogManager getDoclibCatalogManager() {
		return doclibCatalogManager;
	}

	public void setDoclibCatalogManager(DoclibCatalogManager doclibCatalogManager) {
		this.doclibCatalogManager = doclibCatalogManager;
	}

	public Doclib getDoclib() {
		return doclib;
	}

	public void setDoclib(Doclib doclib) {
		this.doclib = doclib;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public DoclibCatalog getDoclibCatalog() {
		return doclibCatalog;
	}

	public void setDoclibCatalog(DoclibCatalog doclibCatalog) {
		this.doclibCatalog = doclibCatalog;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public DoclibAttachManager getDoclibAttachManager() {
		return doclibAttachManager;
	}

	public void setDoclibAttachManager(DoclibAttachManager doclibAttachManager) {
		this.doclibAttachManager = doclibAttachManager;
	}
	public List getDoclist() {
		return doclist;
	}
	public void setDoclist(List doclist) {
		this.doclist = doclist;
	}
	public void setHdjyManager(HdjyManager hdjyManager) {
		this.hdjyManager = hdjyManager;
	}
	public void setEformFjManager(EFormFjManager eformFjManager) {
		this.eformFjManager = eformFjManager;
	}
	public ByteArrayInputStream getInput() {
		return input;
	}
	public void setInput(ByteArrayInputStream input) {
		this.input = input;
	}
	public void setTzzlManager(TzzlManager tzzlManager) {
		this.tzzlManager = tzzlManager;
	}
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
