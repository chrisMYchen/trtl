package edu.brown.cs.ebwhite.user;

import java.util.Set;

public interface User extends Entity {

  @Override
  int getId();

  Set<Integer> getFriends();

}
