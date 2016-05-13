package edu.brown.cs.ebwhite.note;

import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

/**
 * Class that represents a Note object.
 * @author kzliu
 *
 */
public class Note {
  /**
   * Timestamp for the note.
   */
  private long timestamp;
  /**
   * Text content for the note.
   */
  private String text;
  /**
   * Note's id in the database.
   */
  private int id;
  /**
   * LatLong representing where the note was posted.
   */
  private LatLong latlng;
  /**
   * A user object representing the user who dropped the note.
   */
  private User user;
  /**
   * Integer representing privacy of the note, 1 if private, 0 otherwise.
   */
  private int privacy;
  /**
   * Path for the image attached to the note (if exists) in the file
   * system.
   */
  private String image;
  /**
   * Number of votes the note has.
   */
  private int vote;
  /**
   * Status true if the user viewing the note has voted for note, false
   * otherwise.
   */
  private boolean voteStatus;

  /**
   * Constructor for a Note.
   * @param time Timestamp for the note
   * @param content Text content for the note
   * @param ID Note's id
   * @param uID user id for the user who posted the note
   * @param lat latitude value for where the note was posted
   * @param lng longitude value for where the note was posted
   * @param priv privacy of the note (1 if private, 0 if public)
   * @param voteNum Number of votes the note has
   * @param vStatus boolean representing if the user viewing the note has
   * voted for it
   */
  public Note(long time, String content, int ID, int uID, double lat,
      double lng, int priv, int voteNum, boolean vStatus) {
    this.timestamp = time;
    this.text = content;
    this.id = ID;
    this.user = new UserProxy(uID);
    this.latlng = new LatLong(lat, lng);
    this.privacy = priv;
    this.image = null;
    this.vote = voteNum;
    this.voteStatus = vStatus;
  }

  /**
   * Creates a note out of a builder.
   * @param b NoteBuilder
   */
  public Note(NoteBuilder b) {
    this.timestamp = b.timestamp;
    this.id = b.id;
    this.text = b.text;
    this.user = b.user;
    this.latlng = new LatLong(b.lat, b.lng);
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

  /**
   * Getter for timestamp on note.
   * @return timestamp value of the note
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Getter for the content of the note.
   * @return text content of the note
   */
  public String getText() {
    return text;
  }

  /**
   * Setter for the note's text content.
   * @param text new text content for the note
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets the id of the note.
   * @return id of the note
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for the user object representing the user who posted the note.
   * @return a User object
   */
  public User getUser() {
    return user;
  }

  /**
   * Getter for the privacy of the note.
   * @return 1 is private, 0 otherwise
   */
  public int getPrivacy() {
    return privacy;
  }

  /**
   * Getter for the number of votes the note has.
   * @return number of votes the note has
   */
  public int getVote() {
    return vote;
  }

  /**
   * Getter for the LatLong value for where the note was posted.
   * @return a latlong value attached to the note
   */
  public LatLong getLatLong() {
    return latlng;
  }

  /**
   * Getter for the path of the note's attached image in the file system.
   * @return path of the image location in the file system
   */
  public String getImage() {
    return image;
  }

  /**
   * Note builder for a note.
   * @author TRTL Team
   *
   */
  public static class NoteBuilder {
    /**
     * Timestamp for the note.
     */
    private long timestamp;
    /**
     * Text content for the note.
     */
    private String text;
    /**
     * Note's id in the database.
     */
    private int id;
    /**
     * A user object representing the user who dropped the note.
     */
    private User user;
    /**
     * Double representing the latitude value for the note.
     */
    private double lat;
    /**
     * Double representing the longitude value for the note.
     */
    private double lng;
    /**
     * Integer representing privacy of the note, 1 if private, 0 otherwise.
     */
    private int privacy;
    /**
     * Path for the image attached to the note (if exists) in the file
     * system.
     */
    private String image;
    /**
     * Number of votes the note has.
     */
    private int vote;
    /**
     * Status true if the user viewing the note has voted for note, false
     * otherwise.
     */
    private boolean voteStatus;

    /**
     * Note builder for a note.
     * @param myId id of the note
     * @param time timestamp on the note
     */
    public NoteBuilder(int myId, long time) {
      this.timestamp = time;
      this.id = myId;
    }

    /**
     * Setter for the note's text content.
     * @param content new text content for the note
     * @return Note builder
     */
    public NoteBuilder setContent(String content) {
      this.text = content;
      return this;
    }

    /**
     * Setter for the image path in the file system.
     * @param imagePath String representing the image path in the file
     * system
     * @return Noter builder
     */
    public NoteBuilder setImage(String imagePath) {
      this.image = imagePath;
      return this;
    }

    /**
     * Setter for a latitude value.
     * @param myLat the latitude value for the note
     * @return Note builder
     */
    public NoteBuilder setLat(double myLat) {
      this.lat = myLat;
      return this;
    }

    /**
     * Setting for the longitude value.
     * @param myLng the longitude value for the note
     * @return Note builder
     */
    public NoteBuilder setLng(double myLng) {
      this.lng = myLng;
      return this;
    }

    /**
     * Setting the user object for the note.
     * @param uId the user id of the author of the note
     * @return Note builder
     */
    public NoteBuilder setUser(int uId) {
      this.user = new UserProxy(uId);
      return this;
    }

    /**
     * Setting the private value of the note.
     * @param priv privacy value for the note (1 is private, 0 otherwise).
     * @return Note builder
     */
    public NoteBuilder setPrivate(int priv) {
      this.privacy = priv;
      return this;
    }

    /**
     * Set the number of votes the note has.
     * @param voteNum Number if votes the note has
     * @return Note builder
     */
    public NoteBuilder setVote(int voteNum) {
      this.vote = voteNum;
      return this;
    }

    /**
     * Set the vote status for the user viewing the note
     * @param vStatus status, true if user viewing note has voted for it, 0
     * otherwise
     * @return Note builder
     */
    public NoteBuilder setVoteStatus(boolean vStatus) {
      this.voteStatus = vStatus;
      return this;
    }

    /**
     * Build method to make a note out of the note builder.
     * @return a Note object
     */
    public Note build() {
      return new Note(this);
    }
  }
}
