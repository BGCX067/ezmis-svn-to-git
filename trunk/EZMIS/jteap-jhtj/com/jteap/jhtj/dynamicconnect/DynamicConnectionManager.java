
package com.jteap.jhtj.dynamicconnect;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.sql.*;
import java.util.StringTokenizer;
@SuppressWarnings({ "unchecked", "serial","unused" })
public class DynamicConnectionManager  extends AbstractObject{
    private Vector dbCMCollection; //动态数据库连接管理集合
    private DBConnectionManager dbDefaultCM; //缺省数据库连接管理
    public Vector drivers;
    
    private static DynamicConnectionManager instance = null;
    private static int client;
    private AppConfig appConfig=AppConfig.getInstance();
    
    
    public synchronized static DynamicConnectionManager getInstance() {

        if (instance == null) {
            instance = new DynamicConnectionManager();
        }
        client++;
        return (instance);
    } //create an instance of connection manager. if exits ,just returen the instance
    
    
    private DynamicConnectionManager() {
        try{
            drivers = new Vector();
            dbCMCollection = new Vector();
            addDefaultDB("");
        }catch(Exception ex){
                //ex.printStackTrace();
        	System.out.println("初始化连接池失败!");
        }
        try {
            jbInit();
        } catch (Exception ex) {
            //ex.printStackTrace();
        	System.out.println("初始化连接池失败!");
        }
    }

    public void addDefaultDB(String propFlag) {
    	 //添加缺省数据库连接管理对象        
        String sDriver = "";
        String sDbUrl = "";
        String sUser = "";
        String sUserPwd = "";
        int iMaxConn = 20;
        try {
            sDriver =appConfig.getConDriverName();
            sDbUrl = appConfig.getConUrl();
            
            sUser =appConfig.getConUserName();
            
            sUserPwd =appConfig.getConUserPwd();

            iMaxConn = Integer.valueOf(appConfig.getMaxConnection()).intValue();
        } catch (Exception ex) {
            System.out.println("Miss Resource File " + ex.getMessage());
        }
        try{

            dbDefaultCM = new DBConnectionManager(sDriver, sDbUrl, sUser,
                                                  sUserPwd,
                                                  iMaxConn);
            loadDriver(sDriver);
        }catch(Exception ex){
            //ex.printStackTrace();
        	System.out.println("添加默认连接失败!");
        }
    }

