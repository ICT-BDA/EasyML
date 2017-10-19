/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Base node in Oozie workflow graph
 */
public abstract class OozieNode implements IsSerializable {
	protected String id = "";

	/** moduleId id */
	protected String moduleId = "";

	/** (x, y) is the position in the panel */
	protected int x = 0;
	protected int y = 0;

	public OozieNode() {
	}

	public void init(String id, String moduleId,
			int x, int y) {
		this.id = id;
		this.moduleId = moduleId;
		this.x = x;
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public abstract String toXML();

	@Override
	public String toString() {
		return toXML();
	}
}
