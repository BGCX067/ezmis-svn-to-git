/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.zbjl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 值班记录实体
 * 
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_ZBJL")
public class ZhiBanJiLu
{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    @Type(type = "string")
    private java.lang.String id;

    @Column(name = "JZH")
    private java.lang.String jzh;

    @Column(name = "GWLB")
    private java.lang.String gwlb;

    @Column(name = "JLLB")
    private java.lang.String jllb;

    @Column(name = "ZBSJ")
    private java.util.Date zbsj;

    @Column(name = "ZBBC")
    private java.lang.String zbbc;

    @Column(name = "ZBZB")
    private java.lang.String zbzb;

    @Column(name = "JLSJ")
    private java.util.Date jlsj;

    @Column(name = "JLNR")
    private java.lang.String jlnr;

    @Column(name = "JLR")
    private java.lang.String jlr;

    @Column(name = "TZGW")
    private java.lang.String tzgw;

    @Column(name = "ZY")
    private java.lang.String zy;

    @Column(name = "ZZJL_TYPE")
    private java.lang.String zzjlType;

    @Column(name = "NRCOLOR")
    private java.lang.String nrcolor;

    public java.lang.String getZzjlType()
    {
        return zzjlType;
    }

    public void setZzjlType(java.lang.String zzjlType)
    {
        this.zzjlType = zzjlType;
    }

    public java.lang.String getId()
    {
        return this.id;
    }

    public void setId(java.lang.String id)
    {
        this.id = id;
    }

    public java.lang.String getJzh()
    {
        return this.jzh;
    }

    public void setJzh(java.lang.String value)
    {
        this.jzh = value;
    }

    public java.lang.String getGwlb()
    {
        return this.gwlb;
    }

    public void setGwlb(java.lang.String value)
    {
        this.gwlb = value;
    }

    public java.lang.String getJllb()
    {
        return this.jllb;
    }

    public void setJllb(java.lang.String value)
    {
        this.jllb = value;
    }

    public java.util.Date getZbsj()
    {
        return this.zbsj;
    }

    public void setZbsj(java.util.Date value)
    {
        this.zbsj = value;
    }

    public java.lang.String getZbbc()
    {
        return this.zbbc;
    }

    public void setZbbc(java.lang.String value)
    {
        this.zbbc = value;
    }

    public java.lang.String getZbzb()
    {
        return this.zbzb;
    }

    public void setZbzb(java.lang.String value)
    {
        this.zbzb = value;
    }

    public java.util.Date getJlsj()
    {
        return this.jlsj;
    }

    public void setJlsj(java.util.Date value)
    {
        this.jlsj = value;
    }

    public java.lang.String getJlnr()
    {
        return this.jlnr;
    }

    public void setJlnr(java.lang.String value)
    {
        this.jlnr = value;
    }

    public java.lang.String getJlr()
    {
        return this.jlr;
    }

    public void setJlr(java.lang.String value)
    {
        this.jlr = value;
    }

    public java.lang.String getTzgw()
    {
        return this.tzgw;
    }

    public void setTzgw(java.lang.String value)
    {
        this.tzgw = value;
    }

    public java.lang.String getZy()
    {
        return this.zy;
    }

    public void setZy(java.lang.String value)
    {
        this.zy = value;
    }

    public java.lang.String getNrcolor()
    {
        return nrcolor;
    }

    public void setNrcolor(java.lang.String nrcolor)
    {
        this.nrcolor = nrcolor;
    }

}
