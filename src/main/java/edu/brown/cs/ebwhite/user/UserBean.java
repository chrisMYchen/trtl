package edu.brown.cs.ebwhite.user;

import java.util.Set;

public class UserBean implements User {

  /**
   * User ID.
   */
  private int mID;
  /**
   * User's username.
   */
  private String username;
  /**
   * The user's first name.
   */
  private String firstName;
  /**
   * The user's last name.
   */
  private String lastName;
  /**
   * The user's email.
   */
  private String email;
  /**
   * The user's phone number.
   */
  private int phone;
  /**
   * Set of integers representing UserIDs of who is following this user.
   */
  private Set<Integer> followers;
  /**
   * Set of integers representing UserIDs of who this user is following.
   */
  private Set<Integer> following;
  /**
   * Set of integers representing userIDs that are pending to follow this user.
   */
  private Set<Integer> pending;
  /**
   * Set of integers representing userIds that this user is trying to follow.
   */
  private Set<Integer> pendingFollowing;
  /**
   * Constructor for the user bean.
   * @param id user ID.
   * @param followerSet this user's followers.
   * @param followingSet the users this user is following.
   * @param pendingSet this user's pending followers.
   * @param pendingFollowingSet the users this user is following pending.
   * @param uname the username.
   * @param fname the first name.
   * @param lname the last name.
   * @param mail the email.
   * @param pho the phone number.
   */
  public UserBean(int id, Set<Integer> followerSet, Set<Integer> followingSet,
      Set<Integer> pendingSet, Set<Integer> pendingFollowingSet, String uname,
      String fname, String lname, String mail, int pho) {
    this.mID = id;
    this.followers = followerSet;
    this.following = followingSet;
    this.pending = pendingSet;
    this.pendingFollowing = pendingFollowingSet;
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

  @Override
  public Set<Integer> getFollowing() {
    return following;
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
    return followers;
  }

  @Override
  public void addFollower(int f) {
    followers.add(f);
  }

  @Override
  public void removeFollower(int f) {
    followers.remove(f);
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

  @Override
  public void addFollowing(int f) {
    following.add(f);
  }

  @Override
  public void removeFollowing(int f) {
    following.remove(f);
  }

  @Override
  public Set<Integer> getPendingFollowing() {
    return pendingFollowing;
  }

  @Override
  public void addPendingFollowing(int f) {
    pendingFollowing.add(f);
  }

  @Override
  public void removePendingFollowing(int f) {
    pendingFollowing.remove(f);
  }
}
