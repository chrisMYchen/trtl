package edu.brown.cs.ebwhite.note;

import java.util.Comparator;
import java.util.Date;

import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

public class NoteRanker implements Comparator<Note> {
  private User currentUser;

  public NoteRanker() {

  }

  public void setCurrentUser(int uID) {
    currentUser = new UserProxy(uID);
  }

  @Override
  public int compare(Note o1, Note o2) {
    // TODO Auto-generated method stub
    // what does eli send us? this is in seconds

    double currentTime = new Date().getTime();
    double timeSinceNote1 = currentTime - o1.getTimestamp();
    double timeSinceNote2 = currentTime - o2.getTimestamp();
    if (currentUser != null) {
      if (currentUser.getFollowers().contains(o1.getUser().getId())) {
        // System.out.println("note 1 friend " + timeSinceNote1);
        timeSinceNote1 = Math.log(timeSinceNote1);
        // System.out.println("note 1 post log " + timeSinceNote1);
      }
      if (currentUser.getFollowers().contains(o2.getUser().getId())) {
        // System.out.println("note 2 friend " + timeSinceNote2);
        timeSinceNote2 = Math.log(timeSinceNote2);
        // System.out.println("note 2 post log " + timeSinceNote2);
      }
      if (o1.getPrivacy() == 1) {
        timeSinceNote1 = Math.log(timeSinceNote1);
      }
      if (o2.getPrivacy() == 1) {
        timeSinceNote2 = Math.log(timeSinceNote2);
      }

    }
    // System.out.println("note " + o1.getId() + ": " + timeSinceNote1 +
    // "note 2 "
    // + o2.getId() + ": "
    // + timeSinceNote2);
    return Double.compare(timeSinceNote1, timeSinceNote2);
  }
}
