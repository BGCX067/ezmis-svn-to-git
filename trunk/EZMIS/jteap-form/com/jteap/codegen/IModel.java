package com.jteap.codegen;

import java.util.Map;

/**
 * 
 * template数据模型
 * 
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public interface IModel {

	/**
	 * model描述
	 * 
	 * @return
	 */
	public String getDisplayDescription();

	/**
	 * 为模板路径提供数据
	 * 
	 * @param fileModel
	 * @throws Exception
	 */
	
	public void mergeFilePathModel(Map fileModel) throws Exception;

	/**
	 * 数据模型描述
	 * 
	 * @return
	 */
	public String getTemplateModelName();

	public String getSystemName();
	
	public String getModuleName();
	
	public void setSystemName(String systemName);
	
	public void setModuleName(String moduleName);
}
