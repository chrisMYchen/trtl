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
  private String image;
  private String username;
  private int vote;
  private boolean voteStatus;

  public Note(long time, String content, int ID, int uID, double lat,
      double lng, int priv, int voteNum, boolean vStatus) {
    this.timestamp = time;
    this.text = content;
    this.id = ID;
    this.user = new UserProxy(uID);
    this.username = user.getUsername();
    this.latlng = new LatLong(lat, lng);
    this.privacy = priv;
    // this.displayName = ownerName;
    this.image = null;
    this.vote = voteNum;
    this.voteStatus = vStatus;
  }

  public Note(NoteBuilder b) {
    this.timestamp = b.timestamp;
    this.id = b.id;
    this.text = b.text;
    this.user = b.user;
    this.username = b.user.getUsername();
    // this.userid = b.uid;
    this.latlng = new LatLong(b.lat, b.lng);
    // this.displayName = b.displayName;
    this.privacy = b.privacy;
    this.image = b.image;
    this.vote = b.vote;
    this.voteStatus = b.voteStatus;
  }

  @Override
  public String toString() {
    return "Note [timestamp=" + timestamp + ", text=" + text + ", id=" + id
        + ", latlng=" + latlng + ", user=" + user + ", privacy=" + privacy
        + ", vote=" + vote + ", voteStatus= " + voteStatus + "]";
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

  public int getPrivacy() {
    return privacy;
  }

  public int getVote() {
    return vote;
  }

  // public int getUserId() {
  // return userid;
  // }

  public LatLong getLatLong() {
    return latlng;
  }

  public String getImage() {
    return image;
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
    private String image;
    private int vote;
    private boolean voteStatus;

    public NoteBuilder(int myId, long time) {
      this.timestamp = time;
      this.id = myId;
    }

    public NoteBuilder setContent(String content) {
      this.text = content;
      return this;
    }

    public NoteBuilder setImage(String imagePath) {
      this.image = imagePath;
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

    public NoteBuilder setVote(int voteNum) {
      this.vote = voteNum;
      return this;
    }

    public NoteBuilder setVoteStatus(boolean vStatus) {
      this.voteStatus = vStatus;
      return this;
    }

    public Note build() {
      return new Note(this);
    }
  }
}
