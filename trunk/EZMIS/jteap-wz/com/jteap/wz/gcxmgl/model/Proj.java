package com.jteap.wz.gcxmgl.model;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.gclbgl.model.Projcat;

import java.util.*;

@Entity
@Table(name = "TB_WZ_PROJ")
public class Proj {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@Column(name = "PROJCODE")
	private String projcode;
	
	@ManyToOne()
	@JoinColumn(name="PROJCATCODE")
	@LazyToOne(LazyToOneOption.PROXY)
	private Projcat projcat;
	
	@ManyToOne()
	@JoinColumn(name="GC__PROJCODE")
	@LazyToOne(LazyToOneOption.PROXY)
	private Proj p_proj;
	
	
	@OneToMany(mappedBy="p_proj")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Proj> c_proj = new ArrayList<Proj>();	//子组织
	
	@Column(name = "PROJNAME")
	private String projname;
	
	@Column(name = "ISHOW")
	private String ishow;
	
	@Column(name = "ISVIABLE")
	private String isviable;
	
	@Column(name = "TASKDESC")
	private String taskdesc;
	
	@Column(name = "TARGET")
	private String target;
	
	@Column(name = "EXECDEPT")
	private String execdept;
	
	@Column(name = "STARTTIME_PLAN")
	private Date starttime_plan;
	
	@Column(name = "ENDTIME_PLAN")
	private Date endtime_plan;
	
	@Column(name = "STARTTIME_FACT")
	private Date starttime_fact;
	
	@Column(name = "ENDTIME_FACT")
	private Date endtime_fact;
	
	@Column(name = "FUNDLIMIT")
	private Double fundlimit;
	
	@Column(name = "FUNDUSED")
	private Double fundused;
	
	@Column(name = "MFUNDLIMIT")
	private Double mfundlimit;
	
	@Column(name = "MFUNDUSED")
	private Double mfundused;
	
	@Column(name = "JUDGE")
	private String judge;
	
	@Column(name = "FINISHED")
	private String finished;
	
	@Column(name = "LIMITON")
	private String limiton;
	
	@Column(name = "TIMELIMIT")
	private String timelimit;
	
	@Column(name = "LEVELCODE")
	private String levelcode;
	
	@Column(name = "GCXMBH")
	private String gcxmbh;
	
	@Column(name = "PPROJFEE")
	private Double pprojfee;
	
	@Column(name = "APROJFEE")
	private Double aprojfee;
	
	@Column(name = "NEEDPLAN")
	private String needplan;
	
	@Column(name = "C_C_SGDW")
	private String c_c_sgdw;
	
	@Column(name = "C_C_FYBM")
	private String c_c_fybm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjcode() {
		return projcode;
	}

	public void setProjcode(String projcode) {
		this.projcode = projcode;
	}

	public Projcat getProjcat() {
		return projcat;
	}

	public void setProjcat(Projcat projcat) {
		this.projcat = projcat;
	}

	public Proj getP_proj() {
		return p_proj;
	}

	public void setP_proj(Proj p_proj) {
		this.p_proj = p_proj;
	}

	public String getProjname() {
		return projname;
	}

	public void setProjname(String projname) {
		this.projname = projname;
	}

	public String getTaskdesc() {
		return taskdesc;
	}

	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getExecdept() {
		return execdept;
	}

	public void setExecdept(String execdept) {
		this.execdept = execdept;
	}

	public Date getStarttime_plan() {
		return starttime_plan;
	}

	public void setStarttime_plan(Date starttime_plan) {
		this.starttime_plan = starttime_plan;
	}

	public Date getEndtime_plan() {
		return endtime_plan;
	}

	public void setEndtime_plan(Date endtime_plan) {
		this.endtime_plan = endtime_plan;
	}

	public Date getStarttime_fact() {
		return starttime_fact;
	}

	public void setStarttime_fact(Date starttime_fact) {
		this.starttime_fact = starttime_fact;
	}

	public Date getEndtime_fact() {
		return endtime_fact;
	}

	public void setEndtime_fact(Date endtime_fact) {
		this.endtime_fact = endtime_fact;
	}

	public Double getFundlimit() {
		return fundlimit;
	}

	public void setFundlimit(Double fundlimit) {
		this.fundlimit = fundlimit;
	}

	public Double getFundused() {
		return fundused;
	}

	public void setFundused(Double fundused) {
		this.fundused = fundused;
	}

	public Double getMfundlimit() {
		return mfundlimit;
	}

	public void setMfundlimit(Double mfundlimit) {
		this.mfundlimit = mfundlimit;
	}

	public Double getMfundused() {
		return mfundused;
	}

	public void setMfundused(Double mfundused) {
		this.mfundused = mfundused;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getFinished() {
		return finished;
	}

	public void setFinished(String finished) {
		this.finished = finished;
	}

	public String getLimiton() {
		return limiton;
	}

	public void setLimiton(String limiton) {
		this.limiton = limiton;
	}

	public String getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}

	public String getLevelcode() {
		return levelcode;
	}

	public void setLevelcode(String levelcode) {
		this.levelcode = levelcode;
	}

	public String getGcxmbh() {
		return gcxmbh;
	}

	public void setGcxmbh(String gcxmbh) {
		this.gcxmbh = gcxmbh;
	}

	public Double getPprojfee() {
		return pprojfee;
	}

	public void setPprojfee(Double pprojfee) {
		this.pprojfee = pprojfee;
	}

	public Double getAprojfee() {
		return aprojfee;
	}

	public void setAprojfee(Double aprojfee) {
		this.aprojfee = aprojfee;
	}

	public String getNeedplan() {
		return needplan;
	}

	public void setNeedplan(String needplan) {
		this.needplan = needplan;
	}

	public String getC_c_sgdw() {
		return c_c_sgdw;
	}

	public void setC_c_sgdw(String c_c_sgdw) {
		this.c_c_sgdw = c_c_sgdw;
	}

	public String getC_c_fybm() {
		return c_c_fybm;
	}

	public void setC_c_fybm(String c_c_fybm) {
		this.c_c_fybm = c_c_fybm;
	}

	public List<Proj> getC_proj() {
		return c_proj;
	}

	public void setC_proj(List<Proj> c_proj) {
		this.c_proj = c_proj;
	}

	public String getIsviable() {
		return isviable;
	}

	public void setIsviable(String isviable) {
		this.isviable = isviable;
	}

	public String getIshow() {
		return ishow;
	}

	public void setIshow(String ishow) {
		this.ishow = ishow;
	}
}
