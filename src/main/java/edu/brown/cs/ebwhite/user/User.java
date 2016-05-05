package edu.brown.cs.ebwhite.user;

import java.util.Set;

public interface User extends Entity {

  @Override
  int getId();

  Set<Integer> getFollowers();

  String getUsername();

  String getFirstName();

  String getLastName();

  String getEmail();

  void addFollower(int f);

  void removeFollower(int f);

  Set<Integer> getPending();

  void addPending(int f);

  void removePending(int f);

  Set<Integer> getFollowing();

  void addFollowing(int f);

  void removeFollowing(int f);

  Set<Integer> getPendingFollowing();

  void addPendingFollowing(int f);

  void removePendingFollowing(int f);

  int getPhone();

}
