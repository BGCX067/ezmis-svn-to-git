package com.jteap.form.eform.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.form.dbdef.model.DefTableInfo;

/**
 * 自定义表单模型对象,该对象的属性即包括excel表单也包括eform表单
 * @author tanchang
 *
 */
@Entity  
@Table(name="tb_form_eform")
@SuppressWarnings("unchecked")
public class EForm {
	public static final String EFORM_TYPE_EFORM="EFORM";
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;	//编号
	
	@Column(name="TITLE")
	private String title;	//标题
	
	@Column(name="SN")
	private String sn;		//表单简称 eform
	
	@Column(name="VERSION")
	private int version=1;	//版本
	
	@Column(name="CREATOR")
	private String creator;	//创建人
	
	@Column(name="CREAT_DT")
	private Date creatDt;	//创建时间
	
	@Column(name="EFORM_TYPE")
	private String type;	//类型 EFORM
	
	@ManyToOne()
	@JoinColumn(name="CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private EFormCatalog catalog;
	
	@Column(name="EFORM_NM")
	private String nm;	//内码 用于关联不同版本的表单的统一性,不同版本的表单，内码相同


	//可编辑域json字符串[{fd:'',cp:'',tp:''},{fd:'',cp:'',tp:''}]
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="EDITABLE_LIST", columnDefinition="CLOB", nullable=true) 
	private String editableInputs;
	
	
	@Column(name="EFORM_HTML_URL")
	private String exHtmlUrl;	//excel to html url
	
	
	///////////////Excel表单专用属性/////////////////////
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="DEF_TABLE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private  DefTableInfo defTable;	//Excel表单关联表名
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="EFORM_ONLOAD_SCRIPT", columnDefinition="CLOB", nullable=true) 
	private String onloadScript;	//表单脚本，该脚本在表单初始化完成之后执行
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "EXCEL_TEMP", columnDefinition = "BLOB",nullable=true) 
	private byte[] excelTemplate;	//excel模板
	
	@Column(name="IS_NEW_VER")
	private boolean newVer;		//是否最新版本
	
	@Column(name="FINAL_MANUSCRIPT")
	private boolean finalManuscript;  //是否已定稿
	
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

	@Column(name="SORTNO")
	private int sortNo = 1;
	
	@Column(name="WIDTH")
	private int width;
	
	@Column(name="HEIGHT")
	private int height;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	//以下是属性方法
	public String getExcelDataItemXml() {
		
		return excelDataItemXml;
	}

	public void setExcelDataItemXml(String excelDataItemXml) {
		this.excelDataItemXml = excelDataItemXml;
	}



	public DefTableInfo getDefTable() {
		return defTable;
	}

	public void setDefTable(DefTableInfo defTable) {
		this.defTable = defTable;
	}

	public String getEditableInputs() {
		return editableInputs;
	}

	public void setEditableInputs(String editableInputs) {
		this.editableInputs = editableInputs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatDt() {
		return creatDt;
	}

	public void setCreatDt(Date creatDt) {
		this.creatDt = creatDt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}




	public byte[] getExcelTemplate() {
		return excelTemplate;
	}

	public void setExcelTemplate(byte[] excelTemplate) {
		this.excelTemplate = excelTemplate;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public boolean isNewVer() {
		return newVer;
	}

	public void setNewVer(boolean newVer) {
		this.newVer = newVer;
	}

	public String getExHtmlUrl() {
		return exHtmlUrl;
	}

	public void setExHtmlUrl(String exHtmlUrl) {
		this.exHtmlUrl = exHtmlUrl;
	}

	public EFormCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(EFormCatalog catalog) {
		this.catalog = catalog;
	}

	public boolean isFinalManuscript() {
		return finalManuscript;
	}

	public void setFinalManuscript(boolean finalManuscript) {
		this.finalManuscript = finalManuscript;
	}

	public String getOnloadScript() {
		return onloadScript;
	}

	public void setOnloadScript(String onloadScript) {
		this.onloadScript = onloadScript;
	}


}
