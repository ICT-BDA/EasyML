/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.orange.links.client.shapes.DrawableSet;

import eml.studio.client.controller.MonitorController;
import eml.studio.client.ui.connection.Connection;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.command.Parameter;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.program.CommonProgramWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.util.Util;
import eml.studio.shared.graph.OozieDatasetNode;
import eml.studio.shared.graph.OozieEdge;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;
import eml.studio.shared.util.ProgramUtil;

/**
 * This class is used to built Oozie graph xml
 */
public class OozieGraphBuilder {
	private Logger logger = Logger.getLogger(OozieGraphBuilder.class.getName());
	private OozieGraph graph = new OozieGraph();

    /**
     * Add widget to oozie graph
     * @param widget
     * @param x X-axis coordinates
     * @param y Y-axis coordinates
     */

	public void addWidget(BaseWidget widget, int x, int y) {
		
		
		if (widget instanceof DatasetWidget) {
			DatasetWidget dwidget = (DatasetWidget) widget;
			OozieDatasetNode node = new OozieDatasetNode();
			this.graph.addDatasetNode( node );
			node.init(widget.getId(), dwidget.getDataset().getId(),
					dwidget.getDataset().getPath(), x, y);
			
			return;
		}
		
		ProgramWidget progWidget = (ProgramWidget) widget;
		OozieProgramNode node = new OozieProgramNode();
		node.init(widget.getId(),progWidget.getProgram().getId(), progWidget.getWorkPath(), x, y,
					progWidget.getModel().getCurOozJobId(), widget
					.getInNodeShapes().size(),
					widget.getOutNodeShapes().size(), 
					ProgramUtil.isDistributed(progWidget.getProgram().getType()) ||
		            ProgramUtil.isETL(progWidget.getProgram().getType()));

		this.graph.addProgramNode(node);
		if (widget instanceof CommonProgramWidget) {
			CommonProgramWidget comWidget = (CommonProgramWidget) widget;
			wrapComNode(node, comWidget);
		}
		String cmdLine = progWidget.getProgramConf().getCommandLine();
		node.setCmdLine(cmdLine);

	}

    /**
     * Wrap common program widget
     * @param node
     * @param comWidget
     */
	private void wrapComNode(OozieProgramNode node, CommonProgramWidget comWidget){
		List<String> params = new LinkedList<String>();
		List<String> files = new LinkedList<String>();
		for (NodeShape shape : comWidget.getOutNodeShapes())
			files.add(((OutNodeShape) shape).getFileId());
		for (Parameter param : comWidget.getProgramConf()
				.getParameters())
			params.add(param.getIndex() + ":" + param.getParamValue());
		node.initAsCommonNode(files, params);
		
	}

    /**
     * Add edge to oozie graph
     * @param conn
     */
	public void addEdge(Connection conn) {
		NodeShape start = (NodeShape) conn.getStartShape();
		NodeShape end = (NodeShape) conn.getEndShape();
		OozieEdge edge = new OozieEdge();
		edge.init(start.getWidget().getId() + ":"
				+ start.getPortId(), end.getWidget().getId() + ":"
				+ end.getPortId());
		this.graph.addEdge(edge);
	}

	public OozieGraph asGraph(){
		return graph;
	}
	
	public OozieGraphBuilder(MonitorController controller){
		buildInitialization(controller);
		loadWidgets(controller);
	}
	
