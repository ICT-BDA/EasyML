/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.graph;

import java.util.List;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import eml.studio.shared.graph.OozieDatasetNode;
import eml.studio.shared.graph.OozieEdge;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;

/**
 * Parse the workflow graph of a oozie job from XML with format:
 * <p/>
 * <graph>
 * <dataset>
 * <id>...</id>
 * <moduleId>module_id</moduleId>
 * <x>100</x>
 * <y>200</y>
 * <file>...</file>
 * </dataset>
 * <program>
 * <id>...</id>
 * <moduleId>module_id</moduleId>
 * <x>100</x>
 * <y>200</y>
 * <file>...</file>
 * <param>...</param>
 * </program>
 * ...
 * <edge>
 * <source>LogisticRegression_Train-1527cdbee30-0d4b:0</source>
 * <destination>LogisticRegression_Predict-1527cdbf997-6501:0</destination>
 * </edge>
 * </graph>
 */
public class OozieGraphXMLParser {

	private static Logger logger = Logger.getLogger(OozieGraphXMLParser.class.getName());
	/**
	 * @param xml xml description
	 * @throws DocumentException 
	 */
	public static OozieGraph parse(String xml) throws DocumentException {
		OozieGraph graph = new OozieGraph();
		Document doc =DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		// parse widgets
		List<Element> nodes = root.elements("widget");
		for( Element node: nodes){
			String type = node.attributeValue("type");
			if (type.equals("dataset")) {
				OozieDatasetNode odn = parseDatasetNode(node);
				graph.addDatasetNode(odn);
			} else if(type.equals("program")){
				OozieProgramNode opn = parseProgramNode(node);
				graph.addProgramNode(opn);
				graph.addActiveNode(opn.getId());
			}

		}

		// parse edges
		List<Element> enodes = root.elements("edge");
		for(Element elem: enodes){
			OozieEdge edge = parseOozieEdge( elem);
			if (edge != null)
				graph.addEdge(edge);
		}

		return graph;
	}

	/** parse a dataset node from xml */
	private static OozieDatasetNode parseDatasetNode(Element xml_node) {
		List<Element> childNodes = xml_node.elements();
		OozieDatasetNode node = new OozieDatasetNode();

		for( Element child : childNodes){
			String value = child.getText();
			String name = child.getName();

			if ("id".equals(name))
				node.setId(value);
			else if ("moduleId".equals(name))
				node.setModuleId(value);
			else if ("x".equals(name))
				node.setX((int) Float.parseFloat(value));
			else if ("y".equals(name))
				node.setY((int) Float.parseFloat(value));
			else if ("file".equals(name))
				node.setFile(value);
		}

		return node;
	}

	/** parse a program node from xml */
	private static OozieProgramNode parseProgramNode(Element xml_node) {
		List<Element> childNodes = xml_node.elements();
		OozieProgramNode node = new OozieProgramNode();

		for( Element child : childNodes){
			String value = child.getText();
			String name = child.getName();

			if ("id".equals(name))
				node.setId(value);
			else if ("moduleId".equals(name))
				node.setModuleId(value);
			else if ( "oozJob".equals(name))
				node.setOozJobId( value );
			else if ("x".equals(name))
				node.setX((int) Float.parseFloat(value));
			else if ("y".equals(name))
				node.setY((int) Float.parseFloat(value));
			else if ("file".equals(name))
				node.addFile(value);
			else if( "input_aliases".equals( name ) ){
				node.addInputAliases( value );
			}else if("output_aliases".equals(name)){
				node.addOutputAliases( value );
			}else if ("param".equals(name))
				node.addParam(value);
			else if( "script".equals(name))
				node.setScript(value);
			else if( "incount".equals(name))
				node.setInputFileCount( Integer.parseInt(value));
			else if( "outcount".equals(name))
				node.setOutputFileCount( Integer.parseInt(value));
			else if("is_distributed".equals(name))
				node.setDistributed(Boolean.valueOf(value));
			else if("is_standalone_script".equals(name)){
				node.setStandaloneScript(Boolean.valueOf(value));
			}else if("work_path".equals(name)){
				node.setWorkPath(value);
			}
			else if("cmd_line".equals(name)){
				node.setCmdLine(value);
			}
		}

		return node;
	}


	/** Parse a edge from xml
	 * @param enode  edge node
	 */
	private static OozieEdge parseOozieEdge(Element enode) {

		String src = "";
		String dst = "";
		List<Element> children = enode.elements();
		for( Element child: children){
			if ("source".equals(child.getName())) {
				src = child.getText();
			} else if ("destination".equals(child.getName())) {
				dst = child.getText();
				break;
			}
		}
		if (src.isEmpty() || dst.isEmpty()) {
			logger.warning("Empty edge, src:" + src + ", dst:" + dst);
			return null;
		}
		else{
			OozieEdge edge = new OozieEdge();
			edge.init(src, dst);
			return edge;
		}
	}
}
