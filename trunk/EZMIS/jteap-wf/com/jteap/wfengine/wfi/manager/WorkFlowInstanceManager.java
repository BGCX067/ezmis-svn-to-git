package com.jteap.wfengine.wfi.manager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.dao.JdbcEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.util.WFConstants;

public class WorkFlowInstanceManager extends JdbcEntityDao
{

    // 流程操作管理器jbpmOperateManager
    private JbpmOperateManager jbpmOperateManager;
    private TaskToDoManager taskToDoManager;

    /**
     * 根据流程实例查询该流程的第一个节点的名称
     * 
     * @param flowConfigId
     * @return
     * @throws Exception
     */
    public String findStartNodeName(String flowConfigId) throws Exception
    {
        String sql = "SELECT PD_ID FROM TB_WF_FLOWCONFIG WHERE ID = ?";
        Connection conn = null;
        String startName = null;
        try
        {
            conn = dataSource.getConnection();

            // 1.取得流程定义编号
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, flowConfigId);
            ResultSet rs = pst.executeQuery();
            String pd_id = null;
            if (rs.next())
            {
                pd_id = rs.getString(1);
            }
            pst.close();
            rs.close();

            // 2.根据流程定义编号查询开始节点
            sql = "SELECT name_ FROM jbpm_node t WHERE id_ in("
                    + "SELECT to_ FROM jbpm_transition t WHERE t.from_ in"
                    + "(SELECT id_ FROM jbpm_node t WHERE PROCESSDEFINITION_ = '"
                    + pd_id + "' and class_='R')" + ")";

            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next())
            {
                startName = rs.getString(1);
            }
            st.close();
            rs.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            conn.close();
        }
        return startName;
    }

    @SuppressWarnings("unchecked")
    public Page getPage(String sql, int start, int limit)
    {
        Page page = new Page();
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            page = this.pagedQueryTableData(sql, start, limit);
            conn = this.dataSource.getConnection();
            statement = conn.createStatement();
            sql = "select count(*) from (" + sql + ")";
            int pagecount = 0;
            rs = statement.executeQuery(sql);
            if (rs.next())
                pagecount = rs.getInt(1);
            Class c = page.getClass();
            Field field = c.getDeclaredField("totalCount");
            field.setAccessible(true);
            field.set(page, pagecount);
            rs.close();
            statement.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("excute sql error");
            e.printStackTrace();
        }
        return page;
    }

    /**
     * 根据流程实例查询该流程的第一个节点的编号
     * 
     * @param flowConfigId
     * @return
     * @throws Exception
     */
    public int findStartNodeId(String flowConfigId) throws Exception
    {
        String sql = "SELECT PD_ID FROM TB_WF_FLOWCONFIG WHERE ID = ?";
        Connection conn = null;
        int startid = 0;
        try
        {
            conn = dataSource.getConnection();

            // 1.取得流程定义编号
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, flowConfigId);
            ResultSet rs = pst.executeQuery();
            String pd_id = null;
            if (rs.next())
            {
                pd_id = rs.getString(1);
            }
            pst.close();
            rs.close();

            // 2.根据流程定义编号查询开始节点
            sql = "SELECT id_ FROM jbpm_node t WHERE id_ in("
                    + "SELECT to_ FROM jbpm_transition t WHERE t.from_ in"
                    + "(SELECT id_ FROM jbpm_node t WHERE PROCESSDEFINITION_ = '"
                    + pd_id + "' and class_='R')" + ")";

            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next())
            {
                startid = rs.getInt(1);
            }
            st.close();
            rs.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            conn.close();
        }
        return startid;
    }

    public String findNextNodes(int nodeid) throws Exception
    {
        Connection conn = null;
        String nodeName = "";
        try
        {
            conn = dataSource.getConnection();

            // 2.根据流程定义编号查询开始节点
            String sql = "SELECT name_ FROM jbpm_node t WHERE id_ in("
                    + "SELECT to_ FROM jbpm_transition t WHERE t.from_="
                    + nodeid + ")";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                nodeName += rs.getString(1) + "~";
            }
            st.close();
            rs.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            conn.close();
        }
        return nodeName;
    }

    /**
     * 
     * 描述 : 根据业务数据来改变待办任务的处理人（一般是在环节定义中定义了人员、部门或者角色规则，将业务数据放入PI的变量中，重新过滤处理人） 作者 :
     * wangyun 时间 : 2010-9-26 参数 : docKey : 业务数据key docValue : 业务数据value docId :
     * 业务数据ID 返回值 : 异常 :
     */
    @SuppressWarnings(
    { "unchecked" })
    public void changeTaskPersonByDocData(Map map, String docId,
            String userLoginName) throws Exception
    {
        Connection conn = null;
        String token = "";
        String pid = "";
        try
        {
            conn = dataSource.getConnection();

            // 根据docId 查询流程PID和TOKEN
            String sql = "select TOKEN_, PROCESSINSTANCE_ from JBPM_VARIABLEINSTANCE where NAME_='docid' and STRINGVALUE_='"
                    + docId + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                token = rs.getString(1);
                pid = rs.getString(2);
            }
            rs.close();
            st.close();

            // 得到当前流程实例和任务实例
            ProcessInstance pi = jbpmOperateManager.getJbpmTemplate()
                    .findProcessInstance(Long.parseLong(pid));
            TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi
                    .getId(), userLoginName);
            Task task = ti.getTask();
            pi.getContextInstance().setTransientVariable(
                    WFConstants.WF_VAR_LOGINNAME, userLoginName);
            // 将业务数据放入环境变量中
            for (Object key : map.keySet())
            {
                pi.getContextInstance().setVariable((String) key, map.get(key));
            }
            // 得到新的处理人
            String curNodePerson = jbpmOperateManager.getActorIdsByTask(task,
                    ti);
            String[] actors = curNodePerson.split(",");
            ti.setPooledActors(actors);
            // 更新待办任务处理人
            taskToDoManager.changeCurNodePerson(pi.getId(), token,
                    curNodePerson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
    }

    public JbpmOperateManager getJbpmOperateManager()
    {
        return jbpmOperateManager;
    }

    public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager)
    {
        this.jbpmOperateManager = jbpmOperateManager;
    }

    public TaskToDoManager getTaskToDoManager()
    {
        return taskToDoManager;
    }

    public void setTaskToDoManager(TaskToDoManager taskToDoManager)
    {
        this.taskToDoManager = taskToDoManager;
    }

}
