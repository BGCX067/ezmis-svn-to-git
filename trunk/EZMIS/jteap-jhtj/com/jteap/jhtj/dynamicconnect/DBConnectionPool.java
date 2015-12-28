
package com.jteap.jhtj.dynamicconnect;

import java.sql.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings({ "unchecked", "serial" })
public class DBConnectionPool extends AbstractObject{

    private String dbUserName;
    private String dbPassword;
    private String connectionURL;
    private int maxConnection;
    private Vector freeConnections;
    private int checkedOut=0;
    protected Log log = LogFactory.getLog(this.getClass());
    public DBConnectionPool(String userName, String password, String url,
                            int maxConn) {
        this.dbUserName = userName;
        this.dbPassword = password;
        this.connectionURL = url;
        this.maxConnection = maxConn;
        freeConnections = new Vector();
    } // initalize

    public synchronized Connection getDBConnection() {
        Connection conn = null;
        if (freeConnections.size() > 0) {
            conn = (Connection) freeConnections.elementAt(0);
            freeConnections.removeAllElements();
            try {
                if (conn.isClosed()) {
                    conn = getDBConnection();
                }
            } catch (SQLException ex) {
                conn = getDBConnection();
            }
        } else if (maxConnection == 0 || checkedOut < maxConnection) {
            conn = newDBConnection();
        }else if(checkedOut > maxConnection){
        	conn = newDBConnection();
        	freeConnections.removeAllElements();
        	checkedOut=0;
        }else{
        	conn = newDBConnection();
        	freeConnections.removeAllElements();
        	checkedOut=0;
        }

        if (conn != null) {
            checkedOut++;
        }
        log.info("1maxConnection:"+maxConnection+"+checkedOut:"+checkedOut);
        return (conn);
    } // using FIFO method to get connection instance

    public synchronized Connection getDBConnection(long timeout) {
        long startTime = new java.util.Date().getTime();
        Connection conn;
        while ((conn = getDBConnection()) == null) {
            try {
                wait(timeout);
            } catch (InterruptedException ex) {}
            if (new java.util.Date().getTime() - startTime >= timeout) {
                return (null);
            }
        }
        return conn;
    }

    public Connection newDBConnection() {
        Connection conn = null;
        try {
            if (dbUserName == null) {
                conn = DriverManager.getConnection(connectionURL);
            } else {
                conn = DriverManager.getConnection(connectionURL, dbUserName,
                        dbPassword);
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
        	
        }finally{
        	if(conn!=null){
        		this.freeDBConnection(conn);
        	}
        }
        return (conn);
    }

    public synchronized void freeDBConnection(Connection conn) {
    	if(checkedOut>=1){
	        freeConnections.addElement(conn);
	        checkedOut--;
	        log.info("2maxConnection:"+maxConnection+"+checkedOut:"+checkedOut);
	        notifyAll();
    	}
    }

    public synchronized void release() throws SQLException {
        Enumeration allConnections = freeConnections.elements();
        while (allConnections.hasMoreElements()) {
        	Connection conn=null;
            try {
                conn = (Connection) allConnections.nextElement();
            }finally{
            	if(conn!=null){
            		conn.close();
            	}
            }
        }
        freeConnections.removeAllElements();
    }

}
