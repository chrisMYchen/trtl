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

  @Override
  public Set<Integer> getFriends() {
    fill();
    return getInternal().getFriends();
  }

  @Override
  protected void fillNew(Connection conn) throws SQLException {
    Set<Integer> friends = new HashSet<>();
    String username = null;
    String firstname = null;
    String lastname = null;
    String email = null;
    int phone = -1;

    String query = "SELECT friendid FROM user_friend WHERE userid=?;";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setInt(1, getId());
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          int friendID = rs.getInt(1);
          friends.add(friendID);
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
    setInternal(new UserBean(getId(), friends, username, firstname, lastname,
        email, phone));
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
}
