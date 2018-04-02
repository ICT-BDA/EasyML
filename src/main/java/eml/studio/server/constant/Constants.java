/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.constant;

import eml.studio.server.util.XMLUtil;

import com.github.drapostolos.typeparser.StringToTypeParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * Read the service configuration from
 * server-config.xml resource file。
 */
public class Constants {

	public static String OOZIE_CLIENT;
	public static String NAME_NODE;
	public static String JOB_TRACKER;
	public static String QUEUE_NAME;
	public static String APP_WORKSPACE;
	public static String DRAFT_PATH;
	public static String DATASET_PATH;
	public static String MODULE_PATH;
	
	// public static final String DB_URL ="jdbc:mysql://mysql:3306/dfgendb";
	public static String DB_URL;
	public static String DB_HOST;
	public static String DB_USER;
	public static String DB_PORT;
	public static String DB_NAME;
	
	public static String DB_PASSWORD;
	public static Integer DB_TIMEOUT;
	/**********************************************
	 *********** MAIL *****************
	 **********************************************/
	public static String MAIL_HOST;
	public static String MAIL_USERNAME;
	public static String MAIL_PASSWORD;
	public static String HOSTNAME_CON;

	// This is the tensorflow cluster ip list, segment by ","
	public static String TENSORFLOW_CLUSTER;  
	public static String TENSORFLOW_MASTER;
	public static String TENSORFLOW_USER;
	public static String TENSORFLOW_MODEL_SCRIPT;
	public static String TENSORFLOW_DATA_SCRIPT;

	static Logger logger = Logger.getLogger(Constants.class.getName());

	static {
		Document dom = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			//Get the configuration file and build the xml dom object.
			dom = XMLUtil.read(classLoader.getResourceAsStream("server-config.xml"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/**
		 * Use the java reflection mechanism to set the corresponding properties
		 */
		Class<Constants> clazz = Constants.class;
		StringToTypeParser parser = StringToTypeParser.newBuilder().build();

		Element root = dom.getDocumentElement();
		NodeList nlist = root.getChildNodes();
		int len = nlist.getLength();
		for (int i = 0; i < len; ++i) {
			Node tagNode = nlist.item(i);
			Node valNode = tagNode.getFirstChild();
			if (valNode == null)
				continue;
			String name = tagNode.getNodeName();
			try {
				Field f = clazz.getDeclaredField(name);
				String val = valNode.getNodeValue();
				// Use parser to automatically identify the type, and for type conversion, parse (String, Class)
				f.set(null, parser.parse(val, f.getType()));
				logger.info(name + " " + val);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		DB_URL="jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME+"?useUnicode=true&amp;characterEncoding=UTF-8";
	
		//Tensorflow file reader
		try {
			InputStream modelReader = classLoader.getResourceAsStream("model_distributed.py");
			byte[] pyByte;

			pyByte = new byte[(int)modelReader.available()];
			modelReader.read(pyByte); 
			modelReader.close();   
			TENSORFLOW_MODEL_SCRIPT = new String(pyByte); 

			InputStream dataReader = classLoader.getResourceAsStream("data_distributed.py");
			pyByte = new byte[(int)dataReader.available()];
			dataReader.read(pyByte); 
			dataReader.close();   
			TENSORFLOW_DATA_SCRIPT = new String(pyByte); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
