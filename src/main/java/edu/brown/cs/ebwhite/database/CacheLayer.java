package edu.brown.cs.ebwhite.database;

import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;

import edu.brown.cs.ebwhite.entities.Entity;

public class CacheLayer<T>{
  Connection db;
  Map<T, Entity<T>> cache = new HashMap<>();

  public CacheLayer(Connection db){
    this.db = db;
  }

  public <E extends Entity<T>> E get(T id, EntityCreator<T,E> f)
    throws SQLException{
    if(cache.containsKey(id)){
      return (E) cache.get(id);
    }
    else{
      E entity;
      entity = f.create(db, id);
      cache.put(id, entity);
      return entity;
    }
  }

  public void remove(T id){
    cache.remove(id);
  }

  public <E extends Entity<T>> void update(E entity, EntityUpdater<T,E> f)
    throws SQLException{
    remove(entity.getID());
    f.update(db, entity);
    remove(entity.getID());
  }

}
