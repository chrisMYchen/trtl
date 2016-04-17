package edu.brown.cs.ebwhite.database;

import java.sql.Connection;
import java.sql.SQLException;

import edu.brown.cs.ebwhite.entities.Entity;

@FunctionalInterface
public interface EntityUpdater<T,E extends Entity<T>>{
  public void update(Connection c, E entity) throws SQLException;
}
