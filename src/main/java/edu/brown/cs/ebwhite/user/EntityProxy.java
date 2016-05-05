package edu.brown.cs.ebwhite.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.ebwhite.database.Db;

/**
 * Abstract entity class is extended by classes that query a database.
 * @author kzliu
 *
 * @param <E>
 *            proxy of type entity
 */
public abstract class EntityProxy<E extends Entity> implements Entity {

  /**
   * unique id for Entity.
   */
  private int id;
  /**
   * Internal Entity the EntityProxy represents to.
   */
  private E internal;

  /**
   * Map of all previously cached Entities.
   */
  private static Map<Integer, Entity> cache = new ConcurrentHashMap<>();

  /**
   * Constructor for an EntityProxy.
   * @param idVal unique id representing the entity
   */
  public EntityProxy(int idVal) {
    this.id = idVal;
  }

  /**
   * Fills an entity from the cache if it exists, otherwise calls for child
   * fill(Connection conn) methods.
   */
  public void fill() {
    if (internal != null) {
      return;
    }
    @SuppressWarnings("unchecked")
    E i = (E) cache.get(id);
    internal = i;

    if (internal == null) {
      try (Connection conn = Db.getConnection()) {
        fillNew(conn);
        cache.put(id, internal);
      } catch (SQLException e) {
        System.out.println("ERROR: Could not connect to database.");
        System.exit(1);
      }
    }

  }


  @Override
  public int getId() {
    return id;
  }

  /**
   * Setting for internal.E
   * @param intern new internal value
   */
  public void setInternal(E intern) {
    internal = intern;
  }

  /**
   * Gets internal value of proxy.
   * @return E which is what the proxy represents
   */
  public E getInternal() {
    return internal;
  }

  /**
   * Fills the connection if the entity is not yet in the cache.
   * @param conn Connection to database
   * @throws SQLException if the entity does not exist in the database
   */
  protected abstract void fillNew(Connection conn) throws SQLException;



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((internal == null) ? 0 : internal.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    @SuppressWarnings("unchecked")
    EntityProxy<E> other = (EntityProxy<E>) obj;
    if (id != other.id)
      return false;
    if (internal == null) {
      if (other.internal != null)
        return false;
    } else if (!internal.equals(other.internal))
      return false;
    return true;
  }

  @Override
  public String toString() {
    if (internal == null) {
      return String.format("(%s)", this.id);
    }
    return String.format("(%s, '%s')", this.id, this.internal.toString());
  }

}
