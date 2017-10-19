/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.util;

import com.google.gwt.xml.client.Node;

import java.util.Date;

public class Util {

	/**
	 * Format generated xml doc by indentation
	 *
	 * @param node
	 * @param indent
	 * @return
	 */
	public static String formatXML(Node node, String indent) {
		StringBuilder formatted = new StringBuilder();

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			StringBuilder attributes = new StringBuilder();
			for (int k = 0; k < node.getAttributes().getLength(); k++) {
				attributes.append(" ");
				attributes.append(node.getAttributes().item(k).getNodeName());
				attributes.append("=\"");
				attributes.append(node.getAttributes().item(k).getNodeValue());
				attributes.append("\"");
			}

			formatted.append(indent);
			formatted.append("<");
			formatted.append(node.getNodeName());
			formatted.append(attributes.toString());
			if (!node.hasChildNodes()) {
				formatted.append("/>\n");
				return formatted.toString();
			}
			if ((node.hasChildNodes() && node.getFirstChild().getNodeType() == Node.TEXT_NODE)) {
				formatted.append(">");
			} else {
				formatted.append(">\n");
			}

			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				formatted
				.append(formatXML(node.getChildNodes().item(i), indent + "   "));
			}

			if (node.hasChildNodes()
					&& node.getFirstChild().getNodeType() != Node.TEXT_NODE) {
				formatted.append(indent);
			}
			formatted.append("</");
			formatted.append(node.getNodeName());
			formatted.append(">\n");
		} else {
			if (node.toString().trim().length() > 0) {
				formatted.append(node.toString());
			}
		}

		String xml = formatted.toString();
		xml = xml.replace("&semi;", ";");
		return xml;
	}

	/**
	 * Get Cookie Date
	 * @return Now Date
	 */
	public static Date getCookieExpireDate() {
		Date now = new Date();
		now.setMonth(now.getMonth() + 1);
		;

		return now;
	}

	/**
	 * Parse string value to int type, string value may be double or int type
	 * 
	 * @param value
	 * @return
	 */
	public static int parseStrToInt(String value)
	{
		if(value.contains("."))
		{
			value = value.substring(0,value.indexOf("."));
		}
		return Integer.valueOf(value);
	}

}
