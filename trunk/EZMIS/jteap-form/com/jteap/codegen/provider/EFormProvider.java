package com.jteap.codegen.provider;

import java.util.ArrayList;
import java.util.List;

import com.jteap.codegen.IModel;
import com.jteap.codegen.IModelProvider;
import com.jteap.codegen.model.Column;
import com.jteap.codegen.model.EFormModel;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;



/**
 * 
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public class EFormProvider implements IModelProvider {
	private String eformSn;
	private String suggestClassName;
	private boolean isTree;

	
	public String getEformSn() {
		return eformSn;
	}
	public void setEformSn(String eformSn) {
		this.eformSn = eformSn;
	}
	public String getSuggestClassName() {
		return suggestClassName;
	}
	public void setSuggestClassName(String suggestClassName) {
		this.suggestClassName = suggestClassName;
	}
	public boolean isTree() {
		return isTree;
	}
	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}
	public EFormProvider(String eformSn,String className,boolean isTree) {
		super();
		this.eformSn = eformSn;
		this.suggestClassName = className;
		this.isTree = isTree;
	}
	public IModel getModel() throws Exception {
		EFormModel eformModel = new EFormModel();
		EFormManager eformManager = (EFormManager) SpringContextUtil.getBean("eformManager");
		EForm eform = eformManager.findUniqueBy("sn", this.eformSn);
		eformModel.setEform(eform);
		eformModel.setClassName(this.suggestClassName);
		eformModel.setIsTree(this.isTree);
		DefTableInfo defTableInfo = eform.getDefTable();
		List columns = new ArrayList();
		for (DefColumnInfo defColumnInfo : defTableInfo.getColumns()) {
			String sqlType = defColumnInfo.getColumntype();
			columns.add(new Column(eformModel,0,sqlType,defColumnInfo.getColumncode(),defColumnInfo.getColumnlength(),0,defColumnInfo.isPk(),false,defColumnInfo.getAllownull(),false,false,"",""));
			
		}
		return eformModel;
	}
}
