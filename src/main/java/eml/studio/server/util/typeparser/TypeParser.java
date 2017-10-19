/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util.typeparser;

/**
 * Interface of TypeParser
 * @param <T>
 */
public interface TypeParser<T> {
	T parse(String value);
	String toString(Object obj);
}