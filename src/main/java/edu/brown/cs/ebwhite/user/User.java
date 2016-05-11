package edu.brown.cs.ebwhite.user;

import java.util.Set;

public interface User extends Entity {

  @Override
  int getId();
  /**
   * Returns the set of userIDs of a user's followers.
   * @return userIDs of followers.
   */
  Set<Integer> getFollowers();
  /**
   * Returns the username of the User.
   * @return the username.
   */
  String getUsername();
  /**
   * Returns the first name of the user.
   * @return first name.
   */
  String getFirstName();
  /**
   * Returns the last name of the user.
   * @return last name.
   */
  String getLastName();
  /**
   * Returns the email of the user.
   * @return email.
   */
  String getEmail();
  /**
   * Adds a follower to the user's follower set.
   * @param f the userID of the follower to add.
   */
  void addFollower(int f);
  /**
   * Removes a follower from the user's followers.
   * @param f the userID of the follower to remove.
   */
  void removeFollower(int f);
  /**
   * Gets all pending follower requests.
   * @return the set of userIDs of pending follower requests.
   */
  Set<Integer> getPending();
  /**
   * Add a new pending follower by their userID.
   * @param f the pending follower's userID.
   */
  void addPending(int f);
  /**
   * Remove a pending follower by their userID.
   * @param f the pending follower's userID.
   */
  void removePending(int f);
  /**
   * Gets the set of userIDs that the user is following.
   * @return set of userIDs the user is following.
   */
  Set<Integer> getFollowing();
  /**
   * Add a userID to follow to the user.
   * @param f the userId.
   */
  void addFollowing(int f);
  /**
   * Remove a userID from who you are following.
   * @param f the userID.
   */
  void removeFollowing(int f);
  /**
   * The set of pending following requests for this user.
   * @return the set of userIDs that you are requesting to follow.
   */
  Set<Integer> getPendingFollowing();
  /**
   * Add a userID to your pending to follow.
   * @param f the userID.
   */
  void addPendingFollowing(int f);
  /**
   * Remove a userID from your pending to follow.
   * @param f the userID.
   */
  void removePendingFollowing(int f);
  /**
   * Gets the phone number of the user.
   * @return phone number.
   */
  int getPhone();

}
