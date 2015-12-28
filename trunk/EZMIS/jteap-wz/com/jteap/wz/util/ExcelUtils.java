package com.jteap.wz.util;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * 通过注解方式填充数据并取得Excel对象
 * */
public class ExcelUtils {
	/**
	 * 通过配置的注解填充表格和数据
	 * */
	@SuppressWarnings("deprecation")
	public static HSSFSheet fillSheet(HSSFSheet sheet,List<?> list,Class<?> cl){
		try {
			Velocity.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Field[] fields = cl.getDeclaredFields();
		//通过注解配置出Excel的列
		List<Field> fieldList = new ArrayList<Field>();
		short count=0;
		 for(int i=0;i<fields.length;i++){
			 ExcelSign sign = fields[i].getAnnotation(ExcelSign.class);
			 if(sign!=null&&StringUtils.isNotEmpty(sign.column())){				 
				 HSSFRow headRow = sheet.createRow(0);
				 HSSFCell headCel= headRow.createCell(count++);
//				 headCel.setEncoding(HSSFCell.ENCODING_UTF_16);
				 headCel.setCellValue(sign.column());
				 fieldList.add(fields[i]);
			 }
		 }
		 //向该列填充数据
		 int i=1;int j=0;
		 for(Object obj:list){
			 HSSFRow row = sheet.createRow(i++);
			 j=-1;
			 VelocityContext context = new VelocityContext();
			 for(Field field:fieldList){
				 j++;
				 String value = null;
				 try{
					 ExcelSign sign = field.getAnnotation(ExcelSign.class);
					 if(sign!=null&&StringUtils.isEmpty(sign.script())){
						HSSFCell cel= row.createCell(j);
//						cel.setEncoding(HSSFCell.ENCODING_UTF_16);
						if(StringUtils.isNotEmpty(sign.index())){
							value = BeanUtils.getProperty(obj, sign.index());
						}else
							value = BeanUtils.getProperty(obj, field.getName());
						cel.setCellValue(value);
					 }
					 context.put(field.getName(), value==null?"":value);					
				 }catch(Exception e){continue;}
			 }
			 j=0;
			 for(Field field:fieldList){				
				try {
					j++;
					ExcelSign sign = field.getAnnotation(ExcelSign.class);
					if(sign!=null&&StringUtils.isNotEmpty(sign.script())){
						HSSFCell cel= row.createCell(j);
//						cel.setEncoding(HSSFCell.ENCODING_UTF_16);
						StringWriter writer = new StringWriter();
						Velocity.evaluate(context, writer, null,sign.script());
						//context.put(field.getName(), writer.toString());
						cel.setCellValue(writer.toString());
						writer.flush();
					}				
				} catch (Exception e1) {
				//	e1.printStackTrace();
					continue;
				}
			 }
		 }
		 return sheet;
	}
}