    public void addConnectionManager(String sDriver, String sDbUrl,
                                     String sUser, String sUserPwd,
                                     int iMaxConn) {
    	//添加动态数据库连接管理对象
        //并放入集合中管理
        Enumeration allDbCM = dbCMCollection.elements();
        boolean isExist = false;
        while (allDbCM.hasMoreElements()) {
            try {
                DBConnectionManager dbCM = (DBConnectionManager) allDbCM.
                                           nextElement();
                if (dbCM.compareTo(sDbUrl, sUser)) {
                    isExist = true;
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            	System.out.println("添加连接失败!");
            }
        }
        if (isExist == false) {

            DBConnectionManager dbCM = new DBConnectionManager(sDriver, sDbUrl,
                    sUser, sUserPwd, iMaxConn);
            dbCMCollection.addElement(dbCM);
            loadDriver(sDriver);
        }
    }

    public Connection getDBConnection() {
        return (dbDefaultCM.getDBConnection());
    } //act as facade

    public Connection getDBConnection(long timeout) {
        return (dbDefaultCM.getDBConnection(timeout));
    } //act as facade

    public Connection getDBConnection(String sDriver, String sDbUrl,
                                      String sDbUser) {
    	//返回数据连接
        Enumeration allDbCM = dbCMCollection.elements();
        java.sql.Connection conn = null;
        boolean isExist = false;
        while (allDbCM.hasMoreElements()) {
            try {
                DBConnectionManager dbCM = (DBConnectionManager) allDbCM.
                                           nextElement();
                if (dbCM.compareTo(sDbUrl, sDbUser)) {
                    conn = dbCM.getDBConnection();
                    isExist = true;
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            	System.out.println("得到连接失败!");
            }
        }
        return (conn);
    } //act as facade


    public Connection getDBConnection(String sDriver, String sDbUrl,
                                      String sDbUser, long timeout) {
    	//返回数据连接,传递参数包括延迟时间
        Enumeration allDbCM = dbCMCollection.elements();
        java.sql.Connection conn = null;
        boolean isExist = false;
        while (allDbCM.hasMoreElements()) {
            try {
                DBConnectionManager dbCM = (DBConnectionManager) allDbCM.
                                           nextElement();
                if (dbCM.compareTo(sDbUrl, sDbUser)) {
                    conn = dbCM.getDBConnection(timeout);
                    isExist = true;
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            	System.out.println("得到连接失败!");
            }
        }
        return (conn);
    } //act as facade
    
    public Connection getDBConnection(String sDs){
    	DynamicConnectionManager dcm=DynamicConnectionManager.getInstance();
    	Connection conn=dcm.getDBConnection();
    	//AppConfig appConfig=AppConfig.getAppConfig();
    	
    	Connection resultConn=null;
    	String sSql="SELECT * FROM ctDataSource WHERE Name='"+sDs+"'";
    	try {
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(sSql);
			while(rs.next()){
				String sDbType=rs.getString("DbType");
				String sConnStr=rs.getString("ConnectStr");
				String sUserName=rs.getString("DbUser");
				String sUserPwd=rs.getString("DbUserPwd");
				resultConn=dcm.getDBConnection(sDbType,sConnStr,sUserName);
				if(resultConn==null){
					String sMaxConnection=appConfig.getMaxConnection();
					sMaxConnection=sMaxConnection==null||sMaxConnection.equals("")?"100":sMaxConnection;					
					dcm.addConnectionManager(sDbType,sConnStr,sUserName,sUserPwd,Integer.parseInt(sMaxConnection));
					resultConn=dcm.getDBConnection(sDbType,sConnStr,sUserName);	
				}
				break;
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO �Զ���� catch ��
			//e.printStackTrace();
			System.out.println("得到连接失败!");
		}    	
    	return resultConn;
    }
    
    private void loadDriver(String sDbDriver) {
        StringTokenizer st = new StringTokenizer(sDbDriver);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();

            try {
                Driver driver = (Driver) Class.forName(driverClassName).
                                newInstance();
                if (drivers.contains(driver)) {

                } else {
                    DriverManager.registerDriver(driver);
                    drivers.addElement(driver);
                }

            } catch (Exception ex) {
                //ex.printStackTrace();
            	System.out.println("加载驱动失败!");
            }
        }
    } //parse the file, load mutil driver class in

    public void freeDBConnection(Connection conn,String url,String username) throws Exception {
        if (conn == null) {
            System.out.print("freeDBConnnection is fail!connection is null!");
        } else {
            DatabaseMetaData dbmd1 = conn.getMetaData();
            //String sDbUrl = dbmd1.getURL();
            //String sDbUser = dbmd1.getUserName();
            
            //暂时隐起来，可以出现错误
//          if (dbDefaultCM.compareTo(url, username)) {
//              //在缺省数据连接管理对象查找
//              dbDefaultCM.freeDBConnection(conn);
//          }
          //在动态数据连接管理对象查找
            Enumeration allDbCM = dbCMCollection.elements();
            boolean isExist = false;
            while (allDbCM.hasMoreElements()) {
                try {
                    DBConnectionManager dbCM = (DBConnectionManager)allDbCM.nextElement();
                    if (dbCM.compareTo(url, username)) {
                        dbCM.freeDBConnection(conn);
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                	System.out.println("释放连接失败!");
                }

            }
        }
    }

    public synchronized int getClientCount() {
        return (client);
    }

    public void realse() {
        Enumeration allDbCM = dbCMCollection.elements();
        while (allDbCM.hasMoreElements()) {
            try {
                DBConnectionManager dbCM = (DBConnectionManager) allDbCM.
                                           nextElement();
                dbCM.realse();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }

        Enumeration enumera = drivers.elements();
        while (enumera.hasMoreElements()) {
            Driver driver = (Driver) enumera.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (Exception ex) {
                System.out.println("Can not deregister driver " +
                                   driver.getClass().getName());
            }
        }

        //dbCMCollection.removeAllElements();
    } //act as facade then de register driver

    private void jbInit() throws Exception {
    }
}
