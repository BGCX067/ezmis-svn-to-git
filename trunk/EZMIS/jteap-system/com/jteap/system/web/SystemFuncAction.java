package com.jteap.system.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Module;
import com.jteap.system.resource.model.Operation;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.Role;
/**
 * 
 *描述：系统功能动作，处理平台相关的功能，比如清空hibernate二级缓存等
 *时间：2010-1-11
 *作者：谭畅
 */
@SuppressWarnings("unchecked")
public class SystemFuncAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private SessionFactory sessionFactory;
	
	
	
	/**
	 *描述：获取系统配置参数动作
	 *时间：2010-1-11
	 *作者：谭畅
	 *参数：无
	 *返回值:页面输出
	 *   {success:1,config:{param:value,param2:value....}}
	 *   {success:1,config:{"YX_PE_2":"20000","YX_PE_5":"1000000","YX_PE_3":"50000","YX_FILE_ILLEGAL_PATTERN":"bat,chm,exe,com,js,vbs,html,htm,inf","YX_DESTORY_CYCLE_PERIOD":"0 59 23 1 * ?","YX_PE_0":"10000","root.password":"888888","YX_DELAY_CYCLE_PERIOD":"0 59 23 1 * ?","YX_DESTORY_BUFFER_TIME":"24","JTEAP_SKIN":"Default","YX_PE_1":"10000","YX_FILE_URL":"E:/YX","YX_PE_4":"5000000"}}
	 *抛出异常：
	 *   @exception Exception
	 */
	public String getSystemConfigAction() throws Exception{
		try{
			SystemConfig.reload();
			Map<String,String> ppMap = SystemConfig.getAllPropertiesInMap();
			String configJson = JSONUtil.mapToJson(ppMap);
			String json = "{success:1,config:"+configJson+"}";
			this.outputJson(json);
		}catch(Exception ex){
			this.outputJson("{success:0}");
			throw new BusinessException(ex);
		}
		return NONE;
	}	
	
	/**
	 * 
	 *描述：保存系统配置
	 *时间：2010-1-11
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *		"{success:1}"
	 *抛出异常：
	 */
	
	public String saveSystemConfigAction() throws Exception{
		Enumeration enumParams = request.getParameterNames();
		Object obj = new Object();
		try{
			//由于需要调用SystemConfig静态方法，针对同一配置文件进行保存操作，需要同步
			synchronized (obj) {
				Map<String,String> propMap = new HashMap<String,String>();
				while(enumParams.hasMoreElements()){
					String paramName = (String) enumParams.nextElement();
					if(paramName.startsWith("param_")){
						String key = paramName.substring(6);
						String paramValue = request.getParameter(paramName);
						propMap.put(key, paramValue);
					}
				}
				SystemConfig.setAndSaveProperties(propMap);
				this.outputJson("{success:1}");
			}
		}catch(Exception ex){
			this.outputJson("{success:0,msg:'保存系统配置出现异常'}");
			//throw ex;
		}
		return NONE;
	}

	/**
	 * 清空hibernate二级缓存
	 * @return
	 * @throws Exception
	 */
	public String clearHibernateL2CacheAction() throws Exception{
		sessionFactory.evict(Group.class);
		sessionFactory.evict(Person.class);
		sessionFactory.evict(Role.class);
		sessionFactory.evict(Resource.class);
		sessionFactory.evict(Module.class);
		sessionFactory.evict(Operation.class);
		sessionFactory.evictCollection("com.jteap.system.resource.model.Resource.childRes");
		sessionFactory.evictCollection("com.jteap.system.group.model.Group.childGroups");
		sessionFactory.evictCollection("com.jteap.system.person.model.Person.groups");
		sessionFactory.evictCollection("com.jteap.system.person.model.Person.roles");
		sessionFactory.evictCollection("com.jteap.system.role.model.Role.persons");
		sessionFactory.evictQueries();
		outputJson("{success:true}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：
	 *	系统探测ACTION，当有CS程序需要保证服务器连接状态是否正常时，使用该动作探测
	 *时间：2010-1-29
	 *作者：Administrator
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public String sysDetectAction() throws Exception{
		this.outputJson("{success:1}");
		return NONE;
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}



	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
