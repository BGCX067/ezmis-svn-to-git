package com.jteap.test.util;

import java.io.File;
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


/**
 * 开始测试之前备份  默认开始之前是不备份的，需要设置bBackupData=true
 * 
 * public void setUp(){
 * 	bBackUpData=true;
 * 	super.setUp();
 * }
 * 
 * 测试之后还原数据
 * 
 * 在使用该测试时，由于调用了applicationcontext中定义的dbschema属性，所以在spring配置文件中必须配置该值
 * eg:
 * 	
    <!-- 数据库Schema 在DBUnit测试中会用到  -->
	<bean id="dbschema" class="java.lang.String">
		<constructor-arg value="dbunit"></constructor-arg>
	</bean>
 * 
 * @author tantyou
 *
 */
public class DBUnitTestCase extends MainTestCase{

	private Connection jdbcConn;			//数据库连接
	private IDatabaseConnection conn;		//dbunit连接
	private String schema="jteap";					//数据库schema
	
	private String xmlFileName;
	private String xlsFileName;
	
	protected boolean bBackupData=false;	//默认情况是不备份元数据库的数据
	
	//指定测试对象的名称，用于区别测试对象生成的备份文件
	protected String getTestObjectName(){
		return this.getClass().getName();
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.xmlFileName="databackup.xml";
		this.xlsFileName="databackup.xls";
		
		DataSource ds=(DataSource) ctx.getBean("dataSource");
		try {
			jdbcConn=ds.getConnection();
			conn= new DatabaseConnection(jdbcConn,schema);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**
		 * 如果存在备份文件就不备份，否则就备份
		 */
		File file=new File(xlsFileName);
		if(file.exists())
			bBackupData=false;
		else
			bBackupData=true;
		
		/**
		 * 开始时备份数据库
		 */
		if(bBackupData)
		exportTableToXls();// or exportTableToXml();
	}

	/**
	 * 导出数据到xml文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exportTableToXml() throws SQLException, DataSetException, FileNotFoundException, IOException {
		String tables[]=this.processTables();
		IDataSet dataSet =(tables==null)? conn.createDataSet():conn.createDataSet(tables);
		XmlDataSet.write(dataSet, new FileOutputStream(xmlFileName)); // xml
	}
	
	/**
	 * 导出数据到Excel文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exportTableToXls() throws SQLException, DataSetException, FileNotFoundException, IOException{
		String tables[]=this.processTables();
		IDataSet dataSet =(tables==null)? conn.createDataSet():conn.createDataSet(tables);
		XlsDataSet.write(dataSet, new FileOutputStream(xlsFileName));
	}
	
	/**
	 * 从xml中回复数据
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws DatabaseUnitException
	 */
	public void resetDataFromXml() throws SQLException, FileNotFoundException, DatabaseUnitException{
		File file=new File(xmlFileName);
		if(file.exists()){
			IDataSet dataSet=new XmlDataSet(new FileInputStream(file));
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
		}

	}
	
	/**
	 * 从excel中恢复数据
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
	public void resetDataFromXls() throws FileNotFoundException, IOException, DatabaseUnitException, SQLException{
		File file=new File(xlsFileName);
		if(file.exists()){
			IDataSet dataSet=new XlsDataSet(new FileInputStream(file));
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
		}
		
	}
	
	/**
	 * 操作完成后还原数据
	 */
	@Override
	protected void tearDown() throws Exception {
		resetDataFromXls();		// or resetDataFromXml();
		super.tearDown();	
		
	}
	
	/**
	 * 提供扩展
	 * 指定当前测试仅仅只涉及到的表名集合
	 * eg:
	 * return new String[]{"table1","table2"};
	 * @return 可返回空值
	 */
	protected String[] processTables(){
		return null;
	}
	
}
