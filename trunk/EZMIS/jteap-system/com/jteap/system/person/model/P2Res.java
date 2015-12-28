package com.jteap.system.person.model;

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

import com.jteap.system.resource.model.Resource;

/**
 * person to resource 中间对象
 * 
 * @author tantyou
 * @date 2008-2-18
 */
@Entity
@Table(name="tb_sys_person2res")
public class P2Res {

	//编号
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	private String id;
	
	//人员
	@ManyToOne()
	@JoinColumn(name="personid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Person person;
	
	//指定人员应该是必须存在的，所以没有必要使用级联保存和级联更新
	@Column(name="indicator")
	private String indicator;
	
	//资源
	@ManyToOne()
	@JoinColumn(name="resourcesid")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Resource resource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}


	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	
	
	
}
