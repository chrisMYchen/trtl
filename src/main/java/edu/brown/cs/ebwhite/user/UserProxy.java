package edu.brown.cs.ebwhite.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserProxy extends EntityProxy<User> implements User {
  /**
   * Constructor for ActorProxy.
   * @param id unique id representing actor
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

    setInternal(new UserBean(getId(), friends));
  }

}
