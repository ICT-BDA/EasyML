/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * The workflow graph description
 *
 */
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
	   * Add fill output file path to node output node
	   *  
	   * @param nodeId   Node id
	   * @param files Output file name list
	   * @param workPath  App work path
	   */
	  public void addNodeOutputFile(String nodeId, List<String> files,String workPath)
	  {
	    NodeDef node = nodeMap.get(nodeId);
	    if(node != null)
	    {
	      for(int i = 0 ; i < files.size() ; i++)
	      {
	        if(workPath!=null)
	        {
	          String totalPath = workPath +files.get(i);
	          node.putOutputFile(i, totalPath);
	        }
	        else
	          node.putOutputFile(i, files.get(i));
	      }
	    }
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
		// Create workflow root
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
				if (toNode.getInDegree() == 0)
					que.add(toNode);
			}
		}

		// Set XML document format
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		// Set XML encoding, use the specified encoding to save the XML document to the string, it can be specified GBK or ISO8859-1
		outputFormat.setEncoding("UTF-8");
		outputFormat.setSuppressDeclaration(true); // Whether generate xml header
		outputFormat.setIndent(true); // Whether set indentation
		outputFormat.setIndent("    "); // Implement indentation with four spaces
		outputFormat.setNewlines(true); // Set whether to wrap

		try {
			// stringWriter is used to save xml document
			StringWriter stringWriter = new StringWriter();
			// xmlWriter is used to write XML document to string(tool)
			XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
			
			// Write the created XML document into the string
			xmlWriter.write(xmldoc);

			xmlWriter.close();

			System.out.println( stringWriter.toString().trim());
			// Print the string, that is, the XML document
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
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true); 
				outputFormat.setIndent("    "); 
				outputFormat.setNewlines(true); 
		try {
			StringWriter stringWriter = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(stringWriter);
			xmlWriter.write(doc);
			xmlWriter.close();
			System.out.println( doc.asXML() );
			System.out.println( stringWriter.toString().trim());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
