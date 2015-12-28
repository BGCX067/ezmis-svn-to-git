package com.jteap.wfengine.workflow.model;

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


@Entity  
@Table(name="tb_wf_node_perm")
public class NodePermission {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@ManyToOne()
	@JoinColumn(name="NODE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private NodeConfig nodeConfig;//权限名称
	
	@Column(name="PERM_NAME")
	private String name;//权限名称
	
	@Column(name="PERM_EXPRESSION")
	private String expression;//表达式
	
	@Column(name="PERM_PROCESS_MODE")
	private String processMode;//处理方式  1:串行处理会签，0:并行处理会签
	
	@Column(name="PERM_PROCESS_KIND")
	private String processKind;//处理类别 single:单人处理,multi:多人处理
	
	@Column(name="PERM_PROCESS_ACTOR")
	private boolean isOneProcessActor;//必须指定一个处理人
	
	@Column(name="PERM_IS_CHOOSE")
	private boolean isChooseActor;//是否可以自由选择处理人

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

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getProcessMode() {
		return processMode;
	}

	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}

	public String getProcessKind() {
		return processKind;
	}

	public void setProcessKind(String processKind) {
		this.processKind = processKind;
	}

	public boolean getIsOneProcessActor() {
		return isOneProcessActor;
	}

	public void setIsOneProcessActor(boolean isOneProcessActor) {
		this.isOneProcessActor = isOneProcessActor;
	}

	public boolean getIsChooseActor() {
		return isChooseActor;
	}

	public void setIsChooseActor(boolean isChooseActor) {
		this.isChooseActor = isChooseActor;
	}

	public NodeConfig getNodeConfig() {
		return nodeConfig;
	}

	public void setNodeConfig(NodeConfig nodeConfig) {
		this.nodeConfig = nodeConfig;
	}

	public void setChooseActor(boolean isChooseActor) {
		this.isChooseActor = isChooseActor;
	}
	
	

}
