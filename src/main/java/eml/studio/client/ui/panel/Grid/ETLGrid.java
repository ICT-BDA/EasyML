/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Grid;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL base information grid.
 */
public abstract class ETLGrid extends Grid {
	// Store the values of TextBox for value check
	List<FocusWidget> paramBoxs = new ArrayList<FocusWidget>();

	public ETLGrid(){
		super(3,2);
	}
	public void lock(){
		for( FocusWidget wgt : paramBoxs ){
			wgt.setEnabled( false );
		}
	}

	public void unlock(){
		for( FocusWidget wgt : paramBoxs ){
			wgt.setEnabled( true );
		}
	}
}
