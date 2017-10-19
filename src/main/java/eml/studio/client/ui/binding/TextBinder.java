/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.binding;

/**
 * A binder interface to synchronized an instance T and O
 * @param <T>
 * @param <O>
 */
public interface TextBinder<T, O> {

	void bind(T text, O obj);

	void sync(T text, O obj);
}
