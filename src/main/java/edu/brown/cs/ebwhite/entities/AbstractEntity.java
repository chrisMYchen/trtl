package edu.brown.cs.ebwhite.entities;

import java.util.Objects;

public abstract class AbstractEntity<T> implements Entity<T>{

  @Override
  public abstract T getID();

  @Override
  public boolean equals(Object obj){
    if (obj == null) {
        return false;
    }
    if (obj == this) {
        return true;
    }
    if(!(obj instanceof Entity)){
        return false;
    }
    @SuppressWarnings("unchecked")
    Entity<T> e = (Entity<T>) obj;
    return this.getID().equals(e.getID());
  }

  @Override
  public int hashCode(){
    return Objects.hash(getID());
  }
}
