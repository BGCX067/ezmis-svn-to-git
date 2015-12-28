package com.jteap.form.cform.tmp;

import com.jteap.core.utils.StringUtil;
import com.jteap.form.cform.util.CFormExpContext;
import com.jteap.form.cform.util.CalculateFormula;


/**
 * 计算公式接口
 * @author tanchang
 *
 */
public class TestCalculateFormula implements CalculateFormula {

	public String calculate(CFormExpContext context) {
		String value = (String) context.getVar("DEPTNAME");
		return StringUtil.dealNull(value);
	}
	
	
}
