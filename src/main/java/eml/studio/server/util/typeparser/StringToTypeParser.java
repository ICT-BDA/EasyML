/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util.typeparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The String type is resolved to the specified type of object,
 * supporting the base type. Other types can be added if necessary
 */
public final class StringToTypeParser {
	private static final Object STATIC_METHOD = null;
	private final Map<Class<?>, TypeParser<?>> typeParsers;

	StringToTypeParser(Map<Class<?>, TypeParser<?>> typeParsers) {
		this.typeParsers = Collections.unmodifiableMap(new HashMap<Class<?>, TypeParser<?>>(typeParsers));
	}

	public static StringToTypeParserBuilder newBuilder() {
		return new StringToTypeParserBuilder();
	}

	/**
	 * This method is static because it is also called from {@link StringToTypeParserBuilder}.
	 */
	static String nullArgumentErrorMsg(String argName) {
		return String.format("Argument named '%s' is illegally set to null!", argName);
	}

	public <T> String toString(Object object, Class<T> type){
		if ( object == null) {
			return null;
		}
		if (type == null) {
			throw new NullPointerException(nullArgumentErrorMsg("type"));
		}

		if (typeParsers.containsKey(type)) {
			return typeParsers.get(type).toString( object );
		} else {
			String message = "There is no registered 'TypeParser' for that type.";
			throw new IllegalArgumentException(message);
		}

	}

	/**
	 * Resolve value to type type
	 * @param value string value
	 * @param type class of type
	 * @param <T> template
	 * @return T type object
	 */
	public <T> T parse(String value, Class<T> type) {
		if (value == null) {
			throw new NullPointerException(nullArgumentErrorMsg("value"));
		}
		if (type == null) {
			throw new NullPointerException(nullArgumentErrorMsg("type"));
		}

		// convert "null" string to null type.
		if (value.trim().equalsIgnoreCase("null")) {
			if (type.isPrimitive()) {
				String message = "'%s' primitive can not be set to null.";
				throw new IllegalArgumentException(String.format(message, type.getName()));
			}
			return null;
		}

		Object result = null;
		if (typeParsers.containsKey(type)) {
			result = callTypeParser(value, type);
		} else if ((result = callFactoryMethodIfExisting("valueOf", value, type)) != null) {
			//
		} else if ((result = callFactoryMethodIfExisting("of", value, type)) != null) {
			//
		} else {
			String message = "There is no registered 'TypeParser' for that type, or that "
					+ "type does not contain one of the following static factory methods: "
					+ "'%s.valueOf(String)', or '%s.of(String)'.";
			message = String.format(message, type.getSimpleName(), type.getSimpleName());
			message = canNotParseErrorMsg(value, type, message);
			throw new IllegalArgumentException(message);
		}
		/*
		 * This cast is correct, since all above checks ensures we're casting to
		 * the right type.
		 */
		@SuppressWarnings("unchecked")
		T temp = (T) result;
		return temp;
	}

	private Object callTypeParser(String value, Class<?> type) {
		try {
			return typeParsers.get(type).parse(value);
		} catch (NumberFormatException e) {
			String message = canNotParseErrorMsg(value, type, numberFormatErrorMsg(e));
			(new IllegalArgumentException(message, e)).printStackTrace();;
		} catch (RuntimeException e) {
			( new IllegalArgumentException(
					canNotParseErrorMsg(value, type, e.getMessage()), e)).printStackTrace();
		}
		return null;
	}

	private String numberFormatErrorMsg(NumberFormatException e) {
		return String.format("Number format exception %s.", e.getMessage());
	}

	private String canNotParseErrorMsg(String value, Class<?> type, String message) {
		return String.format("Can not parse \"%s\" to type '%s' due to: %s", value, type.getName(), message);
	}

	private Object callFactoryMethodIfExisting(String methodName, String value, Class<?> type) {
		Method m;
		try {
			m = type.getDeclaredMethod(methodName, String.class);
			m.setAccessible(true);
			if (!Modifier.isStatic(m.getModifiers())) {
				// Static factory method does not exists, return null
				return null;
			}
		} catch (Exception e) {
			// Static factory method does not exists, return null
			return null;
		}

		try {
			if(type.isEnum()){
				value = value.trim();
			}
			return m.invoke(STATIC_METHOD, value);
		} catch (InvocationTargetException e) {
			// filter out the InvocationTargetException stacktrace/message.
			throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), e.getCause());
		} catch (Throwable t) {
			throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), t);
		}
	}

	/**
	 * Generate error Massages
	 * @param methodName
	 * @param value
	 * @param type
	 * @return
	 */
	private String makeErrorMsg(String methodName, String value,  Class<?> type) {
		String methodSignature = String.format("%s.%s('%s')", type.getName(), methodName, value);
		String message = " Exception thrown in static factory method '%s'. See underlying "
				+ "exception for additional information.";
		return canNotParseErrorMsg(value, type, String.format(message, methodSignature));
	}
}