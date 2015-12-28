package com.jteap.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBUnitUitl {

	static ApplicationContext ctx;
	static DataSource ds ;
	static Connection jdbcConn ;
	static String schema="dbunit";
	static IDatabaseConnection conn ; // oracle
	static{
		 ctx = new ClassPathXmlApplicationContext("./*applicationContext*.xml");
		 ds= (DataSource) ctx.getBean("dataSource");
		 try {
			jdbcConn = ds.getConnection();
			conn= new DatabaseConnection(jdbcConn, "dbunit");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * 导出数据到xml文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportTableToXml() throws SQLException, DataSetException, FileNotFoundException, IOException {
		IDataSet dataSet = conn.createDataSet();
		XmlDataSet.write(dataSet, new FileOutputStream("export-data.xml")); // xml
	}
	
	/**
	 * 导出数据到Excel文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportTableToXls() throws SQLException, DataSetException, FileNotFoundException, IOException{
		IDataSet dataSet = conn.createDataSet();
		XlsDataSet.write(dataSet, new FileOutputStream("export-data.xls"));
	}
	
	/**
	 * 从xml中回复数据
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws DatabaseUnitException
	 */
	public static void resetDataFromXml() throws SQLException, FileNotFoundException, DatabaseUnitException{
		IDataSet dataSet=new XmlDataSet(new FileInputStream("export-data.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
	}
	
	/**
	 * 从excel中恢复数据
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
	public static void resetDataFromXls() throws FileNotFoundException, IOException, DatabaseUnitException, SQLException{
		IDataSet dataSet=new XlsDataSet(new FileInputStream("export-data.xls"));
		DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException, DatabaseUnitException {
		exportTableToXls();
	}
}
