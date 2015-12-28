package com.jteap.codegen.util;

import java.sql.Types;
import java.util.HashMap;

/**
 * 描述数据库类型和java类型对应关系
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public class DatabaseDataTypesUtils {

	private final static IntStringMap _preferredJavaTypeForSqlType = new IntStringMap();

	public static boolean isFloatNumber(int sqlType, int size, int decimalDigits) {
		String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
		if (javaType.endsWith("float") || javaType.endsWith("double")
				|| javaType.endsWith("double")) {
			return true;
		}
		return false;
	}

	public static boolean isIntegerNumber(int sqlType, int size,
			int decimalDigits) {
		String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
		if (javaType.endsWith("long") || javaType.endsWith("int")
				|| javaType.endsWith("short")) {
			return true;
		}
		return false;
	}

	public static boolean isDate(int sqlType, int size, int decimalDigits) {
		String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
		if (javaType.endsWith("Date") || javaType.endsWith("Timestamp")) {
			return true;
		}
		return false;
	}

	public static String getPreferredJavaType(int sqlType, int size,
			int decimalDigits) {
		if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC)
				&& decimalDigits == 0) {
			if(size < 10) {
				return "int";
			} else {
				return "long";
			}
		}
		String result = _preferredJavaTypeForSqlType.getString(sqlType);
		if (result == null) {
			result = "java.lang.Object";
		}
		return result;
	}

	static {
		_preferredJavaTypeForSqlType.put(Types.TINYINT, "byte");
		_preferredJavaTypeForSqlType.put(Types.SMALLINT, "short");
		_preferredJavaTypeForSqlType.put(Types.INTEGER, "int");
		_preferredJavaTypeForSqlType.put(Types.BIGINT, "long");
		_preferredJavaTypeForSqlType.put(Types.REAL, "double");
		_preferredJavaTypeForSqlType.put(Types.FLOAT, "double");
		_preferredJavaTypeForSqlType.put(Types.DOUBLE, "double");
		_preferredJavaTypeForSqlType.put(Types.DECIMAL, "double");
		_preferredJavaTypeForSqlType.put(Types.NUMERIC, "long");
		_preferredJavaTypeForSqlType.put(Types.BIT, "short");
		_preferredJavaTypeForSqlType.put(Types.CHAR, "java.lang.String");
		_preferredJavaTypeForSqlType.put(Types.VARCHAR, "java.lang.String");
		// according to resultset.gif, we should use java.io.Reader, but String
		// is more convenient for EJB
		_preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "java.lang.String");
		_preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
		_preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
		_preferredJavaTypeForSqlType.put(Types.LONGVARBINARY,
				"byte[]");
		_preferredJavaTypeForSqlType.put(Types.DATE, "java.util.Date");
		_preferredJavaTypeForSqlType.put(Types.TIME, "java.util.Date");
		_preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.util.Date");
		_preferredJavaTypeForSqlType.put(Types.CLOB, "java.lang.String");
		_preferredJavaTypeForSqlType.put(Types.BLOB, "byte[]");
		_preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
		_preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
		_preferredJavaTypeForSqlType.put(Types.STRUCT, "java.lang.Object");
		_preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "java.lang.Object");
	}

	@SuppressWarnings("serial")
	private static class IntStringMap extends HashMap {

		public String getString(int i) {
			return (String) get(new Integer(i));
		}

		public String[] getStrings(int i) {
			return (String[]) get(new Integer(i));
		}

	
		public void put(int i, String s) {
			put(new Integer(i), s);
		}

		public void put(int i, String[] sa) {
			put(new Integer(i), sa);
		}
	}

}
