package com.jteap.wfengine.workflow.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 环节操作实体类
 *
 * @author 肖平松	
 * @version Oct 16, 2009
 */
@Entity
@Table(name="tb_wf_node_operation")
public class NodeOperation {
	// 编号
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="OP_ID")
	@Type(type="string")
	private Serializable id;
	
	//操作标识
	@Column(name="OP_MARK")
	private String mark;
	
	//操作名称
	@Column(name="OP_NAME")
	private String name;
	
	//是否显示
	@Column(name="SHOW")
	private String show;

	@ManyToOne()
	@JoinColumn(name="NODE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private NodeConfig nodeConfig;//节点编号
	
	// 部门操作权限
	@Column(name = "OPPERM_GROUP")
	private String opPerm_Group;

	// 角色操作权限
	@Column(name = "OPPERM_ROLE")
	private String opPerm_Role;
	
	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public NodeConfig getNodeConfig() {
		return nodeConfig;
	}

	public void setNodeConfig(NodeConfig nodeConfig) {
		this.nodeConfig = nodeConfig;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getOpPerm_Group() {
		return opPerm_Group;
	}

	public void setOpPerm_Group(String opPerm_Group) {
		this.opPerm_Group = opPerm_Group;
	}

	public String getOpPerm_Role() {
		return opPerm_Role;
	}

	public void setOpPerm_Role(String opPerm_Role) {
		this.opPerm_Role = opPerm_Role;
	}

}
