package com.jteap.jhtj.bbsj.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity  
@Table(name="tj_bbsjts")
public class BbSjTs {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	@Column(name="bbindexid")
	private String bbindexid;
	//可编辑域json字符串[{fd:'',cp:'',tp:''},{fd:'',cp:'',tp:''}]
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="EDITABLE_LIST", columnDefinition="CLOB", nullable=true) 
	private String editableInputs;
	/**
	 * cp:caption 标题
	 * fd:field name字段名称
	 * ce: cell 单元格   49,6
	 * tp: type 数据类型  普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07、计算域08

	 * cf : calculate formula 计算公式
	 * mt :	must 是否必填项目  必填01 选填02
	 * fm: fomat 格式化  适用于数字类型和日期类型，也可是${fdname}222${dt}类似字符串
	 * st: save type 保存方式  追加01 覆盖02
	 * ip: is allow input 是否允许键盘输入 允许01 不允许02
	 * ev: enumValue 枚举值  是,否
	 * vl: 验证规则  x>10 & x<100等
	 * dv: default value 默认值  直接填写/通配符配置  
	 * 		${personLoginName}				当前登录名称 可使用工号
			${sysdt(yyyy-MM-dd hh:mm:ss)}	当前时间
			${sNo(####)}					流水号
			${UUID32}						32位UUID
	 * 
	 * <root tn=''>
	 * 	  	<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
	 * 		<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
	 * 		<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
	 * </root>
	 * 
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="EXCEL_DI_XML", columnDefinition="CLOB", nullable=true) 
	private String excelDataItemXml;	//excel数据项xml
	public String getExcelDataItemXml() {
		return excelDataItemXml;
	}
	public void setExcelDataItemXml(String excelDataItemXml) {
		this.excelDataItemXml = excelDataItemXml;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEditableInputs() {
		return editableInputs;
	}
	public void setEditableInputs(String editableInputs) {
		this.editableInputs = editableInputs;
	}
	public String getBbindexid() {
		return bbindexid;
	}
	public void setBbindexid(String bbindexid) {
		this.bbindexid = bbindexid;
	}
}
