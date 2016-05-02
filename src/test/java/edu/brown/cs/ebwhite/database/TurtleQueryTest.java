package edu.brown.cs.ebwhite.database;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.ebwhite.friends.Friend;
import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;

public class TurtleQueryTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    try {
      Db.database("testDB.sqlite3");

      // String createUserTable = "CREATE TABLE user "
      // +
      // "(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, "
      // + "firstname TEXT, lastname TEXT, email TEXT, phone INTEGER);";
      // String createNotesTable = "CREATE TABLE notes "
      // + "(id INTEGER PRIMARY KEY AUTOINCREMENT, userid INT, timestamp INT, "
      // +
      // "lat REAL, long REAL, coslat REAL, sinlat REAL, coslong REAL, sinlong REAL, "
      // + "text TEXT, private INT, FOREIGN KEY (userid) "
      // + "REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE);";
      // String createFriendTable = "CREATE TABLE user_friend "
      // + "(userid INTEGER, friendid INTEGER, FOREIGN KEY (friendid) "
      // + "REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE);";
      // try (Connection conn = Db.getConnection()) {
      // try (PreparedStatement prep = conn.prepareStatement(createUserTable)) {
      // prep.executeQuery();
      // }
      // try (PreparedStatement prep1 = conn.prepareStatement(createNotesTable))
      // {
      // prep1.executeQuery();
      // }
      // try (PreparedStatement prep2 =
      // conn.prepareStatement(createFriendTable)) {
      // prep2.executeQuery();
      // }
      // }

      TurtleQuery.addUser("hk125", "hemang", "Hemang", "Kaul",
          "hemangkaul@gmail.com", 1);
      TurtleQuery.addUser("ebwhite", "eli", "Eli", "White",
          "eliwhite@gmail.com", 2);
      TurtleQuery.addUser("kzliu", "katie", "Katie", "Liu",
          "katieliu@gmail.com", 3);
      TurtleQuery.addUser("cchen", "chris", "Chris", "Chen",
          "chrischen@gmail.com", 4);

      Friend.addFriend(1, "ebwhite");

      TurtleQuery.postNote(1, System.currentTimeMillis(), 41, -71,
          "this is one note", 0);
      TurtleQuery.postNote(1, System.currentTimeMillis(), 41, -71,
          "this is another note", 0);
      TurtleQuery.postNote(1, System.currentTimeMillis(), 41, -71,
          "this is a private note", 1);
    } catch (ClassNotFoundException e1) {
      System.out.println("ERROR: could not connect to DB");
      e1.printStackTrace();
    }

  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    // String dropUserTable = "DROP TABLE user;";
    // String dropNotesTable = "DROP TABLE notes;";
    // String dropFriendsTable = "DROP TABLE user_friend;";
    // try (Connection conn = Db.getConnection()) {
    // try (PreparedStatement prep = conn.prepareStatement(dropUserTable)) {
    // prep.executeQuery();
    // }
    // try (PreparedStatement prep1 = conn.prepareStatement(dropNotesTable)) {
    // prep1.executeQuery();
    // }
    // try (PreparedStatement prep2 = conn.prepareStatement(dropFriendsTable)) {
    // prep2.executeQuery();
    // }
    // }
  }

  @Before
  public void setUp() {
    // (Optional) Code to run before each test case goes here.
  }

  @After
  public void tearDown() {
    // (Optional) CgedInTestode to run after each test case goes here.
  }

  @Test
  public void getNotesTest() {
    LatLong here = new LatLong(41, -71);
    try {
      List<Note> anonNotes = TurtleQuery.getNotes(-1, here, 10, 0, 3,
          System.currentTimeMillis());
      List<Note> loggedNotes = TurtleQuery.getNotes(2, here, 10, 0, 3,
          System.currentTimeMillis());
      List<Note> notFriendNotes = TurtleQuery.getNotes(3, here, 10, 0, 3,
          System.currentTimeMillis());

      // System.out.println(anonNotes.size());

      assertTrue(anonNotes.size() == 3);
      assertTrue(loggedNotes.size() == 3);
      assertTrue(notFriendNotes.size() == 3);
      assertTrue(anonNotes.get(0).getText().equals("this is another note"));
      assertTrue(loggedNotes.get(0).getText().equals("this is a private note"));
      assertTrue(loggedNotes.get(1).getText().equals("this is another note"));
      assertTrue(notFriendNotes.get(0).getText().equals("this is another note"));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void updateNotesTest() {
    LatLong here = new LatLong(41, -71);
    try {

      Friend.removeFriend(4, "hk125");
      List<Note> beforeFriends = TurtleQuery.getNotes(4, here, 10, 0, 3,
          System.currentTimeMillis());
      Friend.addFriend(4, "hk125");
      TurtleQuery.updateNotes(4, here, 10, 0, 3, System.currentTimeMillis());
      List<Note> afterFriends = TurtleQuery.getNotes(4, here, 10, 0, 4,
          System.currentTimeMillis());

      assertTrue(beforeFriends.size() == 3);
      assertTrue(afterFriends.size() == 4);
      assertTrue(beforeFriends.get(0).getText().equals("this is another note"));
      assertTrue(afterFriends.get(0).getText().equals("this is a private note"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void loginValidTest() {
    int hkUID;
    int ebUID;
    int kzUID;
    int ccUID;
    try {
      hkUID = TurtleQuery.loginValid("hk125", "hemang");
      ebUID = TurtleQuery.loginValid("ebwhite", "eli");
      kzUID = TurtleQuery.loginValid("kzliu", "katie");
      ccUID = TurtleQuery.loginValid("cchen", "chris");
      assertTrue(hkUID == 1);
      assertTrue(ebUID == 2);
      assertTrue(kzUID == 3);
      assertTrue(ccUID == 4);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void getUserIdTest() {
    int hkUID;
    int ebUID;
    int kzUID;
    int ccUID;
    try {
      hkUID = TurtleQuery.getUserID("hk125");
      ebUID = TurtleQuery.getUserID("ebwhite");
      kzUID = TurtleQuery.getUserID("kzliu");
      ccUID = TurtleQuery.getUserID("cchen");
      assertTrue(hkUID == 1);
      assertTrue(ebUID == 2);
      assertTrue(kzUID == 3);
      assertTrue(ccUID == 4);
    } catch (SQLException e) {
      System.out.println(e);
    }

  }
}

// adding a user

// int uID = TurtleQuery.addUser("newUser", "password", "meep", null,
// "asdffdsa@gmail.com", -1);
// System.out.println(uID);
//
// TurtleQuery.postNote(3, 1010100, 71, -41, "hemang3. early note.",
// 1);
// TurtleQuery.postNote(4, 1010100, 71, -41, "new user. early note.",
// 1);

// TurtleQuery.postNote(1, 10101010, 71, -41,
// "This is the second note we ever posted", 0);
// TurtleQuery.postNote(1, 10101010, 71, -41,
// "This is the first private note by katie we ever posted", 1);
// TurtleQuery.postNote(2, 10101010, 71, -41,
// "This is the second private note by chris we ever posted", 1);
// Testing posting notes and time sorting
// TurtleQuery.postNote(2, 101010, 71, -41,
// "This is the third note.", 0);

// notes = TurtleQuery.getNotesAnonymous(new LatLong(71, -41), 500,
// 4, 12,
// 10101010);
// if (notes.isEmpty()) {
// System.out.println("No notes from this timestamp.");
// }
