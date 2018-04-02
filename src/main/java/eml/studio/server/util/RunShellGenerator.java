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
import eml.studio.shared.util.DatasetType;

/**
 * Generator local version run.sh
 */
public class RunShellGenerator {
	/**
	 * generate new cmdLine (content of run.sh)
	 * @param cmdLine origin version
	 * @return string of new cmdline
	 */
	public String generate(String cmdLine) throws CommandParseException {
		Commander commander = CommandParser.parse(cmdLine);
		StringBuffer sb = new StringBuffer("");
		sb.append("#! /bin/bash\n" +
				"#standalone\n" +
				"echo +++++++++++++log information+++++++++++\n" +
				"source /etc/profile\n" +
				"echo $PATH \n" +
				"exit_code=0\n\n" +
				"echo mk action-dir...\n" +
				"actionId=${3##*/}\n" +
				"mkdir $actionId\n" +
				"echo download libaries ...\n" +
				"hdfs dfs -get $2/* 1>>stdout 2>>stderr\n" +
				"#((exit_code=exit_code|$?))\n" +
				"chmod -R +x *\n" +
				"echo download input hdfs files ...\n");

		// readlCmd itype_1 input_1 ... itype_n input_n otype_1 output_1 ... otype_n
		// output_n
		int idx = 3;
		for (FileHolder fp : commander.getInFileHolders()) {
			idx++;
			if(DatasetType.DIRECTORY.getDesc().equals(fp.getContentType()))
			{
				sb.append(String.format(
								"hdfs dfs -get ${%d}/${%d##*/} ${%d##*/} 1>>stdout 2>>stderr\n" +
								"fi\n\n", idx + 1, idx + 1, idx + 1));
				
			}else{
				sb.append(String.format(
						"if [ \"${%d}\" = \"HFile\" ]\n" +
								"then\n" +
								"\t hdfs dfs -getmerge ${%d}/${%d##*/} ${%d##*/} 1>>stdout 2>>stderr\n",
								idx, idx + 1, idx + 1, idx + 1));

				sb.append(String.format(
						"else\n" +
								"\t hdfs dfs -get ${%d}/${%d##*/} ${%d##*/} 1>>stdout 2>>stderr\n" +
								"fi\n\n", idx + 1, idx + 1, idx + 1));
			}
			idx++;
		}

		int outIdx = idx;  //backup output first index
		for (FileHolder fp : commander.getOutFileHolders()) {
			idx++;
			if(DatasetType.DIRECTORY.getDesc().equals(fp.getContentType()))
			{
				sb.append(String.format("mkdir ${%d##*/}\n", idx));
			}
		}
		
		sb.append("\necho execute command line\n" +
				"echo $1 1>>stdout 2>>stderr\n" +
				"eval $1 1>>stdout 2>>stderr\n" +
				"((exit_code=exit_code|$?))\n\n");
		
		idx = outIdx;
		for (FileHolder fp : commander.getOutFileHolders()) {
			idx++;
			sb.append( String.format("mkdir $actionId/${%d##*/}\n", idx));
			sb.append( String.format("mv ${%d##*/} $actionId/${%d##*/} 1>>stdout 2>>stderr\n", idx, idx) );
		}

		sb.append("mv stdout stderr $actionId\n" +
				"hdfs dfs -put $actionId $3\n" +
				"hdfs dfs -chmod -R 777 $3\n\n" +
				"exit $exit_code");

		System.out.print(sb.toString());
		return sb.toString();
	}
}
