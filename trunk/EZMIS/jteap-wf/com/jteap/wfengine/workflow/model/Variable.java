package com.jteap.wfengine.workflow.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.core.utils.DateUtils;

/**
 * 流程变量实体类
 *@filename   Variable.java
 *@author       MrBao
 *@date  	        2009-7-8
 *@remark
 */
@Entity  
@Table(name="tb_wf_variable")
public class Variable {
	
	public static final String 	VAR_TYPE_STRING="字符串";
	public static final String 	VAR_TYPE_LONG="整型";
	public static final String 	VAR_TYPE_DOUBLE="浮点型";
	public static final String 	VAR_TYPE_DATE="日期型";
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@Column(name="VAR_NAME")
	private String name;//变量名
	
	@Column(name="VAR_NAME_CN")
	private String name_cn;//中文名
	
	@Column(name="VAR_TYPE")
	private String type;//变量类型
	
	@Column(name="VAR_VALUE")
	private String value;//变量值
	
	@Column(name="VAR_KIND")
	private String kind;//类别
	
	@Column(name="VAR_STOREMODE")
	private String storemode;//存储方式
	
	@Column(name="VAR_DISPLAY_STYLE")
	private String dispalyStyle;//显示格式
	
	@ManyToOne()
	@JoinColumn(name="FLOWCONFIG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private FlowConfig flowConfig;//流程配置对象
	
	@OneToMany(mappedBy="variable")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="VAR_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<NodeVariable> nodeVariables;//节点变量集合

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_cn() {
		return name_cn;
	}

	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDispalyStyle() {
		return dispalyStyle;
	}

	public void setDispalyStyle(String dispalyStyle) {
		this.dispalyStyle = dispalyStyle;
	}

	public FlowConfig getFlowConfig() {
		return flowConfig;
	}

	public void setFlowConfig(FlowConfig flowConfig) {
		this.flowConfig = flowConfig;
	}

	public String getStoremode() {
		return storemode;
	}

	public void setStoremode(String storemode) {
		this.storemode = storemode;
	}

	public Set<NodeVariable> getNodeVariables() {
		return nodeVariables;
	}

	public void setNodeVariables(Set<NodeVariable> nodeVariables) {
		this.nodeVariables = nodeVariables;
	}
	
	/**
	 * 得到初始值
	 */
	public Object getInitValue(){
		if (VAR_TYPE_STRING.equals(this.type)) {
			return "";
		}
		if (VAR_TYPE_DATE.equals(this.type)) {
			return new Date();
		}
		if (VAR_TYPE_LONG.equals(this.type)) {
			return 0L;
		}
		if (VAR_TYPE_DOUBLE.equals(this.type)) {
			return 0D;
		}
		return null;
	}
	/**
	 * 得到变量的真正的值
	 * 所有的值都是以字符串形式存储，取出来的时候需要根据不同的类型进行不同的转换取得对应的对象的值
	 * @return
	 */
	public Object getRealValue(){
		if(this.value == null)
			return this.getInitValue();
		if (VAR_TYPE_STRING.equals(this.type)) {
			return this.value;
		}
		if (VAR_TYPE_DATE.equals(this.type)) {
			return DateUtils.parseDate(this.value);
		}
		if (VAR_TYPE_LONG.equals(this.type)) {
			return Long.parseLong(this.value);
		}
		if (VAR_TYPE_DOUBLE.equals(this.type)) {
			return Double.parseDouble(this.value);
		}		
		return null;
			
	}
	
}
