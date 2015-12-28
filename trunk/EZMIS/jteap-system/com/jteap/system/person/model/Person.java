package com.jteap.system.person.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.core.Constants;

/**
 * 人员对象
 * @author tantyou
 * @date 2008-1-10
 */
@Entity
@Table(name = "tb_sys_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person {
	/**
	 * 用户状态
	 */
	public static final String PERSON_STATUS_LOCKED="锁定";
	public static final String PERSON_STATUS_NORMAL="正常";
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type="string")
	private Serializable id; // 编号
	
	//用户名
	@Column(name="login_name")
	private String userLoginName;
	
	//密码
	@Column(name="PWD")
	private String userPwd;
	
	//姓名
	@Column(name="PERSONNAME")
	private String userName;
	
	@Column(name="IS_DEL")
	private int isDel;//删除标记 1=删除 0=正常
	
	
	//性别
	@Column(name="sex")
	private String sex;
	
	//生日
	@Column(name="birthday")
	private Date birthday;
	
	//配置
	@Column(name="config")
	private String config;
	
	//排序
	@Column(name="sortno")
	private int sortNo;
	
	//状态
	@Column(name="status")
	private String status;
		
	@OneToMany(mappedBy="person")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="personid")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<P2G> groups;
    

	//所拥有的角色
	@OneToMany(mappedBy="person")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @JoinColumn(name="personid")
    @LazyCollection(LazyCollectionOption.TRUE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<P2Role>  roles;
	

	//创建者
	@Column(name="creator")
	private String creator;
	
	//创建日期
	@Column(name="createdt")
	private Date createDt;

	// 用户登录名2
	@Column(name="login_name2")
	private String userLoginName2;

    //得到字符串类型的日期
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Set<P2G> getGroups() {
		return groups;
	}

	public void setGroups(Set<P2G> groups) {
		this.groups = groups;
	}

	public String getStatus() {
		return status;
	}

	public Set<P2Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<P2Role> roles) {
		this.roles = roles;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person){
			Person p1=(Person) obj;
			return p1.getId().toString().equals(this.getId().toString());
		}else
			return false;
		
	}
	
	/**
	 * 取得当前用户所拥有的权限
	 * @return
	 */
	public String getMyRoles(){
		StringBuffer result = new StringBuffer("");
		if(!this.isRootPerson()){
			Collection<P2Role> p2rs = this.getRoles();
			for (P2Role role : p2rs) {
				result.append(role.getRole().getRolename()+",");
			}
		}
		if(result.length()>0){
			result = result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}

	/**
	 * 取得当前用户所在的组织
	 * @return
	 */
	public String getMyGroupNames(){
		StringBuffer result = new StringBuffer("");
		if(!this.isRootPerson()){
			Collection<P2G> p2gs = this.getGroups();
			for (P2G group : p2gs) {
				result.append(group.getGroup().getGroupName()+",");
			}
		}
		if(result.length()>0){
			result = result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}

	/**
	 *  是否是root用户
	 * @return
	 */
	public boolean isRootPerson(){
		return this.getId().equals(Constants.ADMINISTRATOR_ID) && this.getUserLoginName().equals(Constants.ADMINISTRATOR_ACCOUNT);
		
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public String getUserLoginName2() {
		return userLoginName2;
	}

	public void setUserLoginName2(String userLoginName2) {
		this.userLoginName2 = userLoginName2;
	}


}
