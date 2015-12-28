package com.jteap.yx.aqyxfx.manager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.jhtj.yyjkwh.model.TjDllIO;
import com.jteap.yx.aqyxfx.model.Zb;

public class ZhiBiaoShowManager extends HibernateEntityDao<TjDllIO>
{
    private DataSource dataSource;

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * 根据日期查询指标信息 作者:童贝 时间:Feb 23, 2011
     * 
     * @param tableName
     * @param rq
     * @param zb
     * @return
     * @throws SQLException
     */
    public Zb findZbsjByTablenameAndRq(String tableName, String rq, Zb zb)
            throws SQLException
    {
        String nian = DateUtils.getDate("yyyy");
        String yue = DateUtils.getDate("MM");
        if (StringUtils.isNotEmpty(rq))
        {
            nian = DateUtils
                    .getDate(DateUtils.StrToDate(rq, "yyyy-MM"), "yyyy");
            yue = DateUtils.getDate(DateUtils.StrToDate(rq, "yyyy-MM"), "MM");
        }
        yue = Integer.valueOf(yue).toString();
        String sql = "select * from " + tableName + " t where nian=" + nian
                + " and yue=" + yue;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = dataSource.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next())
            {
                Field[] fields = zb.getClass().getDeclaredFields();
                for (Field field : fields)
                {
                    String name = field.getName();
                    System.out.println(name);
                    BeanUtils.setProperty(zb, name, resultSet.getObject(name.toUpperCase()));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            if (statement != null)
            {
                statement.close();
            }
            if (conn != null)
            {
                conn.close();
            }
        }
        return zb;
    }

    /**
     * getList(通过sql语句查找结果)
     * TODO(这里描述这个方法适用条件 – 可选)
     * @param   name
     * @param  @return    List
     * @return String    DOM对象
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
    */
    public List<Map<String, Object>> findList(String sql)
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultset = null;
        try
        {
            conn = dataSource.getConnection();
            statement = conn.createStatement();
            resultset = statement.executeQuery(sql);
            while (resultset.next())
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", resultset.getObject("ID"));
                map.put("TXR", resultset.getObject("TXR"));
                map.put("CXRQ", resultset.getObject("CXRQ"));
                list.add(map);
            }
        }
        catch (Exception e)
        {
            System.err.println("执行：" + sql + "出错");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (resultset != null)
                    resultset.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            }
            catch (Exception e)
            {
                System.err.println("关闭连接出错");
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 保存指标信息 作者:童贝 时间:Feb 23, 2011
     * 
     * @param zb
     * @throws SQLException
     */
    public void saveZb(String tableName, Zb zb, String cxrq)
            throws SQLException
    {
        String id = UUIDGenerator.hibernateUUID();
        zb.setId(id);
        if (StringUtils.isEmpty(cxrq))
        {
            cxrq = DateUtils.getDate("yyyy-MM");
        }
        String fieldSql = "id,cxrq,";
        String valueSql = "'" + id + "','" + cxrq + "',";
        Field[] fields = zb.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            String name = field.getName();
            String value = (String) BeanUtils.getProperty2(zb, name);
            value = value == null ? "" : value;
            fieldSql += name + ",";
            valueSql += "'" + value + "',";
        }
        if (StringUtils.isNotEmpty(fieldSql))
        {
            fieldSql = fieldSql.substring(0, fieldSql.length() - 1);
        }
        if (StringUtils.isNotEmpty(valueSql))
        {
            valueSql = valueSql.substring(0, valueSql.length() - 1);
        }
        String sql = "insert into " + tableName + " (" + fieldSql + ") values("
                + valueSql + ")";
        this.operateDataBase(sql);
    }

    /**
     * 查看数据是否存在 作者:童贝 时间:Feb 23, 2011
     * 
     * @param nian
     * @param yue
     * @return
     * @throws SQLException
     */
    public String isExData(String tableName, String rq) throws SQLException
    {
        String res = null;
        String nian = DateUtils.getDate("yyyy");
        String yue = DateUtils.getDate("MM");
        if (StringUtils.isNotEmpty(rq))
        {
            nian = DateUtils
                    .getDate(DateUtils.StrToDate(rq, "yyyy-MM"), "yyyy");
            yue = DateUtils.getDate(DateUtils.StrToDate(rq, "yyyy-MM"), "MM");
        }
        String sql = "select * from " + tableName + " t where t.NIAN='" + nian
                + "'" + " and " + " t.YUE = '" + yue + "'";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = dataSource.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next())
            {
                res = "exist";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            if (statement != null)
            {
                statement.close();
            }
            if (conn != null)
            {
                conn.close();
            }
        }
        return res;
    }

    /**
     * 操作数据库 作者:童贝 时间:Feb 23, 2011
     * 
     * @param sql
     * @throws SQLException
     */
    public void operateDataBase(String sql) throws SQLException
    {
        Connection conn = null;
        Statement statement = null;
        try
        {
            conn = this.dataSource.getConnection();
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            // e.printStackTrace();
            System.out.println("操作表结构出错");
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
            if (conn != null)
            {
                conn.close();
            }
        }
    }
}
