package com.jteap.form.cform.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@SuppressWarnings("unchecked")
public class CFormExpContext {
	
	private Map vars = new HashMap();
	public void setVar(String var,Object value){
		vars.put(var, value);
	}
	
	public Object getVar(String var){
		return vars.get(var);
	}
	
	public Set getVars(){
		return vars.keySet();
	}
}
