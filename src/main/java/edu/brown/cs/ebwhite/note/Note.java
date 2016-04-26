package edu.brown.cs.ebwhite.note;

import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

public class Note {
  private long timestamp;
  private String text;
  private int id;
  // private int userid;
  private LatLong latlng;
  // private String displayName;
  private User user;
  private int privacy;

  public Note(long time, String content, int ID, int uID, double lat,
      double lng, int priv) {
    this.timestamp = time;
    this.text = content;
    this.id = ID;
    this.user = new UserProxy(uID);
    this.latlng = new LatLong(lat, lng);
    this.privacy = priv;
    // this.displayName = ownerName;
  }

  public Note(NoteBuilder b){
    this.timestamp = b.timestamp;
    this.id = b.id;
    this.text = b.text;
    this.user = b.user;
    // this.userid = b.uid;
    this.latlng = new LatLong(b.lat, b.lng);
    // this.displayName = b.displayName;
    this.privacy = b.privacy;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  // public int getUserId() {
  // return userid;
  // }

  public LatLong getLatLong() {
    return latlng;
  }

  // public String getUsername() {
  // return displayName;
  // }

  public static class NoteBuilder {
    private long timestamp;
    private String text;
    private int id;
    private User user;
    // private int uid;
    private double lat;
    private double lng;
    // private String displayName;
    private int privacy;

    public NoteBuilder(int myId, long time){
      this.timestamp = time;
      this.id = myId;
    }

    public NoteBuilder setContent(String content){
      this.text = content;
      return this;
    }

    //
    // public NoteBuilder setUser(int uId) {
    // this.uid = uId;
    // return this;
    // }

    public NoteBuilder setLat(double myLat) {
      this.lat = myLat;
      return this;
    }

    public NoteBuilder setLng(double myLng) {
      this.lng = myLng;
      return this;
    }

    // public NoteBuilder setName(String name){
    // this.displayName = name;
    // return this;
    // }

    public NoteBuilder setUser(int uId) {
      this.user = new UserProxy(uId);
      return this;
    }

    public NoteBuilder setPrivate(int priv) {
      this.privacy = priv;
      return this;
    }

    public Note build(){
      return new Note(this);
    }
  }
}
