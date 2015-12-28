package com.jteap.wz.cgjhgl.model;

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

import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.yhdgl.model.Yhdgl;

@Entity
@Table(name = "TB_WZ_RELA_DISTRIBUTE")
public class Distribute {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="YHDBH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Yhdgl yhdgl;
	
	@Column(name = "STYLE")	
	private String style;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzda;
	
	@ManyToOne()
	@JoinColumn(name="CGDBH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Cgjhgl cgjhgl;
	
	@Column(name = "QUANTITY")	
	private Double quantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Yhdgl getYhdgl() {
		return yhdgl;
	}

	public void setYhdgl(Yhdgl yhdgl) {
		this.yhdgl = yhdgl;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Wzda getWzda() {
		return wzda;
	}

	public void setWzda(Wzda wzda) {
		this.wzda = wzda;
	}

	public Cgjhgl getCgjhgl() {
		return cgjhgl;
	}

	public void setCgjhgl(Cgjhgl cgjhgl) {
		this.cgjhgl = cgjhgl;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
}
