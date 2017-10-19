/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Edge of a Oozie workflow graph
 */
public class OozieEdge implements IsSerializable{

	/** id of the source node */
	private String src;
	/** id of the target node */
	private String dst;

	public OozieEdge(){}

	public void init(String src, String dst) {
		this.setSrc(src);
		this.setDst(dst);
	}

	public String getSrc() {
		return src;
	}

	public String getDst() {
		return dst;
	}

	/** Generate a XML string for the edge */
	public String toXML() {
		StringBuffer sb = new StringBuffer(100);
		sb.append("<edge>\n");
		sb.append("  <source>" + getSrc() + "</source>\n");
		sb.append("  <destination>" + getDst() + "</destination>\n");
		sb.append("</edge>\n");
		return sb.toString();
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	@Override
	public String toString() {
		return "OozieEdge [src=" + src + ", dst=" + dst +"]";
	}
}