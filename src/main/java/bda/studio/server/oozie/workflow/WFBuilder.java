/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.server.oozie.workflow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import bda.studio.client.util.UUID;
import bda.studio.server.db.SecureDao;
import bda.studio.shared.graph.OozieDatasetNode;
import bda.studio.shared.graph.OozieEdge;
import bda.studio.shared.graph.OozieGraph;
import bda.studio.shared.graph.OozieNode;
import bda.studio.shared.graph.OozieProgramNode;
import bda.studio.shared.model.Program;

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
			// 如果为分布式程序
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
		for(OozieDatasetNode node : graph.getDatasetNodes() )nodeMap.put(node.getId(), node);
		for(OozieProgramNode node : graph.getProgramNodes() )nodeMap.put(node.getId(), node);
		
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
		// 插入fork和join节点
		NodeDef cur_node = start;
		int count = 0;
		do {
			if (count++ > 10)
				break;
			for (NodeDef suc_node : cur_node.getOutNodes()) {
				suc_node.delInNode(cur_node);
				if (suc_node.getInDegree() == 0) {// 删除cur_node后度为0的节点
					que.add(suc_node);
				}
			}

			cur_node.clearOutNodes();

			if (que.size() > 1) {// 如果出度大于1则需要插入fork/join节点（必须成对配套使用）
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
				// 修改cur_node的后继为fork
				buildLink(cur_node, fork);

				while (!que.isEmpty()) {
					buildLink(fork, que.remove());
				}

				// 在pre_node和suc_node之间插入join节点
				for (NodeDef pre_node : fork.getOutNodes()) {// 对于fork的所有子节点
					for (NodeDef suc_node : pre_node.getOutNodes()) {// 在pre_node和suc_node之间插入join节点
						suc_node.delInNode(pre_node);
						join.addOutNode(suc_node);// 将join指向 suc_node
					}

					// 修改pre_node只有join一个后继节点
					pre_node.clearOutNodes();
					buildLink(pre_node, join);
				}

				// join的所有后继的前继节点设为join
				for (NodeDef suc_node : join.getOutNodes()) {
					suc_node.addInNode(join);
				}
				cur_node = join;
			} else {
				buildLink(cur_node, que.peek());
				cur_node = que.remove();
			}
			// logger.info( cur_node.getName() );
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
