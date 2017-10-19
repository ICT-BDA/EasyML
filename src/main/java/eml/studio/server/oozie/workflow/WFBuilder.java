/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import eml.studio.client.util.UUID;
import eml.studio.server.db.SecureDao;
import eml.studio.shared.graph.OozieDatasetNode;
import eml.studio.shared.graph.OozieEdge;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieNode;
import eml.studio.shared.graph.OozieProgramNode;
import eml.studio.shared.model.Program;

/**
 * The workflow graph builder
 */
public class WFBuilder {

	WFGraph wfGraph = new WFGraph();

	/**
	 * A {@link WFGraph} should be built from an {@link OozieGraph}
	 */
	public void buildFromOozieGraph(OozieGraph graph) throws Exception {
		//first load all program nodes into the WFGraph,
		//and built relations between program nodes
		//according to the edges in OoziGrpah
		loadGraph(graph);

		//insert start node and end node for Oozie workflow into the DAG
		insertStartAndEnd();

		//insert Fork and Join Action for parallel
		insertForkAndJoin();
	}

	/**
	 * Wrap a {@link OozieProgramNode} as {@link NodeDef}
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private NodeDef wrapAsNodeDef(OozieProgramNode node) throws Exception {
		NodeDef action = null;

		Program query = new Program();
		query.setId(node.getModuleId());
		System.out.println( node.getModuleId() );
		Program prog = SecureDao.getObject(query);

		if (prog.isDistributed() || prog.isETL()) {
			// If is distribute program
			action = new DistributeActionNodeDef(prog, node.getId(), node.getCmdLine());
		} else {
			action = new ShellActionNodeDef(prog, node.getId(), node.getCmdLine());
		}
		return action;
	}

	/**
	 * Add all {@link OozieProgramNode} in {@link OozieGraph} into {@link WFGraph},
	 * parse all file dependencies according to the edges in {@link OozieProgramNode}
	 * @param graph the DAG graph
	 * @throws Exception
	 */
	private void loadGraph(OozieGraph graph) throws Exception {
		for (OozieProgramNode node : graph.getProgramNodes()) {
			if (graph.isActiveNode(node.getId())) {
				NodeDef action = wrapAsNodeDef(node);
				wfGraph.addActionNode(action);
			}
		}

		Map<String, OozieNode> nodeMap = new HashMap<String,OozieNode>();
		List<String> nodeOutputFiles = null;
		for(OozieDatasetNode node : graph.getDatasetNodes() )
		{
			nodeOutputFiles = new ArrayList<String>();
			nodeMap.put(node.getId(), node);
			if(graph.isActiveNode(node.getId()))
			{
				nodeOutputFiles.add(node.getFile());
				wfGraph.addNodeOutputFile(node.getId(), nodeOutputFiles,null);  
			}
		}
		for(OozieProgramNode node : graph.getProgramNodes() )
		{
			nodeOutputFiles = new ArrayList<String>();
			nodeMap.put(node.getId(), node);
			if(graph.isActiveNode(node.getId()))
			{
				nodeOutputFiles.addAll(node.getFiles());
				wfGraph.addNodeOutputFile(node.getId(), nodeOutputFiles,node.getWorkPath());
			}
		}

		for (OozieEdge edge : graph.getEdges()) {
			String src = edge.getSrc();
			String dst = edge.getDst();

			String[] src_tok = src.split(":");
			String[] dst_tok = dst.split(":");

			int srcPort = Integer.parseInt(src_tok[1]);
			int dstPort = Integer.parseInt(dst_tok[1]);

			OozieNode srcNode = nodeMap.get(src_tok[0]);
			if (srcNode == null)
				continue;

			String file = null;
			if (srcNode instanceof OozieDatasetNode)
				file = ((OozieDatasetNode) srcNode).getFile();
			else{
				OozieProgramNode node = ((OozieProgramNode) srcNode);
				file = node.getWorkPath() + node.getFiles().get(srcPort);
			}
			wfGraph.addEdge(src_tok[0], dst_tok[0], srcPort, dstPort, file);
		}
	}

	private void insertStartAndEnd() {
		for (NodeDef node : wfGraph.nodeMap.values()) {
			if (node.getInDegree() == 0) {
				wfGraph.start.addOutNode(node);
				node.addInNode(wfGraph.start);
			}
			if (node.getOutDegree() == 0) {
				node.addOutNode(wfGraph.end);
				wfGraph.end.addInNode(node);
			}
		}

		if (wfGraph.start.getOutDegree() == 0 && wfGraph.end.getInDegree() == 0) {
			wfGraph.start.addOutNode(wfGraph.end);
			wfGraph.end.addInNode(wfGraph.start);
		}
	}

	private void insertForkAndJoin() {
		NodeDef start = wfGraph.start;
		NodeDef end = wfGraph.end;
		Queue<NodeDef> que = new LinkedList<NodeDef>();
		// Insert fork and join node
		NodeDef cur_node = start;
		int count = 0;
		do {
			if (count++ > 10)
				break;
			for (NodeDef suc_node : cur_node.getOutNodes()) {
				suc_node.delInNode(cur_node);
				if (suc_node.getInDegree() == 0) {
					que.add(suc_node);
				}
			}

			cur_node.clearOutNodes();

			if (que.size() > 1) {// If the out degree greater than 1, then need to insert fork/join node（the node should be used in pair）
				// A fork node splits one path of execution into multiple
				// concurrent
				// paths of execution.
				// A join node waits until every concurrent execution path of a
				// previous
				// fork node arrives to it.
				// The fork and join nodes must be used in pairs. The join node
				// assumes
				// concurrent execution paths
				// are children of the same fork node.
				String uuid = UUID.randomUUID();
				NodeDef fork = new ForkNodeDef("fork-" + uuid);
				NodeDef join = new JoinNodeDef("join-" + uuid);

				// Modify cur_node descent node  to fork node
				buildLink(cur_node, fork);

				while (!que.isEmpty()) {
					buildLink(fork, que.remove());
				}

				//Insert join node between pre_node and suc_node node
				for (NodeDef pre_node : fork.getOutNodes()) {// For children node of fork node
					for (NodeDef suc_node : pre_node.getOutNodes()) {// Insert join node between pre_node and suc_node
						suc_node.delInNode(pre_node);
						join.addOutNode(suc_node);// Point the join node to suc_node
					}

					//Modify pre_node descent node which only include join node
					pre_node.clearOutNodes();
					buildLink(pre_node, join);
				}

				// Modify the join descent node's father node to join node
				for (NodeDef suc_node : join.getOutNodes()) {
					suc_node.addInNode(join);
				}
				cur_node = join;
			} else {
				buildLink(cur_node, que.peek());
				cur_node = que.remove();
			}
		} while (cur_node != end);

	}

	/**
	 * build link between to nodes
	 * @param pre_node the previous node
	 * @param suc_node the successor node
	 */
	private void buildLink(NodeDef pre_node, NodeDef suc_node) {
		pre_node.addOutNode(suc_node);
		suc_node.addInNode(pre_node);
	}

	public WFGraph asWFGraph(){
		return this.wfGraph;
	}
}
