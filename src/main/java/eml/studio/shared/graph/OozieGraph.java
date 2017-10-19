package eml.studio.shared.graph;


import java.util.LinkedList;
import java.util.List;

import eml.studio.shared.script.Script;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A Oozie workflow graph, whose nodes are Oozie widgets
 */
public class OozieGraph implements IsSerializable {
	/** Dataset nodes */
	LinkedList<OozieDatasetNode> dnodes = new LinkedList<OozieDatasetNode>();
	/** Program nodes */
	LinkedList<OozieProgramNode> pnodes = new LinkedList<OozieProgramNode>();
	LinkedList<OozieEdge> edges = new LinkedList<OozieEdge>();

	/**
	 * activeList
	 */
	private LinkedList<String> activeList = new LinkedList<String>();
	private LinkedList<Script> scriptList = new LinkedList<Script>();

	public void addActiveNode(String nodeId) {
		this.activeList.add(nodeId);
	}

	public boolean isActiveNode(String nodeId) {
		return this.activeList.contains(nodeId);
	}

	public void addScript(Script script){
		this.scriptList.add(script);
	}

	public LinkedList<Script> getScriptList(){
		return this.scriptList;
	}

	/** Return the total number of nodes in the graph */
	public int getNodeNum() {
		return dnodes.size() + pnodes.size();
	}

	public LinkedList<OozieDatasetNode> getDatasetNodes() {
		return dnodes;
	}

	public LinkedList<OozieProgramNode> getProgramNodes() {
		return pnodes;
	}

	public LinkedList<OozieEdge> getEdges() {
		return edges;
	}

	public List<String> getActiveList() {
		return activeList;
	}

	/** Add a dataset node */
	public void addDatasetNode(OozieDatasetNode node) {
		dnodes.add(node);
	}
	/** Add a program node */
	public void addProgramNode(OozieProgramNode node) {
		pnodes.add(node);
	}
	/** Add a edge */
	public void addEdge(OozieEdge edge) {
		edges.add(edge);
	}

	/**
	 *  Generate a XML string for the graph 
	 * */
	public String toXML() {
		StringBuffer sb = new StringBuffer(5000);
		sb.append("<graph>\n");
		for (OozieDatasetNode node : dnodes)
			sb.append(node.toXML());
		for (OozieProgramNode node : pnodes)
			sb.append(node.toXML());
		for (OozieEdge edge : edges)
			sb.append(edge.toXML());
		sb.append("</graph>");
		return sb.toString();
	}

	@Override
	public String toString() {
		return toXML();
	}
}
