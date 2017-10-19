/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import eml.studio.shared.model.Dataset;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface DatasetServiceAsync {

	void getSize(AsyncCallback<Integer> callback);

	void loadPart(int start, int size, AsyncCallback<List<Dataset>> callback);

	void load(AsyncCallback<List<Dataset>> callback);

	void load(String id, AsyncCallback<Dataset> callback);

	void loadFile(String path, AsyncCallback<Dataset> callback);

	void previewFile(String src_uri, int head, AsyncCallback<String> callback);

	void save(Dataset dataset, String src_uri, AsyncCallback<Void> callback);

	void upload(Dataset dataset, String src_uri, AsyncCallback<Dataset> callback);

	void editCategory(String oldCate, String newCate, AsyncCallback<String> callback);

	void edit(Dataset dataset, AsyncCallback<Void> callback);

	void delete(String id, AsyncCallback<Void> callback);

	void deprecate(String id, AsyncCallback<Void> callback);

	void download(String id, AsyncCallback<String> callback);

	void upgrade(String id, String newID, AsyncCallback<Void> callback);

	void isDirectory(String path,AsyncCallback<Boolean> callback);

	void getDirFilesPath(String path,AsyncCallback<List<String>> callback);

	void isFileExist(String path,AsyncCallback<Boolean> callback);

	void getFileSize(String path,AsyncCallback<Double> callback);

	void search(Dataset dataset, String startDate, String endDate, int limitStart, int limitSize, AsyncCallback<List<Dataset>> callback);
}
