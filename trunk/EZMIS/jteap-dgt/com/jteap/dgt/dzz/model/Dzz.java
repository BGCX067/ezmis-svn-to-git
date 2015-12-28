package com.jteap.dgt.dzz.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.dgt.dyxxk.model.Dyxxk;


/**
 * 党组织实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_DGT_DZZ")
@SuppressWarnings("unchecked")
public class Dzz {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="DANGZU_NAME")
	private String dangzu_name;
	
	@Column(name="BEIZHU")
	private String beizhu;
	
	@Column(name="PARENT_ID")
	private String parent_id;
	
	@Column(name="SORTNO")
	private Integer sortno;

	@OneToMany(mappedBy="dzz")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Dyxxk> getDyxxk;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDangzu_name() {
		return dangzu_name;
	}

	public void setDangzu_name(String dangzu_name) {
		this.dangzu_name = dangzu_name;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public Set<Dyxxk> getGetDyxxk() {
		return getDyxxk;
	}

	public void setGetDyxxk(Set<Dyxxk> getDyxxk) {
		this.getDyxxk = getDyxxk;
	}
	
	
	

	
	
	
}
