package com.jteap.system.person.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.hibernate.Query;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.MD5;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;

/**
 * 人员处理类
 * @author tantyou
 * @date 2007-12-10
 */
@SuppressWarnings("unchecked")
public class PersonManager extends HibernateEntityDao<Person> {

//	/**
//	 * 查询出指定组织的管理员
//	 * 
//	 * @param group
//	 * @return
//	 */
//
//	public List<Person> findAdminPersonsByGroup(Group group) {
//		String hql = "from P2G as p2g where p2g.group=? and p2g.isAdmin=true";
//		return this.find(hql, group);
//	}

//	/**
//	 * 查询出指定组织的管理员
//	 * 
//	 * @param group
//	 * @return
//	 */
//	public List<Person> findAdminPersonsByGroupId(Serializable id) {
//		Group group = this.findUniqueBy(Group.class, "id", id);
//		return findAdminPersonsByGroup(group);
//	}
	
	/**
	 * 
	 * 指定用户可以管理哪些组织，将这些组织的编号组织成一个逗号分隔的字符串返回
	 * @param personId 用户编号
	 * @return "1234,1333,432134,"
	 */
	public String findAdminGroupIdsOfThePerson(String personId){
		String hql="from P2G as p2g where p2g.person.id='"+personId+"' and p2g.isAdmin='1'";
		StringBuffer result=new StringBuffer();
		Collection<P2G> p2gs=this.find(hql);
		for (P2G p2g : p2gs) {
			result.append(p2g.getGroup().getId().toString()+",");
		}
		return result.toString();
	}

	
	/**
	 * 根据用户组ids获取组内所有用户集合
	 *@author  MrBao 
	 *@date 	  2009-7-14
	 *@param groupIds
	 *@return
	 *@return Collection<Person>
	 *@remark
	 */
	public Collection<Person> findPersonByGroupIds(String groupIds){
		
		groupIds = StringUtil.idsToWhereIn(groupIds);
		
		Collection<Person> persons = new ArrayList<Person>();
		String hql="from P2G as p2g where p2g.group.id in ("+groupIds+")";
		Collection<P2G> p2gs = this.find(hql);
		for (P2G p2g : p2gs) {
			persons.add(p2g.getPerson());
		}
		
		return persons;
	}

	/**
	 *
	 * 描述 : 根据组织内码获取组织内所有用户集合
	 * 作者 : wangyun
	 * 时间 : 2010-12-23
	 * 参数 : 
	 * 		groupSns ： 组织内码
	 * 返回值 : 
	 * 		人员集合
	 * 异常 : 
	 *
	 */
	public Collection<? extends Person> findPersonByGroupSns(String groupSns) {
		groupSns = StringUtil.idsToWhereIn(groupSns);
		Collection<Person> persons = new ArrayList<Person>();
		String hql="from P2G as p2g where p2g.group.groupSn in ("+groupSns+")";
		Collection<P2G> p2gs = this.find(hql);
		for (P2G p2g : p2gs) {
			persons.add(p2g.getPerson());
		}
		
		return persons;
	}

	/**
	 * 根据角色ids获取角色内所有用户集合
	 *@author  MrBao 
	 *@date 	  2009-7-14
	 *@param roleIds
	 *@return
	 *@return Collection<Person>
	 *@remark
	 */
	public Collection<Person> findPersonByRoleIds(String roleIds){
		
		roleIds = StringUtil.idsToWhereIn(roleIds);
		
		Collection<Person> persons = new ArrayList<Person>();
		String hql="from P2Role as p2r where p2r.role.id in ("+roleIds+")";
		Collection<P2Role> p2rs = this.find(hql);
		for (P2Role p2r : p2rs) {
			persons.add(p2r.getPerson());
		}
		
		return persons;
	}

