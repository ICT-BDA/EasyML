/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Secure Data Access Object(DAO)
 * DAO provides access to the database through the java object function,
 * input parameters and return values are Java simple class object,
 * API users do not need to build sql statement,
 * DAO will help them to build and implement additions and deletions.
 *
 * It is required that the data object type name and the data table name are the same.
 * The attribute name of the data object and the attribute name of the database must be consistent,
 * and the getter and setter methods are implemented.
 *
 */
public class SecureDao extends BaseDao {
	private static Logger logger = Logger.getLogger(SecureDao.class.getName());

	/**
	 * Database query function
	 * The function uses java reflection mechanism to build sql statement automatically,
	 * and use the query results to build the object automatically
	 *
	 * @param o The value of the object that is filled with the query object
	 *             will be treated as a query condition
	 * @return T result type
	 * @throws Exception
	 */
	public static <T> T getObject(T o) throws Exception {
		return getObject(o, "");
	}

	public static <T> T getObject(Class<T> clazz, String append_expr)
			throws Exception {
		Map<String, Field> fields = getTableFields(clazz);
		StringBuffer sql = new StringBuffer("select ");
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			String name = entry.getKey();
			sql.append(name + ",");
		}

		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(" from	" + clazz.getSimpleName().toLowerCase());
		sql.append(" where 1=1	");
		List<T> list = select(sql.toString(), clazz);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public static <T> T getObject(T o, String append_expr) throws Exception {
		List<T> list = find(o, append_expr);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * Query the database according to the value filled in the input object o as the query condition
	 * and return the object of the corresponding type
	 *
	 * @param obj An object that holds the query
	 * @param append_expr Additional conditional expressions
	 * @return result list
	 * @throws Exception
	 */
	public static <T> List<T> find(T obj, String append_expr) throws Exception {
		Class clazz = obj.getClass();
		Map<String, Field> fields = getTableFields(clazz);
		StringBuffer sql = new StringBuffer("select ");
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			String name = entry.getKey();
			sql.append(name + ",");
		}

		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(" from	" + clazz.getSimpleName().toLowerCase());
		sql.append(" where 1=1	");

		List<String> conds = new LinkedList<String>();
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			Field field = entry.getValue();
			String invoke = toString(obj, field);

			if (invoke != null) {
				conds.add(invoke.toString());
				sql.append(getConditionExpression(field));
			}
		}
		return select(sql.toString() + " " + append_expr, conds, clazz);
	}

	/**
	 * Execute sql prepare statement
	 *
	 * @param clazz type of class
	 * @param prepSql prepare statement
	 * @param parameters parameters of prepare statement
	 * @return result list
	 * @throws Exception
	 */
	private static <T> List<T> select(String prepSql, List<String> parameters,
			Class<T> clazz) throws Exception {

		ResultSet resultSet;
		PreparedStatement pstmt;
		List<T> resultList = new ArrayList<T>();
		pstmt = getConnection().prepareStatement(prepSql.toString());
		logger.info(prepSql.toString());

		int i = 1;
		for (String param : parameters) {
			pstmt.setString(i++, param);
		}

		resultSet = pstmt.executeQuery();

		Map<String, Field> fields = getTableFields(clazz);

		while (resultSet.next()) {
			T instance = clazz.newInstance();
			for (Map.Entry<String, Field> entry : fields.entrySet()) {
				String name = entry.getKey();
				Field field = entry.getValue();
                if(resultSet.getString(name)==null)
                	continue;
				invoke(instance, resultSet.getString(name), field);
			}

			resultList.add(instance);
		}
		return resultList;
	}

	/**
	 * Select T type table and list some records
	 *
	 * @param clazz target class type
	 * @param sql sql statement
	 * @return result list
	 * @throws Exception
	 */
	private static <T> List<T> select(String sql, Class<T> clazz)
			throws Exception {

		ResultSet resultSet;
		PreparedStatement pstmt;
		List<T> resultList = new ArrayList<T>();
		pstmt = getConnection().prepareStatement(sql.toString());
		resultSet = pstmt.executeQuery();

		Map<String, Field> fields = getTableFields(clazz);
		while (resultSet.next()) {
			T instance = clazz.newInstance();
			for (Map.Entry<String, Field> entry : fields.entrySet()) {
				String name = entry.getKey();
				Field field = entry.getValue();
                if(resultSet.getString(name)==null)
                	continue;
				invoke(instance, resultSet.getString(name), field);
			}

			if (instance != null) {
				resultList.add(instance);
			}
		}

		return resultList;
	}

	/**
	 * Consult T type table in date base, return all records of it
	 *
	 * @param object T type object
	 * @return List of T type record
	 * @throws Exception
	 */
	public static <T> List<T> listAll(T object) throws Exception {

		return find(object, "");
	}

	/**
	 * Consult T type table in date base, return all records of it
	 *
	 * @param clazz T class type
	 * @return List of T type record
	 * @throws Exception
	 */
	public static <T> List<T> listAll(Class<T> clazz) throws Exception {
		Map<String, Field> fields = getTableFields(clazz);
		StringBuffer sql = new StringBuffer("select ");
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			String name = entry.getKey();
			sql.append(name + ",");
		}

		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(" from	" + clazz.getSimpleName().toLowerCase());
		sql.append(" where 1=1	");
		return select(sql.toString(), clazz);
	}

	/**
	 * Query T type table in date base, return some records of it
	 *
	 * @param object T type object
	 * @param append_expr limited factors
	 * @return List of T type record
	 * @throws Exception
	 */
	public static <T> List<T> listAll(T object, String append_expr)
			throws Exception {

		return find(object, append_expr);
	}

	/**
	 * Insert an object to database
	 *
	 * @param o insert target object
	 * @throws Exception
	 */
	public static void insert(Object o) throws Exception {
		Class clazz = o.getClass();

		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(clazz.getSimpleName().toLowerCase());
		StringBuffer namestr = new StringBuffer(" (");
		StringBuffer valuestr = new StringBuffer(" values(");
		List<String> values = new LinkedList<String>();

		Map<String, Field> fields = getTableFields(clazz);
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			String name = entry.getKey();
			Field field = entry.getValue();
			String invoke = toString(o, field);
			if (invoke != null) {
				namestr.append(name);
				namestr.append(",");
				valuestr.append(getInsertExpression(field));
				values.add(invoke.toString());
			}
		}
		namestr.setCharAt(namestr.length() - 1, ')');
		valuestr.setCharAt(valuestr.length() - 1, ')');
		sql.append(namestr);
		sql.append(valuestr + "\r\n");
		String sql_str = sql.toString();
		execute(sql_str, values);
	}

	/**
	 * Update object in date base
	 * @param o input object
	 * @param setCols cols
	 * @param condCols cols values
	 * @throws Exception
	 */
	public static void update(Object o, String[] setCols, String[] condCols)
			throws Exception {
		Class clazz = o.getClass();

		StringBuffer sql = new StringBuffer("update ");
		sql.append(clazz.getSimpleName().toLowerCase());
		sql.append(" set ");

		List<String> values = new LinkedList<String>();
		Map<String, Field> fields = getTableFields(clazz);
		for (String colName : setCols) {
			String invoke = toString(o, fields.get(colName));
			if (invoke != null) {
				sql.append(getUpdateExpression(fields.get(colName)));
				values.add(invoke.toString());
			}

		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append(" where 1=1  ");

		for (String colName : condCols) {
			String invoke = toString(o, fields.get(colName));
			if (invoke != null) {
				sql.append(getConditionExpression(fields.get(colName)));
				values.add(invoke.toString());
			}

		}

		String sql_str = sql.toString();
		execute(sql_str, values);
	}

	/**
	 * Update object in database with the new value
	 *
	 * @param o input object
	 * @param condCols cols need to update
	 * @throws Exception
	 */
	public static void update(Object o, String... condCols) throws Exception {
		Class clazz = o.getClass();

		StringBuffer sql = new StringBuffer("update ");
		sql.append(clazz.getSimpleName().toLowerCase());
		sql.append(" set ");
		List<String> values = new LinkedList<String>();
		Map<String, Field> fields = getTableFields(clazz);
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			Field field = entry.getValue();
			String invoke = toString(o, field);
			if (invoke != null) {
				sql.append(getUpdateExpression(field));
				values.add(invoke.toString());
			}
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append(" where 1=1  ");
		for (String colName : condCols) {
			String invoke = toString(o, fields.get(colName));
			if (invoke != null) {
				sql.append(getConditionExpression(fields.get(colName)));
				values.add(invoke.toString());
			}
		}

		String sql_str = sql.toString();
		// logger.info(sql_str);

		execute(sql_str, values);
	}

	/**
	 * Delete object in database
	 * @param o input object
	 * @throws Exception
	 */
	public static void delete(Object o) throws Exception {
		Class clazz = o.getClass();
		StringBuffer sql = new StringBuffer("delete ");
		sql.append(clazz.getSimpleName().toLowerCase());
		sql.append(" from ");
		sql.append(clazz.getSimpleName().toLowerCase());
		sql.append(" where 1=1 ");

		// 获取类的所有属性，返回<FieldName,Field> map
		Map<String, Field> fields = getTableFields(clazz);
		List<String> values = new LinkedList<String>();
		// 再遍历一边添加where条件
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			Field field = entry.getValue();
			String invoke = toString(o, field);
			// 如果属性有值
			if (invoke != null) {
				sql.append(getConditionExpression(field));
				values.add(invoke.toString());
			}
		}

		String sql_str = sql.toString();
		execute(sql_str, values);
	}

	/**
	 * Execute sql statement with values
	 * @param sql sql statement
	 * @param values
	 * @throws Exception
	 */
	public static void execute(String sql, List<String> values)
			throws Exception {
		PreparedStatement pstat = getConnection().prepareStatement(sql);
		int i = 1;
		for (String val : values) {
			pstat.setString(i++, val);
		}
		// logger.info(pstat.toString());
		pstat.execute();
	}

	/**
	 * Execute sql statement
	 *
	 * @param sql sql statement
	 * @return type:boolean
	 * @throws Exception
	 */
	public static boolean execute(String sql) throws Exception {
		PreparedStatement pstat = getConnection().prepareStatement(sql);
		return pstat.execute();
	}

}
