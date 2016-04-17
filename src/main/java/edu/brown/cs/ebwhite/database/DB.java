package edu.brown.cs.ebwhite.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB implements AutoCloseable{
  private final Connection conn;

  public DB(String path) throws ClassNotFoundException, SQLException{
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + path;
    conn = DriverManager.getConnection(urlToDB);
  }

  public Connection getConnection(){
    return conn;
  }

  @Override
  public void close() throws SQLException{
    conn.close();
  }
}
