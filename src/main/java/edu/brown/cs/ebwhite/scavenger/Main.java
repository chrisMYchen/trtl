package edu.brown.cs.ebwhite.scavenger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import edu.brown.cs.ebwhite.database.Db;
import edu.brown.cs.ebwhite.database.TurtleQuery;
import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;
import edu.brown.cs.ebwhite.note.NoteRanker;

public class Main {
  private Integer port;

  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;

    try {
      Db.database("turtlDB.sqlite3");
    } catch (ClassNotFoundException e1) {
      System.out.println("ERROR: could not connect to DB");
      e1.printStackTrace();
    }

    try {

      NoteRanker noteRank = new NoteRanker();
      noteRank.setCurrentUser(3);

      List<Note> notes = new ArrayList<>();
      notes = TurtleQuery.getNotes(3, new LatLong(71.0005, -41.0005), 50, 0,
          100, 10101011);
      List<Note> notes2 = TurtleQuery.getNotes(-1, new LatLong(71.0001,
          -41.0001), 500, 0, 100, 10101011);
      notes.sort(noteRank);
      for (Note n : notes) {
        System.out.println(String.format("note id %d is, %s", n.getId(),
            n.getText()));
      }

    } catch (SQLException e) {
      System.out.println("Error posting note.");
      e.printStackTrace();
    }
  }

  private void run() {
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class);
    OptionSpec<String> stringSpec = parser.nonOptions().ofType(String.class);
    OptionSet options = parser.parse(args);

    /* List<String> input = options.valuesOf(stringSpec); */

    if (options.has("port")) {
      port = (Integer) options.valueOf("port");
    } else {
      port = 2456;
    }

    if (options.has("gui")) {
      /*
       * if(input.size() != 1){
       * System.out.println("ERROR: Wrong number of arguments"); System.exit(1);
       * }
       */
      /* Start spark, etc. */
      SparkServer s = new SparkServer(port);
      s.run();
    } else {
      return;
    }
  }

}
