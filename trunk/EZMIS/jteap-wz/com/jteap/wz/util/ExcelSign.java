package com.jteap.wz.util;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)   
public @interface ExcelSign {
	public String column();
	
	public String index() default "";
	
	public String script() default "";
}
