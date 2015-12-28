package com.jteap.jhtj.sjxxxdy.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.sjxxxdy.model.ExcelModel;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.system.dict.model.Dict;
@SuppressWarnings({ "unchecked", "serial" })
public class ImportExcelManager extends HibernateEntityDao {
	private TjInfoItemManager tjInfoItemManager;
	/**
	 * 功能说明:excel数据项导入
	 * @author 童贝
	 * @version Sep 5, 2009
	 * @param fileName
	 */
	public  boolean importExcel(ExcelModel model){
		try{
			InputStream myxls = new FileInputStream(model.getFile());
			Workbook  wb=new HSSFWorkbook(myxls);
			//int sheetNum=wb.getNumberOfSheets();   
	        //for(int i=2;i<sheetNum;i++)   
	        //{   
	        	//得到sheet
	        	Sheet rs = wb.getSheetAt(model.getSheetNum()-1);
	        	Iterator<Row> it =rs.rowIterator();
	        	int flag=0;
	        	int icorder=1;
	        	while(it.hasNext()){
	        		Row row=it.next();
	        		if(null!=row){
	        			if(flag>=(model.getRow()-1)){
	        				TjInfoItem item=new TjInfoItem();
	        				item.setKid(model.getKid());
	        				
	        				Cell cname=row.getCell((short)(model.getCname()-1));
		        			String str1=getCellValue(cname).toString().trim();
		        			item.setIname(str1);
		        			
		        			Cell name= row.getCell((short)(model.getName()-1));
		        			String str2=getCellValue(name).toString().trim();
		        			if(str2==null||str2.equals("")){
		        				break;
		        			}
		        			item.setItem(str2);
		        			
		        			//先验证导入的excel是否已经导过
	        				boolean isValidate=this.validateId(model.getKid(),str2);
		        			if(isValidate){
			        			Cell dw=row.getCell((short)(model.getDw()-1));
			        			String str3=getCellValue(dw).toString().trim();
			        			item.setDw(str3);
			        			
			        			//Cell type=row.getCell((short)4);
			        			//String str4=getCellValue(type).toString();
			        			item.setItype("FLOAT");
			        			
			        			//Cell xsws=row.getCell((short)6);
			        			//Double str5=(Double)getCellValue(xsws);
			        			item.setXsw(2l);
			        			
			        			Cell fz=row.getCell((short)(model.getSjfz()-1));
			        			String str6=getCellValue(fz).toString().trim();
			        			str6=this.getSjlxByIname(str6);
			        			item.setSjlx(str6);
			        			
		        				//计算公式
		        				Cell hcexp=row.getCell((short)(model.getCexp()-1));
		        				String scexp=getCellValue(hcexp).toString().trim();
		        				//计算顺序
		        				Cell hcorder=row.getCell((short)(model.getCorder()-1));
		        				String scorder=getCellValue(hcorder).toString().trim();
		        				//公式不为空的话就设置公式
		        				if(scexp!=null&&!scexp.equals("")&&scorder!=null&&!scorder.equals("")){
		        					item.setQsfs(3l);
		        					item.setCexp(scexp);
		        					item.setCorder(Long.parseLong(scorder.substring(0,scorder.indexOf("."))));
		        				}else{
		        					item.setQsfs(1l);
		        					item.setCorder(0l);
		        				}

			        			//item.setSid(0l);
			        			//item.setJsfs(0l);
			        			//item.setDfunId(0l);
			        			item.setIorder(new Long(icorder));
			        			//item.setIskh(0l);
			        			item.setIsvisible(1l);
			        			
			        			this.save(item);
			        			this.getHibernateTemplate().flush();
			        			
			        			System.out.println(str1);
			        			System.out.println(str2);
			        			System.out.println(str3);
			        			//System.out.println(str4);
			        			//System.out.println(str5);
			        			System.out.println(str6);
			        			icorder++;
		        			}
	        			}
	        			//Iterator<Cell> it1=row.cellIterator();
	        			//while(it1.hasNext()){
	        				//Cell cell=it1.next();
	        				//System.out.println("第"+(row.getRowNum()+1)+"行,第"+(cell.getCellNum()+1)+"列"+cell.getStringCellValue());
	        			//}
	        		}
	        		flag++;
	        	}
	        	
	        //}
		}catch(Exception e){
			e.printStackTrace();
		}
		this.tjInfoItemManager.createTable(model.getKid());
		return true;
	}
	
	
	public  Object getCellValue(Cell obj){
		if(obj==null){
			return "";
		}
		int type=obj.getCellType();
		if(type==0){
			return obj.getNumericCellValue();
		}else if(type==1){
			return obj.getStringCellValue();
		}else if(type==2){
			return obj.getCellFormula();
		}else if(type==3){
			return "";
		}else if(type==4){
			return obj.getBooleanCellValue();
		}
		return "";
	}
	
	
	/**
	 * 功能说明:验证item是否重复
	 * @author 童贝
	 * @version Apr 24, 2009
	 * @param kid
	 * @param item
	 * @return
	 */
	public boolean  validateId(String kid ,String item){
		int len=item.length();
		int trimLen=item.trim().length();
		//避免有空格的情况
		if(len==trimLen){
			String hql1="from TjInfoItem info where info.kid='"+kid+"' and info.item='"+item+"'";
			List<TjInfoItem> list1=this.find(hql1);
			if(list1.size()>0){
				return false;
			}
			String hql2="from TjItemKindKey key where key.kid='"+kid+"' and key.icode='"+item+"'";
			List<TjInfoItem> list2=this.find(hql2);
			if(list2.size()>0){
				return false;
			}
			return true;
		}
		return false;
	}
	
	
	public String getSjlxByIname(String iname){
		String hql="from Dict where key='"+iname+"'";
		List<Dict> dictList= this.find(hql);
		if(dictList.size()>0){
			Dict dict=dictList.get(0);
			return dict.getValue();
		}
		return "A1";
	}


	public TjInfoItemManager getTjInfoItemManager() {
		return tjInfoItemManager;
	}


	public void setTjInfoItemManager(TjInfoItemManager tjInfoItemManager) {
		this.tjInfoItemManager = tjInfoItemManager;
	}
}
