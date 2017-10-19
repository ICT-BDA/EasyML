/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

/**
 * Generator script program's run.sh
 */
public class ProgramAbleRunShellGenerator {

	  /**
	   * Generate the script program cmdLine (content of run.sh)
	   * 
	   * @param inputCount input file count
	   * @return outputCount  output file count
	   */
	public String generate(int inputCount, int outputCount) {

		StringBuffer sb = new StringBuffer("");
		sb.append("#! /bin/bash\n" +
				"#standalone programable\n" +
				"echo +++++++++++++log information+++++++++++\n" +
                "source /etc/profile\n" +
                "echo $PATH \n" +
				"exit_code=0\n\n" +
				"echo mk action-dir...\n" +
				"actionId=${3##*/}\n" +
				"mkdir $actionId\n" +
				"#((exit_code=exit_code|$?))\n" +
				"echo download input hdfs files ...\n");

		// readlCmd itype_1 input_1 ... itype_n input_n otype_1 output_1 ... otype_n
		// output_n
		int idx = 3;
		for ( int i = 0; i < inputCount; ++ i ) {
			idx++;

			sb.append(String.format(
					"if [ \"${%d}\" = \"HFile\" ]\n" +
							"then\n" +
							"\t hdfs dfs -getmerge ${%d}/${%d##*/} ${%d##*/} 1>>stdout 2>>stderr\n",
							idx, idx + 1, idx + 1, idx + 1));

			sb.append(String.format(
					"else\n" +
							"\t hdfs dfs -get ${%d}/${%d##*/} ${%d##*/} 1>>stdout 2>>stderr\n" +
							"fi\n\n", idx + 1, idx + 1, idx + 1));

			idx++;
		}

		sb.append("\necho execute command line\n" +
				"echo $1 1>>stdout 2>>stderr\n" +
				"eval $1 1>>stdout 2>>stderr\n" +
				"((exit_code=exit_code|$?))\n\n");

		for (int i = 0; i < outputCount; ++ i ) {
			idx++;
			sb.append( String.format("mkdir $actionId/${%d##*/}\n", idx));
			sb.append( String.format("mv ${%d##*/} $actionId/${%d##*/} 1>>stdout 2>>stderr\n", idx, idx) );
		}

		sb.append(""+
				"mv stdout stderr $actionId\n" +
				"hdfs dfs -put $actionId $3\n" +
				"hdfs dfs -chmod -R 777 $3\n\n" +
				"exit $exit_code");

		System.out.print(sb.toString());
		return sb.toString();
	}

}
