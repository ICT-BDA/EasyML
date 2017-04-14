/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.ui.widget.program;

import bda.studio.client.ui.widget.command.FileDescription;

/**
 * The conference of Program wedget
 */
public interface ProgramConf {
  
  public String getCommandLine();
  public int getInputFileCount();
  public int getOutputFileCount();
  public FileDescription getInputFile(int index);
  public FileDescription getOutputFile(int index);
  public void setInputFilePath(int index, String path, String filename);
  public void setOutputFilePath(int index, String path, String filename);
  
}