	/**
	 *
	 * 描述 : 根据角色内码获取角色内所有用户集合
	 * 作者 : wangyun
	 * 时间 : 2010-12-23
	 * 参数 : 
	 * 		roleSns ： 角色内码
	 * 返回值 : 
	 * 		人员集合
	 * 异常 : 
	 *
	 */
	public Collection<Person> findPersonByRoleSns(String roleSns) {
		roleSns = StringUtil.idsToWhereIn(roleSns);
		Collection<Person> persons = new ArrayList<Person>();
		String hql="from P2Role as p2r where p2r.role.roleSn in ("+roleSns+")";
		Collection<P2Role> p2rs = this.find(hql);
		for (P2Role p2r : p2rs) {
			persons.add(p2r.getPerson());
		}
		
		return persons;
	}

	/**
	 * 修改用户的密码
	 * 
	 * @param person
	 * @param newPassword
	 *            新的用户密码
	 * @return
	 */
	public boolean changePassword(Person person, String newPassword) {
		MD5 md5 = new MD5();
		String mdPassword = md5.getMD5ofStr(newPassword);
		String hql = "update Person as p set p.userPwd=? where p.id=?";
		Query query = this.createQuery(hql, mdPassword, person.getId());
		int result = query.executeUpdate();
		// 因为hql update对象不会更新缓存中的对象，所以将该对象在缓存中清除，下次查询的时候强制从数据库中读取
		this.evit(person);
		return result > 0;
	}
	/**
	 * 
	 * 方法功能描述 :批理初始化用户密码
	 * @author 唐剑钢
	 * @param ids
	 * @return
	 * 2008-1-31
	 * 返回类型：boolean
	 */
	public boolean initPassword(Serializable[] ids){
		MD5 md5 = new MD5();
		String mdPassword = md5.getMD5ofStr(SystemConfig.getProperty("DEFAULT_START_PASSWORD"));
		String hql="update	Person as p set p.userPwd=? where";
		boolean flag=false;
		for (Serializable id : ids) {
			if(flag)
				hql+=" or";
			hql += " p.id='" + id + "'";
			flag=true;
		}
		Query query = this.createQuery(hql,mdPassword);
		return query.executeUpdate()>0;
	}
	/**
	 * 修改密码
	 * 
	 * @param id
	 * @param newPassword
	 * @return
	 */
	public boolean changePassword(String loginName, String newPassword) {
		MD5 md5 = new MD5();
		String mdPassword = md5.getMD5ofStr(newPassword);
		String hql="update Person as p set p.userPwd=? where p.userLoginName=?";
		Query query = this.createQuery(hql, mdPassword, loginName);
		int result = query.executeUpdate();
		return result > 0;
	}

	/**
	 * 锁定用户
	 * 
	 * @param person
	 * @return
	 */
	public boolean lockAndUnlockPerson(Serializable id, boolean lockStatus) {
		String hql = "update Person as p set p.status=? where p.id=?";
		String status = lockStatus ? Person.PERSON_STATUS_LOCKED
				: Person.PERSON_STATUS_NORMAL;
		Query query = this.createQuery(hql, status, id);
		int result = query.executeUpdate();
		if (result > 0)
			this.evit(this.get(id));
		return result > 0;
	}
	
	/**
	 * 批量锁定及解锁用户
	 * 
	 * @param ids
	 * @param lockStatus
	 * @return
	 */
	public boolean lockAndUnlockPersons(Serializable[] ids, boolean lockStatus) {
		String hql = "update Person as p set p.status=? where";
		boolean flag=false;
		for (Serializable id : ids) {
			if(flag)
				hql+=" or";
			hql += " p.id='" + id + "'";
			flag=true;
		}
		Query query;
		if(lockStatus){
			query = this.createQuery(hql, Person.PERSON_STATUS_LOCKED);
		}else{
			query = this.createQuery(hql, Person.PERSON_STATUS_NORMAL);
		}
		return query.executeUpdate() > 0;
	}
	
