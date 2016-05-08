package edu.brown.cs.ebwhite.note;

import java.util.Comparator;

import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

public class NoteRanker implements Comparator<Note> {
  private User currentUser;
  private LatLong myLocation;
  public NoteRanker() {

  }

  public void setCurrentUser(int uID) {
    currentUser = new UserProxy(uID);
  }

  public void setCurrentLocation(LatLong ll) {
    myLocation = ll;
  }

  @Override
  public int compare(Note o1, Note o2) {
    // TODO Auto-generated method stub
    // what does eli send us? this is in seconds
    o1.getLatLong();
    double currentTime = System.currentTimeMillis();
    double timeSinceNote1 = currentTime - o1.getTimestamp();
    double timeSinceNote2 = currentTime - o2.getTimestamp();
    // System.out.println("current" + currentTime);
    // System.out.println("time since " + timeSinceNote1);
    if (currentUser != null) {
      // if (currentUser.getFollowers().contains(o1.getUser().getId())) {
      // // System.out.println("note 1 friend " + timeSinceNote1);
      // timeSinceNote1 = timeSinceNote1 / 2;
      // // System.out.println("note 1 post log " + timeSinceNote1);
      // }
      // if (currentUser.getFollowers().contains(o2.getUser().getId())) {
      // // System.out.println("note 2 friend " + timeSinceNote2);
      // timeSinceNote2 = timeSinceNote2 / 2;
      // // System.out.println("note 2 post log " + timeSinceNote2);
      // }
      // if (o1.getPrivacy() == 1) {
      // timeSinceNote1 = timeSinceNote1 / 2;
      // }
      // if (o2.getPrivacy() == 1) {
      // timeSinceNote2 = timeSinceNote2 / 2;
      // }
      // timeSinceNote1 *= LatLong.distanceLatLong(myLocation,
      // o1.getLatLong()) * 1000;
      // timeSinceNote2 *= LatLong.distanceLatLong(myLocation,
      // o2.getLatLong()) * 1000;
      // // System.out.println("new val: " + timeSinceNote1);
      // System.out.println(LatLong.distanceLatLong(myLocation,
      // o1.getLatLong()));
    }
    double score1 = (o1.getVote()) / 2;
    double score2 = (o2.getVote()) / 2;
    // one vote = 2 hours
    double value1 = timeSinceNote1 / 25000000 - score1;
    double value2 = timeSinceNote2 / 25000000 - score2;
    // System.out.println("note " + o1.getId() + ": " + timeSinceNote1 +
    // "note 2 "
    // + o2.getId() + ": "
    // + timeSinceNote2);
    return Double.compare(value1, value2);
  }

}
