package com.jteap.form.cform.util;


/**
 * 计算公式接口
 * @author tanchang
 *
 */
public interface CalculateFormula {
	//当前计算域的值
	public static final String PARAM_FIELDVALUE = "value";
	
	//当前计算域的字段名
	public static final String PARAM_FIELDNAME = "fd";
	
	//当前记录的串行化字符串,可从此处取得当前记录的其他值
	public static final String PARAM_RECORD_JSON = "recordJson";
	
	public String calculate(CFormExpContext context);
	
}
