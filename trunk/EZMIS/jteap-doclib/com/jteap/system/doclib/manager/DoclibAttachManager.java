package com.jteap.system.doclib.manager;

import java.util.List;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.doclib.model.DoclibAttach;

/**
 * 文档附件管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked" })
public class DoclibAttachManager extends HibernateEntityDao<DoclibAttach> {

	/**
	 * 根据文档ID获得文档附件信息（不取文档附件内容）
	 */
	public List<DoclibAttach> getAttachByDoclib(String doclibId) {
		StringBuffer hqlSb = new StringBuffer();
		hqlSb
				.append("select new com.jteap.system.doclib.model.DoclibAttach(id,name,type,doclibSize) ");
		hqlSb.append("from DoclibAttach where DOCLIB_ID=?");

		List<DoclibAttach> list = this.find(hqlSb.toString(), doclibId);
		return list;
	}
	
	/**
	 * 
	 *描述：得到附件列表
	 *时间：2010-6-22
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getAttachTableBySet(String path,Set<DoclibAttach> fields){
		StringBuffer result=new StringBuffer();
		result.append("<table>");
		result.append("<tr><td>附件名</td><td>附件类型</td><td>附件大小</td><td>其他</td></tr>");
		for(DoclibAttach attach:fields){
			result.append("<tr><td>"+attach.getName()+"</td><td>"+this.getAttachType(attach.getType().substring(attach.getType().indexOf(".")+1))+"</td><td>"+attach.getDoclibSize()+"</td><td><a href='"+path+"?attachid="+attach.getId()+"'>下载</a></td></tr>");
		}
		result.append("</table>");
		return result.toString();
	}
	
	public String getAttachType(String value){
		if(value.equals("docx")  || value.equals("doc")){
			return "<span style='color:red;font-weight:bold;'>文档</span><img align='absmiddle' src='../icon/word.gif' />";
		}else if(value.equals("xlsx") || value.equals("xls")){
			return "<span style='color:green;font-weight:bold;'>表格</span><img align='absmiddle'  src='../icon/excel.gif' />";
		}else if(value.equals("gif") || value.equals("jpg") || value.equals("bmp")){
			return "<span style='color:green;font-weight:bold;'>图片</span><img align='absmiddle'  src='../icon/icon_14.gif' />";
		}else if(value.equals("pdf")){
			return "<span style='color:blue;font-weight:bold;'>文档</span><img align='absmiddle'  src='../icon/icon_17.gif' />";
		}else if(value.equals("zip") || value.equals("rar")){
			return "<span style='color:blue;font-weight:bold;'>压缩</span><img align='absmiddle'  src='../icon/icon_15.gif' />";
		}else if(value.equals("txt")){
			return "<span style='color:blue;font-weight:bold;'>文本</span><img align='absmiddle'  src='../icon/icon_16.gif' />";
		}else{
			return "<span style='color:blue;font-weight:bold;'>其他</span><img align='absmiddle'  src='../icon/other.gif' />";		
		}
	}
	
	
}
