/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AesServiceAsync {
	
	void aesEncrypt(List<String>contents, AsyncCallback<List<String>> callback);
	
}
