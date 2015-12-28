/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.zbjl.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.yx.zbjl.manager.ZhiBanJiLuManager;
import com.jteap.yx.zbjl.model.ZhiBanJiLu;

@SuppressWarnings({"serial","unchecked","deprecation"})
public class ZhiBanJiLuAction extends AbstractAction {
	
	private ZhiBanJiLuManager zhiBanJiLuManager;
	private PersonManager personManager;
	
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setZhiBanJiLuManager(ZhiBanJiLuManager zhiBanJiLuManager) {
		this.zhiBanJiLuManager = zhiBanJiLuManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return zhiBanJiLuManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]
		{
			"id","jzh","gwlb","jllb","zbsj","zbbc",
			"zbzb","jlsj","jlnr","jlr","tzgw","zy","zzjlType","nrcolor","time"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]
  		{
  			"id","jzh","gwlb","jllb","zbsj","zbbc",
  			"zbzb","jlsj","jlnr","jlr","tzgw","zy","zzjlType","nrcolor"
  		};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		HqlUtil.addOrder(hql, "obj.jlsj", "DESC");
	}

	/**
	 * 根据 登录人岗位 查询值班[记录]
	 * @return
	 */
	public String showRecordByTypeAction(){
		try {
			
			// 每页大小
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
			
			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			
			//岗位类别
			String gwlb = request.getParameter("gwlb");
			if("1".equals(gwlb)){
				gwlb = "#1机长";
			}else if("2".equals(gwlb)){
				gwlb = "#2机长";
			}else if("3".equals(gwlb)){
				gwlb = "#3机长";
			}else if("4".equals(gwlb)){
				gwlb = "#4机长";
			}
			//记录类别
			String jllb = request.getParameter("jllb");
			//值班时间
			String zbsj = request.getParameter("zbsj");
			//值班班次
			String zbbc = request.getParameter("zbbc");
			//值长值班记录类型
			String zzjlType = request.getParameter("zzjlType");
			//值班内容颜色
			//String nrcolorType = request.getParameter("nrcolorType");
			
			String hql = "from ZhiBanJiLu z where z.gwlb=? and z.jllb=? and to_char(z.zbsj,'yyyy-mm-dd')=? and z.zbbc=?";
			Object[] hqlParams = new Object[]{gwlb,jllb,zbsj,zbbc};
			if("值长".equals(gwlb) || "零米".equals(gwlb)){
				if(StringUtils.isNotEmpty(zzjlType)){
					hql += " and z.zzjlType=?";
					hqlParams = new Object[]{gwlb,jllb,zbsj,zbbc,zzjlType};
				}
			}
			hql += " order by z.jlsj DESC";
			
			// 开始分页查询
			Page page = getManager().pagedQueryWithStartIndex(hql,
					Integer.parseInt(start), Integer.parseInt(limit),hqlParams);
			Collection obj = (Collection) page.getResult();
			
			// 将集合JSON化
			String json = JSONUtil.listToJson(obj, listJsonProperties());
			
			json = ("{totalCount:'" + page.getTotalCount() + "',list:" + json + "}");
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 保存或修改 值班记录
	 * @return
	 */
	public String saveOrUpdateJlAction(){
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String json = request.getParameter("json");
			List<Map<String, String>> list = JSONUtil.parseList(json);
			
			//岗位类别
			String gwlb = list.get(0).get("gwlb");
			if("1".equals(gwlb)){
				gwlb = "#1机长";
			}else if("2".equals(gwlb)){
				gwlb = "#2机长";
			}else if("3".equals(gwlb)){
				gwlb = "#3机长";
			}else if("4".equals(gwlb)){
				gwlb = "#4机长";
			}
			
			//记录类别
			String jllb = list.get(0).get("jllb");
			String zbsjString = list.get(0).get("zbsj");
			Date zbsj = null;
			if(StringUtil.isNotEmpty(zbsjString)){
			//值班时间
				zbsj = dateFormat.parse(zbsjString);
			}
			//值班班次
			String zbbc = list.get(0).get("zbbc");
			//值班值别
			String zbzb = list.get(0).get("zbzb");
			
			for (int i = 1; i < list.size(); i++) {
				ZhiBanJiLu zhiBanJiLu = new ZhiBanJiLu();
				String id = list.get(i).get("id");
				if(StringUtil.isNotEmpty(id)){
					//ID
					zhiBanJiLu.setId(id);
				}
				//机组号
				zhiBanJiLu.setJzh(list.get(i).get("jzh"));
				String jlsj = list.get(i).get("jlsj");
				if(StringUtil.isNotEmpty(jlsj)){
					//记录时间
					zhiBanJiLu.setJlsj(dateFormat.parse(jlsj));
				}
				//记录内容
				zhiBanJiLu.setJlnr(list.get(i).get("jlnr"));
				//记录人
				zhiBanJiLu.setJlr(list.get(i).get("jlr"));
				//专业
				zhiBanJiLu.setZy(list.get(i).get("zy"));
				//值长、零米记录类型
				String zzjlType = "";
				if("值长".equals(gwlb) || "零米".equals(gwlb)){
					zzjlType = list.get(i).get("zzjlType");
				}
				
				//岗位类别
				zhiBanJiLu.setGwlb(gwlb);
				//记录类别
				zhiBanJiLu.setJllb(jllb);
				//值班时间
				zhiBanJiLu.setZbsj(zbsj);
				//值班班次
				zhiBanJiLu.setZbbc(zbbc);
				//值班值别
				zhiBanJiLu.setZbzb(zbzb);
				//内容颜色
				zhiBanJiLu.setNrcolor(list.get(i).get("nrcolor"));
				//值长值班记录类别
				zhiBanJiLu.setZzjlType(zzjlType);
				
				zhiBanJiLuManager.save(zhiBanJiLu);
			}
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 保存领导审阅记录
	 * @return
	 */
	public String saveShenYueAction(){
		try {
			String userName=request.getParameter("userLoginName");
			String userPwd=request.getParameter("userPassword");
			String zbsj = request.getParameter("zbsj");
			String zbbc = request.getParameter("zbbc");
			String gwlb = request.getParameter("gwlb");
			
			Person person = personManager.findPersonByLoginNameAndPwd(userName, userPwd);
			if(person  == null){
				this.outputJson("{success:true,validate:false}");
				return NONE;
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ZhiBanJiLu zhiBanJiLu = new ZhiBanJiLu();
			zhiBanJiLu.setJlr(person.getUserName());
			zhiBanJiLu.setJllb("审阅");
			zhiBanJiLu.setZbsj(dateFormat.parse(zbsj));
			zhiBanJiLu.setZbbc(zbbc);
			zhiBanJiLu.setGwlb(gwlb);
			zhiBanJiLu.setJlsj(new Date());
			zhiBanJiLuManager.save(zhiBanJiLu);
			
			this.outputJson("{success:true,validate:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	public String[] tzListJsonProperties() {
		return new String[]
		{
			"id","jlsj","jlnr","jlr","tzgw","time"
		};
	}
	
	/**
	 * 根据 登录人岗位 查询通知信息
	 * @return
	 */
	public String showTzByTypeAction(){
		try {
			//岗位类别
			String gwlb = request.getParameter("gwlb");
			//记录类别
			String jllb = request.getParameter("jllb");
			//String curUserName = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_NAME);
			
			// 每页大小
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			
			String hql = "from ZhiBanJiLu z where z.jllb=? order by z.jlsj DESC";
			List<ZhiBanJiLu> list = zhiBanJiLuManager.find(hql, new Object[]{jllb});
			
			if("1".equals(gwlb)){
				gwlb = "#1机长";
			}else if("2".equals(gwlb)){
				gwlb = "#2机长";
			}else if("3".equals(gwlb)){
				gwlb = "#3机长";
			}else if("4".equals(gwlb)){
				gwlb = "#4机长";
			}
			//根据登录人得岗位类别 判断是否显示通知消息
			List<ZhiBanJiLu> resultList = new ArrayList<ZhiBanJiLu>();
			for(ZhiBanJiLu zhiBanJiLu : list){
				String tzgw = zhiBanJiLu.getTzgw();
				if(tzgw != null && tzgw.indexOf(gwlb) != -1){
					resultList.add(zhiBanJiLu);
				}
			}
			
			//分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if(limitIndex > resultList.size()){
				limitIndex = resultList.size();
			}
			List<ZhiBanJiLu> pageList = resultList.subList(startIndex, limitIndex);
			
			String json = JSONUtil.listToJson(pageList, tzListJsonProperties());
			json = "{totalCount:'" + resultList.size() + "',list:"+ json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 保存或修改 值班通知
	 * @return
	 */
	public String saveOrUpdateTzAction(){
		try {
			String id = request.getParameter("id");
			//通知时间
			String jlsj = request.getParameter("jlsj");
			//通知岗位
			Date tzgwDate = null;
			String tzgw = request.getParameter("tzgw");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:m:s");
			//通知内容
			String jlnr = request.getParameter("jlnr");
			//发布人
			String jlr = request.getParameter("jlr");
			
			ZhiBanJiLu zhiBanJiLu = null;
			if(StringUtil.isNotEmpty(id) && !"null".equals(id)){
				zhiBanJiLu = zhiBanJiLuManager.get(id);
			}else {
				zhiBanJiLu = new ZhiBanJiLu();
			}
			if (StringUtil.isNotEmpty(jlsj)) {
				Date nowTime = new Date();
				if (jlsj.length() == 10) {
					jlsj += " " + nowTime.getHours() + ":" + nowTime.getMinutes() + ":" + nowTime.getSeconds();
				}else {
					jlsj += ":" + nowTime.getSeconds();
				}
				tzgwDate = dateFormat.parse(jlsj);
				zhiBanJiLu.setJlsj(tzgwDate);
			}
			if(StringUtil.isNotEmpty(tzgw)){
				zhiBanJiLu.setTzgw(tzgw);
			}
			if(StringUtil.isNotEmpty(jlnr)){
				zhiBanJiLu.setJlnr(jlnr);
			}
			zhiBanJiLu.setJllb("通知");
			zhiBanJiLu.setJlr(jlr);
			zhiBanJiLuManager.save(zhiBanJiLu);
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 导出Excel动作
	 * @return
	 * @throws Exception
	 */
	public String exportExcel() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = request.getParameter("paraHeader");
		
		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");
		
		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		
		//岗位类别
		String gwlb = request.getParameter("gwlb");
		//记录类别
		String jllb = request.getParameter("jllb");
		//值班时间
		String zbsj = request.getParameter("zbsj");
		//值班班次
		String zbbc = request.getParameter("zbbc");
		
		List<ZhiBanJiLu> list = new ArrayList<ZhiBanJiLu>();
		
		if("记事".equals(jllb)){
			String hql = "from ZhiBanJiLu z where z.gwlb=? and z.jllb=? and to_char(z.zbsj,'yyyy-mm-dd')=? and z.zbbc=? order by z.jlsj DESC";
			
			list = zhiBanJiLuManager.find(hql, new Object[]{gwlb,jllb,zbsj,zbbc}); 
			
		}else if("通知".equals(jllb)){
			String hql = "from ZhiBanJiLu z where z.jllb=? order by z.jlsj DESC";
			list = zhiBanJiLuManager.find(hql, new Object[]{jllb});
			
			//根据登录人得岗位类别 判断是否显示通知消息
			List<ZhiBanJiLu> resultList = new ArrayList<ZhiBanJiLu>();
			for(ZhiBanJiLu zhiBanJiLu : list){
				String[] gwlbArray = zhiBanJiLu.getTzgw().split(",");
				for (int i = 0; i < gwlbArray.length; i++) {
					if(gwlbArray[i].equals(gwlb)){
						resultList.add(zhiBanJiLu);
					}
				}
			}
			
			list = resultList;
		}else if("query".equals(jllb)){
			list = zhiBanJiLuManager.getAll();
		}
		
		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	
	/**
	 * 根据值班班次、值班时间获取审阅信息
	 * @return
	 */
	public String findShenYueAction(){
		String zbsj = request.getParameter("zbsj");
		String zbbc = request.getParameter("zbbc");
		String gwlb = request.getParameter("gwlb");
		if("1".equals(gwlb)){
			gwlb = "#1机长";
		}else if("2".equals(gwlb)){
			gwlb = "#2机长";
		}else if("3".equals(gwlb)){
			gwlb = "#3机长";
		}else if("4".equals(gwlb)){
			gwlb = "#4机长";
		}
		
		String hql = "from ZhiBanJiLu t where t.jllb='审阅' and to_char(t.zbsj,'yyyy-MM-dd')=? and t.zbbc=? and t.gwlb=? order by jlsj desc";
		List<ZhiBanJiLu> list = zhiBanJiLuManager.find(hql, new Object[]{zbsj,zbbc,gwlb});
		
		String jlr = "";
		if(list.size() > 0){
			jlr = list.get(0).getJlr();
		}
		
		try {
			this.outputJson("{success:true,jlr:'" + jlr + "'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 *  返回 值长 预备值长 角色下的所有用户 
	 *  判断登陆用户是否存在当中
	 *  如果存在 则进行解锁操作
	 * @return
	 */
	public String unLockOPAction()throws Exception{
		String userName=request.getParameter("userLoginName");
		String userPwd=request.getParameter("userPassword");
		String gwlb = request.getParameter("gwlb");
		StringBuffer sqlBf = new StringBuffer("select t.id from tb_sys_role t ");
		if("1".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'jz_1' ");
		}else if("2".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'jz_2' ");
		}else if("3".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'jz_3' ");
		}else if("4".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'jz_4' ");
		}else if("值长".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'ZZ' or t.role_sn = 'YB_ZZ' ");
		}else if("零米".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'lm_zbjl' ");
		}else if("zbtz".equals(gwlb)){
			sqlBf.append(" where t.role_sn = 'zbtz'");
		}else{
			sqlBf.append(" where t.role_sn = 'dq_zbjl'");
		}
		sqlBf.append(" or t.role_sn = 'xyzr'");
		String rolIds = zhiBanJiLuManager.findRolIds(sqlBf.toString());;
		Collection<Person> perList = personManager.findPersonByRoleIds(rolIds.substring(0,rolIds.length()-1));
		Person person = personManager.findPersonByLoginNameAndPwd(userName, userPwd);
		if(person!=null){
			if(perList.contains(person)){
				this.outputJson("{success:true,username:'"+person.getUserName()+"'}");
			}else{
				this.outputJson("{success:false}");
			} 
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
}
