package com.jteap.wz.gqjtz.util;

import com.jteap.core.utils.StringUtil;
import com.jteap.form.eform.util.CalculateFormula;
import com.jteap.form.eform.util.EFormExpContext;

public class CalculateZYF implements CalculateFormula {

	public String calculate(EFormExpContext context) {
		String zyf = (String) context.getVar("ZYF");
		if(StringUtil.isEmpty(zyf)) zyf="1";  //默认在用 
		return zyf;
	}

}
