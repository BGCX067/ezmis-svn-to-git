package com.jteap.system.doclib.model;

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

/**
 * TbDoclibAttach entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "TB_DOCLIB_ATTACH")
@SuppressWarnings("unchecked")
public class DoclibAttach {

	public DoclibAttach() {
	}

	public DoclibAttach(String id, String name, String type, String doclibSize) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.doclibSize = doclibSize;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id; // 编号

	@ManyToOne()
	@JoinColumn(name = "DOCLIB_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Doclib doclib;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "DOCLIB_SIZE")
	private String doclibSize;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", columnDefinition = "BLOB", nullable = true)
	private byte[] content;

	@Column(name = "SORTNO")
	private int sortNo;

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Doclib getDoclib() {
		return doclib;
	}

	public void setDoclib(Doclib doclib) {
		this.doclib = doclib;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getDoclibSize() {
		return doclibSize;
	}

	public void setDoclibSize(String doclibSize) {
		this.doclibSize = doclibSize;
	}
}