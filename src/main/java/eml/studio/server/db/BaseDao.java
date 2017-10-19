/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.db;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import eml.studio.server.anotation.Password;
import eml.studio.server.anotation.TableField;
import eml.studio.server.constant.Constants;
import eml.studio.server.util.typeparser.StringToTypeParser;


/**
 * Base Data Access Object(DAO)
 */
public class BaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class.getName());
	private static StringToTypeParser parser = StringToTypeParser.newBuilder().build();
	private static Connection conn = getConnection();

	/**
	 * Connect to the database and get connection
	 */
	public static Connection getConnection() {

		try {
			if (conn != null && conn.isValid(Constants.DB_TIMEOUT))
				return conn;

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(Constants.DB_URL,
					Constants.DB_USER,
					Constants.DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;

	}

	/**
	 * Get password
	 * @param pwd
	 * @return
	 */
	public static String password(String pwd) {
		return pwd;
	}

	/**
	 * Get condition expression
	 * @param field
	 * @return expression
	 */
	protected static String getConditionExpression(Field field) {
		String res;
		String name = getTableFieldName(field);
		if ( !field.isAnnotationPresent(Password.class)) {
			res = " and " + name + " = ?";
		} else {
			res = " and " + name + " = password(?)";
		}
		return res;
	}

	/**
	 * Get update expression
	 * @param field
	 * @return expression
	 */
	protected static String getUpdateExpression(Field field) {
		if ( !field.isAnnotationPresent(Password.class)) {
			return " " + getTableFieldName( field ) + " = ?,";
		} else {
			return " " + getTableFieldName( field ) + " = PASSWORD(?),";
		}
	}

	/**
	 * Get insert expression
	 * @param field
	 * @return expression
	 */
	protected static String getInsertExpression(Field field) {
		if ( !field.isAnnotationPresent(Password.class) ) {
			return "?,";
		} else {
			return "PASSWORD(?),";
		}
	}

	/**
	 * Set the corresponding field based on the entered string value
	 *
	 * @param instance object
	 * @param strVal   input value
	 * @param field    input field
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void invoke(Object instance, String strVal, Field field)
			throws IntrospectionException, InvocationTargetException, IllegalAccessException {

		if (strVal == null) strVal = "";

		Object val = parser.parse(strVal, field.getType());

		PropertyDescriptor pd = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
		Method m = pd.getWriteMethod();// get write method
		m.invoke(instance, val);
	}

	/**
	 * Gets the tableField field for all declared objects
	 *
	 * @param clazz class type of input
	 * @return clazz all the field
	 */
	protected static Map<String, Field> getTableFields(Class clazz) {
		Map<String, Field> fieldMap = new HashMap<String, Field>();

		while (clazz != null) {
			Field fields[] = clazz.getDeclaredFields();
			for (Field f : fields) {
				if( !f.isAnnotationPresent(TableField.class) ) continue;

				String name = getTableFieldName( f );

				if (fieldMap.containsKey(name)) continue;
				else{
					fieldMap.put( name , f);
				}
			}

			clazz = clazz.getSuperclass();
		}
		return fieldMap;
	}

	/**
	 * Given a object o class and its Field，get its value
	 * @param o object
	 * @param field field
	 * @return type:string value
	 * @throws Exception
	 */
	public static String toString(Object o, Field field)  throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(field.getName(), o.getClass());
		Method m = pd.getReadMethod();
		Object invoke = m.invoke(o);
		return parser.toString(invoke, field.getType());
	}

	/**
	 * Get table field name
	 * @param f field
	 * @return type:string field name
	 */
	private static String getTableFieldName(Field f){
		String name = f.getAnnotation(TableField.class).name();
		if( "".equals(name) ) name = f.getName();
		return name;
	}
}
