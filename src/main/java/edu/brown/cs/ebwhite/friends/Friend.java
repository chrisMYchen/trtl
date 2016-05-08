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
 * @author hk125
 *
 */
public class Friend {

  /**
   * add a friend for to a user's list of friends.
   *
   * @param userID
   *          the ID of the user
   * @param friendID
   *          the ID of the friend
   * @return true if successful
   * @throws SQLException
   *           if there is an error with the query
   */
   public static boolean requestFollow(int userID, int friendID)
   throws SQLException {

     String post = "SELECT * FROM user_follower WHERE userid=? AND follower_id = ?;";
     try (Connection conn = Db.getConnection()) {
       try (PreparedStatement prep = conn.prepareStatement(post)) {
         prep.setInt(1, friendID);
         prep.setInt(2, userID);
         try (ResultSet rs = prep.executeQuery()) {
           if (rs.next()) {
             return false;
           }
         }
       }
     }
     post = "INSERT INTO user_pending VALUES (?, ?)";
     User f = new UserProxy(friendID);
     User u = new UserProxy(userID);
     try (Connection conn = Db.getConnection()) {
       try (PreparedStatement prep = conn.prepareStatement(post)) {
         prep.setInt(1, friendID);
         prep.setInt(2, userID);
         prep.executeUpdate();
         f.addPending(userID);
         u.addPendingFollowing(friendID);
         return true;
       }
     }
   }

  /**
   * add a friend for to a user's list of friends.
   *
   * @param userID
   *          the ID of the user
   * @param friendID
   *          the ID of the friend
   * @return true if successful
   * @throws SQLException
   *           if there is an error with the query
   */
  public static boolean acceptPendingRequest(int userID, String friendUsername)
      throws SQLException {

    String post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";
    int friendID = TurtleQuery.getUserID(friendUsername);
    assert (friendID != -1);

    User u = new UserProxy(userID);
    User f = new UserProxy(friendID);
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, friendID);
        prep.executeUpdate();
        u.removePending(friendID);
        f.removePendingFollowing(userID);
      }
    }
    post = "INSERT INTO user_follower VALUES (?, ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, friendID);
        prep.executeUpdate();
        u.addFollower(friendID);
        f.addFollowing(userID);
        return true;
      }
    }
  }

  /**
   * unfollow a friend from a user's list of following.
   *
   * @param userID
   *          the ID of the user
   * @param friendID
   *          the ID of the User u = new UserProxy(friendID); friend
   * @return true if successful
   * @throws SQLException
   *           if there is an error with the query
   */
  public static void unfollow(int userID, int friendID)
      throws SQLException {

    User f = new UserProxy(friendID);
    User u = new UserProxy(userID);

    String post = "DELETE FROM user_follower WHERE (userid = ? AND follower_id = ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, friendID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removeFollower(userID);
        u.removeFollowing(friendID);
      }
    }

    post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, friendID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removePending(userID);
        u.removePendingFollowing(friendID);
      }
    }
  }

  /**
   * removes a follower from your followers.
   *
   * @param userID
   *          the id of the user
   * @param friendUsername
   *          the username of the friend
   * @return true if successful
   * @throws SQLException
   *           if there is an error with the query
   */
  public static void removeFollower(int userID, int friendID)
      throws SQLException {

    User f = new UserProxy(friendID);
    User u = new UserProxy(userID);

    String post = "DELETE FROM user_follower WHERE (userid = ? AND follower_id = ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, friendID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removeFollowing(userID);
        u.removeFollower(friendID);
      }
    }

    post = "DELETE FROM user_pending WHERE (userid = ? AND pending_id = ?);";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, friendID);
        prep.setInt(2, userID);
        prep.executeUpdate();
        f.removePendingFollowing(userID);
        u.removePending(friendID);
      }
    }
  }

  // /**
  // * get a list of user's friends.
  // * @param userID
  // * the ID of the user
  // * @return a list of IDs of the user's friends
  // * @throws SQLException
  // * if there is an error with the query
  // */
  // public static List<Integer> getFriends(int userID) throws SQLException
  // {
  //
  // List<Integer> friends = new ArrayList<>();
  //
  // final int FRIENDCOL = 1;
  //
  // String getFriends = "SELECT * FROM user_friend WHERE userid = ?";
  //
  // try (Connection conn = Db.getConnection()) {
  //
  // try (PreparedStatement prep = conn.prepareStatement(getFriends)) {
  //
  // prep.setInt(1, userID);
  //
  // try (ResultSet rs = prep.executeQuery()) {
  //
  // while (rs.next()) {
  //
  // int friendID = rs.getInt(FRIENDCOL);
  // friends.add(friendID);
  //
  // }
  // }
  // }
  // }
  // return friends;
  // }

}
