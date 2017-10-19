/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.FileDescription;

/**
 * The conference of Program widget
 */
public interface ProgramConf {

	/**
	 * Get program widget commandline
	 * @return  command line string
	 */
	public String getCommandLine();

	/**
	 * Get program input file number
	 * @return  input file number
	 */
	public int getInputFileCount();

	/**
	 * Get program output file number
	 * @return output file number
	 */
	public int getOutputFileCount();

	/**
	 * Get input file description by index
	 * 
	 * @param index  input file index(position of the input shape node in widget)
	 * @return
	 */
	public FileDescription getInputFile(int index);

	/**
	 * Get output file description by index
	 * 
	 * @param index  output file index(position of the output shape node in widget)
	 * @return
	 */
	public FileDescription getOutputFile(int index);

	/**
	 * Set input file path of the program widget
	 * 
	 * @param index  input file index
	 * @param path  input file path 
	 * @param filename  input file name
	 */
	public void setInputFilePath(int index, String path, String filename);

	/**
	 * Set output file path of the program widget
	 * 
	 * @param index  output file index
	 * @param path  output file path 
	 * @param filename  output file name
	 */
	public void setOutputFilePath(int index, String path, String filename);

}
