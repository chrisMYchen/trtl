package edu.brown.cs.ebwhite.database;

import java.sql.Connection;
import java.sql.SQLException;

import edu.brown.cs.ebwhite.entities.Entity;

@FunctionalInterface
public interface EntityCreator<T,E extends Entity<T>>{
  public E create(Connection c, T id) throws SQLException;
}
