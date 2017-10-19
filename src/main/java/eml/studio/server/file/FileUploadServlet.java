/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.file;

import eml.studio.server.constant.Constants;
import eml.studio.server.util.HDFSIO;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FileUploadServlet.class
			.getName());

	public static void unZipFiles(InputStream instream, String ID)
			throws IOException {
		ZipArchiveInputStream zin = new ZipArchiveInputStream(instream);
		java.util.zip.ZipEntry entry = null;
		while ((entry = zin.getNextZipEntry()) != null) {
			String zipEntryName = entry.getName();
			String outPath = zipEntryName.replaceAll("\\*", "/");
			String path = "lib";
			path += zipEntryName.substring(zipEntryName.indexOf('/'), zipEntryName.length());
			System.out.println("[path ]:" + path);
			if (!outPath.endsWith("/")) {
				InputStream in = zin;
				HDFSIO.uploadModel("/" + ID + "/" + path, in);
			}
		}
		zin.close();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		saveUploadFile(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		saveUploadFile(request, response);
	}

	/**
	 * save file upload to server
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 */

	public void saveUploadFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = null;
		List items_findId = null;
		try {
			items = upload.parseRequest(request);
			items_findId = items;
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		String ID = new String();
		Iterator iter_findId = items_findId.iterator();
		while (iter_findId.hasNext()) {
			FileItem item_findId = (FileItem) iter_findId.next();
			if (item_findId.isFormField()) {
				String fieldName = item_findId.getFieldName();
				String fieldValue;
				try {
					fieldValue = item_findId.getString("UTF-8");

					if ("Fileuuid".equals(fieldName)) {
						ID = Constants.MODULE_PATH + "/" + fieldValue;

					} else
						ID = Constants.DATASET_PATH + "/" + fieldValue;
					logger.info("[UUID]:" + fieldName + ":" + ID);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
			} else {
				InputStream in;
				try {
					in = item.getInputStream();
					if (item.getName().endsWith(".zip")) {
						unZipFiles(in, ID);
						HDFSIO.uploadfile("/" + ID + "/", item, item.getName());
					} else {
						if (ID.contains("Data")) {
							HDFSIO.uploadfile("/" + ID + "/", item, ID.split("Data")[1]);
						} else
							HDFSIO.uploadfile("/" + ID + "/", item, item.getName());
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
