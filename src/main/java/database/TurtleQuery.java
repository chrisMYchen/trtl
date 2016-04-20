package database;

import edu.brown.cs.ebwhite.database.Db;
import geo.LatLong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import note.Note;

public class TurtleQuery {
  // how to do radius distance with lat /lon
  // calc this out later
  public static List<Note> getNotesAnonymous(LatLong loc, double radius, int minPost, int maxPost, int timeStamp) throws SQLException {
    double cos_allowed_distance = Math.cos(radius / 6.371); // this is in
                                                            // meters
    double inputLat = loc.getLat();
    double inputLng = loc.getLng();
    double CUR_sin_lat = Math.sin(deg2rad(inputLat));
    double CUR_cos_lat = Math.cos(deg2rad(inputLat));
    double CUR_sin_lng = Math.sin(deg2rad(inputLng));
    double CUR_cos_lng = Math.cos(deg2rad(inputLng));
    final int NIDCOL = 1;
    final int UIDCOL = 2;
    final int TIMECOL = 3;
    final int LATCOL = 4;
    final int LNGCOL = 5;
    final int TXTCOL = 10;

    String getNotes = "SELECT * FROM notes WHERE (? * sinlat + ? * coslat * (coslong* ? + sinlong * ?) > ?) AND (timestamp < ?) AND (private = 0) ORDER BY timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, CUR_sin_lat);
        prep.setDouble(2, CUR_cos_lat);
        prep.setDouble(3, CUR_cos_lng);
        prep.setDouble(4, CUR_sin_lng);
        prep.setDouble(5, cos_allowed_distance);
        prep.setInt(6, timeStamp);
        prep.setInt(7, maxPost - minPost);
        prep.setInt(8, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            int uID = rs.getInt(UIDCOL);
            int time = rs.getInt(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }

  }
  
  public static List<Note> getNotesLoggedIn(int userID, LatLong loc, double radius, int minPost, int maxPost, int timeStamp) throws SQLException {
    double cos_allowed_distance = Math.cos(radius / 6.371); // this is in
                                                            // meters
    double inputLat = loc.getLat();
    double inputLng = loc.getLng();
    double CUR_sin_lat = Math.sin(deg2rad(inputLat));
    double CUR_cos_lat = Math.cos(deg2rad(inputLat));
    double CUR_sin_lng = Math.sin(deg2rad(inputLng));
    double CUR_cos_lng = Math.cos(deg2rad(inputLng));
    final int NIDCOL = 1;
    final int UIDCOL = 2;
    final int TIMECOL = 3;
    final int LATCOL = 4;
    final int LNGCOL = 5;
    final int TXTCOL = 10;

    String getNotes = "SELECT * FROM notes WHERE (? * sinlat + ? * coslat * (coslong* ? + sinlong * ?) > ?) AND (timestamp < ?) ORDER BY timestamp DESC LIMIT ? OFFSET ? ;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, CUR_sin_lat);
        prep.setDouble(2, CUR_cos_lat);
        prep.setDouble(3, CUR_cos_lng);
        prep.setDouble(4, CUR_sin_lng);
        prep.setDouble(5, cos_allowed_distance);
        prep.setInt(6, timeStamp);
        prep.setInt(7, maxPost - minPost);
        prep.setInt(8, minPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            int uID = rs.getInt(UIDCOL);
            int time = rs.getInt(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }

  }

  public static List<Note> updateNotes(LatLong loc, double radius, int minPost, int maxPost, int timeStamp) throws SQLException {
    double cos_allowed_distance = Math.cos(radius / 6.371); // this is in
                                                            // meters
    double inputLat = loc.getLat();
    double inputLng = loc.getLng();
    double CUR_sin_lat = Math.sin(deg2rad(inputLat));
    double CUR_cos_lat = Math.cos(deg2rad(inputLat));
    double CUR_sin_lng = Math.sin(deg2rad(inputLng));
    double CUR_cos_lng = Math.cos(deg2rad(inputLng));
    final int NIDCOL = 1;
    final int UIDCOL = 2;
    final int TIMECOL = 3;
    final int LATCOL = 4;
    final int LNGCOL = 5;
    final int TXTCOL = 10;

    String getNotes = "SELECT * FROM notes WHERE ? * sinlat + ? * coslat * (coslong* ? + sinlong * ?) > ? AND timestamp >= ? limit (? , ?) ORDER BY timestamp ASC;";
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(getNotes)) {
        prep.setDouble(1, CUR_sin_lat);
        prep.setDouble(2, CUR_cos_lat);
        prep.setDouble(3, CUR_cos_lng);
        prep.setDouble(4, CUR_sin_lng);
        prep.setDouble(5, cos_allowed_distance);
        prep.setInt(6, timeStamp);
        prep.setInt(7, minPost);
        prep.setInt(8, maxPost);
        try (ResultSet rs = prep.executeQuery()) {
          List<Note> allNotes = new ArrayList<>();
          while (rs.next()) {
            int noteID = rs.getInt(NIDCOL);
            int uID = rs.getInt(UIDCOL);
            int time = rs.getInt(TIMECOL);
            double lat = rs.getDouble(LATCOL);
            double lng = rs.getDouble(LNGCOL);
            String txt = rs.getString(TXTCOL);
            Note n = new Note.NoteBuilder(noteID, time).setUser(uID)
                .setContent(txt).setLat(lat).setLng(lng).build();
            allNotes.add(n);
          }
          return allNotes;
        }
      }
    }

  }
  public static void postNote(int userID, int time, double lat, double lng, String text, int privacy)
      throws SQLException {
    // ask rohan or look up autoincrement and how it works with.

    // - CHECK TO MAKE SURE AUTOINCREMENT WORKS PROPERLY
    // String userID;
    // int time;
    // double lat;
    // double lng;
    // String text;
 
    double coslat = Math.cos(deg2rad(lat));
    double sinlat = Math.sin(deg2rad(lat));
    double coslng = Math.cos(deg2rad(lng));
    double sinlng = Math.sin(deg2rad(lng));

    String post = "INSERT INTO notes VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // - UserID, TIMESTAMP, Lat, Lon, coslat, sinlat, coslng, sinlng, Text
    try (Connection conn = Db.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(post)) {
        prep.setInt(1, userID);
        prep.setInt(2, time);
        prep.setDouble(3, lat);
        prep.setDouble(4, lng);
        prep.setDouble(5, coslat);
        prep.setDouble(6, sinlat);
        prep.setDouble(7, coslng);
        prep.setDouble(8, sinlng);
        prep.setString(9, text);
        prep.setInt(10, privacy);
        prep.executeUpdate();
      }
    }
  }
  
  public static int addUser(String username, String password, String firstname, String lastname, String email, int phone) throws SQLException {
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
	  try (PreparedStatement prep = conn.prepareStatement("SELECT id FROM user WHERE username=?;")) {
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
  
  public static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

}
