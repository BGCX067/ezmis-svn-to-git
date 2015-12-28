package com.jteap.wz.yhdgl.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.manager.CrkrzmxManager;
import com.jteap.wz.crkrzgl.model.Crkrzgl;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;
import com.jteap.wz.yhdgl.manager.YhdJdbcManager;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class YhdglSelectAction extends AbstractAction {
	private YhdglManager yhdglManager;
	private YhdmxManager yhdmxManager;
	private XqjhsqManager xqjhsqManager;
	private PersonManager personManager;
	private GroupManager groupManager;
	private P2GManager pgManager;

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public P2GManager getPgManager() {
		return pgManager;
	}

	public void setPgManager(P2GManager pgManager) {
		this.pgManager = pgManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		try {
			this.isUseQueryCache = false;
			String bljhsqid = (String) request.getParameter("bljhsqid");
			//type【1】表示借料单选择，【2】表示补料计划申请选择
			String type = (String) request.getParameter("type");
			//zt【0】表示未生效，【2】表示已经生效，这里应该都是待验收生效状态【2】或【1】表示正式入库已生效
			String zt = (String) request.getParameter("zt");
			if(StringUtil.isNotEmpty(type)){
				if(("1").equals(type)){
					//当前登录人所属班组
					String currentGroupName = sessionAttrs.get(
							Constants.SESSION_CURRENT_GROUP_NAME).toString();
					HqlUtil.addCondition(hql, "bz", currentGroupName);
					//flag表示区分是正常入库，还是自由入库（正常入库：1；自由入库：2）
					HqlUtil.addCondition(hql, "flag", "2");
					HqlUtil.addCondition(hql, "zt", zt);
					HqlUtil.addCondition(hql, "zt", "1", HqlUtil.LOGIC_OR);
					//jlzt【0】表示没有选择过的验货单；【1】表示已经选择过的验货单
					HqlUtil.addCondition(hql, "jlzt","0"); 
				}else if(("2").equals(type)){
					Xqjhsq bljhsq = xqjhsqManager.get(bljhsqid);
					String yhdid = bljhsq.getYhdid();
					HqlUtil.addCondition(hql, "zt", zt);
					HqlUtil.addCondition(hql, "zt", "1", HqlUtil.LOGIC_OR);
					HqlUtil.addCondition(hql, "id", yhdid);
					HqlUtil.addCondition(hql, "flag", "2");
					//blzt【0】表示没有选择过的验货单；【1】表示已经选择过的验货单
					HqlUtil.addCondition(hql, "blzt","0");
				}
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "bh", "desc");
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return yhdglManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "ysrq", "ghdw", "cgy", "bz", "flag", "personCgy",
				"personBgy", "id", "userName", "zt", "id", "bgy", "bh", "htbh",
				"dhrq", "yhdmxs", "cgjhmx", "xqjhDetail", "xqjh", "sqbm",
				"time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ysrq", "ghdw", "cgy", "zt", "id", "bgy", "bh",
				"htbh", "dhrq", "time" };
	}

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}
	
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}

	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
	}

}
