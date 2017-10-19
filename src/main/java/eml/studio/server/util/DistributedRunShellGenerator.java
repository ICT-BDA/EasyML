/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.ui.widget.command.CommandParser;
import eml.studio.client.ui.widget.command.Commander;
import eml.studio.client.ui.widget.command.FileHolder;

/**
 * TODO: combine with RunShellGenerator using uniq generate(),
 * which automatically determine local or distributed
 */
public class DistributedRunShellGenerator extends RunShellGenerator {
	@Override
	public String generate(String cmdLine) throws CommandParseException {
		Commander commander = CommandParser.parse(cmdLine);

		StringBuffer sb = new StringBuffer("");
		sb.append("#! /bin/bash\n" + "#distribute\n"
				+"echo +++++++++++++Log information+++++++++++\n" +
				"source /etc/profile\n"
				+"echo $PATH \n"
				+ "exit_code=0\n\n" + "echo make action directory on hdfs ... \n"
				+ "hdfs dfs -mkdir $3\n\n" + "echo download libaries ...\n"
				+ "hdfs dfs -get $2/* 1>>stdout 2>>stderr\n"
				+ "#((exit_code=exit_code|$?))\n" + "chmod -R +x *\n\n");

		int idx = 3;
		for (FileHolder fp : commander.getOutFileHolders()) {
			idx++;
			sb.append("echo making file moduleId directory ${" + idx + "}...\n");
			sb.append("hdfs dfs -mkdir ${" + idx + "} 1>>stdout 2>>stderr\n");
		}

		sb.append("source /etc/profile\n");
		sb.append("\necho execute command line\n"
				+ "echo $1 1>>stdout 2>>stderr\n"
				+ "eval $1 1>>stdout 2>>stderr\n"
				+ "((exit_code=exit_code|$?))\n\n");

		sb.append("if [ \"$exit_code\" -ne \"0\" ]\n" + "then\n"
				+ "        hdfs dfs -put stderr $3\n"
				+ "        hdfs dfs -put stdout $3\n" + "        exit $exit_code\n"
				+ "fi\n\n" + "echo upload output files to hdfs\n");

		sb.append("hdfs dfs -put stderr $3\n" + "hdfs dfs -put stdout $3\n\n"
				+ "hdfs dfs -chmod -R 777 $3\n\n"
				+ "exit $exit_code");


		return sb.toString();
	}
}
