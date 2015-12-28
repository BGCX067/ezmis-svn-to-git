package com.jteap.jx.qxgl.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.qxgl.manager.QxglManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.wfengine.wfi.manager.WorkFlowInstanceManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;

/**
 * 缺陷管理Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings(
{ "unchecked", "serial" })
public class QxglAction extends AbstractAction
{

    private QxglManager qxglManager;
    private PersonManager personManager;
    private DataSource dataSource;
    private WorkFlowInstanceManager workFlowInstanceManager;
    private TaskToDoManager taskToDoManager;
    private DictManager dictManager;
    // 流程操作管理器jbpmOperateManager
    private JbpmOperateManager jbpmOperateManager;
    private WorkFlowLogManager workFlowLogManager;

    /**
     * 
     * 描述 : 查看待处理缺陷单 作者 : wangyun 时间 : Jul 19, 2010
     */
    public String showDclQxdAction() throws Exception
    {
        String jxbm = request.getParameter("jxbm");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        String dictCatalogName = "QXZY_" + jxbm;
        List<Dict> lst = (List<Dict>) dictManager
                .findDictByUniqueCatalogName(dictCatalogName);
        StringBuffer qxzy = new StringBuffer();
        for (Dict dict : lst)
        {
            qxzy.append("'");
            qxzy.append(dict.getValue());
            qxzy.append("',");
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            if ("CURSIGNINNAME".equalsIgnoreCase(sort))
            {
                orderSql = " order by b.CURSIGNIN " + dir;
            }
            else if ("POST_PERSON".equalsIgnoreCase(sort))
            {
                orderSql = " order by b.POST_PERSON " + dir;
            }
            else if ("POST_TIME".equalsIgnoreCase(sort))
            {
                orderSql = " order by b.POST_TIME " + dir;
            }
            else
            {
                orderSql = " order by a." + sort + " " + dir;
            }
        }

        String userLoginName = sessionAttrs.get(
                Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select b.FLOW_TOPIC,");
            sbSql.append("b.ID as TASKTODOID, ");
            sbSql.append("b.FLOW_NAME, ");
            sbSql.append("b.FLOW_INSTANCE_ID, ");
            sbSql.append("b.CURRENT_TASKNAME, ");
            sbSql.append("b.POST_PERSON, ");
            sbSql.append("to_char(b.POST_TIME,'yyyy-MM-dd HH24:mi:ss') as POST_TIME, ");
            sbSql.append("b.TOKEN, ");
            sbSql.append("b.CURSIGNIN, ");
            sbSql.append("a.*, ");
            sbSql.append("d.personname as curSignInName ");
            sbSql.append("from tb_wf_todo b, ");
            sbSql.append("tb_jx_qxgl_qxd a, ");
            sbSql.append("jbpm_variableinstance c, ");
            sbSql.append("tb_sys_person d ");
            sbSql.append("where b.current_process_person like '%");
            sbSql.append(userLoginName);
            sbSql.append("%' ");
            sbSql.append("and b.flag = '1' ");
            sbSql.append("and b.flow_instance_id = c.processinstance_ ");
            sbSql.append("and a.id = c.stringvalue_ ");
            sbSql.append("and c.name_='docid' ");
            sbSql.append("and a.status != '已作废' ");
            sbSql.append("and d.id(+) = b.cursignin ");
            sbSql.append("and a.qxzy in (");
            sbSql.append(qxzy.substring(0, qxzy.lastIndexOf(",")));
            sbSql.append(") ");
            
            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and " + hqlWhereTemp);
            }

            if (StringUtils.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }
            else{
                sbSql.append(" order by a.status,a.fxsj desc");
            }

            String sql = sbSql.toString();
            Page page = qxglManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    new String[]
                    { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
                            "FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
                            "POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
                            "XQBM", "SBMC", "QXMC", "XQSJ", "STATUS", "ID",
                            "QXDBH","curSignInName" });
            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 查看已处理缺陷单 作者 : wangyun 时间 : Jul 19, 2010
     */
    public String showYclQxdAction() throws Exception
    {
        String jxbm = request.getParameter("jxbm");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        String dictCatalogName = "QXZY_" + jxbm;
        List<Dict> lst = (List<Dict>) dictManager
                .findDictByUniqueCatalogName(dictCatalogName);
        StringBuffer qxzy = new StringBuffer();
        for (Dict dict : lst)
        {
            qxzy.append("'");
            qxzy.append(dict.getValue());
            qxzy.append("',");
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            if ("START_".equals(sort) || "END_".equals(sort))
            {
                orderSql = " order by c." + sort + " " + dir;
            }
            else
            {
                orderSql = " order by a." + sort + " " + dir;
            }
        }

        String userLoginName = sessionAttrs.get(
                Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and a.status is not null and a.status != '已作废' ");
            sbSql.append("and e.id = any (select max(id) from tb_wf_log where ");
            sbSql.append("task_login_name ='");
            sbSql.append(userLoginName);
            sbSql.append("' group by pi_id) ");
            sbSql.append("and not exists ");
            sbSql.append("(select 1 from tb_wf_todo f  where a.id=docid and f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"
                            + userLoginName + "%')");
            sbSql.append("and a.qxzy in (");
            sbSql.append(qxzy.substring(0, qxzy.lastIndexOf(",")));
            sbSql.append(") ");

            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and " + hqlWhereTemp);
            }

            if (StringUtils.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }

            String sql = sbSql.toString();
            Page page = qxglManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProperties());

            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 查看草稿箱 作者 : wangyun 时间 : Jul 21, 2010 异常 : Exception
     */
    public String showCgxAction() throws Exception
    {
        String jxbm = request.getParameter("jxbm");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        String dictCatalogName = "QXZY_" + jxbm;
        List<Dict> lst = (List<Dict>) dictManager
                .findDictByUniqueCatalogName(dictCatalogName);
        StringBuffer qxzy = new StringBuffer();
        for (Dict dict : lst)
        {
            qxzy.append("'");
            qxzy.append(dict.getValue());
            qxzy.append("',");
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            if ("START_".equals(sort) || "END_".equals(sort))
            {
                orderSql = " order by c." + sort + " " + dir;
            }
            else
            {
                orderSql = " order by a." + sort + " " + dir;
            }
        }

        String userLoginName = sessionAttrs.get(
                Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and e.task_login_name ='");
            sbSql.append(userLoginName);
            sbSql.append("' ");
            sbSql.append("and a.status is null ");
            sbSql
                    .append("and e.id = any (select max(id) from tb_wf_log group by pi_id) ");
            sbSql.append("and a.qxzy in (");
            sbSql.append(qxzy.substring(0, qxzy.lastIndexOf(",")));
            sbSql.append(") ");

            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and " + hqlWhereTemp);
            }

            if (StringUtils.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }

            String sql = sbSql.toString();
            Page page = qxglManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProperties());

            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 查看全厂 作者 : wangyun 时间 : Jul 21, 2010 异常 : Exception
     */
    public String showQcAction() throws Exception
    {
        String jxbm = request.getParameter("jxbm");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        String dictCatalogName = "QXZY_" + jxbm;
        List<Dict> lst = (List<Dict>) dictManager
                .findDictByUniqueCatalogName(dictCatalogName);
        StringBuffer qxzy = new StringBuffer();
        for (Dict dict : lst)
        {
            qxzy.append("'");
            qxzy.append(dict.getValue());
            qxzy.append("',");
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            if ("START_".equals(sort) || "END_".equals(sort))
            {
                orderSql = " order by c." + sort + " " + dir;
            }
            else
            {
                orderSql = " order by a." + sort + " " + dir;
            }
        }

        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and a.status is not null ");
            sbSql
                    .append("and e.id = any (select max(id) from tb_wf_log group by pi_id) ");
            sbSql.append("and a.qxzy in (");
            sbSql.append(qxzy.substring(0, qxzy.lastIndexOf(",")));
            sbSql.append(") ");

            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and " + hqlWhereTemp);
            }

            if (StringUtils.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }

            String sql = sbSql.toString();
            Page page = qxglManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProperties());

            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 查看作废 作者 : wangyun 时间 : Jul 19, 2010
     */
    public String showZfQxdAction() throws Exception
    {
        String jxbm = request.getParameter("jxbm");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");

        String dictCatalogName = "QXZY_" + jxbm;
        List<Dict> lst = (List<Dict>) dictManager
                .findDictByUniqueCatalogName(dictCatalogName);
        StringBuffer qxzy = new StringBuffer();
        for (Dict dict : lst)
        {
            qxzy.append("'");
            qxzy.append(dict.getValue());
            qxzy.append("',");
        }

        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "1";

        int iStart = Integer.parseInt(start);
        int iLimit = Integer.parseInt(limit);

        // 排序条件
        String orderSql = "";
        if (StringUtil.isNotEmpty(sort))
        {
            if ("START_".equals(sort) || "END_".equals(sort))
            {
                orderSql = " order by c." + sort + " " + dir;
            }
            else
            {
                orderSql = " order by a." + sort + " " + dir;
            }
        }

        String userLoginName = sessionAttrs.get(
                Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
        try
        {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select a.*,");
            sbSql.append("b.processinstance_,");
            sbSql.append("c.id_,");
            sbSql.append("c.version_,");
            sbSql.append("c.start_,");
            sbSql.append("c.end_,");
            sbSql.append("d.flow_name,");
            sbSql.append("d.id as FLOW_CONFIG_ID,");
            sbSql.append("d.flow_form_id ");
            sbSql.append("from tb_jx_qxgl_qxd a,");
            sbSql.append("jbpm_variableinstance b,");
            sbSql.append("jbpm_processinstance c,");
            sbSql.append("tb_wf_flowconfig d,");
            sbSql.append("tb_wf_log e ");
            sbSql.append("where b.name_ = 'docid' ");
            sbSql.append("and b.stringvalue_ = a.id ");
            sbSql.append("and b.processinstance_ = c.id_ ");
            sbSql.append("and c.processdefinition_ = d.pd_id ");
            sbSql.append("and e.pi_id = c.id_ ");
            sbSql.append("and a.status ='已作废' ");
            sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
            sbSql.append("task_login_name ='");
            sbSql.append(userLoginName);
            sbSql.append("' group by pi_id) ");
            sbSql.append("and a.qxzy in (");
            sbSql.append(qxzy.substring(0, qxzy.lastIndexOf(",")));
            sbSql.append(") ");

            String sqlWhere = request.getParameter("queryParamsSql");
            if (StringUtils.isNotEmpty(sqlWhere))
            {
                String hqlWhereTemp = sqlWhere.replace("$", "%");
                sbSql.append(" and " + hqlWhereTemp);
            }

            if (StringUtils.isNotEmpty(orderSql))
            {
                sbSql.append(orderSql);
            }
            String sql = sbSql.toString();
            Page page = qxglManager.pagedQueryTableData(sql, iStart, iLimit);

            String json = JSONUtil.listToJson((List) page.getResult(),
                    listJsonProperties());

            json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
                    + "}";
            this.outputJson(json);
        }
        catch (Exception ex)
        {
            this.outputJson("{success:false}");
            ex.printStackTrace();
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 缺陷单作废 作者 : wangyun 时间 : Jul 28, 2010 异常 : Exception
     */
    public String cancelQxdAction() throws Exception
    {
        String id = request.getParameter("id");
        String pid = request.getParameter("pid");
        Connection conn = null;
        try
        {
            conn = this.dataSource.getConnection();
            String sql = "update TB_JX_QXGL_QXD t set t.status = '已作废' where t.id ='"
                    + id + "'";
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();

            List<TaskToDo> lstTaskToDo = taskToDoManager.findBy("flowInstance",
                    pid);
            for (TaskToDo taskToDo : lstTaskToDo)
            {
                taskToDo.setFlag(false);
                taskToDoManager.save(taskToDo);
            }

            this.outputJson("{success:true}");
        }
        catch (Exception e)
        {
            this.outputJson("{success:false}");
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return NONE;
    }

    /**
     * 
     * 描述 : 专业转发 作者 : wangyun 时间 : Jul 29, 2010 异常 : Exception
     */
    public String zyzfAction() throws Exception
    {
        String qxdbh = request.getParameter("qxdbh");
        String zrbm = request.getParameter("zrbm");
        String zfValue = request.getParameter("zfValue");

        StringBuffer updateStr = new StringBuffer();
        updateStr.append(zrbm).append("转发给").append(zfValue);

        String userLoginName = (String) sessionAttrs
                .get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
        Connection conn = null;
        
        try
        {
            conn = dataSource.getConnection();
            String sql = "select id from TB_JX_QXGL_QXD t where t.qxdbh='"
                    + qxdbh + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String id = "";
            String pid = "";
            while (rs.next())
            {
                id = rs.getString(1);
            }
            Map map = new HashMap();
            map.put("ZRBM", zfValue);
            
            workFlowInstanceManager.changeTaskPersonByDocData(map, id,
                    userLoginName);
            sql = "select PROCESSINSTANCE_ from JBPM_VARIABLEINSTANCE where NAME_='docid' and STRINGVALUE_='"+id+"'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
           while(rs.next()){
               pid = rs.getString(1);
           }
           // 得到当前流程实例和任务实例
           ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
           TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi.getId(), userLoginName);
           Task task = ti.getTask();
           // 得到新的处理人
           String curNodePerson = jbpmOperateManager.getActorIdsByTask(task, ti);
           String nextTaksActor=changeToChineseName(curNodePerson);
           String taskActor=sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();;
           String taskActorLoginName=sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
           //记录处理日志
           workFlowLogManager.addFlowLog(Long.valueOf(pid), "检修班消缺", taskActor, taskActorLoginName, nextTaksActor, "检修班消缺", updateStr.toString(),"");
           
           updateStr.append("于：").append(DateUtils.formatDate(new Date()));
           sql = "update TB_JX_QXGL_QXD t set t.ZRBM='" + zfValue
                    + "',t.jxbqrjg='" + updateStr.toString()
                    + "' where t.qxdbh='" + qxdbh + "'";
           st.executeUpdate(sql);
           st.close();
           outputJson("{success:true}");
        }
        catch (Exception e)
        {
            outputJson("{success:false}");
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return NONE;
    }
    /**
     * 将登陆名称转换为用户中文名
     * @param nextActors 要转换的处理人字符串(例如:0001,0002~0003,0004)
     * @return
     */
    private String changeToChineseName(String userLoginNames){
        String nextActor = userLoginNames;
        if (StringUtil.isNotEmpty(userLoginNames)) {
            String tempActor = "";
            String[] nextTasksActorTemp = userLoginNames.split("~");
            for(int j = 0;j < nextTasksActorTemp.length;j++){
                String[] nextTasksActorArray = nextTasksActorTemp[j].split(",");
                for (int i = 0; i < nextTasksActorArray.length; i++) {
                    if (nextTasksActorArray[i]
                            .equals(Constants.ADMINISTRATOR_ACCOUNT)) {
                        tempActor += Constants.ADMINISTRATOR_NAME+",";
                    } else {
                        tempActor += personManager.findPersonByLoginName(
                                nextTasksActorArray[i]).getUserName()
                                + ",";
                    }
                }
            }
            nextActor = tempActor.substring(0, tempActor.length() - 1);
        }
        return nextActor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HibernateEntityDao getManager()
    {
        return null;
    }

    @Override
    public String[] listJsonProperties()
    {
        return new String[]
        { "ID_", "VERSION_", "START_", "END_", "PROCESSINSTANCE_", "FLOW_NAME",
                "FLOW_CONFIG_ID", "FLOW_FORM_ID", "QXZY", "SBMC", "QXMC",
                "STATUS", "ID" };
    }

    @Override
    public String[] updateJsonProperties()
    {
        return null;
    }

    public QxglManager getQxglManager()
    {
        return qxglManager;
    }

    public void setQxglManager(QxglManager qxglManager)
    {
        this.qxglManager = qxglManager;
    }

    public PersonManager getPersonManager()
    {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager)
    {
        this.personManager = personManager;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public WorkFlowInstanceManager getWorkFlowInstanceManager()
    {
        return workFlowInstanceManager;
    }

    public void setWorkFlowInstanceManager(
            WorkFlowInstanceManager workFlowInstanceManager)
    {
        this.workFlowInstanceManager = workFlowInstanceManager;
    }

    public TaskToDoManager getTaskToDoManager()
    {
        return taskToDoManager;
    }

    public void setTaskToDoManager(TaskToDoManager taskToDoManager)
    {
        this.taskToDoManager = taskToDoManager;
    }

    public DictManager getDictManager()
    {
        return dictManager;
    }

    public void setDictManager(DictManager dictManager)
    {
        this.dictManager = dictManager;
    }

    public JbpmOperateManager getJbpmOperateManager()
    {
        return jbpmOperateManager;
    }

    public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager)
    {
        this.jbpmOperateManager = jbpmOperateManager;
    }

    public WorkFlowLogManager getWorkFlowLogManager()
    {
        return workFlowLogManager;
    }

    public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager)
    {
        this.workFlowLogManager = workFlowLogManager;
    }

}
