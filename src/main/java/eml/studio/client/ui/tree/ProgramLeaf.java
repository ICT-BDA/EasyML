/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.shared.model.Program;

/**
 * TreeMenu Leaf for Programs
 */
public class ProgramLeaf extends Leaf<Program> {

	public ProgramLeaf(Program m) {
		super(m.getName(), m);
	    if(m.isETL()){
	        this.label.addStyleName("bda-treeleaf-etl");
	      }else if(m.isTensorflow())
	      	this.label.addStyleName("bda-treeleaf-tensorflow");
	      else{
	        if (m.isDistributed())
	          this.label.addStyleName("bda-treeleaf-distribute");
	        else
	          this.label.addStyleName("bda-treeleaf-standalone");
	      }


	}
}
