package edu.brown.cs.ebwhite.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up database connection.
 *
 */
public final class Db {

  /**
   * instantiates new Db.
   */
  private Db() {

  }

  /**
   * Path to database.
   */
  private static String mFile;
  /**
   * Connection to database.
   */
  private static ThreadLocal<Connection> conn;

  /**
   * Pass in path to the database file to work with.
   *
   * @param file path to database.
   * @throws ClassNotFoundException Problem with Class.
   */
  public static void database(String file) throws ClassNotFoundException {
    mFile = file;
    Class.forName("org.sqlite.JDBC");
    conn = new ThreadLocal<>();
  }

  /**
   * Returns new connection to database.
   *
   * @return Connection to database.
   * @throws SQLException SQLException with database.
   * @throws NullPointerException Connection was null.
   */
  public static synchronized Connection getConnection() throws SQLException {
    // make this work with threadlocal
    String urlToDB = "jdbc:sqlite:" + mFile;
    conn.set(DriverManager.getConnection(urlToDB));
    if (conn.get() == null) {
      System.out.println("conn.get() is null");
      throw new NullPointerException();
    }
    try (Statement stat = conn.get().createStatement()) {
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    }
    return conn.get();
  }

  /**
   * Close database connection.
   *
   * @throws SQLException SQLException.
   */
  public static void close() throws SQLException {
    if (conn.get() != null) {
      conn.get().close();
    }
  }
}
