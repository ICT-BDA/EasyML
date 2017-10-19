/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util.typeparser;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eml.studio.server.util.TimeUtils;

/**
 * Default Type Parsers Class
 */
class DefaultTypeParsers {
	private static final String BOOLEAN_ERROR_MESSAGE = "\"%s\" is not parsable to a Boolean.";
	private static final String CHARACTER_ERROR_MESSAGE = "\"%s\" must only contain a single character.";
	private static final Map<Class<?>, TypeParser<?>> DEFAULT_TYPE_PARSERS = new HashMap<Class<?>, TypeParser<?>>();
	private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>();

	static{
		WRAPPER_TO_PRIMITIVE.put(Boolean.class, boolean.class);
		WRAPPER_TO_PRIMITIVE.put(Byte.class, byte.class);
		WRAPPER_TO_PRIMITIVE.put(Short.class, short.class);
		WRAPPER_TO_PRIMITIVE.put(Character.class, char.class);
		WRAPPER_TO_PRIMITIVE.put(Integer.class, int.class);
		WRAPPER_TO_PRIMITIVE.put(Long.class, long.class);
		WRAPPER_TO_PRIMITIVE.put(Float.class, float.class);
		WRAPPER_TO_PRIMITIVE.put(Double.class, double.class);
	}

	static{
		put(Boolean.class, new TypeParser<Boolean>() {
			@Override
			public Boolean parse(final String value0) {
				String value = value0.trim().toLowerCase();
				if(value.equals("是")){
					return Boolean.TRUE;
				} else if(value.equals("否")){
					return Boolean.FALSE;
				} else if(value.equals("1")){
					return Boolean.TRUE;
				} else if(value.equals("0")){
					return Boolean.FALSE;
				} else if(value.equals("true")){
					return Boolean.TRUE;
				} else if(value.equals("false")){
					return Boolean.FALSE;
				} else if(value.equals("yes")){
					return Boolean.TRUE;
				} else if(value.equals("no")){
					return Boolean.FALSE;
				} else if( value.equals("") )
					return Boolean.FALSE;
				throw new IllegalArgumentException(String.format(BOOLEAN_ERROR_MESSAGE, value0));
			}

			@Override
			public String toString(Object type) {
				return (Boolean)type?"1":"0";
			}
		});

		put(Character.class, new TypeParser<Character>() {
			@Override
			public Character parse(String value) {
				if(value.length() == 1){
					return Character.valueOf(value.charAt(0));
				}
				throw new IllegalArgumentException(String.format(CHARACTER_ERROR_MESSAGE, value));
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Byte.class, new TypeParser<Byte>() {
			@Override
			public Byte parse(String value) {
				return Byte.valueOf(value.trim());
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Integer.class, new TypeParser<Integer>() {
			@Override
			public Integer parse(String value) {
				return Integer.valueOf(value.trim());
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Long.class, new TypeParser<Long>() {
			@Override
			public Long parse(String value) {
				return Long.valueOf(value.trim());
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Short.class, new TypeParser<Short>() {
			@Override
			public Short parse(String value) {
				return Short.valueOf(value.trim());
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Float.class, new TypeParser<Float>() {
			@Override
			public Float parse(String value) {
				return Float.valueOf(value);
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(Double.class, new TypeParser<Double>() {
			@Override
			public Double parse(String value) {
				return Double.valueOf(value);
			}

			@Override
			public String toString(Object type) {
				return type.toString();
			}
		});

		put(File.class, new TypeParser<File>() {
			@Override
			public File parse(String value) {
				return new File(value.trim());
			}

			@Override
			public String toString(Object type) {
				return ((File)type).getAbsolutePath();
			}
		});

		put(String.class, new TypeParser<String>() {
			@Override
			public String parse(String value) {
				return value;
			}

			@Override
			public String toString(Object type) {
				return (String)type;
			}
		});

		put(Date.class, new TypeParser<Date>(){

			@Override
			public Date parse(String value) {
				Date date = TimeUtils.parse(value);
				return date;
			}

			@Override
			public String toString(Object type) {
				return TimeUtils.format((Date)type);
			}

		});
	}

	private DefaultTypeParsers() {
		new AssertionError("Not meant for instantiation");
	}

	static Map<Class<?>, TypeParser<?>> copy() {
		return new HashMap<Class<?>, TypeParser<?>>(DEFAULT_TYPE_PARSERS);
	}

	private static <T> void put(Class<T> type, TypeParser<? extends T> typeParser) {
		DEFAULT_TYPE_PARSERS.put(type, typeParser);
		if (WRAPPER_TO_PRIMITIVE.containsKey(type)) {
			// add primitive targetType if existing, example int.class, boolean.class etc.
			Class<?> primitiveType = WRAPPER_TO_PRIMITIVE.get(type);
			DEFAULT_TYPE_PARSERS.put(primitiveType, typeParser);
		}
	}
}