	/**
	 * 根据用户名称和密码 找出对应的用户对象
	 * @param userLoginName
	 * @param pwd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person findPersonByLoginNameAndPwd(String userLoginName,String pwd){
		MD5 md5 = new MD5();
		String hql="from Person as p where (p.userLoginName=? and p.userPwd=?) or(p.userLoginName2=? and p.userPwd=?)";
		String md5Pwd=md5.getMD5ofStr(pwd);
		List tmps=this.find(hql, userLoginName,md5Pwd,userLoginName,md5Pwd);
		if(tmps.size()>0){
			return (Person) tmps.toArray()[0];
		}else
		return null;
	}
	
	/**
	 * 根据用户名称和密码(MD5) 找出对应的用户对象
	 * @param userLoginName
	 * @param pwd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person findPersonByLoginNameAndPwdMd5(String userLoginName,String pwd){
		String hql="from Person as p where p.userLoginName=? and p.userPwd=?";
		List tmps=this.find(hql, userLoginName,pwd);
		if(tmps.size()>0){
			return (Person) tmps.toArray()[0];
		}else {
			hql = "from Person as p where p.userLoginName2=? and p.userPwd=?";
			tmps=this.find(hql, userLoginName,pwd);
			if(tmps.size()>0){
				return (Person) tmps.toArray()[0];
			}
		}
		return null;
	}

	/**
	 * 根据登录用户名取得人员对象
	 * @param userLoginName
	 * @return
	 */
	public Person findPersonByLoginName(String userLoginName){
		return this.findUniqueBy("userLoginName", userLoginName);
	}
	/**
	 * 根据用户姓名取得人员对象
	 * @param userLoginName
	 * @return
	 */
	public List<Person> findPersonByUserName(String userName){
		return this.find("from Person p where p.userName=?", userName);
	}
	/**
	 * 指定用户名&密码的人是否存在
	 * @param userLoginName
	 * @param userPwd
	 * @return
	 */
	public boolean isPersonExist(String userLoginName,String userPwd){
		return this.findPersonByLoginNameAndPwd(userLoginName, userPwd)!=null;
	}
    
	/**
	 * 取得当前登录用户对象
	 * @param sessionAttrs
	 * @return
	 */
	public Person getCurrentPerson(Map sessionAttrs) {
		String currentPersonId = (String) sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);
		Person person=null;
		if (currentPersonId.equals(Constants.ADMINISTRATOR_ID)) {
			person = new Person();
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
		} else {
			person = get(Person.class, currentPersonId);
		}
		return person;
	}  

	/**
	 * 
	 * 描述 : 根据所有用户的中文名，生成对应拼音结构的登录名（结构如： 张三->zhangs）
	 * 作者 : wangyun
	 * 时间 : Aug 24, 2010
	 * 异常 : Exception
	 * 
	 */
	public void hz2Py() throws RuntimeException {
		try {
			List<Person> lstPersons = this.getAll();
			for (int i = 0; i < lstPersons.size(); i++) {
				Person person = lstPersons.get(i);
				String userName = person.getUserName();
				userName = this.converterToFirstLetterSpell(person.getUserName());
				person.setUserLoginName2(userName);
				this.save(person);
				this.flush();
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述 : 将名字转换为拼音，姓为全拼（如：张三->zhangs，同名加上数字编号:例 张山->zhangs2） 
	 * 作者 : wangyun 
	 * 时间 : Aug 24, 2010
	 * 参数 : 
	 * 		person ： 需要转换的人员
	 * 返回值 : 
	 * 		pinyinName ： 转换后的人员拼音
	 */
	public String converterToFirstLetterSpell(String userName) {
		String pinyinName = "";
		char[] nameChar = userName.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					if (i <= 1) {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
					} else {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
					}
					
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		
		// 如果是添加用户，则同名加上数字编号
		List<Person> samePerson = this.findPersonLoginName(pinyinName);
		if (samePerson.size()>0) {
			pinyinName += samePerson.size()+1;
		}
		System.out.println(pinyinName);
		return pinyinName;
	}

	/**
	 * 
	 * 描述 : 根据登录名找到符合规则的所有的用户（规则为： login_name + 数字结尾）
	 * 作者 : wangyun
	 * 时间 : Aug 25, 2010
	 * 参数 : 
	 * 		loginName ： 登录名
	 * 返回值 : 
	 * 		lst ： 所有符合规则的用户
	 * 
	 */
	public List<Person> findPersonLoginName(String loginName) {
		Query query = this.getSession().createSQLQuery("select * from tb_sys_person  where regexp_like(login_name2,'^"+loginName+"[0-9]*$')");
		List<Person> lst = query.list();
		return lst;
	}


}
