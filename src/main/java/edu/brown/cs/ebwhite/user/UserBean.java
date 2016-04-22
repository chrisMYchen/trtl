package edu.brown.cs.ebwhite.user;

import java.util.Set;

public class UserBean implements User {

  /**
   * Node ID.
   */
  private int mID;

  private Set<Integer> friends;

  public UserBean(int id, Set<Integer> friendSet) {
    this.mID = id;
    this.friends = friendSet;
  }

  @Override
  public int getId() {
    return mID;
  }

  @Override
  public Set<Integer> getFriends() {
    return friends;
  }


}
