package edu.brown.cs.ebwhite.friends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.brown.cs.ebwhite.database.Db;
import edu.brown.cs.ebwhite.database.TurtleQuery;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

/**
 * Interact with the friend system.
 *
 * @author TRTL team
 *
 */
public class Friend {

  /**
   * add a friend for to a user's list of friends.
   *
   * @param userID the ID of the user
   * @param toFollowID the ID of the friend
   * @return true if successful, false if already following
   * @throws SQLException if there is an error with the query
   */
  public static boolean requestFollow(int userID, int toFollowID)
      throws SQLException, IllegalArgumentException {
    if (userID != toFollowID) {
      String post = "SELECT * FROM user_follower WHERE userid=? AND follower_id = ?;";
      try (Connection conn = Db.getConnection()) {
        try (PreparedStatement prep = conn.prepareStatement(post)) {
          prep.setInt(1, toFollowID);
          prep.setInt(2, userID);
          try (ResultSet rs = prep.executeQuery()) {
            if (rs.next()) {
              return false;
            }
          }
        }
      }
      post = "INSERT INTO user_pending VALUES (?, ?)";
      User f = new UserProxy(toFollowID);
      User u = new UserProxy(userID);
      try (Connection conn = Db.getConnection()) {
        try (PreparedStatement prep = conn.prepareStatement(post)) {
          prep.setInt(1, toFollowID);
          prep.setInt(2, userID);
          prep.executeUpdate();
          f.addPending(userID);
          u.addPendingFollowing(toFollowID);
          return true;
        }
      }
    }
    throw new IllegalArgumentException("You can't follow yourself!");
  }

  /**
   * add a friend for to a user's list of friends.
   *
   * @param userID the ID of the user
   * @param pendingFollowerString the pending follower username as a string
   * @return true if successful
   * @throws SQLException if there is an error with the query
   */
  public static boolean acceptPendingRequest(int userID,
      String pendingFollowerString) throws SQLException {

    String post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";
    int pendingFollowerID = TurtleQuery.getUserID(pendingFollowerString);
    assert (pendingFollowerID != -1);

    User u = new UserProxy(userID);
    User f = new UserProxy(pendingFollowerID);
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, pendingFollowerID);
        prep.executeUpdate();
        u.removePending(pendingFollowerID);
        f.removePendingFollowing(userID);
      }
    }
    post = "INSERT INTO user_follower VALUES (?, ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, pendingFollowerID);
        prep.executeUpdate();
        u.addFollower(pendingFollowerID);
        f.addFollowing(userID);
        return true;
      }
    }
  }

  /**
   * unfollow a friend from a user's list of following.
   *
   * @param userID the ID of the user
   * @param followingID the ID of the friend to unfollow
   * @throws SQLException if there is an error with the query
   */
  public static void unfollow(int userID, int followingID)
      throws SQLException {

    User f = new UserProxy(followingID);
    User u = new UserProxy(userID);

    String post = "DELETE FROM user_follower WHERE (userid = ? AND follower_id = ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, followingID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removeFollower(userID);
        u.removeFollowing(followingID);
      }
    }

    post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, followingID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removePending(userID);
        u.removePendingFollowing(followingID);
      }
    }
  }

  /**
   * removes a follower from your followers.
   *
   * @param userID the id of the user
   * @param followerID the username of the friend
   * @throws SQLException if there is an error with the query
   */
  public static void removeFollower(int userID, int followerID)
      throws SQLException {

    User f = new UserProxy(followerID);
    User u = new UserProxy(userID);

    String post = "DELETE FROM user_follower WHERE (userid = ? AND follower_id = ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, followerID);
        prep.executeUpdate();
        f.removeFollowing(userID);
        u.removeFollower(followerID);
      }
    }

    post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, followerID);
        prep.executeUpdate();
        f.removePendingFollowing(userID);
        u.removePending(followerID);
      }
    }
  }

}
