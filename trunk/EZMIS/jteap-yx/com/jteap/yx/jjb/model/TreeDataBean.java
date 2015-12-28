package com.jteap.yx.jjb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 运行表单 树数据bean
 * @author lvchao
 *
 */
@Entity
@Table(name = "TB_YX_TDATA")
public class TreeDataBean {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;                    //编码
	
//	@ManyToOne()
//	@JoinColumn(name="PARTENT_ID")
//	@LazyToOne(LazyToOneOption.PROXY)
//	private TreeDataBean parent;           
//	
//	@OneToMany(mappedBy="parent")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	private Set<TreeDataBean> children;  
	
	@Column(name = "NAME")
	private String name;                   
	
	@Column(name = "VALUE")
	private String value;                   
	
	@Column(name = "FLAG")
	private String flag;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public TreeDataBean getParent() {
//		return parent;
//	}
//
//	public void setParent(TreeDataBean parent) {
//		this.parent = parent;
//	}
//
//	public Set<TreeDataBean> getChildren() {
//		return children;
//	}
//
//	public void setChildren(Set<TreeDataBean> children) {
//		this.children = children;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}       
	
}
