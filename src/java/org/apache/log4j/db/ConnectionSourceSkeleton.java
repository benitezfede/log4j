/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.db;

import org.apache.log4j.db.dialect.Util;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorHandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


/**
 * @author Ceki G&uuml;lc&uuml;
 */
public abstract class ConnectionSourceSkeleton implements ConnectionSource {
  protected String user = null;
  protected String password = null;
  protected ErrorHandler errorHandler = null;

  // initially we have an unkonw dialect
  protected int dialectCode = UNKNOWN_DIALECT;
  protected boolean supportsGetGeneratedKeys = false;
  protected boolean supportsBatchUpdates = false;
  

  /**
   * Learn relevant information about this connection source.
   *
   */
  public void discoverConnnectionProperties() {
    try {
      Connection connection = getConnection();
      if(connection == null) {
        LogLog.warn("Could not get a conneciton");
        return;
      }
      DatabaseMetaData meta = connection.getMetaData();
      supportsGetGeneratedKeys = Util.supportsGetGeneratedKeys(meta);
      supportsBatchUpdates = Util.supportsBatchUpdates(meta);
      dialectCode = Util.discoverSQLDialect(meta);
    } catch (SQLException se) {
      LogLog.warn("Could not discover the dialect to use.", se);
    }
  }

  /**
   * Does this connection support the JDBC Connection.getGeneratedKeys method?
   */
  public boolean supportsGetGeneratedKeys() {
    return supportsGetGeneratedKeys;
  }

  /**
   * Get teh errorHandler for this connection source
   */
  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  /**
   * Sets the error handler.
   * @param errorHandler  the error handler to set
   */
  public void setErrorHandler(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }

  public int getSQLDialectCode() {
    return dialectCode;
  }

  /**
   * Get the password for this connection source.
   */
  public String getPassword() {
    return password;
  }


  /**
   * Sets the password.
   * @param password The password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get the user for this connection source.
   */
  public String getUser() {
    return user;
  }

  /**
   * Sets the username.
   * @param username The username to set
   */
  public void setUser(String username) {
    this.user = username;
  }
  
  /**
   * Does this connection support batch updates?
   */
  public boolean supportsBatchUpdates() {
    return supportsBatchUpdates;
  }

}
