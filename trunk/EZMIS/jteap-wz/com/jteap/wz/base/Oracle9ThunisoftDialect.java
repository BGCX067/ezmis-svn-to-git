package com.jteap.wz.base;

import org.hibernate.dialect.Oracle9iDialect;



public class Oracle9ThunisoftDialect extends Oracle9iDialect {
	  public Oracle9ThunisoftDialect() 
	  {  
	    super(); 
	  }  
	  
	  public boolean supportsLimit()
	  {  
	    return false;  
	  } 
}
