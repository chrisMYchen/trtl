package edu.brown.cs.ebwhite.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.ebwhite.database.Db;

public class UserProxy extends EntityProxy<User> implements User {
  /**
   * Constructor for ActorProxy.
   *
   * @param id
   *          unique id representing actor
   */
  public UserProxy(int id) {
    super(id);
  }

  // @Override
  // public Set<Integer> getFriends() {
  // fill();
  // return getInternal().getFriends();
  // }

  @Override
  protected void fillNew(Connection conn) throws SQLException {
    Set<Integer> followers = new HashSet<>();
    Set<Integer> pending = new HashSet<>();
    String username = null;
    String firstname = null;
    String lastname = null;
    String email = null;
    int phone = -1;

    String query = "SELECT follower_id FROM user_follower WHERE userid=?;";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setInt(1, getId());
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          int friendID = rs.getInt(1);
          followers.add(friendID);
        }
      }
    }

    query = "SELECT pending_id FROM user_pending WHERE userid=?;";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setInt(1, getId());
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          int pendingID = rs.getInt(1);
          pending.add(pendingID);
        }
      }
    }

    query = "SELECT * FROM user WHERE id=?;";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setInt(1, getId());
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          username = rs.getString(2);
          firstname = rs.getString(4);
          lastname = rs.getString(5);
          email = rs.getString(6);
          phone = rs.getInt(7);
        }
      }
    }
    setInternal(new UserBean(getId(), followers, pending, username, firstname,
        lastname, email, phone));
  }

  public static User ofName(String username) throws SQLException {
    String query = "SELECT id FROM user WHERE username=?;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.setString(1, username);
        try (ResultSet rs = prep.executeQuery()) {
          if (rs.next()) {
            return new UserProxy(rs.getInt(1));
          }
        }
      }
    }
    return null;
  }

  @Override
  public String getUsername() {
    fill();
    return getInternal().getUsername();
  }

  @Override
  public String getFirstName() {
    fill();
    return getInternal().getFirstName();
  }

  @Override
  public String getLastName() {
    fill();
    return getInternal().getLastName();
  }

  @Override
  public String getEmail() {
    fill();
    return getInternal().getEmail();
  }

  @Override
  public int getPhone() {
    fill();
    return getInternal().getPhone();
  }

  // @Override
  // public void addFriend(int f) {
  // fill();
  // getInternal().addFriend(f);
  // }
  //
  // @Override
  // public void removeFriend(int f) {
  // fill();
  // getInternal().removeFriend(f);
  // }

  @Override
  public Set<Integer> getFollowers() {
    fill();
    return getInternal().getFollowers();
  }

  @Override
  public void addFollower(int f) {
    fill();
    getInternal().addFollower(f);
  }

  @Override
  public void removeFollower(int f) {
    fill();
    getInternal().removeFollower(f);
  }

  @Override
  public Set<Integer> getPending() {
    fill();
    return getInternal().getPending();
  }

  @Override
  public void addPending(int f) {
    fill();
    getInternal().addPending(f);
  }

  @Override
  public void removePending(int f) {
    fill();
    getInternal().removePending(f);
  }
}
