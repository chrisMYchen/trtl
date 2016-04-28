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

  public static void main(String[] args) {
    Integer port;
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class);
    parser.accepts("database").withRequiredArg().ofType(String.class);
    OptionSpec<String> secure = parser.accepts("keystore").withRequiredArg().ofType(String.class);
    parser.accepts("keypass").requiredIf(secure).withRequiredArg().ofType(String.class);
    OptionSpec<String> stringSpec = parser.nonOptions().ofType(String.class);
    OptionSet options = parser.parse(args);

    if (options.has("port")) {
      port = (Integer) options.valueOf("port");
    } else {
      port = 2456;
    }

    if(options.has("database")){
      String path = (String) options.valueOf("database");
      try {
        Db.database(path);
      } catch (ClassNotFoundException e1) {
        System.out.println("ERROR: could not connect to DB: " + path);
        e1.printStackTrace();
      }
    }
    else{
      try {
        Db.database("turtlDB.sqlite3");
      } catch (ClassNotFoundException e1) {
        System.out.println("ERROR: could not connect to DB turtlDB.sqlite3");
        e1.printStackTrace();
      }
    }

    if (options.has("gui")) {
      if(options.has(secure)){
        String keystore = (String) options.valueOf(secure);
        String keypass = (String) options.valueOf("keypass");
        SparkServer s = new SparkServer(port, keystore, keypass);
      }
      /* Start spark, etc. */
      SparkServer s = new SparkServer(port);
      s.run();
    } else {
      return;
    }
  }
}