	/**
	 * Generate the random file name of the intermediate file
     * determine the active node
	 * @param controller
	 */
	private void buildInitialization(MonitorController controller){
		Map<String, BaseWidget> widgets = controller.getWidgets();
		logger.info("(Oozie Graph build Initialization ): Setting Widgets");
	    Set<String> activeNodes = new HashSet<String>();
	    
	    //Get the nodes that need to be re-executed
	    //(including non-successful nodes and their descendants)
	    for( Map.Entry<String, BaseWidget> entry: widgets.entrySet())
	    {
	      BaseWidget wgt = entry.getValue();
	      if( !( wgt instanceof ProgramWidget ) ) continue;
	      ProgramWidget pw = (ProgramWidget)wgt;
	      if( !ProgramUtil.isSuccess( pw.getAction() ))
	      {
	        activeNodes.add(pw.getId());
	        pw.getModel().setInActionList(true);
	        getActiveNode(pw,activeNodes);
	      }
	    }
	    
		for( Map.Entry<String, BaseWidget> entry: widgets.entrySet()){
			BaseWidget wgt = entry.getValue();
			if( !( wgt instanceof ProgramWidget ) ) continue;
			ProgramWidget pw = (ProgramWidget)wgt;
			//Skip the node that has been executed successfully
			if( !activeNodes.contains(pw.getId()) ){ 
				pw.getModel().setInActionList(false);
				continue;
			}
		
			pw.getModel().setCurOozJobId( ProgramWidget.Model.LATEST_OOZIE_JOBID );
	        pw.randFileName();

		}
		logger.info("Active node size = "+activeNodes.size());
		for(String nodeId : activeNodes)
		{
			logger.info("Active node :"+nodeId);
			graph.addActiveNode(nodeId);
		}
		
		logger.info("(Oozie Graph build Initialization ): Setting edges");
		Set<Connection> connDrawSet = controller.getConnDrawSet();
	    //Set the file entry address for each component dependent
	    for (Connection conn : connDrawSet) {
	    	
	    	NodeShape start_shape = (NodeShape)conn.getStartShape();
	    	
	    	//Get the shape of the line
	        OutNodeShape src_shape = (OutNodeShape) conn.getStartShape();
	        NodeShape dst_shape = (NodeShape) conn.getEndShape();
	        ProgramWidget dst_wgt = (ProgramWidget) dst_shape.getWidget();
	        dst_wgt.getProgramConf().setInputFilePath( dst_shape.getPortId(), 
        			src_shape.getWorkflowPath() + "/" + src_shape.getFileId(), src_shape.getFileId());
	    }
	}

    /**
     * Load all widgets form controller
     * @param controller
     */
	private void loadWidgets(MonitorController controller){
		Map<String, BaseWidget> widgets = controller.getWidgets();
	    for (Map.Entry<String, BaseWidget> entry : widgets.entrySet()) {
	      BaseWidget widget = entry.getValue();
	      Integer x = widget.getController().getWidgetPanel().getWidgetLeft(widget);
	      Integer y = widget.getController().getWidgetPanel().getWidgetTop(widget);
	      int tx = Util.parseStrToInt(String.valueOf(x));
	      int ty = Util.parseStrToInt(String.valueOf(y));
	      
	      this.addWidget(widget, tx, ty);
	    }

	    Set<Connection> connDrawSet = controller.getConnDrawSet();
	    for (Connection conn : connDrawSet) {
	    	this.addEdge(conn);
	    }
	}
	
	/**
	 * Get active node from DAG to execute
	 * 
	 * @param pw  Program widget
	 * @param activeNodes  Active nodes
	 */
	private  void  getActiveNode(ProgramWidget pw,Set<String> activeNodes)
	{
		List<OutNodeShape> outNodeShapes = pw.getOutNodeShapes();
		logger.info("n(out nodes) = " + outNodeShapes.size() );

		for ( OutNodeShape shape: outNodeShapes) {
			DrawableSet<Connection> connections = shape.getConnections();
			logger.info("size="+connections.size());
			if(connections != null)
			{
				for(Connection connection : connections) {
					InNodeShape inShape = (InNodeShape) connection.getEndShape();
					ProgramWidget dstWidget = (ProgramWidget) inShape.getWidget();
					logger.info("【Id】"+dstWidget.getId());
					String id = dstWidget.getId();
					if (!activeNodes.contains(id))
					{
						dstWidget.getModel().setInActionList(true);
						activeNodes.add(dstWidget.getId());
					}
					getActiveNode(dstWidget, activeNodes);
				}
			}
		}
	}
}
