package com.jteap.jhtj.ljydy.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

/**
 * 
 *描述：应用系统连接源
 *时间：2010-3-31
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_APPSYSTEM")
public class AppSystemConn{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="SID")
	@Type(type="string")
	private String id;
	@Column(name="SNAME")
	private String name; // 应用系统名称
	@Column(name="dbType")
	private Long dbType; // 数据库类型,1-SQL-Server、2-Oracle、3-SyBase、4-DB2
	@Column(name="server")
	private String server; // 服务器名称或者IP地址
	@Column(name="SOURCE")
	private String dbName; // 数据库名称
	@Column(name="userId")
	private String userId; // 用户名
	@Column(name="PWD")
	private String userPwd; // 密码
	@Column(name="port")
	private String port; // 连接端口
	@Column(name="ADOSTR")
	private String url; // 连接字符串
	@Column(name="DRIVERNAME")
	private String className; // 驱动类名
	
	@OneToMany(mappedBy="system")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="sid")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<AppSystemField> appSystemFields;
	
	@Column(name="sortno")
	private Integer sortno;
	/**
	 * 数据库类型
	 */
	public static final String CONN_TYPE_ORACLE = "Oracle 9i";

	public static final String CONN_TYPE_MSSQLSERVER = "Microsoft SQL Server 2000";

	public static final String CONN_TYPE_MYSQL = "MySQL";

	public static final String CONN_TYPE_SYBASE = "Sybase";

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDbName() {
		return dbName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Long getDbType() {
		return dbType;
	}

	public void setDbType(Long dbType) {
		this.dbType = dbType;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String toString() {
		return "[应用系统连接源]" + name + "：" + url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public Set<AppSystemField> getAppSystemFields() {
		return appSystemFields;
	}

	public void setAppSystemFields(Set<AppSystemField> appSystemFields) {
		this.appSystemFields = appSystemFields;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	/**
	 * 根据当前信息取得数据库连接对象
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection1() throws SQLException {
		Connection conn = null;
		try {
			Class.forName(this.className);

			conn = DriverManager.getConnection(url, userId, userPwd);
		} catch (ClassNotFoundException e) {
			//conn=null;
			//e.printStackTrace();
		} catch (SQLException e) {
			//conn=null;
			//e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return conn;
	}
}
