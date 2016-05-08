package edu.brown.cs.ebwhite.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;

public class TurtleQuery {
  private final static int NIDCOL = 1;
  private final static int TIMECOL = 3;
  private final static int LATCOL = 4;
  private final static int LNGCOL = 5;
  private final static int TXTCOL = 6;
  private final static int VOTECOL = 13;
  private final static int AUTOKEYS = Statement.RETURN_GENERATED_KEYS;

  public static List<Note> getNotes(int userID, LatLong loc, double radius,
      int minPost, int maxPost, long timeStamp, int filter) throws SQLException {
    double allowed_distance_latitude = radius / 110575; // this is in
    // meters

    double inputLat = loc.getLat();
    double inputLng = loc.getLng();
    double allowed_distance_longitude = Math.abs(radius
        / (111320 * Math.cos(inputLat)));
    double bottom_lat = inputLat - allowed_distance_latitude;
    double top_lat = inputLat + allowed_distance_latitude;
    double left_lng = inputLng - allowed_distance_longitude;
    double right_lng = inputLng + allowed_distance_longitude;

    if (userID != -1) {
      return getNotesLoggedIn(userID, bottom_lat, top_lat, left_lng, right_lng,
          minPost, maxPost, timeStamp, filter);
    } else {
      return getNotesAnonymous(bottom_lat, top_lat, left_lng, right_lng,
          minPost, maxPost, timeStamp);
    }
  }

  // String notes = "SELECT * FROM notes AS n, "

