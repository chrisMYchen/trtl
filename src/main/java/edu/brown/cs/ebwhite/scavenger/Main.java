package edu.brown.cs.ebwhite.scavenger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import note.Note;
import database.TurtleQuery;
import edu.brown.cs.ebwhite.database.Db;
import geo.LatLong;

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
//      TurtleQuery.postNote(1, 10101010, 71, -41, "This is the first note we ever posted");
    	/* Testing posting notes and time sorting
      TurtleQuery.postNote(2, 101010, 71, -41, "This is the third note.");
      List<Note> notes = new ArrayList<>();

      notes = TurtleQuery.getNotes(new LatLong(71, -41), 500, 0, 100, 10101011);
      for (Note n: notes){
        System.out.println(String.format("user %d says, %s", n.getId(), n.getText()));
      }
      notes = TurtleQuery.getNotes(new LatLong(71, -41), 500, 4, 12, 10101010);
      if (notes.isEmpty()) {
        System.out.println("No notes from this timestamp.");
      }
      */
    	int uID = TurtleQuery.addUser("chrisMYchen", "password", "Chris" , null , "christopher.s.chen@gmail.com", -1);
    	System.out.println(uID);
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

    if(options.has("port")){
      port = (Integer) options.valueOf("port");
    }
    else{
      port = 2456;
    }

    if(options.has("gui")){
/*      if(input.size() != 1){
        System.out.println("ERROR: Wrong number of arguments");
        System.exit(1);
      } */
      /* Start spark, etc. */
      SparkServer s = new SparkServer(port);
      s.run();
    }
    else{
      return;
    }
  }

}
