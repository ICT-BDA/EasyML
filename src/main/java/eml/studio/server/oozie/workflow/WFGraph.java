/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class WFGraph {

	protected Map<String, NodeDef> nodeMap = new HashMap<String, NodeDef>();

	protected NodeDef start = new StartNodeDef();
	protected NodeDef end = new EndNodeDef();

	/**
	 * nodes that will be execute in the Oozie Job
	 * @param action the action added to Oozie
	 * @throws Exception
	 */
	public void addActionNode(NodeDef action) throws Exception {
		nodeMap.put(action.getName(), action);
	}

	/**
	 * Add edge from source node to destination node
	 * @param srcNodeId the source node id
	 * @param dstNodeId the destination node id
	 * @param srcNodePort the source node port
	 * @param dstNodePort the destination node port
	 * @param file the file name of dataflow this edge respected to
	 */
	public void addEdge(String srcNodeId, String dstNodeId, int srcNodePort,
			int dstNodePort, String file) {
		NodeDef srcNode = nodeMap.get(srcNodeId);
		NodeDef dstNode = nodeMap.get(dstNodeId);
		if (srcNode != null && dstNode != null) {
			srcNode.addOutNode(dstNode);
			dstNode.addInNode(srcNode);
		}
		// 设置
		if (srcNode != null)
			srcNode.putOutputFile(srcNodePort, file);
		if (dstNode != null)
			dstNode.putInputFile(dstNodePort, file);
	}

	/**
	 * Transform the Graph into an workflow xml definition
	 * @param jobname the job name of Oozie job, can't be null
	 * @return workflow xml
	 */
	public String toWorkflow(String jobname) {
		Namespace xmlns = new Namespace("", "uri:oozie:workflow:0.4"); // root namespace uri
		QName qName = QName.get("workflow-app", xmlns); // your root element's name
		Element workflow = DocumentHelper.createElement(qName);
		Document xmldoc = DocumentHelper.createDocument(workflow);
		// 创建workflow的根

		workflow.addAttribute("xmlns", "uri:oozie:workflow:0.4");
		// <workflow-app name='xxx'></workflow-app>
		if (jobname == null || "".equals(jobname))
			workflow.addAttribute("name", "Not specified");
		else
			workflow.addAttribute("name", jobname);

		Queue<NodeDef> que = new LinkedList<NodeDef>();
		que.add(start);

		while (!que.isEmpty()) {
			NodeDef cur = que.remove();

			cur.append2XML(workflow);

			for (NodeDef toNode : cur.getOutNodes()) {
				toNode.delInNode(cur);
				// Window.alert( toNode.getName() + " " + toNode.getInDegree()
				// );
				if (toNode.getInDegree() == 0)
					que.add(toNode);
			}
		}

		// 设置XML文档格式
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		// 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
		outputFormat.setEncoding("UTF-8");
		outputFormat.setSuppressDeclaration(true); //是否生产xml头
		outputFormat.setIndent(true); // 设置是否缩进
		outputFormat.setIndent("    "); // 以四个空格方式实现缩进
		outputFormat.setNewlines(true); // 设置是否换行

		try {
			// stringWriter字符串是用来保存XML文档的
			StringWriter stringWriter = new StringWriter();
			// xmlWriter是用来把XML文档写入字符串的(工具)
			XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
			
			// 把创建好的XML文档写入字符串
			xmlWriter.write(xmldoc);

			xmlWriter.close();

			System.out.println( stringWriter.toString().trim());
			// 打印字符串,即是XML文档
			return stringWriter.toString().trim();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]){

		Namespace rootNs = new Namespace("", "uri:oozie:workflow:0.4"); // root namespace uri
		QName rootQName = QName.get("workflow-app", rootNs); // your root element's name
		Element workflow = DocumentHelper.createElement(rootQName);
		Document doc = DocumentHelper.createDocument(workflow);
		
		workflow.addAttribute("name", "test");
		Element test = workflow.addElement("test");
		test.addText("hello");
		// 设置XML文档格式
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				// 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
				outputFormat.setEncoding("UTF-8");
//				outputFormat.setSuppressDeclaration(true); //是否生产xml头
				outputFormat.setIndent(true); // 设置是否缩进
				outputFormat.setIndent("    "); // 以四个空格方式实现缩进
				outputFormat.setNewlines(true); // 设置是否换行
		try {
			// stringWriter字符串是用来保存XML文档的
			StringWriter stringWriter = new StringWriter();
			// xmlWriter是用来把XML文档写入字符串的(工具)
			XMLWriter xmlWriter = new XMLWriter(stringWriter);//, outputFormat);
			
			// 把创建好的XML文档写入字符串
			xmlWriter.write(doc);

			xmlWriter.close();
			System.out.println( doc.asXML() );

			System.out.println( stringWriter.toString().trim());
			// 打印字符串,即是XML文档

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
