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

    int currentTime = (int) (new Date().getTime() / 1000);
    double timeSinceNote1 = currentTime - o1.getTimestamp();
    double timeSinceNote2 = currentTime - o2.getTimestamp();
    if (currentUser != null) {
      if (currentUser.getFriends().contains(o1.getUser().getId())) {
        timeSinceNote1 = Math.log(timeSinceNote1);
      }
      if (currentUser.getFriends().contains(o2.getUser().getId())) {
        timeSinceNote2 = Math.log(timeSinceNote2);
      }
    }
    return Double.compare(timeSinceNote1, timeSinceNote2);
  }
}
