package com.jteap.codegen.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jteap.codegen.util.DatabaseDataTypesUtils;
import com.jteap.codegen.util.JavaDataTypesUtils;
import com.jteap.codegen.util.StringHelper;


/**
 * 
 * @author tantyou
 */
public class Column {
	/**
	 * Reference to the containing table
	 */
	private final EFormModel _eformModel;

	/**
	 * The java.sql.Types type
	 */
	private final int _sqlType;

	/**
	 * The sql typename. provided by JDBC driver
	 */
	private final String _sqlTypeName;

	/**
	 * The name of the column
	 */
	private final String _sqlName;

	/**
	 * True if the column is a primary key
	 */
	private boolean _isPk;

	/**
	 * True if the column is a foreign key
	 */
	private boolean _isFk;

	/**
	 * @todo-javadoc Describe the column
	 */
	private final int _size;

	/**
	 * @todo-javadoc Describe the column
	 */
	private final int _decimalDigits;

	/**
	 * True if the column is nullable
	 */
	private final boolean _isNullable;

	/**
	 * True if the column is indexed
	 */
	private final boolean _isIndexed;

	/**
	 * True if the column is unique
	 */
	private final boolean _isUnique;

	/**
	 * Null if the DB reports no default value
	 */
	private final String _defaultValue;
	
	private final String _remark;

	public String getRemark(){
		return _remark;
	}
	
	public String getChineseName(){
		if(this._remark != null && !this._remark.equals("")){
			return this._remark;
		}else{
			return this.getColumnNameLower();
		}
	}

	/**
	 * Get static reference to Log4J Logger
	 */
	private static Log _log = LogFactory.getLog(Column.class);

	// String description;
	//
	// String humanName;
	//
	// int order;
	//
	// boolean isHtmlHidden;
	//
	// String validateString;

	/**
	 * Describe what the DbColumn constructor does
	 * 
	 * @param table
	 *            Describe what the parameter does
	 * @param sqlType
	 *            Describe what the parameter does
	 * @param sqlTypeName
	 *            Describe what the parameter does
	 * @param sqlName
	 *            Describe what the parameter does
	 * @param size
	 *            Describe what the parameter does
	 * @param decimalDigits
	 *            Describe what the parameter does
	 * @param isPk
	 *            Describe what the parameter does
	 * @param isNullable
	 *            Describe what the parameter does
	 * @param isIndexed
	 *            Describe what the parameter does
	 * @param defaultValue
	 *            Describe what the parameter does
	 * @param isUnique
	 *            Describe what the parameter does
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for constructor
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public Column(EFormModel eFormModel, int sqlType, String sqlTypeName, String sqlName,
			int size, int decimalDigits, boolean isPk, boolean isFk,
			boolean isNullable, boolean isIndexed, boolean isUnique,
			String defaultValue,String remark) {
		_eformModel = eFormModel;
		_sqlType = sqlType;
		_sqlName = sqlName;
		_sqlTypeName = sqlTypeName;
		_size = size;
		_decimalDigits = decimalDigits;
		_isPk = isPk;
		_isFk = isFk;
		_isNullable = isNullable;
		_isIndexed = isIndexed;
		_isUnique = isUnique;
		_defaultValue = defaultValue;
		_remark = remark;

		_log.debug(sqlName + " isPk -> " + _isPk);

	}

	/**
	 * Gets the SqlType attribute of the Column object
	 * 
	 * @return The SqlType value
	 */
	public int getSqlType() {
		return _sqlType;
	}

	/**
	 * Gets the Table attribute of the DbColumn object
	 * 
	 * @return The Table value
	 */
	public EFormModel getEFormModel() {
		return _eformModel;
	}

	/**
	 * Gets the Size attribute of the DbColumn object
	 * 
	 * @return The Size value
	 */
	public int getSize() {
		return _size;
	}

	/**
	 * Gets the DecimalDigits attribute of the DbColumn object
	 * 
	 * @return The DecimalDigits value
	 */
	public int getDecimalDigits() {
		return _decimalDigits;
	}

	/**
	 * Gets the SqlTypeName attribute of the Column object
	 * 
	 * @return The SqlTypeName value
	 */
	public String getSqlTypeName() {
		return _sqlTypeName;
	}

	/**
	 * Gets the SqlName attribute of the Column object
	 * 
	 * @return The SqlName value
	 */
	public String getSqlName() {
		return _sqlName;
	}

	/**
	 * Gets the Pk attribute of the Column object
	 * 
	 * @return The Pk value
	 */
	public boolean isPk() {
		return _isPk;
	}

	/**
	 * Gets the Fk attribute of the Column object
	 * 
	 * @return The Fk value
	 */
	public boolean isFk() {
		return _isFk;
	}

	/**
	 * Gets the Nullable attribute of the Column object
	 * 
	 * @return The Nullable value
	 */
	public final boolean isNullable() {
		return _isNullable;
	}

	/**
	 * Gets the Indexed attribute of the DbColumn object
	 * 
	 * @return The Indexed value
	 */
	public final boolean isIndexed() {
		return _isIndexed;
	}

	/**
	 * Gets the Unique attribute of the DbColumn object
	 * 
	 * @return The Unique value
	 */
	public boolean isUnique() {
		return _isUnique;
	}

	/**
	 * Gets the DefaultValue attribute of the DbColumn object
	 * 
	 * @return The DefaultValue value
	 */
	public final String getDefaultValue() {
		return _defaultValue;
	}

	
	/**
	 * Describe what the method does
	 * 
	 * @param o
	 *            Describe what the parameter does
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for return value
	 */
	public boolean equals(Object o) {
		// we can compare by identity, since there won't be dupes
		return this == o;
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for return value
	 */
	public String toString() {
		return getSqlName();
	}


	/**
	 * Sets the Pk attribute of the DbColumn object
	 * 
	 * @param flag
	 *            The new Pk value
	 */
	void setFk(boolean flag) {
		_isFk = flag;
	}

	public String getColumnName() {
		return StringHelper.makeAllWordFirstLetterUpperCase(getSqlName());
	}

	public String getColumnNameLower() {
		
		return StringHelper.uncapitalize(getColumnName());
	}

	public boolean getIsNotIdOrVersionField() {
		return !isPk();
	}

	public String getValidateString() {
		String result = getNoRequiredValidateString();
		if (!isNullable()) {
			result = "required " + result;
		}
		return result;
	}

	public String getNoRequiredValidateString() {
		String result = "";
		if (getSqlName().indexOf("mail") >= 0) {
			result += "validate-email ";
		}
		if (DatabaseDataTypesUtils.isFloatNumber(getSqlType(), getSize(),
				getDecimalDigits())) {
			result += "validate-number ";
		}
		if (DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(),
				getDecimalDigits())) {
			result += "validate-integer ";
		}
		// if(DatabaseDataTypesUtils.isDate(getSqlType(), getSize(),
		// getDecimalDigits())) {
		// result += "validate-date ";
		// }
		return result;
	}

	public boolean getIsDateTimeColumn() {
		return DatabaseDataTypesUtils.isDate(getSqlType(), getSize(),
				getDecimalDigits());
	}

	public String getJavaType() {
//		System.out.println(this.getColumnName());
		return DatabaseDataTypesUtils.getPreferredJavaType(getSqlType(),
				getSize(), getDecimalDigits());
	}

	public String getAsType() {
		String javaType = getJavaType();
		return JavaDataTypesUtils.getPreferredAsType(javaType);
	}
}
