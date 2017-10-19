/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.db;

import java.util.ArrayList;

public abstract class ETLDBService {
    public abstract ArrayList<String> getTables(String url, String user, String passwd);
    public abstract ArrayList<String> getColumns(String url, String user, String passwd, String table);
}
