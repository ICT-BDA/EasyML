/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.file;

import eml.studio.server.constant.Constants;
import eml.studio.server.util.HDFSIO;

import org.apache.hadoop.fs.Path;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DOWNLOAD_BASE_DIR="./download";
	private static final Integer FILE_BUFFER_SIZE = 4096;

	private static Logger logger = Logger.getLogger(
			FileDownloadServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		saveDownloadFile(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		saveDownloadFile(request, response);
	}

	/**
	 * Save download file
	 *
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void saveDownloadFile(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String filename = req.getParameter("filename");
		logger.info("Download file:" + filename);

		if (filename == null) {
			resp.sendError(0, "Missing file Name");
			logger.warning("[Missing file Name]");
			return;
		}
		resp.setContentType("application/octet-stream");
		if(!HDFSIO.isDirectory(new Path(filename))) //若当前文件不是目录，则直接下载
		{
			logger.info("Directory download file:"+filename);
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + filename);
			InputStream in = HDFSIO.downInputStream( Constants.NAME_NODE+ "/" + filename);
			ServletOutputStream out = resp.getOutputStream();
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();
		}
		else //If the directory, then download the directory from HDFS to the server,
			// and then compressed, and then download to the client
		{
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + filename+".zip");
			String[] filePathStrs = filename.split("/");
			String dirName = filePathStrs[filePathStrs.length-1]; //目录名称(非路径)

			String localDirPath = DOWNLOAD_BASE_DIR+"/"+dirName; //文件下载保存的本地目录

			long startTime = System.currentTimeMillis();

			//Download the results directory from HDFS to local (if it not exist)
			if(!new File(DOWNLOAD_BASE_DIR+"/"+dirName).exists())
			{
				downloadFile(filename);  
				logger.info("Download dir success, save path is : "+localDirPath);
			}
			else
				logger.info(localDirPath+" has exist!");

			//Compress the resulting file in zip format (if it does not exist)
			String zipFilePath = localDirPath+".zip";
			if(!new File(zipFilePath).exists())
			{
				zipFiles(localDirPath,zipFilePath);
				logger.info("Compress dir success, save paths is : "+zipFilePath);
			}
			else
				logger.info(zipFilePath+" has exist!");

			FileInputStream in = new FileInputStream(zipFilePath);
			ServletOutputStream out = resp.getOutputStream();
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();
			long endTime = System.currentTimeMillis();
			logger.info("Download time:"+(endTime - startTime));

			File results = new File(DOWNLOAD_BASE_DIR);
			if(results.exists())
			{   
				deleteFile(results.getAbsolutePath());
				logger.info("Delete result dir success!");
			}
		}

	}

	/**
	 * Download files or files from HDFS to local
	 *
	 * @param filePath  File or file directory path (HDFS path)
	 * @throws IOException
	 */
	public static void downloadFile(String filePath) throws IOException
	{
		String[] paths = filePath.split("/");
		String fileName = paths[paths.length-1];
		String baseDir="";
		download(filePath,fileName,baseDir);
	}

	/**
	 * Method of download file from HDFS
	 * 
	 * @param filePath File or directory path (HDFS path)
	 * @param fileName  file name
	 * @param baseDir  path of parent file
	 * @throws IOException
	 */
	private static void download(String filePath,String fileName,String baseDir) throws IOException
	{
		Path uri = new Path(filePath);
		if(HDFSIO.isDirectory(uri))
		{
			File dirFile = new File(DOWNLOAD_BASE_DIR+"/"+baseDir+fileName);
			if(!dirFile.exists())
			{
				logger.info("Download mkdir:"+dirFile.getPath());
				dirFile.mkdirs();
			}
			Path[] paths = HDFSIO.list(filePath);
			for(Path p : paths)
				download(p.toString(),p.getName(),baseDir+fileName+"/");
		}
		else
		{
			InputStream in = HDFSIO.downInputStream(filePath);
			logger.info("Download file:"+DOWNLOAD_BASE_DIR+"/"+baseDir+fileName);
			File file = new File(DOWNLOAD_BASE_DIR+"/"+baseDir+fileName);
			if(!file.exists())
				file.createNewFile();
			FileOutputStream out=new FileOutputStream(file);
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close(); 
			out.flush();
			out.close();
		}
	}

	/**
	 * Compressed file or directory
	 * 
	 * @param srcPath  The address of the file or folder
	 * @param zipFilePath  The address of the compressed package
	 */
	public static void zipFiles(String srcPath,String zipFilePath) {  
		File file = new File(srcPath);  
		if (!file.exists())  
			throw new RuntimeException(srcPath + "not exist！");  
		try {  
			FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);  
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,  
					new CRC32());  
			ZipOutputStream out = new ZipOutputStream(cos);  
			String baseDir="";
			zip(file,out,baseDir);
			out.close();  
		} catch (Exception e) {  
			throw new RuntimeException(e);  
		}  
	} 

	/**
	 * Main method of compress the file
	 * 
	 * @param file  origin file
	 * @param out  compressed file stream
	 * @param baseDir  file path
	 */
	private static void zip(File file,ZipOutputStream out,String baseDir)
	{
		if (file.isDirectory()) {  
			logger.info("Compress dir:"+baseDir+file.getName());
			zipDirectory(file, out, baseDir);  
		} else {  
			logger.info("Compress file:"+baseDir+file.getName());
			zipSingleFile(file, out, baseDir);  
		}  
	}

	/**
	 * If a compressed directory is recursively compressed
	 *  
	 * @param dir  diretory
	 * @param out  compressed file stream
	 * @param baseDir  The relative position of the files in the catalog
	 */
	private static void zipDirectory(File dir, ZipOutputStream out,String baseDir) {  
		if (!dir.exists())  
			return;  

		File[] files = dir.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			zip(files[i], out , baseDir + dir.getName() + "/");  
		}  
	}  

	/**
	 * Compress a single file
	 *  
	 * @param file file
	 * @param out  file stream
	 * @param baseDir  The relative address of the file
	 */
	private static void zipSingleFile(File file, ZipOutputStream out,String baseDir) {  
		if (!file.exists()) {  
			return;  
		}  
		try {  
			int buffer = FILE_BUFFER_SIZE;
			BufferedInputStream bis = new BufferedInputStream(  
					new FileInputStream(file));  
			ZipEntry entry = new ZipEntry(baseDir + file.getName());  
			out.putNextEntry(entry);  
			int count;  
			byte data[] = new byte[buffer];  
			while ((count = bis.read(data, 0, buffer)) != -1) {  
				out.write(data, 0, count);  
			}  
			bis.close();  
		} catch (Exception e) {  
			throw new RuntimeException(e);  
		}  
	}  

	/**
	 * Delete a file or directory
	 * 
	 * @param path  path of file or directory
	 * @return type:boolean
	 */
	private static boolean deleteFile(String path)
	{
		File delFile = new File(path);
		if(delFile.isDirectory())
		{
			File[] dirFiles = delFile.listFiles();
			for(File f : dirFiles)
			{
				deleteFile(f.getAbsolutePath());
			}
		}
		return delFile.delete();
	}

}
