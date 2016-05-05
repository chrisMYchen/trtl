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
  // private final static int UIDCOL = 2;
  private final static int TIMECOL = 3;
  private final static int LATCOL = 4;
  private final static int LNGCOL = 5;
  private final static int TXTCOL = 10;
  private final static int AUTOKEYS = Statement.RETURN_GENERATED_KEYS;

  public static List<Note> getNotes(int userID, LatLong loc,
      double radius, int minPost, int maxPost, long timeStamp)
      throws SQLException {
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
      return getNotesLoggedIn(userID, bottom_lat, top_lat, left_lng,
          right_lng, minPost, maxPost, timeStamp);
    } else {
      return getNotesAnonymous(bottom_lat, top_lat, left_lng,
          right_lng, minPost, maxPost, timeStamp);
    }
  }

  public static List<Note> getNotesAnonymous(double bottom_lat,
      double top_lat, double left_lng, double right_lng, int minPost,
      int maxPost, long timeStamp) throws SQLException {

    String getNotes = "SELECT * FROM notes WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?)"
        + " AND (timestamp < ?) AND (private = 0) ORDER BY timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, timeStamp);
        prep.setInt(6, maxPost - minPost);
        prep.setInt(7, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            // int uID = rs.getInt(UIDCOL);
            long time = rs.getLong(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            // uID just not placed in note if not logged in
            Note n = new Note.NoteBuilder(noteID, time).setContent(
                txt).setLat(lat).setLng(lng).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }

  }

  public static List<Note> getNotesLoggedIn(int userID,
      double bottom_lat, double top_lat, double left_lng,
      double right_lng, int minPost, int maxPost, long timeStamp)
      throws SQLException {

    String getNotes = "SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat,"
        + " n.long, n.text, n.private FROM notes as n, user_follower as uf"
        + " WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?)"
        + " AND (timestamp < ?) AND (n.private = 0 OR n.userid = ? OR"
        + " (n.private = 1 AND uf.follower_id = ? AND uf.userid = n.userid)) "
        + " ORDER BY n.timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, timeStamp);
        prep.setInt(6, userID);
        prep.setInt(7, userID);
        prep.setInt(8, maxPost - minPost);
        prep.setInt(9, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(1);
            int uID = rs.getInt(2);
            long time = rs.getLong(3);
            double lat = rs.getDouble(4);
            double lng = rs.getDouble(5);
            String txt = rs.getString(6);
            int priv = rs.getInt(7);
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).setPrivate(
                    priv).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }
  }

  public static List<Note> updateNotes(int userID, LatLong loc,
      double radius, int minPost, int maxPost, long timeStamp)
      throws SQLException {
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
      return updateNotesLoggedIn(userID, bottom_lat, top_lat,
          left_lng, right_lng, minPost, maxPost, timeStamp);
    } else {
      return updateNotesAnonymous(bottom_lat, top_lat, left_lng,
          right_lng, minPost, maxPost, timeStamp);
    }
  }

  public static List<Note> updateNotesAnonymous(double bottom_lat,
      double top_lat, double left_lng, double right_lng, int minPost,
      int maxPost, long timeStamp) throws SQLException {
    String getNotes = "SELECT * FROM notes WHERE (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?)"
        + " AND (timestamp >= ?) AND (private = 0) ORDER BY timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, timeStamp);
        prep.setInt(6, maxPost - minPost);
        prep.setInt(7, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            // int uID = rs.getInt(UIDCOL);
            long time = rs.getLong(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            // uID just not placed in note if not logged in
            Note n = new Note.NoteBuilder(noteID, time).setContent(
                txt).setLat(lat).setLng(lng).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }
  }

  public static List<Note> updateNotesLoggedIn(int userID,
      double bottom_lat, double top_lat, double left_lng,
      double right_lng, int minPost, int maxPost, long timeStamp)
      throws SQLException {
    String getNotes = "SELECT DISTINCT n.id, n.userid, n.timestamp, n.lat, n.long, n.text, n.private FROM notes as n, user_follower as uf WHERE "
        + " (long BETWEEN ? AND ?) AND (lat BETWEEN ? AND ?) AND (timestamp >= ?) AND "
        + " (private = 0 OR n.userid = ? OR (n.private = 1 AND uf.follower_id = ? AND uf.userid = n.userid)) "
        + " ORDER BY n.timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, left_lng);
        prep.setDouble(2, right_lng);
        prep.setDouble(3, bottom_lat);
        prep.setDouble(4, top_lat);
        prep.setLong(5, timeStamp);
        prep.setInt(6, userID);
        prep.setInt(7, userID);
        prep.setInt(8, maxPost - minPost);
        prep.setInt(9, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(1);
            int uID = rs.getInt(2);
            long time = rs.getLong(3);
            double lat = rs.getDouble(4);
            double lng = rs.getDouble(5);
            String txt = rs.getString(6);
            int priv = rs.getInt(7);
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).setPrivate(
                    priv).build();
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

  public static int postNote(int userID, long time, double lat,
      double lng, String text, int privacy)
      throws SQLException {
    double coslat = Math.cos(deg2rad(lat));
    double sinlat = Math.sin(deg2rad(lat));
    double coslng = Math.cos(deg2rad(lng));
    double sinlng = Math.sin(deg2rad(lng));

    String post = "INSERT INTO notes VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    // - UserID, TIMESTAMP, Lat, Lon, coslat, sinlat, coslng, sinlng,
    // Text
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post, AUTOKEYS)) {
        prep.setInt(1, userID);
        prep.setLong(2, time);
        prep.setDouble(3, lat);
        prep.setDouble(4, lng);
        prep.setDouble(5, coslat);
        prep.setDouble(6, sinlat);
        prep.setDouble(7, coslng);
        prep.setDouble(8, sinlng);
        prep.setString(9, text);
        prep.setInt(10, privacy);
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
        return rs.getInt(1);
      }
    }
  }

  public static void setImagePath(int imageid, String path) throws SQLException{
    String query = "UPDATE image_note SET path=? WHERE id=?";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.setString(1, path);
        prep.setInt(2, imageid);
        prep.executeUpdate();
      }
    }
  }


  public static int addUser(String username, String password,
      String firstname, String lastname, String email, int phone)
      throws SQLException {
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

  public static void removeNote(int nodeId) throws SQLException {
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn
          .prepareStatement("DELETE FROM notes WHERE id=?;")) {
        prep.setInt(1, nodeId);
        prep.executeUpdate();
      }
    }
  }

  public static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

}
