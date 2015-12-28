package com.jteap.codegen.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.jteap.codegen.IModel;
import com.jteap.form.eform.model.EForm;
@SuppressWarnings("unchecked")
public class EFormModel implements IModel {

	private EForm eform;
	private String system;
	private String module;
	private String className;
	private boolean isTree;
	
	private List columns;
	
	

	public List getColumns() {
		return columns;
	}

	public void setColumns(List columns) {
		this.columns = columns;
	}

	public boolean getIsTree() {
		return isTree;
	}

	public void setIsTree(boolean isTree) {
		this.isTree = isTree;
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public EForm getEform() {
		return eform;
	}

	public void setEform(EForm eform) {
		this.eform = eform;
	}

	public String getDisplayDescription() {
		return eform.getSn();
	}

	public String getModuleName() {
		return module;
	}

	public String getSystemName() {
		return system;
	}

	public String getTemplateModelName() {
		return "eform";
	}

	@SuppressWarnings("unchecked")
	public void mergeFilePathModel(Map fileModel) throws Exception {
		fileModel.putAll(BeanUtils.describe(this));
		fileModel.put("system", this.getSystemName());
		fileModel.put("module", this.getModuleName());
	}

	public void setModuleName(String moduleName) {
		this.module = moduleName;
		
	}

	public void setSystemName(String systemName) {
		this.system = systemName;
		
	}

}
