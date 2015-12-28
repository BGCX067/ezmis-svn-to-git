package com.jteap.jhtj.dynamicconnect;

import java.sql.*;


@SuppressWarnings({ "unchecked", "serial" ,"unused"})
public class DBConnectionManager  extends AbstractObject{
    private DBConnectionPool pool;
    private int client;

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbUserPwd;
    private int maxConn;

    public DBConnectionManager(String sDriver, String sDbUrl, String sUser,
                               String sUserPwd, int iMaxConn) {
        dbDriver = sDriver;
        dbUrl = sDbUrl;
        dbUser = sUser;
        dbUserPwd = sUserPwd;
        maxConn = iMaxConn;
        init();
    }

    void init() {
        createPool();
    } //using Properties.load() method to locate outter properties file

    public boolean compareTo(String sDbUrl, String sUser) {
        boolean bResult = false;
        String sMatUser = "sa";
        if (sUser.equalsIgnoreCase("dbo")) {
            sMatUser = "sa";
        } else {
            sMatUser = sUser;
        }
        if (sDbUrl.equalsIgnoreCase(dbUrl) && sMatUser.equalsIgnoreCase(dbUser)) {
            bResult = true;
        }
        return bResult;
    }


    public void createPool() {
        pool = new DBConnectionPool(dbUser, dbUserPwd, dbUrl, maxConn);
    } //parse the file, load username,password,url and maxconnection in

    public synchronized int getClientCount() {
        return (client);
    }

    public Connection getDBConnection() {
        if (pool != null) {
            return (pool.getDBConnection());
        }
        return (null);
    } //act as facade

    public Connection getDBConnection(long timeout) {
        if (pool != null) {
            return (pool.getDBConnection(timeout));
        }
        return (null);
    } //act as facade

    public void freeDBConnection(Connection conn) {

        if (pool != null) {
            pool.freeDBConnection(conn);
        }
    } //act as facade

    public void realse() throws SQLException {
        if (this.client != 0) {
            return;
        }
        if (pool != null) {
            pool.release();
        }
    } //act as facade then de register driver
    
}
