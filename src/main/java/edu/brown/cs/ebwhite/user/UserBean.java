package edu.brown.cs.ebwhite.user;

import java.util.Set;

public class UserBean implements User {

  /**
   * Node ID.
   */
  private int mID;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private int phone;
  private Set<Integer> friends;
  private Set<Integer> pending;

  public UserBean(int id, Set<Integer> friendSet, Set<Integer> pendingSet,
      String uname, String fname,
      String lname, String mail, int pho) {
    this.mID = id;
    this.friends = friendSet;
    this.pending = pendingSet;
    this.username = uname;
    this.firstName = fname;
    this.lastName = lname;
    this.email = mail;
    this.phone = pho;
  }

  @Override
  public int getId() {
    return mID;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public int getPhone() {
    return phone;
  }

  // @Override
  // public Set<Integer> getFriends() {
  // return friends;
  // }
  //
  // @Override
  // public void addFriend(int f) {
  // friends.add(f);
  // }
  //
  // @Override
  // public void removeFriend(int f) {
  // friends.remove(f);
  // }

  @Override
  public Set<Integer> getFollowers() {
    return friends;
  }

  @Override
  public void addFollower(int f) {
    friends.add(f);
  }

  @Override
  public void removeFollower(int f) {
    friends.remove(f);
  }

  @Override
  public Set<Integer> getPending() {
    return pending;
  }

  @Override
  public void addPending(int f) {
    pending.add(f);

  }

  @Override
  public void removePending(int f) {
    pending.remove(f);

  }

}
