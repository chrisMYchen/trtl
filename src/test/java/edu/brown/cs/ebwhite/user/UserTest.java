package edu.brown.cs.ebwhite.user;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.ebwhite.database.Db;

public class UserTest {

  private static String dbPath = "turtlDB.sqlite3";

  @BeforeClass
  public static void setUpClass() throws Exception {
    Db.database(dbPath);
  }

  @Test
  public void invalidName() {

    boolean exists = true;
    User u1 = null;
    try {
      u1 = UserProxy.ofName("thiswillnotwork");
    } catch (SQLException e) {
      exists = false;
    }
    assertTrue(true);
    assertTrue(u1 == null);
  }

  @Test
  public void findUser() {
    boolean exists = true;
    User u1 = null;
    try {
      u1 = UserProxy.ofName("KZLiu");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("HERE1");
      exists = false;
    }
    assertTrue(exists);
    assertTrue(u1 != null);
    assertTrue(u1.getUsername().equals("KZLiu"));
    assertTrue(u1.getFollowing().size() > 0);
  }

  @Test
  public void sameUser() {
    boolean exists = true;
    User u1 = null;
    User u2 = null;
    try {
      u1 = UserProxy.ofName("KZLiu");
      u2 = new UserProxy(u1.getId());
    } catch (SQLException e) {
      exists = false;
    }
    assertTrue(exists);
    assertTrue(u1 != null);
    assertTrue(u1.getUsername().equals(u2.getUsername()));
    assertTrue(u1.getId() == u2.getId());
    assertTrue(u1.getFollowers().equals(u2.getFollowers()));
    assertTrue(u1.equals(u2));
  }

  @Test
  public void differentUser() {
    // Db.setDb(dbPath);
    boolean exists = true;
    User u1 = null;
    User u2 = null;
    try {
      u1 = UserProxy.ofName("KZLiu");
      u2 = UserProxy.ofName("ebwhite");
    } catch (SQLException e) {
      System.out.println("HERE1");
      exists = false;
    }
    assertTrue(exists);
    assertTrue(u1 != null);
    assertTrue(!u1.getUsername().equals(u2.getUsername()));
    assertTrue(u1.getId() != u2.getId());
    assertTrue(!u1.equals(u2));
  }

}
