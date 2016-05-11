package edu.brown.cs.ebwhite.scavenger;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import edu.brown.cs.ebwhite.database.Db;

/**
 * The Main class handles the running of TRTL. We use the data
 * specified by flags to change how TRTL behaves. We can
 * specify port, database, gui, keystore, and keypass.
 * @author cchen5
 *
 */
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

    if (options.has("database")) {
      String path = (String) options.valueOf("database");
      try {
        Db.database(path);
      } catch (ClassNotFoundException e1) {
        System.out.println("ERROR: could not connect to DB: " + path);
        e1.printStackTrace();
      }
    } else {
      try {
        Db.database("turtlDB.sqlite3");
      } catch (ClassNotFoundException e1) {
        System.out.println("ERROR: could not connect to DB turtlDB.sqlite3");
        e1.printStackTrace();
      }
    }

    if (options.has("gui")) {
      if(options.has(secure)){
        String keystore = options.valueOf(secure);
        String keypass = (String) options.valueOf("keypass");
        SparkServer s = new SparkServer(port, keystore, keypass);
        s.run();
      } else {
        SparkServer s = new SparkServer(port);
        s.run();
      }

    } else {
      return;
    }
  }
}
