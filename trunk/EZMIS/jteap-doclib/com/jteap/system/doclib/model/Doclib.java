package com.jteap.system.doclib.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

import com.jteap.core.utils.DateUtils;

/**
 * AbstractTbDoclib entity provides the base persistence definition of the
 * TbDoclib entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_DOCLIB")
@SuppressWarnings("unchecked")
public class Doclib {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id; // 编号

	@ManyToOne()
	@JoinColumn(name = "CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibCatalog doclibCatalog;

	@OneToMany(mappedBy = "doclib")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "SORTNO")
	private Set<DoclibAttach> attachs;
	
	@OneToMany(mappedBy = "doclib")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<DoclibFv> doclibFvs;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CREATOR")
	private String creator;
	
	@Column(name = "CREATEDT")
	private Date createdt;
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "CONTENT", columnDefinition = "CLOB",nullable=true)
	private java.lang.String content;
	
	@ManyToOne()
	@JoinColumn(name = "LEVEL_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibLevel doclibLevel;
	
	@Column(name = "PAGE_URL")
	private String pathurl;

	public String getPathurl() {
		return pathurl;
	}

	public void setPathurl(String pathurl) {
		this.pathurl = pathurl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedt() {
		return this.createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}

	public DoclibCatalog getDoclibCatalog() {
		return doclibCatalog;
	}

	public void setDoclibCatalog(DoclibCatalog doclibCatalog) {
		this.doclibCatalog = doclibCatalog;
	}

	public Set<DoclibAttach> getAttachs() {
		return attachs;
	}

	public void setAttachs(Set<DoclibAttach> attachs) {
		this.attachs = attachs;
	}

	public DoclibLevel getDoclibLevel() {
		return doclibLevel;
	}

	public void setDoclibLevel(DoclibLevel doclibLevel) {
		this.doclibLevel = doclibLevel;
	}

	public Set<DoclibFv> getDoclibFvs() {
		return doclibFvs;
	}

	public void setDoclibFvs(Set<DoclibFv> doclibFvs) {
		this.doclibFvs = doclibFvs;
	}

	public java.lang.String getContent() {
		return content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}
	public String getCreateDtStr(){
		return DateUtils.getDate(this.createdt, "yyyy-MM-dd");
	}
	public String getCreateDtStrNy(){
		return DateUtils.getDate(this.createdt,"MM/dd");
	}

}