package com.jteap.form.eform.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@SuppressWarnings("unchecked")
public class EFormExpContext {
	
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
	public Map getMap(){
		return vars;
	}
}
