package com.jteap.system.dataperm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.system.person.model.Person;

@Entity
@Table(name = "TB_SYS_DATAPERM_PERSON")
public class Perm2Person {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="PERSONID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Person person;
	
	@ManyToOne()
	@JoinColumn(name="DATAPERMID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DataPerm perm;

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

	public DataPerm getPerm() {
		return perm;
	}

	public void setPerm(DataPerm perm) {
		this.perm = perm;
	}

}
