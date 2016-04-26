package edu.brown.cs.ebwhite.friends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ebwhite.database.Db;
import edu.brown.cs.ebwhite.database.TurtleQuery;

/**
 * Interact with the friend system.
 *
 * @author hk125
 *
 */
public class Friend {

  /**
   * add a friend for to a user's list of friends.
   * @param userID
   *          the ID of the user
   * @param friendID
   *          the ID of the friend
   * @throws SQLException
   *           if there is an error with the query
   */
  public static boolean addFriend(int userID, String friendUsername)
      throws SQLException {
    String post = "INSERT INTO user_friend VALUES (?, ?), (?, ?)";
    int friendID = -1;
    friendID = TurtleQuery.getUserID(friendUsername);
    if (friendID == -1) {
      return false;
    } else {
      try (Connection conn = Db.getConnection()) {
        try (PreparedStatement prep = conn.prepareStatement(post)) {
          prep.setInt(1, userID);
          prep.setInt(2, friendID);
          prep.setInt(3, friendID);
          prep.setInt(4, userID);
          prep.executeUpdate();
          return true;
        }
      }
    }

  }

  /**
   * remove a friend from a user's list of friends.
   * @param userID
   *          the ID of the user
   * @param friendID
   *          the ID of the friend
   * @throws SQLException
   *           if there is an error with the query
   */
  public static boolean removeFriend(int userID, String friendUsername)
      throws SQLException {
    String post = "DELETE FROM user_friend WHERE (userid = ? AND friendid = ?) OR (userid = ? AND friendid = ?)";
    int friendID = -1;
    friendID = TurtleQuery.getUserID(friendUsername);
    if (friendID == -1) {
      return false;
    } else {
      try (Connection conn = Db.getConnection()) {
        try (PreparedStatement prep = conn.prepareStatement(post)) {
          prep.setInt(1, userID);
          prep.setInt(2, friendID);
          prep.setInt(3, friendID);
          prep.setInt(4, userID);
          prep.executeUpdate();
          return true;
        }
      }
    }
  }

  /**
   * get a list of user's friends.
   * @param userID
   *          the ID of the user
   * @return a list of IDs of the user's friends
   * @throws SQLException
   *           if there is an error with the query
   */
  public static List<Integer> getFriends(int userID) throws SQLException {

    List<Integer> friends = new ArrayList<>();

    final int FRIENDCOL = 1;

    String getFriends = "SELECT * FROM user_friend WHERE userid = ?";

    try (Connection conn = Db.getConnection()) {

      try (PreparedStatement prep = conn.prepareStatement(getFriends)) {

        prep.setInt(1, userID);

        try (ResultSet rs = prep.executeQuery()) {

          while (rs.next()) {

            int friendID = rs.getInt(FRIENDCOL);
            friends.add(friendID);

          }
        }
      }
    }
    return friends;
  }

}