  public static List<Note> getNotesAnonymous(double bottom_lat, double top_lat,
      double left_lng, double right_lng, int minPost, int maxPost,
      long timeStamp) throws SQLException {
    String getNotes = "SELECT *, COUNT(v.user_id) as NumberOfVotes FROM notes AS n "
        + "LEFT JOIN vote as v ON n.id = v.note_id LEFT JOIN image_note AS i ON n.id=i.noteid "
        + "WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?) AND (timestamp < ?) "
        + "AND (private = 0) GROUP BY n.id ORDER BY timestamp;";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, timeStamp);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            // int uID = rs.getInt(UIDCOL);
            long time = rs.getLong(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            int votes = rs.getInt(VOTECOL);
            // uID just not placed in note if not logged in
            String image = rs.getString("path");
            Note n = new Note.NoteBuilder(noteID, time).setContent(txt)
                .setLat(lat).setLng(lng).setImage(image).setVote(votes).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }

  }

  public static List<Note> getNotesLoggedIn(int userID, double bottom_lat,
      double top_lat, double left_lng, double right_lng, int minPost,
      int maxPost, long timeStamp, int filter) throws SQLException {
    String getNotes = "SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat, n.long, n.text, n.private, n.NumberOfVotes, n.path,"
        + " CASE WHEN v.user_id=? THEN 'True' ELSE 'False' END"
        + " FROM (SELECT DISTINCT *, COUNT(v.user_id) as NumberOfVotes"
        + " FROM (SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat, n.long, n.text, n.private"
        + " FROM notes as n, user_follower as uf"
        + " WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?) AND (timestamp < ?) ";


    if (filter == 0) {
      getNotes += " AND (n.private = 0 OR n.userid = ? OR"
          + " (n.private = 1 AND uf.follower_id = ? AND uf.userid = n.userid)) "
          + " ORDER BY n.timestamp DESC) AS n ";
    }

    if (filter == 1) {
      getNotes += " AND ((n.userid = ?) OR "
          + " (uf.follower_id = ? AND uf.userid = n.userid)) "
          + " ORDER BY n.timestamp DESC) AS n ";
    }

    else if (filter == 2) {
      getNotes += " AND n.userid = ? ORDER BY n.timestamp DESC) AS n ";
    }

    getNotes += " LEFT JOIN vote as v ON n.id = v.note_id"
        + " LEFT JOIN image_note AS i ON i.noteid=n.id GROUP BY n.id) AS n"
        + " LEFT JOIN vote as v ON v.note_id=n.id AND v.user_id=?;";

    System.out.println(getNotes);
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setInt(1, userID);
        prep.setDouble(2, left_lng);
        prep.setDouble(3, right_lng);
        prep.setDouble(4, bottom_lat);
        prep.setDouble(5, top_lat);
        prep.setLong(6, timeStamp);
        if (filter == 0) {
          prep.setInt(7, userID);
          prep.setInt(8, userID);
          prep.setInt(9, userID);
          // prep.setInt(8, maxPost - minPost);
          // prep.setInt(9, minPost);
        } else if (filter == 1) {
          prep.setInt(7, userID);
          prep.setInt(8, userID);
          prep.setInt(9, userID);
          // prep.setInt(8, maxPost - minPost);
          // prep.setInt(9, minPost);
        } else if (filter == 2) {
          prep.setInt(7, userID);
          prep.setInt(8, userID);
          // prep.setInt(7, maxPost - minPost);
          // prep.setInt(8, minPost);
        }
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {

            // System.out.println("one note");
            int noteID = rs.getInt(1);
            int uID = rs.getInt(2);
            long time = rs.getLong(3);
            double lat = rs.getDouble(4);
            double lng = rs.getDouble(5);
            String txt = rs.getString(6);
            int priv = rs.getInt(7);
            int vote = rs.getInt(8);
            String image = rs.getString("path");
            boolean voteStatus = Boolean.parseBoolean(rs.getString(10));

            // System.out.println("noteID " + noteID + " : " + voteStatus
            // + "string: " + rs.getString(10));
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).setPrivate(priv)
                .setImage(image).setVote(vote).setVoteStatus(voteStatus)
                .build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }
  }

  public static List<Note> updateNotes(int userID, LatLong loc, double radius,
      int minPost, int maxPost, long start_time, long end_time, int filter) throws SQLException {
    double allowed_distance_latitude = radius / 110575; // this is in
    // meters
    double inputLat = loc.getLat();
    double inputLng = loc.getLng();
    double allowed_distance_longitude = Math.abs(radius
        / (111320 * Math.cos(inputLat)));
    double bottom_lat = inputLat - allowed_distance_latitude;
    double top_lat = inputLat + allowed_distance_latitude;
    double left_lng = inputLng - allowed_distance_longitude;
    double right_lng = inputLng + allowed_distance_longitude;
    if (userID != -1) {
      return updateNotesLoggedIn(userID, bottom_lat, top_lat, left_lng,
          right_lng, minPost, maxPost, start_time, end_time, filter);
    } else {
      return updateNotesAnonymous(bottom_lat, top_lat, left_lng, right_lng,
          minPost, maxPost, start_time, end_time);
    }
  }

  public static List<Note> updateNotesAnonymous(double bottom_lat,
      double top_lat, double left_lng, double right_lng, int minPost,
      int maxPost, long start_time, long end_time) throws SQLException {
    String getNotes = "SELECT *, COUNT(v.user_id) as NumberOfVotes FROM notes AS n "
        + "LEFT JOIN vote as v ON n.id = v.note_id LEFT JOIN image_note AS i ON n.id=i.noteid "
        + "WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?) AND (timestamp >= ? AND timestamp < ?) "
        + "AND (private = 0) GROUP BY n.id ORDER BY timestamp;";

    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, start_time);
        prep.setLong(6, end_time);
        // prep.setInt(6, maxPost - minPost);
        // prep.setInt(7, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            // int uID = rs.getInt(UIDCOL);
            long time = rs.getLong(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            int vote = rs.getInt(VOTECOL);
            String image = rs.getString("path");
            // uID just not placed in note if not logged in
            Note n = new Note.NoteBuilder(noteID, time).setContent(txt)
                .setLat(lat).setLng(lng).setImage(image).setVote(vote).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }
  }

  public static List<Note> updateNotesLoggedIn(int userID, double bottom_lat,
      double top_lat, double left_lng, double right_lng, int minPost,
      int maxPost, long start_time, long end_time, int filter) throws SQLException {
    String getNotes = "SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat, n.long, n.text, n.private, n.NumberOfVotes, n.path,"
        + " CASE WHEN v.user_id=? THEN 'True' ELSE 'False' END"
        + " FROM (SELECT DISTINCT *, COUNT(v.user_id) as NumberOfVotes"
        + " FROM (SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat, n.long, n.text, n.private"
        + " FROM notes as n, user_follower as uf"
        + " WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?) AND (timestamp >= ? AND timestamp < ?) ";

    if (filter == 0) {
      getNotes += " AND (n.private = 0 OR n.userid = ? OR"
          + " (n.private = 1 AND uf.follower_id = ? AND uf.userid = n.userid)) "
          + " ORDER BY n.timestamp DESC) AS n ";
    }

    if (filter == 1) {
      getNotes += " AND ((n.userid = ?) OR "
          + " (uf.follower_id = ? AND uf.userid = n.userid)) "
          + " ORDER BY n.timestamp DESC) AS n ";
    }

    else if (filter == 2) {
      getNotes += " AND n.userid = ? ORDER BY n.timestamp DESC) AS n ";
    }

    getNotes += " LEFT JOIN vote as v ON n.id = v.note_id"
        + " LEFT JOIN image_note AS i ON i.noteid=n.id GROUP BY n.id) AS n"
        + " LEFT JOIN vote as v ON v.note_id=n.id AND v.user_id=?;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setInt(1, userID);
        prep.setDouble(2, left_lng);
        prep.setDouble(3, right_lng);
        prep.setDouble(4, bottom_lat);
        prep.setDouble(5, top_lat);
        prep.setLong(6, start_time);
        prep.setLong(7, end_time);
        if (filter == 0) {
          prep.setInt(8, userID);
          prep.setInt(9, userID);
          prep.setInt(10, userID);
          // prep.setInt(8, maxPost - minPost);
          // prep.setInt(9, minPost);
        } else if (filter == 1) {
          prep.setInt(8, userID);
          prep.setInt(9, userID);
          prep.setInt(10, userID);
          // prep.setInt(8, maxPost - minPost);
          // prep.setInt(9, minPost);
        } else if (filter == 2) {
          prep.setInt(8, userID);
          prep.setInt(9, userID);
          // prep.setInt(7, maxPost - minPost);
          // prep.setInt(8, minPost);
        }
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {

            // System.out.println("one note");
            int noteID = rs.getInt(1);
            int uID = rs.getInt(2);
            long time = rs.getLong(3);
            double lat = rs.getDouble(4);
            double lng = rs.getDouble(5);
            String txt = rs.getString(6);
            int priv = rs.getInt(7);
            int vote = rs.getInt(8);
            String image = rs.getString("path");
            boolean voteStatus = Boolean.parseBoolean(rs.getString(10));

            // System.out.println("noteID " + noteID + " : " + voteStatus
            // + "string: " + rs.getString(10));
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).setPrivate(priv)
                .setImage(image).setVote(vote).setVoteStatus(voteStatus)
                .build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }
  }

  public static int loginValid(String username, String password)
      throws SQLException {
    String query = "SELECT id from user WHERE LOWER(username) = LOWER(?) AND password = ? LIMIT 1";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.setString(1, username);
        prep.setString(2, password);
        try (ResultSet rs = prep.executeQuery()) {
          if (rs.next()) {
            return rs.getInt(1);
          } else {
            return -1;
          }
        }
      }
    }
  }

  public static int postNote(int userID, long time, double lat, double lng,
      String text, int privacy) throws SQLException {

    String post = "INSERT INTO notes VALUES (NULL, ?, ?, ?, ?, ?, ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post, AUTOKEYS)) {
        prep.setInt(1, userID);
        prep.setLong(2, time);
        prep.setDouble(3, lat);
        prep.setDouble(4, lng);
        prep.setString(5, text);
        prep.setInt(6, privacy);
        prep.executeUpdate();
        ResultSet rs = prep.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
      }
    }
  }

  public static int addImage(int noteid, String path) throws SQLException {
    String query = "INSERT INTO image_note VALUES (NULL, ?, ?);";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(query, AUTOKEYS)) {
        prep.setInt(1, noteid);
        prep.setString(2, path);
        prep.executeUpdate();

        /* Get autoincrement key */
        ResultSet rs = prep.getGeneratedKeys();
        rs.next();
        int imageID = rs.getInt(1);
        String imagepath = path + imageID + ".jpg";
        setImagePath(imageID, imagepath);
        return imageID;
      }
    }
  }

  public static void setImagePath(int imageid, String path) throws SQLException {
    String query = "UPDATE image_note SET path=? WHERE id=?";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.setString(1, path);
        prep.setInt(2, imageid);
        prep.executeUpdate();
      }
    }
  }

  public static int addUser(String username, String password, String firstname,
      String lastname, String email, int phone) throws SQLException {
    String post = "INSERT INTO user VALUES (NULL, ?, ?, ?, ?, ?, ?);";
    try (Connection conn = Db.getConnection()) {

      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, firstname);
        prep.setString(4, lastname);
        prep.setString(5, email);
        prep.setInt(6, phone);
        prep.executeUpdate();
      }
    }
    return getUserID(username);
  }

  public static int getUserID(String username) throws SQLException {
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn
          .prepareStatement("SELECT id FROM user WHERE LOWER(username)= LOWER(?) LIMIT 1;")) {
        prep.setString(1, username);
        try (ResultSet rs = prep.executeQuery()) {
          int uID = -1;
          if (rs.next()) {
            uID = rs.getInt(1);
          }
          return uID;
        }
      }
    }
  }

  public static void removeNote(int noteId, int userId) throws SQLException {
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn
          .prepareStatement("DELETE FROM notes WHERE id=? AND userid=?;")) {
        prep.setInt(1, noteId);
        prep.setInt(2, userId);
        prep.executeUpdate();
      }
      try (PreparedStatement prep = conn
          .prepareStatement("DELETE FROM image_note WHERE noteid=?;")) {
        prep.setInt(1, noteId);
        prep.executeUpdate();
      }
    }
  }

  public static void upvote(int noteID, int userID) throws SQLException {
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn
          .prepareStatement("INSERT INTO vote VALUES (?,?);")) {
        prep.setInt(1, noteID);
        prep.setInt(2, userID);
        prep.executeUpdate();
      }
    }
  }

  public static void removeUpvote(int noteID, int userID) throws SQLException {
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn
          .prepareStatement("DELETE FROM vote WHERE note_id=? AND user_id=?;")) {
        prep.setInt(1, noteID);
        prep.setInt(2, userID);
        prep.executeUpdate();
      }
    }
  }

  // public static void isUpvoted(List<Note> notes, int userID) throws
  // SQLException {
  // try (Connection conn = Db.getConnection()) {
  // try (PreparedStatement prep = conn
  // .prepareStatement("SELECT * FROM vote WHERE note_id=? user_id=?;")) {
  //
  // prep.setInt(1, userID);
  // prep.setInt(1, userID);
  // prep.executeUpdate();
  // }
  // }
  // }

  public static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

}
